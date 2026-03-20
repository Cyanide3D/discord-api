package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import ru.cyanide3d.discord.jda.api.contexts.SlashOptionReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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

    private final Set<ChannelType> channelTypes;

    private final Number minValue;

    private final Number maxValue;

    private final Integer minLength;

    private final Integer maxLength;

    private final Map<DiscordLocale, String> nameLocalizations;

    private final Map<DiscordLocale, String> descriptionLocalizations;

    private final Function<OptionMapping, T> reader;

    private OptionSpec(OptionType type, String name, String description, boolean required, boolean autoComplete, List<ChoiceSpec> choices, Set<ChannelType> channelTypes, Number minValue, Number maxValue, Integer minLength, Integer maxLength, Map<DiscordLocale, String> nameLocalizations, Map<DiscordLocale, String> descriptionLocalizations, Function<OptionMapping, T> reader) {
        this.type = Objects.requireNonNull(type, "type");
        this.name = Objects.requireNonNull(name, "name");
        this.description = Objects.requireNonNull(description, "description");
        this.required = required;
        this.autoComplete = autoComplete;
        this.choices = List.copyOf(Objects.requireNonNull(choices, "choices"));
        this.channelTypes = Set.copyOf(Objects.requireNonNull(channelTypes, "channelTypes"));
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.nameLocalizations = Map.copyOf(Objects.requireNonNull(nameLocalizations, "nameLocalizations"));
        this.descriptionLocalizations = Map.copyOf(Objects.requireNonNull(descriptionLocalizations, "descriptionLocalizations"));
        this.reader = Objects.requireNonNull(reader, "reader");
    }

    public static <T> OptionSpec<T> of(OptionType type, String name, String description, Function<OptionMapping, T> reader) {
        return new OptionSpec<>(type, name, description, false, false, List.of(), Set.of(), null, null, null, null, Map.of(), Map.of(), reader);
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

    public static OptionSpec<User> user(String name, String description) {
        return of(OptionType.USER, name, description, OptionMapping::getAsUser);
    }

    public static OptionSpec<Member> member(String name, String description) {
        return of(OptionType.USER, name, description, OptionMapping::getAsMember);
    }

    public static OptionSpec<Role> role(String name, String description) {
        return of(OptionType.ROLE, name, description, OptionMapping::getAsRole);
    }

    public static OptionSpec<IMentionable> mentionable(String name, String description) {
        return of(OptionType.MENTIONABLE, name, description, OptionMapping::getAsMentionable);
    }

    public static OptionSpec<GuildChannelUnion> channel(String name, String description) {
        return of(OptionType.CHANNEL, name, description, OptionMapping::getAsChannel);
    }

    public static OptionSpec<Message.Attachment> attachment(String name, String description) {
        return of(OptionType.ATTACHMENT, name, description, OptionMapping::getAsAttachment);
    }

    public static OptionSpec<String> userId(String name, String description) {
        return user(name, description).map(User::getId);
    }

    public static OptionSpec<String> roleId(String name, String description) {
        return role(name, description).map(Role::getId);
    }

    public static OptionSpec<String> mentionableId(String name, String description) {
        return mentionable(name, description).map(IMentionable::getId);
    }

    public static OptionSpec<String> channelId(String name, String description) {
        return channel(name, description).map(GuildChannelUnion::getId);
    }

    public static OptionSpec<String> attachmentUrl(String name, String description) {
        return attachment(name, description).map(Message.Attachment::getUrl);
    }

    public <R> OptionSpec<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper");

        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                mapping -> mapper.apply(reader.apply(mapping))
        );
    }

    public OptionSpec<T> required() {
        return required(true);
    }

    public OptionSpec<T> optional() {
        return required(false);
    }

    public OptionSpec<T> required(boolean required) {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> autoComplete() {
        return autoComplete(true);
    }

    public OptionSpec<T> withoutAutoComplete() {
        return autoComplete(false);
    }

    public OptionSpec<T> autoComplete(boolean autoComplete) {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> choice(ChoiceSpec choice) {
        ArrayList<ChoiceSpec> copy = new ArrayList<>(choices);
        copy.add(Objects.requireNonNull(choice, "choice"));

        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                copy,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> choices(ChoiceSpec... choices) {
        Objects.requireNonNull(choices, "choices");

        OptionSpec<T> spec = this;
        for (ChoiceSpec choice : choices) {
            spec = spec.choice(choice);
        }
        return spec;
    }

    public OptionSpec<T> choices(Collection<? extends ChoiceSpec> choices) {
        Objects.requireNonNull(choices, "choices");

        OptionSpec<T> spec = this;
        for (ChoiceSpec choice : choices) {
            spec = spec.choice(choice);
        }
        return spec;
    }

    public OptionSpec<T> clearChoices() {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                List.of(),
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> choice(String choiceName, String value) {
        return choice(ChoiceSpec.of(choiceName, value));
    }

    public OptionSpec<T> choice(String choiceName, long value) {
        return choice(ChoiceSpec.of(choiceName, value));
    }

    public OptionSpec<T> choice(String choiceName, double value) {
        return choice(ChoiceSpec.of(choiceName, value));
    }

    public OptionSpec<T> channelTypes(ChannelType... channelTypes) {
        Objects.requireNonNull(channelTypes, "channelTypes");

        LinkedHashSet<ChannelType> copy = new LinkedHashSet<>();
        for (ChannelType channelType : channelTypes) {
            copy.add(Objects.requireNonNull(channelType, "channelType"));
        }

        return channelTypes(copy);
    }

    public OptionSpec<T> channelTypes(Collection<ChannelType> channelTypes) {
        LinkedHashSet<ChannelType> copy = new LinkedHashSet<>(Objects.requireNonNull(channelTypes, "channelTypes"));

        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                copy,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> clearChannelTypes() {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                Set.of(),
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> minValue(long minValue) {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> minValue(double minValue) {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> maxValue(long maxValue) {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> maxValue(double maxValue) {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> range(long minValue, long maxValue) {
        return minValue(minValue).maxValue(maxValue);
    }

    public OptionSpec<T> range(double minValue, double maxValue) {
        return minValue(minValue).maxValue(maxValue);
    }

    public OptionSpec<T> clearValueRange() {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                null,
                null,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> minLength(int minLength) {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> maxLength(int maxLength) {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> length(int exactLength) {
        return lengthRange(exactLength, exactLength);
    }

    public OptionSpec<T> lengthRange(int minLength, int maxLength) {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> clearLengthRange() {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                null,
                null,
                nameLocalizations,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> nameLocalization(DiscordLocale locale, String localizedName) {
        LinkedHashMap<DiscordLocale, String> copy = new LinkedHashMap<>(nameLocalizations);
        copy.put(Objects.requireNonNull(locale, "locale"), Objects.requireNonNull(localizedName, "localizedName"));

        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                copy,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> nameLocalizations(Map<DiscordLocale, String> localizations) {
        LinkedHashMap<DiscordLocale, String> copy = new LinkedHashMap<>(nameLocalizations);
        copy.putAll(Objects.requireNonNull(localizations, "localizations"));

        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                copy,
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> clearNameLocalizations() {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                Map.of(),
                descriptionLocalizations,
                reader
        );
    }

    public OptionSpec<T> descriptionLocalization(DiscordLocale locale, String localizedDescription) {
        LinkedHashMap<DiscordLocale, String> copy = new LinkedHashMap<>(descriptionLocalizations);
        copy.put(Objects.requireNonNull(locale, "locale"), Objects.requireNonNull(localizedDescription, "localizedDescription"));

        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                copy,
                reader
        );
    }

    public OptionSpec<T> descriptionLocalizations(Map<DiscordLocale, String> localizations) {
        LinkedHashMap<DiscordLocale, String> copy = new LinkedHashMap<>(descriptionLocalizations);
        copy.putAll(Objects.requireNonNull(localizations, "localizations"));

        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                copy,
                reader
        );
    }

    public OptionSpec<T> clearDescriptionLocalizations() {
        return new OptionSpec<>(
                type,
                name,
                description,
                required,
                autoComplete,
                choices,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                nameLocalizations,
                Map.of(),
                reader
        );
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public T read(OptionMapping mapping) {
        return reader.apply(mapping);
    }

    public OptionData toOptionData() {
        validateSpec();

        OptionData data = new OptionData(type, name, description, required, autoComplete);

        if (!nameLocalizations.isEmpty()) {
            data.setNameLocalizations(nameLocalizations);
        }

        if (!descriptionLocalizations.isEmpty()) {
            data.setDescriptionLocalizations(descriptionLocalizations);
        }

        if (!channelTypes.isEmpty()) {
            data.setChannelTypes(channelTypes);
        }

        if (minLength != null && maxLength != null) {
            data.setRequiredLength(minLength, maxLength);
        } else {
            if (minLength != null) {
                data.setMinLength(minLength);
            }
            if (maxLength != null) {
                data.setMaxLength(maxLength);
            }
        }

        applyMinValue(data);
        applyMaxValue(data);

        for (int i = 0; i < choices.size(); i++) {
            ChoiceSpec choice = choices.get(i);
            choice.validateFor(type, name, i);
            choice.apply(data);
        }

        return data;
    }

    private void applyMinValue(OptionData data) {
        if (minValue == null) {
            return;
        }

        if (minValue instanceof Long longValue) {
            data.setMinValue(longValue);
            return;
        }

        data.setMinValue(minValue.doubleValue());
    }

    private void applyMaxValue(OptionData data) {
        if (maxValue == null) {
            return;
        }

        if (maxValue instanceof Long longValue) {
            data.setMaxValue(longValue);
            return;
        }

        data.setMaxValue(maxValue.doubleValue());
    }

    private void validateSpec() {
        requireNonBlank(name, "OptionSpec.name must not be blank");
        requireNonBlank(description, "OptionSpec.description must not be blank");
        validateLocalizations();

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

        if (!channelTypes.isEmpty() && type != OptionType.CHANNEL) {
            throw new IllegalStateException(
                    "Channel type restrictions are only supported for option type CHANNEL (" + name + ")"
            );
        }

        validateStringBounds();
        validateNumericBounds();
    }

    private void validateLocalizations() {
        validateLocalizationMap(nameLocalizations, "name", name);
        validateLocalizationMap(descriptionLocalizations, "description", name);
    }

    private void validateLocalizationMap(Map<DiscordLocale, String> localizations, String field, String optionName) {
        for (Map.Entry<DiscordLocale, String> entry : localizations.entrySet()) {
            DiscordLocale locale = entry.getKey();
            String value = entry.getValue();

            if (locale == null) {
                throw new IllegalStateException(
                        "Option '" + optionName + "' has null locale in " + field + " localizations"
                );
            }

            requireNonBlank(
                    value,
                    "Option '" + optionName + "' has blank localized " + field + " for locale " + locale
            );
        }
    }

    private void validateStringBounds() {
        if (minLength == null && maxLength == null) {
            return;
        }

        if (type != OptionType.STRING) {
            throw new IllegalStateException(
                    "String length limits are only supported for option type STRING (" + name + ")"
            );
        }

        if (minLength != null && minLength < 0) {
            throw new IllegalStateException("Option '" + name + "' has negative minLength");
        }

        if (maxLength != null && maxLength < 1) {
            throw new IllegalStateException("Option '" + name + "' has non-positive maxLength");
        }

        if (minLength != null && maxLength != null && minLength > maxLength) {
            throw new IllegalStateException(
                    "Option '" + name + "' has minLength greater than maxLength"
            );
        }
    }

    private void validateNumericBounds() {
        if (minValue == null && maxValue == null) {
            return;
        }

        if (type != OptionType.INTEGER && type != OptionType.NUMBER) {
            throw new IllegalStateException(
                    "Numeric bounds are only supported for option types INTEGER and NUMBER (" + name + ")"
            );
        }

        if (type == OptionType.INTEGER) {
            validateIntegerBound(minValue, "minValue");
            validateIntegerBound(maxValue, "maxValue");
        } else {
            validateNumberBound(minValue, "minValue");
            validateNumberBound(maxValue, "maxValue");
        }

        if (minValue != null && maxValue != null && minValue.doubleValue() > maxValue.doubleValue()) {
            throw new IllegalStateException(
                    "Option '" + name + "' has minValue greater than maxValue"
            );
        }
    }

    private void validateIntegerBound(Number value, String field) {
        if (value == null) {
            return;
        }

        if (!(value instanceof Long)) {
            throw new IllegalStateException(
                    "Option '" + name + "' has non-integer " + field + " for INTEGER option"
            );
        }
    }

    private void validateNumberBound(Number value, String field) {
        if (value == null) {
            return;
        }

        if (!(value instanceof Long) && !(value instanceof Double)) {
            throw new IllegalStateException(
                    "Option '" + name + "' has unsupported numeric type for " + field
            );
        }

        double numericValue = value.doubleValue();
        if (Double.isNaN(numericValue) || Double.isInfinite(numericValue)) {
            throw new IllegalStateException(
                    "Option '" + name + "' has non-finite " + field
            );
        }
    }

    private static void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(message);
        }
    }
}