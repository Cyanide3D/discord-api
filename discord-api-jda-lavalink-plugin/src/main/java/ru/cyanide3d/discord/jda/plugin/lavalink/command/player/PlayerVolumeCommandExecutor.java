package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerActionResult;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;

import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.VOLUME;

public class PlayerVolumeCommandExecutor extends AbstractPlayerCommandExecutor implements SlashExecutor {

    @Autowired
    private PlayerManager playerManager;

    @Override
    public void execute(SlashCommandContext ctx) {
        long requestedVolume = ctx.requireOption(VOLUME);
        if (requestedVolume < 0 || requestedVolume > 1000) {
            ctx.replyEphemeral("Громкость должна быть в диапазоне от 0 до 1000.");
            return;
        }

        long guildId = ctx.requireGuild().getIdLong();
        PlayerActionResult result = playerManager.setVolume(guildId, (int) requestedVolume);
        replyWithPlayerResult(ctx, result);
    }
}