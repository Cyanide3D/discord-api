package ru.cyanide3d.discord.jda.api.restriction;

import ru.cyanide3d.discord.jda.api.contexts.EventContext;

public interface RestrictionFailureNotifier {

    void notify(RestrictionResult result, EventContext<?> context);

}
