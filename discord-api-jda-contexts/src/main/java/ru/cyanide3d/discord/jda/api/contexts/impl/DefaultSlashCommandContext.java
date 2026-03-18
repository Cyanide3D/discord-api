package ru.cyanide3d.discord.jda.api.contexts.impl;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandOptions;
import ru.cyanide3d.discord.jda.api.contexts.SlashPath;

@Getter
public class DefaultSlashCommandContext
        extends AbstractInteractionResponseEventContext<SlashCommandInteractionEvent>
        implements SlashCommandContext {

    private final SlashPath path;

    private final SlashCommandOptions options;

    public DefaultSlashCommandContext(SlashCommandInteractionEvent event, SlashPath path, SlashCommandOptions options, DiscordRestActionExecutor restActionExecutor) {
        super(event, restActionExecutor);
        this.path = path;
        this.options = options;
    }

}