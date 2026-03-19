package ru.cyanide3d.discord.jda.plugin.lavalink.event;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.contexts.GuildVoiceUpdateContext;
import ru.cyanide3d.discord.jda.event.AbstractDiscordJDAEventHandler;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;

public class VoiceChannelLeftBotEvent extends AbstractDiscordJDAEventHandler<GuildVoiceUpdateContext> {

    @Autowired
    private PlayerManager playerManager;

    @Override
    public Class<? extends GenericEvent> getSupportedJdaEventType() {
        return GuildVoiceUpdateEvent.class;
    }

    @Override
    public void onEvent(GuildVoiceUpdateContext ctx) {
        if (!ctx.requireMember().equals(ctx.requireGuild().getSelfMember())) {
            return;
        }

        if (ctx.isLeave()) {
            playerManager.forget(ctx.requireGuild().getIdLong());
        }
    }

}
