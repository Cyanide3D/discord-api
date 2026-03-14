package ru.cyanide3d.discord.jda.api.contexts.impl;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.EventContextFactory;

import static ru.cyanide3d.utils.CastUtils.cast;

@Slf4j
public class EventContextFactoryImpl implements EventContextFactory {

    @Override
    public <T extends GenericEvent> EventContext<T> create(T event) {
        return doCreate(event);
    }

    protected <T extends GenericEvent> EventContext<T> doCreate(T event) {
        if (event instanceof SlashCommandInteractionEvent e) {
            return cast(new DefaultSlashCommandContext(e));
        }
        if (event instanceof StringSelectInteractionEvent e) {
            return cast(new DefaultStringSelectInteractionContext(e));
        }
        if (event instanceof ModalInteractionEvent e) {
            return cast(new DefaultModalInteractionContext(e));
        }
        if (event instanceof MessageReceivedEvent e) {
            return cast(new DefaultMessageReceivedContext(e));
        }
        if (event instanceof MessageReactionAddEvent e) {
            return cast(new DefaultMessageReactionAddContext(e));
        }
        if (event instanceof MessageReactionRemoveEvent e) {
            return cast(new DefaultMessageReactionRemoveContext(e));
        }
        if (event instanceof GuildMemberRemoveEvent e) {
            return cast(new DefaultGuildMemberRemoveContext(e));
        }
        if (event instanceof GuildMemberJoinEvent e) {
            return cast(new DefaultGuildMemberJoinContext(e));
        }
        if (event instanceof ButtonInteractionEvent e) {
            return cast(new DefaultButtonInteractionContext(e));
        }

        log.debug("context for event {} not supported. fallback to default event context", event.getClass().getName());
        return new DefaultJDAEventContext<>(event);
    }

}
