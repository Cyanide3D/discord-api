package ru.cyanide3d.discord.jda.event;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.EventContextFactory;
import ru.cyanide3d.discord.jda.api.event.DiscordJDAEventHandler;
import ru.cyanide3d.discord.jda.api.event.JDAEventDispatcher;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionService;

import static ru.cyanide3d.utils.CastUtils.cast;

@Slf4j
public class JDAEventDispatcherImpl implements JDAEventDispatcher {

    @Autowired
    private ObjectProvider<DiscordJDAEventHandler<?>> discordEventHandlers;

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private EventContextFactory eventContextFactory;

    @Override
    public void dispatch(GenericEvent event) {
        EventContext<?> eventContext = createEventContext(event);
        log.debug("Received GenericEvent: {}", event.getClass().getName());
        discordEventHandlers.stream()
                .filter(handler -> handler.supports(event, eventContext))
                .map(handler -> {
                    log.debug("Handling GenericEvent by handler: {}", handler.getClass().getName());
                    return handler;
                })
                .filter(handler -> {
                    RestrictionResult result = enforceRestrictions(handler, eventContext);
                    if (!result.isAllowed()) {
                        log.debug("Skipping handler {} because restriction failed: {}",
                                handler.getClass().getName(), result);
                        return false;
                    }
                    return true;
                })
                .forEach(handler -> {
                    try {
                        handler.onEvent(cast(eventContext));
                    } catch (Exception e) {
                        log.error("Failed to handle GenericEvent by handler: {}.", handler.getClass().getName(), e);
                    }
                });
    }

    protected EventContext<GenericEvent> createEventContext(GenericEvent event) {
        return eventContextFactory.create(event);
    }

    protected RestrictionResult enforceRestrictions(DiscordJDAEventHandler<?> handler, EventContext<?> eventContext) {
        Restriction<?> restriction = handler.getRestriction();
        return restrictionService.check(restriction, eventContext);
    }

}
