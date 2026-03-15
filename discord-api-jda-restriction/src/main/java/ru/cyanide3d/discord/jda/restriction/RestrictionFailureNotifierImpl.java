package ru.cyanide3d.discord.jda.restriction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.capability.InteractionResponseCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageChannelSendCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageReplyCapability;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionFailureNotifier;
import ru.cyanide3d.discord.jda.api.restriction.RestrictionResult;

@Slf4j
public class RestrictionFailureNotifierImpl implements RestrictionFailureNotifier {

    @Override
    public void notify(RestrictionResult result, EventContext<?> context) {
        String reason = result.getReason();
        if (!StringUtils.hasText(reason)) {
            return;
        }

        try {
            if (context instanceof InteractionResponseCapability interaction) {
                interaction.reply(reason);
                return;
            }

            if (context instanceof MessageReplyCapability messageReply) {
                messageReply.reply(reason);
                return;
            }

            if (context instanceof MessageChannelSendCapability channelSend) {
                channelSend.sendMessage(reason);
            }
        } catch (Exception e) {
            log.warn("Failed to notify restriction denial for event context: {}", context.getClass().getName(), e);
        }
    }

}
