package ru.cyanide3d.discord.jda.api.contexts;

import java.util.List;

public final class InteractionReaders {

    private InteractionReaders() {
    }

    public static final ContextValueReader<InteractionEventContext<?>, Boolean> HAS_GUILD =
            Readers.of(Readers.raw(InteractionEventContext.class), InteractionEventContext::hasGuild);

    public static final ContextValueReader<InteractionEventContext<?>, Boolean> HAS_MEMBER =
            Readers.of(Readers.raw(InteractionEventContext.class), InteractionEventContext::hasMember);

    public static final ContextValueReader<? super InteractionEventContext<?>, String> USER_ID =
            Readers.map(Readers.user(), user -> user.getId());

    public static final ContextValueReader<? super InteractionEventContext<?>, String> USER_NAME =
            Readers.map(Readers.user(), user -> user.getName());

    public static final ContextValueReader<? super InteractionEventContext<?>, String> CHANNEL_ID =
            Readers.map(Readers.channel(), channel -> channel.getId());

    public static final ContextValueReader<? super InteractionEventContext<?>, String> GUILD_ID =
            Readers.flatMap(Readers.guild(), guild -> java.util.Optional.of(guild.getId()));

    public static final ContextValueReader<? super InteractionEventContext<?>, String> MEMBER_EFFECTIVE_NAME =
            Readers.flatMap(Readers.member(), member -> java.util.Optional.of(member.getEffectiveName()));

    public static final ContextValueReader<ButtonInteractionContext, String> BUTTON_COMPONENT_ID =
            Readers.buttonComponentId();

    public static final ContextValueReader<StringSelectInteractionContext, String> STRING_SELECT_COMPONENT_ID =
            Readers.stringSelectComponentId();

    public static final ContextValueReader<StringSelectInteractionContext, List<String>> STRING_SELECT_VALUES =
            Readers.stringSelectValues();

    public static final ContextValueReader<ModalInteractionContext, String> MODAL_ID =
            Readers.modalId();
}