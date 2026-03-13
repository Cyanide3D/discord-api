package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommandContext extends InteractionEventContext<SlashCommandInteractionEvent> {

    SlashPath getPath();

}
