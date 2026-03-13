package ru.cyanide3d.discord.jda.api.properties;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Collection;

public interface DiscordJDABotProperties {

    DiscordJDAPresenceProperties getPresence();

    String getBotToken();

    boolean isAutoReconnect();

    int getMaxReconnectDelay();

    boolean isRequestTimeoutRetry();

    int getSlowEventThreshold();

    String getChunkingFilterMode();

    Collection<GatewayIntent> getGatewayIntents();

    Collection<CacheFlag> getDisabledCacheFlags();

    Collection<CacheFlag> getEnabledCacheFlags();

    Collection<String> getStringGatewayIntents();

    Collection<String> getStringDisabledCacheFlags();

    Collection<String> getStringEnabledCacheFlags();

    Collection<String> getMemberCachePolicies();

    Collection<String> getChunkingFilterIds();

}
