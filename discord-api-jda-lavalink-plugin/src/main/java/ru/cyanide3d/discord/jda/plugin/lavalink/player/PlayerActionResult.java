package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import lombok.Getter;

@Getter
public class PlayerActionResult {

    private final boolean success;

    private final PlayerActionType actionType;

    private final String errorCode;

    protected PlayerActionResult(boolean success, PlayerActionType actionType, String errorCode) {
        this.success = success;
        this.actionType = actionType;
        this.errorCode = errorCode;
    }

    public static PlayerActionResult success(PlayerActionType actionType) {
        return new PlayerActionResult(true, actionType, null);
    }

    public static PlayerActionResult failure(PlayerActionType actionType, String errorCode) {
        return new PlayerActionResult(false, actionType, errorCode);
    }
}