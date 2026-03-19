package ru.cyanide3d.discord.jda.plugin.lavalink.command.player;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;
import ru.cyanide3d.discord.jda.api.restriction.Restrictions;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.GuildPlayerRegistry;
import ru.cyanide3d.discord.jda.plugin.lavalink.player.GuildPlayerState;

public final class PlayerCommandRestrictions {

    private PlayerCommandRestrictions() {
    }

    public static Restriction<SlashCommandContext> play() {
        return all(
                adapt(Restrictions.guildOnly()),
                adapt(Restrictions.memberOnly()),
                userInVoiceChannel(),
                botNotConnectedOrInSameVoiceChannel()
        );
    }

    public static Restriction<SlashCommandContext> control(GuildPlayerRegistry guildPlayerRegistry) {
        return all(
                adapt(Restrictions.guildOnly()),
                adapt(Restrictions.memberOnly()),
                userInVoiceChannel(),
                botInVoiceChannel(),
                userInSameVoiceChannelAsBot(),
                activePlayer(guildPlayerRegistry)
        );
    }

    public static Restriction<SlashCommandContext> userInVoiceChannel() {
        return Restrictions.predicate(
                ctx -> {
                    GuildVoiceState voiceState = ctx.requireMember().getVoiceState();
                    return voiceState != null
                            && voiceState.inAudioChannel()
                            && voiceState.getChannel() != null;
                },
                "Нужно быть в голосовом чате, чтобы использовать эту команду."
        );
    }

    public static Restriction<SlashCommandContext> botInVoiceChannel() {
        return Restrictions.predicate(
                ctx -> {
                    GuildVoiceState voiceState = ctx.requireGuild().getSelfMember().getVoiceState();
                    return voiceState != null
                            && voiceState.inAudioChannel()
                            && voiceState.getChannel() != null;
                },
                "Бот сейчас не подключён к голосовому каналу."
        );
    }

    public static Restriction<SlashCommandContext> botNotConnectedOrInSameVoiceChannel() {
        return Restrictions.predicate(
                ctx -> {
                    GuildVoiceState memberVoiceState = ctx.requireMember().getVoiceState();
                    GuildVoiceState botVoiceState = ctx.requireGuild().getSelfMember().getVoiceState();

                    if (memberVoiceState == null || memberVoiceState.getChannel() == null) {
                        return false;
                    }

                    if (botVoiceState == null || !botVoiceState.inAudioChannel() || botVoiceState.getChannel() == null) {
                        return true;
                    }

                    return botVoiceState.getChannel().getIdLong() == memberVoiceState.getChannel().getIdLong();
                },
                "Бот уже используется в другом голосовом канале."
        );
    }

    public static Restriction<SlashCommandContext> userInSameVoiceChannelAsBot() {
        return Restrictions.predicate(
                ctx -> {
                    GuildVoiceState memberVoiceState = ctx.requireMember().getVoiceState();
                    GuildVoiceState botVoiceState = ctx.requireGuild().getSelfMember().getVoiceState();

                    if (memberVoiceState == null || memberVoiceState.getChannel() == null) {
                        return false;
                    }

                    if (botVoiceState == null || botVoiceState.getChannel() == null) {
                        return false;
                    }

                    return memberVoiceState.getChannel().getIdLong() == botVoiceState.getChannel().getIdLong();
                },
                "Нужно находиться в том же голосовом канале, что и бот."
        );
    }

    public static Restriction<SlashCommandContext> activePlayer(GuildPlayerRegistry guildPlayerRegistry) {
        return Restrictions.predicate(
                ctx -> {
                    GuildPlayerState state = guildPlayerRegistry.get(ctx.requireGuild().getIdLong());
                    return state != null && state.hasCurrentTrack();
                },
                "Сейчас ничего не воспроизводится."
        );
    }

    @SafeVarargs
    public static Restriction<SlashCommandContext> all(Restriction<? super SlashCommandContext>... restrictions) {
        Restriction<SlashCommandContext> result = Restriction.allow();
        for (Restriction<? super SlashCommandContext> restriction : restrictions) {
            if (restriction != null) {
                result = result.and(restriction);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static Restriction<SlashCommandContext> adapt(Restriction<?> restriction) {
        return (Restriction<SlashCommandContext>) restriction;
    }
}