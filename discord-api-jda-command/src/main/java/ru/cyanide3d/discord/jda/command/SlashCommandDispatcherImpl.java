package ru.cyanide3d.discord.jda.command;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.ResolvedSlashLeaf;
import ru.cyanide3d.discord.jda.api.command.SlashCommandContextFactory;
import ru.cyanide3d.discord.jda.api.command.SlashCommandDispatcher;
import ru.cyanide3d.discord.jda.api.command.SlashCommandRegistry;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.contexts.DiscordRestActionExecutor;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.api.contexts.SlashPath;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionFailureNotifier;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionService;

import java.util.Optional;

import static ru.cyanide3d.utils.CastUtils.cast;

@Slf4j
public class SlashCommandDispatcherImpl implements SlashCommandDispatcher {

    @Autowired
    private SlashCommandRegistry slashCommandRegistry;

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private SlashCommandContextFactory contextFactory;

    @Autowired
    private RestrictionFailureNotifier restrictionFailureNotifier;

    @Autowired
    private DiscordRestActionExecutor actionExecutor;

    @Override
    public void dispatch(SlashCommandInteractionEvent event) {
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
        SlashCommandContext eventContext = createEventContext(event, leaf);
        RestrictionResult restrictionResult = checkRestriction(leaf.getRestriction(), eventContext);
        if (restrictionResult.isAllowed()) {
            runCommand(event, eventContext, leaf.getExecutor());
        } else {
            failedCheckRestrictionNotification(restrictionResult, eventContext);
        }
    }

    protected SlashCommandContext createEventContext(@NotNull SlashCommandInteractionEvent event, @NotNull ResolvedSlashLeaf leaf) {
        return contextFactory.create(event, leaf);
    }

    protected void failedCheckRestrictionNotification(RestrictionResult restrictionResult, EventContext<?> eventContext) {
        restrictionFailureNotifier.notify(eventContext, restrictionResult);
    }

    protected RestrictionResult checkRestriction(Restriction<?> restriction, EventContext<?> eventContext) {
        return restrictionService.check(restriction, eventContext);
    }

    protected void handleCommandNotFound(@NotNull SlashCommandInteractionEvent event, SlashPath slashKey) {
        actionExecutor.queue(event.reply("Команда не поддерживается: " + slashKey)
                .setEphemeral(true), "handleCommandNotFound");

        log.debug("Trying to send command when command not found: {}", slashKey);
    }

    protected void runCommand(SlashCommandInteractionEvent event, EventContext<?> eventContext, SlashExecutor slashExecutor) {
        try {
            slashExecutor.execute(cast(eventContext));
        } catch (Exception ex) {
            if (event.isAcknowledged()) {
                actionExecutor.queue(event.getHook().sendMessage("Ошибка при выполнении команды.").setEphemeral(true), "runCommandError");
            } else {
                actionExecutor.queue(event.reply("Ошибка при выполнении команды.").setEphemeral(true), "runCommandError");
            }
            log.warn("Error while executing: {}", SlashPath.fromEvent(event), ex);
        }
    }

}
