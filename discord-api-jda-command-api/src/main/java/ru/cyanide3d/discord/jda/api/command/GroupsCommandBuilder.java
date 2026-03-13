package ru.cyanide3d.discord.jda.api.command;

import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.contexts.SlashCommandContext;

import java.util.function.Consumer;

public interface GroupsCommandBuilder {

    GroupsCommandBuilder restrict(Restriction<SlashCommandContext> restriction);

    GroupsCommandBuilder group(String name, String description, Consumer<GroupBuilder> cfg);

    SlashCommand build();

}