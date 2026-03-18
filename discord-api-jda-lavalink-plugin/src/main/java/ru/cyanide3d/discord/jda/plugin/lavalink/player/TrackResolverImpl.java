package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.LavalinkLoadResult;
import dev.arbjerg.lavalink.client.player.PlaylistLoaded;
import dev.arbjerg.lavalink.client.player.SearchResult;
import dev.arbjerg.lavalink.client.player.TrackLoaded;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class TrackResolverImpl implements TrackResolver {

    @Autowired
    private LavalinkClient lavalinkClient;

    @Override
    public TrackLoadResult resolve(TrackIdentifier identifier, long guildId) {
        Link link = lavalinkClient.getOrCreateLink(guildId);

        LavalinkLoadResult result = link.loadItem(identifier.buildStringIdentifier()).block();
        if (result == null) {
            return TrackLoadResult.of(Collections.emptyList(), false, identifier.sourceName());
        }

        if (result instanceof TrackLoaded trackLoaded) {
            return TrackLoadResult.of(
                    Collections.singletonList(trackLoaded.getTrack()),
                    false,
                    identifier.sourceName()
            );
        }

        if (result instanceof SearchResult searchResult) {
            return TrackLoadResult.of(
                    searchResult.getTracks(),
                    false,
                    identifier.sourceName()
            );
        }

        if (result instanceof PlaylistLoaded playlistLoaded) {
            return TrackLoadResult.of(
                    playlistLoaded.getTracks(),
                    true,
                    identifier.sourceName()
            );
        }

        return TrackLoadResult.of(Collections.emptyList(), false, identifier.sourceName());
    }
}