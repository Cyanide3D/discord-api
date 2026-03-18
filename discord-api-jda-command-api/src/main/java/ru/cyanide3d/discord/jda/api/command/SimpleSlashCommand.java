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

import static ru.cyanide3d.utils.CastUtils.cast;

@Getter
public class SimpleSlashCommand extends AbstractSlashCommand {

    private final List<OptionSpec<?>> options;

    private final ExecutorSpec executorSpec;

    public SimpleSlashCommand(String name, String description, Restriction<SlashCommandContext> restriction, List<OptionSpec<?>> options, ExecutorSpec executorSpec) {
        super(name, description, restriction);
        this.options = new ArrayList<>(options);
        this.executorSpec = executorSpec;
    }

    @Override
    public CompiledSlashCommand compile(SlashExecutorResolver<?> resolver) {
        CommandIndex commandIndex = new CommandIndex();
        commandIndex.put(new SlashPath(getName()), getRestriction(), new ArrayList<>(getOptions()), resolver.resolve(cast(getExecutorSpec())));
        return new CompiledSlashCommand(this, commandIndex, buildCommandData());
    }

    public List<OptionSpec<?>> getOptions() {
        return Collections.unmodifiableList(options);
    }

    protected CommandData buildCommandData() {
        SlashCommandData data = Commands.slash(getName(), getDescription());
        for (OptionSpec<?> option : options) {
            data.addOptions(option.toOptionData());
        }
        return data;
    }
}