package ru.cyanide3d.discord.jda.api.command;

public interface SlashCommand {

    String getName();

    String getDescription();

    CompiledSlashCommand compile(SlashExecutorResolver<?> resolver);

}