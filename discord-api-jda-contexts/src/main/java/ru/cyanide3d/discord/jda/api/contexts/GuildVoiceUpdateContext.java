package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

public interface GuildVoiceUpdateContext extends MemberEventContext<GuildVoiceUpdateEvent> {

    AudioChannelUnion getChannelJoinedOrNull();

    AudioChannelUnion getChannelLeftOrNull();

    default boolean isJoin() {
        return getChannelJoinedOrNull() != null && getChannelLeftOrNull() == null;
    }

    default boolean isLeave() {
        return getChannelJoinedOrNull() == null && getChannelLeftOrNull() != null;
    }

    default boolean isMove() {
        return getChannelJoinedOrNull() != null && getChannelLeftOrNull() != null;
    }

}
