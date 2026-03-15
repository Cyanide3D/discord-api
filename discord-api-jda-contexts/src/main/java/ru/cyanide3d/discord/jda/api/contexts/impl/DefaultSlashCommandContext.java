package ru.cyanide3d.discord.jda.api.contexts.impl;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.api.contexts.SlashPath;

@Getter
public class DefaultSlashCommandContext
        extends AbstractInteractionResponseEventContext<SlashCommandInteractionEvent>
        implements SlashCommandContext {

    private final SlashPath path;

    public DefaultSlashCommandContext(SlashCommandInteractionEvent event, DiscordRestActionExecutor restActionExecutor) {
        super(event, restActionExecutor);
        this.path = SlashPath.fromEvent(event);
    }

}