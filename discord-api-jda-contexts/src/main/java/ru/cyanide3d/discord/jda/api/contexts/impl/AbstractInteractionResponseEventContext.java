package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.capability.InteractionResponseCapability;

public abstract class AbstractInteractionResponseEventContext<T extends GenericInteractionCreateEvent & IReplyCallback>
        extends AbstractInteractionEventContext<T>
        implements InteractionResponseCapability {

    private final DiscordRestActionExecutor restActionExecutor;

    protected AbstractInteractionResponseEventContext(T event, DiscordRestActionExecutor restActionExecutor) {
        super(event);
        this.restActionExecutor = restActionExecutor;
    }

    @Override
    public void reply(String content) {
        restActionExecutor.queue(getEvent().reply(content), getClass().getName());
    }

    @Override
    public void reply(MessageCreateData message) {
        restActionExecutor.queue(getEvent().reply(message), getClass().getName());
    }

    @Override
    public void replyEphemeral(String content) {
        restActionExecutor.queue(getEvent().reply(content).setEphemeral(true), getClass().getName());
    }

    @Override
    public void replyEphemeral(MessageCreateData message) {
        restActionExecutor.queue(getEvent().reply(message).setEphemeral(true), getClass().getName());
    }

    @Override
    public void deferReply() {
        restActionExecutor.queue(getEvent().deferReply(), getClass().getName());
    }

    @Override
    public void deferReply(boolean ephemeral) {
        restActionExecutor.queue(getEvent().deferReply(ephemeral), getClass().getName());
    }

    @Override
    public IReplyCallback getReplyCallback() {
        return getEvent();
    }
}