package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.plugin.lavalink.BotVoiceChannelSummoner;
import ru.cyanide3d.discord.jda.plugin.lavalink.diag.UserNotInVoiceChannelException;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerPlayResult;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerResultMessageFormatter;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.YoutubeTrackIdentifier;

import static ru.cyanide3d.discord.jda.plugin.lavalink.command.player.PlayerCommandSpec.QUERY;

public class PlayerPlayCommandExecutor implements SlashExecutor {

    @Autowired
    private BotVoiceChannelSummoner botVoiceChannelSummoner;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private PlayerResultMessageFormatter playerResultMessageFormatter;

    @Override
    public void execute(SlashCommandContext ctx) {
        String query = ctx.requireOption(QUERY);

        Guild guild = ctx.requireGuild();
        Member member = ctx.requireMember();

        try {
            botVoiceChannelSummoner.summonTo(guild, member);
        } catch (UserNotInVoiceChannelException e) {
            ctx.replyEphemeral("Нужно быть в голосовом чате, чтобы использовать команду.");
            return;
        }

        PlayerPlayResult result = playerManager.play(guild.getIdLong(), YoutubeTrackIdentifier.of(query));

        String message = playerResultMessageFormatter.format(result);
        if (result.isSuccess()) {
            ctx.reply(message);
        } else {
            ctx.replyEphemeral(message);
        }
    }
}