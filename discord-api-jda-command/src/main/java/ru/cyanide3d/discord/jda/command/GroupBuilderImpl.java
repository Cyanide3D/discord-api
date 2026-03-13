package ru.cyanide3d.discord.jda.command;

import ru.cyanide3d.discord.jda.api.command.GroupBuilder;
import ru.cyanide3d.discord.jda.api.command.GroupsCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.LeafCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SubcommandGroupSpec;
import ru.cyanide3d.discord.jda.api.command.SubcommandSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GroupBuilderImpl implements GroupBuilder {

    private final GroupsCommandBuilder parent;

    private final String name;

    private final String description;

    private final Consumer<SubcommandGroupSpec> consumer;

    private final List<SubcommandSpec> subcommands = new ArrayList<>();

    public GroupBuilderImpl(GroupsCommandBuilder parent, String name, String description, Consumer<SubcommandGroupSpec> consumer) {
        this.parent = parent;
        this.name = name;
        this.description = description;
        this.consumer = consumer;
    }

    @Override
    public GroupBuilder subcommand(String name, String description, Consumer<LeafCommandBuilder<GroupBuilder>> cfg) {
        LeafCommandBuilder<GroupBuilder> builder = new LeafCommandBuilderImpl<>(this, name, description, subcommands::add);
        cfg.accept(builder);
        return this;
    }

    @Override
    public GroupsCommandBuilder add() {
        if (subcommands.isEmpty()) {
            throw new IllegalStateException("Group must contain at least one subcommand");
        }
        consumer.accept(new SubcommandGroupSpec(name, description, subcommands));
        return parent;
    }

}