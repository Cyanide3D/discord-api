package ru.cyanide3d.discord.jda.api.contexts;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class SimpleContextValueReader<C extends EventContext<?>, T> implements ContextValueReader<C, T> {

    private final Class<C> contextType;

    private final Function<C, Optional<T>> reader;

    private SimpleContextValueReader(Class<C> contextType, Function<C, Optional<T>> reader) {
        this.contextType = Objects.requireNonNull(contextType, "contextType");
        this.reader = Objects.requireNonNull(reader, "reader");
    }

    public static <C extends EventContext<?>, T> SimpleContextValueReader<C, T> optional(Class<C> contextType, Function<C, Optional<T>> reader) {
        return new SimpleContextValueReader<>(contextType, reader);
    }

    public static <C extends EventContext<?>, T> SimpleContextValueReader<C, T> of(Class<C> contextType, Function<C, T> reader) {
        Objects.requireNonNull(reader, "reader");
        return new SimpleContextValueReader<>(contextType, context -> Optional.ofNullable(reader.apply(context)));
    }

    @Override
    public Class<C> getContextType() {
        return contextType;
    }

    @Override
    public Optional<T> read(C context) {
        return reader.apply(context);
    }
}