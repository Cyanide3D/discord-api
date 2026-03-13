package ru.cyanide3d.discord.jda.command;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.cyanide3d.discord.jda.api.command.CommandIndex;
import ru.cyanide3d.discord.jda.api.command.CompiledSlashCommand;
import ru.cyanide3d.discord.jda.api.command.SlashCommand;
import ru.cyanide3d.discord.jda.api.command.SlashCommandCompiler;
import ru.cyanide3d.discord.jda.api.command.SlashExecutorResolver;

import java.util.List;

public class SlashCommandCompilerImpl implements SlashCommandCompiler {

    @Autowired
    private ObjectProvider<SlashExecutorResolver<?>> slashExecutorResolverProvider;

    @Override
    public CompiledSlashCommand compile(SlashCommand command) {
        CompiledSlashCommand compiled = command.compile(getSlashExecutorResolver());
        validateCompiledCommand(compiled);
        return compiled;
    }

    protected void validateCompiledCommand(CompiledSlashCommand compiledSlashCommand) {
        if (compiledSlashCommand == null) {
            throw new IllegalStateException("Compiled slash command is null");
        }

        SlashCommand command = compiledSlashCommand.getSource();
        if (command == null) {
            throw new IllegalStateException("Compiled slash command is null");
        }

        if (!StringUtils.hasText(command.getName())) {
            throw new IllegalStateException("Slash command name cannot be empty");
        }

        if (!StringUtils.hasText(command.getDescription())) {
            throw new IllegalStateException("Slash command description cannot be empty");
        }

        if (compiledSlashCommand.getCommandData() == null) {
            throw new IllegalStateException("CommandData is null for command: " + command.getName());
        }

        CommandIndex commandIndex = compiledSlashCommand.getCommandIndex();
        if (commandIndex == null) {
            throw new IllegalStateException("CommandIndex is null for command: " + command.getName());
        }

        if (commandIndex.getLeaves() == null || commandIndex.getLeaves().isEmpty()) {
            throw new IllegalStateException("No leaves found for command: " + command.getName());
        }
    }

    protected SlashExecutorResolver<?> getSlashExecutorResolver() {
        List<SlashExecutorResolver<?>> resolvers = slashExecutorResolverProvider.stream().toList();
        return new CompositeSlashExecutorResolver(resolvers);
    }

}
