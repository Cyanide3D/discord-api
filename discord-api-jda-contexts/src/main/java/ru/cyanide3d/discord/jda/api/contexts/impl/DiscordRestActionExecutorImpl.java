package ru.cyanide3d.discord.jda.api.contexts.impl;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.RestAction;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
public class DiscordRestActionExecutorImpl implements DiscordRestActionExecutor {

    @Override
    public <T> void queue(RestAction<T> action, String operation) {
        queue(action, operation, t -> { }, error -> { });
    }

    @Override
    public <T> void queue(RestAction<T> action, String operation, Consumer<? super T> success) {
        queue(action, operation, success, error -> { });
    }

    @Override
    public <T> void queue(RestAction<T> action, String operation, Consumer<? super T> success, Consumer<? super Throwable> failure) {
        action.queue(
                result -> {
                    try {
                        log.debug("Success executing: {}", operation);
                        success.accept(result);
                    } catch (Exception ex) {
                        log.error("Discord REST success handler failed: op={}", operation, ex);
                    }
                },
                error -> {
                    logFailure(error, operation);

                    try {
                        failure.accept(error);
                    } catch (Exception ex) {
                        log.error("Discord REST failure handler failed: op={}", operation, ex);
                    }
                }
        );
    }

    @Override
    public <T> CompletableFuture<T> submit(RestAction<T> action, String operation) {
        CompletableFuture<T> future = action.submit();
        future.whenComplete((result, error) -> {
            if (error != null) {
                logFailure(unwrapCompletionError(error), operation);
            } else {
                log.debug("Success executing: {}", operation);
            }
        });
        return future;
    }

    private Throwable unwrapCompletionError(Throwable error) {
        Throwable current = error;
        while (current instanceof java.util.concurrent.CompletionException
                || current instanceof java.util.concurrent.ExecutionException) {
            Throwable cause = current.getCause();
            if (cause == null) {
                break;
            }
            current = cause;
        }
        return current;
    }

    private void logFailure(Throwable error, String operation) {
        if (error instanceof ErrorResponseException e) {
            log.warn("Discord REST failed: op={}, errorResponse={}, message={}",
                    operation, e.getErrorResponse(), e.getMessage());
        } else {
            log.warn("Discord REST failed: op={}", operation, error);
        }
    }

}
