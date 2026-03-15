package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.MessageReactionAddContext;

public class DefaultMessageReactionAddContext
        extends AbstractMessageReactionContext<MessageReactionAddEvent>
        implements MessageReactionAddContext {

    public DefaultMessageReactionAddContext(MessageReactionAddEvent event, DiscordRestActionExecutor restActionExecutor) {
        super(event, restActionExecutor);
    }

    @Override
    public MessageChannel getMessageChannel() {
        return getEvent().getChannel();
    }
}