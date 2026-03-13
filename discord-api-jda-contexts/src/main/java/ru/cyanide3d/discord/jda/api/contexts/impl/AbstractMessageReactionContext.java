package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import ru.cyanide3d.discord.jda.api.contexts.MessageReactionContext;

public abstract class AbstractMessageReactionContext<T extends GenericMessageReactionEvent>
        extends AbstractJDAEventContext<T>
        implements MessageReactionContext<T> {

    public AbstractMessageReactionContext(T event) {
        super(event);
    }

    @Override
    public MessageChannel getChannel() {
        return getEvent().getChannel();
    }

    @Override
    public EmojiUnion getEmoji() {
        return getEvent().getEmoji();
    }

    @Override
    public long getMessageIdLong() {
        return getEvent().getMessageIdLong();
    }

    @Override
    public Member getMember() {
        return getEvent().getMember();
    }

    @Override
    public Guild getGuild() {
        return getEvent().getGuild();
    }

    @Override
    public User getUser() {
        return getEvent().getUser();
    }

}
