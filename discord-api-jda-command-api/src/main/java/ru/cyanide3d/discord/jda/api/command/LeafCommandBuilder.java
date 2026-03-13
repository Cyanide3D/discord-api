package ru.cyanide3d.discord.jda.api.command;

import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.contexts.SlashCommandContext;

public interface LeafCommandBuilder<T> {

    LeafCommandBuilder<T> restrict(Restriction<SlashCommandContext> restriction);

    LeafCommandBuilder<T> option(OptionSpec option);

    LeafCommandBuilder<T> onExecute(SlashExecutor executor);

    LeafCommandBuilder<T> onExecute(Class<? extends SlashExecutor> executorType);

    T add();

}