/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1309
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_7923
 *  net.minecraft.class_8710
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.api;

import com.afwid.api.AfwDamageBehavior;
import com.afwid.data.AfwAnimationDefinitions;
import com.afwid.network.AnimationStageInfo;
import com.afwid.network.StopAllAnimationsS2CPayload;
import com.afwid.server.AfwServerAnimationController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_7923;
import net.minecraft.class_8710;
import org.jetbrains.annotations.Nullable;

public final class AfwAnimationApi {
    private AfwAnimationApi() {
    }

    public static boolean hasEnabledGenericAnimationForEntityType(@Nullable class_1299<?> type, boolean baby) {
        if (type == null) {
            return false;
        }
        return AfwAnimationApi.hasEnabledGenericAnimationForEntityType(class_7923.field_41177.method_10221(type), baby);
    }

    public static boolean hasEnabledGenericAnimationForEntityType(@Nullable class_2960 entityTypeId, boolean baby) {
        return AfwAnimationDefinitions.hasEnabledGenericAnimationForEntityType(entityTypeId, baby);
    }

    @Nullable
    public static UUID startAnimationNow(class_3218 world, class_2960 animationId, List<? extends class_1297> actors, @Nullable List<String> actorKeys, @Nullable List<AnimationStageInfo> stages) {
        return AfwAnimationApi.startAnimationNow(world, animationId, actors, actorKeys, stages, StartOptions.defaults());
    }

    @Nullable
    public static UUID startAnimationNow(class_3218 world, class_2960 animationId, List<? extends class_1297> actors, @Nullable List<String> actorKeys, @Nullable List<AnimationStageInfo> stages, @Nullable StartOptions options) {
        if (world == null || animationId == null || actors == null || actors.isEmpty()) {
            return null;
        }
        if (!AfwAnimationDefinitions.isAnimationEnabled(animationId)) {
            return null;
        }
        StartOptions safeOptions = options == null ? StartOptions.defaults() : options;
        ArrayList<class_1297> actorList = new ArrayList<class_1297>(actors.size());
        actorList.addAll(actors);
        actorList.sort(Comparator.comparingInt(class_1297::method_5628));
        UUID positionAnchorUuid = null;
        class_1297 positionAnchor = safeOptions.positionAnchor();
        if (positionAnchor != null && positionAnchor.method_73183() == world) {
            UUID candidate = positionAnchor.method_5667();
            boolean anchorIsActor = actorList.stream().anyMatch(e -> e.method_5667().equals(candidate));
            if (anchorIsActor) {
                positionAnchorUuid = candidate;
            }
        }
        return AfwServerAnimationController.startNow(world, safeOptions.requester(), animationId, actorList, actorKeys, stages, safeOptions.damageBehavior() == null ? AfwDamageBehavior.IGNORE_DAMAGE : safeOptions.damageBehavior(), safeOptions.ignoreAttackers(), positionAnchorUuid, AfwAnimationApi.sanitizeMetadata(safeOptions.metadata()), safeOptions.forceChat());
    }

    @Nullable
    public static class_2960 findMatchedAnimationId(List<? extends class_1297> actors, @Nullable Set<String> requiredAnimationTags) {
        if (actors == null || actors.isEmpty()) {
            return null;
        }
        ArrayList<class_1297> actorList = new ArrayList<class_1297>(actors.size());
        actorList.addAll(actors);
        actorList.sort(Comparator.comparingInt(class_1297::method_5628));
        AfwAnimationDefinitions.MatchResult match = AfwAnimationDefinitions.match(actorList, requiredAnimationTags == null ? Set.of() : requiredAnimationTags);
        AfwAnimationDefinitions.Definition chosen = match.chosen();
        return chosen == null ? null : chosen.id();
    }

