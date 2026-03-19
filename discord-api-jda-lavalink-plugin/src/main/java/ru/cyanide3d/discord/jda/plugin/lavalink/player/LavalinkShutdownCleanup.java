package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class LavalinkShutdownCleanup {

    @Autowired
    private GuildPlayerRegistry guildPlayerRegistry;

    @Autowired
    private PlayerManager playerManager;

    @PreDestroy
    public void cleanup() {
        for (Long guildId : guildPlayerRegistry.guildIds()) {
            try {
                playerManager.forget(guildId);
            } catch (Exception e) {
                log.warn("Failed to cleanup player for guildId={}", guildId, e);
            }
        }
    }
}