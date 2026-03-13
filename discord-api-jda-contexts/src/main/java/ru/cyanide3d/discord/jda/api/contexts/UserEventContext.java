package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;

public interface UserEventContext<E extends GenericEvent> extends JDAEventContext<E> {

    User getUser();

}