package ru.cyanide3d.discord.jda.api.restriction;


import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import ru.cyanide3d.discord.jda.api.contexts.ChannelEventContext;
import ru.cyanide3d.discord.jda.api.contexts.EventContext;
import ru.cyanide3d.discord.jda.api.contexts.GuildEventContext;
import ru.cyanide3d.discord.jda.api.contexts.InteractionEventContext;
import ru.cyanide3d.discord.jda.api.contexts.MemberEventContext;
import ru.cyanide3d.discord.jda.api.contexts.MessageEventContext;
import ru.cyanide3d.discord.jda.api.contexts.MessageReceivedContext;
import ru.cyanide3d.discord.jda.api.contexts.SlashCommandContext;
import ru.cyanide3d.discord.jda.api.contexts.UserEventContext;
import ru.cyanide3d.discord.jda.api.contexts.capability.InteractionResponseCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageChannelSendCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageReactionCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.MessageReplyCapability;
import ru.cyanide3d.discord.jda.api.contexts.capability.TypingCapability;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static ru.cyanide3d.utils.CastUtils.cast;

@UtilityClass
public class Restrictions {

    public static <T extends EventContext<?>> Restriction<T> and(Restriction<? super T>... restrictions) {
        return Arrays.stream(restrictions)
                .filter(Objects::nonNull)
                .reduce(Restriction.allow(), Restriction::and, Restriction::and);
    }

    public static <T extends EventContext<?>> Restriction<T> or(Restriction<? super T>... restrictions) {
        return Arrays.stream(restrictions)
                .filter(Objects::nonNull)
                .map(r -> (Restriction<T>) r)
                .reduce(Restriction::or)
                .orElseGet(Restriction::allow);
    }

    public static <B extends EventContext<?>> Restriction<EventContext<?>> when(Class<?> contextType, Restriction<? super B> rule, MissingCapabilityPolicy policy) {
        Objects.requireNonNull(contextType, "contextType");
        Objects.requireNonNull(rule, "rule");
        Objects.requireNonNull(policy, "policy");

        return ctx -> {
            if (contextType.isInstance(ctx)) {
                return rule.check(cast(contextType.cast(ctx)));
            }
            return missingCapability(policy);
        };
    }

    public static Restriction<EventContext<?>> whenMessage(Restriction<? super MessageEventContext<?>> rule, MissingCapabilityPolicy policy) {
        return when(MessageEventContext.class, rule, policy);
    }

    public static Restriction<EventContext<?>> whenGuild(Restriction<? super GuildEventContext<?>> rule, MissingCapabilityPolicy policy) {
        return when(GuildEventContext.class, rule, policy);
    }

    public static Restriction<EventContext<?>> whenMember(Restriction<? super MemberEventContext<?>> rule, MissingCapabilityPolicy policy) {
        return when(MemberEventContext.class, rule, policy);
    }

    public static Restriction<EventContext<?>> whenUser(Restriction<? super UserEventContext<?>> rule, MissingCapabilityPolicy policy) {
        return when(UserEventContext.class, rule, policy);
    }

    public static Restriction<EventContext<?>> whenChannel(Restriction<? super ChannelEventContext<?>> rule, MissingCapabilityPolicy policy) {
        return when(ChannelEventContext.class, rule, policy);
    }

    public static Restriction<EventContext<?>> whenInteraction(Restriction<? super InteractionEventContext<?>> rule, MissingCapabilityPolicy policy) {
        return when(InteractionEventContext.class, rule, policy);
    }

    public static Restriction<EventContext<?>> whenSlashCommand(Restriction<? super SlashCommandContext> rule, MissingCapabilityPolicy policy) {
        return when(SlashCommandContext.class, rule, policy);
    }

    public static Restriction<EventContext<?>> whenMessageReceived(Restriction<? super MessageReceivedContext> rule, MissingCapabilityPolicy policy) {
        return when(MessageReceivedContext.class, rule, policy);
    }

    public static <T extends EventContext<?>> Restriction<T> predicate(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        return ctx -> predicate.test(ctx)
                ? RestrictionResult.allow()
                : RestrictionResult.deny();
    }

    public static <T extends EventContext<?>> Restriction<T> predicate(Predicate<? super T> predicate, String denyReason) {
        Objects.requireNonNull(predicate, "predicate");
        return ctx -> predicate.test(ctx)
                ? RestrictionResult.allow()
                : RestrictionResult.deny(denyReason);
    }

    public static <T extends EventContext<?>> Restriction<T> predicate(Predicate<? super T> predicate, Function<? super T, String> denyReasonProvider) {
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(denyReasonProvider, "denyReasonProvider");

        return ctx -> predicate.test(ctx)
                ? RestrictionResult.allow()
                : RestrictionResult.deny(denyReasonProvider.apply(ctx));
    }

