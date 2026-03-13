package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

public interface MessageReactionContext<T extends GenericMessageReactionEvent> extends MemberEventContext<T> {

    MessageChannel getChannel();

    EmojiUnion getEmoji();

    long getMessageIdLong();

}
