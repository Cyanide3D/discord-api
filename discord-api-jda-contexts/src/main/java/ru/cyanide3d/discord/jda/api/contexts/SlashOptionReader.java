package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public interface SlashOptionReader<T> {

    String getName();

    boolean isRequired();

    T read(OptionMapping mapping);
}