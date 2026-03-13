package ru.cyanide3d.discord.jda.api.restriction;

import ru.cyanide3d.discord.jda.api.restriction.contexts.EventContext;

import java.util.Arrays;
import java.util.Objects;

public class Restrictions {

    public static <T extends EventContext> Restriction<T> and(Restriction<T>... restrictions) {
        return Arrays.stream(restrictions)
                .filter(Objects::nonNull)
                .reduce(Restriction::and)
                .orElse(null);
    }

    public static Restriction<EventContext> whenMessage(Restriction<? super EventContext> messageRule, MissingCapabilityPolicy policy) {
        return ctx -> {
            if (ctx instanceof HasMessage) {
                return messageRule.check(ctx);
            }
            return policy == MissingCapabilityPolicy.DENY
                    ? RestrictionResult.deny("Event has no message")
                    : RestrictionResult.allow();
        };
    }

}
