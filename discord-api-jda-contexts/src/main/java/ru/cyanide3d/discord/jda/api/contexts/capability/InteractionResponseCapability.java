package ru.cyanide3d.discord.jda.api.contexts.capability;

import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public interface InteractionResponseCapability {

    void reply(String content);

    void reply(MessageCreateData message);

    void replyEphemeral(String content);

    void replyEphemeral(MessageCreateData message);

    void deferReply();

    void deferReply(boolean ephemeral);

    IReplyCallback getReplyCallback();

}