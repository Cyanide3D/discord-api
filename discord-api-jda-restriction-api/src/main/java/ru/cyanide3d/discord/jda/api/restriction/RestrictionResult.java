package ru.cyanide3d.discord.jda.api.restriction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class RestrictionResult {

    public enum Status {
        ALLOWED,
        DENIED,
        ERROR
    }

    private final Status status;

    private final String reason;

    private final Throwable cause;

    private RestrictionResult(Status status, String reason, Throwable cause) {
        this.status = status;
        this.reason = reason;
        this.cause = cause;
    }

    public static RestrictionResult allow() {
        return new RestrictionResult(Status.ALLOWED, null, null);
    }

    public static RestrictionResult deny() {
        return new RestrictionResult(Status.DENIED, null, null);
    }

    public static RestrictionResult deny(String reason) {
        return new RestrictionResult(Status.DENIED, reason, null);
    }

    public static RestrictionResult error(Throwable cause) {
        return new RestrictionResult(Status.ERROR, null, cause);
    }

    public static RestrictionResult error(String reason, Throwable cause) {
        return new RestrictionResult(Status.ERROR, reason, cause);
    }

    public Status getStatus() {
        return status;
    }

    public boolean isAllowed() {
        return status == Status.ALLOWED;
    }

    public boolean isDenied() {
        return status == Status.DENIED;
    }

    public boolean isError() {
        return status == Status.ERROR;
    }

    public String getReason() {
        return reason;
    }

    public Throwable getCause() {
        return cause;
    }
}