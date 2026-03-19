package ru.cyanide3d.discord.jda.restriction;

import lombok.extern.slf4j.Slf4j;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.capability.InteractionResponseCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageChannelSendCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageReplyCapability;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionFailureNotifier;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;

@Slf4j
public class RestrictionFailureNotifierImpl implements RestrictionFailureNotifier {

    @Override
    public void notifyDenied(EventContext<?> context, RestrictionResult result) {
        if (context instanceof InteractionResponseCapability interaction) {
            String message = getDeniedMessage(result);
            interaction.replyEphemeral(message);
            return;
        }

        if (context instanceof MessageReplyCapability messageReplyCapability) {
            String message = getDeniedMessage(result);
            messageReplyCapability.reply(message);
            return;
        }

        if (context instanceof MessageChannelSendCapability channelSendCapability) {
            String message = getDeniedMessage(result);
            channelSendCapability.sendMessage(message);
            return;
        }
    }

    @Override
    public void notifyError(EventContext<?> context, RestrictionResult result) {
        if (context instanceof InteractionResponseCapability interaction) {
            interaction.replyEphemeral(getErrorMessage(result));
            return;
        }

        if (context instanceof MessageReplyCapability replyCapability) {
            replyCapability.reply(getErrorMessage(result));
            return;
        }

        if (context instanceof MessageChannelSendCapability channelSendCapability) {
            String message = getDeniedMessage(result);
            channelSendCapability.sendMessage(message);
            return;
        }
    }

    protected String getDeniedMessage(RestrictionResult result) {
        return result.getReason() != null
                ? result.getReason()
                : "Недостаточно прав для выполнения этой команды.";
    }

    protected String getErrorMessage(RestrictionResult result) {
        return "Во время проверки ограничений произошла внутренняя ошибка.";
    }
}
