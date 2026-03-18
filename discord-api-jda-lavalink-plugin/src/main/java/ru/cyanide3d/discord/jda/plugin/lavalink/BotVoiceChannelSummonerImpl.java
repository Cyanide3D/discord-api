package ru.cyanide3d.discord.jda.plugin.lavalink;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import ru.cyanide3d.discord.jda.plugin.lavalink.diag.JoiningErrorException;
import ru.cyanide3d.discord.jda.plugin.lavalink.diag.UserNotInVoiceChannelException;

public class BotVoiceChannelSummonerImpl implements BotVoiceChannelSummoner {

    @Override
    public void summonTo(AudioChannel channel) {
        Guild guild = channel.getGuild();
        doSummon(guild, channel);
    }

    @Override
    public void summonTo(Guild guild, Member member) {
        GuildVoiceState voiceState = member.getVoiceState();
        if (voiceState == null || !voiceState.inAudioChannel()) {
            throw new UserNotInVoiceChannelException(member.getIdLong());
        }
        doSummon(guild, voiceState.getChannel());
    }

    protected void doSummon(Guild guild, AudioChannel audioChannel) {
        AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(audioChannel);
        try {
            audioManager.openAudioConnection(audioChannel);
        } catch (Exception e) {
            throw new JoiningErrorException(e);
        }
    }

}
