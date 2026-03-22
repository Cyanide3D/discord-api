package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.player.Track;
import lombok.Getter;

@Getter
public class PlayerPlayResult extends PlayerActionResult {

    private final Track track;

    private final int queuePosition;

    private final int playlistSize;

    private PlayerPlayResult(PlayerResultStatus status, PlayerActionType actionType, String errorCode, Track track, int queuePosition, int playlistSize) {
        super(status, actionType, errorCode);
        this.track = track;
        this.queuePosition = queuePosition;
        this.playlistSize = playlistSize;
    }

    public static PlayerPlayResult started(Track track) {
        return new PlayerPlayResult(PlayerResultStatus.SUCCESS, PlayerActionType.STARTED, null, track, -1, 0);
    }

    public static PlayerPlayResult enqueued(Track track, int queuePosition) {
        return new PlayerPlayResult(PlayerResultStatus.SUCCESS, PlayerActionType.ENQUEUED, null, track, queuePosition, 0);
    }

    public static PlayerPlayResult playlistStarted(Track firstTrack, int playlistSize) {
        return new PlayerPlayResult(PlayerResultStatus.SUCCESS, PlayerActionType.PLAYLIST_STARTED, null, firstTrack, -1, playlistSize);
    }

    public static PlayerPlayResult playlistEnqueued(Track firstTrack, int playlistSize, int queuePosition) {
        return new PlayerPlayResult(PlayerResultStatus.SUCCESS, PlayerActionType.PLAYLIST_ENQUEUED, null, firstTrack, queuePosition, playlistSize);
    }

    public static PlayerPlayResult notFound() {
        return new PlayerPlayResult(PlayerResultStatus.FAILURE, PlayerActionType.NOT_FOUND, "TRACK_NOT_FOUND", null, -1, 0);
    }

    public static PlayerPlayResult failed(String errorCode) {
        return new PlayerPlayResult(PlayerResultStatus.FAILURE, PlayerActionType.FAILED, errorCode, null, -1, 0);
    }
}