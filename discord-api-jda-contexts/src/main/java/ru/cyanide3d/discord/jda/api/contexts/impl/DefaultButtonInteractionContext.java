package ru.cyanide3d.discord.jda.api.contexts.impl;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.ButtonInteractionContext;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;

public class DefaultButtonInteractionContext
        extends AbstractInteractionResponseEventContext<ButtonInteractionEvent>
        implements ButtonInteractionContext {

    public DefaultButtonInteractionContext(ButtonInteractionEvent event, DiscordRestActionExecutor restActionExecutor) {
        super(event, restActionExecutor);
    }

    @Override
    public String getComponentId() {
        return getEvent().getComponentId();
    }

}