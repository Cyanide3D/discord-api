package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import dev.arbjerg.lavalink.client.player.Track;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.GuildPlayerRegistry;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.GuildPlayerState;

import java.util.List;

import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.PAGE;

public class PlayerQueueCommandExecutor implements SlashExecutor {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private GuildPlayerRegistry guildPlayerRegistry;

    @Override
    public void execute(SlashCommandContext ctx) {
        long guildId = ctx.requireGuild().getIdLong();
        GuildPlayerState state = guildPlayerRegistry.get(guildId);

        if (state == null || !state.hasCurrentTrack()) {
            ctx.replyEphemeral("Сейчас ничего не воспроизводится.");
            return;
        }

        long requestedPage = ctx.getOption(PAGE).orElse(1L);
        if (requestedPage < 1) {
            ctx.replyEphemeral("Номер страницы должен быть больше или равен 1.");
            return;
        }

        List<Track> queuedTracks = List.copyOf(state.getQueue());
        int totalTracks = queuedTracks.size();
        int totalPages = Math.max(1, (int) Math.ceil(totalTracks / (double) PAGE_SIZE));
        int page = (int) Math.min(requestedPage, totalPages);

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalTracks);

        StringBuilder message = new StringBuilder();
        message.append("🎶 **Очередь проигрывателя**\n");
        message.append("Сейчас играет: **")
                .append(trackTitle(state.getCurrentTrack()))
                .append("**");

        if (state.isPaused()) {
            message.append(" *(на паузе)*");
        }

        message.append("\n");
        message.append("Треков в очереди: **")
                .append(totalTracks)
                .append("**\n");

        if (totalTracks == 0) {
            message.append("Очередь пуста.");
            ctx.reply(message.toString());
            return;
        }

        message.append("Страница **")
                .append(page)
                .append('/')
                .append(totalPages)
                .append("**\n\n");

        for (int i = fromIndex; i < toIndex; i++) {
            Track track = queuedTracks.get(i);
            message.append(i + 1)
                    .append(". **")
                    .append(trackTitle(track))
                    .append("**\n");
        }

        ctx.reply(message.toString().trim());
    }

    protected String trackTitle(Track track) {
        if (track == null || track.getInfo() == null || track.getInfo().getTitle() == null) {
            return "unknown track";
        }
        return track.getInfo().getTitle();
    }
}