package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import ru.cyanide3d.discord.jda.api.contexts.GuildVoiceUpdateContext;

public class DefaultGuildVoiceUpdateContext
        extends DefaultGenericGuildVoiceEventContext<GuildVoiceUpdateEvent>
        implements GuildVoiceUpdateContext {

    public DefaultGuildVoiceUpdateContext(GuildVoiceUpdateEvent event) {
        super(event);
    }

    @Override
    public AudioChannelUnion getChannelJoinedOrNull() {
        return getEvent().getChannelJoined();
    }

    @Override
    public AudioChannelUnion getChannelLeftOrNull() {
        return getEvent().getChannelLeft();
    }

}
