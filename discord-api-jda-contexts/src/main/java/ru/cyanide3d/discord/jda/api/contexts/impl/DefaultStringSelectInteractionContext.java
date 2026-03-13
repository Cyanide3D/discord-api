package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.StringSelectInteractionContext;

import java.util.List;

public class DefaultStringSelectInteractionContext
        extends AbstractInteractionEventContext<StringSelectInteractionEvent>
        implements StringSelectInteractionContext {

    public DefaultStringSelectInteractionContext(StringSelectInteractionEvent event) {
        super(event);
    }

    @Override
    public List<String> getValues() {
        return getEvent().getValues();
    }

    @Override
    public String getComponentId() {
        return getEvent().getComponentId();
    }

}
