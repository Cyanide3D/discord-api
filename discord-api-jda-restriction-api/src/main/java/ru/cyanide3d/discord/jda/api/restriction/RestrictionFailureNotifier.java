package ru.cyanide3d.discord.jda.api.restriction;

import ru.cyanide3d.discord.jda.api.contexts.EventContext;

public interface RestrictionFailureNotifier {

    default void notify(EventContext<?> context, RestrictionResult result) {
        if (result.isDenied()) {
            notifyDenied(context, result);
        }
        if (result.isError()) {
            notifyError(context, result);
        }
    }

    void notifyDenied(EventContext<?> context, RestrictionResult result);

    void notifyError(EventContext<?> context, RestrictionResult result);

}
