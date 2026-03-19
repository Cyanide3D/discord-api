package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import ru.cyanide3d.discord.jda.api.contexts.GuildJoinContext;

public class DefaultGuildJoinContext
        extends DefaultGenericGuildEventContext<GuildJoinEvent>
        implements GuildJoinContext {

    public DefaultGuildJoinContext(GuildJoinEvent event) {
        super(event);
    }

}
