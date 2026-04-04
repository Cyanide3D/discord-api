package ru.cyanide3d.discord.jda.api.contexts;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ContextValueReader<C extends EventContext<?>, T> {

    Class<C> getContextType();

    Optional<T> read(C context);

    default String getDisplayName() {
        String simpleName = getClass().getSimpleName();
        if (simpleName == null || simpleName.isBlank()) {
            return getClass().getName();
        }
        return simpleName;
    }

    default boolean supports(EventContext<?> context) {
        Objects.requireNonNull(context, "context");
        return getContextType().isInstance(context);
    }

    default Optional<T> readFrom(EventContext<?> context) {
        Objects.requireNonNull(context, "context");

        if (!supports(context)) {
            return Optional.empty();
        }

        return read(getContextType().cast(context));
    }

    default T require(C context) {
        Objects.requireNonNull(context, "context");
        return read(context).orElseThrow(() ->
                new IllegalStateException(
                        "Reader '" + getDisplayName()
                                + "' cannot extract value from context "
                                + context.getClass().getSimpleName()
                )
        );
    }

    default T require(C context, Supplier<? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(exceptionSupplier, "exceptionSupplier");
        return read(context).orElseThrow(exceptionSupplier);
    }

    default T requireFrom(EventContext<?> context) {
        Objects.requireNonNull(context, "context");
        return readFrom(context).orElseThrow(() ->
                new IllegalStateException(
                        "Reader '" + getDisplayName()
                                + "' cannot extract value from context "
                                + context.getClass().getSimpleName()
                )
        );
    }

    default T requireFrom(EventContext<?> context, Supplier<? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(exceptionSupplier, "exceptionSupplier");
        return readFrom(context).orElseThrow(exceptionSupplier);
    }

    default ContextValueReader<C, T> named(String name) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }

        ContextValueReader<C, T> self = this;

        return new ContextValueReader<>() {
            @Override
            public Class<C> getContextType() {
                return self.getContextType();
            }

            @Override
            public Optional<T> read(C context) {
                return self.read(context);
            }

            @Override
            public String getDisplayName() {
                return name;
            }
        };
    }

    default <R> ContextValueReader<C, R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper");

        ContextValueReader<C, T> self = this;

        return new ContextValueReader<>() {
            @Override
            public Class<C> getContextType() {
                return self.getContextType();
            }

            @Override
            public Optional<R> read(C context) {
                return self.read(context).map(mapper);
            }

            @Override
            public String getDisplayName() {
                return self.getDisplayName() + ".map(...)";
            }
        };
    }

    default <R> ContextValueReader<C, R> andThen(Function<? super T, ? extends R> mapper) {
        return map(mapper);
    }

    default <R> ContextValueReader<C, R> flatMap(Function<? super T, Optional<R>> mapper) {
        Objects.requireNonNull(mapper, "mapper");

        ContextValueReader<C, T> self = this;

        return new ContextValueReader<>() {
            @Override
            public Class<C> getContextType() {
                return self.getContextType();
            }

            @Override
            public Optional<R> read(C context) {
                return self.read(context).flatMap(mapper);
            }

            @Override
            public String getDisplayName() {
                return self.getDisplayName() + ".flatMap(...)";
            }
        };
    }

    default <R> ContextValueReader<C, R> mapOptional(Function<? super T, Optional<R>> mapper) {
        return flatMap(mapper);
    }

    default ContextValueReader<C, T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        ContextValueReader<C, T> self = this;

        return new ContextValueReader<>() {
            @Override
            public Class<C> getContextType() {
                return self.getContextType();
            }

            @Override
            public Optional<T> read(C context) {
                return self.read(context).filter(predicate);
            }

            @Override
            public String getDisplayName() {
                return self.getDisplayName() + ".filter(...)";
            }
        };
    }

    default ContextValueReader<C, T> peek(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "consumer");

        ContextValueReader<C, T> self = this;

        return new ContextValueReader<>() {
            @Override
            public Class<C> getContextType() {
                return self.getContextType();
            }

            @Override
            public Optional<T> read(C context) {
                Optional<T> value = self.read(context);
                value.ifPresent(consumer);
                return value;
            }

            @Override
            public String getDisplayName() {
                return self.getDisplayName() + ".peek(...)";
            }
        };
    }

    default <R> ContextValueReader<C, R> cast(Class<R> targetType) {
        Objects.requireNonNull(targetType, "targetType");

        ContextValueReader<C, T> self = this;

        return new ContextValueReader<>() {
            @Override
            public Class<C> getContextType() {
                return self.getContextType();
            }

            @Override
            public Optional<R> read(C context) {
                return self.read(context)
                        .filter(targetType::isInstance)
                        .map(targetType::cast);
            }

            @Override
            public String getDisplayName() {
                return self.getDisplayName() + ".cast(" + targetType.getSimpleName() + ")";
            }
        };
    }

    default ContextValueReader<C, T> or(ContextValueReader<C, ? extends T> fallback) {
        Objects.requireNonNull(fallback, "fallback");

        ContextValueReader<C, T> self = this;

        return new ContextValueReader<>() {
            @Override
            public Class<C> getContextType() {
                return self.getContextType();
            }

            @Override
            public Optional<T> read(C context) {
                Optional<T> primary = self.read(context);
                if (primary.isPresent()) {
                    return primary;
                }

                return fallback.read(context).map(value -> (T) value);
            }

            @Override
            public String getDisplayName() {
                return self.getDisplayName() + ".or(" + fallback.getDisplayName() + ")";
            }
        };
    }

    default ContextValueReader<C, T> orElse(T fallbackValue) {
        ContextValueReader<C, T> self = this;

        return new ContextValueReader<>() {
            @Override
            public Class<C> getContextType() {
                return self.getContextType();
            }

            @Override
            public Optional<T> read(C context) {
                Optional<T> primary = self.read(context);
                if (primary.isPresent()) {
                    return primary;
                }

                return Optional.ofNullable(fallbackValue);
            }

            @Override
            public String getDisplayName() {
                return self.getDisplayName() + ".orElse(...)";
            }
        };
    }

    default ContextValueReader<C, T> orElseGet(Supplier<? extends T> fallbackSupplier) {
        Objects.requireNonNull(fallbackSupplier, "fallbackSupplier");

        ContextValueReader<C, T> self = this;

        return new ContextValueReader<>() {
            @Override
            public Class<C> getContextType() {
                return self.getContextType();
            }

            @Override
            public Optional<T> read(C context) {
                Optional<T> primary = self.read(context);
                if (primary.isPresent()) {
                    return primary;
                }

                return Optional.ofNullable(fallbackSupplier.get());
            }

            @Override
            public String getDisplayName() {
                return self.getDisplayName() + ".orElseGet(...)";
            }
        };
    }
}