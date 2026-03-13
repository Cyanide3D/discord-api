package ru.cyanide3d.discord.jda.api.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceSpec {

    private final String name;

    private final Object value;

    public static ChoiceSpec of(String name, String value) {
        return new ChoiceSpec(name, value);
    }

    public static ChoiceSpec of(String name, long value) {
        return new ChoiceSpec(name, value);
    }

    public static ChoiceSpec of(String name, double value) {
        return new ChoiceSpec(name, value);
    }

    public void apply(OptionData data) {
        if (value instanceof String s) {
            data.addChoice(name, s);
            return;
        }
        if (value instanceof Long l) {
            data.addChoice(name, l);
            return;
        }
        if (value instanceof Double d) {
            data.addChoice(name, d);
            return;
        }
        throw new IllegalStateException("Unsupported choice value type: " + value.getClass().getName());
    }

}