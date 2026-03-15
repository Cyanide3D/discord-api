package ru.cyanide3d.discord.jda.api.restriction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class RestrictionResult {

    private final boolean allowed;

    private final String reason;

    public RestrictionResult(boolean allowed, String reason) {
        this.allowed = allowed;
        this.reason = reason;
    }

    public static RestrictionResult allow() {
        return new RestrictionResult(true, null);
    }

    public static RestrictionResult deny() {
        return new RestrictionResult(false, null);
    }

    public static RestrictionResult deny(String reason) {
        return new RestrictionResult(false, reason);
    }

}
