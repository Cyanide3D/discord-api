package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageChannelSendCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageReactionCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageReplyCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.TypingCapability;

public interface MessageReceivedContext
        extends MessageEventContext<MessageReceivedEvent>,
        MemberEventContext<MessageReceivedEvent>,
        MessageChannelSendCapability,
        MessageReplyCapability,
        MessageReactionCapability,
        TypingCapability {

    boolean isFromGuild();

    boolean isFromPrivate();

}