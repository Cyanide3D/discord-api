package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.GenericEvent;

public interface EventContext<T extends GenericEvent> {

    T getEvent();

}
