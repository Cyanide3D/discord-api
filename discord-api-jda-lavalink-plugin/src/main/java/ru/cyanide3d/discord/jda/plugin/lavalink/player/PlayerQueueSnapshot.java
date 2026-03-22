package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.player.Track;
import lombok.Getter;

import java.util.List;

@Getter
public class PlayerQueueSnapshot {

    private final Track currentTrack;

    private final List<Track> queue;

    private final boolean paused;

    private final int volume;

    public PlayerQueueSnapshot(Track currentTrack, List<Track> queue, boolean paused, int volume) {
        this.currentTrack = currentTrack;
        this.queue = List.copyOf(queue);
        this.paused = paused;
        this.volume = volume;
    }
}