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

import java.util.List;

@Slf4j
public class SlashCommandDiscordJDAEventListenerAdapter extends AbstractDiscordJDAEventListenerAdapter implements AutoEnabledEventListener {

    @Autowired
    private SlashCommandRegistry slashCommandRegistry;

    @Autowired
    private SlashCommandDispatcher slashCommandDispatcher;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
       slashCommandDispatcher.dispatch(event);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandDataList = slashCommandRegistry.getCommandDatas();

        log.info("Found {} command definitions", commandDataList.size());

        queue(event.getJDA().updateCommands()
                .addCommands(commandDataList), "updateCommands", this::logCommands);
    }

    private void logCommands(List<Command> commands) {
        for (Command command : commands) {
            log.info("Enabled {} command: {}", command.getFullCommandName(), command.getDescription());
        }
    }

}
