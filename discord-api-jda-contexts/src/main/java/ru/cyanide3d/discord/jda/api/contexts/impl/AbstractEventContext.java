package ru.cyanide3d.discord.jda.api.contexts.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.events.GenericEvent;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;

@AllArgsConstructor
@Getter
public abstract class AbstractEventContext<T extends GenericEvent> implements EventContext<T> {

    private final T event;

}
