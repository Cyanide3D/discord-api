package ru.cyanide3d.discord.jda.plugin.lavalink.autoconfiguration;

import dev.arbjerg.lavalink.client.LavalinkClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.cyanide3d.discord.jda.plugin.lavalink.BotVoiceChannelConnector;
import ru.cyanide3d.discord.jda.plugin.lavalink.BotVoiceChannelConnectorImpl;
import ru.cyanide3d.discord.jda.plugin.lavalink.DiscordJDALavalinkCustomizer;
import ru.cyanide3d.discord.jda.plugin.lavalink.LavalinkClientFactory;
import ru.cyanide3d.discord.jda.plugin.lavalink.LavalinkClientFactoryImpl;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.DiscordJdaLavalinkSlashCommandRegistryCustomizer;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerClearQueueCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerPauseCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerPlayCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerQueueCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerResumeCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerSeekCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerSkipCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerStopCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerVolumeCommandExecutor;
import ru.cyanide3d.discord.jda.plugin.lavalink.event.LeaveGuildBotEvent;
import ru.cyanide3d.discord.jda.plugin.lavalink.event.VoiceChannelLeftBotEvent;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.GuildPlayerRegistry;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.LavalinkShutdownCleanup;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.LavalinkTrackQueueListener;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManagerImpl;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerResultMessageFormatter;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerResultMessageFormatterImpl;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.TrackResolver;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.TrackResolverImpl;
import ru.cyanide3d.discord.jda.plugin.lavalink.property.DiscordJDALavalinkProperties;
import ru.cyanide3d.discord.jda.plugin.lavalink.property.DiscordJDALavalinkPropertiesImpl;

@AutoConfiguration
public class DiscordJDALavalinkAutoConfiguration {

    @Bean
    public DiscordJDALavalinkCustomizer discordJDALavalinkCustomizer() {
        return new DiscordJDALavalinkCustomizer();
    }

    @Bean
    public LavalinkClient lavalinkClient(LavalinkClientFactory lavalinkClientFactory) {
        return lavalinkClientFactory.create();
    }

    @Bean
    @ConditionalOnMissingBean
    public LavalinkClientFactory lavalinkClientFactory() {
        return new LavalinkClientFactoryImpl();
    }

    @Bean
    @ConfigurationProperties(prefix = "discord.jda.lavalink")
    public DiscordJDALavalinkProperties discordJDALavalinkProperties() {
        return new DiscordJDALavalinkPropertiesImpl();
    }

    @Bean
    public DiscordJdaLavalinkSlashCommandRegistryCustomizer discordJdaLavalinkSlashCommandRegistryCustomizer() {
        return new DiscordJdaLavalinkSlashCommandRegistryCustomizer();
    }

    @Bean
    public PlayerPlayCommandExecutor playerPlayCommandExecutor() {
        return new PlayerPlayCommandExecutor();
    }

    @Bean
    public PlayerStopCommandExecutor playerStopCommandExecutor() {
        return new PlayerStopCommandExecutor();
    }

    @Bean
    public PlayerPauseCommandExecutor playerPauseCommandExecutor() {
        return new PlayerPauseCommandExecutor();
    }

    @Bean
    public PlayerManager playerManager() {
        return new PlayerManagerImpl();
    }

    @Bean
    public PlayerResultMessageFormatter  playerResultMessageFormatter() {
        return new PlayerResultMessageFormatterImpl();
    }

    @Bean
    public TrackResolver  trackResolver() {
        return new TrackResolverImpl();
    }

    @Bean
    public GuildPlayerRegistry  guildPlayerRegistry() {
        return new GuildPlayerRegistry();
    }

    @Bean
    public BotVoiceChannelConnector botVoiceChannelConnector() {
        return new BotVoiceChannelConnectorImpl();
    }

    @Bean
    public VoiceChannelLeftBotEvent voiceChannelLeftBotEvent() {
        return new VoiceChannelLeftBotEvent();
    }

    @Bean
    public LavalinkTrackQueueListener lavalinkTrackQueueListener() {
        return new LavalinkTrackQueueListener();
    }

    @Bean
    public LeaveGuildBotEvent  leaveGuildBotEvent() {
        return new LeaveGuildBotEvent();
    }

    @Bean
    public LavalinkShutdownCleanup lavalinkShutdownCleanup() {
        return new LavalinkShutdownCleanup();
    }

    @Bean
    public PlayerClearQueueCommandExecutor playerClearQueueCommandExecutor() {
        return new PlayerClearQueueCommandExecutor();
    }

    @Bean
    public PlayerQueueCommandExecutor playerQueueCommandExecutor() {
        return new PlayerQueueCommandExecutor();
    }

    @Bean
    public PlayerResumeCommandExecutor playerResumeCommandExecutor() {
        return new PlayerResumeCommandExecutor();
    }

    @Bean
    public PlayerSeekCommandExecutor playerSeekCommandExecutor() {
        return new PlayerSeekCommandExecutor();
    }

    @Bean
    public PlayerSkipCommandExecutor playerSkipCommandExecutor() {
        return new PlayerSkipCommandExecutor();
    }

    @Bean
    public PlayerVolumeCommandExecutor playerVolumeCommandExecutor() {
        return new PlayerVolumeCommandExecutor();
    }
}
