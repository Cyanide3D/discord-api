package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import ru.cyanide3d.discord.jda.plugin.lavalink.ReactorUtils;

import java.util.concurrent.CompletableFuture;

public interface TrackResolver {

    default TrackLoadResult resolve(TrackIdentifier identifier, long guildId) {
        return ReactorUtils.join(resolveAsync(identifier, guildId), guildId, "resolve_track");
    }

    CompletableFuture<TrackLoadResult> resolveAsync(TrackIdentifier identifier, long guildId);

}