    @Nullable
    public static MatchedAnimation findAnimationForActors(class_3218 world, List<? extends class_1297> actors, @Nullable MatchOptions options) {
        AfwAnimationDefinitions.MatchResult match;
        List<AfwAnimationDefinitions.Definition> candidates;
        if (world == null || actors == null || actors.isEmpty()) {
            return null;
        }
        MatchOptions safeOptions = options == null ? MatchOptions.defaults() : options;
        ArrayList<class_1297> actorList = new ArrayList<class_1297>(actors.size());
        actorList.addAll(actors);
        actorList.sort(Comparator.comparingInt(class_1297::method_5628));
        Set<String> safeTags = safeOptions.requiredAnimationTags() == null ? Set.of() : safeOptions.requiredAnimationTags();
        Set<Object> excluded = safeOptions.excludedAnimationIds() == null ? Set.of() : safeOptions.excludedAnimationIds();
        UUID positionAnchorUuid = null;
        class_1297 positionAnchor = safeOptions.positionAnchor();
        if (positionAnchor != null && positionAnchor.method_73183() == world) {
            UUID candidate = positionAnchor.method_5667();
            boolean anchorIsActor = actors.stream().anyMatch(e -> e != null && e.method_5667().equals(candidate));
            if (anchorIsActor) {
                positionAnchorUuid = candidate;
            }
        }
        if ((candidates = (match = AfwAnimationDefinitions.match(actorList, safeTags)).candidatesSorted()) == null || candidates.isEmpty()) {
            return null;
        }
        int currentSpecificity = Integer.MIN_VALUE;
        ArrayList<AfwAnimationDefinitions.Definition> eligibleAtSpecificity = new ArrayList<AfwAnimationDefinitions.Definition>();
        for (AfwAnimationDefinitions.Definition candidate : candidates) {
            AnimationCandidateFilter candidateFilter;
            class_1297 activeActor;
            if (candidate == null) continue;
            if (currentSpecificity == Integer.MIN_VALUE) {
                currentSpecificity = candidate.specificity();
            } else if (candidate.specificity() != currentSpecificity) {
                MatchedAnimation picked = AfwAnimationApi.pickWeightedMatchedAnimation(world, actorList, eligibleAtSpecificity);
                if (picked != null) {
                    return picked;
                }
                eligibleAtSpecificity.clear();
                currentSpecificity = candidate.specificity();
            }
            if (excluded.contains(candidate.id()) || (activeActor = safeOptions.requiredActiveActor()) != null && !AfwAnimationDefinitions.canResolveActorAsActivity(candidate, actorList, activeActor, AfwAnimationDefinitions.ActorActivity.ACTIVE) || safeOptions.requireStartEligibility() && !AfwServerAnimationController.isDefinitionStartEligible(world, candidate, actorList, positionAnchorUuid) || (candidateFilter = safeOptions.candidateFilter()) != null && !candidateFilter.test(world, candidate, List.copyOf(actorList))) continue;
            eligibleAtSpecificity.add(candidate);
        }
        return AfwAnimationApi.pickWeightedMatchedAnimation(world, actorList, eligibleAtSpecificity);
    }

    @Nullable
    private static MatchedAnimation pickWeightedMatchedAnimation(class_3218 world, List<class_1297> actors, List<AfwAnimationDefinitions.Definition> definitions) {
        AfwAnimationDefinitions.Definition picked;
        if (world == null || actors == null || definitions == null || definitions.isEmpty()) {
            return null;
        }
        double totalWeight = 0.0;
        for (AfwAnimationDefinitions.Definition definition : definitions) {
            double weight;
            if (definition == null || !((weight = AfwAnimationApi.sanitizeWeight(definition.weight())) > 0.0)) continue;
            totalWeight += weight;
        }
        if (!(totalWeight > 0.0) || !Double.isFinite(totalWeight)) {
            picked = definitions.get(world.method_8409().method_43048(definitions.size()));
        } else {
            double roll = world.method_8409().method_43058() * totalWeight;
            double running = 0.0;
            picked = definitions.getLast();
            for (AfwAnimationDefinitions.Definition definition : definitions) {
                if (definition == null || !(roll < (running += AfwAnimationApi.sanitizeWeight(definition.weight())))) continue;
                picked = definition;
                break;
            }
        }
        if (picked == null) {
            return null;
        }
        List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(picked, actors, world.method_8409());
        if (actorKeys == null || actorKeys.size() != actors.size()) {
            return null;
        }
        return new MatchedAnimation(picked.id(), List.copyOf(actorKeys), picked.stages() == null ? List.of() : List.copyOf(picked.stages()));
    }

    private static double sanitizeWeight(double weight) {
        return Double.isFinite(weight) && weight > 0.0 ? weight : 0.0;
    }

    public static boolean isAnimationStartEligible(class_3218 world, @Nullable AfwAnimationDefinitions.Definition definition, List<? extends class_1297> actors, @Nullable class_1297 positionAnchor) {
        if (world == null || actors == null || actors.isEmpty()) {
            return false;
        }
        UUID positionAnchorUuid = null;
        if (positionAnchor != null && positionAnchor.method_73183() == world) {
            UUID candidate = positionAnchor.method_5667();
            boolean anchorIsActor = actors.stream().anyMatch(e -> e != null && e.method_5667().equals(candidate));
            if (anchorIsActor) {
                positionAnchorUuid = candidate;
            }
        }
        return AfwServerAnimationController.isDefinitionStartEligible(world, definition, actors, positionAnchorUuid);
    }

    public static boolean advanceStage(class_3218 world, UUID instanceId) {
        return AfwServerAnimationController.advanceStage(world, instanceId);
    }

    public static boolean advanceToStage(class_3218 world, UUID instanceId, int stageNumber) {
        return AfwServerAnimationController.advanceToStage(world, instanceId, stageNumber);
    }

