package ru.cyanide3d.discord.jda.api.contexts;

import java.util.Objects;
import java.util.Optional;

public interface ContextValueReader<C extends EventContext<?>, T> {

    Class<C> getContextType();

    Optional<T> read(C context);

    default boolean supports(EventContext<?> context) {
        Objects.requireNonNull(context, "context");
        return getContextType().isInstance(context);
    }

    default Optional<T> readFrom(EventContext<?> context) {
        Objects.requireNonNull(context, "context");

        if (!supports(context)) {
            return Optional.empty();
        }

        return read(getContextType().cast(context));
    }

    default T requireFrom(EventContext<?> context) {
        return readFrom(context).orElseThrow(() ->
                new IllegalStateException("Reader " + getClass().getSimpleName() + " cannot extract value from context " + context.getClass().getSimpleName()));
    }

}