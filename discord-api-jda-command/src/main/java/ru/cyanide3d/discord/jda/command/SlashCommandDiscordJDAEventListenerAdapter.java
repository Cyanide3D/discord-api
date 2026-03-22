package ru.cyanide3d.discord.jda.command;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.AutoEnabledEventListener;
import ru.cyanide3d.discord.jda.api.command.SlashCommandDispatcher;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistry;
import ru.cyanide3d.discord.jda.api.event.AbstractDiscordJDAEventListenerAdapter;
import ru.cyanide3d.discord.jda.command.properties.DiscordJDASlashCommandProperties;
import ru.cyanide3d.discord.jda.command.properties.SlashCommandSyncMode;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SlashCommandDiscordJDAEventListenerAdapter extends AbstractDiscordJDAEventListenerAdapter implements AutoEnabledEventListener {

    @Autowired
    private SlashCommandRegistry slashCommandRegistry;

    @Autowired
    private SlashCommandDispatcher slashCommandDispatcher;

    @Autowired
    private DiscordJDASlashCommandProperties slashCommandProperties;

    private final AtomicBoolean synced = new AtomicBoolean(false);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        slashCommandDispatcher.dispatch(event);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        if (!synced.compareAndSet(false, true)) {
            return;
        }

        List<CommandData> commandDataList = slashCommandRegistry.getCommandDatas();

        log.info("Found {} command definitions", commandDataList.size());

        SlashCommandSyncMode syncMode = slashCommandProperties.getSyncMode();
        if (syncMode == null) {
            syncMode = SlashCommandSyncMode.DISABLED;
        }

        switch (syncMode) {
            case DISABLED -> log.info("Slash command sync is disabled");
            case UPSERT_GLOBAL -> upsertGlobalCommands(event, commandDataList);
            case REPLACE_GLOBAL -> replaceGlobalCommands(event, commandDataList);
        }
    }

    protected void replaceGlobalCommands(ReadyEvent event, List<CommandData> commandDataList) {
        queue(event.getJDA().updateCommands().addCommands(commandDataList), "updateCommands", this::logCommands);
    }

    protected void upsertGlobalCommands(ReadyEvent event, List<CommandData> commandDataList) {
        for (CommandData commandData : commandDataList) {
            queue(event.getJDA().upsertCommand(commandData), "upsertCommand", this::logCommand);
        }
    }

    private void logCommands(List<Command> commands) {
        for (Command command : commands) {
            logCommand(command);
        }
    }

    private void logCommand(Command command) {
        log.info("Enabled {} command: {}", command.getFullCommandName(), command.getDescription());
    }
}