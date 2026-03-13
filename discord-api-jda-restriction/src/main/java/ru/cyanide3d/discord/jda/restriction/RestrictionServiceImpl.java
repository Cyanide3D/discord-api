package ru.cyanide3d.discord.jda.restriction;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.restriction.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.restriction.EventContextFactory;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionEngine;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionService;

import static ru.cyanide3d.utils.CastUtils.cast;

@Slf4j
public class RestrictionServiceImpl implements RestrictionService {

    @Autowired
    private RestrictionEngine restrictionEngine;

    @Autowired
    private EventContextFactory eventContextFactory;

    @Override
    public RestrictionResult check(Restriction<?> restriction, GenericEvent event) {
        log.debug("Checking restriction for event {}", event.getClass().getSimpleName());
        try {
            if (restriction == null) {
                log.debug("Skipping restriction check for event {} because restriction is null", event.getClass().getSimpleName());
                return RestrictionResult.allow();
            }
            EventContext eventContext = eventContextFactory.create(event);
            RestrictionResult result = restrictionEngine.check(restriction, cast(eventContext));
            log.debug("End of checking restriction for event {} result: {}", event.getClass().getSimpleName(), result);
            return result;
        } catch (Exception e) {
            log.error("Error while checking restrictions", e);
            return RestrictionResult.deny("Error happened when checking restriction");
        }
    }

}
