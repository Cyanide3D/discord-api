package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class YoutubeTrackIdentifier implements TrackIdentifier {

    private final String request;

    public static YoutubeTrackIdentifier of(String request) {
        return new YoutubeTrackIdentifier(request);
    }

    @Override
    public String buildStringIdentifier() {
        String trimmed = request.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }
        return "ytsearch:" + trimmed;
    }
}