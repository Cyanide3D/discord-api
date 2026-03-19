package ru.cyanide3d.discord.jda.api.event;

import org.springframework.core.ResolvableType;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;

public interface DiscordJDAEventHandler<T extends EventContext<?>> {

    void onEvent(T event);

    default boolean supportEventContext(EventContext<?> ctx) {
        return getEventClass().isAssignableFrom(ctx.getClass());
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
