package ru.cyanide3d.discord.jda.autoconfiguration;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
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
import ru.cyanide3d.discord.jda.api.properties.DiscordJDABuilderMode;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDAPresenceProperties;
import ru.cyanide3d.discord.jda.event.DiscordJDAEventManager;
import ru.cyanide3d.discord.jda.restriction.configuration.DiscordJDARestrictionConfiguration;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

@AutoConfiguration
@ConditionalOnProperty(value = "discord.jda.enabled", havingValue = "true", matchIfMissing = true)
@Import({
        DiscordJDARestrictionConfiguration.class
})
@Slf4j
public class DiscordJDABotAutoConfiguration {

    @Autowired
    private ObjectProvider<DiscordJDABotCustomizer> discordBotCustomizers;

    @Bean
    @ConditionalOnMissingBean
    @AutoConfiguredDiscordBot
    public JDA discordBot(
            DiscordJDABotProperties properties,
            DiscordJDAEventManager discordJDAEventManager,
            TrackingMemberCachePolicy trackingMemberCachePolicy,
            TrackingChunkingFilter trackingChunkingFilter,
            ObjectProvider<AutoEnabledEventListener> autoEnabledEventListeners,
            @Qualifier("eventExecutor") ExecutorService eventExecutor,
            @Qualifier("discordHttpClient") ObjectProvider<OkHttpClient> httpClientProvider,
            @Qualifier("discordHttpClientBuilder") ObjectProvider<OkHttpClient.Builder> httpClientBuilderProvider
    ) {
        log.info("Starting automatic discord jda bot configuration");

        DiscordJDAPresenceProperties presence = properties.getPresence();
        JDABuilder builder = createBuilder(properties);

        builder.setEventManager(discordJDAEventManager)
                .setAutoReconnect(properties.isAutoReconnect())
                .setMaxReconnectDelay(properties.getMaxReconnectDelay())
                .setEventPool(eventExecutor, true)
                .setRequestTimeoutRetry(properties.isRequestTimeoutRetry())
                .setStatus(presence.getStatus())
                .setActivity(presence.toJdaActivity());

        applyHttpConfiguration(builder, httpClientProvider.getIfAvailable(), httpClientBuilderProvider.getIfAvailable());

        if (properties.getBuilderMode() == DiscordJDABuilderMode.DEFAULT) {
            builder.enableIntents(properties.getGatewayIntents())
                    .disableCache(properties.getDisabledCacheFlags())
                    .enableCache(properties.getEnabledCacheFlags())
                    .setMemberCachePolicy(trackingMemberCachePolicy)
                    .setChunkingFilter(trackingChunkingFilter);
        } else {
            log.info("Discord JDA builder mode is LIGHT, cache/chunking/member-cache settings are skipped");
        }

        log.info("""
                \nBuilder mode: {}
                Event executor threads: {}
                Auto-reconnect: {}
                Max reconnect delay: {}
                Enabled gateway intents: {}
                Enabled cache flags: {}
                Disabled cache flags: {}
                Request timeout retry: {}
                """,
                properties.getBuilderMode(),
                properties.getEventExecutorThreads(),
                properties.isAutoReconnect(),
                properties.getMaxReconnectDelay(),
                String.join(",", properties.getStringGatewayIntents()),
                String.join(",", properties.getStringEnabledCacheFlags()),
                String.join(",", properties.getStringDisabledCacheFlags()),
                properties.isRequestTimeoutRetry());

        autoEnabledEventListeners.forEach(builder::addEventListeners);

        return buildAndConfigureJDA(builder);
    }

    private JDABuilder createBuilder(DiscordJDABotProperties properties) {
        if (properties.getBuilderMode() == DiscordJDABuilderMode.LIGHT) {
            return createLightBuilder(properties.getBotToken(), properties.getGatewayIntents());
        }

        return JDABuilder.createDefault(properties.getBotToken());
    }

    private JDABuilder createLightBuilder(String token, Collection<GatewayIntent> intents) {
        try {
            Method collectionFactory = JDABuilder.class.getMethod("createLight", String.class, Collection.class);
            return (JDABuilder) collectionFactory.invoke(null, token, intents);
        } catch (NoSuchMethodException ignored) {
            try {
                Method arrayFactory = JDABuilder.class.getMethod("createLight", String.class, GatewayIntent[].class);
                GatewayIntent[] array = intents.toArray(new GatewayIntent[0]);
                return (JDABuilder) arrayFactory.invoke(null, token, (Object) array);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to create JDABuilder in LIGHT mode", e);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create JDABuilder in LIGHT mode", e);
        }
    }

    private void applyHttpConfiguration(JDABuilder builder, OkHttpClient httpClient, OkHttpClient.Builder httpClientBuilder) {
        if (httpClient != null && httpClientBuilder != null) {
            throw new IllegalStateException(
                    "Only one of 'discordHttpClient' or 'discordHttpClientBuilder' may be defined"
            );
        }

        if (httpClient != null) {
            builder.setHttpClient(httpClient);
            return;
        }

        if (httpClientBuilder != null) {
            builder.setHttpClientBuilder(httpClientBuilder);
        }
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