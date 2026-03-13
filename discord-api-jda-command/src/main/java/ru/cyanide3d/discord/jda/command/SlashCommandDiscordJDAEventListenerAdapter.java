package ru.cyanide3d.discord.jda.command;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.AutoEnabledEventListener;
import ru.cyanide3d.discord.jda.api.command.ResolvedSlashLeaf;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistry;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.EventContextFactory;
import ru.cyanide3d.discord.jda.api.contexts.SlashPath;
import ru.cyanide3d.discord.jda.api.event.AbstractDiscordJDAEventListenerAdapter;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionService;

import java.util.List;
import java.util.Optional;

import static ru.cyanide3d.utils.CastUtils.cast;

@Slf4j
public class SlashCommandDiscordJDAEventListenerAdapter extends AbstractDiscordJDAEventListenerAdapter implements AutoEnabledEventListener {

    @Autowired
    private SlashCommandRegistry slashCommandRegistry;

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private EventContextFactory eventContextFactory;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        SlashPath slashPath = SlashPath.fromEvent(event);
        if (log.isDebugEnabled()) {
            log.debug("Slash command interaction received: {}", slashPath);
        }

        Optional<ResolvedSlashLeaf> leafOpt = slashCommandRegistry.findLeaf(slashPath);
        if (leafOpt.isEmpty()) {
            handleCommandNotFound(event, slashPath);
            return;
        }

        ResolvedSlashLeaf leaf = leafOpt.get();
        EventContext<?> eventContext = createEventContext(event);
        RestrictionResult restrictionResult = checkRestriction(leaf.getRestriction(), eventContext);
        if (restrictionResult.isAllowed()) {
            runCommand(event, eventContext, leaf.getExecutor());
        } else {
            failedCheckRestrictionNotification(restrictionResult, event);
        }
    }

    protected EventContext<SlashCommandInteractionEvent> createEventContext(@NotNull SlashCommandInteractionEvent event) {
        return eventContextFactory.create(event);
    }

    protected void failedCheckRestrictionNotification(RestrictionResult restrictionResult, SlashCommandInteractionEvent event) {
        queue(event.reply(restrictionResult.getReason()).setEphemeral(true)); //TODO
    }

    protected RestrictionResult checkRestriction(Restriction<?> restriction, EventContext<?> eventContext) {
        return restrictionService.check(restriction, eventContext);
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

    protected void handleCommandNotFound(@NotNull SlashCommandInteractionEvent event, SlashPath slashKey) {
        queue(event.reply("Команда не поддерживается: " + slashKey.getCommand() + " " + slashKey.getSubcommand())
                .setEphemeral(true), "handleCommandNotFound");

        log.debug("Trying to send command when command not found: {}", slashKey);
    }

    protected void runCommand(SlashCommandInteractionEvent event, EventContext<?> eventContext, SlashExecutor slashExecutor) {
        try {
            slashExecutor.execute(cast(eventContext));
        } catch (Exception ex) {
            if (event.isAcknowledged()) {
               queue(event.getHook().sendMessage("Ошибка при выполнении команды.").setEphemeral(true), "runCommandError");
            } else {
                queue(event.reply("Ошибка при выполнении команды.").setEphemeral(true), "runCommandError");
            }
            log.warn("Error while executing: {}", SlashPath.fromEvent(event), ex);
        }
    }

}
