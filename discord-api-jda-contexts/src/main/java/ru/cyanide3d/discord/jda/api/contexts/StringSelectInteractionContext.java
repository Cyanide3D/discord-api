package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.List;

public interface StringSelectInteractionContext extends InteractionEventContext<StringSelectInteractionEvent> {

    List<String> getValues();

    String getComponentId();

}