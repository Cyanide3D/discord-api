package ru.cyanide3d.discord.jda.api.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import ru.cyanide3d.discord.jda.api.contexts.SlashOptionReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Getter
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionSpec<T> implements SlashOptionReader<T> {

    private final OptionType type;

    private final String name;

    private final String description;

    private final boolean required;

    private final boolean autoComplete;

    private final List<ChoiceSpec> choices;

    private final Function<OptionMapping, T> reader;

    public static <T> OptionSpec<T> of(OptionType type, String name, String description, Function<OptionMapping, T> reader) {
        return new OptionSpec<>(type, name, description, false, false, new ArrayList<>(), reader);
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
        return withRequired(true);
    }

    public OptionSpec<T> optional() {
        return withRequired(false);
    }

    public OptionSpec<T> autoComplete() {
        return withAutoComplete(true);
    }

    public OptionSpec<T> choice(String name, String value) {
        List<ChoiceSpec> copy = new ArrayList<>(choices);
        copy.add(ChoiceSpec.of(name, value));
        return withChoices(copy);
    }

    public OptionSpec<T> choice(String name, long value) {
        List<ChoiceSpec> copy = new ArrayList<>(choices);
        copy.add(ChoiceSpec.of(name, value));
        return withChoices(copy);
    }

    public OptionSpec<T> choice(String name, double value) {
        List<ChoiceSpec> copy = new ArrayList<>(choices);
        copy.add(ChoiceSpec.of(name, value));
        return withChoices(copy);
    }

    public List<ChoiceSpec> getChoices() {
        return Collections.unmodifiableList(choices);
    }

    public OptionData toOptionData() {
        OptionData data = new OptionData(type, name, description, required, autoComplete);
        for (ChoiceSpec choice : choices) {
            choice.apply(data);
        }
        return data;
    }

    @Override
    public T read(OptionMapping mapping) {
        return reader.apply(mapping);
    }
}