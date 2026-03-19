package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import ru.cyanide3d.discord.jda.api.contexts.ChannelEventContext;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.GuildEventContext;

public class DefaultGenericMessageEventContext<T extends GenericMessageEvent>
        extends AbstractMessageChannelSendEventContext<T>
        implements GuildEventContext<T>, ChannelEventContext<T> {

    public DefaultGenericMessageEventContext(T event, DiscordRestActionExecutor restActionExecutor) {
        super(event, restActionExecutor);
    }

    @Override
    public Guild getGuildOrNull() {
        return getEvent().isFromGuild()
                ? getEvent().getGuild()
                : null;
    }

    @Override
    public Channel getChannel() {
        return getEvent().getChannel();
    }

    @Override
    public MessageChannel getMessageChannel() {
        return getEvent().getChannel();
    }

}
