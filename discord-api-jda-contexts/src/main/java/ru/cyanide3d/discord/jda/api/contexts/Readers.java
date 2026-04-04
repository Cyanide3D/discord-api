package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class Readers {

    private Readers() {
    }

    @SuppressWarnings("unchecked")
    public static <C extends EventContext<?>> Class<C> raw(Class<?> type) {
        return (Class<C>) type;
    }

    public static <C extends EventContext<?>, T> ContextValueReader<C, T> of(
            Class<C> contextType,
            Function<C, T> reader
    ) {
        return SimpleContextValueReader.of(contextType, reader);
    }

    public static <C extends EventContext<?>, T> ContextValueReader<C, T> optional(
            Class<C> contextType,
            Function<C, Optional<T>> reader
    ) {
        return SimpleContextValueReader.optional(contextType, reader);
    }

    public static <C extends EventContext<?>, S, T> ContextValueReader<C, T> map(
            ContextValueReader<C, S> source,
            Function<? super S, ? extends T> mapper
    ) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(mapper, "mapper");

        return optional(
                source.getContextType(),
                context -> source.read(context).map(mapper)
        );
    }

    public static <C extends EventContext<?>, S, T> ContextValueReader<C, T> flatMap(
            ContextValueReader<C, S> source,
            Function<? super S, Optional<T>> mapper
    ) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(mapper, "mapper");

        return optional(
                source.getContextType(),
                context -> source.read(context).flatMap(mapper)
        );
    }

    public static <C extends EventContext<?>> ContextValueReader<C, Boolean> supports(
            ContextValueReader<?, ?> reader,
            Class<C> contextType
    ) {
        Objects.requireNonNull(reader, "reader");
        Objects.requireNonNull(contextType, "contextType");

        return of(contextType, ctx -> reader.supports(ctx));
    }

    public static ContextValueReader<JDAEventContext<?>, JDA> jda() {
        return of(raw(JDAEventContext.class), JDAEventContext::getJDA);
    }

    public static ContextValueReader<UserEventContext<?>, User> user() {
        return of(raw(UserEventContext.class), UserEventContext::getUser);
    }

    public static ContextValueReader<UserEventContext<?>, String> userId() {
        return map(user(), User::getId);
    }

    public static ContextValueReader<UserEventContext<?>, Long> userIdLong() {
        return map(user(), User::getIdLong);
    }

    public static ContextValueReader<UserEventContext<?>, String> userName() {
        return map(user(), User::getName);
    }

    public static ContextValueReader<GuildEventContext<?>, Guild> guild() {
        return optional(raw(GuildEventContext.class), ctx -> Optional.ofNullable(ctx.getGuildOrNull()));
    }

    public static ContextValueReader<GuildEventContext<?>, Guild> requiredGuild() {
        return of(raw(GuildEventContext.class), GuildEventContext::requireGuild);
    }

    public static ContextValueReader<GuildEventContext<?>, String> guildId() {
        return flatMap(guild(), guild -> Optional.of(guild.getId()));
    }

    public static ContextValueReader<GuildEventContext<?>, Long> guildIdLong() {
        return flatMap(guild(), guild -> Optional.of(guild.getIdLong()));
    }

    public static ContextValueReader<GuildEventContext<?>, String> guildName() {
        return flatMap(guild(), guild -> Optional.of(guild.getName()));
    }

    public static ContextValueReader<MemberEventContext<?>, Member> member() {
        return optional(raw(MemberEventContext.class), ctx -> Optional.ofNullable(ctx.getMemberOrNull()));
    }

    public static ContextValueReader<MemberEventContext<?>, Member> requiredMember() {
        return of(raw(MemberEventContext.class), MemberEventContext::requireMember);
    }

    public static ContextValueReader<MemberEventContext<?>, String> memberId() {
        return flatMap(member(), member -> Optional.of(member.getId()));
    }

    public static ContextValueReader<MemberEventContext<?>, Long> memberIdLong() {
        return flatMap(member(), member -> Optional.of(member.getIdLong()));
    }

    public static ContextValueReader<MemberEventContext<?>, String> memberEffectiveName() {
        return flatMap(member(), member -> Optional.of(member.getEffectiveName()));
    }

    public static ContextValueReader<ChannelEventContext<?>, Channel> channel() {
        return of(raw(ChannelEventContext.class), ChannelEventContext::getChannel);
    }

    public static ContextValueReader<ChannelEventContext<?>, String> channelId() {
        return map(channel(), Channel::getId);
    }

    public static ContextValueReader<ChannelEventContext<?>, Long> channelIdLong() {
        return map(channel(), Channel::getIdLong);
    }

    public static ContextValueReader<ChannelEventContext<?>, String> channelName() {
        return flatMap(channel(), channel -> Optional.ofNullable(channel.getName()));
    }

    public static ContextValueReader<MessageEventContext<?>, Message> message() {
        return of(raw(MessageEventContext.class), MessageEventContext::getMessage);
    }

    public static ContextValueReader<MessageEventContext<?>, String> messageId() {
        return map(message(), Message::getId);
    }

    public static ContextValueReader<MessageEventContext<?>, Long> messageIdLong() {
        return map(message(), Message::getIdLong);
    }

    public static ContextValueReader<MessageEventContext<?>, String> messageContentRaw() {
        return map(message(), Message::getContentRaw);
    }

    public static ContextValueReader<MessageEventContext<?>, String> messageContentDisplay() {
        return map(message(), Message::getContentDisplay);
    }

    public static ContextValueReader<MessageReceivedContext, Boolean> isGuildMessage() {
        return of(MessageReceivedContext.class, MessageReceivedContext::isGuildMessage);
    }

    public static ContextValueReader<MessageReceivedContext, Boolean> isDirectMessage() {
        return of(MessageReceivedContext.class, MessageReceivedContext::isDirectMessage);
    }

    public static ContextValueReader<MessageReactionContext<?>, MessageChannel> reactionChannel() {
        return of(raw(MessageReactionContext.class), MessageReactionContext::getChannel);
    }

    public static ContextValueReader<MessageReactionContext<?>, EmojiUnion> reactionEmoji() {
        return of(raw(MessageReactionContext.class), MessageReactionContext::getEmoji);
    }

    public static ContextValueReader<MessageReactionContext<?>, Long> reactionMessageIdLong() {
        return of(raw(MessageReactionContext.class), MessageReactionContext::getMessageIdLong);
    }

    public static ContextValueReader<GuildVoiceUpdateContext, AudioChannelUnion> voiceChannelJoined() {
        return optional(GuildVoiceUpdateContext.class, ctx -> Optional.ofNullable(ctx.getChannelJoinedOrNull()));
    }

    public static ContextValueReader<GuildVoiceUpdateContext, AudioChannelUnion> voiceChannelLeft() {
        return optional(GuildVoiceUpdateContext.class, ctx -> Optional.ofNullable(ctx.getChannelLeftOrNull()));
    }

    public static ContextValueReader<GuildVoiceUpdateContext, Boolean> voiceIsJoin() {
        return of(GuildVoiceUpdateContext.class, GuildVoiceUpdateContext::isJoin);
    }

    public static ContextValueReader<GuildVoiceUpdateContext, Boolean> voiceIsLeave() {
        return of(GuildVoiceUpdateContext.class, GuildVoiceUpdateContext::isLeave);
    }

    public static ContextValueReader<GuildVoiceUpdateContext, Boolean> voiceIsMove() {
        return of(GuildVoiceUpdateContext.class, GuildVoiceUpdateContext::isMove);
    }

    public static <T> ContextValueReader<SlashCommandContext, T> option(SlashOptionReader<T> option) {
        Objects.requireNonNull(option, "option");
        return option;
    }

    public static <T> ContextValueReader<SlashCommandContext, T> requiredOption(SlashOptionReader<T> option) {
        Objects.requireNonNull(option, "option");
        return of(SlashCommandContext.class, ctx -> ctx.requireOption(option));
    }

    public static ContextValueReader<SlashCommandContext, Boolean> hasOption(SlashOptionReader<?> option) {
        Objects.requireNonNull(option, "option");
        return of(SlashCommandContext.class, ctx -> ctx.hasOption(option));
    }

    public static ContextValueReader<SlashCommandContext, SlashPath> slashPath() {
        return of(SlashCommandContext.class, SlashCommandContext::getPath);
    }

    public static ContextValueReader<SlashCommandContext, SlashCommandOptions> slashOptions() {
        return of(SlashCommandContext.class, SlashCommandContext::getOptions);
    }

    public static ContextValueReader<ButtonInteractionContext, String> buttonComponentId() {
        return of(ButtonInteractionContext.class, ButtonInteractionContext::getComponentId);
    }

    public static ContextValueReader<StringSelectInteractionContext, String> stringSelectComponentId() {
        return of(StringSelectInteractionContext.class, StringSelectInteractionContext::getComponentId);
    }

    public static ContextValueReader<StringSelectInteractionContext, List<String>> stringSelectValues() {
        return of(StringSelectInteractionContext.class, StringSelectInteractionContext::getValues);
    }

    public static ContextValueReader<ModalInteractionContext, String> modalId() {
        return of(ModalInteractionContext.class, ModalInteractionContext::getModalId);
    }
}