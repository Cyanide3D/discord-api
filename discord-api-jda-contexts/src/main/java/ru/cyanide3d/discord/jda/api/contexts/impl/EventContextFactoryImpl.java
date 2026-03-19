package ru.cyanide3d.discord.jda.api.contexts.impl;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.EventContextFactory;

import static ru.cyanide3d.utils.CastUtils.cast;

@Slf4j
public class EventContextFactoryImpl implements EventContextFactory {

    @Autowired
    private DiscordRestActionExecutor discordRestActionExecutor;

    @Override
    public <T extends GenericEvent> EventContext<T> create(T event) {
        return doCreate(event);
    }

    protected <T extends GenericEvent> EventContext<T> doCreate(T event) {
        if (event instanceof ButtonInteractionEvent e) {
            return cast(new DefaultButtonInteractionContext(e, discordRestActionExecutor));
        }
        if (event instanceof ModalInteractionEvent e) {
            return cast(new DefaultModalInteractionContext(e, discordRestActionExecutor));
        }
        if (event instanceof StringSelectInteractionEvent e) {
            return cast(new DefaultStringSelectInteractionContext(e, discordRestActionExecutor));
        }
        if (event instanceof MessageReceivedEvent e) {
            return cast(new DefaultMessageReceivedContext(e, discordRestActionExecutor));
        }
        if (event instanceof MessageReactionAddEvent e) {
            return cast(new DefaultMessageReactionAddContext(e, discordRestActionExecutor));
        }
        if (event instanceof MessageReactionRemoveEvent e) {
            return cast(new DefaultMessageReactionRemoveContext(e, discordRestActionExecutor));
        }
        if (event instanceof GuildMemberJoinEvent e) {
            return cast(new DefaultGuildMemberJoinContext(e));
        }
        if (event instanceof GuildMemberRemoveEvent e) {
            return cast(new DefaultGuildMemberRemoveContext(e));
        }
        if (event instanceof GuildVoiceUpdateEvent e) {
            return cast(new DefaultGuildVoiceUpdateContext(e));
        }
        if (event instanceof GuildJoinEvent e) {
            return cast(new DefaultGuildJoinContext(e));
        }
        if (event instanceof GuildLeaveEvent e) {
            return cast(new DefaultGuildLeaveContext(e));
        }
        if (event instanceof GenericGuildMemberEvent e) {
            return cast(new DefaultGenericGuildMemberEventContext<>(e));
        }
        if (event instanceof GenericGuildVoiceEvent e) {
            return cast(new DefaultGenericGuildVoiceEventContext<>(e));
        }
        if (event instanceof GenericMessageEvent e) {
            return cast(new DefaultGenericMessageEventContext<>(e, discordRestActionExecutor));
        }
        if (event instanceof GenericUserEvent e) {
            return cast(new DefaultGenericUserEventContext<>(e));
        }
        if (event instanceof GenericGuildEvent e) {
            return cast(new DefaultGenericGuildEventContext<>(e));
        }

        log.debug("Context for event {} not supported, fallback to default jda event context", event.getClass().getName());
        return cast(new DefaultJDAEventContext<>(event));
    }
}
