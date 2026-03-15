package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.capability.InteractionResponseCapability;

public interface SlashCommandContext extends InteractionEventContext<SlashCommandInteractionEvent>, InteractionResponseCapability {

    SlashPath getPath();

}