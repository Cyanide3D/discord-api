package ru.cyanide3d.discord.jda.plugin.lavalink.property;

import dev.arbjerg.lavalink.client.NodeOptions;
import lombok.Getter;

import java.net.URI;

@Getter
public class LavalinkNode {

    private String name;

    private Long httpTimeout;

    private String serverUri;

    private String password;

    public NodeOptions buildNodeOptions() {
        return new NodeOptions.Builder()
                .setName(getName())
                .setHttpTimeout(getHttpTimeout())
                .setServerUri(URI.create(getServerUri()))
                .setPassword(getPassword())
                .build();
    }

}
