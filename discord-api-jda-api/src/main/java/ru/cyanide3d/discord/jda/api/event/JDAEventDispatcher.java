package ru.cyanide3d.discord.jda.api.event;

import net.dv8tion.jda.api.events.GenericEvent;

public interface JDAEventDispatcher {

    void dispatch(GenericEvent event);

}
