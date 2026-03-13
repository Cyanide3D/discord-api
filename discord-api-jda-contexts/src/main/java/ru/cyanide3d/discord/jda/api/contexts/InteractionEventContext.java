package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public interface InteractionEventContext<T extends GenericInteractionCreateEvent>
        extends ChannelEventContext<T>, GuildEventContext<T>, MemberEventContext<T>, UserEventContext<T> {


}