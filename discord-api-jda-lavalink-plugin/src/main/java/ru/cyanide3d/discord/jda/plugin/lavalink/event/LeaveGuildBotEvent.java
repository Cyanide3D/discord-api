package ru.cyanide3d.discord.jda.plugin.lavalink.event;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.contexts.JDAEventContext;
import ru.cyanide3d.discord.jda.event.AbstractDiscordJDAEventHandler;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;

public class LeaveGuildBotEvent extends AbstractDiscordJDAEventHandler<JDAEventContext<?>> {

    @Autowired
    private PlayerManager playerManager;

    @Override
    public void onEvent(JDAEventContext<?> ctx) {
        GenericEvent event = ctx.getEvent();
        if (!(event instanceof GuildLeaveEvent guildLeaveEvent)) {
            return;
        }

        playerManager.forget(guildLeaveEvent.getGuild().getIdLong());
    }

}
