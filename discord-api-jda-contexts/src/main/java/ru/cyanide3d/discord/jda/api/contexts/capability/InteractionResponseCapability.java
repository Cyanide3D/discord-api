package ru.cyanide3d.discord.jda.api.contexts.capability;

import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public interface InteractionResponseCapability {

    void reply(String content);

    void reply(MessageCreateData message);

    void replyEphemeral(String content);

    void replyEphemeral(MessageCreateData message);

    void deferReply();

    void deferReplyEphemeral();

    boolean isAcknowledged();

    void followup(String content);

    void followup(MessageCreateData content);

    void followupEphemeral(MessageCreateData content);

    void followupEphemeral(String content);

    IReplyCallback getReplyCallback();

}