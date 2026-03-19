package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import ru.cyanide3d.discord.jda.api.contexts.MemberEventContext;

public class DefaultGenericGuildVoiceEventContext<T extends GenericGuildVoiceEvent>
        extends DefaultGenericGuildEventContext<T>
        implements MemberEventContext<T> {

    public DefaultGenericGuildVoiceEventContext(T event) {
        super(event);
    }

    @Override
    public Member getMemberOrNull() {
        return getEvent().getMember();
    }

    @Override
    public User getUser() {
        return getEvent().getMember().getUser();
    }

}
