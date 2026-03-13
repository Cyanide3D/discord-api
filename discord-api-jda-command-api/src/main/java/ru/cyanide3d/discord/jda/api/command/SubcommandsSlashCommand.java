package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import ru.cyanide3d.discord.jda.api.contexts.SlashPath;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.cyanide3d.discord.jda.api.restriction.Restrictions.and;
import static ru.cyanide3d.utils.CastUtils.cast;

@Getter
public class SubcommandsSlashCommand extends AbstractSlashCommand {

    private final List<SubcommandSpec> subcommands;

    public SubcommandsSlashCommand(String name, String description, Restriction<SlashCommandContext> restriction, List<SubcommandSpec> subcommands) {
        super(name, description, restriction);
        this.subcommands = new ArrayList<>(subcommands);
    }

    @Override
    public CompiledSlashCommand compile(SlashExecutorResolver<?> resolver) {
        return new CompiledSlashCommand(this, buildCommandIndex(resolver), buildCommandData());
    }

    public List<SubcommandSpec> getSubcommands() {
        return Collections.unmodifiableList(subcommands);
    }

    protected CommandData buildCommandData() {
        SlashCommandData data = Commands.slash(getName(), getDescription());
        for (SubcommandSpec subcommand : subcommands) {
            data.addSubcommands(subcommand.toSubcommandData());
        }
        return data;
    }

    protected CommandIndex buildCommandIndex(SlashExecutorResolver<?> resolver) {
        CommandIndex index = new CommandIndex();

        getSubcommands().forEach(subcommand ->
                index.put(new SlashPath(getName(), subcommand.getName()), and(getRestriction(), subcommand.getRestriction()), resolver.resolve(cast(subcommand.getExecutorSpec()))));

        return index;
    }

}