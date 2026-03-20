package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerActionResult;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;

public class PlayerClearQueueCommandExecutor extends AbstractPlayerCommandExecutor implements SlashExecutor {

    @Autowired
    private PlayerManager playerManager;

    @Override
    public void execute(SlashCommandContext ctx) {
        long guildId = ctx.requireGuild().getIdLong();

        PlayerActionResult result = playerManager.clearQueue(guildId);
        replyWithPlayerResult(ctx, result);
    }
}