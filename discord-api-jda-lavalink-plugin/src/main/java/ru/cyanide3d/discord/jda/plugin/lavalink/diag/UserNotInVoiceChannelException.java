package ru.cyanide3d.discord.jda.plugin.lavalink.diag;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserNotInVoiceChannelException extends RuntimeException {

    private final Long userId;

}
