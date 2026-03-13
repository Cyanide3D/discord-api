package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.ButtonInteractionContext;

public class DefaultButtonInteractionContext
        extends AbstractInteractionEventContext<ButtonInteractionEvent>
        implements ButtonInteractionContext {

    public DefaultButtonInteractionContext(ButtonInteractionEvent event) {
        super(event);
    }

    @Override
    public String getComponentId() {
        return getEvent().getComponentId();
    }

}