    public static void adjustSpeedSilently(class_3218 world, UUID instanceId, double multiplier) {
        AfwServerAnimationController.adjustSpeedSilently(world, instanceId, multiplier);
    }

    @Nullable
    public static AnimationStageInfo getCurrentStage(class_3218 world, UUID instanceId) {
        return AfwServerAnimationController.getCurrentStage(world, instanceId);
    }

    public static Map<String, String> getInstanceMetadata(class_3218 world, UUID instanceId) {
        return AfwServerAnimationController.getInstanceMetadata(world, instanceId);
    }

    public static boolean stopAnimation(class_3218 world, UUID instanceId) {
        return AfwServerAnimationController.stopInstance(world, instanceId);
    }

    public static boolean enqueueAnimation(class_3218 world, class_2960 animationId, List<? extends class_1297> actors, int insertIndex, @Nullable AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable Map<String, String> metadata, @Nullable class_3222 requester) {
        return AfwServerAnimationController.enqueueForPlayer(world, requester, animationId, actors, insertIndex, damageBehavior == null ? AfwDamageBehavior.IGNORE_DAMAGE : damageBehavior, ignoreAttackers, AfwAnimationApi.sanitizeMetadata(metadata), false);
    }

    public static void clearQueuedAnimations(class_3218 world, class_3222 player) {
        AfwServerAnimationController.clearPlayerQueue(world, player);
    }

    public static void clearQueuedAnimationsForInstance(class_3218 world, UUID instanceId) {
        AfwServerAnimationController.clearQueueForInstance(world, instanceId);
    }

    public static boolean isActorPendingOrActive(class_3218 world, UUID actorUuid) {
        return AfwServerAnimationController.isActorPendingOrActive(world, actorUuid);
    }

    public static boolean shouldIgnoreAttackers(class_3218 world, class_1309 actor) {
        if (world == null || actor == null) {
            return false;
        }
        return AfwServerAnimationController.shouldIgnoreAttackers(world, actor);
    }

    public static void setAnimationStartProtection(class_3222 player, int ticks) {
        AfwServerAnimationController.setAnimationStartProtection(player, ticks);
    }

    public static void clearAnimationStartProtection(class_3222 player) {
        AfwServerAnimationController.clearAnimationStartProtection(player);
    }

    public static boolean hasAnimationStartProtection(class_3218 world, class_3222 player) {
        return AfwServerAnimationController.hasAnimationStartProtection(world, player);
    }

    public static void stopAll(class_3218 world) {
        long stopTick = world.method_75260() + 1L;
        StopAllAnimationsS2CPayload payload = new StopAllAnimationsS2CPayload(stopTick);
        for (class_3222 player : Objects.requireNonNull(world.method_8503()).method_3760().method_14571()) {
            if (player.method_51469() != world) continue;
            ServerPlayNetworking.send((class_3222)player, (class_8710)payload);
        }
        AfwServerAnimationController.clearAllInstancesInWorld(world);
    }

    private static Map<String, String> sanitizeMetadata(@Nullable Map<String, String> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, String> out = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            if (entry == null) continue;
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null || key.isBlank() || value == null) continue;
            out.put(key, value);
        }
        return out.isEmpty() ? Map.of() : Map.copyOf(out);
    }

    public record StartOptions(@Nullable AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable class_1297 positionAnchor, @Nullable class_3222 requester, @Nullable Map<String, String> metadata, boolean forceChat) {
        public static StartOptions defaults() {
            return new StartOptions(AfwDamageBehavior.IGNORE_DAMAGE, false, null, null, Map.of(), false);
        }
    }

    public record MatchOptions(@Nullable Set<String> requiredAnimationTags, @Nullable class_1297 positionAnchor, boolean requireStartEligibility, @Nullable class_1297 requiredActiveActor, @Nullable Set<class_2960> excludedAnimationIds, @Nullable AnimationCandidateFilter candidateFilter) {
        public MatchOptions(@Nullable Set<String> requiredAnimationTags, @Nullable class_1297 positionAnchor, boolean requireStartEligibility, @Nullable class_1297 requiredActiveActor, @Nullable Set<class_2960> excludedAnimationIds) {
            this(requiredAnimationTags, positionAnchor, requireStartEligibility, requiredActiveActor, excludedAnimationIds, null);
        }

        public static MatchOptions defaults() {
            return new MatchOptions(Set.of(), null, false, null, Set.of(), null);
        }

        public static MatchOptions startEligible(@Nullable class_1297 positionAnchor) {
            return new MatchOptions(Set.of(), positionAnchor, true, null, Set.of(), null);
        }
    }

    public record MatchedAnimation(class_2960 animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
    }

    @FunctionalInterface
    public static interface AnimationCandidateFilter {
        public boolean test(class_3218 var1, AfwAnimationDefinitions.Definition var2, List<class_1297> var3);
    }
}

