package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import lombok.Getter;

@Getter
public class PlayerActionResult {

    private final PlayerResultStatus status;

    private final PlayerActionType actionType;

    private final String errorCode;

    protected PlayerActionResult(PlayerResultStatus status, PlayerActionType actionType, String errorCode) {
        this.status = status;
        this.actionType = actionType;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return status == PlayerResultStatus.SUCCESS;
    }

    public boolean isNoop() {
        return status == PlayerResultStatus.NOOP;
    }

    public boolean isFailure() {
        return status == PlayerResultStatus.FAILURE;
    }

    public static PlayerActionResult success(PlayerActionType actionType) {
        return new PlayerActionResult(PlayerResultStatus.SUCCESS, actionType, null);
    }

    public static PlayerActionResult noop(PlayerActionType actionType, String errorCode) {
        return new PlayerActionResult(PlayerResultStatus.NOOP, actionType, errorCode);
    }

    public static PlayerActionResult failure(PlayerActionType actionType, String errorCode) {
        return new PlayerActionResult(PlayerResultStatus.FAILURE, actionType, errorCode);
    }
}