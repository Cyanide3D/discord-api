package ru.cyanide3d.discord.jda.api.contexts.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cyanide3d.discord.jda.api.contexts.EventContextFactory;
import ru.cyanide3d.discord.jda.api.contexts.impl.EventContextFactoryImpl;

@Configuration
public class DiscordJDAContextsConfiguration {

    @Bean
    public EventContextFactory eventContextFactory() {
        return new EventContextFactoryImpl();
    }

}
