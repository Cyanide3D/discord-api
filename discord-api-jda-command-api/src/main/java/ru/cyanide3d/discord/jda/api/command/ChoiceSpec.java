package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public final class ChoiceSpec {

    private enum Kind {
        STRING,
        LONG,
        DOUBLE
    }

    private final String name;

    private final Object value;

    private final Kind kind;

    private final Map<DiscordLocale, String> nameLocalizations;

    private ChoiceSpec(String name, Object value, Kind kind, Map<DiscordLocale, String> nameLocalizations) {
        this.name = Objects.requireNonNull(name, "name");
        this.value = Objects.requireNonNull(value, "value");
        this.kind = Objects.requireNonNull(kind, "kind");
        this.nameLocalizations = Map.copyOf(Objects.requireNonNull(nameLocalizations, "nameLocalizations"));
    }

    public static ChoiceSpec of(String name, String value) {
        return new ChoiceSpec(name, value, Kind.STRING, Map.of());
    }

    public static ChoiceSpec of(String name, long value) {
        return new ChoiceSpec(name, value, Kind.LONG, Map.of());
    }

    public static ChoiceSpec of(String name, double value) {
        return new ChoiceSpec(name, value, Kind.DOUBLE, Map.of());
    }

    public ChoiceSpec nameLocalization(DiscordLocale locale, String localizedName) {
        LinkedHashMap<DiscordLocale, String> copy = new LinkedHashMap<>(nameLocalizations);
        copy.put(Objects.requireNonNull(locale, "locale"), Objects.requireNonNull(localizedName, "localizedName"));

        return new ChoiceSpec(name, value, kind, copy);
    }

    public ChoiceSpec nameLocalizations(Map<DiscordLocale, String> localizations) {
        LinkedHashMap<DiscordLocale, String> copy = new LinkedHashMap<>(nameLocalizations);
        copy.putAll(Objects.requireNonNull(localizations, "localizations"));

        return new ChoiceSpec(name, value, kind, copy);
    }

    public ChoiceSpec clearNameLocalizations() {
        return new ChoiceSpec(name, value, kind, Map.of());
    }

    public void validateFor(OptionType optionType, String optionName, int index) {
        requireNonBlank(name, "Choice #" + index + " of option '" + optionName + "' has blank name");

        for (Map.Entry<DiscordLocale, String> entry : nameLocalizations.entrySet()) {
            DiscordLocale locale = entry.getKey();
            String localizedName = entry.getValue();

            if (locale == null) {
                throw new IllegalStateException("Choice #" + index + " of option '" + optionName + "' has null localization locale");
            }

            requireNonBlank(localizedName, "Choice #" + index + " of option '" + optionName + "' has blank localized name for locale " + locale);
        }

        switch (kind) {
            case STRING -> validateStringChoice(optionType, optionName, index);
            case LONG -> validateLongChoice(optionType, optionName, index);
            case DOUBLE -> validateDoubleChoice(optionType, optionName, index);
            default -> throw new IllegalStateException("Unsupported choice kind: " + kind);
        }
    }

    public void apply(OptionData data) {
        data.addChoices(toCommandChoice());
    }

    public Command.Choice toCommandChoice() {
        Command.Choice choice = switch (kind) {
            case STRING -> new Command.Choice(name, (String) value);
            case LONG -> new Command.Choice(name, (long) value);
            case DOUBLE -> new Command.Choice(name, (double) value);
        };

        if (!nameLocalizations.isEmpty()) {
            choice.setNameLocalizations(nameLocalizations);
        }

        return choice;
    }

    private void validateStringChoice(OptionType optionType, String optionName, int index) {
        if (optionType != OptionType.STRING) {
            throw new IllegalStateException("Choice #" + index + " of option '" + optionName + "' is STRING, but option type is " + optionType);
        }

        String stringValue = (String) value;
        requireNonBlank(stringValue, "Choice #" + index + " of option '" + optionName + "' has blank string value");
    }

    private void validateLongChoice(OptionType optionType, String optionName, int index) {
        if (optionType != OptionType.INTEGER && optionType != OptionType.NUMBER) {
            throw new IllegalStateException(
                    "Choice #" + index + " of option '" + optionName + "' is LONG, but option type is " + optionType
            );
        }
    }

    private void validateDoubleChoice(OptionType optionType, String optionName, int index) {
        if (optionType != OptionType.NUMBER) {
            throw new IllegalStateException("Choice #" + index + " of option '" + optionName + "' is DOUBLE, but option type is " + optionType);
        }

        double doubleValue = (double) value;
        if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
            throw new IllegalStateException("Choice #" + index + " of option '" + optionName + "' has non-finite double value");
        }
    }

    private static void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(message);
        }
    }
}