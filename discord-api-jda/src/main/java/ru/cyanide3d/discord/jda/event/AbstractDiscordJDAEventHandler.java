package ru.cyanide3d.discord.jda.event;

import net.dv8tion.jda.api.events.GenericEvent;
import ru.cyanide3d.discord.jda.api.event.AbstractRestActionHandler;
import ru.cyanide3d.discord.jda.api.event.DiscordJDAEventHandler;

public abstract class AbstractDiscordJDAEventHandler<T extends GenericEvent>
        extends AbstractRestActionHandler
        implements DiscordJDAEventHandler<T> {



}
