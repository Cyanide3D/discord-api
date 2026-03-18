package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cyanide3d.discord.jda.api.contexts.SlashOptionReader;
import ru.cyanide3d.discord.jda.api.contexts.SlashPath;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ResolvedSlashLeaf {

    private final SlashPath path;

    private final Restriction<?> restriction;

    private final List<SlashOptionReader<?>> options;

    private final SlashExecutor executor;

}