package ru.cyanide3d.discord.jda.plugin.lavalink;

import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.loadbalancing.ILoadBalancer;
import dev.arbjerg.lavalink.client.loadbalancing.builtin.VoiceRegionPenaltyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDABotProperties;
import ru.cyanide3d.discord.jda.plugin.lavalink.property.DiscordJDALavalinkProperties;
import ru.cyanide3d.discord.jda.plugin.lavalink.property.LavalinkNode;

import java.util.List;

public class LavalinkClientFactoryImpl implements LavalinkClientFactory {

    @Autowired
    private DiscordJDABotProperties jdaProperties;

    @Autowired
    private DiscordJDALavalinkProperties lavalinkProperties;

    @Override
    public LavalinkClient create() {
        String token = jdaProperties.getBotToken();
        LavalinkClient client = new LavalinkClient(
                Helpers.getUserIdFromToken(token)
        );

        ILoadBalancer loadBalancer = client.getLoadBalancer();
        loadBalancer.addPenaltyProvider(new VoiceRegionPenaltyProvider());

        List<LavalinkNode> nodes = lavalinkProperties.getNodes();

        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalStateException("At least one lavalink node is required");
        }

        nodes.stream()
                        .map(LavalinkNode::buildNodeOptions)
                                .forEach(client::addNode);

        return client;
    }

}
