package ru.cyanide3d.discord.jda.command;

import ru.cyanide3d.discord.jda.api.command.ExecutorSpec;
import ru.cyanide3d.discord.jda.api.command.OptionSpec;
import ru.cyanide3d.discord.jda.api.command.SimpleCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SimpleSlashCommand;
import ru.cyanide3d.discord.jda.api.command.SlashCommand;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.contexts.SlashCommandContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleCommandBuilderImpl implements SimpleCommandBuilder {

    private final String name;

    private final String description;

    private Restriction<SlashCommandContext> restriction;

    private final List<OptionSpec> options = new ArrayList<>();

    private ExecutorSpec executorSpec;

    public SimpleCommandBuilderImpl(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public SimpleCommandBuilder restrict(Restriction<SlashCommandContext> restriction) {
        this.restriction = restriction;
        return this;
    }

    @Override
    public SimpleCommandBuilder option(OptionSpec option) {
        options.add(Objects.requireNonNull(option));
        return this;
    }

    @Override
    public SimpleCommandBuilder onExecute(SlashExecutor executor) {
        this.executorSpec = new InstanceExecutorSpec(executor);
        return this;
    }

    @Override
    public SimpleCommandBuilder onExecute(Class<? extends SlashExecutor> executorType) {
        this.executorSpec = new BeanTypeExecutorSpec(executorType);
        return this;
    }

    @Override
    public SlashCommand build() {
        if (executorSpec == null) {
            throw new IllegalStateException("Simple slash command must have executorSpec");
        }
        return new SimpleSlashCommand(name, description, restriction, options, executorSpec);
    }

}