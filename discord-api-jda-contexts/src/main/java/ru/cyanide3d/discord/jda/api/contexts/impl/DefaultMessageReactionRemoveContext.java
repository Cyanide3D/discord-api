package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import ru.cyanide3d.discord.jda.api.contexts.MessageReactionRemoveContext;

public class DefaultMessageReactionRemoveContext
        extends AbstractMessageReactionContext<MessageReactionRemoveEvent>
        implements MessageReactionRemoveContext {

    public DefaultMessageReactionRemoveContext(MessageReactionRemoveEvent event) {
        super(event);
    }

}
