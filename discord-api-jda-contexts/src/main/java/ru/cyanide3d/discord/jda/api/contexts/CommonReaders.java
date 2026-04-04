package ru.cyanide3d.discord.jda.api.contexts;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;

import java.util.List;
import java.util.Optional;

public final class CommonReaders {

    private CommonReaders() {
    }

    @SuppressWarnings("unchecked")
    private static <C extends EventContext<?>> Class<C> raw(Class<?> type) {
        return (Class<C>) type;
    }

    public static final ContextValueReader<JDAEventContext<?>, JDA> JDA =
            SimpleContextValueReader.of(
                    raw(JDAEventContext.class),
                    ctx -> ctx.getJDA()
            );

    public static final ContextValueReader<UserEventContext<?>, User> USER =
            SimpleContextValueReader.of(
                    raw(UserEventContext.class),
                    ctx -> ctx.getUser()
            );

    public static final ContextValueReader<UserEventContext<?>, String> USER_ID =
            SimpleContextValueReader.of(
                    raw(UserEventContext.class),
                    ctx -> ctx.getUser().getId()
            );

    public static final ContextValueReader<UserEventContext<?>, Long> USER_ID_LONG =
            SimpleContextValueReader.of(
                    raw(UserEventContext.class),
                    ctx -> ctx.getUser().getIdLong()
            );

    public static final ContextValueReader<UserEventContext<?>, String> USER_NAME =
            SimpleContextValueReader.of(
                    raw(UserEventContext.class),
                    ctx -> ctx.getUser().getName()
            );

    public static final ContextValueReader<GuildEventContext<?>, Guild> GUILD =
            SimpleContextValueReader.optional(
                    raw(GuildEventContext.class),
                    ctx -> Optional.ofNullable(ctx.getGuildOrNull())
            );

    public static final ContextValueReader<GuildEventContext<?>, String> GUILD_ID =
            SimpleContextValueReader.optional(
                    raw(GuildEventContext.class),
                    ctx -> Optional.ofNullable(ctx.getGuildOrNull()).map(Guild::getId)
            );

    public static final ContextValueReader<GuildEventContext<?>, Long> GUILD_ID_LONG =
            SimpleContextValueReader.optional(
                    raw(GuildEventContext.class),
                    ctx -> Optional.ofNullable(ctx.getGuildOrNull()).map(Guild::getIdLong)
            );

    public static final ContextValueReader<GuildEventContext<?>, String> GUILD_NAME =
            SimpleContextValueReader.optional(
                    raw(GuildEventContext.class),
                    ctx -> Optional.ofNullable(ctx.getGuildOrNull()).map(Guild::getName)
            );

    public static final ContextValueReader<MemberEventContext<?>, Member> MEMBER =
            SimpleContextValueReader.optional(
                    raw(MemberEventContext.class),
                    ctx -> Optional.ofNullable(ctx.getMemberOrNull())
            );

    public static final ContextValueReader<MemberEventContext<?>, String> MEMBER_ID =
            SimpleContextValueReader.optional(
                    raw(MemberEventContext.class),
                    ctx -> Optional.ofNullable(ctx.getMemberOrNull()).map(Member::getId)
            );

    public static final ContextValueReader<MemberEventContext<?>, Long> MEMBER_ID_LONG =
            SimpleContextValueReader.optional(
                    raw(MemberEventContext.class),
                    ctx -> Optional.ofNullable(ctx.getMemberOrNull()).map(Member::getIdLong)
            );

    public static final ContextValueReader<MemberEventContext<?>, String> MEMBER_EFFECTIVE_NAME =
            SimpleContextValueReader.optional(
                    raw(MemberEventContext.class),
                    ctx -> Optional.ofNullable(ctx.getMemberOrNull()).map(Member::getEffectiveName)
            );

    public static final ContextValueReader<ChannelEventContext<?>, Channel> CHANNEL =
            SimpleContextValueReader.of(
                    raw(ChannelEventContext.class),
                    ctx -> ctx.getChannel()
            );

    public static final ContextValueReader<ChannelEventContext<?>, String> CHANNEL_ID =
            SimpleContextValueReader.of(
                    raw(ChannelEventContext.class),
                    ctx -> ctx.getChannel().getId()
            );

    public static final ContextValueReader<ChannelEventContext<?>, Long> CHANNEL_ID_LONG =
            SimpleContextValueReader.of(
                    raw(ChannelEventContext.class),
                    ctx -> ctx.getChannel().getIdLong()
            );

    public static final ContextValueReader<ChannelEventContext<?>, String> CHANNEL_NAME =
            SimpleContextValueReader.optional(
                    raw(ChannelEventContext.class),
                    ctx -> Optional.ofNullable(ctx.getChannel().getName())
            );

    public static final ContextValueReader<MessageEventContext<?>, Message> MESSAGE =
            SimpleContextValueReader.of(
                    raw(MessageEventContext.class),
                    ctx -> ctx.getMessage()
            );