    public static Restriction<EventContext<?>> messageOnly() {
        return whenMessage(Restriction.allow(), MissingCapabilityPolicy.DENY);
    }

    public static Restriction<EventContext<?>> guildOnly() {
        return whenGuild(
                ctx -> ctx.hasGuild()
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny("This action is available only in guild."),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> memberOnly() {
        return whenMember(
                ctx -> ctx.hasMember()
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny("This action requires guild member context."),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> selfBotOnly() {
        return whenMember(
                ctx -> ctx.requireMember().equals(ctx.requireGuild().getSelfMember())
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny(),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> interactionOnly() {
        return whenInteraction(Restriction.allow(), MissingCapabilityPolicy.DENY);
    }

    public static Restriction<EventContext<?>> slashCommandOnly() {
        return whenSlashCommand(Restriction.allow(), MissingCapabilityPolicy.DENY);
    }

    public static Restriction<EventContext<?>> messageReceivedOnly() {
        return whenMessageReceived(Restriction.allow(), MissingCapabilityPolicy.DENY);
    }

    public static Restriction<EventContext<?>> canReplyToInteractionOnly() {
        return predicate(
                ctx -> ctx instanceof InteractionResponseCapability,
                "This action requires interaction reply capability."
        );
    }

    public static Restriction<EventContext<?>> canReplyToMessageOnly() {
        return predicate(
                ctx -> ctx instanceof MessageReplyCapability,
                "This action requires message reply capability."
        );
    }

    public static Restriction<EventContext<?>> canSendMessageOnly() {
        return predicate(
                ctx -> ctx instanceof MessageChannelSendCapability,
                "This action requires channel send capability."
        );
    }

    public static Restriction<EventContext<?>> canTypeOnly() {
        return predicate(
                ctx -> ctx instanceof TypingCapability,
                "This action requires typing capability."
        );
    }

    public static Restriction<EventContext<?>> canReactOnly() {
        return predicate(
                ctx -> ctx instanceof MessageReactionCapability,
                "This action requires reaction capability."
        );
    }

    public static Restriction<EventContext<?>> guildMessageOnly() {
        return whenMessageReceived(
                ctx -> ctx.isGuildMessage()
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny("This action is available only for guild messages."),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> directMessageOnly() {
        return whenMessageReceived(
                ctx -> ctx.isDirectMessage()
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny("This action is available only in direct messages."),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> notWebhookMessage() {
        return whenMessageReceived(
                ctx -> !ctx.getEvent().isWebhookMessage()
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny("Webhook messages are not allowed."),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> notBotUser() {
        return whenUser(
                ctx -> !ctx.getUser().isBot()
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny("Bots are not allowed."),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> humanMessageOnly() {
        return and(
                messageReceivedOnly(),
                notWebhookMessage(),
                notBotUser()
        );
    }

    public static Restriction<EventContext<?>> userId(long userId) {
        return userId(Long.toUnsignedString(userId));
    }

    public static Restriction<EventContext<?>> userId(String userId) {
        Objects.requireNonNull(userId, "userId");
        return whenUser(
                ctx -> userId.equals(ctx.getUser().getId())
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny("This action is not allowed for this user."),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> userIds(Collection<String> userIds) {
        Objects.requireNonNull(userIds, "userIds");
        return whenUser(
                ctx -> userIds.contains(ctx.getUser().getId())
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny("This action is not allowed for this user."),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> user(Predicate<? super User> predicate) {
        return user(predicate, "User restriction check failed.");
    }

    public static Restriction<EventContext<?>> user(Predicate<? super User> predicate, String denyReason) {
        Objects.requireNonNull(predicate, "predicate");
        return whenUser(
                ctx -> predicate.test(ctx.getUser())
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny(denyReason),
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> member(Predicate<? super Member> predicate) {
        return member(predicate, "Member restriction check failed.");
    }

    public static Restriction<EventContext<?>> member(Predicate<? super Member> predicate, String denyReason) {
        Objects.requireNonNull(predicate, "predicate");
        return whenMember(
                ctx -> {
                    Member member = ctx.getMemberOrNull();
                    if (member == null) {
                        return RestrictionResult.deny(denyReason);
                    }
                    return predicate.test(member)
                            ? RestrictionResult.allow()
                            : RestrictionResult.deny(denyReason);
                },
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> guild(Predicate<? super Guild> predicate) {
        return guild(predicate, "Guild restriction check failed.");
    }

    public static Restriction<EventContext<?>> guild(Predicate<? super Guild> predicate, String denyReason) {
        Objects.requireNonNull(predicate, "predicate");
        return whenGuild(
                ctx -> {
                    Guild guild = ctx.getGuildOrNull();
                    if (guild == null) {
                        return RestrictionResult.deny(denyReason);
                    }
                    return predicate.test(guild)
                            ? RestrictionResult.allow()
                            : RestrictionResult.deny(denyReason);
                },
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> guildId(long guildId) {
        return guildId(Long.toUnsignedString(guildId));
    }

    public static Restriction<EventContext<?>> guildId(String guildId) {
        Objects.requireNonNull(guildId, "guildId");
        return guild(
                g -> guildId.equals(g.getId()),
                "This action is not allowed in this guild."
        );
    }

    public static Restriction<EventContext<?>> guildIds(Collection<String> guildIds) {
        Objects.requireNonNull(guildIds, "guildIds");
        return guild(
                g -> guildIds.contains(g.getId()),
                "This action is not allowed in this guild."
        );
    }

    public static Restriction<EventContext<?>> hasRole(String roleId) {
        Objects.requireNonNull(roleId, "roleId");
        return member(
                m -> m.getRoles().stream().anyMatch(role -> roleId.equals(role.getId())),
                "Required role is missing."
        );
    }

    public static Restriction<EventContext<?>> hasAnyRole(Collection<String> roleIds) {
        Objects.requireNonNull(roleIds, "roleIds");
        return member(
                m -> m.getRoles().stream().anyMatch(role -> roleIds.contains(role.getId())),
                "None of required roles is present."
        );
    }

    public static Restriction<EventContext<?>> hasAllRoles(Collection<String> roleIds) {
        Objects.requireNonNull(roleIds, "roleIds");
        return member(
                m -> roleIds.stream().allMatch(requiredId ->
                        m.getRoles().stream().anyMatch(role -> requiredId.equals(role.getId()))
                ),
                "Not all required roles are present."
        );
    }

    public static Restriction<EventContext<?>> hasPermission(Permission permission) {
        Objects.requireNonNull(permission, "permission");
        return member(
                m -> m.hasPermission(permission),
                "Required permission is missing."
        );
    }

    public static Restriction<EventContext<?>> hasAnyPermission(Permission... permissions) {
        Objects.requireNonNull(permissions, "permissions");
        return member(
                m -> Arrays.stream(permissions).anyMatch(m::hasPermission),
                "None of required permissions is present."
        );
    }

    public static Restriction<EventContext<?>> hasAllPermissions(Permission... permissions) {
        Objects.requireNonNull(permissions, "permissions");
        return member(
                m -> m.hasPermission(permissions),
                "Not all required permissions are present."
        );
    }

    public static Restriction<EventContext<?>> administratorOnly() {
        return hasPermission(Permission.ADMINISTRATOR);
    }

    public static Restriction<EventContext<?>> channel(Predicate<? super Channel> predicate) {
        return channel(predicate, "Channel restriction check failed.");
    }

    public static Restriction<EventContext<?>> channel(Predicate<? super Channel> predicate, String denyReason) {
        Objects.requireNonNull(predicate, "predicate");
        return whenChannel(
                ctx -> {
                    Channel channel = ctx.getChannel();
                    return predicate.test(channel)
                            ? RestrictionResult.allow()
                            : RestrictionResult.deny(denyReason);
                },
                MissingCapabilityPolicy.DENY
        );
    }

    public static Restriction<EventContext<?>> channelId(long channelId) {
        return channelId(Long.toUnsignedString(channelId));
    }

    public static Restriction<EventContext<?>> channelId(String channelId) {
        Objects.requireNonNull(channelId, "channelId");
        return channel(
                ch -> channelId.equals(ch.getId()),
                "This action is not allowed in this channel."
        );
    }

    public static Restriction<EventContext<?>> channelIds(Collection<String> channelIds) {
        Objects.requireNonNull(channelIds, "channelIds");
        return channel(
                ch -> channelIds.contains(ch.getId()),
                "This action is not allowed in this channel."
        );
    }

    public static Restriction<EventContext<?>> message(Predicate<? super Message> predicate) {
        return message(predicate, "Message restriction check failed.");
    }

    public static Restriction<EventContext<?>> message(Predicate<? super Message> predicate, String denyReason) {
        Objects.requireNonNull(predicate, "predicate");
        return whenMessage(
                ctx -> predicate.test(ctx.getMessage())
                        ? RestrictionResult.allow()
                        : RestrictionResult.deny(denyReason),
                MissingCapabilityPolicy.DENY
        );
    }

    private static RestrictionResult missingCapability(MissingCapabilityPolicy policy) {
        return policy == MissingCapabilityPolicy.DENY
                ? RestrictionResult.deny()
                : RestrictionResult.allow();
    }

}
