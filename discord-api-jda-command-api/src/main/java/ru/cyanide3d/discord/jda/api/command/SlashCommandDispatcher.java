package ru.cyanide3d.discord.jda.api.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommandDispatcher {

    void dispatch(SlashCommandInteractionEvent event);

}
