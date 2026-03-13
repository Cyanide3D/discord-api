package ru.cyanide3d.discord.jda.plugin.lavalink.autoconfiguration;

import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.client.loadbalancing.ILoadBalancer;
import dev.arbjerg.lavalink.client.loadbalancing.builtin.VoiceRegionPenaltyProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDABotProperties;
import ru.cyanide3d.discord.jda.plugin.lavalink.DiscordJDALavalinkCustomizer;

import java.net.URI;

@AutoConfiguration
public class DiscordJDALavalinkAutoConfiguration {

    @Bean
    public DiscordJDALavalinkCustomizer discordJDALavalinkCustomizer() {
        return new DiscordJDALavalinkCustomizer();
    }

    @Bean
    public LavalinkClient lavalinkClient(DiscordJDABotProperties properties) {
        String token = properties.getBotToken();
        LavalinkClient client = new LavalinkClient(
                Helpers.getUserIdFromToken(token)
        );

        ILoadBalancer loadBalancer = client.getLoadBalancer();
        loadBalancer.addPenaltyProvider(new VoiceRegionPenaltyProvider());

        NodeOptions node = new NodeOptions.Builder()
                .setName("local")
                .setHttpTimeout(5000L)
                .setServerUri(URI.create("ws://127.0.0.1:2333"))
                .setPassword("wwwwww")
                .build();

        client.addNode(node);

        return client;
    }

}
