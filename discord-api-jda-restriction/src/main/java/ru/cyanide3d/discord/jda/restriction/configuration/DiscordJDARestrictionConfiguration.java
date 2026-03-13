package ru.cyanide3d.discord.jda.restriction.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cyanide3d.discord.jda.api.restriction.EventContextFactory;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionEngine;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionService;
import ru.cyanide3d.discord.jda.restriction.EventContextFactoryImpl;
import ru.cyanide3d.discord.jda.restriction.RestrictionEngineImpl;
import ru.cyanide3d.discord.jda.restriction.RestrictionServiceImpl;

@Configuration
public class DiscordJDARestrictionConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EventContextFactory eventContextFactory() {
        return new EventContextFactoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestrictionEngine restrictionEngine() {
        return new RestrictionEngineImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestrictionService restrictionService() {
        return new RestrictionServiceImpl();
    }


}
