package ru.cyanide3d.discord.jda.command;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import ru.cyanide3d.discord.jda.api.command.CommandIndex;
import ru.cyanide3d.discord.jda.api.command.CompiledSlashCommand;
import ru.cyanide3d.discord.jda.api.command.ResolvedSlashLeaf;
import ru.cyanide3d.discord.jda.api.command.SlashCommand;
import ru.cyanide3d.discord.jda.api.command.SlashCommandCompiler;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistry;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistryCustomizer;
import ru.cyanide3d.discord.jda.api.command.SlashPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SlashCommandRegistryImpl implements SlashCommandRegistry, InitializingBean {

    private final Map<String, CompiledSlashCommand> slashCommands = new LinkedHashMap<>();

    private final Map<SlashPath, ResolvedSlashLeaf> leaves = new HashMap<>();

    @Autowired
    private ObjectProvider<SlashCommandRegistryCustomizer> customizers;

    @Autowired
    private SlashCommandCompiler slashCommandCompiler;

    @Override
    public void register(@NonNull SlashCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        if (slashCommands.containsKey(command.getName())) {
            throw new IllegalStateException("Slash command already registered: " + command.getName());
        }

        CompiledSlashCommand compiledSlashCommand = compileCommand(command);
        CommandIndex commandIndex = compiledSlashCommand.getCommandIndex();

        registerLeaves(command, commandIndex);

        this.slashCommands.put(command.getName(), compiledSlashCommand);
    }

    protected void registerLeaves(SlashCommand command, CommandIndex commandIndex) {
        List<SlashPath> inserted = new ArrayList<>();

        try {
            for (Map.Entry<SlashPath, ResolvedSlashLeaf> entry : commandIndex.getLeaves().entrySet()) {
                SlashPath path = entry.getKey();
                ResolvedSlashLeaf leaf = entry.getValue();

                if (path == null) {
                    throw new IllegalStateException("Leaf path is null for command: " + command.getName());
                }

                if (leaf == null) {
                    throw new IllegalStateException("Leaf is null for path: " + path + ", command: " + command.getName());
                }

                ResolvedSlashLeaf previous = leaves.putIfAbsent(path, leaf);
                if (previous != null) {
                    throw new IllegalStateException(
                            "Duplicate slash path detected: " + path + ", command: " + command.getName()
                    );
                }

                inserted.add(path);
            }
        } catch (RuntimeException ex) {
            for (SlashPath path : inserted) {
                leaves.remove(path);
            }
            throw ex;
        }
    }

    protected CompiledSlashCommand compileCommand(SlashCommand command) {
        return slashCommandCompiler.compile(command);
    }

    @Override
    public List<CommandData> getCommandDatas() {
        return slashCommands.values().stream()
                .map(CompiledSlashCommand::getCommandData)
                .toList();
    }

    @Override
    public Optional<ResolvedSlashLeaf> findLeaf(@NonNull SlashPath path) {
        Objects.requireNonNull(path, "Path cannot be null");
        return Optional.ofNullable(leaves.get(path));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        customizers.forEach((customizer) -> customizer.customize(this));
    }

}
