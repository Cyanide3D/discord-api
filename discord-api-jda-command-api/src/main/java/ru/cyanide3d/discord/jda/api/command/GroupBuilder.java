package ru.cyanide3d.discord.jda.api.command;

import java.util.function.Consumer;

public interface GroupBuilder {

    GroupBuilder subcommand(String name, String description, Consumer<LeafCommandBuilder<GroupBuilder>> cfg);

    GroupsCommandBuilder add();

}