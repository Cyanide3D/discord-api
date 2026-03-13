package ru.cyanide3d.discord.jda.autoconfiguration;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import ru.cyanide3d.discord.jda.api.AutoConfiguredDiscordBot;
import ru.cyanide3d.discord.jda.api.AutoEnabledEventListener;
import ru.cyanide3d.discord.jda.api.DiscordJDABotCustomizer;
import ru.cyanide3d.discord.jda.api.TrackingChunkingFilter;
import ru.cyanide3d.discord.jda.api.TrackingMemberCachePolicy;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDABotProperties;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDAPresenceProperties;
import ru.cyanide3d.discord.jda.configuration.DiscordJDAConfiguration;
import ru.cyanide3d.discord.jda.event.DiscordJDAEventManager;
import ru.cyanide3d.discord.jda.restriction.configuration.DiscordJDARestrictionConfiguration;

import java.util.List;
import java.util.concurrent.ExecutorService;

@AutoConfiguration
@ConditionalOnProperty(value = "discord.jda.enabled", havingValue = "true", matchIfMissing = true)
@Import({
        DiscordJDARestrictionConfiguration.class,
        DiscordJDAConfiguration.class
})
@Slf4j
public class DiscordJDABotAutoConfiguration {

    @Autowired
    private ObjectProvider<DiscordJDABotCustomizer> discordBotCustomizers;

    @Bean
    @ConditionalOnMissingBean
    @AutoConfiguredDiscordBot
    public JDA discordBot(DiscordJDABotProperties properties,
                          DiscordJDAEventManager discordJDAEventManager,
                          TrackingMemberCachePolicy trackingMemberCachePolicy,
                          TrackingChunkingFilter trackingChunkingFilter,
                          ObjectProvider<AutoEnabledEventListener> autoEnabledEventListeners,
                          @Qualifier("eventExecutor") ExecutorService eventExecutor,
                          @Qualifier("discordHttpClient") ObjectProvider<OkHttpClient> httpClientProvider,
                          @Qualifier("discordHttpClientBuilder") ObjectProvider<OkHttpClient.Builder> httpClientBuilderProvider) {
        log.info("Starting automatic discord jda bot configuration");

        DiscordJDAPresenceProperties presence = properties.getPresence();

        JDABuilder builder = JDABuilder.createDefault(properties.getBotToken())
                .setEventManager(discordJDAEventManager)
                .setAutoReconnect(properties.isAutoReconnect())
                .setMaxReconnectDelay(properties.getMaxReconnectDelay())
                .setEventPool(eventExecutor, true)
                .enableIntents(properties.getGatewayIntents())
                .disableCache(properties.getDisabledCacheFlags())
                .setRequestTimeoutRetry(properties.isRequestTimeoutRetry())
                .setHttpClient(httpClientProvider.getIfAvailable())
                .setHttpClientBuilder(httpClientBuilderProvider.getIfAvailable())
                //TODO внутри какие то дебильные проверки на !=, с этим надо поосторожнее.
                .setMemberCachePolicy(trackingMemberCachePolicy)
                .setChunkingFilter(trackingChunkingFilter)
                .setStatus(presence.getStatus())
                .setActivity(presence.toJdaActivity())
                .enableCache(properties.getEnabledCacheFlags());

        log.info("""
                \nAuto-reconnect: {}
                Max reconnect delay: {}
                Enabled gateway intents: {}
                Enabled cache flags: {}
                Disabled cache flags: {}
                Request timeout retry: {}
                """, properties.isAutoReconnect(), properties.getMaxReconnectDelay(),
                String.join(",", properties.getStringGatewayIntents()),
                String.join(",", properties.getStringEnabledCacheFlags()),
                String.join(",", properties.getStringDisabledCacheFlags()),
                properties.isRequestTimeoutRetry());

        log.info("End discord jda bot configuration");

        autoEnabledEventListeners.forEach(builder::addEventListeners);

        return buildAndConfigureJDA(builder);
    }

    private JDA buildAndConfigureJDA(JDABuilder builder) {
        List<DiscordJDABotCustomizer> customizers = discordBotCustomizers.stream().toList();
        log.info("Found {} discord jda bot customizers", customizers.size());

        customizers.forEach(customizer -> customizer.customize(builder));

        log.info("Starting discord jda bot...");
        JDA jda = builder.build();
        log.info("Discord jda bot was started.");

        customizers.forEach(customizer -> customizer.customize(jda));

        return jda;
    }

}
