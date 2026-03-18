package ru.cyanide3d.discord.jda.api.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public final class ChoiceSpec {

    private enum Kind {
        STRING,
        LONG,
        DOUBLE
    }

    private final String name;

    private final Object value;

    private final Kind kind;

    private ChoiceSpec(String name, Object value, Kind kind) {
        this.name = name;
        this.value = value;
        this.kind = kind;
    }

    public static ChoiceSpec of(String name, String value) {
        return new ChoiceSpec(name, value, Kind.STRING);
    }

    public static ChoiceSpec of(String name, long value) {
        return new ChoiceSpec(name, value, Kind.LONG);
    }

    public static ChoiceSpec of(String name, double value) {
        return new ChoiceSpec(name, value, Kind.DOUBLE);
    }

    public void validateFor(OptionType optionType, String optionName, int index) {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException(
                    "Choice #" + index + " of option '" + optionName + "' has blank name"
            );
        }

        if (value == null) {
            throw new IllegalStateException(
                    "Choice #" + index + " of option '" + optionName + "' has null value"
            );
        }

        switch (kind) {
            case STRING -> {
                if (optionType != OptionType.STRING) {
                    throw new IllegalStateException(
                            "Choice #" + index + " of option '" + optionName + "' is STRING, but option type is " + optionType
                    );
                }

                String s = (String) value;
                if (s.isBlank()) {
                    throw new IllegalStateException(
                            "Choice #" + index + " of option '" + optionName + "' has blank string value"
                    );
                }
            }

            case LONG -> {
                if (optionType != OptionType.INTEGER) {
                    throw new IllegalStateException(
                            "Choice #" + index + " of option '" + optionName + "' is LONG, but option type is " + optionType
                    );
                }
            }

            case DOUBLE -> {
                if (optionType != OptionType.NUMBER) {
                    throw new IllegalStateException(
                            "Choice #" + index + " of option '" + optionName + "' is DOUBLE, but option type is " + optionType
                    );
                }

                double d = (double) value;
                if (Double.isNaN(d) || Double.isInfinite(d)) {
                    throw new IllegalStateException(
                            "Choice #" + index + " of option '" + optionName + "' has non-finite double value"
                    );
                }
            }
        }
    }

    public void apply(OptionData data) {
        switch (kind) {
            case STRING -> data.addChoice(name, (String) value);
            case LONG -> data.addChoice(name, (long) value);
            case DOUBLE -> data.addChoice(name, (double) value);
        }
    }
}