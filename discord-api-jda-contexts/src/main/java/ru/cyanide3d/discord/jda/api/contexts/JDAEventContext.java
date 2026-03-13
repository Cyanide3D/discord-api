package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;

public interface JDAEventContext<E extends GenericEvent> extends EventContext<E> {

    JDA getJDA();

}