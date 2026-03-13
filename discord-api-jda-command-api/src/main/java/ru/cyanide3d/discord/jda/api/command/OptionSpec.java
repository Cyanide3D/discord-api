package ru.cyanide3d.discord.jda.api.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionSpec {

    private final OptionType type;

    private final String name;

    private final String description;

    private final boolean required;

    private final boolean autoComplete;

    private final List<ChoiceSpec> choices;

    public static OptionSpec of(OptionType type, String name, String description) {
        return new OptionSpec(type, name, description, false, false, new ArrayList<>());
    }

    public static OptionSpec string(String name, String description) {
        return of(OptionType.STRING, name, description);
    }

    public static OptionSpec integer(String name, String description) {
        return of(OptionType.INTEGER, name, description);
    }

    public static OptionSpec bool(String name, String description) {
        return of(OptionType.BOOLEAN, name, description);
    }

    public static OptionSpec user(String name, String description) {
        return of(OptionType.USER, name, description);
    }

    public static OptionSpec channel(String name, String description) {
        return of(OptionType.CHANNEL, name, description);
    }

    public static OptionSpec role(String name, String description) {
        return of(OptionType.ROLE, name, description);
    }

    public static OptionSpec mentionable(String name, String description) {
        return of(OptionType.MENTIONABLE, name, description);
    }

    public static OptionSpec number(String name, String description) {
        return of(OptionType.NUMBER, name, description);
    }

    public static OptionSpec attachment(String name, String description) {
        return of(OptionType.ATTACHMENT, name, description);
    }

    public OptionSpec required() {
        return withRequired(true);
    }

    public OptionSpec optional() {
        return withRequired(false);
    }

    public OptionSpec autoComplete() {
        return withAutoComplete(true);
    }

    public OptionSpec choice(String name, String value) {
        List<ChoiceSpec> copy = new ArrayList<>(choices);
        copy.add(ChoiceSpec.of(name, value));
        return withChoices(copy);
    }

    public OptionSpec choice(String name, long value) {
        List<ChoiceSpec> copy = new ArrayList<>(choices);
        copy.add(ChoiceSpec.of(name, value));
        return withChoices(copy);
    }

    public OptionSpec choice(String name, double value) {
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

}