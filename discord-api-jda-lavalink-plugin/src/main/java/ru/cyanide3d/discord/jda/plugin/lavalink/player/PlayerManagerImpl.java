package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.PlayerUpdateBuilder;
import dev.arbjerg.lavalink.client.player.Track;
import dev.arbjerg.lavalink.client.player.TrackUpdateBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ru.cyanide3d.discord.jda.plugin.lavalink.ReactorUtils.awaitVoid;

@Slf4j
public class PlayerManagerImpl implements PlayerManager {

    @Autowired
    private LavalinkClient lavalinkClient;

    @Autowired
    private GuildPlayerRegistry guildPlayerRegistry;

    @Autowired
    private TrackResolver trackResolver;

    private final ConcurrentHashMap<Long, ReentrantLock> guildLocks = new ConcurrentHashMap<>();

    @Override
    public PlayerPlayResult play(long guildId, TrackIdentifier identifier) {
        return withGuildLock(guildId, () -> doPlay(guildId, identifier));
    }

    @Override
    public PlayerActionResult pause(long guildId) {
        return withGuildLock(guildId, () -> doPause(guildId));
    }

    @Override
    public PlayerActionResult resume(long guildId) {
        return withGuildLock(guildId, () -> doResume(guildId));
    }

    @Override
    public PlayerActionResult stop(long guildId) {
        return withGuildLock(guildId, () -> doStop(guildId));
    }

    @Override
    public PlayerActionResult skip(long guildId) {
        return withGuildLock(guildId, () -> doSkip(guildId));
    }

    @Override
    public PlayerActionResult seek(long guildId, long positionMs) {
        return withGuildLock(guildId, () -> doSeek(guildId, positionMs));
    }

    @Override
    public PlayerActionResult setVolume(long guildId, int volume) {
        return withGuildLock(guildId, () -> doSetVolume(guildId, volume));
    }

    @Override
    public PlayerActionResult clearQueue(long guildId) {
        return withGuildLock(guildId, () -> doClearQueue(guildId));
    }

    @Override
    public PlayerActionResult playNextIfAvailable(long guildId) {
        return withGuildLock(guildId, () -> doPlayNextIfAvailable(guildId));
    }

    @Override
    public PlayerActionResult forget(long guildId) {
        return withGuildLock(guildId, () -> doForget(guildId));
    }

    @Override
    public Optional<PlayerQueueSnapshot> getQueueSnapshot(long guildId) {
        return withGuildLock(guildId, () -> {
            GuildPlayerState state = guildPlayerRegistry.get(guildId);
            if (state == null) {
                return Optional.empty();
            }

            return Optional.of(new PlayerQueueSnapshot(state.getCurrentTrack(), state.queueSnapshot(), state.isPaused(), state.getVolume()));
        });
    }

    protected PlayerPlayResult doPlay(long guildId, TrackIdentifier identifier) {
        final TrackLoadResult loadResult;
        try {
            loadResult = trackResolver.resolve(identifier, guildId);
        } catch (Exception e) {
            log.warn("Failed to resolve track for guildId={}, source={}", guildId, identifier.sourceName(), e);
            return PlayerPlayResult.failed("LOAD_FAILED");
        }

        if (loadResult.isEmpty()) {
            return PlayerPlayResult.notFound();
        }

        GuildPlayerState state = guildPlayerRegistry.getOrCreate(guildId);
        List<Track> tracks = loadResult.getTracks();

        if (loadResult.isPlaylist()) {
            return playPlaylist(guildId, state, tracks);
        }

        Track first = tracks.get(0);
        if (!state.hasCurrentTrack()) {
            startTrackNow(guildId, state, first);
            return PlayerPlayResult.started(first);
        }

        state.enqueue(first);
        return PlayerPlayResult.enqueued(first, state.queueSize());
    }

    protected PlayerActionResult doPause(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || !state.hasCurrentTrack()) {
            return PlayerActionResult.noop(PlayerActionType.PAUSED, "NO_ACTIVE_TRACK");
        }

        if (state.isPaused()) {
            return PlayerActionResult.noop(PlayerActionType.PAUSED, "ALREADY_PAUSED");
        }

        updatePlayerSafely(guildId, player -> player.setPaused(true), "pause");
        state.setPaused(true);

