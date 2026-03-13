package ru.cyanide3d.discord.jda.command;

import ru.cyanide3d.discord.jda.api.command.ExecutorSpec;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.command.SlashExecutorResolver;

import java.util.List;

import static ru.cyanide3d.utils.CastUtils.cast;

public class CompositeSlashExecutorResolver implements SlashExecutorResolver<ExecutorSpec> {

    private final List<SlashExecutorResolver<?>> resolvers;

    public CompositeSlashExecutorResolver(List<SlashExecutorResolver<?>> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public boolean supports(ExecutorSpec spec) {
        return resolvers.stream().anyMatch(resolver -> resolver.supports(spec));
    }

    @Override
    public SlashExecutor resolve(ExecutorSpec spec) {
        for (SlashExecutorResolver<?> resolver : resolvers) {
            if (resolver.supports(spec)) {
                return resolver.resolve(cast(spec));
            }
        }
        throw new IllegalStateException("No SlashExecutorResolver found for spec: " + spec.getClass().getName());
    }

}