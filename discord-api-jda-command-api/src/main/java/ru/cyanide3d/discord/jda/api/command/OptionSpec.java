package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import ru.cyanide3d.discord.jda.api.contexts.SlashOptionReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Getter
public final class OptionSpec<T> implements SlashOptionReader<T> {

    private static final int MAX_CHOICES = 25;

    private static final Set<OptionType> CHOICE_CAPABLE_TYPES = Set.of(
            OptionType.STRING,
            OptionType.INTEGER,
            OptionType.NUMBER
    );

    private static final Set<OptionType> AUTOCOMPLETE_CAPABLE_TYPES = Set.of(
            OptionType.STRING,
            OptionType.INTEGER,
            OptionType.NUMBER
    );

    private final OptionType type;

    private final String name;

    private final String description;

    private final boolean required;

    private final boolean autoComplete;

    private final List<ChoiceSpec> choices;

    private final Function<OptionMapping, T> reader;

    private OptionSpec(OptionType type, String name, String description, boolean required, boolean autoComplete, List<ChoiceSpec> choices, Function<OptionMapping, T> reader) {
        this.type = Objects.requireNonNull(type, "type");
        this.name = Objects.requireNonNull(name, "name");
        this.description = Objects.requireNonNull(description, "description");
        this.required = required;
        this.autoComplete = autoComplete;
        this.choices = List.copyOf(Objects.requireNonNull(choices, "choices"));
        this.reader = Objects.requireNonNull(reader, "reader");
    }

    public static <T> OptionSpec<T> of(OptionType type, String name, String description, Function<OptionMapping, T> reader) {
        return new OptionSpec<>(type, name, description,
                false,
                false,
                List.of(),
                reader
        );
    }

    public static OptionSpec<String> string(String name, String description) {
        return of(OptionType.STRING, name, description, OptionMapping::getAsString);
    }

    public static OptionSpec<Long> integer(String name, String description) {
        return of(OptionType.INTEGER, name, description, OptionMapping::getAsLong);
    }

    public static OptionSpec<Boolean> bool(String name, String description) {
        return of(OptionType.BOOLEAN, name, description, OptionMapping::getAsBoolean);
    }

    public static OptionSpec<Double> number(String name, String description) {
        return of(OptionType.NUMBER, name, description, OptionMapping::getAsDouble);
    }

    public static OptionSpec<String> userId(String name, String description) {
        return of(OptionType.USER, name, description, m -> m.getAsUser().getId());
    }

    public OptionSpec<T> required() {
        return new OptionSpec<>(type, name, description, true, autoComplete, choices, reader);
    }

    public OptionSpec<T> optional() {
        return new OptionSpec<>(type, name, description, false, autoComplete, choices, reader);
    }

    public OptionSpec<T> autoComplete() {
        return new OptionSpec<>(type, name, description, required, true, choices, reader);
    }

    public OptionSpec<T> choice(String choiceName, String value) {
        List<ChoiceSpec> copy = new ArrayList<>(choices);
        copy.add(ChoiceSpec.of(choiceName, value));

        return new OptionSpec<>(type, name, description, required, autoComplete, copy, reader);
    }

    public OptionSpec<T> choice(String choiceName, long value) {
        List<ChoiceSpec> copy = new ArrayList<>(choices);
        copy.add(ChoiceSpec.of(choiceName, value));

        return new OptionSpec<>(type, name, description, required, autoComplete, copy, reader);
    }

    public OptionSpec<T> choice(String choiceName, double value) {
        List<ChoiceSpec> copy = new ArrayList<>(choices);
        copy.add(ChoiceSpec.of(choiceName, value));

        return new OptionSpec<>(type, name, description, required, autoComplete, copy, reader);
    }

    @Override
    public T read(OptionMapping mapping) {
        return reader.apply(mapping);
    }

    public OptionData toOptionData() {
        validateSpec();

        OptionData data = new OptionData(type, name, description, required, autoComplete);

        for (int i = 0; i < choices.size(); i++) {
            ChoiceSpec choice = choices.get(i);
            choice.validateFor(type, name, i);
            choice.apply(data);
        }

        return data;
    }

    private void validateSpec() {
        requireNonBlank(name, "name");
        requireNonBlank(description, "description");

        if (autoComplete && !AUTOCOMPLETE_CAPABLE_TYPES.contains(type)) {
            throw new IllegalStateException(
                    "Auto-complete is not supported for option type " + type + " (" + name + ")"
            );
        }

        if (!choices.isEmpty() && !CHOICE_CAPABLE_TYPES.contains(type)) {
            throw new IllegalStateException(
                    "Predefined choices are not supported for option type " + type + " (" + name + ")"
            );
        }

        if (choices.size() > MAX_CHOICES) {
            throw new IllegalStateException(
                    "Option '" + name + "' has " + choices.size() + " choices, but maximum is " + MAX_CHOICES
            );
        }

        if (autoComplete && !choices.isEmpty()) {
            throw new IllegalStateException(
                    "Option '" + name + "' cannot use both predefined choices and auto-complete"
            );
        }
    }

    private static void requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("OptionSpec." + field + " must not be blank");
        }
    }
}