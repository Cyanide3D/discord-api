package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.capability.InteractionResponseCapability;

import java.util.Optional;

public interface SlashCommandContext extends InteractionEventContext<SlashCommandInteractionEvent>, InteractionResponseCapability {

    SlashPath getPath();

    SlashCommandOptions getOptions();

    default <T> Optional<T> getOption(SlashOptionReader<T> option) {
        return getOptions().get(option);
    }

    default <T> T requireOption(SlashOptionReader<T> option) {
        return getOptions().require(option);
    }

    default boolean hasOption(SlashOptionReader<?> option) {
        return getOptions().has(option);
    }


}