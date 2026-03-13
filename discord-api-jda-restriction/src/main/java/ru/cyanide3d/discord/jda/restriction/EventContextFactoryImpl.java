package ru.cyanide3d.discord.jda.restriction;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.cyanide3d.discord.jda.api.restriction.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.restriction.EventContextFactory;
import ru.cyanide3d.discord.jda.api.restriction.contexts.SlashCommandContext;

import java.util.Optional;

public class EventContextFactoryImpl implements EventContextFactory {

    @Override
    public EventContext create(GenericEvent event) {
        Optional<EventContext> eventContext = doCreate(event);
        if (eventContext.isEmpty()) {
            throw new IllegalStateException("No EventContext found for event " + event);
        }
        return eventContext.get();
    }

    protected Optional<EventContext> doCreate(GenericEvent event) {
        if (event instanceof SlashCommandInteractionEvent e) {
            return Optional.of(SlashCommandContext.of(e));
        }
        return Optional.empty();
    }

}
