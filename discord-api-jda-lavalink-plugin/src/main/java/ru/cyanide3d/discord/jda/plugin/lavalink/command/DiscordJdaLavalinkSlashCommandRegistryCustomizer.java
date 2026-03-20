package ru.cyanide3d.discord.jda.plugin.lavalink.command;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.LeafCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistry;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistryCustomizer;
import ru.cyanide3d.discord.jda.api.command.SubcommandsCommandBuilder;
import ru.cyanide3d.discord.jda.command.Slash;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerClearQueueCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandRestrictions;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerLeaveCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerPauseCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerPlayCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerQueueCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerResumeCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerSeekCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerSkipCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerStopCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerVolumeCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.GuildPlayerRegistry;

import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.PAGE;
import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.POSITION_SECONDS;
import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.QUERY;
import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.VOLUME;

public class DiscordJdaLavalinkSlashCommandRegistryCustomizer implements SlashCommandRegistryCustomizer {

    @Autowired
    private GuildPlayerRegistry guildPlayerRegistry;

    @Override
    public void customize(SlashCommandRegistry slashCommandRegistry) {
        slashCommandRegistry.register(Slash.commands("player", "Управление проигрывателем")
                .subcommand("play", "Включить плеер", this::buildPlayCommand)
                .subcommand("pause", "Поставить воспроизведение на паузу", this::buildPauseCommand)
                .subcommand("resume", "Продолжить воспроизведение", this::buildResumeCommand)
                .subcommand("skip", "Пропустить текущий трек", this::buildSkipCommand)
                .subcommand("seek", "Перемотать текущий трек", this::buildSeekCommand)
                .subcommand("volume", "Изменить громкость", this::buildVolumeCommand)
                .subcommand("queue", "Показать очередь треков", this::buildQueueCommand)
                .subcommand("clear", "Очистить очередь", this::buildClearQueueCommand)
                .subcommand("stop", "Остановить плеер", this::buildStopCommand)
                .subcommand("leave", "Отключить бота от голосового канала", this::buildLeaveCommand)
                .build());
    }

    protected void buildPauseCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerPauseCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.control(guildPlayerRegistry))
                .add();
    }

    protected void buildResumeCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerResumeCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.control(guildPlayerRegistry))
                .add();
    }

    protected void buildSkipCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerSkipCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.control(guildPlayerRegistry))
                .add();
    }

    protected void buildSeekCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerSeekCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.control(guildPlayerRegistry))
                .option(POSITION_SECONDS)
                .add();
    }

    protected void buildVolumeCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerVolumeCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.control(guildPlayerRegistry))
                .option(VOLUME)
                .add();
    }

    protected void buildQueueCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerQueueCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.control(guildPlayerRegistry))
                .option(PAGE)
                .add();
    }

    protected void buildClearQueueCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerClearQueueCommandExecutor.class)
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

    protected void buildLeaveCommand(LeafCommandBuilder<SubcommandsCommandBuilder> builder) {
        builder.onExecute(PlayerLeaveCommandExecutor.class)
                .restrict(PlayerCommandRestrictions.leave())
                .add();
    }

}
