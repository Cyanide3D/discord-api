package ru.cyanide3d.discord.jda.api.restriction;

import net.dv8tion.jda.api.entities.Message;

import java.util.Optional;

public interface HasMessage {

    Optional<Message> message();

}
