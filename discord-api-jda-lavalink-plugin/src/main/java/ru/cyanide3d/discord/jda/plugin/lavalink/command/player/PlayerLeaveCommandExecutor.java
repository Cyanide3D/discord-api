package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.plugin.lavalink.BotVoiceChannelConnector;
import ru.cyanide3d.discord.jda.plugin.lavalink.diag.LeavingErrorException;

public class PlayerLeaveCommandExecutor implements SlashExecutor {

    @Autowired
    private BotVoiceChannelConnector botVoiceChannelConnector;

    @Override
    public void execute(SlashCommandContext ctx) {
        Guild guild = ctx.requireGuild();

        try {
            botVoiceChannelConnector.disconnectFrom(guild);
        } catch (LeavingErrorException e) {
            ctx.replyEphemeral("Не удалось отключить бота от голосового канала.");
            return;
        }

        ctx.reply("👋 Покидаю голосовой канал.");
    }
}