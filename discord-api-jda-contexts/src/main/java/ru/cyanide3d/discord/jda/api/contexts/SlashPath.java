package ru.cyanide3d.discord.jda.api.contexts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@EqualsAndHashCode
public class SlashPath {

    private final String command;

    private final String group;

    private final String subcommand;

    public SlashPath(String command) {
        this(command, null, null);
    }

    public SlashPath(String command, String subcommand) {
        this(command, null, subcommand);
    }

    public SlashPath(String command, String group, String subcommand) {
        this.command = command;
        this.group = group;
        this.subcommand = subcommand;
    }

    public static SlashPath fromEvent(SlashCommandInteractionEvent event) {
        return new SlashPath(
                event.getName(),
                event.getSubcommandGroup(),
                event.getSubcommandName()
        );
    }

    public boolean isRoot() {
        return group == null && subcommand == null;
    }

    public boolean isGrouped() {
        return group != null;
    }

    @Override
    public String toString() {
        return Stream.of(command, group, subcommand)
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(" "));
    }
}