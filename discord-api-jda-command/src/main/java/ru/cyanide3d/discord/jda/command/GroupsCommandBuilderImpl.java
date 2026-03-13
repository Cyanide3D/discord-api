package ru.cyanide3d.discord.jda.command;

import ru.cyanide3d.discord.jda.api.command.GroupBuilder;
import ru.cyanide3d.discord.jda.api.command.GroupedSlashCommand;
import ru.cyanide3d.discord.jda.api.command.GroupsCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SlashCommand;
import ru.cyanide3d.discord.jda.api.command.SubcommandGroupSpec;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GroupsCommandBuilderImpl implements GroupsCommandBuilder {

    private final String name;

    private final String description;

    private Restriction<SlashCommandContext> restriction;

    private final List<SubcommandGroupSpec> groups = new ArrayList<>();

    public GroupsCommandBuilderImpl(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public GroupsCommandBuilder restrict(Restriction<SlashCommandContext> restriction) {
        this.restriction = restriction;
        return this;
    }

    @Override
    public GroupsCommandBuilder group(String name, String description, Consumer<GroupBuilder> cfg) {
        GroupBuilder builder = new GroupBuilderImpl(this, name, description, groups::add);
        cfg.accept(builder);
        return this;
    }

    @Override
    public SlashCommand build() {
        if (groups.isEmpty()) {
            throw new IllegalStateException("Grouped slash command must contain at least one group");
        }
        return new GroupedSlashCommand(name, description, restriction, groups);
    }

}