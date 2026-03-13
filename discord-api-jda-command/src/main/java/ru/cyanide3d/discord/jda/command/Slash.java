package ru.cyanide3d.discord.jda.command;

import lombok.experimental.UtilityClass;
import ru.cyanide3d.discord.jda.api.command.GroupsCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SimpleCommandBuilder;
import ru.cyanide3d.discord.jda.api.command.SubcommandsCommandBuilder;

@UtilityClass
public class Slash {

    public static SimpleCommandBuilder simple(String name, String description) {
        return new SimpleCommandBuilderImpl(name, description);
    }

    public static SubcommandsCommandBuilder commands(String name, String description) {
        return new SubcommandsCommandBuilderImpl(name, description);
    }

    public static GroupsCommandBuilder groups(String name, String description) {
        return new GroupsCommandBuilderImpl(name, description);
    }

}