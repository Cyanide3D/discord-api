package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface MessageReceivedContext extends MessageEventContext<MessageReceivedEvent>, MemberEventContext<MessageReceivedEvent> {

    boolean isFromGuild();

    boolean isFromPrivate();

}