    public static final ContextValueReader<MessageEventContext<?>, String> MESSAGE_ID =
            SimpleContextValueReader.of(
                    raw(MessageEventContext.class),
                    ctx -> ctx.getMessage().getId()
            );

    public static final ContextValueReader<MessageEventContext<?>, Long> MESSAGE_ID_LONG =
            SimpleContextValueReader.of(
                    raw(MessageEventContext.class),
                    ctx -> ctx.getMessage().getIdLong()
            );

    public static final ContextValueReader<MessageEventContext<?>, String> MESSAGE_CONTENT_RAW =
            SimpleContextValueReader.of(
                    raw(MessageEventContext.class),
                    ctx -> ctx.getMessage().getContentRaw()
            );

    public static final ContextValueReader<MessageEventContext<?>, String> MESSAGE_CONTENT_DISPLAY =
            SimpleContextValueReader.of(
                    raw(MessageEventContext.class),
                    ctx -> ctx.getMessage().getContentDisplay()
            );

    public static final ContextValueReader<MessageReceivedContext, Boolean> IS_GUILD_MESSAGE =
            SimpleContextValueReader.of(
                    MessageReceivedContext.class,
                    MessageReceivedContext::isGuildMessage
            );

    public static final ContextValueReader<MessageReceivedContext, Boolean> IS_DIRECT_MESSAGE =
            SimpleContextValueReader.of(
                    MessageReceivedContext.class,
                    MessageReceivedContext::isDirectMessage
            );

    public static final ContextValueReader<MessageReactionContext<?>, EmojiUnion> REACTION_EMOJI =
            SimpleContextValueReader.of(
                    raw(MessageReactionContext.class),
                    ctx -> ctx.getEmoji()
            );

    public static final ContextValueReader<MessageReactionContext<?>, Long> REACTION_MESSAGE_ID =
            SimpleContextValueReader.of(
                    raw(MessageReactionContext.class),
                    MessageReactionContext::getMessageIdLong
            );

    public static final ContextValueReader<GuildVoiceUpdateContext, AudioChannelUnion> VOICE_CHANNEL_JOINED =
            SimpleContextValueReader.optional(
                    GuildVoiceUpdateContext.class,
                    ctx -> Optional.ofNullable(ctx.getChannelJoinedOrNull())
            );

    public static final ContextValueReader<GuildVoiceUpdateContext, AudioChannelUnion> VOICE_CHANNEL_LEFT =
            SimpleContextValueReader.optional(
                    GuildVoiceUpdateContext.class,
                    ctx -> Optional.ofNullable(ctx.getChannelLeftOrNull())
            );

    public static final ContextValueReader<GuildVoiceUpdateContext, Boolean> VOICE_IS_JOIN =
            SimpleContextValueReader.of(
                    GuildVoiceUpdateContext.class,
                    GuildVoiceUpdateContext::isJoin
            );

    public static final ContextValueReader<GuildVoiceUpdateContext, Boolean> VOICE_IS_LEAVE =
            SimpleContextValueReader.of(
                    GuildVoiceUpdateContext.class,
                    GuildVoiceUpdateContext::isLeave
            );

    public static final ContextValueReader<GuildVoiceUpdateContext, Boolean> VOICE_IS_MOVE =
            SimpleContextValueReader.of(
                    GuildVoiceUpdateContext.class,
                    GuildVoiceUpdateContext::isMove
            );

    public static final ContextValueReader<SlashCommandContext, SlashPath> SLASH_PATH =
            SimpleContextValueReader.of(
                    SlashCommandContext.class,
                    SlashCommandContext::getPath
            );

    public static final ContextValueReader<SlashCommandContext, SlashCommandOptions> SLASH_OPTIONS =
            SimpleContextValueReader.of(
                    SlashCommandContext.class,
                    SlashCommandContext::getOptions
            );

    public static final ContextValueReader<ButtonInteractionContext, String> BUTTON_COMPONENT_ID =
            SimpleContextValueReader.of(
                    ButtonInteractionContext.class,
                    ButtonInteractionContext::getComponentId
            );

    public static final ContextValueReader<StringSelectInteractionContext, String> STRING_SELECT_COMPONENT_ID =
            SimpleContextValueReader.of(
                    StringSelectInteractionContext.class,
                    StringSelectInteractionContext::getComponentId
            );

    public static final ContextValueReader<StringSelectInteractionContext, List<String>> STRING_SELECT_VALUES =
            SimpleContextValueReader.of(
                    StringSelectInteractionContext.class,
                    StringSelectInteractionContext::getValues
            );

    public static final ContextValueReader<ModalInteractionContext, String> MODAL_ID =
            SimpleContextValueReader.of(
                    ModalInteractionContext.class,
                    ModalInteractionContext::getModalId
            );
}