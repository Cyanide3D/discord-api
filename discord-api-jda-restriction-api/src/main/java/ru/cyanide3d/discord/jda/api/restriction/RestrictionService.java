package ru.cyanide3d.discord.jda.api.restriction;

import ru.cyanide3d.discord.jda.api.contexts.EventContext;

public interface RestrictionService {

    RestrictionResult check(Restriction<?> restriction, EventContext<?> eventContext);

}
