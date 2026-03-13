package ru.cyanide3d.discord.jda.api.command;

import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;

import java.util.function.Consumer;

public interface SubcommandsCommandBuilder {

    SubcommandsCommandBuilder restrict(Restriction<SlashCommandContext> restriction);

    SubcommandsCommandBuilder subcommand(String name, String description, Consumer<LeafCommandBuilder<SubcommandsCommandBuilder>> cfg);

    SlashCommand build();

}