/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
 *  net.fabricmc.fabric.api.networking.v1.PlayerLookup
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.entity.damage.DamageSource
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.world.BlockView
 *  net.minecraft.world.World
 *  net.minecraft.block.BedBlock
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$Mutable
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Direction$Axis
 *  net.minecraft.util.math.Position
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.block.SlabBlock
 *  net.minecraft.text.Text
 *  net.minecraft.util.shape.VoxelShape
 *  net.minecraft.block.BlockState
 *  net.minecraft.block.enums.BedPart
 *  net.minecraft.state.property.Property
 *  net.minecraft.block.enums.SlabType
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.registry.tag.BlockTags
 *  net.minecraft.registry.tag.FluidTags
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.registry.tag.TagKey
 *  net.minecraft.registry.Registries
 *  net.minecraft.registry.RegistryKeys
 *  net.minecraft.entity.damage.DamageTypes
 *  net.minecraft.network.packet.CustomPayload
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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.block.BedBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.SlabBlock;
import net.minecraft.text.Text;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.state.property.Property;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.network.packet.CustomPayload;
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
    private static final Identifier BEDS_BLOCK_TAG_ID = Identifier.of((String)"minecraft", (String)"beds");
    private static final Map<RegistryKey<World>, Map<UUID, Long>> IN_WALL_GRACE_BY_WORLD = new HashMap<RegistryKey<World>, Map<UUID, Long>>();
    private static final Map<RegistryKey<World>, Map<UUID, RecentNoRestoreTransition>> RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD = new HashMap<RegistryKey<World>, Map<UUID, RecentNoRestoreTransition>>();
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
    private static final Map<RegistryKey<World>, Map<UUID, PlayerQueueState>> PLAYER_QUEUES_BY_WORLD = new HashMap<RegistryKey<World>, Map<UUID, PlayerQueueState>>();
    private static final Map<RegistryKey<World>, Map<UUID, UUID>> QUEUED_MOB_TO_PLAYER_BY_WORLD = new HashMap<RegistryKey<World>, Map<UUID, UUID>>();
    private static final Map<RegistryKey<World>, Map<UUID, DeferredQueueContext>> DEFERRED_QUEUE_CONTEXT_BY_WORLD = new HashMap<RegistryKey<World>, Map<UUID, DeferredQueueContext>>();
    private static final Map<RegistryKey<World>, Map<UUID, AiDisableState>> AI_DISABLE_STATE_BY_WORLD = new HashMap<RegistryKey<World>, Map<UUID, AiDisableState>>();
    private static final Map<RegistryKey<World>, Integer> NOAI_CLEANUP_TICKS_BY_WORLD = new HashMap<RegistryKey<World>, Integer>();
    private static final Map<RegistryKey<World>, Map<UUID, PlayerLockState>> PLAYER_LOCK_STATE_BY_WORLD = new HashMap<RegistryKey<World>, Map<UUID, PlayerLockState>>();
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
                AfwAnimationDefinitions.reloadFromServerResourceManager(server.getResourceManager(), "server started with empty definition cache");
            }
            AI_DISABLE_STATE_BY_WORLD.clear();
            NOAI_CLEANUP_TICKS_BY_WORLD.clear();
            STARTUP_NOAI_CLEANUP_UNTIL_TICK = server.getTicks() + 20;
            STARTUP_START_BLOCK_UNTIL_TICK = server.getTicks() + 40;
            PLAYER_LOCK_STATE_BY_WORLD.clear();
            PLAYER_QUEUES_BY_WORLD.clear();
            QUEUED_MOB_TO_PLAYER_BY_WORLD.clear();
            DEFERRED_QUEUE_CONTEXT_BY_WORLD.clear();
            ANIMATION_START_PROTECTION.clear();
            RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.clear();
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> server.execute(() -> {
            ServerPlayerEntity player = handler.player;
            if (player == null) {
                return;
            }
            ServerWorld patt0$temp = player.getEntityWorld();
            if (!(patt0$temp instanceof ServerWorld)) {
                return;
            }
            ServerWorld world = patt0$temp;
            AfwServerAnimationController.stopActiveInstancesForDisconnectingPlayer(world, player.getUuid());
            AfwServerAnimationController.releasePlayerLocks(world, List.of(player.getUuid()));
            AfwServerAnimationController.clearPlayerQueue(world, player);
            AfwServerAnimationController.clearDeferredQueueOriginal(world, player.getUuid());
            ANIMATION_START_PROTECTION.remove(player.getUuid());
            AfwServerAnimationController.removePlayerFromInstanceSubscribers(player.getUuid());
        }));
        ServerTickEvents.END_WORLD_TICK.register(AfwServerAnimationController::tickWorld);
    }

    @Nullable
    public static UUID startNow(ServerWorld world, @Nullable ServerPlayerEntity requester, Identifier animationId, List<Entity> actorsSorted, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable UUID positionAnchorUuid, @Nullable Map<String, String> metadata, boolean forceChat) {
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
        ArrayList<Entity> actors = new ArrayList<Entity>(actorsSorted.size());
        actors.addAll(actorsSorted);
        actors.sort(Comparator.comparingInt(Entity::getId));
        int players = 0;
        ArrayList<UUID> actorUuids = new ArrayList<UUID>(actors.size());
        for (Entity e : actors) {
            if (e == null || e.getEntityWorld() != world) {
                return null;
            }
            if (e instanceof PlayerEntity) {
                ++players;
            } else if (!(e instanceof MobEntity)) {
                AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("matched_unpathable_actor", animationId), forceChat);
                return null;
            }
            actorUuids.add(e.getUuid());
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
    public static MatchedStartRequest startEligibleMatchedNow(ServerWorld world, @Nullable ServerPlayerEntity requester, List<? extends Entity> actorsSorted, @Nullable Set<String> requiredAnimationTags, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable UUID positionAnchorUuid, @Nullable Map<String, String> metadata, boolean forceChat) {
        if (world == null || actorsSorted == null || actorsSorted.isEmpty()) {
            return null;
        }
        ArrayList<Entity> actors = new ArrayList<Entity>(actorsSorted.size());
        actors.addAll(actorsSorted);
        actors.sort(Comparator.comparingInt(Entity::getId));
        EligibleDefinition eligible = AfwServerAnimationController.selectEligibleDefinition(world, actors, requiredAnimationTags == null ? Set.of() : requiredAnimationTags, positionAnchorUuid);
        if (eligible == null) {
            return null;
        }
        UUID instanceId = AfwServerAnimationController.startNow(world, requester, eligible.definition().id(), actors, eligible.actorKeys(), eligible.definition().stages(), damageBehavior, ignoreAttackers, positionAnchorUuid, AfwServerAnimationController.sanitizeMetadata(metadata), forceChat);
        return new MatchedStartRequest(instanceId, eligible.definition().id(), eligible.actorKeys(), eligible.definition().stages());
    }

    public static boolean isDefinitionStartEligible(ServerWorld world, @Nullable AfwAnimationDefinitions.Definition definition, List<? extends Entity> actorsSorted, @Nullable UUID positionAnchorUuid) {
        if (world == null || definition == null || actorsSorted == null || actorsSorted.isEmpty()) {
            return false;
        }
        ArrayList<Entity> actors = new ArrayList<Entity>(actorsSorted.size());
        actors.addAll(actorsSorted);
        actors.sort(Comparator.comparingInt(Entity::getId));
        for (Entity actor : actors) {
            if (actor != null && actor.getEntityWorld() == world) continue;
            return false;
        }
        List<UUID> actorUuids = actors.stream().map(Entity::getUuid).toList();
        List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(definition, actors, world.getRandom());
        UUID effectiveAnchorUuid = AfwServerAnimationController.resolveEffectivePositionAnchorUuid(world, definition.id(), actorUuids, actorKeys, positionAnchorUuid);
        return AfwServerAnimationController.checkStartRequirements(world, definition, actorUuids, effectiveAnchorUuid).valid();
    }

    public static void setAnimationStartProtection(@Nullable ServerPlayerEntity player, int ticks) {
        if (player == null) {
            return;
        }
        ServerWorld class_32182 = player.getEntityWorld();
        if (!(class_32182 instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = class_32182;
        if (ticks <= 0) {
            ANIMATION_START_PROTECTION.remove(player.getUuid());
            return;
        }
        long until = world.getTime() + (long)ticks;
        ANIMATION_START_PROTECTION.put(player.getUuid(), new AnimationStartProtection((RegistryKey<World>)world.getRegistryKey(), until));
    }

    public static void clearAnimationStartProtection(@Nullable ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        ANIMATION_START_PROTECTION.remove(player.getUuid());
    }

    public static boolean hasAnimationStartProtection(@Nullable ServerWorld world, @Nullable ServerPlayerEntity player) {
        if (world == null || player == null) {
            return false;
        }
        AnimationStartProtection protection = ANIMATION_START_PROTECTION.get(player.getUuid());
        if (protection == null) {
            return false;
        }
        if (!protection.worldKey.equals((Object)world.getRegistryKey())) {
            ANIMATION_START_PROTECTION.remove(player.getUuid());
            return false;
        }
        if (world.getTime() >= protection.untilTick) {
            ANIMATION_START_PROTECTION.remove(player.getUuid());
            return false;
        }
        return true;
    }

    public static boolean enqueueForPlayer(ServerWorld world, @Nullable ServerPlayerEntity requester, Identifier animationId, List<? extends Entity> actors, int insertIndex, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable Map<String, String> metadata, boolean forceChat) {
        if (world == null || animationId == null || actors == null || actors.isEmpty()) {
            return false;
        }
        UUID playerUuid = AfwServerAnimationController.inferSinglePlayerUuid(world, actors);
        if (playerUuid == null) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_enqueue_failed_exactly_one_player", new Object[0]), forceChat);
            return false;
        }
        PlayerQueueState queue = PLAYER_QUEUES_BY_WORLD.computeIfAbsent((RegistryKey<World>)world.getRegistryKey(), k -> new HashMap()).computeIfAbsent(playerUuid, PlayerQueueState::new);
        if (queue.entries.size() >= 8) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_enqueue_failed_full", 8), forceChat);
            return false;
        }
        ArrayList<UUID> actorUuids = new ArrayList<UUID>();
        ArrayList<UUID> nonPlayers = new ArrayList<UUID>();
        for (Entity class_12972 : actors) {
            if (class_12972 == null || class_12972.getEntityWorld() != world) continue;
            UUID uuid = class_12972.getUuid();
            if (class_12972 instanceof ServerPlayerEntity) {
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
        Map mobOwner = QUEUED_MOB_TO_PLAYER_BY_WORLD.computeIfAbsent((RegistryKey<World>)world.getRegistryKey(), k -> new HashMap());
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

    public static void clearPlayerQueue(ServerWorld world, @Nullable ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        AfwServerAnimationController.clearPlayerQueueByUuid(world, player.getUuid(), AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_cleared_for_player", new Object[0]));
    }

    public static void clearQueueForInstance(ServerWorld world, UUID instanceId) {
        if (world == null || instanceId == null) {
            return;
        }
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world || inst.playerUuid == null) {
            return;
        }
        AfwServerAnimationController.clearPlayerQueueByUuid(world, inst.playerUuid, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_cleared_for_active_instance", new Object[0]));
    }

    private static void stopActiveInstancesForDisconnectingPlayer(ServerWorld world, UUID playerUuid) {
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
    private static EligibleDefinition selectEligibleDefinition(ServerWorld world, List<Entity> actorsSorted, Set<String> requiredAnimationTags, @Nullable UUID positionAnchorUuid) {
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
        List<UUID> actorUuids = actorsSorted.stream().map(Entity::getUuid).toList();
        for (AfwAnimationDefinitions.Definition definition : candidates) {
            if (definition.specificity() != bestSpecificity) break;
            List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(definition, actorsSorted, world.getRandom());
            UUID effectiveAnchorUuid = AfwServerAnimationController.resolveEffectivePositionAnchorUuid(world, definition.id(), actorUuids, actorKeys, positionAnchorUuid);
            RequirementCheck check = AfwServerAnimationController.checkStartRequirements(world, definition, actorUuids, effectiveAnchorUuid);
            if (!check.valid()) continue;
            eligible.add(new EligibleDefinition(definition, actorKeys, check.blockPlacement()));
        }
        if (eligible.isEmpty()) {
            return null;
        }
        return AfwServerAnimationController.pickWeightedEligible(eligible, world.getRandom());
    }

    private static RequirementCheck checkStartRequirements(ServerWorld world, @Nullable AfwAnimationDefinitions.Definition definition, List<UUID> actorUuids, @Nullable UUID effectiveAnchorUuid) {
        if (definition == null) {
            return new RequirementCheck(false, null);
        }
        AfwAnimationDefinitions.BlockRequirements blockRequirements = definition.blockRequirements();
        AfwAnimationDefinitions.WaterRequirement waterRequirement = definition.waterRequirement();
        if (blockRequirements != null && waterRequirement != AfwAnimationDefinitions.WaterRequirement.NONE) {
            return new RequirementCheck(false, null);
        }
        if (blockRequirements != null) {
            Entity anchor = AfwServerAnimationController.pickAnchorEntity(world, actorUuids, effectiveAnchorUuid);
            BlockPlacement placement = anchor == null ? null : AfwServerAnimationController.findBlockPlacement(world, anchor, blockRequirements);
            return new RequirementCheck(placement != null, placement);
        }
        return new RequirementCheck(AfwServerAnimationController.waterRequirementMet(world, definition, actorUuids), null);
    }

    private static EligibleDefinition pickWeightedEligible(List<EligibleDefinition> definitions, Random random) {
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
            return definitions.get(random.nextInt(definitions.size()));
        }
        double roll = random.nextDouble() * totalWeight;
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

    public static boolean advanceStage(ServerWorld world, UUID instanceId) {
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

    public static boolean advanceToStage(ServerWorld world, UUID instanceId, int stageNumber) {
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return false;
        }
        if (stageNumber <= 0) {
            return false;
        }
        return AfwServerAnimationController.advanceToStageIndex(world, instanceId, inst, stageNumber - 1);
    }

    private static boolean advanceToStageIndex(ServerWorld world, UUID instanceId, ActiveInstance inst, int targetIndex) {
        if (inst == null || inst.world != world || inst.stages == null || inst.stages.isEmpty()) {
            return false;
        }
        if (targetIndex < 0 || targetIndex >= inst.stages.size()) {
            return false;
        }
        long advanceTick = world.getTime();
        if (inst.stageTracker != null) {
            inst.stageTracker.stageIndex = targetIndex;
            inst.stageTracker.stageStartTick = advanceTick;
        }
        inst.speed = AfwServerAnimationController.clampSpeed(AfwServerAnimationController.resolveStageSpeed(inst.stages, inst.stageTracker == null ? 0 : inst.stageTracker.stageIndex));
        AdvanceAnimationStageS2CPayload payload = new AdvanceAnimationStageS2CPayload(instanceId, advanceTick, targetIndex);
        Set<ServerPlayerEntity> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, null, inst);
        AfwServerAnimationController.rememberInstanceSubscribers(inst, targets);
        for (ServerPlayerEntity p : targets) {
            ServerPlayNetworking.send((ServerPlayerEntity)p, (CustomPayload)payload);
            ServerPlayNetworking.send((ServerPlayerEntity)p, (CustomPayload)new AnimationSpeedUpdateS2CPayload(instanceId, inst.speed));
        }
        ((AfwAnimationEvents.StageAdvance)AfwAnimationEvents.STAGE_ADVANCE.invoker()).onStageAdvance(world, instanceId, inst.animationId, List.copyOf(inst.actorUuids), AfwServerAnimationController.sanitizeActorKeys(inst.actorKeys, inst.actorUuids.size()), advanceTick);
        return true;
    }

    public static boolean stopInstance(ServerWorld world, UUID instanceId) {
        return AfwServerAnimationController.stopInstance(world, instanceId, true, StopReason.EXTERNAL_STOP);
    }

    public static void adjustSpeed(ServerWorld world, UUID instanceId, double multiplier, @Nullable ServerPlayerEntity requester) {
        AfwServerAnimationController.adjustSpeed(world, instanceId, multiplier, requester, requester != null);
    }

    public static void adjustSpeedSilently(ServerWorld world, UUID instanceId, double multiplier) {
        AfwServerAnimationController.adjustSpeed(world, instanceId, multiplier, null, false);
    }

    private static void adjustSpeed(ServerWorld world, UUID instanceId, double multiplier, @Nullable ServerPlayerEntity requester, boolean notify) {
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
        long now = world.getTime();
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
        Set<ServerPlayerEntity> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, requester, inst);
        AfwServerAnimationController.rememberInstanceSubscribers(inst, targets);
        for (ServerPlayerEntity p : targets) {
            ServerPlayNetworking.send((ServerPlayerEntity)p, (CustomPayload)payload);
        }
        if (notify) {
            AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.ALWAYS, AfwServerAnimationController.dbg("speed_set", String.format(Locale.ROOT, "%.2fx", newSpeed)), true);
        }
    }

    public static boolean stopInstance(ServerWorld world, UUID instanceId, boolean restoreTransforms) {
        return AfwServerAnimationController.stopInstance(world, instanceId, restoreTransforms, StopReason.EXTERNAL_STOP);
    }

    public static boolean stopInstance(ServerWorld world, UUID instanceId, boolean restoreTransforms, StopReason reason) {
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return false;
        }
        long stopTick = world.getTime() + 1L;
        boolean chained = false;
        if (reason == StopReason.NATURAL_END && inst.playerUuid != null) {
            chained = AfwServerAnimationController.startNextQueuedForPlayerDeferringRestore(world, inst, instanceId);
        }
        StopAnimationS2CPayload payload = new StopAnimationS2CPayload(instanceId, stopTick);
        Set<ServerPlayerEntity> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, null, inst);
        for (ServerPlayerEntity p : targets) {
            ServerPlayNetworking.send((ServerPlayerEntity)p, (CustomPayload)payload);
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

    public static boolean handleActorDamage(ServerWorld world, LivingEntity actor, DamageSource source) {
        UUID actorUuid = actor.getUuid();
        long now = world.getTime();
        boolean blockDamage = false;
        ArrayList<UUID> stopInstances = null;
        if (source != null && source.isOf(DamageTypes.IN_WALL)) {
            Object until;
            Map<UUID, Long> grace = IN_WALL_GRACE_BY_WORLD.get(world.getRegistryKey());
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
        if (source != null && source.isOf(DamageTypes.DROWN) && AfwServerAnimationController.isUnderwaterBreathingProtected(world, actorUuid)) {
            return false;
        }
        if (ACTIVE_INSTANCES.isEmpty()) {
            return true;
        }
        block5: for (Map.Entry<UUID, ActiveInstance> entry : List.copyOf(ACTIVE_INSTANCES.entrySet())) {
            ActiveInstance inst;
            inst = entry.getValue();
            if (inst.world != world || now < inst.startTick || !inst.actorUuidSet.contains(actorUuid)) continue;
            if (inst.ignoreAttackers && source != null && source.getAttacker() instanceof MobEntity) {
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

    public static boolean shouldIgnoreAttackers(ServerWorld world, LivingEntity actor) {
        if (ACTIVE_INSTANCES.isEmpty()) {
            return false;
        }
        UUID actorUuid = actor.getUuid();
        for (ActiveInstance inst : ACTIVE_INSTANCES.values()) {
            if (inst.world != world || !inst.ignoreAttackers || !inst.actorUuidSet.contains(actorUuid)) continue;
            return true;
        }
        return false;
    }

    private static boolean isUnderwaterBreathingProtected(ServerWorld world, UUID actorUuid) {
        if (world == null || actorUuid == null || ACTIVE_INSTANCES.isEmpty()) {
            return false;
        }
        for (ActiveInstance inst : ACTIVE_INSTANCES.values()) {
            if (inst.world != world || !inst.underwaterBreathing || !inst.actorUuidSet.contains(actorUuid)) continue;
            return true;
        }
        return false;
    }

    private static ActiveInstance findActiveInstanceForActors(ServerWorld world, List<UUID> actorUuids) {
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

    public static void stopInstanceAndBroadcast(ServerPlayerEntity requester, UUID instanceId) {
        ServerWorld class_32182 = requester.getEntityWorld();
        if (!(class_32182 instanceof ServerWorld)) {
            return;
        }
        ServerWorld requesterWorld = class_32182;
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null) {
            AfwServerAnimationController.sendOrLogDebug(requesterWorld, requester, AfwDebugChatCategory.ALWAYS, AfwServerAnimationController.dbg("no_such_active_instance", new Object[0]), true);
            return;
        }
        if (inst.world != requesterWorld) {
            AfwServerAnimationController.sendOrLogDebug(requesterWorld, requester, AfwDebugChatCategory.ALWAYS, AfwServerAnimationController.dbg("instance_in_different_world", new Object[0]), true);
            return;
        }
        long now = requesterWorld.getTime();
        long stopTick = now + 1L;
        StopAnimationS2CPayload payload = new StopAnimationS2CPayload(instanceId, stopTick);
        Set<ServerPlayerEntity> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(requesterWorld, requester, inst);
        for (ServerPlayerEntity p : targets) {
            ServerPlayNetworking.send((ServerPlayerEntity)p, (CustomPayload)payload);
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
        AnimationFramework.LOGGER.info("Broadcast STOP: instance={} stopTick={} actors={} requester={}", new Object[]{instanceId, stopTick, inst.actorUuids.size(), requester.getName().getString()});
    }

    public static void clearAllInstancesInWorld(ServerWorld world) {
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
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.remove(world.getRegistryKey());
        if (queues != null) {
            Map<UUID, UUID> byMob = QUEUED_MOB_TO_PLAYER_BY_WORLD.get(world.getRegistryKey());
            if (byMob != null) {
                byMob.clear();
            }
            DEFERRED_QUEUE_CONTEXT_BY_WORLD.remove(world.getRegistryKey());
            for (UUID playerUuid : queues.keySet()) {
                ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(playerUuid);
                AfwServerAnimationController.sendOrLogDebug(world, player, AfwDebugChatCategory.INFO, AfwServerAnimationController.dbg("queue_cleared", new Object[0]), false);
            }
        }
        if ((grace = IN_WALL_GRACE_BY_WORLD.get(world.getRegistryKey())) != null) {
            grace.clear();
        }
        AfwServerAnimationController.processPendingAiRestores(world);
        AfwServerAnimationController.clearAllPlayerLocksInWorld(world);
        RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.remove(world.getRegistryKey());
    }

    private static void tickWorld(ServerWorld world) {
        AfwServerAnimationController.cleanupInWallGrace(world, world.getTime());
        AfwServerAnimationController.cleanupNoRestoreTransitions(world, world.getTime());
        AfwServerAnimationController.tickActiveInstances(world);
        AfwServerAnimationController.tickNoAiCleanup(world);
        AfwServerAnimationController.tickPlayerQueues(world);
    }

    private static void cacheNoRestoreTransition(ServerWorld world, ActiveInstance inst) {
        if (world == null || inst == null || inst.sharedTransform == null || inst.actorUuidSet == null || inst.actorUuidSet.isEmpty()) {
            return;
        }
        long expiresTick = world.getTime() + 20L;
        RecentNoRestoreTransition transition = new RecentNoRestoreTransition(expiresTick, inst.sharedTransform, inst.blockPlacement);
        Map byActor = RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.computeIfAbsent((RegistryKey<World>)world.getRegistryKey(), ignored -> new HashMap());
        for (UUID actorUuid : inst.actorUuidSet) {
            if (actorUuid == null) continue;
            byActor.put(actorUuid, transition);
        }
    }

    @Nullable
    private static RecentNoRestoreTransition consumeNoRestoreTransition(ServerWorld world, List<UUID> actorUuids, Map<String, String> metadata) {
        if (world == null || actorUuids == null || actorUuids.isEmpty() || metadata == null) {
            return null;
        }
        if (!"true".equals(metadata.get(META_JOIN_REPLACE))) {
            return null;
        }
        Map<UUID, RecentNoRestoreTransition> byActor = RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.get(world.getRegistryKey());
        if (byActor == null || byActor.isEmpty()) {
            return null;
        }
        long now = world.getTime();
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
            RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.remove(world.getRegistryKey());
        }
        return selected;
    }

    private static void cleanupNoRestoreTransitions(ServerWorld world, long now) {
        Map<UUID, RecentNoRestoreTransition> byActor = RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.get(world.getRegistryKey());
        if (byActor == null || byActor.isEmpty()) {
            return;
        }
        byActor.entrySet().removeIf(entry -> entry.getValue() == null || ((RecentNoRestoreTransition)entry.getValue()).expiresTick() < now);
        if (byActor.isEmpty()) {
            RECENT_NO_RESTORE_TRANSITIONS_BY_WORLD.remove(world.getRegistryKey());
        }
    }

    private static boolean shouldBlockStart(ServerWorld world, List<UUID> actorUuids) {
        if (actorUuids == null || actorUuids.isEmpty()) {
            return false;
        }
        for (UUID uuid : actorUuids) {
            ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(uuid);
            if (player == null || player.getEntityWorld() != world || !AfwServerAnimationController.hasAnimationStartProtection(world, player)) continue;
            return true;
        }
        return false;
    }

    private static Text dbg(String key, Object ... args) {
        return Text.translatable((String)("debug.animationframework." + key), (Object[])AfwServerAnimationController.sanitizeTranslatableArgs(args));
    }

    private static Object[] sanitizeTranslatableArgs(Object ... args) {
        if (args == null || args.length == 0) {
            return new Object[0];
        }
        Object[] safe = new Object[args.length];
        for (int i = 0; i < args.length; ++i) {
            Object arg = args[i];
            if (arg instanceof Identifier) {
                Identifier id = (Identifier)arg;
                safe[i] = id.getPath();
                continue;
            }
            safe[i] = arg instanceof Text || arg == null ? arg : String.valueOf(arg);
        }
        return safe;
    }

    private static void sendOrLogDebug(ServerWorld world, @Nullable ServerPlayerEntity requester, AfwDebugChatCategory category, String message, boolean forceChat) {
        AfwServerAnimationController.sendOrLogDebug(world, requester, category, (Text)Text.literal((String)message), forceChat);
    }

    private static void sendOrLogDebug(ServerWorld world, @Nullable ServerPlayerEntity requester, AfwDebugChatCategory category, Text message, boolean forceChat) {
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
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!AnimationFramework.shouldSendDebugChat(player, category, forceChat)) continue;
            AnimationFramework.sendDebugChat(player, category, message, forceChat);
        }
    }

    private static void tickActiveInstances(ServerWorld world) {
        ActiveInstance inst;
        long now = world.getTime();
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
                Entity e = world.getEntity(uuid);
                if (!(e instanceof ServerPlayerEntity)) continue;
                playersToLock.put(uuid, inst.sharedTransform);
                if (!inst.underwaterBreathing) continue;
                underwaterPlayers.add(uuid);
            }
        }
        for (StageAdvance adv : stageAdvances) {
            inst = ACTIVE_INSTANCES.get(adv.instanceId());
            if (inst == null || inst.world != world) continue;
            AdvanceAnimationStageS2CPayload payload = new AdvanceAnimationStageS2CPayload(adv.instanceId(), adv.advanceTick(), adv.stageIndex());
            Set<ServerPlayerEntity> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, null, inst);
            AfwServerAnimationController.rememberInstanceSubscribers(inst, targets);
            for (ServerPlayerEntity p : targets) {
                ServerPlayNetworking.send((ServerPlayerEntity)p, (CustomPayload)payload);
                ServerPlayNetworking.send((ServerPlayerEntity)p, (CustomPayload)new AnimationSpeedUpdateS2CPayload(adv.instanceId(), inst.speed));
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
    private static UUID startAndBroadcast(ServerWorld world, @Nullable ServerPlayerEntity requester, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata, boolean forceChat, @Nullable Float transitionYaw) {
        return AfwServerAnimationController.startAndBroadcast(world, requester, animationId, actorUuids, actorKeys, stages, damageBehavior, ignoreAttackers, metadata, forceChat, null, null, transitionYaw);
    }

    @Nullable
    private static UUID startAndBroadcast(ServerWorld world, @Nullable ServerPlayerEntity requester, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata, boolean forceChat, @Nullable UUID preferredAnchorUuid, @Nullable Float transitionYaw) {
        return AfwServerAnimationController.startAndBroadcast(world, requester, animationId, actorUuids, actorKeys, stages, damageBehavior, ignoreAttackers, metadata, forceChat, preferredAnchorUuid, null, transitionYaw);
    }

    @Nullable
    private static UUID startAndBroadcast(ServerWorld world, @Nullable ServerPlayerEntity requester, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata, boolean forceChat, @Nullable UUID preferredAnchorUuid, @Nullable BlockPlacement cachedBlockPlacement, @Nullable Float transitionYaw) {
        List<AnimationStageInfo> safeStages;
        AfwAnimationDefinitions.WaterRequirement waterRequirement;
        long startTick = world.getTime() + 1L;
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
            Entity anchor = AfwServerAnimationController.pickAnchorEntity(world, actorUuids, effectivePositionAnchorUuid);
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
            Entity waterAnchor = AfwServerAnimationController.findWaterAnchor(world, actorUuids, waterRequirement);
            if (waterAnchor == null) {
                AfwServerAnimationController.sendOrLogDebug(world, requester, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("start_cancelled_water_requirement_not_met", new Object[0]), forceChat);
                return null;
            }
            SharedTransform base = sharedTransform;
            sharedTransform = new SharedTransform(waterAnchor.getEntityPos(), base.yaw(), base.pitch(), base.headYaw(), base.bodyYaw());
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
            float yaw = MathHelper.wrapDegrees((float)effectiveTransitionYaw.floatValue());
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
        Vec3d cameraOrbitTarget = placement == null ? null : placement.cameraOrbitTarget();
        StartAnimationS2CPayload payload = new StartAnimationS2CPayload(animationId, instanceId, actorUuids, safeActorKeys, safeStages, startTick, initialSpeed, lockOrientation, lockedYaw, lockedHeadYaw, lockedPitch, cameraOrbitTarget);
        AfwServerAnimationController.disableAiForActors(world, actorUuids);
        if (placement != null) {
            AfwServerAnimationController.markInWallGrace(world, actorUuids);
        }
        AfwServerAnimationController.applySharedTransform(world, actorUuids, sharedTransform, preservePlayerLook);
        if (ignoreAttackers) {
            AfwServerAnimationController.clearAttackTargets(world, actorUuidSet);
        }
        Set<ServerPlayerEntity> targets = AfwServerAnimationController.computeBroadcastTargets(world, requester, actorUuids);
        for (ServerPlayerEntity p : targets) {
            ServerPlayNetworking.send((ServerPlayerEntity)p, (CustomPayload)payload);
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
        String requesterName = requester == null ? "api" : requester.getName().getString();
        AnimationFramework.LOGGER.info("Broadcast START: animation={} instance={} startTick={} actors={} requester={}", new Object[]{AfwServerAnimationController.describeAnimationIdForDebug(animationId), instanceId, startTick, actorUuids.size(), requesterName});
        return instanceId;
    }

    private static Set<ServerPlayerEntity> computeBroadcastTargets(ServerWorld world, @Nullable ServerPlayerEntity requester, List<UUID> actorUuids) {
        HashSet<ServerPlayerEntity> targets = new HashSet<ServerPlayerEntity>();
        if (requester != null) {
            targets.add(requester);
        }
        for (UUID uuid : actorUuids) {
            Entity e = world.getEntity(uuid);
            if (e != null) {
                targets.addAll(PlayerLookup.tracking((Entity)e));
                if (!(e instanceof ServerPlayerEntity)) continue;
                ServerPlayerEntity sp = (ServerPlayerEntity)e;
                targets.add(sp);
                continue;
            }
            ServerPlayerEntity p = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(uuid);
            if (p == null || p.getEntityWorld() != world) continue;
            targets.add(p);
            targets.addAll(PlayerLookup.tracking((Entity)p));
        }
        return targets;
    }

    private static Set<ServerPlayerEntity> computeInstanceBroadcastTargets(ServerWorld world, @Nullable ServerPlayerEntity requester, ActiveInstance inst) {
        Set<ServerPlayerEntity> targets = AfwServerAnimationController.computeBroadcastTargets(world, requester, inst == null ? List.of() : inst.actorUuids);
        if (world == null || inst == null || inst.subscribedPlayerUuids.isEmpty()) {
            return targets;
        }
        for (UUID playerUuid : inst.subscribedPlayerUuids) {
            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(playerUuid);
            if (player == null || player.getEntityWorld() != world) continue;
            targets.add(player);
        }
        return targets;
    }

    private static void rememberInstanceSubscribers(ActiveInstance inst, Set<ServerPlayerEntity> targets) {
        if (inst == null || targets == null || targets.isEmpty()) {
            return;
        }
        for (ServerPlayerEntity player : targets) {
            if (player == null || player.getEntityWorld() != inst.world) continue;
            inst.subscribedPlayerUuids.add(player.getUuid());
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

    private static boolean allSelectedPlayersNearRequester(List<Entity> actors, ServerPlayerEntity requester, double maxDistance) {
        if (actors == null || requester == null) {
            return false;
        }
        double maxDistanceSq = maxDistance * maxDistance;
        for (Entity actor : actors) {
            PlayerEntity playerActor;
            if (!(actor instanceof PlayerEntity) || !((playerActor = (PlayerEntity)actor).squaredDistanceTo((Entity)requester) > maxDistanceSq)) continue;
            return false;
        }
        return true;
    }

    private static Vec3d computeCentroid(List<? extends Entity> entities) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (Entity class_12972 : entities) {
            Vec3d p = class_12972.getEntityPos();
            x += p.x;
            y += p.y;
            z += p.z;
        }
        double n = entities.size();
        return new Vec3d(x / n, y / n, z / n);
    }

    private static Map<UUID, OriginalTransform> captureOriginalTransforms(ServerWorld world, List<UUID> actorUuids) {
        HashMap<UUID, OriginalTransform> originals = new HashMap<UUID, OriginalTransform>();
        for (UUID uuid : actorUuids) {
            Entity e = world.getEntity(uuid);
            if (e instanceof LivingEntity) {
                Vec3d VanillaChestLootTableGenerator;
                LivingEntity living = (LivingEntity)e;
                if (living instanceof ServerPlayerEntity) {
                    ServerPlayerEntity player = (ServerPlayerEntity)living;
                    VanillaChestLootTableGenerator = AfwServerAnimationController.resolveMountedPlayerRestorePosition(player);
                } else {
                    VanillaChestLootTableGenerator = null;
                }
                Vec3d restorePos = VanillaChestLootTableGenerator;
                originals.put(uuid, new OriginalTransform((RegistryKey<World>)world.getRegistryKey(), living.getEntityPos(), living.getYaw(), living.getPitch(), living.getHeadYaw(), living.getBodyYaw(), restorePos));
                continue;
            }
            if (e == null) continue;
            originals.put(uuid, new OriginalTransform((RegistryKey<World>)world.getRegistryKey(), e.getEntityPos(), e.getYaw(), e.getPitch(), e.getYaw(), e.getYaw()));
        }
        return originals;
    }

    @Nullable
    private static UUID resolveEffectivePositionAnchorUuid(ServerWorld world, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, @Nullable UUID preferredAnchorUuid) {
        String anchorActorKey;
        Entity explicitAnchor = AfwServerAnimationController.resolveAnchorEntity(world, actorUuids, preferredAnchorUuid);
        if (explicitAnchor != null) {
            return explicitAnchor.getUuid();
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
            Entity anchor = AfwServerAnimationController.resolveAnchorEntity(world, actorUuids, actorUuids.get(i));
            return anchor == null ? null : anchor.getUuid();
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
    private static Entity resolveAnchorEntity(ServerWorld world, List<UUID> actorUuids, @Nullable UUID anchorUuid) {
        if (world == null || actorUuids == null || actorUuids.isEmpty() || anchorUuid == null) {
            return null;
        }
        if (!actorUuids.contains(anchorUuid)) {
            return null;
        }
        Entity entity = world.getEntity(anchorUuid);
        if (entity != null) {
            return entity;
        }
        ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(anchorUuid);
        if (player != null && player.getEntityWorld() == world) {
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
        return new SharedTransform(Vec3d.ZERO, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    private static Entity pickAnchorEntity(ServerWorld world, List<UUID> actorUuids, @Nullable UUID preferredAnchorUuid) {
        Entity anchor = AfwServerAnimationController.resolveAnchorEntity(world, actorUuids, preferredAnchorUuid);
        if (anchor != null) {
            return anchor;
        }
        return AfwServerAnimationController.pickAnchorEntity(world, actorUuids);
    }

    private static Entity pickAnchorEntity(ServerWorld world, List<UUID> actorUuids) {
        Entity e;
        for (UUID uuid : actorUuids) {
            e = world.getEntity(uuid);
            if (!(e instanceof PlayerEntity)) continue;
            return e;
        }
        for (UUID uuid : actorUuids) {
            e = world.getEntity(uuid);
            if (e == null) continue;
            return e;
        }
        return null;
    }

    private static boolean waterRequirementMet(ServerWorld world, @Nullable AfwAnimationDefinitions.Definition def, List<UUID> actorUuids) {
        AfwAnimationDefinitions.WaterRequirement req;
        AfwAnimationDefinitions.WaterRequirement waterRequirement = req = def == null ? AfwAnimationDefinitions.WaterRequirement.NONE : def.waterRequirement();
        if (req == AfwAnimationDefinitions.WaterRequirement.NONE) {
            return !AfwServerAnimationController.anyActorOnWaterFooting(world, actorUuids);
        }
        return AfwServerAnimationController.findWaterAnchor(world, actorUuids, req) != null;
    }

    private static boolean anyActorOnWaterFooting(ServerWorld world, List<UUID> actorUuids) {
        for (UUID uuid : actorUuids) {
            BlockPos underFeet;
            Entity e = world.getEntity(uuid);
            if (e == null || !world.getFluidState(underFeet = e.getBlockPos().down()).isIn(FluidTags.WATER)) continue;
            return true;
        }
        return false;
    }

    @Nullable
    private static Entity findWaterAnchor(ServerWorld world, List<UUID> actorUuids, AfwAnimationDefinitions.WaterRequirement requirement) {
        for (UUID uuid : actorUuids) {
            Entity e = world.getEntity(uuid);
            if (e == null || !AfwServerAnimationController.meetsWaterRequirement(e, requirement)) continue;
            return e;
        }
        return null;
    }

    private static boolean meetsWaterRequirement(Entity e, AfwAnimationDefinitions.WaterRequirement requirement) {
        return switch (requirement) {
            default -> throw new MatchException(null, null);
            case AfwAnimationDefinitions.WaterRequirement.SURFACE -> {
                if (e.isTouchingWater() && !e.isSubmergedInWater()) {
                    yield true;
                }
                yield false;
            }
            case AfwAnimationDefinitions.WaterRequirement.UNDERWATER -> e.isSubmergedInWater();
            case AfwAnimationDefinitions.WaterRequirement.NONE -> true;
        };
    }

    @Nullable
    private static BlockPlacement findWallPlacement(ServerWorld world, Entity anchor, AfwAnimationDefinitions.BlockRequirements requirements) {
        Vec3d anchorPos = anchor.getEntityPos();
        BlockPos anchorBlock = BlockPos.ofFloored((Position)anchorPos);
        int radius = AfwServerAnimationController.getBlockScanRadius();
        double bestDistSq = Double.MAX_VALUE;
        BlockPlacement best = null;
        int minY = anchorBlock.getY() - 1;
        int maxY = anchorBlock.getY() + 1;
        for (int y = minY; y <= maxY; ++y) {
            for (int dx = -radius; dx <= radius; ++dx) {
                for (int dz = -radius; dz <= radius; ++dz) {
                    BlockPos frontPos;
                    Vec3d center;
                    double distSq;
                    Direction facing;
                    int wallHeight;
                    BlockPos pos = new BlockPos(anchorBlock.getX() + dx, y, anchorBlock.getZ() + dz);
                    if (!AfwServerAnimationController.isSolidFloorBlock(world, pos.down()) || !AfwServerAnimationController.isWallRequirementBlock(world, pos, requirements) || !AfwServerAnimationController.matchesWallHeight(wallHeight = AfwServerAnimationController.countWallHeight(world, pos, requirements.height(), requirements), requirements.height()) || !AfwServerAnimationController.hasWallHeightCapClearance(world, pos, wallHeight, requirements.height()) || !AfwServerAnimationController.checkClearance(world, pos, facing = AfwServerAnimationController.chooseFacingToward(anchorPos, pos), requirements.clearance()) || !((distSq = (center = Vec3d.ofCenter((Vec3i)(frontPos = pos.offset(facing)))).squaredDistanceTo(anchorPos)) < bestDistSq)) continue;
                    float yaw = AfwServerAnimationController.directionToYaw(facing.getOpposite());
                    Vec3d sharedPos = new Vec3d(center.x, (double)frontPos.getY(), center.z);
                    double inset = AfwServerAnimationController.getInsetForWallBlock(world, pos);
                    if (inset > 0.0) {
                        Vec3d insetDir = Vec3d.of((Vec3i)facing.getOpposite().getVector()).multiply(inset);
                        sharedPos = sharedPos.add(insetDir);
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
    private static BlockPlacement findBlockPlacement(ServerWorld world, Entity anchor, AfwAnimationDefinitions.BlockRequirements requirements) {
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
    private static BlockPlacement findCenterSupportPlacement(ServerWorld world, Entity anchor, AfwAnimationDefinitions.BlockRequirements requirements) {
        Vec3d anchorPos = anchor.getEntityPos();
        BlockPos anchorBlock = BlockPos.ofFloored((Position)anchorPos);
        int radius = AfwServerAnimationController.getBlockScanRadius();
        double bestDistSq = Double.MAX_VALUE;
        BlockPlacement best = null;
        int minY = anchorBlock.getY() - 1;
        int maxY = anchorBlock.getY() + 1;
        for (int y = minY; y <= maxY; ++y) {
            for (int dx = -radius; dx <= radius; ++dx) {
                for (int dz = -radius; dz <= radius; ++dz) {
                    BlockPos supportPos = new BlockPos(anchorBlock.getX() + dx, y, anchorBlock.getZ() + dz);
                    Double supportTopOffset = AfwServerAnimationController.supportTopOffset(world, supportPos, requirements.support(), requirements.blocks());
                    if (supportTopOffset == null) continue;
                    Direction[] directions = AfwServerAnimationController.directionsByDistanceToAnchor(anchorPos, supportPos, supportTopOffset);
                    if (requirements.placement() == AfwAnimationDefinitions.CenterPlacement.SURFACE_FOOTPRINT) {
                        for (Direction direction : directions) {
                            for (int lateralSign : new int[]{1, -1}) {
                                Vec3d footprintCenter;
                                double distSq;
                                BedFootprintFacing bedFacing;
                                if (!AfwServerAnimationController.checkCenterSurfaceFootprint(world, supportPos, direction, lateralSign, supportTopOffset, requirements) || (bedFacing = AfwServerAnimationController.resolveBedFootprintFacing(world, supportPos, direction, lateralSign, requirements)).bedFootprint() && bedFacing.facing() == null || !((distSq = (footprintCenter = AfwServerAnimationController.centerSurfaceFootprint(supportPos, direction, lateralSign, supportTopOffset, requirements.surfaceFootprint())).squaredDistanceTo(anchorPos)) < bestDistSq)) continue;
                                Direction placementFacing = bedFacing.facing() == null ? direction : bedFacing.facing().getOpposite();
                                BlockPos placementPos = supportPos;
                                if (bedFacing.facing() != null) {
                                    AfwAnimationDefinitions.SurfaceFootprint footprint = requirements.surfaceFootprint();
                                    Direction side = lateralSign >= 0 ? direction.rotateYCounterclockwise() : direction.rotateYClockwise();
                                    placementPos = supportPos.offset(bedFacing.facing(), Math.max(0, footprint.depth() - 1)).offset(side, Math.max(0, footprint.width() - 1));
                                }
                                float yaw = AfwServerAnimationController.directionToYaw(placementFacing.getOpposite());
                                Vec3d sharedPos = new Vec3d((double)placementPos.getX() + 0.5, (double)placementPos.getY(), (double)placementPos.getZ() + 0.5);
                                if (!AfwServerAnimationController.hasBlockRequirementLineOfSight(world, anchor, footprintCenter)) continue;
                                bestDistSq = distSq;
                                SharedTransform transform = new SharedTransform(sharedPos, yaw, 0.0f, yaw, yaw);
                                best = new BlockPlacement(placementPos, placementFacing, transform, null, true);
                            }
                        }
                        continue;
                    }
                    if (!AfwServerAnimationController.checkCenterSupportSurfaceRadius(world, supportPos, supportTopOffset, requirements.surfaceRadius(), requirements.clearance())) continue;
                    for (Direction direction : directions) {
                        BlockPos sidePos;
                        Vec3d sideCenter;
                        double distSq;
                        if (!AfwServerAnimationController.checkCenterDirectionalClearance(world, supportPos, direction, supportTopOffset, requirements.clearance()) || !((distSq = (sideCenter = new Vec3d((double)(sidePos = supportPos.offset(direction)).getX() + 0.5, (double)supportPos.getY() + supportTopOffset, (double)sidePos.getZ() + 0.5)).squaredDistanceTo(anchorPos)) < bestDistSq)) continue;
                        float yaw = AfwServerAnimationController.directionToYaw(direction.getOpposite());
                        Vec3d sharedPos = new Vec3d((double)supportPos.getX() + 0.5, (double)supportPos.getY(), (double)supportPos.getZ() + 0.5);
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

    private static Direction[] directionsByDistanceToAnchor(Vec3d anchorPos, BlockPos supportPos, double supportTopOffset) {
        Direction[] directions = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
        Arrays.sort(directions, Comparator.comparingDouble(direction -> {
            BlockPos side = supportPos.offset(direction);
            Vec3d center = new Vec3d((double)side.getX() + 0.5, (double)supportPos.getY() + supportTopOffset, (double)side.getZ() + 0.5);
            return center.squaredDistanceTo(anchorPos);
        }));
        return directions;
    }

    @Nullable
    private static Double supportTopOffset(ServerWorld world, BlockPos pos, AfwAnimationDefinitions.CenterSupport support, @Nullable AfwAnimationDefinitions.BlockPredicate blocks) {
        if (support == null) {
            return null;
        }
        BlockState state = world.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) {
            return null;
        }
        if (!AfwServerAnimationController.matchesBlockPredicate(state, blocks)) {
            return null;
        }
        if (support == AfwAnimationDefinitions.CenterSupport.SURFACE) {
            if (blocks == null || blocks.isEmpty()) {
                return null;
            }
            VoxelShape shape = state.getCollisionShape((BlockView)world, pos);
            if (shape.isEmpty()) {
                return null;
            }
            return shape.getMax(Direction.Axis.Y);
        }
        if (support == AfwAnimationDefinitions.CenterSupport.HALF_HEIGHT) {
            if (AfwServerAnimationController.isHalfHeightSupport(state)) {
                return 0.5625;
            }
            return null;
        }
        if (state.getBlock() instanceof SlabBlock) {
            SlabType type = (SlabType)state.get((Property)SlabBlock.TYPE);
            if (support == AfwAnimationDefinitions.CenterSupport.SLAB) {
                return type == SlabType.BOTTOM ? Double.valueOf(0.5) : null;
            }
            if (support == AfwAnimationDefinitions.CenterSupport.FULL_BLOCK) {
                return type == SlabType.TOP || type == SlabType.DOUBLE ? Double.valueOf(1.0) : null;
            }
        }
        if (support == AfwAnimationDefinitions.CenterSupport.FULL_BLOCK && AfwServerAnimationController.isFullHeightSupport(world, pos, state)) {
            return 1.0;
        }
        return null;
    }

    private static boolean isHalfHeightSupport(BlockState state) {
        if (state.getBlock() instanceof BedBlock) {
            return true;
        }
        return state.getBlock() instanceof SlabBlock && state.get((Property)SlabBlock.TYPE) == SlabType.BOTTOM;
    }

    private static boolean isFullHeightSupport(ServerWorld world, BlockPos pos, BlockState state) {
        if (state.isAir() || state.isReplaceable()) {
            return false;
        }
        if (state.isSolidBlock((BlockView)world, pos)) {
            return true;
        }
        VoxelShape shape = state.getCollisionShape((BlockView)world, pos);
        return !shape.isEmpty() && shape.getMax(Direction.Axis.Y) >= 0.999;
    }

    private static boolean checkCenterDirectionalClearance(ServerWorld world, BlockPos supportPos, Direction facing, double supportTopOffset, AfwAnimationDefinitions.Clearance clearance) {
        if (clearance == null) {
            return true;
        }
        int width = Math.max(0, clearance.width());
        int height = Math.max(1, clearance.height());
        int depth = Math.max(1, clearance.depth());
        Direction left = facing.rotateYCounterclockwise();
        for (int d = 1; d <= depth; ++d) {
            for (int w = -width; w <= width; ++w) {
                BlockPos check = supportPos.offset(facing, d).offset(left, w);
                if (!AfwServerAnimationController.isDirectionalClearanceFree(world, check, supportPos.getY(), height)) {
                    return false;
                }
                if (AfwServerAnimationController.isSolidFloorBlock(world, check.down())) continue;
                return false;
            }
        }
        return true;
    }

    private static boolean checkCenterSurfaceFootprint(ServerWorld world, BlockPos supportPos, Direction facing, int lateralSign, double supportTopOffset, AfwAnimationDefinitions.BlockRequirements requirements) {
        AfwAnimationDefinitions.SurfaceFootprint footprint = requirements.surfaceFootprint();
        if (footprint == null) {
            return false;
        }
        int width = Math.max(1, footprint.width());
        int depth = Math.max(1, footprint.depth());
        int height = Math.max(1, footprint.height());
        int margin = Math.max(0, footprint.margin());
        Direction side = lateralSign >= 0 ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
        double supportTopY = (double)supportPos.getY() + supportTopOffset;
        for (int d = -margin; d < depth + margin; ++d) {
            for (int w = -margin; w < width + margin; ++w) {
                boolean insideFootprint;
                BlockPos check = supportPos.offset(facing, d).offset(side, w);
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

    private static BedFootprintFacing resolveBedFootprintFacing(ServerWorld world, BlockPos supportPos, Direction facing, int lateralSign, AfwAnimationDefinitions.BlockRequirements requirements) {
        AfwAnimationDefinitions.SurfaceFootprint footprint = requirements.surfaceFootprint();
        if (footprint == null) {
            return BedFootprintFacing.none();
        }
        List<BlockPos> positions = AfwServerAnimationController.centerSurfaceFootprintPositions(supportPos, facing, lateralSign, footprint);
        if (positions.isEmpty()) {
            return BedFootprintFacing.none();
        }
        int bedCount = 0;
        for (BlockPos pos : positions) {
            if (!(world.getBlockState(pos).getBlock() instanceof BedBlock)) continue;
            ++bedCount;
        }
        if (bedCount == 0) {
            return BedFootprintFacing.none();
        }
        boolean bedOnlyPredicate = AfwServerAnimationController.isBedOnlyBlockPredicate(requirements.blocks());
        if (bedCount != positions.size()) {
            return bedOnlyPredicate ? BedFootprintFacing.invalid() : BedFootprintFacing.none();
        }
        HashSet<BlockPos> footprintPositions = new HashSet<BlockPos>(positions);
        HashSet<BlockPos> pairedPositions = new HashSet<BlockPos>();
        Direction expectedFacing = null;
        for (BlockPos pos : positions) {
            BlockPos otherPos;
            BlockState state = world.getBlockState(pos);
            Direction bedFacing = (Direction)state.get((Property)BedBlock.FACING);
            BedPart part = (BedPart)state.get((Property)BedBlock.PART);
            if (expectedFacing == null) {
                expectedFacing = bedFacing;
            } else if (expectedFacing != bedFacing) {
                return BedFootprintFacing.invalid();
            }
            BlockPos class_23382 = otherPos = part == BedPart.FOOT ? pos.offset(bedFacing) : pos.offset(bedFacing.getOpposite());
            if (!footprintPositions.contains(otherPos)) {
                return BedFootprintFacing.invalid();
            }
            BlockState otherState = world.getBlockState(otherPos);
            if (!(otherState.getBlock() instanceof BedBlock)) {
                return BedFootprintFacing.invalid();
            }
            if (otherState.get((Property)BedBlock.FACING) != bedFacing) {
                return BedFootprintFacing.invalid();
            }
            if (otherState.get((Property)BedBlock.PART) == part) {
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

    private static List<BlockPos> centerSurfaceFootprintPositions(BlockPos supportPos, Direction facing, int lateralSign, AfwAnimationDefinitions.SurfaceFootprint footprint) {
        int width = Math.max(1, footprint == null ? 1 : footprint.width());
        int depth = Math.max(1, footprint == null ? 1 : footprint.depth());
        Direction side = lateralSign >= 0 ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>(width * depth);
        for (int d = 0; d < depth; ++d) {
            for (int w = 0; w < width; ++w) {
                positions.add(supportPos.offset(facing, d).offset(side, w));
            }
        }
        return positions;
    }

    private static boolean isBedOnlyBlockPredicate(@Nullable AfwAnimationDefinitions.BlockPredicate blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return false;
        }
        for (Identifier tagId : blocks.tagIds()) {
            if (BEDS_BLOCK_TAG_ID.equals((Object)tagId)) continue;
            return false;
        }
        for (Identifier blockId : blocks.blockIds()) {
            if (Registries.BLOCK.get(blockId) instanceof BedBlock) continue;
            return false;
        }
        return !blocks.tagIds().isEmpty() || !blocks.blockIds().isEmpty();
    }

    private static boolean isSurfaceFootprintMarginClear(ServerWorld world, BlockPos pos, double supportTopY, int height) {
        Box clearanceBox = new Box((double)pos.getX(), supportTopY + 1.0E-4, (double)pos.getZ(), (double)pos.getX() + 1.0, supportTopY + (double)Math.max(1, height), (double)pos.getZ() + 1.0);
        return world.isSpaceEmpty(clearanceBox) && !AfwServerAnimationController.containsFluid(world, clearanceBox);
    }

    private static Vec3d centerSurfaceFootprint(BlockPos supportPos, Direction facing, int lateralSign, double supportTopOffset, AfwAnimationDefinitions.SurfaceFootprint footprint) {
        int width = Math.max(1, footprint == null ? 1 : footprint.width());
        int depth = Math.max(1, footprint == null ? 1 : footprint.depth());
        Direction side = lateralSign >= 0 ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
        double forwardOffset = (double)(depth - 1) * 0.5;
        double sideOffset = (double)(width - 1) * 0.5;
        return new Vec3d((double)supportPos.getX() + 0.5 + (double)facing.getOffsetX() * forwardOffset + (double)side.getOffsetX() * sideOffset, (double)supportPos.getY() + supportTopOffset, (double)supportPos.getZ() + 0.5 + (double)facing.getOffsetZ() * forwardOffset + (double)side.getOffsetZ() * sideOffset);
    }

    private static boolean checkCenterSupportSurfaceRadius(ServerWorld world, BlockPos supportPos, double supportTopOffset, int surfaceRadius, AfwAnimationDefinitions.Clearance clearance) {
        int radius = Math.max(0, surfaceRadius);
        int height = Math.max(1, clearance == null ? 2 : clearance.height());
        double supportTopY = (double)supportPos.getY() + supportTopOffset;
        for (int dx = -radius; dx <= radius; ++dx) {
            for (int dz = -radius; dz <= radius; ++dz) {
                BlockPos check = supportPos.add(dx, 0, dz);
                if (!AfwServerAnimationController.isSameOrLowerSurface(world, check, supportTopOffset)) {
                    return false;
                }
                if (AfwServerAnimationController.isSpaceClearAboveSupport(world, check, supportTopY, height)) continue;
                return false;
            }
        }
        return true;
    }

    private static boolean isSameOrLowerSurface(ServerWorld world, BlockPos pos, double supportTopOffset) {
        BlockState state = world.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) {
            return false;
        }
        if (state.isAir() || state.isReplaceable()) {
            return true;
        }
        VoxelShape shape = state.getCollisionShape((BlockView)world, pos);
        if (shape.isEmpty()) {
            return true;
        }
        return shape.getMax(Direction.Axis.Y) <= supportTopOffset + 1.0E-4;
    }

    private static boolean isDirectionalClearanceFree(ServerWorld world, BlockPos pos, int supportY, int height) {
        Box clearanceBox = new Box((double)pos.getX(), (double)supportY + 1.0E-4, (double)pos.getZ(), (double)pos.getX() + 1.0, (double)(supportY + Math.max(1, height)), (double)pos.getZ() + 1.0);
        return world.isSpaceEmpty(clearanceBox) && !AfwServerAnimationController.containsFluid(world, clearanceBox);
    }

    private static boolean isSpaceClearAboveSupport(ServerWorld world, BlockPos pos, double supportTopY, int height) {
        Box clearanceBox = new Box((double)pos.getX(), supportTopY + 1.0E-4, (double)pos.getZ(), (double)pos.getX() + 1.0, supportTopY + (double)Math.max(1, height), (double)pos.getZ() + 1.0);
        return world.isSpaceEmpty(clearanceBox) && !AfwServerAnimationController.containsFluid(world, clearanceBox);
    }

    private static Text blockRequirementFailureMessage(AfwAnimationDefinitions.BlockRequirements requirements, Identifier animationId, ServerWorld world, List<UUID> actorUuids) {
        String animation = AfwServerAnimationController.describeAnimationIdForDebug(animationId);
        String actors = AfwServerAnimationController.describeActorsForDebug(world, actorUuids);
        if (requirements != null && requirements.isCenterSupport()) {
            return AfwServerAnimationController.dbg("start_cancelled_no_valid_center_support", animation, actors);
        }
        return AfwServerAnimationController.dbg("start_cancelled_no_valid_wall", animation, actors);
    }

    private static boolean checkClearance(ServerWorld world, BlockPos wallPos, Direction facing, AfwAnimationDefinitions.Clearance clearance) {
        if (clearance == null) {
            return true;
        }
        int width = Math.max(1, clearance.width());
        int height = Math.max(1, clearance.height());
        int depth = Math.max(1, clearance.depth());
        Direction left = facing.rotateYCounterclockwise();
        for (int dy = 0; dy < height; ++dy) {
            for (int d = 1; d <= depth; ++d) {
                for (int w = -width; w <= width; ++w) {
                    BlockPos check = wallPos.offset(facing, d).offset(left, w).up(dy);
                    if (!AfwServerAnimationController.isFreeSpace(world, check)) {
                        return false;
                    }
                    if (dy != 0 || AfwServerAnimationController.isSolidWallBlock(world, check.down())) continue;
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

    private static boolean hasBlockRequirementLineOfSight(ServerWorld world, Entity anchor, Vec3d target) {
        Vec3d end;
        if (world == null || anchor == null || target == null) {
            return false;
        }
        Vec3d start = anchor.getEyePos();
        if (start.squaredDistanceTo(end = target.add(0.0, 0.75, 0.0)) < 1.0E-6) {
            return true;
        }
        BlockHitResult result = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, anchor));
        return result == null || result.getType() == HitResult.Type.MISS;
    }

    private static void markInWallGrace(ServerWorld world, List<UUID> actorUuids) {
        if (actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        long until = world.getTime() + 5L;
        Map grace = IN_WALL_GRACE_BY_WORLD.computeIfAbsent((RegistryKey<World>)world.getRegistryKey(), k -> new HashMap());
        for (UUID uuid : actorUuids) {
            Long existing = (Long)grace.get(uuid);
            if (existing != null && existing >= until) continue;
            grace.put(uuid, until);
        }
    }

    private static void cleanupInWallGrace(ServerWorld world, long now) {
        Map<UUID, Long> grace = IN_WALL_GRACE_BY_WORLD.get(world.getRegistryKey());
        if (grace == null || grace.isEmpty()) {
            return;
        }
        grace.entrySet().removeIf(entry -> (Long)entry.getValue() < now);
    }

    private static Direction chooseFacingToward(Vec3d anchorPos, BlockPos wallPos) {
        Vec3d center = Vec3d.ofCenter((Vec3i)wallPos);
        double dx = anchorPos.x - center.x;
        double dz = anchorPos.z - center.z;
        if (Math.abs(dx) >= Math.abs(dz)) {
            return dx >= 0.0 ? Direction.EAST : Direction.WEST;
        }
        return dz >= 0.0 ? Direction.SOUTH : Direction.NORTH;
    }

    private static float directionToYaw(Direction dir) {
        return switch (dir) {
            case Direction.WEST -> 90.0f;
            case Direction.NORTH -> 180.0f;
            case Direction.EAST -> -90.0f;
            default -> 0.0f;
        };
    }

    private static int countWallHeight(ServerWorld world, BlockPos pos, AfwAnimationDefinitions.WallHeight height, AfwAnimationDefinitions.BlockRequirements requirements) {
        if (height == null) {
            return 0;
        }
        int min = Math.max(1, height.min());
        Integer max = height.max();
        int limit = max != null ? max + 1 : min;
        int actual = 0;
        BlockPos cursor = pos;
        while (actual < limit && AfwServerAnimationController.isWallRequirementBlock(world, cursor, requirements)) {
            ++actual;
            cursor = cursor.up();
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

    private static boolean hasWallHeightCapClearance(ServerWorld world, BlockPos pos, int actual, AfwAnimationDefinitions.WallHeight height) {
        if (height == null || height.max() == null) {
            return true;
        }
        if (actual != height.max()) {
            return true;
        }
        return AfwServerAnimationController.isFreeSpace(world, pos.up(actual));
    }

    private static boolean isSolidWallBlock(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) {
            return false;
        }
        if (state.isIn(BlockTags.FENCES) || state.isIn(BlockTags.WALLS)) {
            return true;
        }
        if (state.isSolidBlock((BlockView)world, pos)) {
            return true;
        }
        VoxelShape shape = state.getCollisionShape((BlockView)world, pos);
        return !shape.isEmpty() && shape.getMax(Direction.Axis.Y) >= 0.999;
    }

    private static boolean isWallRequirementBlock(ServerWorld world, BlockPos pos, AfwAnimationDefinitions.BlockRequirements requirements) {
        AfwAnimationDefinitions.BlockPredicate blocks;
        AfwAnimationDefinitions.BlockPredicate blockPredicate = blocks = requirements == null ? null : requirements.blocks();
        if (blocks == null || blocks.isEmpty()) {
            return AfwServerAnimationController.isSolidWallBlock(world, pos);
        }
        BlockState state = world.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) {
            return false;
        }
        if (!AfwServerAnimationController.matchesBlockPredicate(state, blocks)) {
            return false;
        }
        return !state.getCollisionShape((BlockView)world, pos).isEmpty();
    }

    private static boolean matchesBlockPredicate(BlockState state, @Nullable AfwAnimationDefinitions.BlockPredicate blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return true;
        }
        Identifier blockId = Registries.BLOCK.getId((Object)state.getBlock());
        if (blocks.blockIds().contains(blockId)) {
            return true;
        }
        for (Identifier tagId : blocks.tagIds()) {
            if (!state.isIn(TagKey.of((RegistryKey)RegistryKeys.BLOCK, (Identifier)tagId))) continue;
            return true;
        }
        return false;
    }

    private static boolean isSolidFloorBlock(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) {
            return false;
        }
        if (state.isAir() || state.isReplaceable()) {
            return false;
        }
        if (state.isIn(BlockTags.FENCES) || state.isIn(BlockTags.WALLS)) {
            return false;
        }
        if (state.isSolidBlock((BlockView)world, pos)) {
            return true;
        }
        VoxelShape shape = state.getCollisionShape((BlockView)world, pos);
        return !shape.isEmpty() && shape.getMax(Direction.Axis.Y) >= 0.999;
    }

    private static double getInsetForWallBlock(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isIn(BlockTags.FENCES)) {
            return 0.4;
        }
        if (state.isIn(BlockTags.WALLS)) {
            return 0.3;
        }
        return 0.0;
    }

    private static boolean isFreeSpace(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) {
            return false;
        }
        return state.isAir() || state.isReplaceable();
    }

    private static void applySharedTransform(ServerWorld world, List<UUID> actorUuids, SharedTransform sharedTransform, boolean preservePlayerLook) {
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(world.getRegistryKey());
        for (UUID uuid : actorUuids) {
            AiDisableState state;
            Entity class_12972;
            Entity e = world.getEntity(uuid);
            if (e == null) continue;
            Objects.requireNonNull(e);
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{ServerPlayerEntity.class, LivingEntity.class}, (Object)class_12972, n)) {
                case 0: {
                    ServerPlayerEntity player = (ServerPlayerEntity)class_12972;
                    AfwServerAnimationController.preparePlayerForAnimationTeleport(player);
                    float teleportYaw = preservePlayerLook ? player.getYaw() : sharedTransform.yaw();
                    float teleportPitch = preservePlayerLook ? player.getPitch() : sharedTransform.pitch();
                    player.networkHandler.requestTeleport(sharedTransform.pos().x, sharedTransform.pos().y, sharedTransform.pos().z, teleportYaw, teleportPitch);
                    player.setHeadYaw(sharedTransform.headYaw());
                    player.setBodyYaw(sharedTransform.bodyYaw());
                    break;
                }
                case 1: {
                    LivingEntity living = (LivingEntity)class_12972;
                    living.refreshPositionAndAngles(sharedTransform.pos().x, sharedTransform.pos().y, sharedTransform.pos().z, sharedTransform.yaw(), sharedTransform.pitch());
                    living.setHeadYaw(sharedTransform.headYaw());
                    living.setBodyYaw(sharedTransform.bodyYaw());
                    break;
                }
                default: {
                    e.refreshPositionAndAngles(sharedTransform.pos().x, sharedTransform.pos().y, sharedTransform.pos().z, sharedTransform.yaw(), sharedTransform.pitch());
                }
            }
            e.setVelocity(Vec3d.ZERO);
            e.fallDistance = 0.0;
            if (!(e instanceof MobEntity)) continue;
            MobEntity mob = (MobEntity)e;
            if (states == null || (state = states.get(uuid)) == null) continue;
            state.lockedYaw = sharedTransform.yaw();
            state.lockedHeadYaw = sharedTransform.headYaw();
            state.lockedBodyYaw = sharedTransform.bodyYaw();
            state.lockedPitch = sharedTransform.pitch();
        }
    }

    private static void preparePlayerForAnimationTeleport(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        if (player.hasVehicle()) {
            player.stopRiding();
        }
        if (player.isSleeping()) {
            player.wakeUp(true, true);
        }
        player.setVelocity(Vec3d.ZERO);
        player.fallDistance = 0.0;
    }

    @Nullable
    private static SharedTransform findSafeGroundPlacement(ServerWorld world, SharedTransform sharedTransform) {
        Vec3d startPos = sharedTransform.pos();
        FloorHit baseFloor = AfwServerAnimationController.findNearestFloor(world, startPos, 2);
        if (!baseFloor.found) {
            return null;
        }
        SharedTransform base = AfwServerAnimationController.withSharedPosition(sharedTransform, baseFloor.pos);
        if (AfwServerAnimationController.isGroundPlacementClear(world, baseFloor.pos)) {
            return base;
        }
        for (GroundPlacementOffset offset : GROUND_PLACEMENT_OFFSETS) {
            Vec3d candidateStart = startPos.add(offset.dx(), 0.0, offset.dz());
            FloorHit candidateFloor = AfwServerAnimationController.findNearestFloor(world, candidateStart, 2);
            if (!candidateFloor.found || !AfwServerAnimationController.isGroundPlacementClear(world, candidateFloor.pos)) continue;
            return AfwServerAnimationController.withSharedPosition(sharedTransform, candidateFloor.pos);
        }
        return null;
    }

    private static SharedTransform withSharedPosition(SharedTransform sharedTransform, Vec3d pos) {
        return new SharedTransform(pos, sharedTransform.yaw(), sharedTransform.pitch(), sharedTransform.headYaw(), sharedTransform.bodyYaw());
    }

    @Nullable
    private static SharedTransform findMountedPlayerDismountTransform(ServerWorld world, List<UUID> actorUuids, SharedTransform base) {
        if (world == null || actorUuids == null || actorUuids.isEmpty() || base == null) {
            return null;
        }
        for (UUID actorUuid : actorUuids) {
            Entity vehicle;
            ServerPlayerEntity player;
            Entity entity = world.getEntity(actorUuid);
            if (!(entity instanceof ServerPlayerEntity) || !(player = (ServerPlayerEntity)entity).hasVehicle() || (vehicle = player.getVehicle()) == null || vehicle.isRemoved()) continue;
            Vec3d dismountPos = vehicle.updatePassengerForDismount((LivingEntity)player);
            if (dismountPos == null) {
                dismountPos = vehicle.getEntityPos();
            }
            return AfwServerAnimationController.withSharedPosition(base, dismountPos);
        }
        return null;
    }

    @Nullable
    private static Vec3d resolveMountedPlayerRestorePosition(ServerPlayerEntity player) {
        if (player == null || !player.hasVehicle()) {
            return null;
        }
        Entity vehicle = player.getVehicle();
        if (vehicle == null || vehicle.isRemoved()) {
            return null;
        }
        Vec3d dismountPos = vehicle.updatePassengerForDismount((LivingEntity)player);
        if (dismountPos == null || vehicle.getBoundingBox().expand(0.05).contains(dismountPos)) {
            Box box = vehicle.getBoundingBox();
            dismountPos = new Vec3d(vehicle.getX(), box.maxY + 0.05, vehicle.getZ());
        }
        return dismountPos;
    }

    @Nullable
    private static SharedTransform resolveQueuedPlacementTransform(ServerWorld world, @Nullable UUID playerUuid, SharedTransform base, Map<String, String> metadata) {
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
        Vec3d topPos = new Vec3d((double)previousPlacement.wallPos().getX() + 0.5, (double)previousPlacement.wallPos().getY() + supportTopOffset, (double)previousPlacement.wallPos().getZ() + 0.5);
        if (!AfwServerAnimationController.isGroundPlacementClear(world, topPos)) {
            return null;
        }
        return AfwServerAnimationController.withSharedPosition(base, topPos);
    }

    private static double supportSurfaceTopOffset(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) {
            return -1.0;
        }
        VoxelShape shape = state.getCollisionShape((BlockView)world, pos);
        if (shape.isEmpty()) {
            return -1.0;
        }
        return shape.getMax(Direction.Axis.Y);
    }

    private static boolean isGroundPlacementClear(ServerWorld world, Vec3d pos) {
        Box clearanceBox = new Box(pos.x - 0.75, pos.y + 0.01, pos.z - 0.75, pos.x + 0.75, pos.y + 2.0, pos.z + 0.75);
        return world.isSpaceEmpty(clearanceBox) && !AfwServerAnimationController.containsFluid(world, clearanceBox);
    }

    private static boolean containsFluid(ServerWorld world, Box box) {
        int minX = MathHelper.floor((double)box.minX);
        int maxX = MathHelper.floor((double)(box.maxX - 1.0E-7));
        int minY = MathHelper.floor((double)box.minY);
        int maxY = MathHelper.floor((double)(box.maxY - 1.0E-7));
        int minZ = MathHelper.floor((double)box.minZ);
        int maxZ = MathHelper.floor((double)(box.maxZ - 1.0E-7));
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int y = minY; y <= maxY; ++y) {
            for (int x = minX; x <= maxX; ++x) {
                for (int z = minZ; z <= maxZ; ++z) {
                    mutable.set(x, y, z);
                    if (world.getFluidState((BlockPos)mutable).isEmpty()) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static FloorHit findNearestFloor(ServerWorld world, Vec3d startPos, int maxDropBlocks) {
        BlockPos startBlock = BlockPos.ofFloored((Position)startPos);
        BlockPos.Mutable cursor = new BlockPos.Mutable(startBlock.getX(), startBlock.getY(), startBlock.getZ());
        int minY = Math.max(world.getBottomY(), startBlock.getY() - maxDropBlocks);
        double maxFloorY = startPos.y + 0.001;
        for (int y = startBlock.getY(); y >= minY; --y) {
            double floorY;
            double top;
            VoxelShape shape;
            cursor.setY(y);
            BlockState state = world.getBlockState((BlockPos)cursor);
            if (state.isAir() || (shape = state.getCollisionShape((BlockView)world, (BlockPos)cursor)).isEmpty() || (top = shape.getMax(Direction.Axis.Y)) <= 0.0 || !((floorY = (double)y + top) <= maxFloorY)) continue;
            return new FloorHit(true, new Vec3d(startPos.x, floorY, startPos.z));
        }
        return new FloorHit(false, startPos);
    }

    private static List<GroundPlacementOffset> createGroundPlacementOffsets() {
        int steps = Math.max(1, MathHelper.ceil((double)4.0));
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
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(inst.world.getRegistryKey());
        for (Map.Entry<UUID, OriginalTransform> entry : originals.entrySet()) {
            AiDisableState state;
            Entity class_12972;
            Entity e;
            OriginalTransform ot;
            if (skipActors != null && skipActors.contains(entry.getKey()) || (ot = entry.getValue()).worldKey() != inst.world.getRegistryKey() || (e = inst.world.getEntity(entry.getKey())) == null) continue;
            Objects.requireNonNull(e);
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{ServerPlayerEntity.class, LivingEntity.class}, (Object)class_12972, n)) {
                case 0: {
                    ServerPlayerEntity player = (ServerPlayerEntity)class_12972;
                    AfwServerAnimationController.preparePlayerForAnimationTeleport(player);
                    Vec3d restorePos = ot.restorePosOrPos();
                    player.networkHandler.requestTeleport(restorePos.x, restorePos.y, restorePos.z, player.getYaw(), player.getPitch());
                    AfwServerAnimationController.clearDeferredQueueOriginal(inst.world, entry.getKey());
                    break;
                }
                case 1: {
                    LivingEntity living = (LivingEntity)class_12972;
                    living.refreshPositionAndAngles(ot.pos().x, ot.pos().y, ot.pos().z, ot.yaw(), ot.pitch());
                    living.setHeadYaw(ot.headYaw());
                    living.setBodyYaw(ot.bodyYaw());
                    break;
                }
                default: {
                    e.refreshPositionAndAngles(ot.pos().x, ot.pos().y, ot.pos().z, ot.yaw(), ot.pitch());
                }
            }
            e.setVelocity(Vec3d.ZERO);
            e.fallDistance = 0.0;
            if (!(e instanceof MobEntity) || states == null || (state = states.get(entry.getKey())) == null) continue;
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

    private static boolean startNextQueuedForPlayerDeferringRestore(ServerWorld world, ActiveInstance inst, @Nullable UUID ignoreInstanceId) {
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

    private static boolean captureDeferredQueueContextFromActiveInstance(ServerWorld world, UUID playerUuid) {
        ActiveInstance active = AfwServerAnimationController.findActiveInstanceForActors(world, List.of(playerUuid));
        return AfwServerAnimationController.captureDeferredQueueContext(world, playerUuid, active);
    }

    private static boolean captureDeferredQueueContext(ServerWorld world, UUID playerUuid, @Nullable ActiveInstance inst) {
        DeferredQueueContext context;
        if (world == null || playerUuid == null || inst == null) {
            return false;
        }
        OriginalTransform original = AfwServerAnimationController.getOriginalTransformForActor(inst, playerUuid);
        if (original == null) {
            return false;
        }
        Map byPlayer = DEFERRED_QUEUE_CONTEXT_BY_WORLD.computeIfAbsent((RegistryKey<World>)world.getRegistryKey(), ignored -> new HashMap());
        return byPlayer.putIfAbsent(playerUuid, context = new DeferredQueueContext(original, AfwServerAnimationController.queueContinuationPlacementForInstance(inst))) == null;
    }

    private static Map<UUID, OriginalTransform> applyDeferredQueueOriginal(ServerWorld world, @Nullable UUID playerUuid, Map<UUID, OriginalTransform> originals, Map<String, String> metadata) {
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
    private static OriginalTransform getDeferredQueueOriginal(ServerWorld world, UUID playerUuid) {
        DeferredQueueContext context = AfwServerAnimationController.getDeferredQueueContext(world, playerUuid);
        return context == null ? null : context.originalTransform();
    }

    private static void clearDeferredQueueOriginal(ServerWorld world, UUID playerUuid) {
        Map<UUID, DeferredQueueContext> byPlayer = DEFERRED_QUEUE_CONTEXT_BY_WORLD.get(world.getRegistryKey());
        if (byPlayer == null) {
            return;
        }
        byPlayer.remove(playerUuid);
        if (byPlayer.isEmpty()) {
            DEFERRED_QUEUE_CONTEXT_BY_WORLD.remove(world.getRegistryKey());
        }
    }

    @Nullable
    private static DeferredQueueContext getDeferredQueueContext(ServerWorld world, UUID playerUuid) {
        Map<UUID, DeferredQueueContext> byPlayer = DEFERRED_QUEUE_CONTEXT_BY_WORLD.get(world.getRegistryKey());
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

    private static void disableAiForActors(ServerWorld world, List<UUID> actorUuids) {
        if (actorUuids.isEmpty()) {
            return;
        }
        Map states = AI_DISABLE_STATE_BY_WORLD.computeIfAbsent((RegistryKey<World>)world.getRegistryKey(), k -> new HashMap());
        for (UUID uuid : actorUuids) {
            Entity e = world.getEntity(uuid);
            if (!(e instanceof MobEntity)) continue;
            MobEntity mob = (MobEntity)e;
            AiDisableState state = (AiDisableState)states.get(uuid);
            if (state == null) {
                boolean originalAiDisabled = mob.isAiDisabled();
                state = new AiDisableState(originalAiDisabled);
                states.put(uuid, state);
            } else {
                ++state.locks;
                state.needsRestore = false;
            }
            AfwServerAnimationController.captureOrientation(mob, state);
            AfwServerAnimationController.applyNoAiTags(mob, state);
            mob.setAiDisabled(true);
            mob.getNavigation().stop();
            mob.setVelocity(Vec3d.ZERO);
            mob.fallDistance = 0.0;
            mob.setYaw(state.lockedYaw);
            mob.setPitch(state.lockedPitch);
            mob.setHeadYaw(state.lockedHeadYaw);
            mob.setBodyYaw(state.lockedBodyYaw);
        }
    }

    private static void restoreAiForActors(ServerWorld world, List<UUID> actorUuids) {
        if (actorUuids.isEmpty()) {
            return;
        }
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(world.getRegistryKey());
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
            AI_DISABLE_STATE_BY_WORLD.remove(world.getRegistryKey());
        }
    }

    private static void tryRestoreAi(ServerWorld world, UUID actorUuid, AiDisableState state, Map<UUID, AiDisableState> states) {
        Entity e = world.getEntity(actorUuid);
        if (e instanceof MobEntity) {
            MobEntity mob = (MobEntity)e;
            AfwServerAnimationController.restoreAiState(mob, state);
            states.remove(actorUuid);
        } else if (e != null) {
            states.remove(actorUuid);
        }
    }

    private static void processPendingAiRestores(ServerWorld world) {
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(world.getRegistryKey());
        if (states == null || states.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<UUID, AiDisableState>> it = states.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, AiDisableState> entry = it.next();
            AiDisableState state = entry.getValue();
            if (state.locks > 0 || !state.needsRestore) continue;
            Entity e = world.getEntity(entry.getKey());
            if (e instanceof MobEntity) {
                MobEntity mob = (MobEntity)e;
                AfwServerAnimationController.restoreAiState(mob, state);
                it.remove();
                continue;
            }
            if (e == null) continue;
            it.remove();
        }
        if (states.isEmpty()) {
            AI_DISABLE_STATE_BY_WORLD.remove(world.getRegistryKey());
        }
    }

    private static void freezeActiveActors(ServerWorld world, Set<UUID> actorUuids) {
        if (actorUuids.isEmpty()) {
            return;
        }
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(world.getRegistryKey());
        if (states == null || states.isEmpty()) {
            return;
        }
        for (UUID uuid : actorUuids) {
            Entity e;
            AiDisableState state = states.get(uuid);
            if (state == null || !((e = world.getEntity(uuid)) instanceof MobEntity)) continue;
            MobEntity mob = (MobEntity)e;
            AfwServerAnimationController.freezeMobDuringAnimation(mob, state);
        }
    }

    private static void captureOrientation(MobEntity mob, AiDisableState state) {
        state.lockedYaw = mob.getYaw();
        state.lockedHeadYaw = mob.getHeadYaw();
        state.lockedBodyYaw = mob.getBodyYaw();
        state.lockedPitch = mob.getPitch();
    }

    private static void freezeMobDuringAnimation(MobEntity mob, AiDisableState state) {
        mob.setVelocity(Vec3d.ZERO);
        mob.fallDistance = 0.0;
        mob.setYaw(state.lockedYaw);
        mob.setPitch(state.lockedPitch);
        mob.setHeadYaw(state.lockedHeadYaw);
        mob.setBodyYaw(state.lockedBodyYaw);
    }

    private static void applyNoAiTags(MobEntity mob, AiDisableState state) {
        mob.addCommandTag(AFW_NOAI_TAG);
        if (state.originalAiDisabled) {
            mob.addCommandTag(AFW_NOAI_ORIG_TAG);
        } else {
            mob.removeCommandTag(AFW_NOAI_ORIG_TAG);
        }
    }

    public static void sendActiveInstancesToPlayer(ServerPlayerEntity player) {
        ServerWorld class_32182 = player.getEntityWorld();
        if (!(class_32182 instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = class_32182;
        for (Map.Entry entry : ACTIVE_INSTANCES.entrySet()) {
            ActiveInstance inst = (ActiveInstance)entry.getValue();
            if (inst.world != world) continue;
            List<String> safeKeys = AfwServerAnimationController.sanitizeActorKeys(inst.actorKeys, inst.actorUuids.size());
            List<AnimationStageInfo> safeStages = inst.stages == null ? List.of() : List.copyOf(inst.stages);
            StartAnimationS2CPayload payload = new StartAnimationS2CPayload(inst.animationId, (UUID)entry.getKey(), inst.actorUuids, safeKeys, safeStages, inst.startTick, inst.speed, inst.lockOrientation, inst.lockedYaw, inst.lockedHeadYaw, inst.lockedPitch, inst.cameraOrbitTarget);
            ServerPlayNetworking.send((ServerPlayerEntity)player, (CustomPayload)payload);
            inst.subscribedPlayerUuids.add(player.getUuid());
        }
    }

    private static void tickPlayerQueues(ServerWorld world) {
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.get(world.getRegistryKey());
        if (queues == null || queues.isEmpty()) {
            return;
        }
        ArrayList<UUID> removePlayers = new ArrayList<UUID>();
        for (Map.Entry<UUID, PlayerQueueState> entry : queues.entrySet()) {
            UUID playerUuid = entry.getKey();
            ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(playerUuid);
            if (player == null || player.getEntityWorld() != world || !player.isAlive() || player.isRemoved()) {
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
    private static UUID inferSinglePlayerUuid(ServerWorld world, List<? extends Entity> actors) {
        UUID found = null;
        for (Entity class_12972 : actors) {
            ServerPlayerEntity player;
            if (!(class_12972 instanceof ServerPlayerEntity) || (player = (ServerPlayerEntity)class_12972).getEntityWorld() != world) continue;
            if (found != null && !found.equals(player.getUuid())) {
                return null;
            }
            found = player.getUuid();
        }
        return found;
    }

    @Nullable
    private static UUID resolvePlayerUuid(ServerWorld world, List<UUID> actorUuids) {
        if (actorUuids == null || actorUuids.isEmpty()) {
            return null;
        }
        UUID found = null;
        for (UUID uuid : actorUuids) {
            Entity entity = world.getEntity(uuid);
            if (!(entity instanceof ServerPlayerEntity)) continue;
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

    private static String describeActorForDebug(@Nullable ServerWorld world, @Nullable UUID actorUuid) {
        ServerPlayerEntity player;
        if (actorUuid == null || world == null) {
            return "(unknown)";
        }
        Entity entity = world.getEntity(actorUuid);
        if (entity == null && world.getServer() != null && (player = world.getServer().getPlayerManager().getPlayer(actorUuid)) != null && player.getEntityWorld() == world) {
            entity = player;
        }
        if (entity == null) {
            String uuid = actorUuid.toString();
            return "missing:" + uuid.substring(0, 8);
        }
        String display = entity.getDisplayName().getString();
        Identifier typeId = Registries.ENTITY_TYPE.getId((Object)entity.getType());
        if (display == null || display.isBlank()) {
            return typeId.getPath();
        }
        return display;
    }

    private static String describeActorsForDebug(@Nullable ServerWorld world, @Nullable List<UUID> actorUuids) {
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

    private static String describeAnimationIdForDebug(@Nullable Identifier animationId) {
        if (animationId == null) {
            return "(unknown)";
        }
        return animationId.getPath();
    }

    private static boolean isActorActiveIgnoringInstance(ServerWorld world, UUID actorUuid, @Nullable UUID ignoreInstanceId) {
        for (Map.Entry<UUID, ActiveInstance> entry : ACTIVE_INSTANCES.entrySet()) {
            if (ignoreInstanceId != null && ignoreInstanceId.equals(entry.getKey())) continue;
            ActiveInstance inst = entry.getValue();
            if (inst.world != world || !inst.actorUuidSet.contains(actorUuid)) continue;
            return true;
        }
        return false;
    }

    private static boolean startNextQueuedForPlayer(ServerWorld world, UUID playerUuid, @Nullable UUID ignoreInstanceId, @Nullable Float transitionYaw) {
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.get(world.getRegistryKey());
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

    private static QueueStartResult tryStartQueuedEntry(ServerWorld world, PlayerQueueState queue, QueuedEntry entry, @Nullable Float transitionYaw) {
        ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(queue.playerUuid);
        if (player == null || player.getEntityWorld() != world || !player.isAlive() || player.isRemoved()) {
            AfwServerAnimationController.sendOrLogDebug(world, null, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_skipped_player_unavailable", AfwServerAnimationController.describeAnimationIdForDebug(entry.animationId), AfwServerAnimationController.describeActorsForDebug(world, entry.actorUuids)), false);
            return QueueStartResult.SKIP;
        }
        ArrayList<Object> resolvedActors = new ArrayList<Object>();
        boolean missingNonPlayer = false;
        for (UUID actorUuid : entry.actorUuids) {
            LivingEntity living;
            if (actorUuid.equals(queue.playerUuid)) {
                resolvedActors.add(player);
                continue;
            }
            Entity entity = world.getEntity(actorUuid);
            if (!(entity instanceof LivingEntity) || !(living = (LivingEntity)entity).isAlive() || living.isRemoved()) {
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
        ArrayList<Entity> sorted = new ArrayList<Entity>(resolvedActors);
        sorted.sort(Comparator.comparingInt(Entity::getId));
        List<UUID> uuids = sorted.stream().map(Entity::getUuid).toList();
        if (!AfwServerAnimationController.waterRequirementMet(world, def, uuids)) {
            AfwServerAnimationController.sendOrLogDebug(world, player, AfwDebugChatCategory.WARNING, AfwServerAnimationController.dbg("queue_skipped_requirements_not_met", AfwServerAnimationController.describeAnimationIdForDebug(entry.animationId), AfwServerAnimationController.describeActorsForDebug(world, entry.actorUuids)), false);
            return QueueStartResult.SKIP;
        }
        List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(def, sorted, world.getRandom());
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

    private static void clearQueuedMobMappings(ServerWorld world, UUID playerUuid, QueuedEntry entry) {
        Map<UUID, UUID> byMob = QUEUED_MOB_TO_PLAYER_BY_WORLD.get(world.getRegistryKey());
        if (byMob == null || byMob.isEmpty() || entry == null) {
            return;
        }
        for (UUID actorUuid : entry.actorUuids) {
            UUID owner;
            if (actorUuid == null || actorUuid.equals(playerUuid) || !playerUuid.equals(owner = byMob.get(actorUuid))) continue;
            byMob.remove(actorUuid);
        }
    }

    private static void clearPlayerQueueByUuid(ServerWorld world, UUID playerUuid, @Nullable AfwDebugChatCategory category, @Nullable Text debugMessage) {
        if (world == null || playerUuid == null) {
            return;
        }
        AfwServerAnimationController.clearDeferredQueueOriginal(world, playerUuid);
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.get(world.getRegistryKey());
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
            PLAYER_QUEUES_BY_WORLD.remove(world.getRegistryKey());
        }
        if (category != null && debugMessage != null) {
            ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(playerUuid);
            AfwServerAnimationController.sendOrLogDebug(world, player, category, debugMessage, false);
        }
    }

    private static void handleQueueOnStop(ServerWorld world, ActiveInstance inst, StopReason reason) {
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

    public static boolean isActorPendingOrActive(ServerWorld world, UUID actorUuid) {
        if (AfwServerAnimationController.isActorActiveIgnoringInstance(world, actorUuid, null)) {
            return true;
        }
        Map<UUID, PlayerQueueState> queues = PLAYER_QUEUES_BY_WORLD.get(world.getRegistryKey());
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

    private static void tickNoAiCleanup(ServerWorld world) {
        RegistryKey key = world.getRegistryKey();
        boolean startupWindowCleanup = AfwServerAnimationController.isWithinStartupNoAiCleanupWindow(world);
        if (!startupWindowCleanup && world.getPlayers().isEmpty()) {
            return;
        }
        if (!startupWindowCleanup) {
            int ticksLeft = NOAI_CLEANUP_TICKS_BY_WORLD.getOrDefault(key, 0) - 1;
            if (ticksLeft > 0) {
                NOAI_CLEANUP_TICKS_BY_WORLD.put((RegistryKey<World>)key, ticksLeft);
                return;
            }
            NOAI_CLEANUP_TICKS_BY_WORLD.put((RegistryKey<World>)key, 6000);
        }
        Map<UUID, AiDisableState> states = AI_DISABLE_STATE_BY_WORLD.get(key);
        for (Entity entity : world.iterateEntities()) {
            AiDisableState state;
            MobEntity mob;
            if (!(entity instanceof MobEntity) || !(mob = (MobEntity)entity).getCommandTags().contains(AFW_NOAI_TAG) && !mob.getCommandTags().contains(AFW_NOAI_ORIG_TAG)) continue;
            UUID uuid = mob.getUuid();
            if (!startupWindowCleanup && AfwServerAnimationController.isActorActive(world, uuid)) continue;
            AiDisableState aiDisableState = state = states == null ? null : states.get(uuid);
            if (state != null) {
                state.locks = 0;
                state.needsRestore = true;
                AfwServerAnimationController.tryRestoreAi(world, uuid, state, states);
                continue;
            }
            boolean originalDisabled = mob.getCommandTags().contains(AFW_NOAI_ORIG_TAG);
            mob.setAiDisabled(originalDisabled);
            AfwServerAnimationController.clearNoAiTags(mob);
        }
        if (states != null && states.isEmpty()) {
            AI_DISABLE_STATE_BY_WORLD.remove(key);
        }
    }

    private static boolean isWithinStartupNoAiCleanupWindow(ServerWorld world) {
        if (world == null || world.getServer() == null) {
            return false;
        }
        int now = world.getServer().getTicks();
        return now <= STARTUP_NOAI_CLEANUP_UNTIL_TICK;
    }

    private static boolean isWithinStartupStartBlockWindow(ServerWorld world) {
        if (world == null || world.getServer() == null) {
            return false;
        }
        int now = world.getServer().getTicks();
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

    public static boolean isActorActive(ServerWorld world, UUID actorUuid) {
        for (ActiveInstance inst : ACTIVE_INSTANCES.values()) {
            if (inst.world != world || !inst.actorUuidSet.contains(actorUuid)) continue;
            return true;
        }
        return false;
    }

    private static void clearNoAiTags(MobEntity mob) {
        mob.removeCommandTag(AFW_NOAI_TAG);
        mob.removeCommandTag(AFW_NOAI_ORIG_TAG);
    }

    private static void restoreAiState(MobEntity mob, AiDisableState state) {
        mob.setVelocity(Vec3d.ZERO);
        mob.fallDistance = 0.0;
        mob.setAiDisabled(state.originalAiDisabled);
        AfwServerAnimationController.clearNoAiTags(mob);
    }

    private static void acquirePlayerLocks(ServerWorld world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        Map states = PLAYER_LOCK_STATE_BY_WORLD.computeIfAbsent((RegistryKey<World>)world.getRegistryKey(), k -> new HashMap());
        for (UUID uuid : actorUuids) {
            Entity entity = world.getEntity(uuid);
            if (!(entity instanceof ServerPlayerEntity)) continue;
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            PlayerLockState existing = (PlayerLockState)states.get(uuid);
            if (existing == null) {
                states.put(uuid, new PlayerLockState(player.hasNoGravity()));
            } else {
                ++existing.locks;
            }
            player.setNoGravity(true);
            player.setVelocity(Vec3d.ZERO);
            player.fallDistance = 0.0;
        }
    }

    private static void releasePlayerLocks(ServerWorld world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        Map<UUID, PlayerLockState> states = PLAYER_LOCK_STATE_BY_WORLD.get(world.getRegistryKey());
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
            ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(uuid);
            if (player != null) {
                player.setNoGravity(state.originalNoGravity);
                player.fallDistance = 0.0;
            }
            states.remove(uuid);
        }
        if (states.isEmpty()) {
            PLAYER_LOCK_STATE_BY_WORLD.remove(world.getRegistryKey());
        }
    }

    private static void clearAllPlayerLocksInWorld(ServerWorld world) {
        if (world == null) {
            return;
        }
        Map<UUID, PlayerLockState> states = PLAYER_LOCK_STATE_BY_WORLD.remove(world.getRegistryKey());
        if (states == null || states.isEmpty()) {
            return;
        }
        for (Map.Entry<UUID, PlayerLockState> entry : states.entrySet()) {
            ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(entry.getKey());
            if (player == null) continue;
            player.setNoGravity(entry.getValue().originalNoGravity);
            player.fallDistance = 0.0;
        }
    }

    private static void cleanupStalePlayerLocks(ServerWorld world, Set<UUID> currentlyLockedPlayers) {
        UUID uuid;
        if (world == null) {
            return;
        }
        Set<Object> lockedNow = currentlyLockedPlayers == null ? Set.of() : currentlyLockedPlayers;
        Map<UUID, PlayerLockState> states = PLAYER_LOCK_STATE_BY_WORLD.get(world.getRegistryKey());
        if (states != null && !states.isEmpty()) {
            Iterator<Map.Entry<UUID, PlayerLockState>> it = states.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, PlayerLockState> entry = it.next();
                uuid = entry.getKey();
                if (lockedNow.contains(uuid) || AfwServerAnimationController.isActorActive(world, uuid)) continue;
                ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(uuid);
                if (player != null) {
                    player.setNoGravity(entry.getValue().originalNoGravity);
                    player.fallDistance = 0.0;
                }
                it.remove();
            }
            if (states.isEmpty()) {
                PLAYER_LOCK_STATE_BY_WORLD.remove(world.getRegistryKey());
            }
        }
        for (ServerPlayerEntity player : world.getPlayers()) {
            uuid = player.getUuid();
            if (lockedNow.contains(uuid) || AfwServerAnimationController.isActorActive(world, uuid) || !player.hasNoGravity() || player.isSpectator() || player.getAbilities().flying) continue;
            player.setNoGravity(false);
            player.fallDistance = 0.0;
        }
    }

    private static void lockActivePlayers(ServerWorld world, Map<UUID, SharedTransform> playersToLock, Set<UUID> underwaterPlayers) {
        if (playersToLock.isEmpty()) {
            return;
        }
        for (Map.Entry<UUID, SharedTransform> entry : playersToLock.entrySet()) {
            Entity e = world.getEntity(entry.getKey());
            if (!(e instanceof ServerPlayerEntity)) continue;
            ServerPlayerEntity player = (ServerPlayerEntity)e;
            AfwServerAnimationController.preparePlayerForAnimationTeleport(player);
            SharedTransform sharedTransform = entry.getValue();
            Vec3d targetPos = sharedTransform.pos();
            double dx = player.getX() - targetPos.x;
            double dy = player.getY() - targetPos.y;
            double dz = player.getZ() - targetPos.z;
            if (dx * dx + dy * dy + dz * dz > 4.0E-4) {
                player.setPosition(targetPos.x, targetPos.y, targetPos.z);
                player.networkHandler.requestTeleport(targetPos.x, targetPos.y, targetPos.z, player.getYaw(), player.getPitch());
            }
            player.setNoGravity(true);
            if (underwaterPlayers.contains(player.getUuid())) {
                player.setAir(player.getMaxAir());
            }
            player.setVelocity(Vec3d.ZERO);
            player.fallDistance = 0.0;
        }
    }

    private static void nudgeNonActiveMobsAwayFromAnimatedPlayers(ServerWorld world, Set<UUID> activePlayerUuids, long nowTick) {
        if (world == null || activePlayerUuids == null || activePlayerUuids.isEmpty()) {
            return;
        }
        if (nowTick % 4L != 0L) {
            return;
        }
        block0: for (UUID playerUuid : activePlayerUuids) {
            Box searchBox;
            List nearby;
            ServerPlayerEntity player = Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayer(playerUuid);
            if (player == null || player.getEntityWorld() != world || !player.isAlive() || player.isRemoved() || (nearby = world.getOtherEntities((Entity)player, searchBox = player.getBoundingBox().expand(1.0, 0.5, 1.0), entity -> {
                MobEntity mob;
                return entity instanceof MobEntity && (mob = (MobEntity)entity).isAlive() && !mob.isRemoved() && !AfwServerAnimationController.isActorPendingOrActive(world, mob.getUuid());
            })).isEmpty()) continue;
            int nudged = 0;
            for (Entity entity2 : nearby) {
                MobEntity mob;
                if (!(entity2 instanceof MobEntity) || AfwServerAnimationController.isActorPendingOrActive(world, (mob = (MobEntity)entity2).getUuid())) continue;
                if (nudged >= 6) continue block0;
                if (!AfwServerAnimationController.nudgeMobAwayFromPlayer(player, mob)) continue;
                ++nudged;
            }
        }
    }

    private static boolean nudgeMobAwayFromPlayer(ServerPlayerEntity player, MobEntity mob) {
        double minDistSq;
        double dz;
        double dx = mob.getX() - player.getX();
        double horizontalDistSq = dx * dx + (dz = mob.getZ() - player.getZ()) * dz;
        if (horizontalDistSq >= (minDistSq = 0.7224999999999999)) {
            return false;
        }
        Vec3d dir = AfwServerAnimationController.resolveHorizontalNudgeDirection(mob, dx, dz, horizontalDistSq);
        double dist = Math.sqrt(Math.max(horizontalDistSq, 1.0E-6));
        double overlap = Math.max(0.0, 0.85 - dist);
        double strength = 0.08 * (0.6 + overlap / 0.85);
        mob.addVelocity(dir.x * strength, 0.0, dir.z * strength);
        mob.fallDistance = 0.0;
        return true;
    }

    private static Vec3d resolveHorizontalNudgeDirection(MobEntity mob, double dx, double dz, double horizontalDistSq) {
        if (horizontalDistSq > 1.0E-6) {
            double invLen = 1.0 / Math.sqrt(horizontalDistSq);
            return new Vec3d(dx * invLen, 0.0, dz * invLen);
        }
        long bits = mob.getUuid().getLeastSignificantBits() ^ mob.getUuid().getMostSignificantBits();
        double angle = (double)(bits & 0xFFFFL) * 9.587379924285257E-5;
        return new Vec3d(Math.cos(angle), 0.0, Math.sin(angle));
    }

    private static void clearAttackTargets(ServerWorld world, Set<UUID> actorUuids) {
        if (actorUuids.isEmpty()) {
            return;
        }
        for (Entity entity : world.iterateEntities()) {
            MobEntity mob;
            LivingEntity target;
            if (!(entity instanceof MobEntity) || (target = (mob = (MobEntity)entity).getTarget()) == null || !actorUuids.contains(target.getUuid())) continue;
            mob.setTarget(null);
            mob.getNavigation().stop();
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

    private static boolean maybeAdvanceStages(ServerWorld world, UUID instanceId, ActiveInstance inst, long now, List<StageAdvance> stageAdvances, List<UUID> toRemove) {
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
                Set<ServerPlayerEntity> targets = AfwServerAnimationController.computeInstanceBroadcastTargets(world, null, inst);
                for (ServerPlayerEntity p : targets) {
                    ServerPlayNetworking.send((ServerPlayerEntity)p, (CustomPayload)payload);
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
    public static AnimationStageInfo getCurrentStage(ServerWorld world, UUID instanceId) {
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return null;
        }
        return AfwServerAnimationController.resolveCurrentStage(inst);
    }

    @Nullable
    public static ActiveInstanceSnapshot getActiveInstanceSnapshot(ServerWorld world, UUID instanceId) {
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

    public static Map<String, String> getInstanceMetadata(ServerWorld world, UUID instanceId) {
        ActiveInstance inst = ACTIVE_INSTANCES.get(instanceId);
        if (inst == null || inst.world != world) {
            return Map.of();
        }
        return inst.metadata;
    }

    private record BlockPlacement(BlockPos wallPos, Direction facing, SharedTransform sharedTransform, @Nullable Vec3d cameraOrbitTarget, boolean centerSupport) {
    }

    private record EligibleDefinition(AfwAnimationDefinitions.Definition definition, List<String> actorKeys, @Nullable BlockPlacement blockPlacement) {
    }

    public record MatchedStartRequest(@Nullable UUID instanceId, Identifier animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
    }

    private record RequirementCheck(boolean valid, @Nullable BlockPlacement blockPlacement) {
    }

    private record AnimationStartProtection(RegistryKey<World> worldKey, long untilTick) {
    }

    private static final class PlayerQueueState {
        final UUID playerUuid;
        final Deque<QueuedEntry> entries = new ArrayDeque<QueuedEntry>();

        PlayerQueueState(UUID playerUuid) {
            this.playerUuid = playerUuid;
        }
    }

    private record QueuedEntry(UUID entryId, Identifier animationId, List<UUID> actorUuids, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata) {
    }

    private static final class ActiveInstance {
        final ServerWorld world;
        final Identifier animationId;
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
        final Vec3d cameraOrbitTarget;
        final Set<UUID> subscribedPlayerUuids = ConcurrentHashMap.newKeySet();
        double speed;

        ActiveInstance(ServerWorld world, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, long startTick, Map<UUID, OriginalTransform> originalTransforms, SharedTransform sharedTransform, boolean lockOrientation, float lockedYaw, float lockedHeadYaw, float lockedPitch, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata, boolean underwaterBreathing, @Nullable UUID playerUuid, Set<UUID> actorUuidSet, StageTracker stageTracker, @Nullable BlockPlacement blockPlacement, @Nullable Vec3d cameraOrbitTarget, double speed) {
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

    private record SharedTransform(Vec3d pos, float yaw, float pitch, float headYaw, float bodyYaw) {
    }

    private record RecentNoRestoreTransition(long expiresTick, SharedTransform sharedTransform, @Nullable BlockPlacement blockPlacement) {
    }

    private record StageAdvance(UUID instanceId, long advanceTick, int stageIndex) {
    }

    private record OriginalTransform(RegistryKey<World> worldKey, Vec3d pos, float yaw, float pitch, float headYaw, float bodyYaw, @Nullable Vec3d restorePos) {
        private OriginalTransform(RegistryKey<World> worldKey, Vec3d pos, float yaw, float pitch, float headYaw, float bodyYaw) {
            this(worldKey, pos, yaw, pitch, headYaw, bodyYaw, null);
        }

        private Vec3d restorePosOrPos() {
            return this.restorePos == null ? this.pos : this.restorePos;
        }
    }

    private record BedFootprintFacing(boolean bedFootprint, @Nullable Direction facing) {
        static BedFootprintFacing none() {
            return new BedFootprintFacing(false, null);
        }

        static BedFootprintFacing invalid() {
            return new BedFootprintFacing(true, null);
        }

        static BedFootprintFacing valid(Direction facing) {
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

    private record FloorHit(boolean found, Vec3d pos) {
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

    public record ActiveInstanceSnapshot(Identifier animationId, List<UUID> actorUuids, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata) {
    }
}

