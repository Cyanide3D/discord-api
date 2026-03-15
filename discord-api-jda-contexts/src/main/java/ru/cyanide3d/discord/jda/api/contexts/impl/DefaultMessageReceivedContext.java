package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.MessageReceivedContext;

public class DefaultMessageReceivedContext
        extends AbstractMessageChannelSendEventContext<MessageReceivedEvent>
        implements MessageReceivedContext {

    private final DiscordRestActionExecutor restActionExecutor;

    public DefaultMessageReceivedContext(MessageReceivedEvent event, DiscordRestActionExecutor restActionExecutor) {
        super(event, restActionExecutor);
        this.restActionExecutor = restActionExecutor;
    }

    @Override
    public MessageChannel getMessageChannel() {
        return getEvent().getChannel();
    }

    @Override
    public Message getMessage() {
        return getEvent().getMessage();
    }

    @Override
    public void reply(String content) {
        restActionExecutor.queue(getEvent().getMessage().reply(content), "message.reply");
    }

    @Override
    public void reply(MessageCreateData message) {
        restActionExecutor.queue(getEvent().getMessage().reply(message), "message.reply");
    }

    @Override
    public void addReaction(Emoji emoji) {
        restActionExecutor.queue(getEvent().getMessage().addReaction(emoji), "message.addReaction");
    }

    @Override
    public boolean isGuildMessage() {
        return getEvent().isFromGuild();
    }

    @Override
    public boolean isDirectMessage() {
        return getEvent().isFromType(ChannelType.PRIVATE);
    }

    @Override
    public Member getMemberOrNull() {
        return getEvent().getMember();
    }

    @Override
    public Guild getGuildOrNull() {
        return getEvent().getGuild();
    }

    @Override
    public Channel getChannel() {
        return getEvent().getChannel();
    }

    @Override
    public User getUser() {
        return getEvent().getAuthor();
    }
}
