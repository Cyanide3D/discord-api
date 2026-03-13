package ru.cyanide3d.discord.jda.api.contexts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Getter
@ToString
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

}