package ru.cyanide3d.discord.jda.api.restriction.contexts;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.Optional;

public interface EventContext {

    User user();

    boolean isBot();

    Optional<Guild> guild();

    Optional<Member> member();

    Optional<MessageChannel> channel();

}
