package ru.cyanide3d.discord.jda.plugin.lavalink.command;

import ru.cyanide3d.discord.jda.api.command.LeafCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistry;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistryCustomizer;
import ru.cyanide3d.discord.jda.api.command.SubcommandsCommandBuilder;
import ru.cyanide3d.discord.jda.command.Slash;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerPauseCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerPlayCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerStopCommandExecutor;

import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.QUERY;

public class DiscordJdaLavalinkSlashCommandRegistryCustomizer implements SlashCommandRegistryCustomizer {

    @Override
    public void customize(SlashCommandRegistry slashCommandRegistry) {
        slashCommandRegistry.register(Slash.commands("player", "Управление проигрывателем")
                .subcommand("play", "Включить плеер", this::buildPlayCommand)
                .subcommand("stop", "Остановить плеер", this::buildStopCommand)
                .subcommand("pause", "Поставить плеер на паузу", this::buildPauseCommand)
                .build());
    }

    protected void buildPauseCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerPauseCommandExecutor.class)
                .add();
    }

    protected void buildStopCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerStopCommandExecutor.class)
                .add();
    }

    protected void buildPlayCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerPlayCommandExecutor.class)
                .option(QUERY)
                .add();
    }

}
