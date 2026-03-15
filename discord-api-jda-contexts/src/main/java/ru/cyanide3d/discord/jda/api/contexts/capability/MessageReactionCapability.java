package ru.cyanide3d.discord.jda.api.contexts.capability;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public interface MessageReactionCapability {

    void addReaction(Emoji emoji);

}