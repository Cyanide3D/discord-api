package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.player.Track;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

@Getter
public class GuildPlayerState {

    private final long guildId;

    private final Deque<Track> queue = new ArrayDeque<>();

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
        queue.offerLast(track);
    }

    public Track pollNext() {
        return queue.pollFirst();
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

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    public List<Track> queueSnapshot() {
        return List.copyOf(queue);
    }
}