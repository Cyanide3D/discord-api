package ru.cyanide3d.discord.jda.api.contexts;

import java.util.Optional;

public interface SlashCommandOptions {

    <T> Optional<T> get(SlashOptionReader<T> option);

    <T> T require(SlashOptionReader<T> option);

    boolean has(SlashOptionReader<?> option);
}