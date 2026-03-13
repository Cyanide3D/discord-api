package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import ru.cyanide3d.discord.jda.api.contexts.JDAEventContext;

public abstract class AbstractJDAEventContext<T extends GenericEvent>
        extends AbstractEventContext<T>
        implements JDAEventContext<T> {

    public AbstractJDAEventContext(T event) {
        super(event);
    }

    @Override
    public JDA getJDA() {
        return getEvent().getJDA();
    }

}
