package ru.cyanide3d.discord.jda.event;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.EventContextFactory;
import ru.cyanide3d.discord.jda.api.event.DiscordJDAEventHandler;
import ru.cyanide3d.discord.jda.api.event.JDAEventDispatcher;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class JDAEventDispatcherImpl implements JDAEventDispatcher, SmartInitializingSingleton {

    @Autowired
    private ObjectProvider<DiscordJDAEventHandler<?>> discordEventHandlers;

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private EventContextFactory eventContextFactory;

    private volatile List<DiscordJDAEventHandler<?>> orderedHandlers = List.of();

    private volatile Map<Class<? extends GenericEvent>, List<DiscordJDAEventHandler<?>>> handlersBySupportedType = Map.of();

    private final Map<Class<? extends GenericEvent>, List<DiscordJDAEventHandler<?>>> resolvedHandlersCache =
            new ConcurrentHashMap<>();

    @Override
    public void afterSingletonsInstantiated() {
        List<DiscordJDAEventHandler<?>> handlers = new ArrayList<>(discordEventHandlers.stream().toList());
        AnnotationAwareOrderComparator.sort(handlers);

        Map<Class<? extends GenericEvent>, List<DiscordJDAEventHandler<?>>> grouped = new LinkedHashMap<>();
        for (DiscordJDAEventHandler<?> handler : handlers) {
            grouped.computeIfAbsent(handler.getSupportedJdaEventType(), key -> new ArrayList<>()).add(handler);
        }

        Map<Class<? extends GenericEvent>, List<DiscordJDAEventHandler<?>>> immutableGrouped = new LinkedHashMap<>();
        for (Map.Entry<Class<? extends GenericEvent>, List<DiscordJDAEventHandler<?>>> entry : grouped.entrySet()) {
            immutableGrouped.put(entry.getKey(), List.copyOf(entry.getValue()));
        }

        this.orderedHandlers = List.copyOf(handlers);
        this.handlersBySupportedType = Map.copyOf(immutableGrouped);
        this.resolvedHandlersCache.clear();

        log.info("Initialized JDA event dispatcher with {} handlers", handlers.size());
    }

    @Override
    public void dispatch(GenericEvent event) {
        Class<? extends GenericEvent> actualEventType = event.getClass();
        List<DiscordJDAEventHandler<?>> candidates = findCandidates(actualEventType);

        if (candidates.isEmpty()) {
            log.debug("No handlers registered for event type {}", actualEventType.getName());
            return;
        }

        EventContext<GenericEvent> eventContext = createEventContext(event);
        log.debug("Received GenericEvent: {}", actualEventType.getName());

        for (DiscordJDAEventHandler<?> handler : candidates) {
            if (!handler.supports(event, eventContext)) {
                continue;
            }

            RestrictionResult result = enforceRestrictions(handler, eventContext);
            if (!result.isAllowed()) {
                log.debug("Skipping handler {} because restriction failed: {}",
                        handler.getClass().getName(), result);
                continue;
            }

            invokeHandler(handler, eventContext);
        }
    }

    protected EventContext<GenericEvent> createEventContext(GenericEvent event) {
        return eventContextFactory.create(event);
    }

    protected RestrictionResult enforceRestrictions(DiscordJDAEventHandler<?> handler, EventContext<?> eventContext) {
        Restriction<?> restriction = handler.getRestriction();
        return restrictionService.check(restriction, eventContext);
    }

    protected List<DiscordJDAEventHandler<?>> findCandidates(Class<? extends GenericEvent> actualEventType) {
        return resolvedHandlersCache.computeIfAbsent(actualEventType, this::resolveCandidates);
    }

    protected List<DiscordJDAEventHandler<?>> resolveCandidates(Class<? extends GenericEvent> actualEventType) {
        LinkedHashSet<DiscordJDAEventHandler<?>> result = new LinkedHashSet<>();

        for (Map.Entry<Class<? extends GenericEvent>, List<DiscordJDAEventHandler<?>>> entry : handlersBySupportedType.entrySet()) {
            if (entry.getKey().isAssignableFrom(actualEventType)) {
                result.addAll(entry.getValue());
            }
        }

        if (result.isEmpty()) {
            for (DiscordJDAEventHandler<?> handler : orderedHandlers) {
                if (handler.getSupportedJdaEventType().isAssignableFrom(actualEventType)) {
                    result.add(handler);
                }
            }
        }

        return List.copyOf(result);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void invokeHandler(DiscordJDAEventHandler<?> handler, EventContext<?> eventContext) {
        try {
            log.debug("Handling GenericEvent by handler: {}", handler.getClass().getName());
            ((DiscordJDAEventHandler) handler).onEvent(eventContext);
        } catch (RuntimeException e) {
            log.error("Failed to handle GenericEvent by handler: {}", handler.getClass().getName(), e);
        }
    }
}