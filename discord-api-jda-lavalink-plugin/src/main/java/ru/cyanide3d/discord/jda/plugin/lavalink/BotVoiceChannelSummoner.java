package ru.cyanide3d.discord.jda.plugin.lavalink;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

public interface BotVoiceChannelSummoner {

    void summonTo(AudioChannel channel);

    void summonTo(Guild guild, Member member);

}
