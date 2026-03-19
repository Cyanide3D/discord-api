package ru.cyanide3d.discord.jda.api.event;

import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.core.ResolvableType;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;

public interface DiscordJDAEventHandler<T extends EventContext<?>> {

    void onEvent(T event);

    default Class<? extends GenericEvent> getSupportedJdaEventType() {
        return GenericEvent.class;
    }

    default boolean supports(GenericEvent event, EventContext<?> ctx) {
        return getSupportedJdaEventType().isInstance(event)
                && getEventClass().isAssignableFrom(ctx.getClass());
    }

    default Class<?> getEventClass() {
        return ResolvableType.forClass(getClass())
                .as(DiscordJDAEventHandler.class)
                .getGeneric(0)
                .resolve();
    }

    default Restriction<T> getRestriction() {
        return Restriction.allow();
    }

}
