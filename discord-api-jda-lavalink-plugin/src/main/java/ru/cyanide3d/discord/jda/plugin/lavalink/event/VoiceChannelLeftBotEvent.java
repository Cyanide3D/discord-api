package ru.cyanide3d.discord.jda.plugin.lavalink.event;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.contexts.JDAEventContext;
import ru.cyanide3d.discord.jda.event.AbstractDiscordJDAEventHandler;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;

public class VoiceChannelLeftBotEvent extends AbstractDiscordJDAEventHandler<JDAEventContext<?>> {

    @Autowired
    private PlayerManager playerManager;

    @Override
    public void onEvent(JDAEventContext<?> ctx) {
        GenericEvent event = ctx.getEvent();
        if (!(event instanceof GuildVoiceUpdateEvent voiceUpdateEvent)) {
            return;
        }

        Guild guild = voiceUpdateEvent.getGuild();
        Member member = voiceUpdateEvent.getMember();

        if (!member.equals(guild.getSelfMember())) {
            return;
        }

        if (voiceUpdateEvent.getChannelJoined() == null && voiceUpdateEvent.getChannelLeft() != null) {
            playerManager.forget(guild.getIdLong());
        }
    }

}
