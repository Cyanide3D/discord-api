package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import ru.cyanide3d.discord.jda.api.contexts.GuildLeaveContext;

public class DefaultGuildLeaveContext
        extends DefaultGenericGuildEventContext<GuildLeaveEvent>
        implements GuildLeaveContext {

    public DefaultGuildLeaveContext(GuildLeaveEvent event) {
        super(event);
    }

}
