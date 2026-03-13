package ru.cyanide3d.discord.jda.event;

import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.event.AbstractRestActionHandler;
import ru.cyanide3d.discord.jda.api.event.DiscordJDAEventHandler;

public abstract class AbstractDiscordJDAEventHandler<T extends EventContext<?>>
        extends AbstractRestActionHandler
        implements DiscordJDAEventHandler<T> {



}
