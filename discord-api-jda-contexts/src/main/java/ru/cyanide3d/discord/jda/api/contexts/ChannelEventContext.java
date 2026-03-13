package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.GenericEvent;

public interface ChannelEventContext<E extends GenericEvent> extends JDAEventContext<E> {

    Channel getChannel();

}
