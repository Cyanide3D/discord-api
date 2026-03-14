package ru.cyanide3d.discord.jda.plugin.lavalink.property;

import lombok.Getter;

import java.util.List;

public class DiscordJDALavalinkPropertiesImpl implements DiscordJDALavalinkProperties {

    @Getter
    private List<LavalinkNode> nodes;

}
