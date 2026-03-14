package ru.cyanide3d.discord.jda.autoconfiguration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import ru.cyanide3d.discord.jda.TrackingChunkingFilterImpl;
import ru.cyanide3d.discord.jda.TrackingMemberCachePolicyImpl;
import ru.cyanide3d.discord.jda.api.AutoConfiguredDiscordBot;
import ru.cyanide3d.discord.jda.api.TrackingChunkingFilter;
import ru.cyanide3d.discord.jda.api.TrackingMemberCachePolicy;
import ru.cyanide3d.discord.jda.api.contexts.configuration.DiscordJDAContextsConfiguration;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDABotProperties;
import ru.cyanide3d.discord.jda.command.configuration.DiscordJDASlashCommandConfiguration;
import ru.cyanide3d.discord.jda.configuration.DiscordJDAEventHandlerConfiguration;
import ru.cyanide3d.discord.jda.event.DiscordJDAEventManager;
import ru.cyanide3d.discord.jda.properties.DiscordJDABotPropertiesImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoConfiguration(after = DiscordJDABotAutoConfiguration.class)
@ConditionalOnBean(annotation = AutoConfiguredDiscordBot.class)
@Import({
        DiscordJDAEventHandlerConfiguration.class,
        DiscordJDASlashCommandConfiguration.class,
        DiscordJDAContextsConfiguration.class
})
public class DiscordJDAAutoconfiguredBotAutoConfiguration {

    @Bean
    @Qualifier("eventExecutor")
    public ExecutorService eventExecutorService() {
        return Executors.newFixedThreadPool(8);
    }

    @Bean
    @ConditionalOnMissingBean
    public TrackingMemberCachePolicy trackingMemberCachePolicy() {
        return new TrackingMemberCachePolicyImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public TrackingChunkingFilter trackingChunkingFilter() {
        return new TrackingChunkingFilterImpl();
    }

    @Bean
    @ConfigurationProperties(prefix = "discord.jda")
    public DiscordJDABotProperties discordBotProperties() {
        return new DiscordJDABotPropertiesImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public DiscordJDAEventManager discordJDAEventManager(DiscordJDABotProperties properties) {
        return new DiscordJDAEventManager(properties.getSlowEventThreshold());
    }

}
