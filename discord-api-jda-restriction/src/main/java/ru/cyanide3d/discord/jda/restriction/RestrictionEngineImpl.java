package ru.cyanide3d.discord.jda.restriction;

import org.springframework.util.StringUtils;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionEngine;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;

public class RestrictionEngineImpl implements RestrictionEngine {

    @Override
    public <T extends EventContext<?>> RestrictionResult check(Restriction<T> restriction, T ctx) {
        return restriction.check(ctx);
    }

    @Override
    public <T extends EventContext<?>> void enforce(Restriction<T> restriction, T ctx) {
        RestrictionResult result = check(restriction, ctx);
        if (!result.isAllowed()) {
            throw new IllegalStateException(getReason(result));
        }
    }

    private String getReason(RestrictionResult result) {
        String reason = result.getReason();
        if (StringUtils.hasText(reason)) {
            return reason;
        }
        return "<unknown reason>";
    }

}
