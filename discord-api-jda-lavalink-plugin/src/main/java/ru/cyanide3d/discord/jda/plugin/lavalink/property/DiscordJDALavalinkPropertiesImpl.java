package ru.cyanide3d.discord.jda.plugin.lavalink.property;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiscordJDALavalinkPropertiesImpl implements DiscordJDALavalinkProperties {

    private List<LavalinkNode> nodes;

}
