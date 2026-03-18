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
        if (getEvent().isAcknowledged()) {
            followup(content);
            return;
        }
        restActionExecutor.queue(getEvent().reply(content), "interaction.reply");
    }

    @Override
    public void reply(MessageCreateData message) {
        if (getEvent().isAcknowledged()) {
            followup(message);
            return;
        }
        restActionExecutor.queue(getEvent().reply(message), "interaction.reply");
    }

    @Override
    public void replyEphemeral(String content) {
        if (getEvent().isAcknowledged()) {
            followupEphemeral(content);
            return;
        }
        restActionExecutor.queue(getEvent().reply(content).setEphemeral(true), "interaction.reply.ephemeral");
    }

    @Override
    public void replyEphemeral(MessageCreateData message) {
        if (getEvent().isAcknowledged()) {
            followupEphemeral(message);
            return;
        }
        restActionExecutor.queue(getEvent().reply(message).setEphemeral(true), "interaction.reply.ephemeral");
    }

    @Override
    public void deferReply() {
        if (getEvent().isAcknowledged()) {
            return;
        }
        restActionExecutor.queue(getEvent().deferReply(), "interaction.deferReply");
    }

    @Override
    public void deferReplyEphemeral() {
        if (getEvent().isAcknowledged()) {
            return;
        }
        restActionExecutor.queue(getEvent().deferReply(true), "interaction.deferReply");
    }

    @Override
    public boolean isAcknowledged() {
        return getEvent().isAcknowledged();
    }

    @Override
    public void followup(String content) {
        if (!getEvent().isAcknowledged()) {
            throw new IllegalStateException("Cannot send followup before interaction acknowledgement");
        }

        restActionExecutor.queue(getEvent().getHook().sendMessage(content), "interaction.followup");
    }

    @Override
    public void followup(MessageCreateData content) {
        if (!getEvent().isAcknowledged()) {
            throw new IllegalStateException("Cannot send followup before interaction acknowledgement");
        }

        restActionExecutor.queue(getEvent().getHook().sendMessage(content), "interaction.followup");
    }

    @Override
    public void followupEphemeral(String content) {
        if (!getEvent().isAcknowledged()) {
            throw new IllegalStateException("Cannot send followup before interaction acknowledgement");
        }

        restActionExecutor.queue(getEvent().getHook().sendMessage(content).setEphemeral(true), "interaction.followupEphemeral");
    }

    @Override
    public void followupEphemeral(MessageCreateData content) {
        if (!getEvent().isAcknowledged()) {
            throw new IllegalStateException("Cannot send followup before interaction acknowledgement");
        }

        restActionExecutor.queue(getEvent().getHook().sendMessage(content).setEphemeral(true), "interaction.followupEphemeral");
    }

    @Override
    public IReplyCallback getReplyCallback() {
        return getEvent();
    }
}