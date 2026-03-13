package ru.cyanide3d.discord.jda.api.command;

import lombok.Getter;
import org.springframework.lang.NonNull;
import ru.cyanide3d.discord.jda.api.restriction.Restriction;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class CommandIndex {

    private final Map<SlashPath, ResolvedSlashLeaf> leaves = new LinkedHashMap<>();

    public void put(@NonNull ResolvedSlashLeaf leaf) {
        Objects.requireNonNull(leaf, "Leaf cannot be null");
        Objects.requireNonNull(leaf.getPath(), "Leaf path cannot be null");
        Objects.requireNonNull(leaf.getExecutor(), "Leaf executor cannot be null");

        ResolvedSlashLeaf previous = leaves.putIfAbsent(leaf.getPath(), leaf);
        if (previous != null) {
            throw new IllegalStateException("Duplicate slash path: " + leaf.getPath());
        }
    }

    public void put(@NonNull SlashPath path, Restriction<?> restriction, @NonNull SlashExecutor executor) {
        this.put(new ResolvedSlashLeaf(path, restriction, executor));
    }

    public ResolvedSlashLeaf get(SlashPath path) {
        return leaves.get(path);
    }

    public Map<SlashPath, ResolvedSlashLeaf> getLeaves() {
        return Collections.unmodifiableMap(leaves);
    }

}