package ru.cyanide3d.discord.jda.plugin.lavalink;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import ru.cyanide3d.discord.jda.plugin.lavalink.diag.JoiningErrorException;
import ru.cyanide3d.discord.jda.plugin.lavalink.diag.LeavingErrorException;
import ru.cyanide3d.discord.jda.plugin.lavalink.diag.UserNotInVoiceChannelException;

public class BotVoiceChannelConnectorImpl implements BotVoiceChannelConnector {

    @Override
    public void connectTo(AudioChannel channel) {
        Guild guild = channel.getGuild();
        doConnect(guild, channel);
    }

    @Override
    public void connectTo(Guild guild, Member member) {
        GuildVoiceState voiceState = member.getVoiceState();
        if (voiceState == null || !voiceState.inAudioChannel() || voiceState.getChannel() == null) {
            throw new UserNotInVoiceChannelException(member.getIdLong());
        }
        doConnect(guild, voiceState.getChannel());
    }

    @Override
    public void disconnectFrom(Guild guild) {
        try {
            guild.getJDA().getDirectAudioController().disconnect(guild);
        } catch (Exception e) {
            throw new LeavingErrorException(e);
        }
    }

    protected void doConnect(Guild guild, AudioChannel audioChannel) {
        JDA jda = guild.getJDA();
        try {
            jda.getDirectAudioController().connect(audioChannel);
        } catch (Exception e) {
            throw new JoiningErrorException(e);
        }
    }

}
