package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.capability.InteractionResponseCapability;

public interface ModalInteractionContext extends InteractionEventContext<ModalInteractionEvent>, InteractionResponseCapability {

    String getModalId();

}