package ru.cyanide3d.discord.jda.restriction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionEngine;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionService;

import static ru.cyanide3d.utils.CastUtils.cast;

@Slf4j
public class RestrictionServiceImpl implements RestrictionService {

    @Autowired
    private RestrictionEngine restrictionEngine;

    @Override
    public RestrictionResult check(Restriction<?> restriction, EventContext<?> eventContext) {
        log.debug("Checking restriction for event {}", eventContext.getClass().getSimpleName());
        try {
            if (restriction == null) {
                log.debug("Skipping restriction check for event {} because restriction is null", eventContext.getClass().getSimpleName());
                return RestrictionResult.allow();
            }
            RestrictionResult result = restrictionEngine.check(restriction, cast(eventContext));
            log.debug("End of checking restriction for event {} result: {}", eventContext.getClass().getSimpleName(), result);
            return result;
        } catch (Exception e) {
            log.error("Error while checking restriction for event {}", eventContext.getClass().getSimpleName(), e);
            return RestrictionResult.error(e);
        }
    }

}
