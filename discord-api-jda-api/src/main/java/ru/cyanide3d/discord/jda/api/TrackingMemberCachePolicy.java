package ru.cyanide3d.discord.jda.api;

import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.Map;

public interface TrackingMemberCachePolicy extends MemberCachePolicy {

    Map<String, MemberCachePolicy> getPolicies();

    MemberCachePolicy getPolicy(String source);

    void put(String source, MemberCachePolicy policy);

    void remove(String source);

}
