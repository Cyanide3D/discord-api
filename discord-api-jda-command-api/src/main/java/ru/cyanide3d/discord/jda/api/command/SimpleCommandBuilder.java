package ru.cyanide3d.discord.jda.api.command;

import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;

public interface SimpleCommandBuilder {

    SimpleCommandBuilder restrict(Restriction<SlashCommandContext> restriction);

    SimpleCommandBuilder option(OptionSpec<?> option);

    SimpleCommandBuilder onExecute(SlashExecutor executor);

    SimpleCommandBuilder onExecute(Class<? extends SlashExecutor> executorType);

    SlashCommand build();

}