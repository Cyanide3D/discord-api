package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public interface InteractionEventContext<T extends GenericInteractionCreateEvent> extends EventContext<T> {

    Channel getChannel();

    Guild getGuild();

    Member getMember();

    User getUser();

}
