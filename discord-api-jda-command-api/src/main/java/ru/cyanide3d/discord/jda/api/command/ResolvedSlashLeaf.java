package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;

@Getter
@RequiredArgsConstructor
public class ResolvedSlashLeaf {

    private final SlashPath path;

    private final Restriction<?> restriction;

    private final SlashExecutor executor;

}