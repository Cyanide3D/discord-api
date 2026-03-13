package ru.cyanide3d.discord.jda.command;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.command.ExecutorSpec;
import ru.cyanide3d.discord.jda.api.command.SlashExecutor;
import ru.cyanide3d.discord.jda.api.command.SlashExecutorResolver;

public class SpringSlashExecutorResolver<T extends BeanTypeExecutorSpec> implements SlashExecutorResolver<T> {

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public SlashExecutor resolve(T spec) {
        return beanFactory.getBean(spec.getType());
    }

    @Override
    public boolean supports(ExecutorSpec spec) {
        return spec instanceof BeanTypeExecutorSpec;
    }

}