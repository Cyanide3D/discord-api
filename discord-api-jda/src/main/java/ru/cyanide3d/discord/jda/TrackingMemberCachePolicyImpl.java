package ru.cyanide3d.discord.jda;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cyanide3d.discord.jda.api.properties.DiscordJDABotProperties;
import ru.cyanide3d.discord.jda.api.TrackingMemberCachePolicy;

import java.util.HashMap;
import java.util.Map;

public class TrackingMemberCachePolicyImpl implements TrackingMemberCachePolicy, InitializingBean {

    private final Map<String, MemberCachePolicy> memberCachePolicies = new HashMap<>();

    @Autowired
    private DiscordJDABotProperties properties;

    @Override
    public boolean cacheMember(@NotNull Member member) {
        return getCombinedPolicies().cacheMember(member);
    }

    private MemberCachePolicy getCombinedPolicies() {
        return this.memberCachePolicies.values().stream()
                .reduce(MemberCachePolicy::or)
                .orElse(DEFAULT);
    }

    @Override
    public Map<String, MemberCachePolicy> getPolicies() {
        return this.memberCachePolicies;
    }

    @Override
    public MemberCachePolicy getPolicy(String source) {
        return this.memberCachePolicies.get(source);
    }

    @Override
    public void put(String source, MemberCachePolicy policy) {
        this.memberCachePolicies.put(source, policy);
    }

    @Override
    public void remove(String source) {
        this.memberCachePolicies.remove(source);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        properties.getMemberCachePolicies().stream()
                .map(this::buildPolicy)
                .reduce(MemberCachePolicy::or)
                .ifPresent(policy -> memberCachePolicies.put("property", policy));
    }

    protected MemberCachePolicy buildPolicy(String policy) {
        return switch (policy) {
            case "NONE" -> NONE;
            case "ALL" -> ALL;
            case "OWNER" -> OWNER;
            case "ONLINE" -> ONLINE;
            case "VOICE" -> VOICE;
            case "BOOSTER" -> BOOSTER;
            case "PENDING" -> PENDING;
            case "DEFAULT" -> DEFAULT;
            default -> throw new IllegalArgumentException("Unknown policy: " + policy);
        };
    }

}
