package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.GenericEvent;
import ru.cyanide3d.discord.jda.api.contexts.JDAEventContext;

public class DefaultJDAEventContext<E extends GenericEvent>
        extends AbstractJDAEventContext<E>
        implements JDAEventContext<E> {

    public DefaultJDAEventContext(E event) {
        super(event);
    }

}