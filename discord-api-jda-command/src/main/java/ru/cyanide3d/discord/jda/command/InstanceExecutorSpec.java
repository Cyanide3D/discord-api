package ru.cyanide3d.discord.jda.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cyanide3d.discord.jda.api.command.ExecutorSpec;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;

@RequiredArgsConstructor
@Getter
public class InstanceExecutorSpec implements ExecutorSpec {

    @Getter
    private final SlashExecutor executor;

}