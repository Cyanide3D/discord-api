package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import ru.cyanide3d.discord.jda.api.contexts.GuildMemberJoinContext;

public class DefaultGuildMemberJoinContext
        extends AbstractJDAEventContext<GuildMemberJoinEvent>
        implements GuildMemberJoinContext {

    public DefaultGuildMemberJoinContext(GuildMemberJoinEvent event) {
        super(event);
    }

    @Override
    public Member getMemberOrNull() {
        return getEvent().getMember();
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
