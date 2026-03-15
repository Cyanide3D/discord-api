package ru.cyanide3d.discord.jda.api.contexts.capability;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public interface MessageChannelSendCapability {

    MessageChannel getMessageChannel();

    void sendMessage(String content);

    void sendMessage(MessageCreateData message);

}