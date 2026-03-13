package ru.cyanide3d.discord.jda.command;

import ru.cyanide3d.discord.jda.api.command.ExecutorSpec;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.command.SlashExecutorResolver;

public class InstanceSlashExecutorResolver<T extends InstanceExecutorSpec> implements SlashExecutorResolver<T> {

    @Override
    public boolean supports(ExecutorSpec spec) {
        return spec instanceof InstanceExecutorSpec;
    }

    @Override
    public SlashExecutor resolve(T spec) {
        return spec.getExecutor();
    }

}