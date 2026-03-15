package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageChannelSendCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.TypingCapability;

public abstract class AbstractMessageChannelSendEventContext<T extends GenericEvent>
        extends AbstractJDAEventContext<T>
        implements MessageChannelSendCapability, TypingCapability {

    private final DiscordRestActionExecutor restActionExecutor;

    protected AbstractMessageChannelSendEventContext(T event, DiscordRestActionExecutor restActionExecutor) {
        super(event);
        this.restActionExecutor = restActionExecutor;
    }

    @Override
    public void sendMessage(String content) {
        restActionExecutor.queue(getMessageChannel().sendMessage(content), getClass().getName());
    }

    @Override
    public void sendMessage(MessageCreateData message) {
        restActionExecutor.queue(getMessageChannel().sendMessage(message), getClass().getName());
    }

    @Override
    public void sendTyping() {
        restActionExecutor.queue(getMessageChannel().sendTyping(), getClass().getName());
    }

    @Override
    public abstract MessageChannel getMessageChannel();
}