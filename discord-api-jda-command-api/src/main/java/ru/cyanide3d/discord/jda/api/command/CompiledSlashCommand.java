package ru.cyanide3d.discord.jda.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@AllArgsConstructor
@Getter
public class CompiledSlashCommand {

    private final SlashCommand source;

    private final CommandIndex commandIndex;

    private final CommandData commandData;

}
