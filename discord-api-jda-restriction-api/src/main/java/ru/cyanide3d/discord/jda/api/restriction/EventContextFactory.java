package ru.cyanide3d.discord.jda.api.restriction;

import net.dv8tion.jda.api.events.GenericEvent;
import ru.cyanide3d.discord.jda.api.restriction.contexts.EventContext;

public interface EventContextFactory {

    EventContext create(GenericEvent event);

}
