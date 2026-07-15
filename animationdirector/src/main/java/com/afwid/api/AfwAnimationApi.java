/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.registry.Registries
 *  net.minecraft.network.packet.CustomPayload
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.api;

import com.afwid.api.AfwDamageBehavior;
import com.afwid.data.AfwAnimationDefinitions;
import com.afwid.network.AnimationStageInfo;
import com.afwid.network.StopAllAnimationsS2CPayload;
import com.afwid.network.AfwServerNetworking;
import com.afwid.server.AfwServerAnimationController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

public final class AfwAnimationApi {
    private AfwAnimationApi() {
    }

    public static boolean hasEnabledGenericAnimationForEntityType(@Nullable EntityType<?> type, boolean baby) {
        if (type == null) {
            return false;
        }
        return AfwAnimationApi.hasEnabledGenericAnimationForEntityType(Registries.ENTITY_TYPE.getId(type), baby);
    }

    public static boolean hasEnabledGenericAnimationForEntityType(@Nullable Identifier entityTypeId, boolean baby) {
        return AfwAnimationDefinitions.hasEnabledGenericAnimationForEntityType(entityTypeId, baby);
    }

    @Nullable
    public static UUID startAnimationNow(ServerWorld world, Identifier animationId, List<? extends Entity> actors, @Nullable List<String> actorKeys, @Nullable List<AnimationStageInfo> stages) {
        return AfwAnimationApi.startAnimationNow(world, animationId, actors, actorKeys, stages, StartOptions.defaults());
    }

    @Nullable
    public static UUID startAnimationNow(ServerWorld world, Identifier animationId, List<? extends Entity> actors, @Nullable List<String> actorKeys, @Nullable List<AnimationStageInfo> stages, @Nullable StartOptions options) {
        if (world == null || animationId == null || actors == null || actors.isEmpty()) {
            return null;
        }
        if (!AfwAnimationDefinitions.isAnimationEnabled(animationId)) {
            return null;
        }
        StartOptions safeOptions = options == null ? StartOptions.defaults() : options;
        ArrayList<Entity> actorList = new ArrayList<Entity>(actors.size());
        actorList.addAll(actors);
        actorList.sort(Comparator.comparingInt(Entity::getId));
        UUID positionAnchorUuid = null;
        Entity positionAnchor = safeOptions.positionAnchor();
        if (positionAnchor != null && positionAnchor.getEntityWorld() == world) {
            UUID candidate = positionAnchor.getUuid();
            boolean anchorIsActor = actorList.stream().anyMatch(e -> e.getUuid().equals(candidate));
            if (anchorIsActor) {
                positionAnchorUuid = candidate;
            }
        }
        return AfwServerAnimationController.startNow(world, safeOptions.requester(), animationId, actorList, actorKeys, stages, safeOptions.damageBehavior() == null ? AfwDamageBehavior.IGNORE_DAMAGE : safeOptions.damageBehavior(), safeOptions.ignoreAttackers(), positionAnchorUuid, AfwAnimationApi.sanitizeMetadata(safeOptions.metadata()), safeOptions.forceChat());
    }

    @Nullable
    public static Identifier findMatchedAnimationId(List<? extends Entity> actors, @Nullable Set<String> requiredAnimationTags) {
        if (actors == null || actors.isEmpty()) {
            return null;
        }
        ArrayList<Entity> actorList = new ArrayList<Entity>(actors.size());
        actorList.addAll(actors);
        actorList.sort(Comparator.comparingInt(Entity::getId));
        AfwAnimationDefinitions.MatchResult match = AfwAnimationDefinitions.match(actorList, requiredAnimationTags == null ? Set.of() : requiredAnimationTags);
        AfwAnimationDefinitions.Definition chosen = match.chosen();
        return chosen == null ? null : chosen.id();
    }

