package ru.cyanide3d.discord.jda.command.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscordJDASlashCommandProperties {

    private SlashCommandSyncMode syncMode = SlashCommandSyncMode.DISABLED;

}