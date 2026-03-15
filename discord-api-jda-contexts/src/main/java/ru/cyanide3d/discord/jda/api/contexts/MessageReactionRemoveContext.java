package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageChannelSendCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.TypingCapability;

public interface MessageReactionRemoveContext
        extends MessageReactionContext<MessageReactionRemoveEvent>,
        MessageChannelSendCapability,
        TypingCapability {
}