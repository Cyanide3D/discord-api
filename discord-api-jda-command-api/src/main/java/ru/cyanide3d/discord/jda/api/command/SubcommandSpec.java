package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class SubcommandSpec {

     private final String name;

     private final String description;

     private final List<OptionSpec<?>> options;

     private final Restriction<SlashCommandContext> restriction;

     private final ExecutorSpec executorSpec;

     public SubcommandSpec(String name, String description, List<OptionSpec<?>> options, Restriction<SlashCommandContext> restriction, ExecutorSpec executorSpec) {
         this.name = name;
         this.description = description;
         this.options = new ArrayList<>(options);
         this.restriction = restriction;
         this.executorSpec = executorSpec;
     }

     public List<OptionSpec<?>> getOptions() {
         return Collections.unmodifiableList(options);
     }

     public SubcommandData toSubcommandData() {
         SubcommandData data = new SubcommandData(name, description);
         for (OptionSpec<?> option : options) {
             data.addOptions(option.toOptionData());
         }
         return data;
     }

}