    @Nullable
    public static MatchedAnimation findAnimationForActors(ServerWorld world, List<? extends Entity> actors, @Nullable MatchOptions options) {
        AfwAnimationDefinitions.MatchResult match;
        List<AfwAnimationDefinitions.Definition> candidates;
        if (world == null || actors == null || actors.isEmpty()) {
            return null;
        }
        MatchOptions safeOptions = options == null ? MatchOptions.defaults() : options;
        ArrayList<Entity> actorList = new ArrayList<Entity>(actors.size());
        actorList.addAll(actors);
        actorList.sort(Comparator.comparingInt(Entity::getId));
        Set<String> safeTags = safeOptions.requiredAnimationTags() == null ? Set.of() : safeOptions.requiredAnimationTags();
        Set<Identifier> excluded = safeOptions.excludedAnimationIds() == null
                ? Set.of() : safeOptions.excludedAnimationIds();
        UUID positionAnchorUuid = null;
        Entity positionAnchor = safeOptions.positionAnchor();
        if (positionAnchor != null && positionAnchor.getEntityWorld() == world) {
            UUID candidate = positionAnchor.getUuid();
            boolean anchorIsActor = actors.stream().anyMatch(e -> e != null && e.getUuid().equals(candidate));
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
            Entity activeActor;
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
    private static MatchedAnimation pickWeightedMatchedAnimation(ServerWorld world, List<Entity> actors, List<AfwAnimationDefinitions.Definition> definitions) {
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
            picked = definitions.get(world.getRandom().nextInt(definitions.size()));
        } else {
            double roll = world.getRandom().nextDouble() * totalWeight;
            double running = 0.0;
            picked = definitions.get(definitions.size() - 1);
            for (AfwAnimationDefinitions.Definition definition : definitions) {
                if (definition == null || !(roll < (running += AfwAnimationApi.sanitizeWeight(definition.weight())))) continue;
                picked = definition;
                break;
            }
        }
        if (picked == null) {
            return null;
        }
        List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(picked, actors, world.getRandom());
        if (actorKeys == null || actorKeys.size() != actors.size()) {
            return null;
        }
        return new MatchedAnimation(picked.id(), List.copyOf(actorKeys), picked.stages() == null ? List.of() : List.copyOf(picked.stages()));
    }

    private static double sanitizeWeight(double weight) {
        return Double.isFinite(weight) && weight > 0.0 ? weight : 0.0;
    }

    public static boolean isAnimationStartEligible(ServerWorld world, @Nullable AfwAnimationDefinitions.Definition definition, List<? extends Entity> actors, @Nullable Entity positionAnchor) {
        if (world == null || actors == null || actors.isEmpty()) {
            return false;
        }
        UUID positionAnchorUuid = null;
        if (positionAnchor != null && positionAnchor.getEntityWorld() == world) {
            UUID candidate = positionAnchor.getUuid();
            boolean anchorIsActor = actors.stream().anyMatch(e -> e != null && e.getUuid().equals(candidate));
            if (anchorIsActor) {
                positionAnchorUuid = candidate;
            }
        }
        return AfwServerAnimationController.isDefinitionStartEligible(world, definition, actors, positionAnchorUuid);
    }

    public static boolean advanceStage(ServerWorld world, UUID instanceId) {
        return AfwServerAnimationController.advanceStage(world, instanceId);
    }

    public static boolean advanceToStage(ServerWorld world, UUID instanceId, int stageNumber) {
        return AfwServerAnimationController.advanceToStage(world, instanceId, stageNumber);
    }

    public static void adjustSpeedSilently(ServerWorld world, UUID instanceId, double multiplier) {
        AfwServerAnimationController.adjustSpeedSilently(world, instanceId, multiplier);
    }

    @Nullable
    public static AnimationStageInfo getCurrentStage(ServerWorld world, UUID instanceId) {
        return AfwServerAnimationController.getCurrentStage(world, instanceId);
    }

    public static Map<String, String> getInstanceMetadata(ServerWorld world, UUID instanceId) {
        return AfwServerAnimationController.getInstanceMetadata(world, instanceId);
    }

    public static boolean stopAnimation(ServerWorld world, UUID instanceId) {
        return AfwServerAnimationController.stopInstance(world, instanceId);
    }

    public static boolean enqueueAnimation(ServerWorld world, Identifier animationId, List<? extends Entity> actors, int insertIndex, @Nullable AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable Map<String, String> metadata, @Nullable ServerPlayerEntity requester) {
        return AfwServerAnimationController.enqueueForPlayer(world, requester, animationId, actors, insertIndex, damageBehavior == null ? AfwDamageBehavior.IGNORE_DAMAGE : damageBehavior, ignoreAttackers, AfwAnimationApi.sanitizeMetadata(metadata), false);
    }

    public static void clearQueuedAnimations(ServerWorld world, ServerPlayerEntity player) {
        AfwServerAnimationController.clearPlayerQueue(world, player);
    }

    public static void clearQueuedAnimationsForInstance(ServerWorld world, UUID instanceId) {
        AfwServerAnimationController.clearQueueForInstance(world, instanceId);
    }

    public static boolean isActorPendingOrActive(ServerWorld world, UUID actorUuid) {
        return AfwServerAnimationController.isActorPendingOrActive(world, actorUuid);
    }

    public static boolean shouldIgnoreAttackers(ServerWorld world, LivingEntity actor) {
        if (world == null || actor == null) {
            return false;
        }
        return AfwServerAnimationController.shouldIgnoreAttackers(world, actor);
    }

    public static void setAnimationStartProtection(ServerPlayerEntity player, int ticks) {
        AfwServerAnimationController.setAnimationStartProtection(player, ticks);
    }

    public static void clearAnimationStartProtection(ServerPlayerEntity player) {
        AfwServerAnimationController.clearAnimationStartProtection(player);
    }

    public static boolean hasAnimationStartProtection(ServerWorld world, ServerPlayerEntity player) {
        return AfwServerAnimationController.hasAnimationStartProtection(world, player);
    }

    public static void stopAll(ServerWorld world) {
        long stopTick = world.getTime() + 1L;
        StopAllAnimationsS2CPayload payload = new StopAllAnimationsS2CPayload(stopTick);
        for (ServerPlayerEntity player : Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayerList()) {
            if (player.getEntityWorld() != world) continue;
            AfwServerNetworking.send(player, payload);
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

    public record StartOptions(@Nullable AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable Entity positionAnchor, @Nullable ServerPlayerEntity requester, @Nullable Map<String, String> metadata, boolean forceChat) {
        public static StartOptions defaults() {
            return new StartOptions(AfwDamageBehavior.IGNORE_DAMAGE, false, null, null, Map.of(), false);
        }
    }

    public record MatchOptions(@Nullable Set<String> requiredAnimationTags, @Nullable Entity positionAnchor, boolean requireStartEligibility, @Nullable Entity requiredActiveActor, @Nullable Set<Identifier> excludedAnimationIds, @Nullable AnimationCandidateFilter candidateFilter) {
        public MatchOptions(@Nullable Set<String> requiredAnimationTags, @Nullable Entity positionAnchor, boolean requireStartEligibility, @Nullable Entity requiredActiveActor, @Nullable Set<Identifier> excludedAnimationIds) {
            this(requiredAnimationTags, positionAnchor, requireStartEligibility, requiredActiveActor, excludedAnimationIds, null);
        }

        public static MatchOptions defaults() {
            return new MatchOptions(Set.of(), null, false, null, Set.of(), null);
        }

        public static MatchOptions startEligible(@Nullable Entity positionAnchor) {
            return new MatchOptions(Set.of(), positionAnchor, true, null, Set.of(), null);
        }
    }

    public record MatchedAnimation(Identifier animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
    }

    @FunctionalInterface
    public static interface AnimationCandidateFilter {
        public boolean test(ServerWorld var1, AfwAnimationDefinitions.Definition var2, List<Entity> var3);
    }
}

