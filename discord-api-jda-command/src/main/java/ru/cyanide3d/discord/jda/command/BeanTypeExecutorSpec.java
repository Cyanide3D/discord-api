package ru.cyanide3d.discord.jda.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cyanide3d.discord.jda.api.command.ExecutorSpec;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;

@RequiredArgsConstructor
public class BeanTypeExecutorSpec implements ExecutorSpec {

    @Getter
    private final Class<? extends SlashExecutor> type;

}