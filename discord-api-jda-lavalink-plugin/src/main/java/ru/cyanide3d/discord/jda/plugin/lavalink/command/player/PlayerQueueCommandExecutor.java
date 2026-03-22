package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import dev.arbjerg.lavalink.client.player.Track;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerQueueSnapshot;

import java.util.List;
import java.util.Optional;

import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.PAGE;

public class PlayerQueueCommandExecutor implements SlashExecutor {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private PlayerManager playerManager;

    @Override
    public void execute(SlashCommandContext ctx) {
        long guildId = ctx.requireGuild().getIdLong();

        long requestedPage = ctx.getOption(PAGE).orElse(1L);
        if (requestedPage < 1) {
            ctx.replyEphemeral("Номер страницы должен быть больше или равен 1.");
            return;
        }

        Optional<PlayerQueueSnapshot> snapshotOptional = playerManager.getQueueSnapshot(guildId);
        if (snapshotOptional.isEmpty()) {
            ctx.replyEphemeral("Сейчас ничего не воспроизводится.");
            return;
        }

        PlayerQueueSnapshot snapshot = snapshotOptional.get();
        if (snapshot.getCurrentTrack() == null) {
            ctx.replyEphemeral("Сейчас ничего не воспроизводится.");
            return;
        }

        List<Track> queuedTracks = snapshot.getQueue();
        int totalTracks = queuedTracks.size();
        int totalPages = Math.max(1, (int) Math.ceil(totalTracks / (double) PAGE_SIZE));
        int page = (int) Math.min(requestedPage, totalPages);

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalTracks);

        StringBuilder message = new StringBuilder();
        message.append("🎶 **Очередь проигрывателя**\n");
        message.append("Сейчас играет: **")
                .append(trackTitle(snapshot.getCurrentTrack()))
                .append("**");

        if (snapshot.isPaused()) {
            message.append(" *(на паузе)*");
        }

        message.append("\n");
        message.append("Громкость: **")
                .append(snapshot.getVolume())
                .append("**\n");
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
                .append("/")
                .append(totalPages)
                .append("**\n\n");

        for (int i = fromIndex; i < toIndex; i++) {
            Track track = queuedTracks.get(i);
            message.append(i + 1)
                    .append(". **")
                    .append(trackTitle(track))
                    .append("**");

            String author = trackAuthor(track);
            if (author != null && !author.isBlank()) {
                message.append(" — ").append(author);
            }

            String duration = trackDuration(track);
            if (duration != null) {
                message.append(" `").append(duration).append("`");
            }

            message.append("\n");
        }

        ctx.reply(message.toString().trim());
    }

    protected String trackTitle(Track track) {
        if (track == null || track.getInfo() == null || track.getInfo().getTitle() == null) {
            return "unknown track";
        }
        return track.getInfo().getTitle();
    }

    protected String trackAuthor(Track track) {
        if (track == null || track.getInfo() == null) {
            return null;
        }
        return track.getInfo().getAuthor();
    }

    protected String trackDuration(Track track) {
        if (track == null || track.getInfo() == null) {
            return null;
        }

        long length = track.getInfo().getLength();
        if (length <= 0) {
            return null;
        }

        long totalSeconds = length / 1000L;
        long hours = totalSeconds / 3600L;
        long minutes = (totalSeconds % 3600L) / 60L;
        long seconds = totalSeconds % 60L;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }

        return String.format("%d:%02d", minutes, seconds);
    }
}