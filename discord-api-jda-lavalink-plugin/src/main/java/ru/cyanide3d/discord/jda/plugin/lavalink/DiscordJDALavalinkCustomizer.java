package ru.cyanide3d.discord.jda.plugin.lavalink;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.DiscordJDABotCustomizer;

public class DiscordJDALavalinkCustomizer implements DiscordJDABotCustomizer {

    @Autowired
    private LavalinkClient lavalinkClient;

    @Override
    public void customize(JDABuilder builder) {
        builder.setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(lavalinkClient));
    }

}
