package ru.cyanide3d.discord.jda.command;

import ru.cyanide3d.discord.jda.api.command.LeafCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SlashCommand;
import ru.cyanide3d.discord.jda.api.command.SubcommandSpec;
import ru.cyanide3d.discord.jda.api.command.SubcommandsCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SubcommandsSlashCommand;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SubcommandsCommandBuilderImpl implements SubcommandsCommandBuilder {

    private final String name;

    private final String description;

    private Restriction<SlashCommandContext> restriction;

    private final List<SubcommandSpec> subcommands = new ArrayList<>();

    public SubcommandsCommandBuilderImpl(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public SubcommandsCommandBuilder restrict(Restriction<SlashCommandContext> restriction) {
        this.restriction = restriction;
        return this;
    }

    @Override
    public SubcommandsCommandBuilder subcommand(String name, String description, Consumer<LeafCommandBuilder<SubcommandsCommandBuilder>> cfg) {
        LeafCommandBuilder<SubcommandsCommandBuilder> builder = new LeafCommandBuilderImpl<>(this, name, description, subcommands::add);
        cfg.accept(builder);
        return this;
    }

    @Override
    public SlashCommand build() {
        if (subcommands.isEmpty()) {
            throw new IllegalStateException("Subcommands slash command must contain at least one subcommand");
        }
        return new SubcommandsSlashCommand(name, description, restriction, subcommands);
    }

}