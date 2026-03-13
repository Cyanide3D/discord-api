package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class SubcommandGroupSpec {

    private final String name;

    private final String description;

    private final List<SubcommandSpec> subcommands;

    public SubcommandGroupSpec(String name, String description, List<SubcommandSpec> subcommands) {
        this.name = name;
        this.description = description;
        this.subcommands = new ArrayList<>(subcommands);
    }

    public List<SubcommandSpec> getSubcommands() {
        return Collections.unmodifiableList(subcommands);
    }

    public SubcommandGroupData toGroupData() {
        SubcommandGroupData data = new SubcommandGroupData(name, description);
        for (SubcommandSpec subcommand : subcommands) {
            data.addSubcommands(subcommand.toSubcommandData());
        }
        return data;
    }

}