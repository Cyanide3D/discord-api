package ru.cyanide3d.discord.jda.api.command;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.lang.NonNull;
import ru.cyanide3d.discord.jda.api.contexts.SlashPath;

import java.util.List;
import java.util.Optional;

public interface SlashCommandRegistry {

    void register(@NonNull SlashCommand command);

    List<CommandData> getCommandDatas();

    Optional<ResolvedSlashLeaf> findLeaf(@NonNull SlashPath path);

}
