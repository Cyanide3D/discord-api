package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.cyanide3d.discord.jda.api.contexts.MessageReceivedContext;

public class DefaultMessageReceivedContext
        extends AbstractJDAEventContext<MessageReceivedEvent>
        implements MessageReceivedContext {

    public DefaultMessageReceivedContext(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public boolean isFromGuild() {
        return getEvent().isFromGuild();
    }

    @Override
    public boolean isFromPrivate() {
        return !isFromGuild();
    }

    @Override
    public Message getMessage() {
        return getEvent().getMessage();
    }

    @Override
    public Channel getChannel() {
        return getEvent().getChannel();
    }

    @Override
    public User getUser() {
        return getEvent().getAuthor();
    }

    @Override
    public Member getMember() {
        return getEvent().getMember();
    }

    @Override
    public Guild getGuild() {
        return getEvent().getGuild();
    }
}
