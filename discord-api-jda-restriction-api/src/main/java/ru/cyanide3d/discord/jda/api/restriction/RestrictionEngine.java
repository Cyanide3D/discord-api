package ru.cyanide3d.discord.jda.api.restriction;


import ru.cyanide3d.discord.jda.api.contexts.EventContext;

public interface RestrictionEngine {

    <T extends EventContext<?>> RestrictionResult check(Restriction<T> restriction, T ctx);

    <T extends EventContext<?>> void enforce(Restriction<T> restriction, T ctx);

}
