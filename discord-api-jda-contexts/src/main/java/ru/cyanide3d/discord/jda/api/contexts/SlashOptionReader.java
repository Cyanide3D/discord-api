package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;
import java.util.Optional;

public interface SlashOptionReader<T> extends ContextValueReader<SlashCommandContext, T> {

    String getName();

    boolean isRequired();

    T readOption(OptionMapping mapping);

    default boolean isCompatibleWith(SlashOptionReader<?> other) {
        return other != null
                && Objects.equals(getName(), other.getName())
                && getClass().equals(other.getClass());
    }

    @Override
    default Class<SlashCommandContext> getContextType() {
        return SlashCommandContext.class;
    }

    @Override
    default Optional<T> read(SlashCommandContext context) {
        return context.getOptions().get(this);
    }

}