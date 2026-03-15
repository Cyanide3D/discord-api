package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.capability.InteractionResponseCapability;

public interface ButtonInteractionContext extends InteractionEventContext<ButtonInteractionEvent>, InteractionResponseCapability {

    String getComponentId();

}