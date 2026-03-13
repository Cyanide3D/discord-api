package ru.cyanide3d.discord.jda.api;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public interface DiscordJDABotCustomizer {

    default void customize(JDABuilder builder) {

    }

    default void customize(JDA bot) {

    }

}
