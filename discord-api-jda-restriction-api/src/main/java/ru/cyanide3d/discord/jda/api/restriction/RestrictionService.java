package ru.cyanide3d.discord.jda.api.restriction;

import net.dv8tion.jda.api.events.GenericEvent;

public interface RestrictionService {

    RestrictionResult check(Restriction<?> restriction, GenericEvent event);

}
