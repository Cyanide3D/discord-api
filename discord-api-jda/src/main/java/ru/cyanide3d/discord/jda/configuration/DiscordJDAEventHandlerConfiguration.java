package ru.cyanide3d.discord.jda.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cyanide3d.discord.jda.event.GenericEventDiscordJDAEventListenerAdapter;

@Configuration
public class DiscordJDAEventHandlerConfiguration {

    @Bean
    public GenericEventDiscordJDAEventListenerAdapter jdaDiscordEventListenerAdapter() {
        return new GenericEventDiscordJDAEventListenerAdapter();
    }

}
