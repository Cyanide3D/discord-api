package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface ModalInteractionContext extends InteractionEventContext<ModalInteractionEvent> {

    String getModalId();

}