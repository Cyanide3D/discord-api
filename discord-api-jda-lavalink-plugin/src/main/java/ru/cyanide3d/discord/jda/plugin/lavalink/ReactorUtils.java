package ru.cyanide3d.discord.jda.plugin.lavalink;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@UtilityClass
public class ReactorUtils {

    private static final Duration LAVALINK_TIMEOUT = Duration.ofSeconds(10);

    public static <T> CompletableFuture<T> toFuture(Mono<T> mono, long guildId, String action) {
        return mono.timeout(LAVALINK_TIMEOUT)
                .toFuture()
                .handle((value, error) -> {
                    if (error == null) {
                        return value;
                    }

                    throw new CompletionException(new IllegalStateException(
                            "Lavalink action failed: " + action + ", guildId=" + guildId,
                            unwrap(error)
                    ));
                });
    }

    public static <T> T await(Mono<T> mono, long guildId, String action) {
        return join(toFuture(mono, guildId, action), guildId, action);
    }

    public static void awaitVoid(Mono<?> mono, long guildId, String action) {
        join(toFuture(mono.thenReturn(Boolean.TRUE), guildId, action), guildId, action);
    }

    public static <T> T join(CompletableFuture<T> future, long guildId, String action) {
        try {
            return future.join();
        } catch (CompletionException e) {
            Throwable cause = unwrap(e);
            if (cause instanceof IllegalStateException) {
                throw (IllegalStateException) cause;
            }

            throw new IllegalStateException(
                    "Lavalink action failed: " + action + ", guildId=" + guildId,
                    cause
            );
        }
    }

    private static Throwable unwrap(Throwable error) {
        Throwable current = error;
        while (current instanceof CompletionException && current.getCause() != null) {
            current = current.getCause();
        }
        return current;
    }
}