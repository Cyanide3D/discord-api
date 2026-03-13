package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.ModalInteractionContext;

public class DefaultModalInteractionContext
        extends AbstractInteractionEventContext<ModalInteractionEvent>
        implements ModalInteractionContext {

    public DefaultModalInteractionContext(ModalInteractionEvent event) {
        super(event);
    }

    @Override
    public String getModalId() {
        return getEvent().getModalId();
    }

}
