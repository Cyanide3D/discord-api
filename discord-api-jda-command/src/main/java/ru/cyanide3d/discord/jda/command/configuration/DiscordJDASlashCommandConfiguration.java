package ru.cyanide3d.discord.jda.command.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cyanide3d.discord.jda.api.command.SlashCommandCompiler;
import ru.cyanide3d.discord.jda.api.command.SlashCommandContextFactory;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistry;
import ru.cyanide3d.discord.jda.command.InstanceSlashExecutorResolver;
import ru.cyanide3d.discord.jda.command.SlashCommandCompilerImpl;
import ru.cyanide3d.discord.jda.command.SlashCommandContextFactoryImpl;
import ru.cyanide3d.discord.jda.command.SlashCommandDiscordJDAEventListenerAdapter;
import ru.cyanide3d.discord.jda.command.SlashCommandRegistryImpl;
import ru.cyanide3d.discord.jda.command.SpringSlashExecutorResolver;

@Configuration
public class DiscordJDASlashCommandConfiguration {

    @Bean
    public SlashCommandDiscordJDAEventListenerAdapter slashCommandDiscordJDAEventListenerAdapter() {
        return new SlashCommandDiscordJDAEventListenerAdapter();
    }

    @Bean
    @ConditionalOnMissingBean
    public SlashCommandRegistry slashCommandRegistry() {
        return new SlashCommandRegistryImpl();
    }

    @Bean
    public SpringSlashExecutorResolver<?> springSlashExecutorResolver() {
        return new SpringSlashExecutorResolver<>();
    }

    @Bean
    public InstanceSlashExecutorResolver<?> instanceSlashExecutorResolver() {
        return new InstanceSlashExecutorResolver<>();
    }

    @Bean
    public SlashCommandContextFactory slashCommandContextFactory() {
        return new SlashCommandContextFactoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SlashCommandCompiler slashCommandCompiler() {
        return new SlashCommandCompilerImpl();
    }

}
