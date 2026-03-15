package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.StringSelectInteractionContext;

import java.util.List;

public class DefaultStringSelectInteractionContext
        extends AbstractInteractionResponseEventContext<StringSelectInteractionEvent>
        implements StringSelectInteractionContext {

    public DefaultStringSelectInteractionContext(StringSelectInteractionEvent event, DiscordRestActionExecutor restActionExecutor) {
        super(event, restActionExecutor);
    }

    @Override
    public String getComponentId() {
        return getEvent().getComponentId();
    }

    @Override
    public List<String> getValues() {
        return getEvent().getValues();
    }

}