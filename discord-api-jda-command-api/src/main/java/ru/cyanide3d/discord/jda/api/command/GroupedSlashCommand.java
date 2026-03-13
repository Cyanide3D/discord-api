package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.contexts.SlashCommandContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.cyanide3d.discord.jda.api.restriction.Restrictions.and;
import static ru.cyanide3d.utils.CastUtils.cast;

@Getter
public class GroupedSlashCommand extends AbstractSlashCommand {

    private final List<SubcommandGroupSpec> groups;

    public GroupedSlashCommand(String name, String description, Restriction<SlashCommandContext> restriction, List<SubcommandGroupSpec> groups) {
        super(name, description, restriction);
        this.groups = new ArrayList<>(groups);
    }

    @Override
    public CompiledSlashCommand compile(SlashExecutorResolver<?> resolver) {
        return new CompiledSlashCommand(this, buildCommandIndex(resolver), buildCommandData());
    }

    public List<SubcommandGroupSpec> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    protected CommandData buildCommandData() {
        SlashCommandData data = Commands.slash(getName(), getDescription());
        for (SubcommandGroupSpec group : groups) {
            data.addSubcommandGroups(group.toGroupData());
        }
        return data;
    }

    protected CommandIndex buildCommandIndex(SlashExecutorResolver<?> resolver) {
        CommandIndex index = new CommandIndex();

        getGroups().forEach(group ->
                group.getSubcommands().forEach(sub ->
                        index.put(new SlashPath(getName(), group.getName(), sub.getName()), and(getRestriction(), sub.getRestriction()), resolver.resolve(cast(sub.getExecutorSpec()))))
        );

        return index;
    }

}