package ru.cyanide3d.discord.jda.plugin.lavalink;

import reactor.core.publisher.Mono;

import java.time.Duration;

public class ReactorUtils {

    private static final Duration LAVALINK_TIMEOUT = Duration.ofSeconds(10);

    public static <T> T await(Mono<T> mono, long guildId, String action) {
        try {
            return mono.timeout(LAVALINK_TIMEOUT).block();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Lavalink action failed: " + action + ", guildId=" + guildId,
                    e
            );
        }
    }

    public static void awaitVoid(Mono<?> mono, long guildId, String action) {
        await(mono.then(Mono.just(Boolean.TRUE)), guildId, action);
    }

}
