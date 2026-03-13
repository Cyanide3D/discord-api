package ru.cyanide3d.discord.jda.api.restriction.contexts;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Optional;

@AllArgsConstructor(staticName = "of")
public class SlashCommandContext implements EventContext {

    private SlashCommandInteractionEvent event;

    @Override
    public User user() {
        return event.getUser();
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @Override
    public Optional<Guild> guild() {
        return Optional.ofNullable(event.getGuild());
    }

    @Override
    public Optional<Member> member() {
        return Optional.ofNullable(event.getMember());
    }

    @Override
    public Optional<MessageChannel> channel() {
        return Optional.of(event.getChannel());
    }

}
