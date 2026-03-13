package ru.cyanide3d.discord.jda.api.event;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.DiscordRestActionExecutor;

import java.util.function.Consumer;

public abstract class AbstractDiscordJDAEventListenerAdapter extends ListenerAdapter {

    @Autowired
    private DiscordRestActionExecutor actionExecutor;

    protected <R> void queue(RestAction<R> action, String operation) {
        actionExecutor.queue(action, operation);
    }

    protected <R> void queue(RestAction<R> action, String operation, Consumer<? super R> success) {
        actionExecutor.queue(action, operation, success);
    }

    protected <R> void queue(RestAction<R> action) {
        queue(action, getClass().getName());
    }

    protected <R> void queue(RestAction<R> action, Consumer<? super R> success) {
        queue(action, getClass().getName(), success);
    }

}
