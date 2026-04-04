package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandOptions;
import ru.cyanide3d.discord.jda.api.contexts.SlashOptionReader;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class DefaultSlashCommandOptions implements SlashCommandOptions {

    private final Map<String, SlashOptionReader<?>> declaredByName;

    private final SlashCommandInteractionEvent event;

    public DefaultSlashCommandOptions(Iterable<? extends SlashOptionReader<?>> declaredOptions, SlashCommandInteractionEvent event) {
        this.event = Objects.requireNonNull(event, "event");
        this.declaredByName = new LinkedHashMap<>();

        for (SlashOptionReader<?> option : declaredOptions) {
            SlashOptionReader<?> previous = this.declaredByName.put(option.getName(), option);
            if (previous != null && !previous.isCompatibleWith(option)) {
                throw new IllegalArgumentException("Duplicate declared option name with incompatible reader: " + option.getName());
            }
        }
    }

    @Override
    public <T> Optional<T> get(SlashOptionReader<T> option) {
        requireCompatibleOption(option);

        OptionMapping mapping = event.getOption(option.getName());
        if (mapping == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(option.readOption(mapping));
    }

    @Override
    public <T> T require(SlashOptionReader<T> option) {
        return get(option).orElseThrow(() ->
                new IllegalStateException("Required option missing: " + option.getName()));
    }

    @Override
    public boolean has(SlashOptionReader<?> option) {
        try {
            requireCompatibleOption(option);
            return event.getOption(option.getName()) != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void requireCompatibleOption(SlashOptionReader<?> option) {
        SlashOptionReader<?> declaredOption = declaredByName.get(option.getName());
        if (declaredOption == null) {
            throw new IllegalArgumentException("Option not declared for this command: " + option.getName());
        }

        if (!declaredOption.isCompatibleWith(option) || !option.isCompatibleWith(declaredOption)) {
            throw new IllegalArgumentException(
                    "Incompatible option reader for '" + option.getName() + "'. Declared="
                            + declaredOption.getClass().getName()
                            + ", actual=" + option.getClass().getName()
            );
        }
    }
}