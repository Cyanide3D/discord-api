package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.event.TrackEndEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Disposable;

@Slf4j
public class LavalinkTrackQueueListener {

    @Autowired
    private LavalinkClient lavalinkClient;

    @Autowired
    private PlayerManager playerManager;

    private Disposable subscription;

    @PostConstruct
    public void subscribe() {
        this.subscription = lavalinkClient.on(TrackEndEvent.class)
                .subscribe(
                        this::handleTrackEnd,
                        error -> log.error("Lavalink TrackEndEvent listener crashed", error)
                );
    }

    @PreDestroy
    public void unsubscribe() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    protected void handleTrackEnd(TrackEndEvent event) {
        try {
            if (!shouldPlayNext(event)) {
                log.debug(
                        "Ignoring TrackEndEvent for guildId={}, reason={}",
                        event.getGuildId(),
                        event.getEndReason()
                );
                return;
            }

            log.debug(
                    "Track ended for guildId={}, reason={}, trying to play next",
                    event.getGuildId(),
                    event.getEndReason()
            );

            playerManager.playNextIfAvailable(event.getGuildId());
        } catch (Exception e) {
            log.error("Failed to process TrackEndEvent for guildId={}", event.getGuildId(), e);
        }
    }

    protected boolean shouldPlayNext(TrackEndEvent event) {
        String reason = event.getEndReason().name();

        return "FINISHED".equals(reason) || "LOAD_FAILED".equals(reason);
    }
}