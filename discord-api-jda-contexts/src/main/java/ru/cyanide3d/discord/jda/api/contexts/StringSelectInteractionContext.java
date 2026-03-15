package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import ru.cyanide3d.discord.jda.api.contexts.capability.InteractionResponseCapability;

import java.util.List;

public interface StringSelectInteractionContext extends InteractionEventContext<StringSelectInteractionEvent>, InteractionResponseCapability {

    String getComponentId();

    List<String> getValues();

}