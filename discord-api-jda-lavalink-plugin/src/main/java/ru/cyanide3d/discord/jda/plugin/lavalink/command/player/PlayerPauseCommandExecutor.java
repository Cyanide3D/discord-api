package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.GuildPlayerRegistry;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.GuildPlayerState;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerActionResult;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerResultMessageFormatter;

public class PlayerPauseCommandExecutor implements SlashExecutor {

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private GuildPlayerRegistry guildPlayerRegistry;

    @Autowired
    private PlayerResultMessageFormatter playerResultMessageFormatter;

    @Override
    public void execute(SlashCommandContext ctx) {
        long guildId = ctx.requireGuild().getIdLong();

        GuildPlayerState state = guildPlayerRegistry.get(guildId);
        if (state == null) {
            ctx.replyEphemeral("Сейчас ничего не воспроизводится.");
            return;
        }

        PlayerActionResult result = state.isPaused()
                ? playerManager.resume(guildId)
                : playerManager.pause(guildId);

        String message = playerResultMessageFormatter.format(result);
        if (result.isSuccess()) {
            ctx.reply(message);
        } else {
            ctx.replyEphemeral(message);
        }
    }
}