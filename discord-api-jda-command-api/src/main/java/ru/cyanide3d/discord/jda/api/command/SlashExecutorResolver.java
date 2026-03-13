package ru.cyanide3d.discord.jda.api.command;

public interface SlashExecutorResolver<T extends ExecutorSpec> {

    SlashExecutor resolve(T spec);

    boolean supports(ExecutorSpec spec);

}