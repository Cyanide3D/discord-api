package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;

public interface MemberEventContext<E extends GenericEvent> extends GuildEventContext<E>, UserEventContext<E> {

    Member getMember();

}