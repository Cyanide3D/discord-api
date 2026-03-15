package ru.cyanide3d.discord.jda.api.contexts.impl;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.RestAction;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;

import java.util.function.Consumer;

@Slf4j
public class DiscordRestActionExecutorImpl implements DiscordRestActionExecutor {

    @Override
    public <T> void queue(RestAction<T> action, String operation) {
        queue(action, operation, success -> { });
    }

    @Override
    public <T> void queue(RestAction<T> action, String operation, Consumer<? super T> success) {
        action.queue(wrapSuccess(success, operation), error -> logFailure(error, operation));
    }

    private <T> Consumer<? super T> wrapSuccess(Consumer<? super T> success,  String operation) {
        return t -> {
            log.debug("Success executing: {}", operation);
            success.accept(t);
        };
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
