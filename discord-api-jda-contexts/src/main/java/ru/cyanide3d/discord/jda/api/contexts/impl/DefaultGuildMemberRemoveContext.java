package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import ru.cyanide3d.discord.jda.api.contexts.GuildMemberRemoveContext;

public class DefaultGuildMemberRemoveContext
        extends AbstractJDAEventContext<GuildMemberRemoveEvent>
        implements GuildMemberRemoveContext {

    public DefaultGuildMemberRemoveContext(GuildMemberRemoveEvent event) {
        super(event);
    }

    @Override
    public Guild getGuildOrNull() {
        return getEvent().getGuild();
    }

    @Override
    public User getUser() {
        return getEvent().getUser();
    }

}
