package ru.cyanide3d.discord.jda.plugin.lavalink;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

public interface BotVoiceChannelConnector {

    void connectTo(AudioChannel channel);

    void connectTo(Guild guild, Member member);

    void disconnectFrom(Guild guild);

}
