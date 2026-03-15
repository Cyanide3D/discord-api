package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.requests.RestAction;

import java.util.function.Consumer;

public interface DiscordRestActionExecutor {

    <T> void queue(RestAction<T> action, String operation);

    <T> void queue(RestAction<T> action, String operation, Consumer<? super T> success);

}
