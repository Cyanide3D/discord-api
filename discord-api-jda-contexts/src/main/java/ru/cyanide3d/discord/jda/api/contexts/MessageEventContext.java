package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;

public interface MessageEventContext<E extends GenericEvent> extends ChannelEventContext<E> {

    Message getMessage();

}