package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import lombok.experimental.UtilityClass;
import ru.cyanide3d.discord.jda.api.command.OptionSpec;

@UtilityClass
public class PlayerCommandSpec {

    public static final OptionSpec<String> QUERY = OptionSpec.string("query", "URL или название песни").required();

    public static final OptionSpec<Long> POSITION_SECONDS = OptionSpec.integer("seconds", "Позиция в секундах").required();

    public static final OptionSpec<Long> VOLUME = OptionSpec.integer("volume", "Громкость от 0 до 1000").required();

    public static final OptionSpec<Long> PAGE = OptionSpec.integer("page", "Номер страницы очереди").optional();

}
