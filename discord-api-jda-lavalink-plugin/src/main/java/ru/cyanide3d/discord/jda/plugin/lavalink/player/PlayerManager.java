package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import ru.cyanide3d.discord.jda.plugin.lavalink.ReactorUtils;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PlayerManager {

    default PlayerPlayResult play(long guildId, TrackIdentifier identifier) {
        return ReactorUtils.join(playAsync(guildId, identifier), guildId, "play");
    }

    CompletableFuture<PlayerPlayResult> playAsync(long guildId, TrackIdentifier identifier);

    default PlayerActionResult pause(long guildId) {
        return ReactorUtils.join(pauseAsync(guildId), guildId, "pause");
    }

    CompletableFuture<PlayerActionResult> pauseAsync(long guildId);

    default PlayerActionResult resume(long guildId) {
        return ReactorUtils.join(resumeAsync(guildId), guildId, "resume");
    }

    CompletableFuture<PlayerActionResult> resumeAsync(long guildId);

    default PlayerActionResult stop(long guildId) {
        return ReactorUtils.join(stopAsync(guildId), guildId, "stop");
    }

    CompletableFuture<PlayerActionResult> stopAsync(long guildId);

    default PlayerActionResult skip(long guildId) {
        return ReactorUtils.join(skipAsync(guildId), guildId, "skip");
    }

    CompletableFuture<PlayerActionResult> skipAsync(long guildId);

    default PlayerActionResult seek(long guildId, long positionMs) {
        return ReactorUtils.join(seekAsync(guildId, positionMs), guildId, "seek");
    }

    CompletableFuture<PlayerActionResult> seekAsync(long guildId, long positionMs);

    default PlayerActionResult setVolume(long guildId, int volume) {
        return ReactorUtils.join(setVolumeAsync(guildId, volume), guildId, "set_volume");
    }

    CompletableFuture<PlayerActionResult> setVolumeAsync(long guildId, int volume);

    default PlayerActionResult clearQueue(long guildId) {
        return ReactorUtils.join(clearQueueAsync(guildId), guildId, "clear_queue");
    }

    CompletableFuture<PlayerActionResult> clearQueueAsync(long guildId);

    default PlayerActionResult playNextIfAvailable(long guildId) {
        return ReactorUtils.join(playNextIfAvailableAsync(guildId), guildId, "play_next");
    }

    CompletableFuture<PlayerActionResult> playNextIfAvailableAsync(long guildId);

    default PlayerActionResult forget(long guildId) {
        return ReactorUtils.join(forgetAsync(guildId), guildId, "forget");
    }

    CompletableFuture<PlayerActionResult> forgetAsync(long guildId);

    default Optional<PlayerQueueSnapshot> getQueueSnapshot(long guildId) {
        return ReactorUtils.join(getQueueSnapshotAsync(guildId), guildId, "queue_snapshot");
    }

    CompletableFuture<Optional<PlayerQueueSnapshot>> getQueueSnapshotAsync(long guildId);
}