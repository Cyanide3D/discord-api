package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandOptions;
import ru.cyanide3d.discord.jda.api.contexts.SlashOptionReader;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultSlashCommandOptions implements SlashCommandOptions {

    private final Map<String, SlashOptionReader<?>> declared;

    private final SlashCommandInteractionEvent event;

    public DefaultSlashCommandOptions(Iterable<? extends SlashOptionReader<?>> declaredOptions, SlashCommandInteractionEvent event) {
        this.event = event;
        this.declared = new LinkedHashMap<>();
        for (SlashOptionReader<?> option : declaredOptions) {
            this.declared.put(option.getName(), option);
        }
    }

    @Override
    public <T> Optional<T> get(SlashOptionReader<T> option) {
        SlashOptionReader<?> declaredOption = declared.get(option.getName());
        if (declaredOption == null) {
            throw new IllegalArgumentException("Option not declared for this command: " + option.getName());
        }

        OptionMapping mapping = event.getOption(option.getName());
        if (mapping == null) {
            return Optional.empty();
        }

        return Optional.of(option.read(mapping));
    }

    @Override
    public <T> T require(SlashOptionReader<T> option) {
        return get(option).orElseThrow(() ->
                new IllegalStateException("Required option missing: " + option.getName()));
    }

    @Override
    public boolean has(SlashOptionReader<?> option) {
        if (!declared.containsKey(option.getName())) {
            return false;
        }
        return event.getOption(option.getName()) != null;
    }
}