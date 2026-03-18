package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.player.Track;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class TrackLoadResult {

    private final List<Track> tracks;

    private final boolean playlist;

    private final String sourceName;

    public boolean isEmpty() {
        return tracks == null || tracks.isEmpty();
    }

    public Track firstTrack() {
        return isEmpty() ? null : tracks.get(0);
    }

}