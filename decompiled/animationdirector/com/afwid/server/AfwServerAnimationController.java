/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
 *  net.fabricmc.fabric.api.networking.v1.PlayerLookup
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.class_1282
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1922
 *  net.minecraft.class_1937
 *  net.minecraft.class_2244
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2374
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2482
 *  net.minecraft.class_2561
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_2742
 *  net.minecraft.class_2769
 *  net.minecraft.class_2771
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_3481
 *  net.minecraft.class_3486
 *  net.minecraft.class_3532
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_5321
 *  net.minecraft.class_5819
 *  net.minecraft.class_6862
 *  net.minecraft.class_7923
 *  net.minecraft.class_7924
 *  net.minecraft.class_8111
 *  net.minecraft.class_8710
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.server;

import com.afwid.AfwDebugChatCategory;
import com.afwid.AnimationFramework;
import com.afwid.api.AfwAnimationEvents;
import com.afwid.api.AfwDamageBehavior;
import com.afwid.client.config.AfwClientConfig;
import com.afwid.data.AfwAnimationDefinitions;
import com.afwid.network.AdvanceAnimationStageS2CPayload;
import com.afwid.network.AnimationSpeedUpdateS2CPayload;
import com.afwid.network.AnimationStageInfo;
import com.afwid.network.StartAnimationS2CPayload;
import com.afwid.network.StopAnimationS2CPayload;
import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1922;
import net.minecraft.class_1937;
import net.minecraft.class_2244;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2482;
import net.minecraft.class_2561;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2742;
import net.minecraft.class_2769;
import net.minecraft.class_2771;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3481;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_5321;
import net.minecraft.class_5819;
import net.minecraft.class_6862;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import net.minecraft.class_8111;
import net.minecraft.class_8710;
import org.jetbrains.annotations.Nullable;

public final class AfwServerAnimationController {
    private static final double DEBUG_MULTI_PLAYER_REQUESTER_MAX_DIST = 5.0;
    private static final int BLOCK_SCAN_VERTICAL_RANGE = 1;
    private static final double HALF_HEIGHT_SUPPORT_TOP_OFFSET = 0.5625;
    private static final boolean BLOCK_REQUIRE_SOLID_BELOW = true;
    private static final double GROUND_PLACEMENT_SEARCH_RADIUS = 2.0;
    private static final double GROUND_PLACEMENT_SEARCH_STEP = 0.5;
    private static final double GROUND_PLACEMENT_CLEARANCE_HALF_EXTENT = 0.75;
    private static final double GROUND_PLACEMENT_CLEARANCE_HEIGHT = 2.0;
    private static final double GROUND_PLACEMENT_FLOOR_EPSILON = 0.01;
    private static final double PLAYER_LOCK_EPSILON_SQ = 4.0E-4;
    private static final int IN_WALL_GRACE_TICKS = 5;
    private static final double WALL_INSET = 0.3;
    private static final double FENCE_INSET = 0.4;
    private static final int PLAYER_NUDGE_INTERVAL_TICKS = 4;
    private static final int PLAYER_NUDGE_MAX_MOBS_PER_PLAYER = 6;
    private static final double PLAYER_NUDGE_SCAN_RADIUS = 1.0;
    private static final double PLAYER_NUDGE_MIN_DISTANCE = 0.85;
    private static final double PLAYER_NUDGE_STRENGTH = 0.08;
    private static final double PLAYER_NUDGE_EPSILON = 1.0E-6;
    private static final int RECENT_NO_RESTORE_TRANSITION_TICKS = 20;
    private static final class_2960 BEDS_BLOCK_TAG_ID = class_2960.method_60655((String)"minecraft", (String)"beds");
    private static final Map<class_5321<class_1937>, Map<UUID, Long>> IN_WALL_GRACE_BY_WORLD = new HashMap<class_5321<class_1937>, Map<UUID, Long>>();
    private static final Map<class_5321<class_1937>, Map<UUID, RecentNoRestoreTransition>> RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD = new HashMap<class_5321<class_1937>, Map<UUID, RecentNoRestoreTransition>>();
    private static final String AFW_NOAI_TAG = "afw_noai";
    private static final String AFW_NOAI_ORIG_TAG = "afw_noai_orig";
    private static final String META_JOIN_REPLACE = "afw.join_replace";
    private static final int NOAI_CLEANUP_INTERVAL_TICKS = 6000;
    private static final int STARTUP_NOAI_CLEANUP_TICKS = 20;
    private static final int STARTUP_START_BLOCK_TICKS = 40;
    private static final List<GroundPlacementOffset> GROUND_PLACEMENT_OFFSETS = AfwServerAnimationController.createGroundPlacementOffsets();
    private static boolean initialized = false;
    private static int STARTUP_NOAI_CLEANUP_UNTIL_TICK = Integer.MIN_VALUE;
    private static int STARTUP_START_BLOCK_UNTIL_TICK = Integer.MIN_VALUE;
    private static final Map<UUID, ActiveInstance> ACTIVE_INSTANCES = new ConcurrentHashMap<UUID, ActiveInstance>();
    private static final Map<UUID, AnimationStartProtection> ANIMATION_START_PROTECTION = new ConcurrentHashMap<UUID, AnimationStartProtection>();
    private static final int PLAYER_QUEUE_CAP = 8;
    private static final String META_QUEUE_OWNER = "afw.queue_owner";
    private static final String META_QUEUE_CONTINUE = "afw.queue_continue";
    private static final String META_QUEUE_ENTRY_ID = "afw.queue_entry_id";
    private static final String META_QUEUE_TRANSITION = "afw.queue_transition";
    private static final Map<class_5321<class_1937>, Map<UUID, PlayerQueueState>> PLAYER_QUEUES_BY_WORLD = new HashMap<class_5321<class_1937>, Map<UUID, PlayerQueueState>>();
    private static final Map<class_5321<class_1937>, Map<UUID, UUID>> QUEUED_MOB_TO_PLAYER_BY_WORLD = new HashMap<class_5321<class_1937>, Map<UUID, UUID>>();
    private static final Map<class_5321<class_1937>, Map<UUID, DeferredQueueContext>> DEFERRED_QUEUE_CONTEXT_BY_WORLD = new HashMap<class_5321<class_1937>, Map<UUID, DeferredQueueContext>>();
    private static final Map<class_5321<class_1937>, Map<UUID, AiDisableState>> AI_DISABLE_STATE_BY_WORLD = new HashMap<class_5321<class_1937>, Map<UUID, AiDisableState>>();
    private static final Map<class_5321<class_1937>, Integer> NOAI_CLEANUP_TICKS_BY_WORLD = new HashMap<class_5321<class_1937>, Integer>();
    private static final Map<class_5321<class_1937>, Map<UUID, PlayerLockState>> PLAYER_LOCK_STATE_BY_WORLD = new HashMap<class_5321<class_1937>, Map<UUID, PlayerLockState>>();
    private static final int MAX_FLOOR_SNAP_DROP = 2;

