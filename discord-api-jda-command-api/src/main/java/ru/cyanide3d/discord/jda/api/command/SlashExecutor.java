package ru.cyanide3d.discord.jda.api.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@FunctionalInterface
public interface SlashExecutor {

    void execute(SlashCommandInteractionEvent event);

}