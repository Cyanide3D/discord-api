package ru.cyanide3d.discord.jda.plugin.lavalink.command;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.LeafCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistry;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistryCustomizer;
import ru.cyanide3d.discord.jda.api.command.SubcommandsCommandBuilder;
import ru.cyanide3d.discord.jda.command.Slash;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandRestrictions;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerPauseCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerPlayCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerStopCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.GuildPlayerRegistry;

import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.QUERY;

public class DiscordJdaLavalinkSlashCommandRegistryCustomizer implements SlashCommandRegistryCustomizer {

    @Autowired
    private GuildPlayerRegistry guildPlayerRegistry;

    @Override
    public void customize(SlashCommandRegistry slashCommandRegistry) {
        slashCommandRegistry.register(Slash.commands("player", "Управление проигрывателем")
                .subcommand("play", "Включить плеер", this::buildPlayCommand)
                .subcommand("stop", "Остановить плеер", this::buildStopCommand)
                .subcommand("pause", "Пауза / продолжить воспроизведение", this::buildPauseCommand)
                .build());
    }

    protected void buildPauseCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerPauseCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.control(guildPlayerRegistry))
                .add();
    }

    protected void buildStopCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerStopCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.control(guildPlayerRegistry))
                .add();
    }

    protected void buildPlayCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerPlayCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.play())
                .option(QUERY)
                .add();
    }

}
