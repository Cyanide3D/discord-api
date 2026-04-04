package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.GenericEvent;

import java.util.Objects;
import java.util.Optional;

public interface EventContext<T extends GenericEvent> {

    T getEvent();

    default <V> Optional<V> getValue(ContextValueReader<?, V> reader) {
        Objects.requireNonNull(reader, "reader");
        return reader.readFrom(this);
    }

    default <V> V requireValue(ContextValueReader<?, V> reader) {
        Objects.requireNonNull(reader, "reader");
        return reader.requireFrom(this);
    }

    default boolean supports(ContextValueReader<?, ?> reader) {
        Objects.requireNonNull(reader, "reader");
        return reader.supports(this);
    }
}