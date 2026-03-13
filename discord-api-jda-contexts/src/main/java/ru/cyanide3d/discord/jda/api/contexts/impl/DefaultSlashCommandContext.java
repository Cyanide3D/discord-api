package ru.cyanide3d.discord.jda.api.contexts.impl;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.api.contexts.SlashPath;

@Getter
public class DefaultSlashCommandContext
        extends AbstractInteractionEventContext<SlashCommandInteractionEvent>
        implements SlashCommandContext {

    private final SlashPath path;

    public DefaultSlashCommandContext(SlashCommandInteractionEvent event) {
        super(event);
        path = SlashPath.fromEvent(event);
    }

}
