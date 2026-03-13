package ru.cyanide3d.discord.jda.event;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.hooks.InterfacedEventManager;

import java.util.List;

@Slf4j
public class DiscordJDAEventManager implements IEventManager {

    private final IEventManager delegate = new InterfacedEventManager();

    private final long slowEventThresholdMs;

    public DiscordJDAEventManager(long slowEventThresholdMs) {
        this.slowEventThresholdMs = slowEventThresholdMs;
    }

    @Override
    public void register(Object listener) {
        log.info("JDA listener registered: {}", listener.getClass().getName());
        delegate.register(listener);
    }

    @Override
    public void unregister(Object listener) {
        log.info("JDA listener unregistered: {}", listener.getClass().getName());
        delegate.unregister(listener);
    }

    @Override
    public void handle(GenericEvent event) {
        long start = System.nanoTime();
        try {
            delegate.handle(event);
        } finally {
            long tookMs = (System.nanoTime() - start) / 1_000_000;
            if (tookMs >= slowEventThresholdMs) {
                log.warn("Slow JDA event: {} took {} ms", event.getClass().getSimpleName(), tookMs);
            }
        }
    }

    @Override
    public List<Object> getRegisteredListeners() {
        return delegate.getRegisteredListeners();
    }
}
