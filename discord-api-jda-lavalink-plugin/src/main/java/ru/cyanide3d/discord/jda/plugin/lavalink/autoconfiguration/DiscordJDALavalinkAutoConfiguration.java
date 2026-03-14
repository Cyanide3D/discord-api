package ru.cyanide3d.discord.jda.plugin.lavalink.autoconfiguration;

import dev.arbjerg.lavalink.client.LavalinkClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.cyanide3d.discord.jda.plugin.lavalink.DiscordJDALavalinkCustomizer;
import ru.cyanide3d.discord.jda.plugin.lavalink.LavalinkClientFactory;
import ru.cyanide3d.discord.jda.plugin.lavalink.LavalinkClientFactoryImpl;
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

}
