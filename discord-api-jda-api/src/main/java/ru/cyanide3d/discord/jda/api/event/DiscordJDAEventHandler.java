package ru.cyanide3d.discord.jda.api.event;

import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.core.ResolvableType;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;

public interface DiscordJDAEventHandler<T extends GenericEvent> {

    void onEvent(T event);

    default boolean supportEventType(Object event) {
        return getEventClass().equals(event.getClass());
    }

    default Class<?> getEventClass() {
        return ResolvableType.forClass(getClass())
                .as(DiscordJDAEventHandler.class)
                .getGeneric(0)
                .resolve();
    }

    default Restriction<?> getRestriction() {
        return null;
    }

}
