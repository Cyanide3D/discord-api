package ru.cyanide3d.discord.jda.api.contexts;

import java.util.Objects;

public final class SlashReaders {

    private SlashReaders() {
    }

    public static final ContextValueReader<SlashCommandContext, SlashPath> PATH =
            Readers.slashPath();

    public static final ContextValueReader<SlashCommandContext, SlashCommandOptions> OPTIONS =
            Readers.slashOptions();

    public static final ContextValueReader<SlashCommandContext, String> COMMAND_NAME =
            Readers.map(PATH, SlashPath::getCommand);

    public static final ContextValueReader<SlashCommandContext, String> GROUP_NAME =
            Readers.flatMap(PATH, path -> java.util.Optional.ofNullable(path.getGroup()));

    public static final ContextValueReader<SlashCommandContext, String> SUBCOMMAND_NAME =
            Readers.flatMap(PATH, path -> java.util.Optional.ofNullable(path.getSubcommand()));

    public static <T> ContextValueReader<SlashCommandContext, T> option(SlashOptionReader<T> option) {
        Objects.requireNonNull(option, "option");
        return Readers.option(option);
    }

    public static <T> ContextValueReader<SlashCommandContext, T> requiredOption(SlashOptionReader<T> option) {
        Objects.requireNonNull(option, "option");
        return Readers.requiredOption(option);
    }

    public static ContextValueReader<SlashCommandContext, Boolean> hasOption(SlashOptionReader<?> option) {
        Objects.requireNonNull(option, "option");
        return Readers.hasOption(option);
    }
}