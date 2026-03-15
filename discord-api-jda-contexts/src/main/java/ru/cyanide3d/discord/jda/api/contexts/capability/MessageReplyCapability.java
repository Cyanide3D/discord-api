package ru.cyanide3d.discord.jda.api.contexts.capability;

import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public interface MessageReplyCapability {

    void reply(String content);

    void reply(MessageCreateData message);

}