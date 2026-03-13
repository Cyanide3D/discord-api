package ru.cyanide3d.discord.jda.api.command;

import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;

@FunctionalInterface
public interface SlashExecutor {

    void execute(SlashCommandContext ctx);

}