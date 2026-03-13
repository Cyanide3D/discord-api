package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.EventContextFactory;

import static ru.cyanide3d.utils.CastUtils.cast;

public class EventContextFactoryImpl implements EventContextFactory {

    @Override
    public <T extends GenericEvent> EventContext<T> create(T event) {
        return doCreate(event);
    }

    protected <T extends GenericEvent> EventContext<T> doCreate(T event) {
        if (event instanceof SlashCommandInteractionEvent e) {
            return cast(new DefaultSlashCommandContext(e));
        }
        throw new IllegalStateException("Unsupported event type: " + event.getClass().getName());
    }

}
