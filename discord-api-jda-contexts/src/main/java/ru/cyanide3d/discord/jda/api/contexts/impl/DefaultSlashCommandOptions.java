package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandOptions;
import ru.cyanide3d.discord.jda.api.contexts.SlashOptionReader;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DefaultSlashCommandOptions implements SlashCommandOptions {

    private final Map<String, SlashOptionReader<?>> declaredByName;

    private final Set<SlashOptionReader<?>> declaredByIdentity;

    private final SlashCommandInteractionEvent event;

    public DefaultSlashCommandOptions(Iterable<? extends SlashOptionReader<?>> declaredOptions, SlashCommandInteractionEvent event) {
        this.event = event;
        this.declaredByName = new LinkedHashMap<>();
        this.declaredByIdentity = Collections.newSetFromMap(new IdentityHashMap<>());

        for (SlashOptionReader<?> option : declaredOptions) {
            SlashOptionReader<?> previous = this.declaredByName.put(option.getName(), option);
            if (previous != null && previous != option) {
                throw new IllegalArgumentException("Duplicate declared option name: " + option.getName());
            }

            this.declaredByIdentity.add(option);
        }
    }

    @Override
    public <T> Optional<T> get(SlashOptionReader<T> option) {
        SlashOptionReader<?> declaredOption = declaredByName.get(option.getName());
        if (declaredOption == null) {
            throw new IllegalArgumentException("Option not declared for this command: " + option.getName());
        }

        if (!declaredByIdentity.contains(option)) {
            throw new IllegalArgumentException("Use the same declared option instance for '" + option.getName() + "'");
        }

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
        if (!declaredByIdentity.contains(option)) {
            return false;
        }
        return event.getOption(option.getName()) != null;
    }
}