    private AfwServerAnimationController() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (AfwAnimationDefinitions.isEmpty()) {
                AfwAnimationDefinitions.reloadFromServerResourceManager(server.method_34864(), "server started with empty definition cache");
            }
            AI_DISABLE_STATE_BY_WORLD.clear();
            NOAI_CLEANUP_TICKS_BY_WORLD.clear();
            STARTUP_NOAI_CLEANUP_UNTIL_TICK = server.method_3780() + 20;
            STARTUP_START_BLOCK_UNTIL_TICK = server.method_3780() + 40;
            PLAYER_LOCK_STATE_BY_WORLD.clear();
            PLAYER_QUEUES_BY_WORLD.clear();
            QUEUED_MOB_TO_PLAYER_BY_WORLD.clear();
            DEFERRED_QUEUE_CONTEXT_BY_WORLD.clear();
            ANIMATION_START_PROTECTION.clear();
            RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.clear();
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> server.execute(() -> {
            class_3222 player = handler.field_14140;
            if (player == null) {
                return;
            }
            class_3218 patt0$temp = player.method_51469();
            if (!(patt0$temp instanceof class_3218)) {
                return;
            }
            class_3218 world = patt0$temp;
            AfwServerAnimationController.stopActiveInstancesForDisconnectingPlayer(world, player.method_5667());
            AfwServerAnimationController.releasePlayerLocks(world, List.of(player.method_5667()));
            AfwServerAnimationController.clearPlayerQueue(world, player);
            AfwServerAnimationController.clearDeferredQueueOriginal(world, player.method_5667());
            ANIMATION_START_PROTECTION.remove(player.method_5667());
            AfwServerAnimationController.removePlayerFromInstanceSubscribers(player.method_5667());
        }));
        ServerTickEvents.END_WORLD_TICK.register(AfwServerAnimationController::tickWorld);
    }

    @Nullable
    public static UUID startNow(class_3218 world, @Nullable class_3222 requester, class_2960 animationId, List<class_1297> actorsSorted, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable UUID positionAnchorUuid, @Nullable Map<String, String> metadata, boolean forceChat) {
        boolean debugDirectMultiPlayerRequest;
        if (world == null || animationId == null || actorsSorted == null || actorsSorted.isEmpty()) {
            return null;
        }
        if (AfwServerAnimationController.isWithinStartupStartBlockWindow(world)) {
            return null;
        }
        if (!AfwAnimationDefinitions.isAnimationEnabled(animationId)) {
            return null;
        }
        ArrayList<class_1297> actors = new ArrayList<class_1297>(actorsSorted.size());
        actors.addAll(actorsSorted);
        actors.sort(Comparator.comparingInt(class_1297::method_5628));
        int players = 0;
        ArrayList<UUID> actorUuids = new ArrayList<UUID>(actors.size());
        for (class_1297 e : actors) {
            if (e == null || e.method_73183() != world) {
                return null;
            }
            if (e instanceof class_1657) {
                ++players;
            } else if (!(e instanceof class_1308)) {
                AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("matched_unpathable_actor", animationId), forceChat);
                return null;
            }
            actorUuids.add(e.method_5667());
        }
        boolean bl = debugDirectMultiPlayerRequest = players > 1 && requester != null && forceChat;
        if (players > 1 && !debugDirectMultiPlayerRequest) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.SETUP, AfwServerAnimationController.dbg("matched_multiple_players_not_supported", animationId), forceChat);
            return null;
        }
        if (debugDirectMultiPlayerRequest && !AfwServerAnimationController.allSelectedPlayersNearRequester(actors, requester, 5.0)) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.SETUP, AfwServerAnimationController.dbg("start_cancelled_players_too_far_from_requester", String.format(Locale.ROOT, "%.1f", 5.0)), forceChat);
            return null;
        }
        if (AfwServerAnimationController.shouldBlockStart(world, actorUuids)) {
            return null;
        }
        List<String> safeActorKeys = actorKeys != null && actorKeys.size() == actorUuids.size() ? List.copyOf(actorKeys) : List.of();
        List<AnimationStageInfo> safeStages = stages == null ? List.of() : List.copyOf(stages);
        AfwDamageBehavior safeBehavior = damageBehavior == null ? AfwDamageBehavior.IGNORE_DAMAGE : damageBehavior;
        return AfwServerAnimationController.startAndBroadcast(world, requester, animationId, actorUuids, safeActorKeys, safeStages, safeBehavior, ignoreAttackers, AfwServerAnimationController.sanitizeMetadata(metadata), forceChat, positionAnchorUuid, null, null);
    }

    @Nullable
    public static MatchedStartRequest startEligibleMatchedNow(class_3218 world, @Nullable class_3222 requester, List<? extends class_1297> actorsSorted, @Nullable Set<String> requiredAnimationTags, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable UUID positionAnchorUuid, @Nullable Map<String, String> metadata, boolean forceChat) {
        if (world == null || actorsSorted == null || actorsSorted.isEmpty()) {
            return null;
        }
        ArrayList<class_1297> actors = new ArrayList<class_1297>(actorsSorted.size());
        actors.addAll(actorsSorted);
        actors.sort(Comparator.comparingInt(class_1297::method_5628));
        EligibleDefinition eligible = AfwServerAnimationController.selectEligibleDefinition(world, actors, requiredAnimationTags == null ? Set.of() : requiredAnimationTags, positionAnchorUuid);
        if (eligible == null) {
            return null;
        }
        UUID instanceId = AfwServerAnimationController.startNow(world, requester, eligible.definition().id(), actors, eligible.actorKeys(), eligible.definition().stages(), damageBehavior, ignoreAttackers, positionAnchorUuid, AfwServerAnimationController.sanitizeMetadata(metadata), forceChat);
        return new MatchedStartRequest(instanceId, eligible.definition().id(), eligible.actorKeys(), eligible.definition().stages());
    }

    public static boolean isDefinitionStartEligible(class_3218 world, @Nullable AfwAnimationDefinitions.Definition definition, List<? extends class_1297> actorsSorted, @Nullable UUID positionAnchorUuid) {
        if (world == null || definition == null || actorsSorted == null || actorsSorted.isEmpty()) {
            return false;
        }
        ArrayList<class_1297> actors = new ArrayList<class_1297>(actorsSorted.size());
        actors.addAll(actorsSorted);
        actors.sort(Comparator.comparingInt(class_1297::method_5628));
        for (class_1297 actor : actors) {
            if (actor != null && actor.method_73183() == world) continue;
            return false;
        }
        List<UUID> actorUuids = actors.stream().map(class_1297::method_5667).toList();
        List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(definition, actors, world.method_8409());
        UUID effectiveAnchorUuid = AfwServerAnimationController.resolveEffectivePositionAnchorUuid(world, definition.id(), actorUuids, actorKeys, positionAnchorUuid);
        return AfwServerAnimationController.checkStartRequirements(world, definition, actorUuids, effectiveAnchorUuid).valid();
    }

    public static void setAnimationStartProtection(@Nullable class_3222 player, int ticks) {
        if (player == null) {
            return;
        }
        class_3218 class_32182 = player.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            return;
        }
        class_3218 world = class_32182;
        if (ticks <= 0) {
            ANIMATION_START_PROTECTION.remove(player.method_5667());
            return;
        }
        long until = world.method_75260() + (long)ticks;
        ANIMATION_START_PROTECTION.put(player.method_5667(), new AnimationStartProtection((class_5321<class_1937>)world.method_27983(), until));
    }

    public static void clearAnimationStartProtection(@Nullable class_3222 player) {
        if (player == null) {
            return;
        }
        ANIMATION_START_PROTECTION.remove(player.method_5667());
    }

    public static boolean hasAnimationStartProtection(@Nullable class_3218 world, @Nullable class_3222 player) {
        if (world == null || player == null) {
            return false;
        }
        AnimationStartProtection protection = ANIMATION_START_PROTECTION.get(player.method_5667());
        if (protection == null) {
            return false;
        }
        if (!protection.worldKey.equals((Object)world.method_27983())) {
            ANIMATION_START_PROTECTION.remove(player.method_5667());
            return false;
        }
        if (world.method_75260() >= protection.untilTick) {
            ANIMATION_START_PROTECTION.remove(player.method_5667());
            return false;
        }
        return true;
    }

    public static boolean enqueueForPlayer(class_3218 world, @Nullable class_3222 requester, class_2960 animationId, List<? extends class_1297> actors, int insertIndex, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable Map<String, String> metadata, boolean forceChat) {
        if (world == null || animationId == null || actors == null || actors.isEmpty()) {
            return false;
        }
        UUID playerUuid = AfwServerAnimationController.inferSinglePlayerUuid(world, actors);
        if (playerUuid == null) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_enqueue_failed_exactly_one_player", new Object[0]), forceChat);
            return false;
        }
        PlayerQueueState queue = PLAYER_QUEUES_BY_WORLD.computeIfAbsent((class_5321<class_1937>)world.method_27983(), k -> new HashMap()).computeIfAbsent(playerUuid, PlayerQueueState::new);
        if (queue.entries.size() >= 8) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_enqueue_failed_full", 8), forceChat);
            return false;
        }
        ArrayList<UUID> actorUuids = new ArrayList<UUID>();
        ArrayList<UUID> nonPlayers = new ArrayList<UUID>();
        for (class_1297 class_12972 : actors) {
            if (class_12972 == null || class_12972.method_73183() != world) continue;
            UUID uuid = class_12972.method_5667();
            if (class_12972 instanceof class_3222) {
                if (!uuid.equals(playerUuid)) {
                    continue;
                }
            } else {
                nonPlayers.add(uuid);
            }
            actorUuids.add(uuid);
        }
        if (!actorUuids.contains(playerUuid)) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_enqueue_failed_player_missing", new Object[0]), forceChat);
            return false;
        }
        Map mobOwner = QUEUED_MOB_TO_PLAYER_BY_WORLD.computeIfAbsent((class_5321<class_1937>)world.method_27983(), k -> new HashMap());
        for (UUID mobUuid : nonPlayers) {
            UUID existingOwner = (UUID)mobOwner.get(mobUuid);
            if (existingOwner == null) continue;
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_enqueue_denied_mob_already_queued", AfwServerAnimationController.describeActorForDebug(world, mobUuid)), forceChat);
            return false;
        }
        UUID uUID = UUID.randomUUID();
        LinkedHashMap<String, String> safeMetadata = new LinkedHashMap<String, String>(AfwServerAnimationController.sanitizeMetadata(metadata));
        safeMetadata.put(META_QUEUE_OWNER, playerUuid.toString());
        safeMetadata.put(META_QUEUE_ENTRY_ID, uUID.toString());
        QueuedEntry entry = new QueuedEntry(uUID, animationId, List.copyOf(actorUuids), damageBehavior == null ? AfwDamageBehavior.IGNORE_DAMAGE : damageBehavior, ignoreAttackers, Map.copyOf(safeMetadata));
        AfwServerAnimationController.insertQueueEntry(queue.entries, entry, insertIndex);
        AfwServerAnimationController.captureDeferredQueueContextFromActiveInstance(world, playerUuid);
        for (UUID mobUuid : nonPlayers) {
            mobOwner.put(mobUuid, playerUuid);
        }
        AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queued_at_position", animationId, AfwServerAnimationController.describeQueuePosition(queue.entries, uUID)), forceChat);
        return true;
    }

    public static void clearPlayerQueue(class_3218 world, @Nullable class_3222 player) {
        if (world == null || player == null) {
            return;
        }
        AfwServerAnimationController.clearPlayerQueueByUuid(world, player.method_5667(), AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_cleared_for_player", new Object[0]));
    }

    public static void clearQueueForInstance(class_3218 world, UUID instanceId) {
        if (world == null || instanceId == null) {
            return;
        }
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world || inst.playerUuid == null) {
            return;
        }
        AfwServerAnimationController.clearPlayerQueueByUuid(world, inst.playerUuid, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_cleared_for_active_instance", new Object[0]));
    }

    private static void stopActiveInstancesForDisconnectingPlayer(class_3218 world, UUID playerUuid) {
        if (world == null || playerUuid == null || ACTIVE_INSTANCES.isEmpty()) {
            return;
        }
        ArrayList<UUID> instanceIds = new ArrayList<UUID>();
        for (Map.Entry<UUID, ActiveInstance> entry : ACTIVE_INSTANCES.entrySet()) {
            ActiveInstance inst = entry.getValue();
            if (inst.world != world || !inst.actorUuidSet.contains(playerUuid)) continue;
            instanceIds.add(entry.getKey());
        }
        for (UUID instanceId : instanceIds) {
            AfwServerAnimationController.stopInstance(world, instanceId, true, StopReason.EXTERNAL_STOP);
        }
    }

    @Nullable
    private static EligibleDefinition selectEligibleDefinition(class_3218 world, List<class_1297> actorsSorted, Set<String> requiredAnimationTags, @Nullable UUID positionAnchorUuid) {
        if (world == null || actorsSorted == null || actorsSorted.isEmpty()) {
            return null;
        }
        AfwAnimationDefinitions.MatchResult match = AfwAnimationDefinitions.match(actorsSorted, requiredAnimationTags);
        List<AfwAnimationDefinitions.Definition> candidates = match.candidatesSorted();
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        int bestSpecificity = candidates.getFirst().specificity();
        ArrayList<EligibleDefinition> eligible = new ArrayList<EligibleDefinition>();
        List<UUID> actorUuids = actorsSorted.stream().map(class_1297::method_5667).toList();
        for (AfwAnimationDefinitions.Definition definition : candidates) {
            if (definition.specificity() != bestSpecificity) break;
            List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(definition, actorsSorted, world.method_8409());
            UUID effectiveAnchorUuid = AfwServerAnimationController.resolveEffectivePositionAnchorUuid(world, definition.id(), actorUuids, actorKeys, positionAnchorUuid);
            RequirementCheck check = AfwServerAnimationController.checkStartRequirements(world, definition, actorUuids, effectiveAnchorUuid);
            if (!check.valid()) continue;
            eligible.add(new EligibleDefinition(definition, actorKeys, check.blockPlacement()));
        }
        if (eligible.isEmpty()) {
            return null;
        }
        return AfwServerAnimationController.pickWeightedEligible(eligible, world.method_8409());
    }

    private static RequirementCheck checkStartRequirements(class_3218 world, @Nullable AfwAnimationDefinitions.Definition definition, List<UUID> actorUuids, @Nullable UUID effectiveAnchorUuid) {
        if (definition == null) {
            return new RequirementCheck(false, null);
        }
        AfwAnimationDefinitions.BlockRequirements blockRequirements = definition.blockRequirements();
        AfwAnimationDefinitions.WaterRequirement waterRequirement = definition.waterRequirement();
        if (blockRequirements != null && waterRequirement != AfwAnimationDefinitions.WaterRequirement.NONE) {
            return new RequirementCheck(false, null);
        }
        if (blockRequirements != null) {
            class_1297 anchor = AfwServerAnimationController.pickAnchorEntity(world, actorUuids, effectiveAnchorUuid);
            BlockPlacement placement = anchor == null ? null : AfwServerAnimationController.findBlockPlacement(world, anchor, blockRequirements);
            return new RequirementCheck(placement != null, placement);
        }
        return new RequirementCheck(AfwServerAnimationController.waterRequirementMet(world, definition, actorUuids), null);
    }

    private static EligibleDefinition pickWeightedEligible(List<EligibleDefinition> definitions, class_5819 random) {
        if (definitions == null || definitions.isEmpty()) {
            return null;
        }
        double totalWeight = 0.0;
        for (EligibleDefinition eligible : definitions) {
            double weight;
            if (eligible == null || eligible.definition() == null || !((weight = AfwServerAnimationController.sanitizeWeight(eligible.definition().weight())) > 0.0)) continue;
            totalWeight += weight;
        }
        if (!(totalWeight > 0.0) || !Double.isFinite(totalWeight)) {
            return definitions.get(random.method_43048(definitions.size()));
        }
        double roll = random.method_43058() * totalWeight;
        double running = 0.0;
        for (EligibleDefinition eligible : definitions) {
            if (eligible == null || eligible.definition() == null || !(roll < (running += AfwServerAnimationController.sanitizeWeight(eligible.definition().weight())))) continue;
            return eligible;
        }
        return definitions.getLast();
    }

    private static double sanitizeWeight(double raw) {
        if (!Double.isFinite(raw) || raw <= 0.0) {
            return 1.0;
        }
        return raw;
    }

    public static boolean advanceStage(class_3218 world, UUID instanceId) {
        int currentIndex;
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return false;
        }
        int n = currentIndex = inst.stageTracker == null ? 0 : inst.stageTracker.stageIndex;
        if (inst.stages == null || inst.stages.isEmpty()) {
            return false;
        }
        if (currentIndex + 1 >= inst.stages.size()) {
            return AfwServerAnimationController.stopInstance(world, instanceId, true, StopReason.NATURAL_END);
        }
        int targetIndex = currentIndex + 1;
        return AfwServerAnimationController.advanceToStageIndex(world, instanceId, inst, targetIndex);
    }

    public static boolean advanceToStage(class_3218 world, UUID instanceId, int stageNumber) {
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return false;
        }
        if (stageNumber <= 0) {
            return false;
        }
        return AfwServerAnimationController.advanceToStageIndex(world, instanceId, inst, stageNumber - 1);
    }

    private static boolean advanceToStageIndex(class_3218 world, UUID instanceId, ActiveInstance inst, int targetIndex) {
        if (inst == null || inst.world != world || inst.stages == null || inst.stages.isEmpty()) {
            return false;
        }
        if (targetIndex < 0 || targetIndex >= inst.stages.size()) {
            return false;
        }
        long advanceTick = world.method_75260();
        if (inst.stageTracker != null) {
            inst.stageTracker.stageIndex = targetIndex;
            inst.stageTracker.stageStartTick = advanceTick;
        }
        inst.speed = AfwServerAnimationController.clampSpeed(AfwServerAnimationController.resolveStageSpeed(inst.stages, inst.stageTracker == null ? 0 : inst.stageTracker.stageIndex));
        AdvanceAnimationStageS2CPayload payload = new AdvanceAnimationStageS2CPayload(instanceId, advanceTick, targetIndex);
        Set<class_3222> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, null, inst);
        AfwServerAnimationController.rememberInstanceSubscribers(inst, targets);
        for (class_3222 p : targets) {
            ServerPlayNetworking.send((class_3222)p, (class_8710)payload);
            ServerPlayNetworking.send((class_3222)p, (class_8710)new AnimationSpeedUpdateS2CPayload(instanceId, inst.speed));
        }
        ((AfwAnimationEvents.StageAdvance)AfwAnimationEvents.STAGE_ADVANCE.invoker()).onStageAdvance(world, instanceId, inst.animationId, List.copyOf(inst.actorUuids), AfwServerAnimationController.sanitizeActorKeys(inst.actorKeys, inst.actorUuids.size()), advanceTick);
        return true;
    }

    public static boolean stopInstance(class_3218 world, UUID instanceId) {
        return AfwServerAnimationController.stopInstance(world, instanceId, true, StopReason.EXTERNAL_STOP);
    }

    public static void adjustSpeed(class_3218 world, UUID instanceId, double multiplier, @Nullable class_3222 requester) {
        AfwServerAnimationController.adjustSpeed(world, instanceId, multiplier, requester, requester != null);
    }

    public static void adjustSpeedSilently(class_3218 world, UUID instanceId, double multiplier) {
        AfwServerAnimationController.adjustSpeed(world, instanceId, multiplier, null, false);
    }

    private static void adjustSpeed(class_3218 world, UUID instanceId, double multiplier, @Nullable class_3222 requester, boolean notify) {
        double oldSpeed;
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            if (notify) {
                AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.ALWAYS, AfwServerAnimationController.dbg("no_active_instance_to_adjust", new Object[0]), true);
            }
            return;
        }
        double clampedMultiplier = AfwServerAnimationController.sanitizeMultiplier(multiplier);
        double newSpeed = AfwServerAnimationController.clampSpeed(inst.speed * clampedMultiplier);
        if (Math.abs(newSpeed - (oldSpeed = inst.speed)) < 1.0E-4) {
            if (notify) {
                AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.ALWAYS, AfwServerAnimationController.dbg("speed_unchanged", new Object[0]), true);
            }
            return;
        }
        long now = world.method_75260();
        AnimationStageInfo currentStage = AfwServerAnimationController.resolveCurrentStage(inst);
        long duration = Math.max(0L, currentStage.lengthTicks());
        if (duration > 0L && inst.stageTracker != null) {
            long elapsed = Math.max(0L, now - inst.stageTracker.stageStartTick);
            double normalizedProgress = (double)elapsed * oldSpeed;
            long adjustedStart = now - Math.round(normalizedProgress / newSpeed);
            inst.stageTracker.stageStartTick = Math.max(inst.startTick, adjustedStart);
        }
        inst.speed = newSpeed;
        AnimationSpeedUpdateS2CPayload payload = new AnimationSpeedUpdateS2CPayload(instanceId, newSpeed);
        Set<class_3222> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, requester, inst);
        AfwServerAnimationController.rememberInstanceSubscribers(inst, targets);
        for (class_3222 p : targets) {
            ServerPlayNetworking.send((class_3222)p, (class_8710)payload);
        }
        if (notify) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.ALWAYS, AfwServerAnimationController.dbg("speed_set", String.format(Locale.ROOT, "%.2fx", newSpeed)), true);
        }
    }

    public static boolean stopInstance(class_3218 world, UUID instanceId, boolean restoreTransforms) {
        return AfwServerAnimationController.stopInstance(world, instanceId, restoreTransforms, StopReason.EXTERNAL_STOP);
    }

    public static boolean stopInstance(class_3218 world, UUID instanceId, boolean restoreTransforms, StopReason reason) {
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return false;
        }
        long stopTick = world.method_75260() + 1L;
        boolean chained = false;
        if (reason == StopReason.NATURAL_END && inst.playerUuid != null) {
            chained = AfwServerAnimationController.startNextQueuedForPlayerDeferringRestore(world, inst, instanceId);
        }
        StopAnimationS2CPayload payload = new StopAnimationS2CPayload(instanceId, stopTick);
        Set<class_3222> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, null, inst);
        for (class_3222 p : targets) {
            ServerPlayNetworking.send((class_3222)p, (class_8710)payload);
        }
        AfwServerAnimationController.fireStopEvent(instanceId, inst);
        if (restoreTransforms) {
            AfwServerAnimationController.restoreTransforms(inst, chained && inst.playerUuid != null ? Set.of(inst.playerUuid) : null);
        } else {
            AfwServerAnimationController.cacheNoRestoreTransition(world, inst);
        }
        AfwServerAnimationController.restoreAiForActors(inst.world, inst.actorUuids);
        AfwServerAnimationController.releasePlayerLocks(inst.world, inst.actorUuids);
        AfwServerAnimationController.processPendingAiRestores(inst.world);
        ACTIVE_INSTANCES.remove(instanceId);
        if (reason == StopReason.NATURAL_END) {
            if (chained) {
                AfwServerAnimationController.sendOrLogDebug(world, null, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_continued_for_player", new Object[0]), false);
            }
        } else {
            AfwServerAnimationController.handleQueueOnStop(world, inst, reason);
        }
        return true;
    }

    public static boolean handleActorDamage(class_3218 world, class_1309 actor, class_1282 source) {
        UUID actorUuid = actor.method_5667();
        long now = world.method_75260();
        boolean blockDamage = false;
        ArrayList<UUID> stopInstances = null;
        if (source != null && source.method_49708(class_8111.field_42340)) {
            Object until;
            Map<UUID, Long> grace = IN_WALL_GRACE_BY_WORLD.get(world.method_27983());
            if (grace != null && (until = grace.get(actorUuid)) != null) {
                if (now <= (Long)until) {
                    return false;
                }
                grace.remove(actorUuid);
            }
            if (ACTIVE_INSTANCES.isEmpty()) {
                return true;
            }
            for (ActiveInstance inst : ACTIVE_INSTANCES.values()) {
                if (inst.world != world || now < inst.startTick || !inst.actorUuidSet.contains(actorUuid)) continue;
                return false;
            }
        }
        if (source != null && source.method_49708(class_8111.field_42342) && AfwServerAnimationController.isUnderwaterBreathingProtected(world, actorUuid)) {
            return false;
        }
        if (ACTIVE_INSTANCES.isEmpty()) {
            return true;
        }
        block5: for (Map.Entry<UUID, ActiveInstance> entry : List.copyOf(ACTIVE_INSTANCES.entrySet())) {
            ActiveInstance inst;
            inst = entry.getValue();
            if (inst.world != world || now < inst.startTick || !inst.actorUuidSet.contains(actorUuid)) continue;
            if (inst.ignoreAttackers && source != null && source.method_5529() instanceof class_1308) {
                blockDamage = true;
                break;
            }
            AfwDamageBehavior behavior = inst.damageBehavior;
            switch (behavior) {
                case BLOCK_DAMAGE: {
                    blockDamage = true;
                    break block5;
                }
                case STOP_ON_DAMAGE: {
                    if (stopInstances == null) {
                        stopInstances = new ArrayList<UUID>();
                    }
                    stopInstances.add(entry.getKey());
                }
                default: {
                    continue block5;
                }
            }
        }
        if (blockDamage) {
            return false;
        }
        if (stopInstances != null) {
            for (UUID id : stopInstances) {
                AfwServerAnimationController.stopInstance(world, id, true, StopReason.DAMAGE_STOP);
            }
        }
        return true;
    }

    public static boolean shouldIgnoreAttackers(class_3218 world, class_1309 actor) {
        if (ACTIVE_INSTANCES.isEmpty()) {
            return false;
        }
        UUID actorUuid = actor.method_5667();
        for (ActiveInstance inst : ACTIVE_INSTANCES.values()) {
            if (inst.world != world || !inst.ignoreAttackers || !inst.actorUuidSet.contains(actorUuid)) continue;
            return true;
        }
        return false;
    }

    private static boolean isUnderwaterBreathingProtected(class_3218 world, UUID actorUuid) {
        if (world == null || actorUuid == null || ACTIVE_INSTANCES.isEmpty()) {
            return false;
        }
        for (ActiveInstance inst : ACTIVE_INSTANCES.values()) {
            if (inst.world != world || !inst.underwaterBreathing || !inst.actorUuidSet.contains(actorUuid)) continue;
            return true;
        }
        return false;
    }

    private static ActiveInstance findActiveInstanceForActors(class_3218 world, List<UUID> actorUuids) {
        if (actorUuids == null || actorUuids.isEmpty()) {
            return null;
        }
        if (ACTIVE_INSTANCES.isEmpty()) {
            return null;
        }
        for (ActiveInstance inst : ACTIVE_INSTANCES.values()) {
            if (inst.world != world) continue;
            for (UUID uuid : actorUuids) {
                if (!inst.actorUuidSet.contains(uuid)) continue;
                return inst;
            }
        }
        return null;
    }

    public static void stopInstanceAndBroadcast(class_3222 requester, UUID instanceId) {
        class_3218 class_32182 = requester.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            return;
        }
        class_3218 requesterWorld = class_32182;
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null) {
            AfwServerAnimationController.sendOrLogDebug(requesterWorld, requester, AfwDebugChatCategory.ALWAYS, AfwServerAnimationController.dbg("no_such_active_instance", new Object[0]), true);
            return;
        }
        if (inst.world != requesterWorld) {
            AfwServerAnimationController.sendOrLogDebug(requesterWorld, requester, AfwDebugChatCategory.ALWAYS, AfwServerAnimationController.dbg("instance_in_different_world", new Object[0]), true);
            return;
        }
        long now = requesterWorld.method_75260();
        long stopTick = now + 1L;
        StopAnimationS2CPayload payload = new StopAnimationS2CPayload(instanceId, stopTick);
        Set<class_3222> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(requesterWorld, requester, inst);
        for (class_3222 p : targets) {
            ServerPlayNetworking.send((class_3222)p, (class_8710)payload);
        }
        boolean chained = false;
        if (inst.playerUuid != null) {
            chained = AfwServerAnimationController.startNextQueuedForPlayer(requesterWorld, inst.playerUuid, instanceId, AfwServerAnimationController.resolveTransitionYaw(inst));
        }
        AfwServerAnimationController.fireStopEvent(instanceId, inst);
        AfwServerAnimationController.restoreTransforms(inst);
        AfwServerAnimationController.restoreAiForActors(inst.world, inst.actorUuids);
        AfwServerAnimationController.releasePlayerLocks(inst.world, inst.actorUuids);
        AfwServerAnimationController.processPendingAiRestores(inst.world);
        ACTIVE_INSTANCES.remove(instanceId);
        AfwServerAnimationController.sendOrLogDebug(requesterWorld, requester, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("broadcasting_stop", new Object[0]), false);
        if (chained) {
            AfwServerAnimationController.sendOrLogDebug(requesterWorld, requester, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_continued_for_player", new Object[0]), false);
        }
        AnimationFramework.LOGGER.info("Broadcast STOP: instance={} stopTick={} actors={} requester={}", new Object[]{instanceId, stopTick, inst.actorUuids.size(), requester.method_5477().getString()});
    }

    public static void clearAllInstancesInWorld(class_3218 world) {
        Map<UUID, Long> grace;
        ArrayList<UUID> toRemove = new ArrayList<UUID>();
        for (Map.Entry<UUID, ActiveInstance> entry : ACTIVE_INSTANCES.entrySet()) {
            ActiveInstance inst = entry.getValue();
            if (inst.world != world) continue;
            AfwServerAnimationController.fireStopEvent(entry.getKey(), inst);
            AfwServerAnimationController.restoreTransforms(inst);
            AfwServerAnimationController.restoreAiForActors(world, inst.actorUuids);
            AfwServerAnimationController.releasePlayerLocks(world, inst.actorUuids);
            toRemove.add(entry.getKey());
            AfwServerAnimationController.handleQueueOnStop(world, inst, StopReason.STOP_ALL);
        }
        for (UUID id : toRemove) {
            ACTIVE_INSTANCES.remove(id);
        }
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.remove(world.method_27983());
        if (queues != null) {
            Map<UUID, UUID> byMob = QUEUED_MOB_TO_PLAYER_BY_WORLD.get(world.method_27983());
            if (byMob != null) {
                byMob.clear();
            }
            DEFERRED_QUEUE_CONTEXT_BY_WORLD.remove(world.method_27983());
            for (UUID playerUuid : queues.keySet()) {
                class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(playerUuid);
                AfwServerAnimationController.sendOrLogDebug(world, player, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_cleared", new Object[0]), false);
            }
        }
        if ((grace = IN_WALL_GRACE_BY_WORLD.get(world.method_27983())) != null) {
            grace.clear();
        }
        AfwServerAnimationController.processPendingAiRestores(world);
        AfwServerAnimationController.clearAllPlayerLocksInWorld(world);
        RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.remove(world.method_27983());
    }

    private static void tickWorld(class_3218 world) {
        AfwServerAnimationController.cleanupInWallGrace(world, world.method_75260());
        AfwServerAnimationController.cleanupNoRestoreTransitions(world, world.method_75260());
        AfwServerAnimationController.tickActiveInstances(world);
        AfwServerAnimationController.tickNoAiCleanup(world);
        AfwServerAnimationController.tickPlayerQueues(world);
    }

    private static void cacheNoRestoreTransition(class_3218 world, ActiveInstance inst) {
        if (world == null || inst == null || inst.sharedTransform == null || inst.actorUuidSet == null || inst.actorUuidSet.isEmpty()) {
            return;
        }
        long expiresTick = world.method_75260() + 20L;
        RecentNoRestoreTransition transition = new RecentNoRestoreTransition(expiresTick, inst.sharedTransform, inst.blockPlacement);
        Map byActor = RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.computeIfAbsent((class_5321<class_1937>)world.method_27983(), ignored -> new HashMap());
        for (UUID actorUuid : inst.actorUuidSet) {
            if (actorUuid == null) continue;
            byActor.put(actorUuid, transition);
        }
    }

    @Nullable
    private static RecentNoRestoreTransition consumeNoRestoreTransition(class_3218 world, List<UUID> actorUuids, Map<String, String> metadata) {
        if (world == null || actorUuids == null || actorUuids.isEmpty() || metadata == null) {
            return null;
        }
        if (!"true".equals(metadata.get(META_JOIN_REPLACE))) {
            return null;
        }
        Map<UUID, RecentNoRestoreTransition> byActor = RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.get(world.method_27983());
        if (byActor == null || byActor.isEmpty()) {
            return null;
        }
        long now = world.method_75260();
        RecentNoRestoreTransition selected = null;
        for (UUID actorUuid : actorUuids) {
            RecentNoRestoreTransition candidate = byActor.get(actorUuid);
            if (candidate == null) continue;
            if (candidate.expiresTick() < now) {
                byActor.remove(actorUuid);
                continue;
            }
            selected = candidate;
            break;
        }
        for (UUID actorUuid : actorUuids) {
            byActor.remove(actorUuid);
        }
        if (byActor.isEmpty()) {
            RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.remove(world.method_27983());
        }
        return selected;
    }

    private static void cleanupNoRestoreTransitions(class_3218 world, long now) {
        Map<UUID, RecentNoRestoreTransition> byActor = RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.get(world.method_27983());
        if (byActor == null || byActor.isEmpty()) {
            return;
        }
        byActor.entrySet().removeIf(entry -> entry.getValue() == null || ((RecentNoRestoreTransition)entry.getValue()).expiresTick() < now);
        if (byActor.isEmpty()) {
            RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.remove(world.method_27983());
        }
    }

    private static boolean shouldBlockStart(class_3218 world, List<UUID> actorUuids) {
        if (actorUuids == null || actorUuids.isEmpty()) {
            return false;
        }
        for (UUID uuid : actorUuids) {
            class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(uuid);
            if (player == null || player.method_51469() != world || !AfwServerAnimationController.hasAnimationStartProtection(world, player)) continue;
            return true;
        }
        return false;
    }

    private static class_2561 dbg(String key, Object ... args) {
        return class_2561.method_43469((String)("debug.animationframework." + key), (Object[])AfwServerAnimationController.sanitizeTranslatableArgs(args));
    }

    private static Object[] sanitizeTranslatableArgs(Object ... args) {
        if (args == null || args.length == 0) {
            return new Object[0];
        }
        Object[] safe = new Object[args.length];
        for (int i = 0; i < args.length; ++i) {
            Object arg = args[i];
            if (arg instanceof class_2960) {
                class_2960 id = (class_2960)arg;
                safe[i] = id.method_12832();
                continue;
            }
            safe[i] = arg instanceof class_2561 || arg == null ? arg : String.valueOf(arg);
        }
        return safe;
    }

    private static void sendOrLogDebug(class_3218 world, @Nullable class_3222 requester, AfwDebugChatCategory category, String message, boolean forceChat) {
        AfwServerAnimationController.sendOrLogDebug(world, requester, category, (class_2561)class_2561.method_43470((String)message), forceChat);
    }

    private static void sendOrLogDebug(class_3218 world, @Nullable class_3222 requester, AfwDebugChatCategory category, class_2561 message, boolean forceChat) {
        String logMessage = message.getString();
        switch (category) {
            case ERROR: {
                AnimationFramework.LOGGER.error(logMessage);
                break;
            }
            case WARNING: {
                AnimationFramework.LOGGER.warn(logMessage);
                break;
            }
            default: {
                AnimationFramework.LOGGER.info(logMessage);
            }
        }
        if (requester != null) {
            if (AnimationFramework.shouldSendDebugChat(requester, category, forceChat)) {
                AnimationFramework.sendDebugChat(requester, category, message, forceChat);
            }
            return;
        }
        if (world == null) {
            return;
        }
        for (class_3222 player : world.method_18456()) {
            if (!AnimationFramework.shouldSendDebugChat(player, category, forceChat)) continue;
            AnimationFramework.sendDebugChat(player, category, message, forceChat);
        }
    }

    private static void tickActiveInstances(class_3218 world) {
        ActiveInstance inst;
        long now = world.method_75260();
        ArrayList<UUID> toRemove = new ArrayList<UUID>();
        HashSet<UUID> actorsToFreeze = new HashSet<UUID>();
        HashSet<UUID> activePlayerUuids = new HashSet<UUID>();
        HashMap<UUID, SharedTransform> playersToLock = new HashMap<UUID, SharedTransform>();
        HashSet<UUID> underwaterPlayers = new HashSet<UUID>();
        ArrayList<StageAdvance> stageAdvances = new ArrayList<StageAdvance>();
        for (Map.Entry<UUID, ActiveInstance> entry : ACTIVE_INSTANCES.entrySet()) {
            inst = entry.getValue();
            if (inst.world != world || AfwServerAnimationController.maybeAdvanceStages(world, entry.getKey(), inst, now, stageAdvances, toRemove)) continue;
            actorsToFreeze.addAll(inst.actorUuidSet);
            if (inst.playerUuid != null) {
                activePlayerUuids.add(inst.playerUuid);
            }
            if (inst.sharedTransform == null) continue;
            for (UUID uuid : inst.actorUuidSet) {
                class_1297 e = world.method_66347(uuid);
                if (!(e instanceof class_3222)) continue;
                playersToLock.put(uuid, inst.sharedTransform);
                if (!inst.underwaterBreathing) continue;
                underwaterPlayers.add(uuid);
            }
        }
        for (StageAdvance adv : stageAdvances) {
            inst = ACTIVE_INSTANCES.get(adv.instanceId());
            if (inst == null || inst.world != world) continue;
            AdvanceAnimationStageS2CPayload payload = new AdvanceAnimationStageS2CPayload(adv.instanceId(), adv.advanceTick(), adv.stageIndex());
            Set<class_3222> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, null, inst);
            AfwServerAnimationController.rememberInstanceSubscribers(inst, targets);
            for (class_3222 p : targets) {
                ServerPlayNetworking.send((class_3222)p, (class_8710)payload);
                ServerPlayNetworking.send((class_3222)p, (class_8710)new AnimationSpeedUpdateS2CPayload(adv.instanceId(), inst.speed));
            }
            ((AfwAnimationEvents.StageAdvance)AfwAnimationEvents.STAGE_ADVANCE.invoker()).onStageAdvance(world, adv.instanceId(), inst.animationId, List.copyOf(inst.actorUuids), AfwServerAnimationController.sanitizeActorKeys(inst.actorKeys, inst.actorUuids.size()), adv.advanceTick());
        }
        AfwServerAnimationController.freezeActiveActors(world, actorsToFreeze);
        AfwServerAnimationController.lockActivePlayers(world, playersToLock, underwaterPlayers);
        AfwServerAnimationController.nudgeNonActiveMobsAwayFromAnimatedPlayers(world, activePlayerUuids, now);
        AfwServerAnimationController.cleanupStalePlayerLocks(world, playersToLock.keySet());
        for (UUID id : toRemove) {
            ACTIVE_INSTANCES.remove(id);
        }
        AfwServerAnimationController.processPendingAiRestores(world);
    }

    @Nullable
    private static UUID startAndBroadcast(class_3218 world, @Nullable class_3222 requester, class_2960 animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata, boolean forceChat, @Nullable Float transitionYaw) {
        return AfwServerAnimationController.startAndBroadcast(world, requester, animationId, actorUuids, actorKeys, stages, damageBehavior, ignoreAttackers, metadata, forceChat, null, null, transitionYaw);
    }

    @Nullable
    private static UUID startAndBroadcast(class_3218 world, @Nullable class_3222 requester, class_2960 animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata, boolean forceChat, @Nullable UUID preferredAnchorUuid, @Nullable Float transitionYaw) {
        return AfwServerAnimationController.startAndBroadcast(world, requester, animationId, actorUuids, actorKeys, stages, damageBehavior, ignoreAttackers, metadata, forceChat, preferredAnchorUuid, null, transitionYaw);
    }

    @Nullable
    private static UUID startAndBroadcast(class_3218 world, @Nullable class_3222 requester, class_2960 animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata, boolean forceChat, @Nullable UUID preferredAnchorUuid, @Nullable BlockPlacement cachedBlockPlacement, @Nullable Float transitionYaw) {
        List<AnimationStageInfo> safeStages;
        AfwAnimationDefinitions.WaterRequirement waterRequirement;
        long startTick = world.method_75260() + 1L;
        UUID instanceId = UUID.randomUUID();
        Map<UUID, OriginalTransform> originalTransforms = AfwServerAnimationController.captureOriginalTransforms(world, actorUuids);
        Map<String, String> safeMetadata = AfwServerAnimationController.sanitizeMetadata(metadata);
        UUID effectivePositionAnchorUuid = AfwServerAnimationController.resolveEffectivePositionAnchorUuid(world, animationId, actorUuids, actorKeys, preferredAnchorUuid);
        RecentNoRestoreTransition joinTransition = AfwServerAnimationController.consumeNoRestoreTransition(world, actorUuids, safeMetadata);
        Float effectiveTransitionYaw = transitionYaw;
        if (effectiveTransitionYaw == null && joinTransition != null) {
            effectiveTransitionYaw = Float.valueOf(joinTransition.sharedTransform().yaw());
        }
        SharedTransform sharedTransform = joinTransition != null ? joinTransition.sharedTransform() : AfwServerAnimationController.pickSharedTransform(actorUuids, originalTransforms, effectivePositionAnchorUuid);
        AfwAnimationDefinitions.Definition def = AfwAnimationDefinitions.getDefinition(animationId);
        AfwAnimationDefinitions.BlockRequirements blockRequirements = def == null ? null : def.blockRequirements();
        AfwAnimationDefinitions.WaterRequirement waterRequirement2 = waterRequirement = def == null ? AfwAnimationDefinitions.WaterRequirement.NONE : def.waterRequirement();
        BlockPlacement placement = cachedBlockPlacement != null ? cachedBlockPlacement : (joinTransition == null ? null : joinTransition.blockPlacement());
        boolean lockOrientation = false;
        float lockedYaw = 0.0f;
        float lockedHeadYaw = 0.0f;
        float lockedPitch = 0.0f;
        if (blockRequirements != null && waterRequirement != AfwAnimationDefinitions.WaterRequirement.NONE) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.SETUP, AfwServerAnimationController.dbg("start_cancelled_block_and_water_requirement", new Object[0]), forceChat);
            return null;
        }
        if (blockRequirements != null) {
            class_1297 anchor = AfwServerAnimationController.pickAnchorEntity(world, actorUuids, effectivePositionAnchorUuid);
            if (placement == null) {
                BlockPlacement blockPlacement = placement = anchor == null ? null : AfwServerAnimationController.findBlockPlacement(world, anchor, blockRequirements);
            }
            if (placement == null) {
                AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.blockRequirementFailureMessage(blockRequirements, animationId, world, actorUuids), forceChat);
                return null;
            }
            sharedTransform = placement.sharedTransform();
            lockOrientation = true;
            lockedYaw = sharedTransform.yaw();
            lockedHeadYaw = sharedTransform.headYaw();
            lockedPitch = sharedTransform.pitch();
        } else if (waterRequirement != AfwAnimationDefinitions.WaterRequirement.NONE) {
            class_1297 waterAnchor = AfwServerAnimationController.findWaterAnchor(world, actorUuids, waterRequirement);
            if (waterAnchor == null) {
                AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("start_cancelled_water_requirement_not_met", new Object[0]), forceChat);
                return null;
            }
            SharedTransform base = sharedTransform;
            sharedTransform = new SharedTransform(waterAnchor.method_73189(), base.yaw(), base.pitch(), base.headYaw(), base.bodyYaw());
        } else {
            SharedTransform dismountTransform;
            if (AfwServerAnimationController.anyActorOnWaterFooting(world, actorUuids)) {
                AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("start_cancelled_not_allowed_in_water", new Object[0]), forceChat);
                return null;
            }
            UUID queuedPlayerUuid = AfwServerAnimationController.resolvePlayerUuid(world, actorUuids);
            SharedTransform groundTransform = AfwServerAnimationController.resolveQueuedPlacementTransform(world, queuedPlayerUuid, sharedTransform, safeMetadata);
            if (groundTransform == null) {
                groundTransform = AfwServerAnimationController.findSafeGroundPlacement(world, sharedTransform);
            }
            if (groundTransform == null && (dismountTransform = AfwServerAnimationController.findMountedPlayerDismountTransform(world, actorUuids, sharedTransform)) != null) {
                groundTransform = AfwServerAnimationController.findSafeGroundPlacement(world, dismountTransform);
            }
            if (groundTransform == null) {
                AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("start_cancelled_not_enough_space", AfwServerAnimationController.describeAnimationIdForDebug(animationId), AfwServerAnimationController.describeActorsForDebug(world, actorUuids)), forceChat);
                return null;
            }
            sharedTransform = groundTransform;
        }
        if (effectivePositionAnchorUuid != null && !lockOrientation) {
            lockOrientation = true;
            lockedYaw = sharedTransform.yaw();
            lockedHeadYaw = sharedTransform.headYaw();
            lockedPitch = sharedTransform.pitch();
        }
        boolean preservePlayerLook = false;
        if (effectiveTransitionYaw != null && blockRequirements == null) {
            float yaw = class_3532.method_15393((float)effectiveTransitionYaw.floatValue());
            sharedTransform = new SharedTransform(sharedTransform.pos(), yaw, 0.0f, yaw, yaw);
            lockOrientation = true;
            lockedYaw = yaw;
            lockedHeadYaw = yaw;
            lockedPitch = 0.0f;
            preservePlayerLook = true;
        }
        Set<UUID> actorUuidSet = Set.copyOf(new HashSet<UUID>(actorUuids));
        List<String> safeActorKeys = actorKeys != null && actorKeys.size() == actorUuids.size() ? List.copyOf(actorKeys) : List.of();
        List<Object> list = safeStages = stages == null ? List.of() : List.copyOf(stages);
        if (!((AfwAnimationEvents.AllowStart)AfwAnimationEvents.ALLOW_START.invoker()).allowStart(world, animationId, List.copyOf(actorUuids), safeActorKeys, safeStages, requester, damageBehavior, ignoreAttackers, safeMetadata)) {
            return null;
        }
        double initialSpeed = AfwServerAnimationController.clampSpeed(AfwServerAnimationController.resolveStageSpeed(stages, 0));
        class_243 cameraOrbitTarget = placement == null ? null : placement.cameraOrbitTarget();
        StartAnimationS2CPayload payload = new StartAnimationS2CPayload(animationId, instanceId, actorUuids, safeActorKeys, safeStages, startTick, initialSpeed, lockOrientation, lockedYaw, lockedHeadYaw, lockedPitch, cameraOrbitTarget);
        AfwServerAnimationController.disableAiForActors(world, actorUuids);
        if (placement != null) {
            AfwServerAnimationController.markInWallGrace(world, actorUuids);
        }
        AfwServerAnimationController.applySharedTransform(world, actorUuids, sharedTransform, preservePlayerLook);
        if (ignoreAttackers) {
            AfwServerAnimationController.clearAttackTargets(world, actorUuidSet);
        }
        Set<class_3222> targets = AfwServerAnimationController.computeBroadcastTargets(world, requester, actorUuids);
        for (class_3222 p : targets) {
            ServerPlayNetworking.send((class_3222)p, (class_8710)payload);
        }
        UUID playerUuid = AfwServerAnimationController.resolvePlayerUuid(world, actorUuids);
        originalTransforms = AfwServerAnimationController.applyDeferredQueueOriginal(world, playerUuid, originalTransforms, safeMetadata);
        boolean underwaterBreathing = waterRequirement == AfwAnimationDefinitions.WaterRequirement.UNDERWATER;
        ActiveInstance active = new ActiveInstance(world, animationId, List.copyOf(actorUuids), safeActorKeys, safeStages, startTick, originalTransforms, sharedTransform, lockOrientation, lockedYaw, lockedHeadYaw, lockedPitch, damageBehavior, ignoreAttackers, safeMetadata, underwaterBreathing, playerUuid, actorUuidSet, new StageTracker(startTick), placement, cameraOrbitTarget, initialSpeed);
        ACTIVE_INSTANCES.put(instanceId, active);
        AfwServerAnimationController.rememberInstanceSubscribers(active, targets);
        AfwServerAnimationController.acquirePlayerLocks(world, actorUuids);
        ((AfwAnimationEvents.Start)AfwAnimationEvents.START.invoker()).onStart(world, instanceId, animationId, List.copyOf(actorUuids), safeActorKeys, safeStages, startTick, requester, damageBehavior, ignoreAttackers);
        AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("actors_immediate_broadcast_start", new Object[0]), false);
        String requesterName = requester == null ? "api" : requester.method_5477().getString();
        AnimationFramework.LOGGER.info("Broadcast START: animation={} instance={} startTick={} actors={} requester={}", new Object[]{AfwServerAnimationController.describeAnimationIdForDebug(animationId), instanceId, startTick, actorUuids.size(), requesterName});
        return instanceId;
    }

    private static Set<class_3222> computeBroadcastTargets(class_3218 world, @Nullable class_3222 requester, List<UUID> actorUuids) {
        HashSet<class_3222> targets = new HashSet<class_3222>();
        if (requester != null) {
            targets.add(requester);
        }
        for (UUID uuid : actorUuids) {
            class_1297 e = world.method_66347(uuid);
            if (e != null) {
                targets.addAll(PlayerLookup.tracking((class_1297)e));
                if (!(e instanceof class_3222)) continue;
                class_3222 sp = (class_3222)e;
                targets.add(sp);
                continue;
            }
            class_3222 p = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(uuid);
            if (p == null || p.method_51469() != world) continue;
            targets.add(p);
            targets.addAll(PlayerLookup.tracking((class_1297)p));
        }
        return targets;
    }

    private static Set<class_3222> computeInstanceBroadcastTargets(class_3218 world, @Nullable class_3222 requester, ActiveInstance inst) {
        Set<class_3222> targets = AfwServerAnimationController.computeBroadcastTargets(world, requester, inst == null ? List.of() : inst.actorUuids);
        if (world == null || inst == null || inst.subscribedPlayerUuids.isEmpty()) {
            return targets;
        }
        for (UUID playerUuid : inst.subscribedPlayerUuids) {
            class_3222 player = world.method_8503().method_3760().method_14602(playerUuid);
            if (player == null || player.method_51469() != world) continue;
            targets.add(player);
        }
        return targets;
    }

    private static void rememberInstanceSubscribers(ActiveInstance inst, Set<class_3222> targets) {
        if (inst == null || targets == null || targets.isEmpty()) {
            return;
        }
        for (class_3222 player : targets) {
            if (player == null || player.method_51469() != inst.world) continue;
            inst.subscribedPlayerUuids.add(player.method_5667());
        }
    }

    private static void removePlayerFromInstanceSubscribers(UUID playerUuid) {
        if (playerUuid == null || ACTIVE_INSTANCES.isEmpty()) {
            return;
        }
        for (ActiveInstance inst : ACTIVE_INSTANCES.values()) {
            if (inst == null) continue;
            inst.subscribedPlayerUuids.remove(playerUuid);
        }
    }

    private static void fireStopEvent(UUID instanceId, ActiveInstance inst) {
        if (inst == null) {
            return;
        }
        List<UUID> actorUuids = inst.actorUuids == null ? List.of() : List.copyOf(inst.actorUuids);
        List<String> actorKeys = AfwServerAnimationController.sanitizeActorKeys(inst.actorKeys, actorUuids.size());
        List<AnimationStageInfo> stages = inst.stages == null ? List.of() : List.copyOf(inst.stages);
        ((AfwAnimationEvents.Stop)AfwAnimationEvents.STOP.invoker()).onStop(inst.world, instanceId, inst.animationId, actorUuids, actorKeys, stages);
    }

    private static boolean allSelectedPlayersNearRequester(List<class_1297> actors, class_3222 requester, double maxDistance) {
        if (actors == null || requester == null) {
            return false;
        }
        double maxDistanceSq = maxDistance * maxDistance;
        for (class_1297 actor : actors) {
            class_1657 playerActor;
            if (!(actor instanceof class_1657) || !((playerActor = (class_1657)actor).method_5858((class_1297)requester) > maxDistanceSq)) continue;
            return false;
        }
        return true;
    }

    private static class_243 computeCentroid(List<? extends class_1297> entities) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (class_1297 class_12972 : entities) {
            class_243 p = class_12972.method_73189();
            x += p.field_1352;
            y += p.field_1351;
            z += p.field_1350;
        }
        double n = entities.size();
        return new class_243(x / n, y / n, z / n);
    }

    private static Map<UUID, OriginalTransform> captureOriginalTransforms(class_3218 world, List<UUID> actorUuids) {
        HashMap<UUID, OriginalTransform> originals = new HashMap<UUID, OriginalTransform>();
        for (UUID uuid : actorUuids) {
            class_1297 e = world.method_66347(uuid);
            if (e instanceof class_1309) {
                class_243 class_2432;
                class_1309 living = (class_1309)e;
                if (living instanceof class_3222) {
                    class_3222 player = (class_3222)living;
                    class_2432 = AfwServerAnimationController.resolveMountedPlayerRestorePosition(player);
                } else {
                    class_2432 = null;
                }
                class_243 restorePos = class_2432;
                originals.put(uuid, new OriginalTransform((class_5321<class_1937>)world.method_27983(), living.method_73189(), living.method_36454(), living.method_36455(), living.method_5791(), living.method_73188(), restorePos));
                continue;
            }
            if (e == null) continue;
            originals.put(uuid, new OriginalTransform((class_5321<class_1937>)world.method_27983(), e.method_73189(), e.method_36454(), e.method_36455(), e.method_36454(), e.method_36454()));
        }
        return originals;
    }

    @Nullable
    private static UUID resolveEffectivePositionAnchorUuid(class_3218 world, class_2960 animationId, List<UUID> actorUuids, List<String> actorKeys, @Nullable UUID preferredAnchorUuid) {
        String anchorActorKey;
        class_1297 explicitAnchor = AfwServerAnimationController.resolveAnchorEntity(world, actorUuids, preferredAnchorUuid);
        if (explicitAnchor != null) {
            return explicitAnchor.method_5667();
        }
        AfwAnimationDefinitions.Definition def = AfwAnimationDefinitions.getDefinition(animationId);
        String string = anchorActorKey = def == null ? null : AfwServerAnimationController.normalizeActorKey(def.positionAnchorActor());
        if (anchorActorKey == null) {
            return null;
        }
        if (actorKeys == null || actorKeys.size() != actorUuids.size()) {
            return null;
        }
        for (int i = 0; i < actorUuids.size(); ++i) {
            if (!anchorActorKey.equals(AfwServerAnimationController.normalizeActorKey(actorKeys.get(i)))) continue;
            class_1297 anchor = AfwServerAnimationController.resolveAnchorEntity(world, actorUuids, actorUuids.get(i));
            return anchor == null ? null : anchor.method_5667();
        }
        return null;
    }

    @Nullable
    private static String normalizeActorKey(@Nullable String actorKey) {
        if (actorKey == null) {
            return null;
        }
        String value = actorKey.trim();
        if (value.isEmpty()) {
            return null;
        }
        return value.toLowerCase(Locale.ROOT);
    }

    @Nullable
    private static class_1297 resolveAnchorEntity(class_3218 world, List<UUID> actorUuids, @Nullable UUID anchorUuid) {
        if (world == null || actorUuids == null || actorUuids.isEmpty() || anchorUuid == null) {
            return null;
        }
        if (!actorUuids.contains(anchorUuid)) {
            return null;
        }
        class_1297 entity = world.method_66347(anchorUuid);
        if (entity != null) {
            return entity;
        }
        class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(anchorUuid);
        if (player != null && player.method_51469() == world) {
            return player;
        }
        return null;
    }

    private static SharedTransform pickSharedTransform(List<UUID> actorUuids, Map<UUID, OriginalTransform> originals, @Nullable UUID anchorUuid) {
        OriginalTransform anchored;
        if (anchorUuid != null && (anchored = originals.get(anchorUuid)) != null) {
            return new SharedTransform(anchored.pos(), anchored.yaw(), anchored.pitch(), anchored.headYaw(), anchored.bodyYaw());
        }
        for (UUID uuid : actorUuids) {
            OriginalTransform ot = originals.get(uuid);
            if (ot == null) continue;
            return new SharedTransform(ot.pos(), ot.yaw(), ot.pitch(), ot.headYaw(), ot.bodyYaw());
        }
        return new SharedTransform(class_243.field_1353, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    private static class_1297 pickAnchorEntity(class_3218 world, List<UUID> actorUuids, @Nullable UUID preferredAnchorUuid) {
        class_1297 anchor = AfwServerAnimationController.resolveAnchorEntity(world, actorUuids, preferredAnchorUuid);
        if (anchor != null) {
            return anchor;
        }
        return AfwServerAnimationController.pickAnchorEntity(world, actorUuids);
    }

    private static class_1297 pickAnchorEntity(class_3218 world, List<UUID> actorUuids) {
        class_1297 e;
        for (UUID uuid : actorUuids) {
            e = world.method_66347(uuid);
            if (!(e instanceof class_1657)) continue;
            return e;
        }
        for (UUID uuid : actorUuids) {
            e = world.method_66347(uuid);
            if (e == null) continue;
            return e;
        }
        return null;
    }

    private static boolean waterRequirementMet(class_3218 world, @Nullable AfwAnimationDefinitions.Definition def, List<UUID> actorUuids) {
        AfwAnimationDefinitions.WaterRequirement req;
        AfwAnimationDefinitions.WaterRequirement waterRequirement = req = def == null ? AfwAnimationDefinitions.WaterRequirement.NONE : def.waterRequirement();
        if (req == AfwAnimationDefinitions.WaterRequirement.NONE) {
            return !AfwServerAnimationController.anyActorOnWaterFooting(world, actorUuids);
        }
        return AfwServerAnimationController.findWaterAnchor(world, actorUuids, req) != null;
    }

    private static boolean anyActorOnWaterFooting(class_3218 world, List<UUID> actorUuids) {
        for (UUID uuid : actorUuids) {
            class_2338 underFeet;
            class_1297 e = world.method_66347(uuid);
            if (e == null || !world.method_8316(underFeet = e.method_24515().method_10074()).method_15767(class_3486.field_15517)) continue;
            return true;
        }
        return false;
    }

    @Nullable
    private static class_1297 findWaterAnchor(class_3218 world, List<UUID> actorUuids, AfwAnimationDefinitions.WaterRequirement requirement) {
        for (UUID uuid : actorUuids) {
            class_1297 e = world.method_66347(uuid);
            if (e == null || !AfwServerAnimationController.meetsWaterRequirement(e, requirement)) continue;
            return e;
        }
        return null;
    }

    private static boolean meetsWaterRequirement(class_1297 e, AfwAnimationDefinitions.WaterRequirement requirement) {
        return switch (requirement) {
            default -> throw new MatchException(null, null);
            case AfwAnimationDefinitions.WaterRequirement.SURFACE -> {
                if (e.method_5799() && !e.method_5869()) {
                    yield true;
                }
                yield false;
            }
            case AfwAnimationDefinitions.WaterRequirement.UNDERWATER -> e.method_5869();
            case AfwAnimationDefinitions.WaterRequirement.NONE -> true;
        };
    }

    @Nullable
    private static BlockPlacement findWallPlacement(class_3218 world, class_1297 anchor, AfwAnimationDefinitions.BlockRequirements requirements) {
        class_243 anchorPos = anchor.method_73189();
        class_2338 anchorBlock = class_2338.method_49638((class_2374)anchorPos);
        int radius = AfwServerAnimationController.getBlockScanRadius();
        double bestDistSq = Double.MAX_VALUE;
        BlockPlacement best = null;
        int minY = anchorBlock.method_10264() - 1;
        int maxY = anchorBlock.method_10264() + 1;
        for (int y = minY; y <= maxY; ++y) {
            for (int dx = -radius; dx <= radius; ++dx) {
                for (int dz = -radius; dz <= radius; ++dz) {
                    class_2338 frontPos;
                    class_243 center;
                    double distSq;
                    class_2350 facing;
                    int wallHeight;
                    class_2338 pos = new class_2338(anchorBlock.method_10263() + dx, y, anchorBlock.method_10260() + dz);
                    if (!AfwServerAnimationController.isSolidFloorBlock(world, pos.method_10074()) || !AfwServerAnimationController.isWallRequirementBlock(world, pos, requirements) || !AfwServerAnimationController.matchesWallHeight(wallHeight = AfwServerAnimationController.countWallHeight(world, pos, requirements.height(), requirements), requirements.height()) || !AfwServerAnimationController.hasWallHeightCapClearance(world, pos, wallHeight, requirements.height()) || !AfwServerAnimationController.checkClearance(world, pos, facing = AfwServerAnimationController.chooseFacingToward(anchorPos, pos), requirements.clearance()) || !((distSq = (center = class_243.method_24953((class_2382)(frontPos = pos.method_10093(facing)))).method_1025(anchorPos)) < bestDistSq)) continue;
                    float yaw = AfwServerAnimationController.directionToYaw(facing.method_10153());
                    class_243 sharedPos = new class_243(center.field_1352, (double)frontPos.method_10264(), center.field_1350);
                    double inset = AfwServerAnimationController.getInsetForWallBlock(world, pos);
                    if (inset > 0.0) {
                        class_243 insetDir = class_243.method_24954((class_2382)facing.method_10153().method_62675()).method_1021(inset);
                        sharedPos = sharedPos.method_1019(insetDir);
                    }
                    if (!AfwServerAnimationController.hasBlockRequirementLineOfSight(world, anchor, sharedPos)) continue;
                    bestDistSq = distSq;
                    SharedTransform transform = new SharedTransform(sharedPos, yaw, 0.0f, yaw, yaw);
                    best = new BlockPlacement(pos, facing, transform, null, false);
                }
            }
        }
        return best;
    }

    @Nullable
    private static BlockPlacement findBlockPlacement(class_3218 world, class_1297 anchor, AfwAnimationDefinitions.BlockRequirements requirements) {
        if (requirements == null) {
            return null;
        }
        if (requirements.isWall()) {
            return AfwServerAnimationController.findWallPlacement(world, anchor, requirements);
        }
        if (requirements.isCenterSupport()) {
            return AfwServerAnimationController.findCenterSupportPlacement(world, anchor, requirements);
        }
        return null;
    }

    @Nullable
    private static BlockPlacement findCenterSupportPlacement(class_3218 world, class_1297 anchor, AfwAnimationDefinitions.BlockRequirements requirements) {
        class_243 anchorPos = anchor.method_73189();
        class_2338 anchorBlock = class_2338.method_49638((class_2374)anchorPos);
        int radius = AfwServerAnimationController.getBlockScanRadius();
        double bestDistSq = Double.MAX_VALUE;
        BlockPlacement best = null;
        int minY = anchorBlock.method_10264() - 1;
        int maxY = anchorBlock.method_10264() + 1;
        for (int y = minY; y <= maxY; ++y) {
            for (int dx = -radius; dx <= radius; ++dx) {
                for (int dz = -radius; dz <= radius; ++dz) {
                    class_2338 supportPos = new class_2338(anchorBlock.method_10263() + dx, y, anchorBlock.method_10260() + dz);
                    Double supportTopOffset = AfwServerAnimationController.supportTopOffset(world, supportPos, requirements.support(), requirements.blocks());
                    if (supportTopOffset == null) continue;
                    class_2350[] directions = AfwServerAnimationController.directionsByDistanceToAnchor(anchorPos, supportPos, supportTopOffset);
                    if (requirements.placement() == AfwAnimationDefinitions.CenterPlacement.SURFACE_FOOTPRINT) {
                        for (class_2350 direction : directions) {
                            for (int lateralSign : new int[]{1, -1}) {
                                class_243 footprintCenter;
                                double distSq;
                                BedFootprintFacing bedFacing;
                                if (!AfwServerAnimationController.checkCenterSurfaceFootprint(world, supportPos, direction, lateralSign, supportTopOffset, requirements) || (bedFacing = AfwServerAnimationController.resolveBedFootprintFacing(world, supportPos, direction, lateralSign, requirements)).bedFootprint() && bedFacing.facing() == null || !((distSq = (footprintCenter = AfwServerAnimationController.centerSurfaceFootprint(supportPos, direction, lateralSign, supportTopOffset, requirements.surfaceFootprint())).method_1025(anchorPos)) < bestDistSq)) continue;
                                class_2350 placementFacing = bedFacing.facing() == null ? direction : bedFacing.facing().method_10153();
                                class_2338 placementPos = supportPos;
                                if (bedFacing.facing() != null) {
                                    AfwAnimationDefinitions.SurfaceFootprint footprint = requirements.surfaceFootprint();
                                    class_2350 side = lateralSign >= 0 ? direction.method_10160() : direction.method_10170();
                                    placementPos = supportPos.method_10079(bedFacing.facing(), Math.max(0, footprint.depth() - 1)).method_10079(side, Math.max(0, footprint.width() - 1));
                                }
                                float yaw = AfwServerAnimationController.directionToYaw(placementFacing.method_10153());
                                class_243 sharedPos = new class_243((double)placementPos.method_10263() + 0.5, (double)placementPos.method_10264(), (double)placementPos.method_10260() + 0.5);
                                if (!AfwServerAnimationController.hasBlockRequirementLineOfSight(world, anchor, footprintCenter)) continue;
                                bestDistSq = distSq;
                                SharedTransform transform = new SharedTransform(sharedPos, yaw, 0.0f, yaw, yaw);
                                best = new BlockPlacement(placementPos, placementFacing, transform, null, true);
                            }
                        }
                        continue;
                    }
                    if (!AfwServerAnimationController.checkCenterSupportSurfaceRadius(world, supportPos, supportTopOffset, requirements.surfaceRadius(), requirements.clearance())) continue;
                    for (class_2350 direction : directions) {
                        class_2338 sidePos;
                        class_243 sideCenter;
                        double distSq;
                        if (!AfwServerAnimationController.checkCenterDirectionalClearance(world, supportPos, direction, supportTopOffset, requirements.clearance()) || !((distSq = (sideCenter = new class_243((double)(sidePos = supportPos.method_10093(direction)).method_10263() + 0.5, (double)supportPos.method_10264() + supportTopOffset, (double)sidePos.method_10260() + 0.5)).method_1025(anchorPos)) < bestDistSq)) continue;
                        float yaw = AfwServerAnimationController.directionToYaw(direction.method_10153());
                        class_243 sharedPos = new class_243((double)supportPos.method_10263() + 0.5, (double)supportPos.method_10264(), (double)supportPos.method_10260() + 0.5);
                        if (!AfwServerAnimationController.hasBlockRequirementLineOfSight(world, anchor, sideCenter)) continue;
                        bestDistSq = distSq;
                        SharedTransform transform = new SharedTransform(sharedPos, yaw, 0.0f, yaw, yaw);
                        best = new BlockPlacement(supportPos, direction, transform, null, true);
                    }
                }
            }
        }
        return best;
    }

    private static class_2350[] directionsByDistanceToAnchor(class_243 anchorPos, class_2338 supportPos, double supportTopOffset) {
        class_2350[] directions = new class_2350[]{class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034};
        Arrays.sort(directions, Comparator.comparingDouble(direction -> {
            class_2338 side = supportPos.method_10093(direction);
            class_243 center = new class_243((double)side.method_10263() + 0.5, (double)supportPos.method_10264() + supportTopOffset, (double)side.method_10260() + 0.5);
            return center.method_1025(anchorPos);
        }));
        return directions;
    }

    @Nullable
    private static Double supportTopOffset(class_3218 world, class_2338 pos, AfwAnimationDefinitions.CenterSupport support, @Nullable AfwAnimationDefinitions.BlockPredicate blocks) {
        if (support == null) {
            return null;
        }
        class_2680 state = world.method_8320(pos);
        if (!state.method_26227().method_15769()) {
            return null;
        }
        if (!AfwServerAnimationController.matchesBlockPredicate(state, blocks)) {
            return null;
        }
        if (support == AfwAnimationDefinitions.CenterSupport.SURFACE) {
            if (blocks == null || blocks.isEmpty()) {
                return null;
            }
            class_265 shape = state.method_26220((class_1922)world, pos);
            if (shape.method_1110()) {
                return null;
            }
            return shape.method_1105(class_2350.class_2351.field_11052);
        }
        if (support == AfwAnimationDefinitions.CenterSupport.HALF_HEIGHT) {
            if (AfwServerAnimationController.isHalfHeightSupport(state)) {
                return 0.5625;
            }
            return null;
        }
        if (state.method_26204() instanceof class_2482) {
            class_2771 type = (class_2771)state.method_11654((class_2769)class_2482.field_11501);
            if (support == AfwAnimationDefinitions.CenterSupport.SLAB) {
                return type == class_2771.field_12681 ? Double.valueOf(0.5) : null;
            }
            if (support == AfwAnimationDefinitions.CenterSupport.FULL_BLOCK) {
                return type == class_2771.field_12679 || type == class_2771.field_12682 ? Double.valueOf(1.0) : null;
            }
        }
        if (support == AfwAnimationDefinitions.CenterSupport.FULL_BLOCK && AfwServerAnimationController.isFullHeightSupport(world, pos, state)) {
            return 1.0;
        }
        return null;
    }

    private static boolean isHalfHeightSupport(class_2680 state) {
        if (state.method_26204() instanceof class_2244) {
            return true;
        }
        return state.method_26204() instanceof class_2482 && state.method_11654((class_2769)class_2482.field_11501) == class_2771.field_12681;
    }

    private static boolean isFullHeightSupport(class_3218 world, class_2338 pos, class_2680 state) {
        if (state.method_26215() || state.method_45474()) {
            return false;
        }
        if (state.method_26212((class_1922)world, pos)) {
            return true;
        }
        class_265 shape = state.method_26220((class_1922)world, pos);
        return !shape.method_1110() && shape.method_1105(class_2350.class_2351.field_11052) >= 0.999;
    }

    private static boolean checkCenterDirectionalClearance(class_3218 world, class_2338 supportPos, class_2350 facing, double supportTopOffset, AfwAnimationDefinitions.Clearance clearance) {
        if (clearance == null) {
            return true;
        }
        int width = Math.max(0, clearance.width());
        int height = Math.max(1, clearance.height());
        int depth = Math.max(1, clearance.depth());
        class_2350 left = facing.method_10160();
        for (int d = 1; d <= depth; ++d) {
            for (int w = -width; w <= width; ++w) {
                class_2338 check = supportPos.method_10079(facing, d).method_10079(left, w);
                if (!AfwServerAnimationController.isDirectionalClearanceFree(world, check, supportPos.method_10264(), height)) {
                    return false;
                }
                if (AfwServerAnimationController.isSolidFloorBlock(world, check.method_10074())) continue;
                return false;
            }
        }
        return true;
    }

    private static boolean checkCenterSurfaceFootprint(class_3218 world, class_2338 supportPos, class_2350 facing, int lateralSign, double supportTopOffset, AfwAnimationDefinitions.BlockRequirements requirements) {
        AfwAnimationDefinitions.SurfaceFootprint footprint = requirements.surfaceFootprint();
        if (footprint == null) {
            return false;
        }
        int width = Math.max(1, footprint.width());
        int depth = Math.max(1, footprint.depth());
        int height = Math.max(1, footprint.height());
        int margin = Math.max(0, footprint.margin());
        class_2350 side = lateralSign >= 0 ? facing.method_10160() : facing.method_10170();
        double supportTopY = (double)supportPos.method_10264() + supportTopOffset;
        for (int d = -margin; d < depth + margin; ++d) {
            for (int w = -margin; w < width + margin; ++w) {
                boolean insideFootprint;
                class_2338 check = supportPos.method_10079(facing, d).method_10079(side, w);
                boolean bl = insideFootprint = d >= 0 && d < depth && w >= 0 && w < width;
                if (insideFootprint) {
                    Double checkTopOffset = AfwServerAnimationController.supportTopOffset(world, check, requirements.support(), requirements.blocks());
                    if (checkTopOffset == null || Math.abs(checkTopOffset - supportTopOffset) > 1.0E-4) {
                        return false;
                    }
                    if (AfwServerAnimationController.isSpaceClearAboveSupport(world, check, supportTopY, height)) continue;
                    return false;
                }
                if (AfwServerAnimationController.isSurfaceFootprintMarginClear(world, check, supportTopY, height)) continue;
                return false;
            }
        }
        return true;
    }

    private static BedFootprintFacing resolveBedFootprintFacing(class_3218 world, class_2338 supportPos, class_2350 facing, int lateralSign, AfwAnimationDefinitions.BlockRequirements requirements) {
        AfwAnimationDefinitions.SurfaceFootprint footprint = requirements.surfaceFootprint();
        if (footprint == null) {
            return BedFootprintFacing.none();
        }
        List<class_2338> positions = AfwServerAnimationController.centerSurfaceFootprintPositions(supportPos, facing, lateralSign, footprint);
        if (positions.isEmpty()) {
            return BedFootprintFacing.none();
        }
        int bedCount = 0;
        for (class_2338 pos : positions) {
            if (!(world.method_8320(pos).method_26204() instanceof class_2244)) continue;
            ++bedCount;
        }
        if (bedCount == 0) {
            return BedFootprintFacing.none();
        }
        boolean bedOnlyPredicate = AfwServerAnimationController.isBedOnlyBlockPredicate(requirements.blocks());
        if (bedCount != positions.size()) {
            return bedOnlyPredicate ? BedFootprintFacing.invalid() : BedFootprintFacing.none();
        }
        HashSet<class_2338> footprintPositions = new HashSet<class_2338>(positions);
        HashSet<class_2338> pairedPositions = new HashSet<class_2338>();
        class_2350 expectedFacing = null;
        for (class_2338 pos : positions) {
            class_2338 otherPos;
            class_2680 state = world.method_8320(pos);
            class_2350 bedFacing = (class_2350)state.method_11654((class_2769)class_2244.field_11177);
            class_2742 part = (class_2742)state.method_11654((class_2769)class_2244.field_9967);
            if (expectedFacing == null) {
                expectedFacing = bedFacing;
            } else if (expectedFacing != bedFacing) {
                return BedFootprintFacing.invalid();
            }
            class_2338 class_23382 = otherPos = part == class_2742.field_12557 ? pos.method_10093(bedFacing) : pos.method_10093(bedFacing.method_10153());
            if (!footprintPositions.contains(otherPos)) {
                return BedFootprintFacing.invalid();
            }
            class_2680 otherState = world.method_8320(otherPos);
            if (!(otherState.method_26204() instanceof class_2244)) {
                return BedFootprintFacing.invalid();
            }
            if (otherState.method_11654((class_2769)class_2244.field_11177) != bedFacing) {
                return BedFootprintFacing.invalid();
            }
            if (otherState.method_11654((class_2769)class_2244.field_9967) == part) {
                return BedFootprintFacing.invalid();
            }
            pairedPositions.add(pos);
            pairedPositions.add(otherPos);
        }
        if (!pairedPositions.containsAll(footprintPositions) || expectedFacing == null) {
            return BedFootprintFacing.invalid();
        }
        if (facing != expectedFacing || lateralSign < 0) {
            return BedFootprintFacing.invalid();
        }
        return BedFootprintFacing.valid(expectedFacing);
    }

    private static List<class_2338> centerSurfaceFootprintPositions(class_2338 supportPos, class_2350 facing, int lateralSign, AfwAnimationDefinitions.SurfaceFootprint footprint) {
        int width = Math.max(1, footprint == null ? 1 : footprint.width());
        int depth = Math.max(1, footprint == null ? 1 : footprint.depth());
        class_2350 side = lateralSign >= 0 ? facing.method_10160() : facing.method_10170();
        ArrayList<class_2338> positions = new ArrayList<class_2338>(width * depth);
        for (int d = 0; d < depth; ++d) {
            for (int w = 0; w < width; ++w) {
                positions.add(supportPos.method_10079(facing, d).method_10079(side, w));
            }
        }
        return positions;
    }

    private static boolean isBedOnlyBlockPredicate(@Nullable AfwAnimationDefinitions.BlockPredicate blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return false;
        }
        for (class_2960 tagId : blocks.tagIds()) {
            if (BEDS_BLOCK_TAG_ID.equals((Object)tagId)) continue;
            return false;
        }
        for (class_2960 blockId : blocks.blockIds()) {
            if (class_7923.field_41175.method_63535(blockId) instanceof class_2244) continue;
            return false;
        }
        return !blocks.tagIds().isEmpty() || !blocks.blockIds().isEmpty();
    }

    private static boolean isSurfaceFootprintMarginClear(class_3218 world, class_2338 pos, double supportTopY, int height) {
        class_238 clearanceBox = new class_238((double)pos.method_10263(), supportTopY + 1.0E-4, (double)pos.method_10260(), (double)pos.method_10263() + 1.0, supportTopY + (double)Math.max(1, height), (double)pos.method_10260() + 1.0);
        return world.method_18026(clearanceBox) && !AfwServerAnimationController.containsFluid(world, clearanceBox);
    }

    private static class_243 centerSurfaceFootprint(class_2338 supportPos, class_2350 facing, int lateralSign, double supportTopOffset, AfwAnimationDefinitions.SurfaceFootprint footprint) {
        int width = Math.max(1, footprint == null ? 1 : footprint.width());
        int depth = Math.max(1, footprint == null ? 1 : footprint.depth());
        class_2350 side = lateralSign >= 0 ? facing.method_10160() : facing.method_10170();
        double forwardOffset = (double)(depth - 1) * 0.5;
        double sideOffset = (double)(width - 1) * 0.5;
        return new class_243((double)supportPos.method_10263() + 0.5 + (double)facing.method_10148() * forwardOffset + (double)side.method_10148() * sideOffset, (double)supportPos.method_10264() + supportTopOffset, (double)supportPos.method_10260() + 0.5 + (double)facing.method_10165() * forwardOffset + (double)side.method_10165() * sideOffset);
    }

    private static boolean checkCenterSupportSurfaceRadius(class_3218 world, class_2338 supportPos, double supportTopOffset, int surfaceRadius, AfwAnimationDefinitions.Clearance clearance) {
        int radius = Math.max(0, surfaceRadius);
        int height = Math.max(1, clearance == null ? 2 : clearance.height());
        double supportTopY = (double)supportPos.method_10264() + supportTopOffset;
        for (int dx = -radius; dx <= radius; ++dx) {
            for (int dz = -radius; dz <= radius; ++dz) {
                class_2338 check = supportPos.method_10069(dx, 0, dz);
                if (!AfwServerAnimationController.isSameOrLowerSurface(world, check, supportTopOffset)) {
                    return false;
                }
                if (AfwServerAnimationController.isSpaceClearAboveSupport(world, check, supportTopY, height)) continue;
                return false;
            }
        }
        return true;
    }

    private static boolean isSameOrLowerSurface(class_3218 world, class_2338 pos, double supportTopOffset) {
        class_2680 state = world.method_8320(pos);
        if (!state.method_26227().method_15769()) {
            return false;
        }
        if (state.method_26215() || state.method_45474()) {
            return true;
        }
        class_265 shape = state.method_26220((class_1922)world, pos);
        if (shape.method_1110()) {
            return true;
        }
        return shape.method_1105(class_2350.class_2351.field_11052) <= supportTopOffset + 1.0E-4;
    }

    private static boolean isDirectionalClearanceFree(class_3218 world, class_2338 pos, int supportY, int height) {
        class_238 clearanceBox = new class_238((double)pos.method_10263(), (double)supportY + 1.0E-4, (double)pos.method_10260(), (double)pos.method_10263() + 1.0, (double)(supportY + Math.max(1, height)), (double)pos.method_10260() + 1.0);
        return world.method_18026(clearanceBox) && !AfwServerAnimationController.containsFluid(world, clearanceBox);
    }

    private static boolean isSpaceClearAboveSupport(class_3218 world, class_2338 pos, double supportTopY, int height) {
        class_238 clearanceBox = new class_238((double)pos.method_10263(), supportTopY + 1.0E-4, (double)pos.method_10260(), (double)pos.method_10263() + 1.0, supportTopY + (double)Math.max(1, height), (double)pos.method_10260() + 1.0);
        return world.method_18026(clearanceBox) && !AfwServerAnimationController.containsFluid(world, clearanceBox);
    }

    private static class_2561 blockRequirementFailureMessage(AfwAnimationDefinitions.BlockRequirements requirements, class_2960 animationId, class_3218 world, List<UUID> actorUuids) {
        String animation = AfwServerAnimationController.describeAnimationIdForDebug(animationId);
        String actors = AfwServerAnimationController.describeActorsForDebug(world, actorUuids);
        if (requirements != null && requirements.isCenterSupport()) {
            return AfwServerAnimationController.dbg("start_cancelled_no_valid_center_support", animation, actors);
        }
        return AfwServerAnimationController.dbg("start_cancelled_no_valid_wall", animation, actors);
    }

    private static boolean checkClearance(class_3218 world, class_2338 wallPos, class_2350 facing, AfwAnimationDefinitions.Clearance clearance) {
        if (clearance == null) {
            return true;
        }
        int width = Math.max(1, clearance.width());
        int height = Math.max(1, clearance.height());
        int depth = Math.max(1, clearance.depth());
        class_2350 left = facing.method_10160();
        for (int dy = 0; dy < height; ++dy) {
            for (int d = 1; d <= depth; ++d) {
                for (int w = -width; w <= width; ++w) {
                    class_2338 check = wallPos.method_10079(facing, d).method_10079(left, w).method_10086(dy);
                    if (!AfwServerAnimationController.isFreeSpace(world, check)) {
                        return false;
                    }
                    if (dy != 0 || AfwServerAnimationController.isSolidWallBlock(world, check.method_10074())) continue;
                    return false;
                }
            }
        }
        return true;
    }

    private static int getBlockScanRadius() {
        int radius = AfwClientConfig.get().blockSearchRadius();
        return Math.max(1, radius);
    }

    private static boolean hasBlockRequirementLineOfSight(class_3218 world, class_1297 anchor, class_243 target) {
        class_243 end;
        if (world == null || anchor == null || target == null) {
            return false;
        }
        class_243 start = anchor.method_33571();
        if (start.method_1025(end = target.method_1031(0.0, 0.75, 0.0)) < 1.0E-6) {
            return true;
        }
        class_3965 result = world.method_17742(new class_3959(start, end, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, anchor));
        return result == null || result.method_17783() == class_239.class_240.field_1333;
    }

    private static void markInWallGrace(class_3218 world, List<UUID> actorUuids) {
        if (actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        long until = world.method_75260() + 5L;
        Map grace = IN_WALL_GRACE_BY_WORLD.computeIfAbsent((class_5321<class_1937>)world.method_27983(), k -> new HashMap());
        for (UUID uuid : actorUuids) {
            Long existing = (Long)grace.get(uuid);
            if (existing != null && existing >= until) continue;
            grace.put(uuid, until);
        }
    }

    private static void cleanupInWallGrace(class_3218 world, long now) {
        Map<UUID, Long> grace = IN_WALL_GRACE_BY_WORLD.get(world.method_27983());
        if (grace == null || grace.isEmpty()) {
            return;
        }
        grace.entrySet().removeIf(entry -> (Long)entry.getValue() < now);
    }

    private static class_2350 chooseFacingToward(class_243 anchorPos, class_2338 wallPos) {
        class_243 center = class_243.method_24953((class_2382)wallPos);
        double dx = anchorPos.field_1352 - center.field_1352;
        double dz = anchorPos.field_1350 - center.field_1350;
        if (Math.abs(dx) >= Math.abs(dz)) {
            return dx >= 0.0 ? class_2350.field_11034 : class_2350.field_11039;
        }
        return dz >= 0.0 ? class_2350.field_11035 : class_2350.field_11043;
    }

    private static float directionToYaw(class_2350 dir) {
        return switch (dir) {
            case class_2350.field_11039 -> 90.0f;
            case class_2350.field_11043 -> 180.0f;
            case class_2350.field_11034 -> -90.0f;
            default -> 0.0f;
        };
    }

    private static int countWallHeight(class_3218 world, class_2338 pos, AfwAnimationDefinitions.WallHeight height, AfwAnimationDefinitions.BlockRequirements requirements) {
        if (height == null) {
            return 0;
        }
        int min = Math.max(1, height.min());
        Integer max = height.max();
        int limit = max != null ? max + 1 : min;
        int actual = 0;
        class_2338 cursor = pos;
        while (actual < limit && AfwServerAnimationController.isWallRequirementBlock(world, cursor, requirements)) {
            ++actual;
            cursor = cursor.method_10084();
        }
        return actual;
    }

    private static boolean matchesWallHeight(int actual, AfwAnimationDefinitions.WallHeight height) {
        if (height == null) {
            return false;
        }
        int min = Math.max(1, height.min());
        Integer max = height.max();
        if (actual < min) {
            return false;
        }
        return max == null || actual <= max;
    }

    private static boolean hasWallHeightCapClearance(class_3218 world, class_2338 pos, int actual, AfwAnimationDefinitions.WallHeight height) {
        if (height == null || height.max() == null) {
            return true;
        }
        if (actual != height.max()) {
            return true;
        }
        return AfwServerAnimationController.isFreeSpace(world, pos.method_10086(actual));
    }

    private static boolean isSolidWallBlock(class_3218 world, class_2338 pos) {
        class_2680 state = world.method_8320(pos);
        if (!state.method_26227().method_15769()) {
            return false;
        }
        if (state.method_26164(class_3481.field_16584) || state.method_26164(class_3481.field_15504)) {
            return true;
        }
        if (state.method_26212((class_1922)world, pos)) {
            return true;
        }
        class_265 shape = state.method_26220((class_1922)world, pos);
        return !shape.method_1110() && shape.method_1105(class_2350.class_2351.field_11052) >= 0.999;
    }

    private static boolean isWallRequirementBlock(class_3218 world, class_2338 pos, AfwAnimationDefinitions.BlockRequirements requirements) {
        AfwAnimationDefinitions.BlockPredicate blocks;
        AfwAnimationDefinitions.BlockPredicate blockPredicate = blocks = requirements == null ? null : requirements.blocks();
        if (blocks == null || blocks.isEmpty()) {
            return AfwServerAnimationController.isSolidWallBlock(world, pos);
        }
        class_2680 state = world.method_8320(pos);
        if (!state.method_26227().method_15769()) {
            return false;
        }
        if (!AfwServerAnimationController.matchesBlockPredicate(state, blocks)) {
            return false;
        }
        return !state.method_26220((class_1922)world, pos).method_1110();
    }

    private static boolean matchesBlockPredicate(class_2680 state, @Nullable AfwAnimationDefinitions.BlockPredicate blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return true;
        }
        class_2960 blockId = class_7923.field_41175.method_10221((Object)state.method_26204());
        if (blocks.blockIds().contains(blockId)) {
            return true;
        }
        for (class_2960 tagId : blocks.tagIds()) {
            if (!state.method_26164(class_6862.method_40092((class_5321)class_7924.field_41254, (class_2960)tagId))) continue;
            return true;
        }
        return false;
    }

    private static boolean isSolidFloorBlock(class_3218 world, class_2338 pos) {
        class_2680 state = world.method_8320(pos);
        if (!state.method_26227().method_15769()) {
            return false;
        }
        if (state.method_26215() || state.method_45474()) {
            return false;
        }
        if (state.method_26164(class_3481.field_16584) || state.method_26164(class_3481.field_15504)) {
            return false;
        }
        if (state.method_26212((class_1922)world, pos)) {
            return true;
        }
        class_265 shape = state.method_26220((class_1922)world, pos);
        return !shape.method_1110() && shape.method_1105(class_2350.class_2351.field_11052) >= 0.999;
    }

    private static double getInsetForWallBlock(class_3218 world, class_2338 pos) {
        class_2680 state = world.method_8320(pos);
        if (state.method_26164(class_3481.field_16584)) {
            return 0.4;
        }
        if (state.method_26164(class_3481.field_15504)) {
            return 0.3;
        }
        return 0.0;
    }

    private static boolean isFreeSpace(class_3218 world, class_2338 pos) {
        class_2680 state = world.method_8320(pos);
        if (!state.method_26227().method_15769()) {
            return false;
        }
        return state.method_26215() || state.method_45474();
    }

    private static void applySharedTransform(class_3218 world, List<UUID> actorUuids, SharedTransform sharedTransform, boolean preservePlayerLook) {
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(world.method_27983());
        for (UUID uuid : actorUuids) {
            AiDisableState state;
            class_1297 class_12972;
            class_1297 e = world.method_66347(uuid);
            if (e == null) continue;
            Objects.requireNonNull(e);
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{class_3222.class, class_1309.class}, (Object)class_12972, n)) {
                case 0: {
                    class_3222 player = (class_3222)class_12972;
                    AfwServerAnimationController.preparePlayerForAnimationTeleport(player);
                    float teleportYaw = preservePlayerLook ? player.method_36454() : sharedTransform.yaw();
                    float teleportPitch = preservePlayerLook ? player.method_36455() : sharedTransform.pitch();
                    player.field_13987.method_14363(sharedTransform.pos().field_1352, sharedTransform.pos().field_1351, sharedTransform.pos().field_1350, teleportYaw, teleportPitch);
                    player.method_5847(sharedTransform.headYaw());
                    player.method_5636(sharedTransform.bodyYaw());
                    break;
                }
                case 1: {
                    class_1309 living = (class_1309)class_12972;
                    living.method_5808(sharedTransform.pos().field_1352, sharedTransform.pos().field_1351, sharedTransform.pos().field_1350, sharedTransform.yaw(), sharedTransform.pitch());
                    living.method_5847(sharedTransform.headYaw());
                    living.method_5636(sharedTransform.bodyYaw());
                    break;
                }
                default: {
                    e.method_5808(sharedTransform.pos().field_1352, sharedTransform.pos().field_1351, sharedTransform.pos().field_1350, sharedTransform.yaw(), sharedTransform.pitch());
                }
            }
            e.method_18799(class_243.field_1353);
            e.field_6017 = 0.0;
            if (!(e instanceof class_1308)) continue;
            class_1308 mob = (class_1308)e;
            if (states == null || (state = states.get(uuid)) == null) continue;
            state.lockedYaw = sharedTransform.yaw();
            state.lockedHeadYaw = sharedTransform.headYaw();
            state.lockedBodyYaw = sharedTransform.bodyYaw();
            state.lockedPitch = sharedTransform.pitch();
        }
    }

    private static void preparePlayerForAnimationTeleport(class_3222 player) {
        if (player == null) {
            return;
        }
        if (player.method_5765()) {
            player.method_5848();
        }
        if (player.method_6113()) {
            player.method_7358(true, true);
        }
        player.method_18799(class_243.field_1353);
        player.field_6017 = 0.0;
    }

    @Nullable
    private static SharedTransform findSafeGroundPlacement(class_3218 world, SharedTransform sharedTransform) {
        class_243 startPos = sharedTransform.pos();
        FloorHit baseFloor = AfwServerAnimationController.findNearestFloor(world, startPos, 2);
        if (!baseFloor.found) {
            return null;
        }
        SharedTransform base = AfwServerAnimationController.withSharedPosition(sharedTransform, baseFloor.pos);
        if (AfwServerAnimationController.isGroundPlacementClear(world, baseFloor.pos)) {
            return base;
        }
        for (GroundPlacementOffset offset : GROUND_PLACEMENT_OFFSETS) {
            class_243 candidateStart = startPos.method_1031(offset.dx(), 0.0, offset.dz());
            FloorHit candidateFloor = AfwServerAnimationController.findNearestFloor(world, candidateStart, 2);
            if (!candidateFloor.found || !AfwServerAnimationController.isGroundPlacementClear(world, candidateFloor.pos)) continue;
            return AfwServerAnimationController.withSharedPosition(sharedTransform, candidateFloor.pos);
        }
        return null;
    }

    private static SharedTransform withSharedPosition(SharedTransform sharedTransform, class_243 pos) {
        return new SharedTransform(pos, sharedTransform.yaw(), sharedTransform.pitch(), sharedTransform.headYaw(), sharedTransform.bodyYaw());
    }

    @Nullable
    private static SharedTransform findMountedPlayerDismountTransform(class_3218 world, List<UUID> actorUuids, SharedTransform base) {
        if (world == null || actorUuids == null || actorUuids.isEmpty() || base == null) {
            return null;
        }
        for (UUID actorUuid : actorUuids) {
            class_1297 vehicle;
            class_3222 player;
            class_1297 entity = world.method_66347(actorUuid);
            if (!(entity instanceof class_3222) || !(player = (class_3222)entity).method_5765() || (vehicle = player.method_5854()) == null || vehicle.method_31481()) continue;
            class_243 dismountPos = vehicle.method_24829((class_1309)player);
            if (dismountPos == null) {
                dismountPos = vehicle.method_73189();
            }
            return AfwServerAnimationController.withSharedPosition(base, dismountPos);
        }
        return null;
    }

    @Nullable
    private static class_243 resolveMountedPlayerRestorePosition(class_3222 player) {
        if (player == null || !player.method_5765()) {
            return null;
        }
        class_1297 vehicle = player.method_5854();
        if (vehicle == null || vehicle.method_31481()) {
            return null;
        }
        class_243 dismountPos = vehicle.method_24829((class_1309)player);
        if (dismountPos == null || vehicle.method_5829().method_1014(0.05).method_1006(dismountPos)) {
            class_238 box = vehicle.method_5829();
            dismountPos = new class_243(vehicle.method_23317(), box.field_1325 + 0.05, vehicle.method_23321());
        }
        return dismountPos;
    }

    @Nullable
    private static SharedTransform resolveQueuedPlacementTransform(class_3218 world, @Nullable UUID playerUuid, SharedTransform base, Map<String, String> metadata) {
        if (world == null || playerUuid == null || base == null) {
            return null;
        }
        if (metadata == null || !"true".equals(metadata.get(META_QUEUE_CONTINUE))) {
            return null;
        }
        DeferredQueueContext context = AfwServerAnimationController.getDeferredQueueContext(world, playerUuid);
        if (context == null || context.previousPlacement() == null) {
            return null;
        }
        BlockPlacement previousPlacement = context.previousPlacement();
        if (!AfwServerAnimationController.isCenterSupportPlacement(previousPlacement)) {
            SharedTransform previousTransform = previousPlacement.sharedTransform();
            return AfwServerAnimationController.isGroundPlacementClear(world, previousTransform.pos()) ? AfwServerAnimationController.withSharedPosition(base, previousTransform.pos()) : null;
        }
        double supportTopOffset = AfwServerAnimationController.supportSurfaceTopOffset(world, previousPlacement.wallPos());
        if (supportTopOffset <= 0.0) {
            return null;
        }
        class_243 topPos = new class_243((double)previousPlacement.wallPos().method_10263() + 0.5, (double)previousPlacement.wallPos().method_10264() + supportTopOffset, (double)previousPlacement.wallPos().method_10260() + 0.5);
        if (!AfwServerAnimationController.isGroundPlacementClear(world, topPos)) {
            return null;
        }
        return AfwServerAnimationController.withSharedPosition(base, topPos);
    }

    private static double supportSurfaceTopOffset(class_3218 world, class_2338 pos) {
        class_2680 state = world.method_8320(pos);
        if (!state.method_26227().method_15769()) {
            return -1.0;
        }
        class_265 shape = state.method_26220((class_1922)world, pos);
        if (shape.method_1110()) {
            return -1.0;
        }
        return shape.method_1105(class_2350.class_2351.field_11052);
    }

    private static boolean isGroundPlacementClear(class_3218 world, class_243 pos) {
        class_238 clearanceBox = new class_238(pos.field_1352 - 0.75, pos.field_1351 + 0.01, pos.field_1350 - 0.75, pos.field_1352 + 0.75, pos.field_1351 + 2.0, pos.field_1350 + 0.75);
        return world.method_18026(clearanceBox) && !AfwServerAnimationController.containsFluid(world, clearanceBox);
    }

    private static boolean containsFluid(class_3218 world, class_238 box) {
        int minX = class_3532.method_15357((double)box.field_1323);
        int maxX = class_3532.method_15357((double)(box.field_1320 - 1.0E-7));
        int minY = class_3532.method_15357((double)box.field_1322);
        int maxY = class_3532.method_15357((double)(box.field_1325 - 1.0E-7));
        int minZ = class_3532.method_15357((double)box.field_1321);
        int maxZ = class_3532.method_15357((double)(box.field_1324 - 1.0E-7));
        class_2338.class_2339 mutable = new class_2338.class_2339();
        for (int y = minY; y <= maxY; ++y) {
            for (int x = minX; x <= maxX; ++x) {
                for (int z = minZ; z <= maxZ; ++z) {
                    mutable.method_10103(x, y, z);
                    if (world.method_8316((class_2338)mutable).method_15769()) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static FloorHit findNearestFloor(class_3218 world, class_243 startPos, int maxDropBlocks) {
        class_2338 startBlock = class_2338.method_49638((class_2374)startPos);
        class_2338.class_2339 cursor = new class_2338.class_2339(startBlock.method_10263(), startBlock.method_10264(), startBlock.method_10260());
        int minY = Math.max(world.method_31607(), startBlock.method_10264() - maxDropBlocks);
        double maxFloorY = startPos.field_1351 + 0.001;
        for (int y = startBlock.method_10264(); y >= minY; --y) {
            double floorY;
            double top;
            class_265 shape;
            cursor.method_33098(y);
            class_2680 state = world.method_8320((class_2338)cursor);
            if (state.method_26215() || (shape = state.method_26220((class_1922)world, (class_2338)cursor)).method_1110() || (top = shape.method_1105(class_2350.class_2351.field_11052)) <= 0.0 || !((floorY = (double)y + top) <= maxFloorY)) continue;
            return new FloorHit(true, new class_243(startPos.field_1352, floorY, startPos.field_1350));
        }
        return new FloorHit(false, startPos);
    }

    private static List<GroundPlacementOffset> createGroundPlacementOffsets() {
        int steps = Math.max(1, class_3532.method_15384((double)4.0));
        double maxDistSq = 4.0;
        ArrayList<GroundPlacementOffset> offsets = new ArrayList<GroundPlacementOffset>();
        for (int ix = -steps; ix <= steps; ++ix) {
            for (int iz = -steps; iz <= steps; ++iz) {
                double dz;
                double dx;
                double distSq;
                if (ix == 0 && iz == 0 || (distSq = (dx = (double)ix * 0.5) * dx + (dz = (double)iz * 0.5) * dz) > maxDistSq) continue;
                offsets.add(new GroundPlacementOffset(dx, dz, distSq));
            }
        }
        offsets.sort(Comparator.comparingDouble(GroundPlacementOffset::distSq));
        return List.copyOf(offsets);
    }

    private static void restoreTransforms(ActiveInstance inst) {
        AfwServerAnimationController.restoreTransforms(inst, null);
    }

    private static void restoreTransforms(ActiveInstance inst, @Nullable Set<UUID> skipActors) {
        Map<UUID, OriginalTransform> originals = inst.originalTransforms;
        if (originals == null || originals.isEmpty()) {
            return;
        }
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(inst.world.method_27983());
        for (Map.Entry<UUID, OriginalTransform> entry : originals.entrySet()) {
            AiDisableState state;
            class_1297 class_12972;
            class_1297 e;
            OriginalTransform ot;
            if (skipActors != null && skipActors.contains(entry.getKey()) || (ot = entry.getValue()).worldKey() != inst.world.method_27983() || (e = inst.world.method_66347(entry.getKey())) == null) continue;
            Objects.requireNonNull(e);
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{class_3222.class, class_1309.class}, (Object)class_12972, n)) {
                case 0: {
                    class_3222 player = (class_3222)class_12972;
                    AfwServerAnimationController.preparePlayerForAnimationTeleport(player);
                    class_243 restorePos = ot.restorePosOrPos();
                    player.field_13987.method_14363(restorePos.field_1352, restorePos.field_1351, restorePos.field_1350, player.method_36454(), player.method_36455());
                    AfwServerAnimationController.clearDeferredQueueOriginal(inst.world, entry.getKey());
                    break;
                }
                case 1: {
                    class_1309 living = (class_1309)class_12972;
                    living.method_5808(ot.pos().field_1352, ot.pos().field_1351, ot.pos().field_1350, ot.yaw(), ot.pitch());
                    living.method_5847(ot.headYaw());
                    living.method_5636(ot.bodyYaw());
                    break;
                }
                default: {
                    e.method_5808(ot.pos().field_1352, ot.pos().field_1351, ot.pos().field_1350, ot.yaw(), ot.pitch());
                }
            }
            e.method_18799(class_243.field_1353);
            e.field_6017 = 0.0;
            if (!(e instanceof class_1308) || states == null || (state = states.get(entry.getKey())) == null) continue;
            state.lockedYaw = ot.yaw();
            state.lockedHeadYaw = ot.headYaw();
            state.lockedBodyYaw = ot.bodyYaw();
            state.lockedPitch = ot.pitch();
        }
    }

    @Nullable
    private static OriginalTransform getOriginalTransformForActor(ActiveInstance inst, UUID actorUuid) {
        if (inst == null || actorUuid == null || inst.originalTransforms == null) {
            return null;
        }
        return inst.originalTransforms.get(actorUuid);
    }

    private static boolean startNextQueuedForPlayerDeferringRestore(class_3218 world, ActiveInstance inst, @Nullable UUID ignoreInstanceId) {
        if (world == null || inst == null || inst.playerUuid == null) {
            return false;
        }
        boolean inserted = AfwServerAnimationController.captureDeferredQueueContext(world, inst.playerUuid, inst);
        boolean started = AfwServerAnimationController.startNextQueuedForPlayer(world, inst.playerUuid, ignoreInstanceId, AfwServerAnimationController.resolveTransitionYaw(inst));
        if (!started && inserted) {
            AfwServerAnimationController.clearDeferredQueueOriginal(world, inst.playerUuid);
        }
        return started;
    }

    private static boolean captureDeferredQueueContextFromActiveInstance(class_3218 world, UUID playerUuid) {
        ActiveInstance active = AfwServerAnimationController.findActiveInstanceForActors(world, List.of(playerUuid));
        return AfwServerAnimationController.captureDeferredQueueContext(world, playerUuid, active);
    }

    private static boolean captureDeferredQueueContext(class_3218 world, UUID playerUuid, @Nullable ActiveInstance inst) {
        DeferredQueueContext context;
        if (world == null || playerUuid == null || inst == null) {
            return false;
        }
        OriginalTransform original = AfwServerAnimationController.getOriginalTransformForActor(inst, playerUuid);
        if (original == null) {
            return false;
        }
        Map byPlayer = DEFERRED_QUEUE_CONTEXT_BY_WORLD.computeIfAbsent((class_5321<class_1937>)world.method_27983(), ignored -> new HashMap());
        return byPlayer.putIfAbsent(playerUuid, context = new DeferredQueueContext(original, AfwServerAnimationController.queueContinuationPlacementForInstance(inst))) == null;
    }

    private static Map<UUID, OriginalTransform> applyDeferredQueueOriginal(class_3218 world, @Nullable UUID playerUuid, Map<UUID, OriginalTransform> originals, Map<String, String> metadata) {
        if (world == null || playerUuid == null || originals == null || originals.isEmpty()) {
            return originals;
        }
        if (metadata == null || !"true".equals(metadata.get(META_QUEUE_CONTINUE))) {
            return originals;
        }
        OriginalTransform deferred = AfwServerAnimationController.getDeferredQueueOriginal(world, playerUuid);
        if (deferred == null) {
            return originals;
        }
        HashMap<UUID, OriginalTransform> carried = new HashMap<UUID, OriginalTransform>(originals);
        carried.put(playerUuid, deferred);
        return carried;
    }

    @Nullable
    private static OriginalTransform getDeferredQueueOriginal(class_3218 world, UUID playerUuid) {
        DeferredQueueContext context = AfwServerAnimationController.getDeferredQueueContext(world, playerUuid);
        return context == null ? null : context.originalTransform();
    }

    private static void clearDeferredQueueOriginal(class_3218 world, UUID playerUuid) {
        Map<UUID, DeferredQueueContext> byPlayer = DEFERRED_QUEUE_CONTEXT_BY_WORLD.get(world.method_27983());
        if (byPlayer == null) {
            return;
        }
        byPlayer.remove(playerUuid);
        if (byPlayer.isEmpty()) {
            DEFERRED_QUEUE_CONTEXT_BY_WORLD.remove(world.method_27983());
        }
    }

    @Nullable
    private static DeferredQueueContext getDeferredQueueContext(class_3218 world, UUID playerUuid) {
        Map<UUID, DeferredQueueContext> byPlayer = DEFERRED_QUEUE_CONTEXT_BY_WORLD.get(world.method_27983());
        return byPlayer == null ? null : byPlayer.get(playerUuid);
    }

    @Nullable
    private static BlockPlacement queueContinuationPlacementForInstance(ActiveInstance inst) {
        if (inst == null || inst.blockPlacement == null) {
            return null;
        }
        return inst.blockPlacement;
    }

    private static boolean isCenterSupportPlacement(BlockPlacement placement) {
        return placement != null && placement.centerSupport();
    }

    private static void disableAiForActors(class_3218 world, List<UUID> actorUuids) {
        if (actorUuids.isEmpty()) {
            return;
        }
        Map states = AI_DISABLE_STATE_BY_WORLD.computeIfAbsent((class_5321<class_1937>)world.method_27983(), k -> new HashMap());
        for (UUID uuid : actorUuids) {
            class_1297 e = world.method_66347(uuid);
            if (!(e instanceof class_1308)) continue;
            class_1308 mob = (class_1308)e;
            AiDisableState state = (AiDisableState)states.get(uuid);
            if (state == null) {
                boolean originalAiDisabled = mob.method_5987();
                state = new AiDisableState(originalAiDisabled);
                states.put(uuid, state);
            } else {
                ++state.locks;
                state.needsRestore = false;
            }
            AfwServerAnimationController.captureOrientation(mob, state);
            AfwServerAnimationController.applyNoAiTags(mob, state);
            mob.method_5977(true);
            mob.method_5942().method_6340();
            mob.method_18799(class_243.field_1353);
            mob.field_6017 = 0.0;
            mob.method_36456(state.lockedYaw);
            mob.method_36457(state.lockedPitch);
            mob.method_5847(state.lockedHeadYaw);
            mob.method_5636(state.lockedBodyYaw);
        }
    }

    private static void restoreAiForActors(class_3218 world, List<UUID> actorUuids) {
        if (actorUuids.isEmpty()) {
            return;
        }
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(world.method_27983());
        if (states == null) {
            return;
        }
        for (UUID uuid : actorUuids) {
            AiDisableState state = states.get(uuid);
            if (state == null) continue;
            if (state.locks > 1) {
                --state.locks;
                continue;
            }
            state.locks = 0;
            state.needsRestore = true;
            AfwServerAnimationController.tryRestoreAi(world, uuid, state, states);
        }
        if (states.isEmpty()) {
            AI_DISABLE_STATE_BY_WORLD.remove(world.method_27983());
        }
    }

    private static void tryRestoreAi(class_3218 world, UUID actorUuid, AiDisableState state, Map<UUID, AiDisableState> states) {
        class_1297 e = world.method_66347(actorUuid);
        if (e instanceof class_1308) {
            class_1308 mob = (class_1308)e;
            AfwServerAnimationController.restoreAiState(mob, state);
            states.remove(actorUuid);
        } else if (e != null) {
            states.remove(actorUuid);
        }
    }

    private static void processPendingAiRestores(class_3218 world) {
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(world.method_27983());
        if (states == null || states.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<UUID, AiDisableState>> it = states.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, AiDisableState> entry = it.next();
            AiDisableState state = entry.getValue();
            if (state.locks > 0 || !state.needsRestore) continue;
            class_1297 e = world.method_66347(entry.getKey());
            if (e instanceof class_1308) {
                class_1308 mob = (class_1308)e;
                AfwServerAnimationController.restoreAiState(mob, state);
                it.remove();
                continue;
            }
            if (e == null) continue;
            it.remove();
        }
        if (states.isEmpty()) {
            AI_DISABLE_STATE_BY_WORLD.remove(world.method_27983());
        }
    }

    private static void freezeActiveActors(class_3218 world, Set<UUID> actorUuids) {
        if (actorUuids.isEmpty()) {
            return;
        }
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(world.method_27983());
        if (states == null || states.isEmpty()) {
            return;
        }
        for (UUID uuid : actorUuids) {
            class_1297 e;
            AiDisableState state = states.get(uuid);
            if (state == null || !((e = world.method_66347(uuid)) instanceof class_1308)) continue;
            class_1308 mob = (class_1308)e;
            AfwServerAnimationController.freezeMobDuringAnimation(mob, state);
        }
    }

    private static void captureOrientation(class_1308 mob, AiDisableState state) {
        state.lockedYaw = mob.method_36454();
        state.lockedHeadYaw = mob.method_5791();
        state.lockedBodyYaw = mob.method_73188();
        state.lockedPitch = mob.method_36455();
    }

    private static void freezeMobDuringAnimation(class_1308 mob, AiDisableState state) {
        mob.method_18799(class_243.field_1353);
        mob.field_6017 = 0.0;
        mob.method_36456(state.lockedYaw);
        mob.method_36457(state.lockedPitch);
        mob.method_5847(state.lockedHeadYaw);
        mob.method_5636(state.lockedBodyYaw);
    }

    private static void applyNoAiTags(class_1308 mob, AiDisableState state) {
        mob.method_5780(AFW_NOAI_TAG);
        if (state.originalAiDisabled) {
            mob.method_5780(AFW_NOAI_ORIG_TAG);
        } else {
            mob.method_5738(AFW_NOAI_ORIG_TAG);
        }
    }

    public static void sendActiveInstancesToPlayer(class_3222 player) {
        class_3218 class_32182 = player.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            return;
        }
        class_3218 world = class_32182;
        for (Map.Entry entry : ACTIVE_INSTANCES.entrySet()) {
            ActiveInstance inst = (ActiveInstance)entry.getValue();
            if (inst.world != world) continue;
            List<String> safeKeys = AfwServerAnimationController.sanitizeActorKeys(inst.actorKeys, inst.actorUuids.size());
            List<AnimationStageInfo> safeStages = inst.stages == null ? List.of() : List.copyOf(inst.stages);
            StartAnimationS2CPayload payload = new StartAnimationS2CPayload(inst.animationId, (UUID)entry.getKey(), inst.actorUuids, safeKeys, safeStages, inst.startTick, inst.speed, inst.lockOrientation, inst.lockedYaw, inst.lockedHeadYaw, inst.lockedPitch, inst.cameraOrbitTarget);
            ServerPlayNetworking.send((class_3222)player, (class_8710)payload);
            inst.subscribedPlayerUuids.add(player.method_5667());
        }
    }

    private static void tickPlayerQueues(class_3218 world) {
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.get(world.method_27983());
        if (queues == null || queues.isEmpty()) {
            return;
        }
        ArrayList<UUID> removePlayers = new ArrayList<UUID>();
        for (Map.Entry<UUID, PlayerQueueState> entry : queues.entrySet()) {
            UUID playerUuid = entry.getKey();
            class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(playerUuid);
            if (player == null || player.method_51469() != world || !player.method_5805() || player.method_31481()) {
                removePlayers.add(playerUuid);
                continue;
            }
            if (AfwServerAnimationController.isActorActiveIgnoringInstance(world, playerUuid, null)) continue;
            AfwServerAnimationController.startNextQueuedForPlayer(world, playerUuid, null, null);
            PlayerQueueState state = queues.get(playerUuid);
            if (state != null && !state.entries.isEmpty()) continue;
            removePlayers.add(playerUuid);
        }
        for (UUID playerUuid : removePlayers) {
            AfwServerAnimationController.clearPlayerQueueByUuid(world, playerUuid, null, null);
        }
    }

    @Nullable
    private static UUID inferSinglePlayerUuid(class_3218 world, List<? extends class_1297> actors) {
        UUID found = null;
        for (class_1297 class_12972 : actors) {
            class_3222 player;
            if (!(class_12972 instanceof class_3222) || (player = (class_3222)class_12972).method_51469() != world) continue;
            if (found != null && !found.equals(player.method_5667())) {
                return null;
            }
            found = player.method_5667();
        }
        return found;
    }

    @Nullable
    private static UUID resolvePlayerUuid(class_3218 world, List<UUID> actorUuids) {
        if (actorUuids == null || actorUuids.isEmpty()) {
            return null;
        }
        UUID found = null;
        for (UUID uuid : actorUuids) {
            class_1297 entity = world.method_66347(uuid);
            if (!(entity instanceof class_3222)) continue;
            if (found != null && !found.equals(uuid)) {
                return null;
            }
            found = uuid;
        }
        return found;
    }

    private static void insertQueueEntry(Deque<QueuedEntry> entries, QueuedEntry entry, int insertIndex) {
        ArrayList<QueuedEntry> list = new ArrayList<QueuedEntry>(entries);
        int pos = AfwServerAnimationController.resolveInsertPosition(list.size(), insertIndex);
        list.add(pos, entry);
        entries.clear();
        entries.addAll(list);
    }

    private static int resolveInsertPosition(int size, int insertIndex) {
        if (insertIndex == 0) {
            return size;
        }
        if (insertIndex > 0) {
            return Math.max(0, Math.min(size, insertIndex - 1));
        }
        int fromEnd = size + insertIndex + 1;
        return Math.max(0, Math.min(size, fromEnd));
    }

    private static int describeQueuePosition(Deque<QueuedEntry> entries, UUID entryId) {
        int idx = 1;
        for (QueuedEntry entry : entries) {
            if (entry.entryId.equals(entryId)) {
                return idx;
            }
            ++idx;
        }
        return Math.max(1, entries.size());
    }

    private static String describeActorForDebug(@Nullable class_3218 world, @Nullable UUID actorUuid) {
        class_3222 player;
        if (actorUuid == null || world == null) {
            return "(unknown)";
        }
        class_1297 entity = world.method_66347(actorUuid);
        if (entity == null && world.method_8503() != null && (player = world.method_8503().method_3760().method_14602(actorUuid)) != null && player.method_51469() == world) {
            entity = player;
        }
        if (entity == null) {
            String uuid = actorUuid.toString();
            return "missing:" + uuid.substring(0, 8);
        }
        String display = entity.method_5476().getString();
        class_2960 typeId = class_7923.field_41177.method_10221((Object)entity.method_5864());
        if (display == null || display.isBlank()) {
            return typeId.method_12832();
        }
        return display;
    }

    private static String describeActorsForDebug(@Nullable class_3218 world, @Nullable List<UUID> actorUuids) {
        if (actorUuids == null || actorUuids.isEmpty()) {
            return "(none)";
        }
        ArrayList<String> labels = new ArrayList<String>();
        for (UUID actorUuid : actorUuids) {
            if (actorUuid == null) continue;
            labels.add(AfwServerAnimationController.describeActorForDebug(world, actorUuid));
        }
        if (labels.isEmpty()) {
            return "(none)";
        }
        int cap = 4;
        if (labels.size() <= cap) {
            return String.join((CharSequence)", ", labels);
        }
        return String.join((CharSequence)", ", labels.subList(0, cap)) + " +" + (labels.size() - cap) + " more";
    }

    private static String describeAnimationIdForDebug(@Nullable class_2960 animationId) {
        if (animationId == null) {
            return "(unknown)";
        }
        return animationId.method_12832();
    }

    private static boolean isActorActiveIgnoringInstance(class_3218 world, UUID actorUuid, @Nullable UUID ignoreInstanceId) {
        for (Map.Entry<UUID, ActiveInstance> entry : ACTIVE_INSTANCES.entrySet()) {
            if (ignoreInstanceId != null && ignoreInstanceId.equals(entry.getKey())) continue;
            ActiveInstance inst = entry.getValue();
            if (inst.world != world || !inst.actorUuidSet.contains(actorUuid)) continue;
            return true;
        }
        return false;
    }

    private static boolean startNextQueuedForPlayer(class_3218 world, UUID playerUuid, @Nullable UUID ignoreInstanceId, @Nullable Float transitionYaw) {
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.get(world.method_27983());
        if (queues == null) {
            return false;
        }
        PlayerQueueState queue = queues.get(playerUuid);
        if (queue == null || queue.entries.isEmpty()) {
            return false;
        }
        while (!queue.entries.isEmpty()) {
            QueuedEntry entry = queue.entries.peekFirst();
            if (entry == null) {
                queue.entries.pollFirst();
                continue;
            }
            if (AfwServerAnimationController.isActorActiveIgnoringInstance(world, playerUuid, ignoreInstanceId)) {
                return false;
            }
            QueueStartResult result = AfwServerAnimationController.tryStartQueuedEntry(world, queue, entry, transitionYaw);
            if (result == QueueStartResult.RETRY) {
                return false;
            }
            queue.entries.pollFirst();
            AfwServerAnimationController.clearQueuedMobMappings(world, queue.playerUuid, entry);
            if (result != QueueStartResult.STARTED) continue;
            if (queue.entries.isEmpty()) {
                queues.remove(queue.playerUuid);
            }
            return true;
        }
        queues.remove(queue.playerUuid);
        return false;
    }

    private static QueueStartResult tryStartQueuedEntry(class_3218 world, PlayerQueueState queue, QueuedEntry entry, @Nullable Float transitionYaw) {
        class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(queue.playerUuid);
        if (player == null || player.method_51469() != world || !player.method_5805() || player.method_31481()) {
            AfwServerAnimationController.sendOrLogDebug(world, null, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_skipped_player_unavailable", AfwServerAnimationController.describeAnimationIdForDebug(entry.animationId), AfwServerAnimationController.describeActorsForDebug(world, entry.actorUuids)), false);
            return QueueStartResult.SKIP;
        }
        ArrayList<Object> resolvedActors = new ArrayList<Object>();
        boolean missingNonPlayer = false;
        for (UUID actorUuid : entry.actorUuids) {
            class_1309 living;
            if (actorUuid.equals(queue.playerUuid)) {
                resolvedActors.add(player);
                continue;
            }
            class_1297 entity = world.method_66347(actorUuid);
            if (!(entity instanceof class_1309) || !(living = (class_1309)entity).method_5805() || living.method_31481()) {
                missingNonPlayer = true;
                break;
            }
            resolvedActors.add(entity);
        }
        if (missingNonPlayer) {
            AfwServerAnimationController.sendOrLogDebug(world, player, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_skipped_missing_non_player_actor", AfwServerAnimationController.describeAnimationIdForDebug(entry.animationId), AfwServerAnimationController.describeActorsForDebug(world, entry.actorUuids)), false);
            return QueueStartResult.SKIP;
        }
        if (resolvedActors.isEmpty()) {
            AfwServerAnimationController.sendOrLogDebug(world, player, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_skipped_no_actors_resolved", AfwServerAnimationController.describeAnimationIdForDebug(entry.animationId), AfwServerAnimationController.describeActorsForDebug(world, entry.actorUuids)), false);
            return QueueStartResult.SKIP;
        }
        if (AfwServerAnimationController.hasAnimationStartProtection(world, player)) {
            return QueueStartResult.RETRY;
        }
        AfwAnimationDefinitions.Definition def = AfwAnimationDefinitions.getDefinition(entry.animationId);
        if (def == null) {
            AfwServerAnimationController.sendOrLogDebug(world, player, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_skipped_missing_definition", AfwServerAnimationController.describeAnimationIdForDebug(entry.animationId), AfwServerAnimationController.describeActorsForDebug(world, entry.actorUuids)), false);
            return QueueStartResult.SKIP;
        }
        ArrayList<class_1297> sorted = new ArrayList<class_1297>(resolvedActors);
        sorted.sort(Comparator.comparingInt(class_1297::method_5628));
        List<UUID> uuids = sorted.stream().map(class_1297::method_5667).toList();
        if (!AfwServerAnimationController.waterRequirementMet(world, def, uuids)) {
            AfwServerAnimationController.sendOrLogDebug(world, player, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_skipped_requirements_not_met", AfwServerAnimationController.describeAnimationIdForDebug(entry.animationId), AfwServerAnimationController.describeActorsForDebug(world, entry.actorUuids)), false);
            return QueueStartResult.SKIP;
        }
        List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(def, sorted, world.method_8409());
        LinkedHashMap<String, String> nextMetadata = new LinkedHashMap<String, String>(AfwServerAnimationController.sanitizeMetadata(entry.metadata));
        nextMetadata.put(META_QUEUE_OWNER, queue.playerUuid.toString());
        nextMetadata.put(META_QUEUE_CONTINUE, "true");
        nextMetadata.put(META_QUEUE_ENTRY_ID, entry.entryId.toString());
        nextMetadata.put(META_QUEUE_TRANSITION, "true");
        UUID instanceId = AfwServerAnimationController.startAndBroadcast(world, player, def.id(), uuids, actorKeys, def.stages(), entry.damageBehavior, entry.ignoreAttackers, nextMetadata, false, transitionYaw);
        if (instanceId == null) {
            AfwServerAnimationController.sendOrLogDebug(world, player, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_skipped_start_failed", AfwServerAnimationController.describeAnimationIdForDebug(entry.animationId), AfwServerAnimationController.describeActorsForDebug(world, entry.actorUuids)), false);
            return QueueStartResult.SKIP;
        }
        AfwServerAnimationController.sendOrLogDebug(world, player, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queued_animation_started", AfwServerAnimationController.describeAnimationIdForDebug(def.id()), AfwServerAnimationController.describeActorsForDebug(world, uuids)), false);
        return QueueStartResult.STARTED;
    }

    private static void clearQueuedMobMappings(class_3218 world, UUID playerUuid, QueuedEntry entry) {
        Map<UUID, UUID> byMob = QUEUED_MOB_TO_PLAYER_BY_WORLD.get(world.method_27983());
        if (byMob == null || byMob.isEmpty() || entry == null) {
            return;
        }
        for (UUID actorUuid : entry.actorUuids) {
            UUID owner;
            if (actorUuid == null || actorUuid.equals(playerUuid) || !playerUuid.equals(owner = byMob.get(actorUuid))) continue;
            byMob.remove(actorUuid);
        }
    }

    private static void clearPlayerQueueByUuid(class_3218 world, UUID playerUuid, @Nullable AfwDebugChatCategory category, @Nullable class_2561 debugMessage) {
        if (world == null || playerUuid == null) {
            return;
        }
        AfwServerAnimationController.clearDeferredQueueOriginal(world, playerUuid);
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.get(world.method_27983());
        if (queues == null) {
            return;
        }
        PlayerQueueState queue = queues.remove(playerUuid);
        if (queue == null) {
            return;
        }
        for (QueuedEntry entry : queue.entries) {
            AfwServerAnimationController.clearQueuedMobMappings(world, playerUuid, entry);
        }
        if (queues.isEmpty()) {
            PLAYER_QUEUES_BY_WORLD.remove(world.method_27983());
        }
        if (category != null && debugMessage != null) {
            class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(playerUuid);
            AfwServerAnimationController.sendOrLogDebug(world, player, category, debugMessage, false);
        }
    }

    private static void handleQueueOnStop(class_3218 world, ActiveInstance inst, StopReason reason) {
        if (world == null || inst == null || inst.playerUuid == null) {
            return;
        }
        switch (reason.ordinal()) {
            case 0: {
                boolean started = AfwServerAnimationController.startNextQueuedForPlayerDeferringRestore(world, inst, null);
                if (!started) break;
                AfwServerAnimationController.sendOrLogDebug(world, null, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_continued_for_player", new Object[0]), false);
                break;
            }
            case 3: {
                break;
            }
            case 1: 
            case 2: 
            case 4: {
                AfwServerAnimationController.clearPlayerQueueByUuid(world, inst.playerUuid, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_cleared", new Object[0]));
            }
        }
    }

    public static boolean isActorPendingOrActive(class_3218 world, UUID actorUuid) {
        if (AfwServerAnimationController.isActorActiveIgnoringInstance(world, actorUuid, null)) {
            return true;
        }
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.get(world.method_27983());
        if (queues == null || queues.isEmpty()) {
            return false;
        }
        if (queues.containsKey(actorUuid)) {
            return true;
        }
        for (PlayerQueueState queue : queues.values()) {
            for (QueuedEntry entry : queue.entries) {
                if (!entry.actorUuids.contains(actorUuid)) continue;
                return true;
            }
        }
        return false;
    }

    private static void tickNoAiCleanup(class_3218 world) {
        class_5321 key = world.method_27983();
        boolean startupWindowCleanup = AfwServerAnimationController.isWithinStartupNoAiCleanupWindow(world);
        if (!startupWindowCleanup && world.method_18456().isEmpty()) {
            return;
        }
        if (!startupWindowCleanup) {
            int ticksLeft = NOAI_CLEANUP_TICKS_BY_WORLD.getOrDefault(key, 0) - 1;
            if (ticksLeft > 0) {
                NOAI_CLEANUP_TICKS_BY_WORLD.put((class_5321<class_1937>)key, ticksLeft);
                return;
            }
            NOAI_CLEANUP_TICKS_BY_WORLD.put((class_5321<class_1937>)key, 6000);
        }
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(key);
        for (class_1297 entity : world.method_27909()) {
            AiDisableState state;
            class_1308 mob;
            if (!(entity instanceof class_1308) || !(mob = (class_1308)entity).method_5752().contains(AFW_NOAI_TAG) && !mob.method_5752().contains(AFW_NOAI_ORIG_TAG)) continue;
            UUID uuid = mob.method_5667();
            if (!startupWindowCleanup && AfwServerAnimationController.isActorActive(world, uuid)) continue;
            AiDisableState aiDisableState = state = states == null ? null : states.get(uuid);
            if (state != null) {
                state.locks = 0;
                state.needsRestore = true;
                AfwServerAnimationController.tryRestoreAi(world, uuid, state, states);
                continue;
            }
            boolean originalDisabled = mob.method_5752().contains(AFW_NOAI_ORIG_TAG);
            mob.method_5977(originalDisabled);
            AfwServerAnimationController.clearNoAiTags(mob);
        }
        if (states != null && states.isEmpty()) {
            AI_DISABLE_STATE_BY_WORLD.remove(key);
        }
    }

    private static boolean isWithinStartupNoAiCleanupWindow(class_3218 world) {
        if (world == null || world.method_8503() == null) {
            return false;
        }
        int now = world.method_8503().method_3780();
        return now <= STARTUP_NOAI_CLEANUP_UNTIL_TICK;
    }

    private static boolean isWithinStartupStartBlockWindow(class_3218 world) {
        if (world == null || world.method_8503() == null) {
            return false;
        }
        int now = world.method_8503().method_3780();
        return now <= STARTUP_START_BLOCK_UNTIL_TICK;
    }

    private static List<String> sanitizeActorKeys(List<String> actorKeys, int actorCount) {
        if (actorKeys == null || actorKeys.isEmpty()) {
            return List.of();
        }
        if (actorKeys.size() != actorCount) {
            return List.of();
        }
        return List.copyOf(actorKeys);
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

    public static boolean isActorActive(class_3218 world, UUID actorUuid) {
        for (ActiveInstance inst : ACTIVE_INSTANCES.values()) {
            if (inst.world != world || !inst.actorUuidSet.contains(actorUuid)) continue;
            return true;
        }
        return false;
    }

    private static void clearNoAiTags(class_1308 mob) {
        mob.method_5738(AFW_NOAI_TAG);
        mob.method_5738(AFW_NOAI_ORIG_TAG);
    }

    private static void restoreAiState(class_1308 mob, AiDisableState state) {
        mob.method_18799(class_243.field_1353);
        mob.field_6017 = 0.0;
        mob.method_5977(state.originalAiDisabled);
        AfwServerAnimationController.clearNoAiTags(mob);
    }

    private static void acquirePlayerLocks(class_3218 world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        Map states = PLAYER_LOCK_STATE_BY_WORLD.computeIfAbsent((class_5321<class_1937>)world.method_27983(), k -> new HashMap());
        for (UUID uuid : actorUuids) {
            class_1297 entity = world.method_66347(uuid);
            if (!(entity instanceof class_3222)) continue;
            class_3222 player = (class_3222)entity;
            PlayerLockState existing = (PlayerLockState)states.get(uuid);
            if (existing == null) {
                states.put(uuid, new PlayerLockState(player.method_5740()));
            } else {
                ++existing.locks;
            }
            player.method_5875(true);
            player.method_18799(class_243.field_1353);
            player.field_6017 = 0.0;
        }
    }

    private static void releasePlayerLocks(class_3218 world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        Map<UUID, PlayerLockState> states = PLAYER_LOCK_STATE_BY_WORLD.get(world.method_27983());
        if (states == null || states.isEmpty()) {
            return;
        }
        for (UUID uuid : actorUuids) {
            PlayerLockState state = states.get(uuid);
            if (state == null) continue;
            if (state.locks > 1) {
                --state.locks;
                continue;
            }
            class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(uuid);
            if (player != null) {
                player.method_5875(state.originalNoGravity);
                player.field_6017 = 0.0;
            }
            states.remove(uuid);
        }
        if (states.isEmpty()) {
            PLAYER_LOCK_STATE_BY_WORLD.remove(world.method_27983());
        }
    }

    private static void clearAllPlayerLocksInWorld(class_3218 world) {
        if (world == null) {
            return;
        }
        Map<UUID, PlayerLockState> states = PLAYER_LOCK_STATE_BY_WORLD.remove(world.method_27983());
        if (states == null || states.isEmpty()) {
            return;
        }
        for (Map.Entry<UUID, PlayerLockState> entry : states.entrySet()) {
            class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(entry.getKey());
            if (player == null) continue;
            player.method_5875(entry.getValue().originalNoGravity);
            player.field_6017 = 0.0;
        }
    }

    private static void cleanupStalePlayerLocks(class_3218 world, Set<UUID> currentlyLockedPlayers) {
        UUID uuid;
        if (world == null) {
            return;
        }
        Set<Object> lockedNow = currentlyLockedPlayers == null ? Set.of() : currentlyLockedPlayers;
        Map<UUID, PlayerLockState> states = PLAYER_LOCK_STATE_BY_WORLD.get(world.method_27983());
        if (states != null && !states.isEmpty()) {
            Iterator<Map.Entry<UUID, PlayerLockState>> it = states.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, PlayerLockState> entry = it.next();
                uuid = entry.getKey();
                if (lockedNow.contains(uuid) || AfwServerAnimationController.isActorActive(world, uuid)) continue;
                class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(uuid);
                if (player != null) {
                    player.method_5875(entry.getValue().originalNoGravity);
                    player.field_6017 = 0.0;
                }
                it.remove();
            }
            if (states.isEmpty()) {
                PLAYER_LOCK_STATE_BY_WORLD.remove(world.method_27983());
            }
        }
        for (class_3222 player : world.method_18456()) {
            uuid = player.method_5667();
            if (lockedNow.contains(uuid) || AfwServerAnimationController.isActorActive(world, uuid) || !player.method_5740() || player.method_7325() || player.method_31549().field_7479) continue;
            player.method_5875(false);
            player.field_6017 = 0.0;
        }
    }

    private static void lockActivePlayers(class_3218 world, Map<UUID, SharedTransform> playersToLock, Set<UUID> underwaterPlayers) {
        if (playersToLock.isEmpty()) {
            return;
        }
        for (Map.Entry<UUID, SharedTransform> entry : playersToLock.entrySet()) {
            class_1297 e = world.method_66347(entry.getKey());
            if (!(e instanceof class_3222)) continue;
            class_3222 player = (class_3222)e;
            AfwServerAnimationController.preparePlayerForAnimationTeleport(player);
            SharedTransform sharedTransform = entry.getValue();
            class_243 targetPos = sharedTransform.pos();
            double dx = player.method_23317() - targetPos.field_1352;
            double dy = player.method_23318() - targetPos.field_1351;
            double dz = player.method_23321() - targetPos.field_1350;
            if (dx * dx + dy * dy + dz * dz > 4.0E-4) {
                player.method_5814(targetPos.field_1352, targetPos.field_1351, targetPos.field_1350);
                player.field_13987.method_14363(targetPos.field_1352, targetPos.field_1351, targetPos.field_1350, player.method_36454(), player.method_36455());
            }
            player.method_5875(true);
            if (underwaterPlayers.contains(player.method_5667())) {
                player.method_5855(player.method_5748());
            }
            player.method_18799(class_243.field_1353);
            player.field_6017 = 0.0;
        }
    }

    private static void nudgeNonActiveMobsAwayFromAnimatedPlayers(class_3218 world, Set<UUID> activePlayerUuids, long nowTick) {
        if (world == null || activePlayerUuids == null || activePlayerUuids.isEmpty()) {
            return;
        }
        if (nowTick % 4L != 0L) {
            return;
        }
        block0: for (UUID playerUuid : activePlayerUuids) {
            class_238 searchBox;
            List nearby;
            class_3222 player = Objects.requireNonNull(world.method_8503()).method_3760().method_14602(playerUuid);
            if (player == null || player.method_51469() != world || !player.method_5805() || player.method_31481() || (nearby = world.method_8333((class_1297)player, searchBox = player.method_5829().method_1009(1.0, 0.5, 1.0), entity -> {
                class_1308 mob;
                return entity instanceof class_1308 && (mob = (class_1308)entity).method_5805() && !mob.method_31481() && !AfwServerAnimationController.isActorPendingOrActive(world, mob.method_5667());
            })).isEmpty()) continue;
            int nudged = 0;
            for (class_1297 entity2 : nearby) {
                class_1308 mob;
                if (!(entity2 instanceof class_1308) || AfwServerAnimationController.isActorPendingOrActive(world, (mob = (class_1308)entity2).method_5667())) continue;
                if (nudged >= 6) continue block0;
                if (!AfwServerAnimationController.nudgeMobAwayFromPlayer(player, mob)) continue;
                ++nudged;
            }
        }
    }

    private static boolean nudgeMobAwayFromPlayer(class_3222 player, class_1308 mob) {
        double minDistSq;
        double dz;
        double dx = mob.method_23317() - player.method_23317();
        double horizontalDistSq = dx * dx + (dz = mob.method_23321() - player.method_23321()) * dz;
        if (horizontalDistSq >= (minDistSq = 0.7224999999999999)) {
            return false;
        }
        class_243 dir = AfwServerAnimationController.resolveHorizontalNudgeDirection(mob, dx, dz, horizontalDistSq);
        double dist = Math.sqrt(Math.max(horizontalDistSq, 1.0E-6));
        double overlap = Math.max(0.0, 0.85 - dist);
        double strength = 0.08 * (0.6 + overlap / 0.85);
        mob.method_5762(dir.field_1352 * strength, 0.0, dir.field_1350 * strength);
        mob.field_6017 = 0.0;
        return true;
    }

    private static class_243 resolveHorizontalNudgeDirection(class_1308 mob, double dx, double dz, double horizontalDistSq) {
        if (horizontalDistSq > 1.0E-6) {
            double invLen = 1.0 / Math.sqrt(horizontalDistSq);
            return new class_243(dx * invLen, 0.0, dz * invLen);
        }
        long bits = mob.method_5667().getLeastSignificantBits() ^ mob.method_5667().getMostSignificantBits();
        double angle = (double)(bits & 0xFFFFL) * 9.587379924285257E-5;
        return new class_243(Math.cos(angle), 0.0, Math.sin(angle));
    }

    private static void clearAttackTargets(class_3218 world, Set<UUID> actorUuids) {
        if (actorUuids.isEmpty()) {
            return;
        }
        for (class_1297 entity : world.method_27909()) {
            class_1308 mob;
            class_1309 target;
            if (!(entity instanceof class_1308) || (target = (mob = (class_1308)entity).method_5968()) == null || !actorUuids.contains(target.method_5667())) continue;
            mob.method_5980(null);
            mob.method_5942().method_6340();
        }
    }

    private static double clampSpeed(double speed) {
        if (Double.isNaN(speed) || Double.isInfinite(speed) || speed <= 0.0) {
            return 1.0;
        }
        return Math.max(0.1, Math.min(speed, 4.0));
    }

    private static double resolveStageSpeed(List<AnimationStageInfo> stages, int index) {
        if (stages == null || stages.isEmpty()) {
            return 1.0;
        }
        int clamped = Math.max(0, Math.min(index, stages.size() - 1));
        double speed = stages.get(clamped).speed();
        if (Double.isNaN(speed) || Double.isInfinite(speed) || speed <= 0.0) {
            return 1.0;
        }
        return speed;
    }

    private static double sanitizeMultiplier(double multiplier) {
        if (Double.isNaN(multiplier) || Double.isInfinite(multiplier) || multiplier <= 0.0) {
            return 1.0;
        }
        return Math.max(0.1, Math.min(multiplier, 4.0));
    }

    private static boolean maybeAdvanceStages(class_3218 world, UUID instanceId, ActiveInstance inst, long now, List<StageAdvance> stageAdvances, List<UUID> toRemove) {
        List<AnimationStageInfo> stages = inst.stages;
        StageTracker tracker = inst.stageTracker;
        if (stages == null || stages.isEmpty() || tracker == null) {
            return false;
        }
        int idx = Math.max(0, Math.min(tracker.stageIndex, stages.size() - 1));
        long stageStart = tracker.stageStartTick;
        double speed = AfwServerAnimationController.clampSpeed(inst.speed);
        while (true) {
            AnimationStageInfo current = stages.get(idx);
            long duration = Math.max(0L, current.lengthTicks());
            if (current.loop() || duration == 0L) {
                return false;
            }
            long scaledDuration = (long)Math.ceil((double)duration / speed);
            long targetEnd = stageStart + scaledDuration;
            if (now < targetEnd) {
                return false;
            }
            if (idx + 1 >= stages.size()) {
                long stopTick = now + 1L;
                boolean chained = false;
                if (inst.playerUuid != null) {
                    chained = AfwServerAnimationController.startNextQueuedForPlayerDeferringRestore(world, inst, instanceId);
                }
                StopAnimationS2CPayload payload = new StopAnimationS2CPayload(instanceId, stopTick);
                Set<class_3222> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, null, inst);
                for (class_3222 p : targets) {
                    ServerPlayNetworking.send((class_3222)p, (class_8710)payload);
                }
                AfwServerAnimationController.fireStopEvent(instanceId, inst);
                AfwServerAnimationController.restoreTransforms(inst, chained && inst.playerUuid != null ? Set.of(inst.playerUuid) : null);
                AfwServerAnimationController.restoreAiForActors(world, inst.actorUuids);
                AfwServerAnimationController.releasePlayerLocks(world, inst.actorUuids);
                toRemove.add(instanceId);
                if (chained) {
                    AfwServerAnimationController.sendOrLogDebug(world, null, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_continued_for_player", new Object[0]), false);
                }
                return true;
            }
            stageStart = targetEnd;
            tracker.stageIndex = ++idx;
            tracker.stageStartTick = stageStart;
            inst.speed = AfwServerAnimationController.clampSpeed(AfwServerAnimationController.resolveStageSpeed(stages, idx));
            speed = AfwServerAnimationController.clampSpeed(inst.speed);
            stageAdvances.add(new StageAdvance(instanceId, stageStart, idx));
        }
    }

    private static AnimationStageInfo resolveCurrentStage(ActiveInstance inst) {
        if (inst.stages == null || inst.stages.isEmpty()) {
            return new AnimationStageInfo(inst.animationId, true, 0L, true, 1.0);
        }
        int idx = inst.stageTracker == null ? 0 : Math.max(0, Math.min(inst.stageTracker.stageIndex, inst.stages.size() - 1));
        return inst.stages.get(idx);
    }

    @Nullable
    public static AnimationStageInfo getCurrentStage(class_3218 world, UUID instanceId) {
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return null;
        }
        return AfwServerAnimationController.resolveCurrentStage(inst);
    }

    @Nullable
    public static ActiveInstanceSnapshot getActiveInstanceSnapshot(class_3218 world, UUID instanceId) {
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return null;
        }
        return new ActiveInstanceSnapshot(inst.animationId, List.copyOf(inst.actorUuids), inst.damageBehavior, inst.ignoreAttackers, Map.copyOf(inst.metadata));
    }

    @Nullable
    private static Float resolveTransitionYaw(@Nullable ActiveInstance inst) {
        if (inst == null || inst.sharedTransform == null) {
            return null;
        }
        return Float.valueOf(inst.sharedTransform.yaw());
    }

    public static Map<String, String> getInstanceMetadata(class_3218 world, UUID instanceId) {
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return Map.of();
        }
        return inst.metadata;
    }

    private record BlockPlacement(class_2338 wallPos, class_2350 facing, SharedTransform sharedTransform, @Nullable class_243 cameraOrbitTarget, boolean centerSupport) {
    }

    private record EligibleDefinition(AfwAnimationDefinitions.Definition definition, List<String> actorKeys, @Nullable BlockPlacement blockPlacement) {
    }

    public record MatchedStartRequest(@Nullable UUID instanceId, class_2960 animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
    }

    private record RequirementCheck(boolean valid, @Nullable BlockPlacement blockPlacement) {
    }

    private record AnimationStartProtection(class_5321<class_1937> worldKey, long untilTick) {
    }

    private static final class PlayerQueueState {
        final UUID playerUuid;
        final Deque<QueuedEntry> entries = new ArrayDeque<QueuedEntry>();

        PlayerQueueState(UUID playerUuid) {
            this.playerUuid = playerUuid;
        }
    }

    private record QueuedEntry(UUID entryId, class_2960 animationId, List<UUID> actorUuids, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata) {
    }

    private static final class ActiveInstance {
        final class_3218 world;
        final class_2960 animationId;
        final List<UUID> actorUuids;
        final List<String> actorKeys;
        final List<AnimationStageInfo> stages;
        final long startTick;
        final Map<UUID, OriginalTransform> originalTransforms;
        final SharedTransform sharedTransform;
        final boolean lockOrientation;
        final float lockedYaw;
        final float lockedHeadYaw;
        final float lockedPitch;
        final AfwDamageBehavior damageBehavior;
        final boolean ignoreAttackers;
        final Map<String, String> metadata;
        final boolean underwaterBreathing;
        @Nullable
        final UUID playerUuid;
        final Set<UUID> actorUuidSet;
        final StageTracker stageTracker;
        @Nullable
        final BlockPlacement blockPlacement;
        @Nullable
        final class_243 cameraOrbitTarget;
        final Set<UUID> subscribedPlayerUuids = ConcurrentHashMap.newKeySet();
        double speed;

        ActiveInstance(class_3218 world, class_2960 animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, long startTick, Map<UUID, OriginalTransform> originalTransforms, SharedTransform sharedTransform, boolean lockOrientation, float lockedYaw, float lockedHeadYaw, float lockedPitch, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata, boolean underwaterBreathing, @Nullable UUID playerUuid, Set<UUID> actorUuidSet, StageTracker stageTracker, @Nullable BlockPlacement blockPlacement, @Nullable class_243 cameraOrbitTarget, double speed) {
            this.world = world;
            this.animationId = animationId;
            this.actorUuids = actorUuids;
            this.actorKeys = actorKeys;
            this.stages = stages;
            this.startTick = startTick;
            this.originalTransforms = originalTransforms;
            this.sharedTransform = sharedTransform;
            this.lockOrientation = lockOrientation;
            this.lockedYaw = lockedYaw;
            this.lockedHeadYaw = lockedHeadYaw;
            this.lockedPitch = lockedPitch;
            this.damageBehavior = damageBehavior;
            this.ignoreAttackers = ignoreAttackers;
            this.metadata = metadata;
            this.underwaterBreathing = underwaterBreathing;
            this.playerUuid = playerUuid;
            this.actorUuidSet = actorUuidSet;
            this.stageTracker = stageTracker;
            this.blockPlacement = blockPlacement;
            this.cameraOrbitTarget = cameraOrbitTarget;
            this.speed = speed;
        }
    }

    private static enum StopReason {
        NATURAL_END,
        EXTERNAL_STOP,
        DAMAGE_STOP,
        JOIN_REPLACE,
        STOP_ALL;

    }

    private static final class StageTracker {
        int stageIndex = 0;
        long stageStartTick;

        StageTracker(long stageStartTick) {
            this.stageStartTick = stageStartTick;
        }
    }

    private record SharedTransform(class_243 pos, float yaw, float pitch, float headYaw, float bodyYaw) {
    }

    private record RecentNoRestoreTransition(long expiresTick, SharedTransform sharedTransform, @Nullable BlockPlacement blockPlacement) {
    }

    private record StageAdvance(UUID instanceId, long advanceTick, int stageIndex) {
    }

    private record OriginalTransform(class_5321<class_1937> worldKey, class_243 pos, float yaw, float pitch, float headYaw, float bodyYaw, @Nullable class_243 restorePos) {
        private OriginalTransform(class_5321<class_1937> worldKey, class_243 pos, float yaw, float pitch, float headYaw, float bodyYaw) {
            this(worldKey, pos, yaw, pitch, headYaw, bodyYaw, null);
        }

        private class_243 restorePosOrPos() {
            return this.restorePos == null ? this.pos : this.restorePos;
        }
    }

    private record BedFootprintFacing(boolean bedFootprint, @Nullable class_2350 facing) {
        static BedFootprintFacing none() {
            return new BedFootprintFacing(false, null);
        }

        static BedFootprintFacing invalid() {
            return new BedFootprintFacing(true, null);
        }

        static BedFootprintFacing valid(class_2350 facing) {
            return new BedFootprintFacing(true, facing);
        }
    }

    private static final class AiDisableState {
        final boolean originalAiDisabled;
        int locks;
        boolean needsRestore;
        float lockedYaw;
        float lockedHeadYaw;
        float lockedBodyYaw;
        float lockedPitch;

        AiDisableState(boolean originalAiDisabled) {
            this.originalAiDisabled = originalAiDisabled;
            this.locks = 1;
            this.needsRestore = false;
            this.lockedYaw = 0.0f;
            this.lockedHeadYaw = 0.0f;
            this.lockedBodyYaw = 0.0f;
            this.lockedPitch = 0.0f;
        }
    }

    private record FloorHit(boolean found, class_243 pos) {
    }

    private record GroundPlacementOffset(double dx, double dz, double distSq) {
    }

    private record DeferredQueueContext(OriginalTransform originalTransform, @Nullable BlockPlacement previousPlacement) {
    }

    private static enum QueueStartResult {
        STARTED,
        RETRY,
        SKIP;

    }

    private static final class PlayerLockState {
        final boolean originalNoGravity;
        int locks;

        PlayerLockState(boolean originalNoGravity) {
            this.originalNoGravity = originalNoGravity;
            this.locks = 1;
        }
    }

    public record ActiveInstanceSnapshot(class_2960 animationId, List<UUID> actorUuids, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata) {
    }
}