        return PlayerActionResult.success(PlayerActionType.PAUSED);
    }

    protected PlayerActionResult doResume(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || !state.hasCurrentTrack()) {
            return PlayerActionResult.noop(PlayerActionType.RESUMED, "NO_ACTIVE_TRACK");
        }

        if (!state.isPaused()) {
            return PlayerActionResult.noop(PlayerActionType.RESUMED, "ALREADY_RESUMED");
        }

        updatePlayerSafely(guildId, player -> player.setPaused(false), "resume");
        state.setPaused(false);

        return PlayerActionResult.success(PlayerActionType.RESUMED);
    }

    protected PlayerActionResult doStop(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || (!state.hasCurrentTrack() && state.isQueueEmpty())) {
            return PlayerActionResult.noop(PlayerActionType.STOPPED, "ALREADY_STOPPED");
        }

        stopRemoteTrack(guildId);
        resetLocalState(state);
        guildPlayerRegistry.remove(guildId);

        return PlayerActionResult.success(PlayerActionType.STOPPED);
    }

    protected PlayerActionResult doSkip(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || (!state.hasCurrentTrack() && state.isQueueEmpty())) {
            return PlayerActionResult.noop(PlayerActionType.SKIPPED, "NO_ACTIVE_TRACK");
        }

        Track next = state.pollNext();

        if (next == null) {
            stopRemoteTrack(guildId);
            resetLocalState(state);
            guildPlayerRegistry.remove(guildId);

            return PlayerActionResult.success(PlayerActionType.SKIPPED);
        }

        startTrackNow(guildId, state, next);
        return PlayerActionResult.success(PlayerActionType.SKIPPED);
    }

    protected PlayerActionResult doSeek(long guildId, long positionMs) {
        if (positionMs < 0) {
            return PlayerActionResult.failure(PlayerActionType.SEEKED, "INVALID_POSITION");
        }

        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || !state.hasCurrentTrack()) {
            return PlayerActionResult.noop(PlayerActionType.SEEKED, "NO_ACTIVE_TRACK");
        }

        updatePlayerSafely(guildId, player -> player.setPosition(positionMs), "seek");
        return PlayerActionResult.success(PlayerActionType.SEEKED);
    }

    protected PlayerActionResult doSetVolume(long guildId, int volume) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || !state.hasCurrentTrack()) {
            return PlayerActionResult.noop(PlayerActionType.VOLUME_CHANGED, "NO_ACTIVE_TRACK");
        }

        int normalized = Math.max(0, Math.min(1000, volume));

        if (state.getVolume() == normalized) {
            return PlayerActionResult.noop(PlayerActionType.VOLUME_CHANGED, "VOLUME_UNCHANGED");
        }

        updatePlayerSafely(guildId, player -> player.setVolume(normalized), "set volume");
        state.setVolume(normalized);

        return PlayerActionResult.success(PlayerActionType.VOLUME_CHANGED);
    }

    protected PlayerActionResult doClearQueue(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || state.isQueueEmpty()) {
            return PlayerActionResult.noop(PlayerActionType.QUEUE_CLEARED, "QUEUE_ALREADY_EMPTY");
        }

        state.clearQueue();
        return PlayerActionResult.success(PlayerActionType.QUEUE_CLEARED);
    }

    protected PlayerActionResult doPlayNextIfAvailable(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null) {
            return PlayerActionResult.noop(PlayerActionType.SKIPPED, "NO_ACTIVE_TRACK");
        }

        Track next = state.pollNext();

        if (next == null) {
            if (!state.hasCurrentTrack()) {
                guildPlayerRegistry.remove(guildId);
                return PlayerActionResult.noop(PlayerActionType.SKIPPED, "NO_ACTIVE_TRACK");
            }

            stopRemoteTrack(guildId);
            resetLocalState(state);
            guildPlayerRegistry.remove(guildId);
            return PlayerActionResult.success(PlayerActionType.SKIPPED);
        }

        startTrackNow(guildId, state, next);
        return PlayerActionResult.success(PlayerActionType.SKIPPED);
    }

    protected PlayerActionResult doForget(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);

        if (state != null) {
            resetLocalState(state);
        }

        try {
            destroyRemoteLink(guildId);
        } catch (Exception e) {
            log.warn("Failed to destroy lavalink link for guildId={}", guildId, e);
        } finally {
            guildPlayerRegistry.remove(guildId);
        }

        return PlayerActionResult.success(PlayerActionType.FORGET);
    }

    protected PlayerPlayResult playPlaylist(long guildId, GuildPlayerState state, List<Track> tracks) {
        if (tracks.isEmpty()) {
            return PlayerPlayResult.notFound();
        }

        Track first = tracks.get(0);

        if (!state.hasCurrentTrack()) {
            startTrackNow(guildId, state, first);
            tracks.stream().skip(1).forEach(state::enqueue);
            return PlayerPlayResult.playlistStarted(first, tracks.size());
        }

        tracks.forEach(state::enqueue);
        return PlayerPlayResult.playlistEnqueued(first, tracks.size(), state.queueSize());
    }

    protected void startTrackNow(long guildId, GuildPlayerState state, Track track) {
        Link link = lavalinkClient.getOrCreateLink(guildId);

        awaitVoid(link.updatePlayer(player -> player.updateTrack(new TrackUpdateBuilder()
                                        .setEncoded(track.getEncoded())
                                        .build())), guildId, "start track");

        state.setCurrentTrack(track);
        state.setPaused(false);
    }

    protected void stopRemoteTrack(long guildId) {
        try {
            updatePlayerSafely(guildId, player -> player.setTrack(null), "stop track");
        } catch (Exception e) {
            log.warn("Failed to stop remote track for guildId={}", guildId, e);
        }
    }

    protected void destroyRemoteLink(long guildId) {
        Link link = lavalinkClient.getLinkIfCached(guildId);
        if (link == null) {
            return;
        }

        awaitVoid(link.destroy(), guildId, "destroy link");
    }

    protected void resetLocalState(GuildPlayerState state) {
        state.setCurrentTrack(null);
        state.clearQueue();
        state.setPaused(false);
    }

    protected void updatePlayerSafely(long guildId, Consumer<PlayerUpdateBuilder> updater, String action) {
        try {
            awaitVoid(lavalinkClient.getOrCreateLink(guildId).updatePlayer(updater), guildId, action);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to " + action + " for guildId=" + guildId, e);
        }
    }

    protected <T> T withGuildLock(long guildId, Supplier<T> action) {
        ReentrantLock lock = guildLocks.computeIfAbsent(guildId, id -> new ReentrantLock());

        lock.lock();
        try {
            return action.get();
        } finally {
            lock.unlock();
        }
    }
}