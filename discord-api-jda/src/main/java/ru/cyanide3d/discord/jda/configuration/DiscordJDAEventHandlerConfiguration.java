package ru.cyanide3d.discord.jda.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cyanide3d.discord.jda.api.event.JDAEventDispatcher;
import ru.cyanide3d.discord.jda.event.GenericEventDiscordJDAEventListenerAdapter;
import ru.cyanide3d.discord.jda.event.JDAEventDispatcherImpl;

@Configuration
public class DiscordJDAEventHandlerConfiguration {

    @Bean
    public GenericEventDiscordJDAEventListenerAdapter jdaDiscordEventListenerAdapter() {
        return new GenericEventDiscordJDAEventListenerAdapter();
    }

    @Bean
    public JDAEventDispatcher jdaEventDispatcher() {
        return new JDAEventDispatcherImpl();
    }

}
