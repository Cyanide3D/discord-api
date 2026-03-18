package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.Track;
import dev.arbjerg.lavalink.client.player.TrackUpdateBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        Link link = lavalinkClient.getOrCreateLink(guildId);
        link.updatePlayer(player -> player.setPaused(true)).block();

        GuildPlayerState state = guildPlayerRegistry.getOrCreate(guildId);
        state.setPaused(true);

        return PlayerActionResult.success(PlayerActionType.PAUSED);
    }

    @Override
    public PlayerActionResult resume(long guildId) {
        Link link = lavalinkClient.getOrCreateLink(guildId);
        link.updatePlayer(player -> player.setPaused(false)).block();

        GuildPlayerState state = guildPlayerRegistry.getOrCreate(guildId);
        state.setPaused(false);

        return PlayerActionResult.success(PlayerActionType.RESUMED);
    }

    @Override
    public PlayerActionResult stop(long guildId) {
        Link link = lavalinkClient.getOrCreateLink(guildId);
        link.updatePlayer(player -> player.setTrack(null)).block();

        GuildPlayerState state = guildPlayerRegistry.getOrCreate(guildId);
        state.setCurrentTrack(null);
        state.clearQueue();
        state.setPaused(false);

        return PlayerActionResult.success(PlayerActionType.STOPPED);
    }

    @Override
    public PlayerActionResult skip(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.getOrCreate(guildId);
        Track next = state.pollNext();

        if (next == null) {
            Link link = lavalinkClient.getOrCreateLink(guildId);
            link.updatePlayer(player -> player.setTrack(null)).block();

            state.setCurrentTrack(null);
            state.setPaused(false);

            return PlayerActionResult.success(PlayerActionType.SKIPPED);
        }

        startTrackNow(guildId, state, next);
        return PlayerActionResult.success(PlayerActionType.SKIPPED);
    }

    @Override
    public PlayerActionResult seek(long guildId, long positionMs) {
        Link link = lavalinkClient.getOrCreateLink(guildId);
        link.updatePlayer(player -> player.setPosition(positionMs)).block();

        return PlayerActionResult.success(PlayerActionType.SEEKED);
    }

    @Override
    public PlayerActionResult setVolume(long guildId, int volume) {
        int normalized = Math.max(0, Math.min(1000, volume));

        Link link = lavalinkClient.getOrCreateLink(guildId);
        link.updatePlayer(player -> player.setVolume(normalized)).block();

        GuildPlayerState state = guildPlayerRegistry.getOrCreate(guildId);
        state.setVolume(normalized);

        return PlayerActionResult.success(PlayerActionType.VOLUME_CHANGED);
    }

    @Override
    public PlayerActionResult clearQueue(long guildId) {
        GuildPlayerState state = guildPlayerRegistry.getOrCreate(guildId);
        state.clearQueue();

        return PlayerActionResult.success(PlayerActionType.QUEUE_CLEARED);
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
        link.updatePlayer(player ->
                player.updateTrack(
                        new TrackUpdateBuilder()
                                .setEncoded(track.getEncoded())
                                .build()
                )
        ).block();

        state.setCurrentTrack(track);
        state.setPaused(false);
    }
}