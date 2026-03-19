package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import ru.cyanide3d.discord.jda.api.contexts.UserEventContext;

public class DefaultGenericUserEventContext<T extends GenericUserEvent>
        extends AbstractJDAEventContext<T>
        implements UserEventContext<T> {

    public DefaultGenericUserEventContext(T event) {
        super(event);
    }

    @Override
    public User getUser() {
        return getEvent().getUser();
    }

}
