package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.PlayerUpdateBuilder;
import dev.arbjerg.lavalink.client.player.Track;
import dev.arbjerg.lavalink.client.player.TrackUpdateBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Consumer;

import static ru.cyanide3d.discord.jda.plugin.lavalink.ReactorUtils.awaitVoid;

@Slf4j
public class PlayerManagerImpl implements PlayerManager {

    @Autowired
    private LavalinkClient lavalinkClient;

    @Autowired
    private GuildPlayerRegistry guildPlayerRegistry;

    @Autowired
    private TrackResolver trackResolver;

    @Override
    public PlayerPlayResult play(long guildId, TrackIdentifier identifier) {
        TrackLoadResult loadResult = trackResolver.resolve(identifier, guildId);
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

    @Override
    public PlayerActionResult pause(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || !state.hasCurrentTrack()) {
            return PlayerActionResult.success(PlayerActionType.PAUSED);
        }

        updatePlayerSafely(guildId, player -> player.setPaused(true), "pause");
        state.setPaused(true);

        return PlayerActionResult.success(PlayerActionType.PAUSED);
    }

    @Override
    public PlayerActionResult resume(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || !state.hasCurrentTrack()) {
            return PlayerActionResult.success(PlayerActionType.RESUMED);
        }

        updatePlayerSafely(guildId, player -> player.setPaused(false), "resume");
        state.setPaused(false);

        return PlayerActionResult.success(PlayerActionType.RESUMED);
    }

    @Override
    public PlayerActionResult stop(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null) {
            return PlayerActionResult.success(PlayerActionType.STOPPED);
        }

        stopRemoteTrack(guildId);
        resetLocalState(state);
        guildPlayerRegistry.remove(guildId);

        return PlayerActionResult.success(PlayerActionType.STOPPED);
    }

    @Override
    public PlayerActionResult skip(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null) {
            return PlayerActionResult.success(PlayerActionType.SKIPPED);
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

    @Override
    public PlayerActionResult seek(long guildId, long positionMs) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null || !state.hasCurrentTrack()) {
            return PlayerActionResult.success(PlayerActionType.SEEKED);
        }

        updatePlayerSafely(guildId, player -> player.setPosition(positionMs), "seek");

        return PlayerActionResult.success(PlayerActionType.SEEKED);
    }

    @Override
    public PlayerActionResult setVolume(long guildId, int volume) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null) {
            return PlayerActionResult.success(PlayerActionType.VOLUME_CHANGED);
        }

        int normalized = Math.max(0, Math.min(1000, volume));

        updatePlayerSafely(guildId, player -> player.setVolume(normalized), "set volume");
        state.setVolume(normalized);

        return PlayerActionResult.success(PlayerActionType.VOLUME_CHANGED);
    }

    @Override
    public PlayerActionResult clearQueue(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null) {
            return PlayerActionResult.success(PlayerActionType.QUEUE_CLEARED);
        }

        state.clearQueue();
        return PlayerActionResult.success(PlayerActionType.QUEUE_CLEARED);
    }

    @Override
    public PlayerActionResult playNextIfAvailable(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null) {
            return PlayerActionResult.success(PlayerActionType.SKIPPED);
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

    @Override
    public PlayerActionResult forget(long guildId) {
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
        awaitVoid(link.updatePlayer(player -> player.updateTrack(new TrackUpdateBuilder().setEncoded(track.getEncoded()).build())), guildId, "startTrackNow");

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

        awaitVoid(link.destroy(), guildId, "destroy_player");
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

}