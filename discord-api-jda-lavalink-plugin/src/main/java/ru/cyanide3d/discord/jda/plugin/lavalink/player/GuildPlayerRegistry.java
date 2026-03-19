package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GuildPlayerRegistry {

    private final Map<Long, GuildPlayerState> players = new ConcurrentHashMap<>();

    public GuildPlayerState getOrCreate(long guildId) {
        return players.computeIfAbsent(guildId, GuildPlayerState::new);
    }

    public GuildPlayerState get(long guildId) {
        return players.get(guildId);
    }

    public void remove(long guildId) {
        players.remove(guildId);
    }

    public Set<Long> guildIds() {
        return Set.copyOf(players.keySet());
    }
}