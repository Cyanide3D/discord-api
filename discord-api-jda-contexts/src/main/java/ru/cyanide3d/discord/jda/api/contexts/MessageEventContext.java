package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

public interface MessageEventContext<E extends GenericMessageEvent> extends ChannelEventContext<E> {

    Message getMessage();

}