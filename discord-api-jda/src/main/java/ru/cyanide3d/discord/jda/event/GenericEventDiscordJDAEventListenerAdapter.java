package ru.cyanide3d.discord.jda.event;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent;
import net.dv8tion.jda.api.events.session.SessionRecreateEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.AutoEnabledEventListener;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.EventContextFactory;
import ru.cyanide3d.discord.jda.api.event.AbstractDiscordJDAEventListenerAdapter;
import ru.cyanide3d.discord.jda.api.event.DiscordJDAEventHandler;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionFailureNotifier;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionService;

import static ru.cyanide3d.utils.CastUtils.cast;

@Slf4j
public class GenericEventDiscordJDAEventListenerAdapter extends AbstractDiscordJDAEventListenerAdapter implements AutoEnabledEventListener {

    @Autowired
    private ObjectProvider<DiscordJDAEventHandler<?>> discordEventHandlers;

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private EventContextFactory eventContextFactory;

    @Autowired
    private RestrictionFailureNotifier restrictionFailureNotifier;

    @Override
    public void onGenericEvent(GenericEvent event) {
        EventContext<?> eventContext = createEventContext(event);
        log.debug("Received GenericEvent: {}", event.getClass().getName());
        discordEventHandlers.stream()
                .filter(handler -> handler.supportEventContext(eventContext))
                .map(handler -> {
                    log.debug("Handling GenericEvent by handler: {}", handler.getClass().getName());
                    return handler;
                })
                .filter(handler -> {
                    RestrictionResult result = enforceRestrictions(handler, eventContext);
                    if (result.isAllowed()) {
                        return true;
                    }
                    failedCheckRestrictionNotification(result, eventContext);
                    return false;
                })
                .forEach(handler -> {
                    try {
                        handler.onEvent(cast(eventContext));
                    } catch (Exception e) {
                        log.error("Failed to handle GenericEvent by handler: {}.", handler.getClass().getName(), e);
                    }
                });
    }

    protected void failedCheckRestrictionNotification(RestrictionResult restrictionResult, EventContext<?> eventContext) {
        restrictionFailureNotifier.notify(eventContext, restrictionResult);
    }

    protected EventContext<GenericEvent> createEventContext(GenericEvent event) {
        return eventContextFactory.create(event);
    }

    protected RestrictionResult enforceRestrictions(DiscordJDAEventHandler<?> handler, EventContext<?> eventContext) {
        Restriction<?> restriction = handler.getRestriction();
        return restrictionService.check(restriction, eventContext);
    }

    @Override
    public void onReady(ReadyEvent event) {
        log.info("JDA READY ({}): guilds available={}, unavailable={}, total={}, gatewayPingMs={}",
                shardTag(event.getJDA()),
                event.getGuildAvailableCount(),
                event.getGuildUnavailableCount(),
                event.getGuildTotalCount(),
                event.getJDA().getGatewayPing());
    }

    @Override
    public void onSessionDisconnect(SessionDisconnectEvent event) {
        var code = event.getCloseCode();
        log.warn("JDA DISCONNECT ({}): closedByServer={}, closeCode={}, at={}",
                shardTag(event.getJDA()),
                event.isClosedByServer(),
                code == null ? "null" : code + " (" + code.getCode() + ")",
                event.getTimeDisconnected());
    }

    @Override
    public void onSessionResume(SessionResumeEvent event) {
        log.info("JDA RESUME ({}): session resumed, events will be replayed", shardTag(event.getJDA()));
    }

    @Override
    public void onSessionRecreate(SessionRecreateEvent event) {
        log.warn("JDA RECREATE ({}): session recreated, caches replaced, some events may have been missed",
                shardTag(event.getJDA()));
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        var code = event.getCloseCode();
        log.error("JDA SHUTDOWN ({}): closeCode={}, rawCode={}, at={}",
                shardTag(event.getJDA()),
                code == null ? "null" : code + " (" + code.getCode() + ")",
                event.getCode(),
                event.getTimeShutdown());
    }

    private String shardTag(JDA jda) {
        var info = jda.getShardInfo();
        return info == null ? "shard=?" : "shard=" + info.getShardId() + "/" + info.getShardTotal();
    }
}
