package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;

public interface GuildEventContext<E extends GenericEvent> extends JDAEventContext<E> {

    Guild getGuildOrNull();

    default boolean hasGuild() {
        return getGuildOrNull() != null;
    }

    default Guild requireGuild() {
        Guild guild = getGuildOrNull();
        if (guild == null) {
            throw new IllegalStateException("Event is not guild-scoped");
        }
        return guild;
    }

}