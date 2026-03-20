package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerActionResult;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerResultMessageFormatter;

public abstract class AbstractPlayerCommandExecutor {

    @Autowired
    protected PlayerResultMessageFormatter playerResultMessageFormatter;

    protected void replyWithPlayerResult(SlashCommandContext ctx, PlayerActionResult result) {
        String message = playerResultMessageFormatter.format(result);

        if (result != null && result.isSuccess()) {
            ctx.reply(message);
            return;
        }

        ctx.replyEphemeral(message);
    }
}