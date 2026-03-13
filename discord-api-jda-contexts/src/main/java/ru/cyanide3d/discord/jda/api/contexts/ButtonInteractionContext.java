package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonInteractionContext extends InteractionEventContext<ButtonInteractionEvent> {

    String getComponentId();

}