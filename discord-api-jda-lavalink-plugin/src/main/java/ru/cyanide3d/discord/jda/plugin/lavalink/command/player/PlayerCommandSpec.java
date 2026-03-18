package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import lombok.experimental.UtilityClass;
import ru.cyanide3d.discord.jda.api.command.OptionSpec;

@UtilityClass
public class PlayerCommandSpec {

    public static final OptionSpec<String> QUERY = OptionSpec.string("query", "URL или название песни").required();

}
