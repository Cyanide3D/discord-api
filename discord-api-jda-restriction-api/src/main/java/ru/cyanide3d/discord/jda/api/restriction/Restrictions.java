package ru.cyanide3d.discord.jda.api.restriction;


import lombok.experimental.UtilityClass;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.MessageEventContext;

import java.util.Arrays;
import java.util.Objects;

@UtilityClass
public class Restrictions {

    public static <T extends EventContext<?>> Restriction<T> and(Restriction<? super T>... restrictions) {
        return Arrays.stream(restrictions)
                .filter(Objects::nonNull)
                .reduce(Restriction.allow(), Restriction::and, Restriction::and);
    }

    public static Restriction<EventContext<?>> whenMessage(Restriction<? super MessageEventContext<?>> messageRule, MissingCapabilityPolicy policy) {
        return ctx -> {
            if (ctx instanceof MessageEventContext<?> messageContext) {
                return messageRule.check(messageContext);
            }
            return policy == MissingCapabilityPolicy.DENY
                    ? RestrictionResult.deny()
                    : RestrictionResult.allow();
        };
    }

}
