package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.GenericEvent;

public interface EventContextFactory {

    <T extends GenericEvent> EventContext<T> create(T event);

}
