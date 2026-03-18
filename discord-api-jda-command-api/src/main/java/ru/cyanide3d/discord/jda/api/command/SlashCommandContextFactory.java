package ru.cyanide3d.discord.jda.api.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;

public interface SlashCommandContextFactory {

    SlashCommandContext create(SlashCommandInteractionEvent event, ResolvedSlashLeaf leaf);

}
