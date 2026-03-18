package ru.cyanide3d.discord.jda.plugin.lavalink.player;

public interface PlayerManager {

    PlayerPlayResult play(long guildId, TrackIdentifier identifier);

    PlayerActionResult pause(long guildId);

    PlayerActionResult resume(long guildId);

    PlayerActionResult stop(long guildId);

    PlayerActionResult skip(long guildId);

    PlayerActionResult seek(long guildId, long positionMs);

    PlayerActionResult setVolume(long guildId, int volume);

    PlayerActionResult clearQueue(long guildId);

}