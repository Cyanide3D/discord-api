package ru.cyanide3d.discord.jda.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.ResolvedSlashLeaf;
import ru.cyanide3d.discord.jda.api.command.SlashCommandContextFactory;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.impl.DefaultSlashCommandContext;
import ru.cyanide3d.discord.jda.api.contexts.impl.DefaultSlashCommandOptions;

public class SlashCommandContextFactoryImpl implements SlashCommandContextFactory {

    @Autowired
    private DiscordRestActionExecutor discordRestActionExecutor;

    @Override
    public EventContext<SlashCommandInteractionEvent> create(SlashCommandInteractionEvent event, ResolvedSlashLeaf leaf) {
        return new DefaultSlashCommandContext(event, leaf.getPath(), new DefaultSlashCommandOptions(leaf.getOptions(), event), discordRestActionExecutor);
    }

}
