package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageChannelSendCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.TypingCapability;

public interface MessageReactionAddContext
        extends MessageReactionContext<MessageReactionAddEvent>,
        MessageChannelSendCapability,
        TypingCapability {
}