package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import ru.cyanide3d.discord.jda.api.contexts.InteractionEventContext;


public abstract class AbstractInteractionEventContext<T extends GenericInteractionCreateEvent>
        extends AbstractEventContext<T>
        implements InteractionEventContext<T> {

    public AbstractInteractionEventContext(T event) {
        super(event);
    }

    @Override
    public Channel getChannel() {
        return getEvent().getChannel();
    }

    @Override
    public Guild getGuild() {
        return getEvent().getGuild();
    }

    @Override
    public Member getMember() {
        return getEvent().getMember();
    }

    @Override
    public User getUser() {
        return getEvent().getUser();
    }

}
