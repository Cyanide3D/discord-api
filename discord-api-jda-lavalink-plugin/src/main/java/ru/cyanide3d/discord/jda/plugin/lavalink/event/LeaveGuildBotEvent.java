package ru.cyanide3d.discord.jda.plugin.lavalink.event;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.contexts.GuildLeaveContext;
import ru.cyanide3d.discord.jda.event.AbstractDiscordJDAEventHandler;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.PlayerManager;

public class LeaveGuildBotEvent extends AbstractDiscordJDAEventHandler<GuildLeaveContext> {

    @Autowired
    private PlayerManager playerManager;

    @Override
    public Class<? extends GenericEvent> getSupportedJdaEventType() {
        return GuildLeaveEvent.class;
    }

    @Override
    public void onEvent(GuildLeaveContext ctx) {
        playerManager.forget(ctx.requireGuild().getIdLong());
    }

}
