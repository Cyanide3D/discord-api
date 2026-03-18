package ru.cyanide3d.discord.jda.api.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;

public interface SlashCommandContextFactory {

    EventContext<SlashCommandInteractionEvent> create(SlashCommandInteractionEvent event, ResolvedSlashLeaf leaf);

}
