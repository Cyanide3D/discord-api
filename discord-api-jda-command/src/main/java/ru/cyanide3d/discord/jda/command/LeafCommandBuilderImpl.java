package ru.cyanide3d.discord.jda.command;

import ru.cyanide3d.discord.jda.api.command.ExecutorSpec;
import ru.cyanide3d.discord.jda.api.command.LeafCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.OptionSpec;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.command.SubcommandSpec;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.contexts.SlashCommandContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class LeafCommandBuilderImpl<T> implements LeafCommandBuilder<T> {

    private final T parent;

    private final String name;

    private final String description;

    private final Consumer<SubcommandSpec> consumer;

    private final List<OptionSpec> options = new ArrayList<>();

    private Restriction<SlashCommandContext> restriction;

    private ExecutorSpec executorSpec;

    public LeafCommandBuilderImpl(T parent, String name, String description, Consumer<SubcommandSpec> consumer) {
        this.parent = parent;
        this.name = name;
        this.description = description;
        this.consumer = consumer;
    }

    @Override
    public LeafCommandBuilder<T> restrict(Restriction<SlashCommandContext> restriction) {
        this.restriction = restriction;
        return this;
    }

    @Override
    public LeafCommandBuilder<T> option(OptionSpec option) {
        options.add(Objects.requireNonNull(option));
        return this;
    }

    @Override
    public LeafCommandBuilder<T> onExecute(SlashExecutor executor) {
        this.executorSpec = new InstanceExecutorSpec(executor);
        return this;
    }

    @Override
    public LeafCommandBuilder<T> onExecute(Class<? extends SlashExecutor> executorType) {
        this.executorSpec = new BeanTypeExecutorSpec(executorType);
        return this;
    }

    @Override
    public T add() {
        if (executorSpec == null) {
            throw new IllegalStateException("Subcommand must have executorSpec");
        }
        consumer.accept(new SubcommandSpec(name, description, options, restriction, executorSpec));
        return parent;
    }

}