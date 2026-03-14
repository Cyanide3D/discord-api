package ru.cyanide3d.discord.jda.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cyanide3d.discord.jda.DiscordRestActionExecutorImpl;
import ru.cyanide3d.discord.jda.api.DiscordRestActionExecutor;

@Configuration
public class DiscordJDAConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DiscordRestActionExecutor discordRestActionExecutor() {
        return new DiscordRestActionExecutorImpl();
    }

}
