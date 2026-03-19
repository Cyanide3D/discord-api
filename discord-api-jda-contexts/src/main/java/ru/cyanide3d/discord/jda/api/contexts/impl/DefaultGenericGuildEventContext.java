package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import ru.cyanide3d.discord.jda.api.contexts.GuildEventContext;

public class DefaultGenericGuildEventContext<T extends GenericGuildEvent>
        extends AbstractJDAEventContext<T>
        implements GuildEventContext<T> {

    public DefaultGenericGuildEventContext(T event) {
        super(event);
    }

    @Override
    public Guild getGuildOrNull() {
        return getEvent().getGuild();
    }

}
