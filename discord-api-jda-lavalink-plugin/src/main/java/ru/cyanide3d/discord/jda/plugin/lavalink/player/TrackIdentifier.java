package ru.cyanide3d.discord.jda.plugin.lavalink.player;

public interface TrackIdentifier {

    String buildStringIdentifier();

    default boolean supportsPlaylist() {
        return true;
    }

    default String sourceName() {
        return getClass().getSimpleName();
    }

}