package ru.cyanide3d.discord.jda.api.command;

public interface SlashCommandCompiler {

    CompiledSlashCommand compile(SlashCommand command);

}
