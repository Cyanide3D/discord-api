package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerActionResult;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;

import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.POSITION_SECONDS;

public class PlayerSeekCommandExecutor extends AbstractPlayerCommandExecutor implements SlashExecutor {

    @Autowired
    private PlayerManager playerManager;

    @Override
    public void execute(SlashCommandContext ctx) {
        long seconds = ctx.requireOption(POSITION_SECONDS);
        if (seconds < 0) {
            ctx.replyEphemeral("Позиция не может быть отрицательной.");
            return;
        }

        long guildId = ctx.requireGuild().getIdLong();
        long positionMs = seconds * 1000L;

        PlayerActionResult result = playerManager.seek(guildId, positionMs);
        replyWithPlayerResult(ctx, result);
    }
}