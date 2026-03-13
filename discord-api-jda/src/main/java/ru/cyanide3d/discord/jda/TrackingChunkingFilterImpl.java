package ru.cyanide3d.discord.jda;

import net.dv8tion.jda.api.utils.ChunkingFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDABotProperties;
import ru.cyanide3d.discord.jda.api.TrackingChunkingFilter;

import java.util.Collection;

public class TrackingChunkingFilterImpl implements TrackingChunkingFilter, InitializingBean {

    private ChunkingFilter delegate;

    @Autowired
    private DiscordJDABotProperties properties;

    @Override
    public boolean filter(long guildId) {
        return delegate.filter(guildId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String mode = properties.getChunkingFilterMode();
        Collection<String> ids = properties.getChunkingFilterIds();
        delegate = buildDelegate(mode, ids);
    }

    protected ChunkingFilter buildDelegate(String mode, Collection<String> ids) {
        if (mode == null || "none".equalsIgnoreCase(mode)) {
            return ChunkingFilter.NONE;
        }
        if ("all".equalsIgnoreCase(mode)) {
            return ChunkingFilter.ALL;
        }
        if ("include".equalsIgnoreCase(mode)) {
            return ChunkingFilter.include(buildIds(ids));
        }
        if ("exclude".equalsIgnoreCase(mode)) {
            return ChunkingFilter.exclude(buildIds(ids));
        }
        throw new IllegalArgumentException("Unknown ChunkingFilter: " + mode);
    }

    private long[] buildIds(Collection<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new long[0];
        }
        return ids.stream()
                .mapToLong(Long::parseLong)
                .toArray();
    }
}
