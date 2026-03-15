package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;

@Getter
public abstract class AbstractSlashCommand implements SlashCommand {

    private final String name;

    private final String description;

    private final Restriction<SlashCommandContext> restriction;

    protected AbstractSlashCommand(String name, String description, Restriction<SlashCommandContext> restriction) {
        this.name = name;
        this.description = description;
        this.restriction = restriction == null ? Restriction.allow() : restriction;
    }

}