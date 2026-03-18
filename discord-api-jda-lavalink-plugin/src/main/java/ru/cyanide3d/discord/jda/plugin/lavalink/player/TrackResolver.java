package ru.cyanide3d.discord.jda.plugin.lavalink.player;

public interface TrackResolver {

    TrackLoadResult resolve(TrackIdentifier identifier, long guildId);

}