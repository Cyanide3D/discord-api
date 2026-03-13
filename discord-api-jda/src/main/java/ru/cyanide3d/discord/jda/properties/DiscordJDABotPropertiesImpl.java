package ru.cyanide3d.discord.jda.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.validation.annotation.Validated;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDABotProperties;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDAPresenceProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Validated
@Setter
public class DiscordJDABotPropertiesImpl implements DiscordJDABotProperties {

    @Getter
    @NotBlank(message = "discord.jda.bot-token must not be blank when enabled")
    private String botToken;

    @Getter
    private DiscordJDAPresenceProperties presence = new DiscordJDAPresenceProperties();

    @Getter
    private boolean autoReconnect = true;

    @Getter
    @Min(value = 32, message = "discord.jda.max-reconnect-delay must be >= 32 seconds")
    private int maxReconnectDelay = 60;

    @Getter
    private int slowEventThreshold = 1_000;

    @Getter
    private boolean requestTimeoutRetry = true;

    @Getter
    private String chunkingFilterMode = "NONE";

    private List<String> gatewayIntents = new ArrayList<>();

    private List<String> disabledCacheFlags = new ArrayList<>();

    private List<String> enabledCacheFlags = new ArrayList<>();

    @Getter
    private List<String> memberCachePolicies = new ArrayList<>();

    @Getter
    private List<String> chunkingFilterIds = new ArrayList<>();

    @Override
    public Collection<GatewayIntent> getGatewayIntents() {
        return gatewayIntents.stream()
                .map(e -> e.toUpperCase(Locale.ROOT))
                .map(GatewayIntent::valueOf)
                .toList();
    }

    @Override
    public Collection<CacheFlag> getDisabledCacheFlags() {
        return disabledCacheFlags.stream()
                .map(e -> e.toUpperCase(Locale.ROOT))
                .map(CacheFlag::valueOf)
                .toList();
    }

    @Override
    public Collection<CacheFlag> getEnabledCacheFlags() {
        return enabledCacheFlags.stream()
                .map(e -> e.toUpperCase(Locale.ROOT))
                .map(CacheFlag::valueOf)
                .toList();
    }

    @Override
    public Collection<String> getStringGatewayIntents() {
        return gatewayIntents;
    }

    @Override
    public Collection<String> getStringDisabledCacheFlags() {
        return disabledCacheFlags;
    }

    @Override
    public Collection<String> getStringEnabledCacheFlags() {
        return enabledCacheFlags;
    }


}
