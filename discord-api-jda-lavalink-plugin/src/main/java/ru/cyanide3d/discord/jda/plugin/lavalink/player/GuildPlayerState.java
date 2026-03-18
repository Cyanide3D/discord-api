package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.player.Track;
import lombok.Getter;
import lombok.Setter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class GuildPlayerState {

    private final long guildId;

    private final Queue<Track> queue = new ConcurrentLinkedQueue<>();

    @Setter
    private Track currentTrack;

    @Setter
    private boolean paused;

    @Setter
    private int volume = 100;

    public GuildPlayerState(long guildId) {
        this.guildId = guildId;
    }

    public void enqueue(Track track) {
        queue.offer(track);
    }

    public Track pollNext() {
        return queue.poll();
    }

    public void clearQueue() {
        queue.clear();
    }

    public boolean hasCurrentTrack() {
        return currentTrack != null;
    }

    public int queueSize() {
        return queue.size();
    }

}