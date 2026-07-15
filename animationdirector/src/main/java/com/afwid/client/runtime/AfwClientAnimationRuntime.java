/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.sound.SoundEvent
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.registry.Registries
 *  net.minecraft.item.ItemDisplayContext
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.client.runtime;

import com.afwid.api.AfwGeckoModelEvents;
import com.afwid.api.AfwSoundCueEvents;
import com.afwid.client.diagnostics.AfwAnimationAssetDiagnostics;
import com.afwid.client.render.gecko.AfwGeckoResourceResolver;
import com.afwid.client.sound.AfwSoundEffects;
import com.afwid.network.AnimationStageInfo;
import com.afwid.util.AfwStageTimeWarp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundCategory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Registries;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import org.jetbrains.annotations.Nullable;

public final class AfwClientAnimationRuntime {
    private static final List<PendingStart> PENDING_STARTS = new ArrayList<PendingStart>();
    private static final List<PendingStageAdvance> PENDING_STAGE_ADVANCES = new ArrayList<PendingStageAdvance>();
    private static final Map<UUID, InstanceState> INSTANCES = new HashMap<UUID, InstanceState>();
    private static final Map<UUID, Long> PENDING_STOP_TICKS = new HashMap<UUID, Long>();
    private static final Map<UUID, Integer> PENDING_MANUAL_ADVANCE = new HashMap<UUID, Integer>();
    private static final Map<UUID, PendingStageAdvance> PENDING_TARGET_STAGE_ADVANCE = new HashMap<UUID, PendingStageAdvance>();
    private static final Map<UUID, Double> PENDING_SPEED_UPDATES = new HashMap<UUID, Double>();
    private static final ArrayDeque<UUID> COMPLETED_INSTANCES = new ArrayDeque();
    private static final Map<Identifier, Boolean> KNOWN_LOOP_BY_ANIMATION_ID = new HashMap<Identifier, Boolean>();
    private static final Map<UUID, Identifier> ACTIVE_ACTOR_LATEST_ANIMATION_ID = new HashMap<UUID, Identifier>();
    private static final Map<UUID, String> ACTIVE_ACTOR_LATEST_KEY = new HashMap<UUID, String>();
    private static final Map<UUID, Double> ACTIVE_ACTOR_SPEED = new HashMap<UUID, Double>();
    private static final Map<UUID, Map<String, AfwGeckoModelEvents.BoneItemProp>> ACTIVE_ACTOR_BONE_ITEMS = new HashMap<UUID, Map<String, AfwGeckoModelEvents.BoneItemProp>>();
    private static final Map<UUID, RenderHold> ACTOR_RENDER_HOLDS = new HashMap<UUID, RenderHold>();
    private static final Map<UUID, LockedOrientation> LOCKED_ORIENTATION_BY_ACTOR = new HashMap<UUID, LockedOrientation>();
    private static volatile boolean HAS_ACTIVE_INSTANCES = false;
    private static final ArrayDeque<UUID> START_ORDER = new ArrayDeque();
    private static volatile long CURRENT_TICK = 0L;
    private static final long RENDER_HANDOFF_HOLD_TICKS = 1L;
    private static final String PROP_LEFT_BONE = "propleft";
    private static final String PROP_RIGHT_BONE = "propright";
    private static Long STOP_ALL_AT_TICK = null;

    private AfwClientAnimationRuntime() {
    }

    public static void queueStart(Identifier animationId, UUID instanceId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, long startTick, double speed, boolean lockOrientation, float lockedYaw, float lockedHeadYaw, float lockedPitch, Vec3d cameraOrbitTarget) {
        List<String> safeKeys = actorKeys == null ? List.of() : List.copyOf(actorKeys);
        List<AnimationStageInfo> safeStages = stages == null ? List.of() : List.copyOf(stages);
        AfwClientAnimationRuntime.rememberStageMeta(animationId, safeStages);
        AfwAnimationAssetDiagnostics.validateStartPayload(animationId, safeKeys, safeStages);
        UUID anchorUuid = actorUuids.isEmpty() ? null : actorUuids.get(0);
        LockedOrientation locked = lockOrientation ? new LockedOrientation(lockedYaw, lockedHeadYaw, lockedPitch) : null;
        PENDING_STARTS.add(new PendingStart(instanceId, animationId, List.copyOf(actorUuids), safeKeys, safeStages, startTick, AfwClientAnimationRuntime.sanitizeSpeed(speed), anchorUuid, lockOrientation, locked, AfwClientAnimationRuntime.sanitizeCameraOrbitTarget(cameraOrbitTarget)));
    }

    public static void queueStop(UUID instanceId, long stopTick) {
        InstanceState state = INSTANCES.get(instanceId);
        if (state != null) {
            state.stopAtTick = state.stopAtTick == null ? stopTick : Math.min(state.stopAtTick, stopTick);
            return;
        }
        PENDING_STOP_TICKS.merge(instanceId, stopTick, Math::min);
        if (stopTick <= CURRENT_TICK) {
            PENDING_STARTS.removeIf(ps -> ps.instanceId().equals(instanceId));
        }
    }

    public static void queueStopAll(long stopTick) {
        STOP_ALL_AT_TICK = STOP_ALL_AT_TICK == null ? Long.valueOf(stopTick) : Long.valueOf(Math.min(STOP_ALL_AT_TICK, stopTick));
    }

    public static void stopAllNow() {
        STOP_ALL_AT_TICK = null;
        PENDING_STARTS.clear();
        PENDING_STAGE_ADVANCES.clear();
        PENDING_STOP_TICKS.clear();
        PENDING_MANUAL_ADVANCE.clear();
        PENDING_TARGET_STAGE_ADVANCE.clear();
        PENDING_SPEED_UPDATES.clear();
        INSTANCES.clear();
        START_ORDER.clear();
        ACTIVE_ACTOR_LATEST_ANIMATION_ID.clear();
        ACTIVE_ACTOR_LATEST_KEY.clear();
        ACTIVE_ACTOR_SPEED.clear();
        ACTIVE_ACTOR_BONE_ITEMS.clear();
        ACTOR_RENDER_HOLDS.clear();
        LOCKED_ORIENTATION_BY_ACTOR.clear();
        HAS_ACTIVE_INSTANCES = false;
        COMPLETED_INSTANCES.clear();
        KNOWN_LOOP_BY_ANIMATION_ID.clear();
    }

    public static void requestStageAdvance(UUID instanceId) {
        InstanceState state = INSTANCES.get(instanceId);
        if (state != null) {
            ++state.manualAdvanceRequests;
            return;
        }
        PENDING_MANUAL_ADVANCE.merge(instanceId, 1, Integer::sum);
    }

    public static boolean advanceStage(UUID instanceId) {
        InstanceState state = INSTANCES.get(instanceId);
        if (state == null) {
            return false;
        }
        ++state.manualAdvanceRequests;
        return true;
    }

    public static void queueStageAdvance(UUID instanceId, long advanceTick, int stageIndex) {
        if (advanceTick <= CURRENT_TICK) {
            AfwClientAnimationRuntime.requestStageAdvanceTo(instanceId, stageIndex, advanceTick);
            return;
        }
        PENDING_STAGE_ADVANCES.add(new PendingStageAdvance(instanceId, advanceTick, stageIndex));
    }

    private static void requestStageAdvanceTo(UUID instanceId, int stageIndex, long advanceTick) {
        InstanceState state = INSTANCES.get(instanceId);
        if (state != null) {
            state.setStageIndex(stageIndex, Math.max(0L, advanceTick));
            Double queuedSpeed = PENDING_SPEED_UPDATES.remove(instanceId);
            if (queuedSpeed != null) {
                AfwClientAnimationRuntime.applySpeedToState(state, queuedSpeed, Math.max(0L, advanceTick));
            }
            return;
        }
        PENDING_TARGET_STAGE_ADVANCE.put(instanceId, new PendingStageAdvance(instanceId, Math.max(0L, advanceTick), stageIndex));
    }

    public static void queueSpeedUpdate(UUID instanceId, double speed) {
        double sanitized = AfwClientAnimationRuntime.sanitizeSpeed(speed);
        InstanceState state = INSTANCES.get(instanceId);
        if (state != null) {
            MinecraftClient client;
            if (AfwClientAnimationRuntime.hasPendingStageAdvance(instanceId)) {
                PENDING_SPEED_UPDATES.put(instanceId, sanitized);
                return;
            }
            long now = CURRENT_TICK;
            if (now == 0L && (client = MinecraftClient.getInstance()) != null && client.world != null) {
                now = client.world.getTime();
            }
            AfwClientAnimationRuntime.applySpeedToState(state, sanitized, now);
            return;
        }
        PENDING_SPEED_UPDATES.put(instanceId, sanitized);
    }

    private static boolean hasPendingStageAdvance(UUID instanceId) {
        if (instanceId == null) {
            return false;
        }
        if (PENDING_TARGET_STAGE_ADVANCE.containsKey(instanceId)) {
            return true;
        }
        for (PendingStageAdvance pending : PENDING_STAGE_ADVANCES) {
            if (pending == null || !instanceId.equals(pending.instanceId())) continue;
            return true;
        }
        return false;
    }

    public static List<UUID> drainCompletedInstances() {
        if (COMPLETED_INSTANCES.isEmpty()) {
            return List.of();
        }
        ArrayList<UUID> out = new ArrayList<UUID>(COMPLETED_INSTANCES);
        COMPLETED_INSTANCES.clear();
        return out;
    }

    public static void clientTick(MinecraftClient client) {
        InstanceState state3;
        long now;
        if (client.world == null) {
            return;
        }
        CURRENT_TICK = now = client.world.getTime();
        if (STOP_ALL_AT_TICK != null && now >= STOP_ALL_AT_TICK) {
            AfwClientAnimationRuntime.stopAllNow();
            return;
        }
        if (PENDING_STARTS.isEmpty() && INSTANCES.isEmpty() && ACTOR_RENDER_HOLDS.isEmpty() && PENDING_TARGET_STAGE_ADVANCE.isEmpty() && STOP_ALL_AT_TICK == null) {
            if (!PENDING_STOP_TICKS.isEmpty()) {
                PENDING_STOP_TICKS.entrySet().removeIf(e -> (Long)e.getValue() <= now);
            }
            if (!PENDING_STAGE_ADVANCES.isEmpty()) {
                PENDING_STAGE_ADVANCES.removeIf(p -> p.advanceTick() <= now);
            }
            if (!(ACTIVE_ACTOR_LATEST_ANIMATION_ID.isEmpty() && ACTIVE_ACTOR_LATEST_KEY.isEmpty() && ACTIVE_ACTOR_SPEED.isEmpty() && ACTIVE_ACTOR_BONE_ITEMS.isEmpty())) {
                ACTIVE_ACTOR_LATEST_ANIMATION_ID.clear();
                ACTIVE_ACTOR_LATEST_KEY.clear();
                ACTIVE_ACTOR_SPEED.clear();
                ACTIVE_ACTOR_BONE_ITEMS.clear();
                HAS_ACTIVE_INSTANCES = false;
            }
            START_ORDER.clear();
            return;
        }
        if (!ACTOR_RENDER_HOLDS.isEmpty()) {
            ACTOR_RENDER_HOLDS.entrySet().removeIf(e -> now > ((RenderHold)e.getValue()).expiresAtTick());
        }
        int i = 0;
        while (i < PENDING_STARTS.size()) {
            PendingStart ps = PENDING_STARTS.get(i);
            if (now >= ps.startTick()) {
                Long pendingStop;
                LockedOrientation anchorOrientation = ps.lockOrientation() ? ps.lockedOrientation() : AfwClientAnimationRuntime.resolveOrientation(client, ps.anchorUuid());
                double initialSpeed = AfwClientAnimationRuntime.sanitizeSpeed(ps.speed());
                Double queuedSpeed = PENDING_SPEED_UPDATES.remove(ps.instanceId());
                if (queuedSpeed != null) {
                    initialSpeed = queuedSpeed;
                }
                InstanceState state2 = new InstanceState(ps.instanceId(), ps.animationId(), ps.actorUuids(), ps.actorKeys(), ps.stages(), ps.startTick(), initialSpeed, ps.anchorUuid(), anchorOrientation, ps.cameraOrbitTarget());
                PendingStageAdvance pendingTargetStage = PENDING_TARGET_STAGE_ADVANCE.remove(ps.instanceId());
                if (pendingTargetStage != null) {
                    state2.setStageIndex(pendingTargetStage.stageIndex(), pendingTargetStage.advanceTick());
                }
                Integer pendingAdvance = PENDING_MANUAL_ADVANCE.remove(ps.instanceId());
                if (pendingTargetStage == null && pendingAdvance != null) {
                    state2.manualAdvanceRequests = pendingAdvance;
                }
                if ((pendingStop = PENDING_STOP_TICKS.remove(ps.instanceId())) != null) {
                    state2.stopAtTick = pendingStop;
                }
                INSTANCES.put(ps.instanceId(), state2);
                START_ORDER.addLast(ps.instanceId());
                for (UUID actorUuid : ps.actorUuids()) {
                    ACTOR_RENDER_HOLDS.remove(actorUuid);
                }
                PENDING_STARTS.remove(i);
                continue;
            }
            ++i;
        }
        if (!PENDING_STAGE_ADVANCES.isEmpty()) {
            i = 0;
            while (i < PENDING_STAGE_ADVANCES.size()) {
                PendingStageAdvance pending = PENDING_STAGE_ADVANCES.get(i);
                if (now >= pending.advanceTick()) {
                    AfwClientAnimationRuntime.requestStageAdvanceTo(pending.instanceId(), pending.stageIndex(), pending.advanceTick());
                    PENDING_STAGE_ADVANCES.remove(i);
                    continue;
                }
                ++i;
            }
        }
        if (!INSTANCES.isEmpty()) {
            Iterator<Map.Entry<UUID, InstanceState>> expiredIt = INSTANCES.entrySet().iterator();
            while (expiredIt.hasNext()) {
                Map.Entry<UUID, InstanceState> entry = expiredIt.next();
                state3 = (InstanceState)entry.getValue();
                if (now < state3.effectiveStopTick()) continue;
                AfwClientAnimationRuntime.rememberRenderHolds(state3, now);
                expiredIt.remove();
            }
        }
        if (!INSTANCES.isEmpty()) {
            ArrayList<UUID> completed = new ArrayList<UUID>();
            block4: for (InstanceState instanceState : INSTANCES.values()) {
                if (now < instanceState.startTick) continue;
                while (instanceState.manualAdvanceRequests > 0) {
                    --instanceState.manualAdvanceRequests;
                    if (instanceState.advanceStage(now)) {
                        Double queued = PENDING_SPEED_UPDATES.remove(instanceState.instanceId);
                        if (queued == null) continue;
                        AfwClientAnimationRuntime.applySpeedToState(instanceState, queued, now);
                        continue;
                    }
                    completed.add(instanceState.instanceId);
                    continue block4;
                }
            }
            if (!completed.isEmpty()) {
                for (UUID id2 : completed) {
                    InstanceState completedState = INSTANCES.remove(id2);
                    if (completedState != null) {
                        AfwClientAnimationRuntime.rememberRenderHolds(completedState, now);
                    }
                    PENDING_STOP_TICKS.remove(id2);
                    PENDING_MANUAL_ADVANCE.remove(id2);
                    PENDING_TARGET_STAGE_ADVANCE.remove(id2);
                    PENDING_SPEED_UPDATES.remove(id2);
                    START_ORDER.removeIf(existing -> existing.equals(id2));
                    COMPLETED_INSTANCES.add(id2);
                }
            }
        }
        if (START_ORDER.size() > 256) {
            ArrayDeque<UUID> compact = new ArrayDeque<UUID>(START_ORDER.size());
            for (UUID id2 : START_ORDER) {
                if (!INSTANCES.containsKey(id2)) continue;
                compact.addLast(id2);
            }
            START_ORDER.clear();
            START_ORDER.addAll(compact);
        } else {
            START_ORDER.removeIf(id -> !INSTANCES.containsKey(id));
        }
        ACTIVE_ACTOR_LATEST_ANIMATION_ID.clear();
        ACTIVE_ACTOR_LATEST_KEY.clear();
        ACTIVE_ACTOR_SPEED.clear();
        ACTIVE_ACTOR_BONE_ITEMS.clear();
        Iterator<UUID> it = START_ORDER.descendingIterator();
        while (it.hasNext()) {
            UUID id3 = it.next();
            state3 = INSTANCES.get(id3);
            if (state3 == null) continue;
            for (int i2 = 0; i2 < state3.actorUuids.size(); ++i2) {
                String actorKey;
                UUID actorUuid = state3.actorUuids.get(i2);
                ACTIVE_ACTOR_LATEST_ANIMATION_ID.putIfAbsent(actorUuid, state3.currentAnimationId());
                ACTIVE_ACTOR_SPEED.putIfAbsent(actorUuid, state3.speed);
                ACTIVE_ACTOR_BONE_ITEMS.putIfAbsent(actorUuid, AfwClientAnimationRuntime.resolveActorBoneItems(state3, i2));
                if (i2 >= state3.actorKeys.size() || (actorKey = state3.actorKeys.get(i2)) == null || actorKey.isEmpty()) continue;
                ACTIVE_ACTOR_LATEST_KEY.putIfAbsent(actorUuid, actorKey);
            }
        }
        if (!ACTOR_RENDER_HOLDS.isEmpty()) {
            for (Map.Entry<UUID, RenderHold> entry : ACTOR_RENDER_HOLDS.entrySet()) {
                UUID actorUuid = entry.getKey();
                RenderHold hold = entry.getValue();
                ACTIVE_ACTOR_LATEST_ANIMATION_ID.putIfAbsent(actorUuid, hold.animationId());
                ACTIVE_ACTOR_SPEED.putIfAbsent(actorUuid, hold.speed());
                ACTIVE_ACTOR_BONE_ITEMS.putIfAbsent(actorUuid, hold.boneItems());
                if (hold.actorKey() == null || hold.actorKey().isEmpty()) continue;
                ACTIVE_ACTOR_LATEST_KEY.putIfAbsent(actorUuid, hold.actorKey());
            }
        }
        HAS_ACTIVE_INSTANCES = !ACTIVE_ACTOR_LATEST_ANIMATION_ID.isEmpty();
        AfwClientAnimationRuntime.syncLockedOrientations(client);
        AfwClientAnimationRuntime.tickSoundCues(client, now);
    }

    private static void rememberRenderHolds(InstanceState state, long nowTick) {
        if (state == null || state.actorUuids.isEmpty()) {
            return;
        }
        long expiresAt = nowTick + 1L;
        for (int i = 0; i < state.actorUuids.size(); ++i) {
            UUID actorUuid = state.actorUuids.get(i);
            if (actorUuid == null) continue;
            String actorKey = i < state.actorKeys.size() ? state.actorKeys.get(i) : null;
            ACTOR_RENDER_HOLDS.put(actorUuid, new RenderHold(state.instanceId, state.currentAnimationId(), actorKey, state.speed, AfwClientAnimationRuntime.resolveActorBoneItems(state, i), state.anchorOrientation, expiresAt));
        }
    }

    public static boolean hasActiveInstances() {
        return HAS_ACTIVE_INSTANCES;
    }

    public static boolean isActorActive(UUID actorUuid) {
        return ACTIVE_ACTOR_LATEST_ANIMATION_ID.containsKey(actorUuid);
    }

    public static boolean isActorPendingOrActive(UUID actorUuid) {
        if (ACTIVE_ACTOR_LATEST_ANIMATION_ID.containsKey(actorUuid)) {
            return true;
        }
        for (PendingStart ps : PENDING_STARTS) {
            for (UUID uuid : ps.actorUuids()) {
                if (!uuid.equals(actorUuid)) continue;
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static LockedOrientation getLockedOrientation(UUID actorUuid) {
        LockedOrientation locked = LOCKED_ORIENTATION_BY_ACTOR.get(actorUuid);
        if (locked != null) {
            return locked;
        }
        RenderHold hold = ACTOR_RENDER_HOLDS.get(actorUuid);
        if (hold != null && CURRENT_TICK <= hold.expiresAtTick()) {
            return hold.lockedOrientation();
        }
        return null;
    }

    @Nullable
    public static Vec3d findCameraOrbitTargetForActor(UUID actorUuid) {
        if (actorUuid == null) {
            return null;
        }
        long now = CURRENT_TICK;
        Iterator<UUID> it = START_ORDER.descendingIterator();
        while (it.hasNext()) {
            UUID id = it.next();
            InstanceState state = INSTANCES.get(id);
            if (state == null || !state.isActive(now) || !state.containsActor(actorUuid)) continue;
            return state.cameraOrbitTarget;
        }
        return null;
    }

    @Nullable
    public static UUID findAnchorUuidForActor(UUID actorUuid) {
        for (InstanceState state : INSTANCES.values()) {
            if (!state.containsActor(actorUuid)) continue;
            return state.anchorUuid;
        }
        for (PendingStart ps : PENDING_STARTS) {
            for (UUID uuid : ps.actorUuids()) {
                if (!uuid.equals(actorUuid)) continue;
                return ps.anchorUuid();
            }
        }
        return null;
    }

    @Nullable
    public static Long findStartTickForActor(UUID actorUuid) {
        for (InstanceState state : INSTANCES.values()) {
            if (!state.containsActor(actorUuid)) continue;
            return state.startTick;
        }
        for (PendingStart ps : PENDING_STARTS) {
            for (UUID uuid : ps.actorUuids()) {
                if (!uuid.equals(actorUuid)) continue;
                return ps.startTick();
            }
        }
        return null;
    }

    public static Identifier findLatestActiveAnimationIdContaining(UUID actorUuid) {
        return ACTIVE_ACTOR_LATEST_ANIMATION_ID.get(actorUuid);
    }

    public static String findLatestActiveActorKey(UUID actorUuid) {
        return ACTIVE_ACTOR_LATEST_KEY.get(actorUuid);
    }

    public static double findLatestSpeedForActor(UUID actorUuid) {
        return ACTIVE_ACTOR_SPEED.getOrDefault(actorUuid, 1.0);
    }

    public static Map<String, AfwGeckoModelEvents.BoneItemProp> findLatestBoneItemsForActor(UUID actorUuid) {
        Map<String, AfwGeckoModelEvents.BoneItemProp> props = ACTIVE_ACTOR_BONE_ITEMS.get(actorUuid);
        return props == null || props.isEmpty() ? Map.of() : props;
    }

    public static void setPropOverride(UUID instanceId, UUID actorUuid, PropSlot slot, ItemStack stack) {
        if (instanceId == null || actorUuid == null || slot == null) {
            return;
        }
        InstanceState state = INSTANCES.get(instanceId);
        if (state == null) {
            return;
        }
        EnumMap actorOverrides = state.propOverridesByActor.computeIfAbsent(actorUuid, unused -> new EnumMap(PropSlot.class));
        if (stack == null || stack.isEmpty()) {
            actorOverrides.remove((Object)slot);
        } else {
            actorOverrides.put(slot, stack.copy());
        }
        if (actorOverrides.isEmpty()) {
            state.propOverridesByActor.remove(actorUuid);
        }
    }

    public static void setLeftHandPropOverride(UUID instanceId, UUID actorUuid, ItemStack stack) {
        AfwClientAnimationRuntime.setPropOverride(instanceId, actorUuid, PropSlot.LEFT, stack);
    }

    public static void setRightHandPropOverride(UUID instanceId, UUID actorUuid, ItemStack stack) {
        AfwClientAnimationRuntime.setPropOverride(instanceId, actorUuid, PropSlot.RIGHT, stack);
    }

    public static void clearPropOverride(UUID instanceId, UUID actorUuid, PropSlot slot) {
        if (instanceId == null || actorUuid == null || slot == null) {
            return;
        }
        InstanceState state = INSTANCES.get(instanceId);
        if (state == null) {
            return;
        }
        EnumMap<PropSlot, ItemStack> actorOverrides = state.propOverridesByActor.get(actorUuid);
        if (actorOverrides == null) {
            return;
        }
        actorOverrides.remove((Object)slot);
        if (actorOverrides.isEmpty()) {
            state.propOverridesByActor.remove(actorUuid);
        }
    }

    public static void clearLeftHandPropOverride(UUID instanceId, UUID actorUuid) {
        AfwClientAnimationRuntime.clearPropOverride(instanceId, actorUuid, PropSlot.LEFT);
    }

    public static void clearRightHandPropOverride(UUID instanceId, UUID actorUuid) {
        AfwClientAnimationRuntime.clearPropOverride(instanceId, actorUuid, PropSlot.RIGHT);
    }

    public static void clearPropOverrides(UUID instanceId, UUID actorUuid) {
        if (instanceId == null || actorUuid == null) {
            return;
        }
        InstanceState state = INSTANCES.get(instanceId);
        if (state == null) {
            return;
        }
        state.propOverridesByActor.remove(actorUuid);
    }

    public static UUID findLatestActiveInstanceContaining(UUID actorUuid) {
        long now = CURRENT_TICK;
        Iterator<UUID> it = START_ORDER.descendingIterator();
        while (it.hasNext()) {
            UUID id = it.next();
            InstanceState state = INSTANCES.get(id);
            if (state == null || !state.isActive(now) || !state.containsActor(actorUuid)) continue;
            return id;
        }
        RenderHold hold = ACTOR_RENDER_HOLDS.get(actorUuid);
        if (hold != null && now <= hold.expiresAtTick()) {
            return hold.instanceId();
        }
        return null;
    }

    public static boolean shouldHideNameLabelForViewer(UUID viewerUuid, UUID renderedUuid) {
        if (viewerUuid == null || renderedUuid == null || viewerUuid.equals(renderedUuid)) {
            return false;
        }
        UUID viewerInstance = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(viewerUuid);
        if (viewerInstance == null) {
            return false;
        }
        UUID renderedInstance = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(renderedUuid);
        return viewerInstance.equals(renderedInstance);
    }

    public static double findOutsideViewerNameLabelYOffset(UUID viewerUuid, UUID renderedUuid) {
        if (renderedUuid == null) {
            return 0.0;
        }
        InstanceState state = AfwClientAnimationRuntime.findLatestActiveStateContaining(renderedUuid);
        if (state == null) {
            return 0.0;
        }
        if (viewerUuid != null && state.containsActor(viewerUuid)) {
            return 0.0;
        }
        int actorIndex = state.actorUuids.indexOf(renderedUuid);
        if (actorIndex <= 0) {
            return 0.0;
        }
        return Math.min(1.5, (double)actorIndex * 0.28);
    }

    private static InstanceState findLatestActiveStateContaining(UUID actorUuid) {
        if (actorUuid == null) {
            return null;
        }
        long now = CURRENT_TICK;
        Iterator<UUID> it = START_ORDER.descendingIterator();
        while (it.hasNext()) {
            UUID id = it.next();
            InstanceState state = INSTANCES.get(id);
            if (state == null || !state.isActive(now) || !state.containsActor(actorUuid)) continue;
            return state;
        }
        return null;
    }

    public static AnimationStageInfo findCurrentStage(UUID instanceId) {
        InstanceState state = INSTANCES.get(instanceId);
        if (state == null) {
            return null;
        }
        return state.currentStage();
    }

    public static long findStageStartTick(UUID instanceId) {
        InstanceState state = INSTANCES.get(instanceId);
        if (state == null) {
            return -1L;
        }
        return state.stageStartTick;
    }

    public static Double findWarpedAuthoredTimelineSeconds(UUID actorUuid, float tickDelta) {
        if (actorUuid == null) {
            return null;
        }
        long now = CURRENT_TICK;
        Iterator<UUID> it = START_ORDER.descendingIterator();
        while (it.hasNext()) {
            UUID id = it.next();
            InstanceState state = INSTANCES.get(id);
            if (state == null || !state.isActive(now) || !state.containsActor(actorUuid)) continue;
            AnimationStageInfo stage = state.currentStage();
            double cycleTicks = AfwClientAnimationRuntime.exactCycleTicks(stage);
            if (stage == null || !stage.loop() || cycleTicks <= 0.0) {
                return null;
            }
            double offsetTicks = AfwStageTimeWarp.offsetSecondsToTicks(stage.cycleMidpointOffsetSeconds());
            if (!AfwStageTimeWarp.hasWarp(cycleTicks, offsetTicks)) {
                return null;
            }
            double warpedTicks = Math.max(0.0, (double)((float)now + Math.max(0.0f, tickDelta) - (float)state.stageStartTick) * state.speed);
            double authoredTicks = AfwStageTimeWarp.warpedToAuthoredCycleTicks(warpedTicks, cycleTicks, offsetTicks);
            return authoredTicks / 20.0;
        }
        return null;
    }

    public static UUID findLatestActiveInstance() {
        long now = CURRENT_TICK;
        Iterator<UUID> it = START_ORDER.descendingIterator();
        while (it.hasNext()) {
            UUID id = it.next();
            InstanceState state = INSTANCES.get(id);
            if (state == null || !state.isActive(now)) continue;
            return id;
        }
        return null;
    }

    public static boolean isAnimationLooping(Identifier animationId) {
        if (animationId == null) {
            return true;
        }
        Boolean loop = KNOWN_LOOP_BY_ANIMATION_ID.get(animationId);
        return loop != null ? loop : true;
    }

    private static List<AnimationStageInfo> normalizeStages(Identifier baseAnimationId, List<AnimationStageInfo> stages) {
        if (stages == null || stages.isEmpty()) {
            return List.of(new AnimationStageInfo(baseAnimationId, true, 0L, true, 1.0));
        }
        return List.copyOf(stages);
    }

    private static double sanitizeSpeed(double speed) {
        if (Double.isNaN(speed) || Double.isInfinite(speed) || speed <= 0.0) {
            return 1.0;
        }
        return Math.max(0.1, Math.min(speed, 4.0));
    }

    private static double exactCycleTicks(AnimationStageInfo stage) {
        if (stage == null) {
            return 0.0;
        }
        double exact = stage.cycleTicks();
        if (Double.isFinite(exact) && exact > 0.0) {
            return exact;
        }
        return Math.max(0L, stage.lengthTicks());
    }

    private static Vec3d sanitizeCameraOrbitTarget(Vec3d target) {
        return target != null && Double.isFinite(target.x) && Double.isFinite(target.y)
                && Double.isFinite(target.z) ? target : null;
    }

    private static long scaledDuration(long duration, double speed) {
        if (duration <= 0L) {
            return duration;
        }
        double safeSpeed = Math.max(1.0E-4, AfwClientAnimationRuntime.sanitizeSpeed(speed));
        return (long)Math.ceil((double)duration / safeSpeed);
    }

    private static void applySpeedToState(InstanceState state, double newSpeed, long nowTick) {
        double oldSpeed = state.speed;
        double sanitized = AfwClientAnimationRuntime.sanitizeSpeed(newSpeed);
        if (Math.abs(oldSpeed - sanitized) < 1.0E-4) {
            state.speed = sanitized;
            return;
        }
        AnimationStageInfo current = state.currentStage();
        long duration = Math.max(0L, current.lengthTicks());
        if (duration > 0L) {
            long elapsed = Math.max(0L, nowTick - state.stageStartTick);
            double normalizedProgress = (double)elapsed * oldSpeed;
            long adjustedStart = nowTick - Math.round(normalizedProgress / sanitized);
            state.stageStartTick = Math.max(state.startTick, adjustedStart);
        }
        state.speed = sanitized;
    }

    private static void rememberStageMeta(Identifier baseAnimationId, List<AnimationStageInfo> stages) {
        if (stages == null || stages.isEmpty()) {
            KNOWN_LOOP_BY_ANIMATION_ID.putIfAbsent(baseAnimationId, true);
            return;
        }
        for (AnimationStageInfo stage : stages) {
            if (stage == null || stage.animationId() == null) continue;
            KNOWN_LOOP_BY_ANIMATION_ID.put(stage.animationId(), stage.loop());
            Identifier playbackId = stage.effectiveAnimationId();
            if (playbackId == null) continue;
            KNOWN_LOOP_BY_ANIMATION_ID.put(playbackId, stage.loop());
        }
    }

    private static Map<String, AfwGeckoModelEvents.BoneItemProp> resolveActorBoneItems(InstanceState state, int actorIndex) {
        EnumMap<PropSlot, ItemStack> actorOverrides;
        if (state == null || actorIndex < 0 || actorIndex >= state.actorUuids.size()) {
            return Map.of();
        }
        String actorKey = actorIndex < state.actorKeys.size() ? state.actorKeys.get(actorIndex) : null;
        UUID actorUuid = state.actorUuids.get(actorIndex);
        LinkedHashMap<String, AfwGeckoModelEvents.BoneItemProp> out = new LinkedHashMap<String, AfwGeckoModelEvents.BoneItemProp>();
        AnimationStageInfo.ActorPropEntry stageProps = state.currentStage().actorPropsForKey(actorKey);
        if (stageProps != null) {
            AfwGeckoModelEvents.BoneItemProp left = stageProps.leftBoneProp();
            AfwGeckoModelEvents.BoneItemProp right = stageProps.rightBoneProp();
            if (left != null) {
                out.put(PROP_LEFT_BONE, left);
            }
            if (right != null) {
                out.put(PROP_RIGHT_BONE, right);
            }
        }
        if ((actorOverrides = state.propOverridesByActor.get(actorUuid)) != null && !actorOverrides.isEmpty()) {
            for (Map.Entry<PropSlot, ItemStack> entry : actorOverrides.entrySet()) {
                PropSlot slot = entry.getKey();
                ItemStack stack = entry.getValue();
                if (slot == null) continue;
                if (stack == null || stack.isEmpty()) {
                    out.remove(slot.boneName());
                    continue;
                }
                out.put(slot.boneName(), new AfwGeckoModelEvents.BoneItemProp(stack, slot.displayContext()));
            }
        }
        return out.isEmpty() ? Map.of() : Map.copyOf(out);
    }

    private static void syncLockedOrientations(MinecraftClient client) {
        if (client == null || client.world == null) {
            return;
        }
        if (ACTIVE_ACTOR_LATEST_ANIMATION_ID.isEmpty()) {
            LOCKED_ORIENTATION_BY_ACTOR.clear();
            return;
        }
        HashMap<UUID, LockedOrientation> rebuilt = new HashMap<UUID, LockedOrientation>();
        Iterator<UUID> it = START_ORDER.descendingIterator();
        while (it.hasNext()) {
            UUID instanceId = it.next();
            InstanceState state = INSTANCES.get(instanceId);
            if (state == null) continue;
            if (state.anchorOrientation == null) {
                state.anchorOrientation = AfwClientAnimationRuntime.resolveOrientation(client, state.anchorUuid);
            }
            LockedOrientation anchor = state.anchorOrientation;
            for (UUID actorUuid : state.actorUuids) {
                if (!ACTIVE_ACTOR_LATEST_ANIMATION_ID.containsKey(actorUuid) || rebuilt.containsKey(actorUuid)) continue;
                if (anchor != null) {
                    rebuilt.put(actorUuid, anchor);
                    continue;
                }
                Entity entity = findEntity(client.world, actorUuid);
                if (!(entity instanceof LivingEntity)) continue;
                LivingEntity living = (LivingEntity)entity;
                rebuilt.put(actorUuid, new LockedOrientation(living.getBodyYaw(), living.getHeadYaw(), living.getPitch()));
            }
        }
        LOCKED_ORIENTATION_BY_ACTOR.clear();
        LOCKED_ORIENTATION_BY_ACTOR.putAll(rebuilt);
    }

    private static LockedOrientation resolveOrientation(MinecraftClient client, UUID uuid) {
        if (uuid == null || client == null || client.world == null) {
            return null;
        }
        Entity entity = findEntity(client.world, uuid);
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            return new LockedOrientation(living.getBodyYaw(), living.getHeadYaw(), living.getPitch());
        }
        return null;
    }

    private static void tickSoundCues(MinecraftClient client, long now) {
        if (client == null || client.world == null) {
            return;
        }
        if (INSTANCES.isEmpty()) {
            return;
        }
        if (!AfwSoundEffects.hasAny()) {
            return;
        }
        ClientWorld world = client.world;
        for (InstanceState state : INSTANCES.values()) {
            Identifier stageAnimationId;
            MergedSoundTrack soundTrack;
            if (now < state.startTick || now >= state.effectiveStopTick() || state.actorUuids.isEmpty()) continue;
            if (state.soundStageIndex != state.stageIndex) {
                state.soundStageIndex = state.stageIndex;
                state.lastSoundTick = -1.0;
            }
            if ((soundTrack = AfwClientAnimationRuntime.resolveMergedSoundTrack(world, state, stageAnimationId = state.currentAnimationId())) == null || soundTrack.cues().isEmpty()) continue;
            double animTicks = Math.max(0.0, (double)(now - state.stageStartTick) * state.speed);
            double lastTicks = state.lastSoundTick;
            boolean loop = state.currentStage().loop();
            double cycleTicks = soundTrack.cycleTicks();
            if (loop && cycleTicks > 0.0) {
                AfwClientAnimationRuntime.fireLoopingCues(soundTrack.anchor(), state, soundTrack.cues(), stageAnimationId, lastTicks, animTicks, cycleTicks);
            } else {
                AfwClientAnimationRuntime.fireLinearCues(soundTrack.anchor(), state, soundTrack.cues(), stageAnimationId, lastTicks, animTicks, cycleTicks);
            }
            state.lastSoundTick = animTicks;
        }
    }

    private static MergedSoundTrack resolveMergedSoundTrack(ClientWorld world, InstanceState state, Identifier stageAnimationId) {
        if (world == null || state == null || stageAnimationId == null) {
            return null;
        }
        int count = state.actorUuids.size();
        if (count == 0) {
            return null;
        }
        LivingEntity anchor = AfwClientAnimationRuntime.resolveSoundAnchor(world, state);
        if (anchor == null) {
            return null;
        }
        ArrayList<MergedSoundCue> rawCues = new ArrayList<MergedSoundCue>();
        double maxLengthTicks = 0.0;
        for (int index : AfwClientAnimationRuntime.soundActorOrder(state)) {
            SoundTrackCandidate candidate = AfwClientAnimationRuntime.resolveSoundCandidateForIndex(world, state, stageAnimationId, index);
            if (candidate == null) continue;
            maxLengthTicks = Math.max(maxLengthTicks, candidate.soundAnimation().lengthTicks());
            for (AfwSoundEffects.SoundCue cue : candidate.soundAnimation().cues()) {
                if (cue == null) continue;
                rawCues.add(new MergedSoundCue(candidate.animationResource(), candidate.animationKey(), cue, cue.timeTicks()));
            }
        }
        if (rawCues.isEmpty()) {
            return null;
        }
        double exactStageCycleTicks = AfwClientAnimationRuntime.exactCycleTicks(state.currentStage());
        double cycleTicks = exactStageCycleTicks > 0.0 ? exactStageCycleTicks : maxLengthTicks;
        double offsetTicks = AfwStageTimeWarp.offsetSecondsToTicks(state.currentStage().cycleMidpointOffsetSeconds());
        boolean warp = state.currentStage().loop() && AfwStageTimeWarp.hasWarp(cycleTicks, offsetTicks);
        LinkedHashMap<Long, MergedSoundCue> byTime = new LinkedHashMap<Long, MergedSoundCue>();
        for (MergedSoundCue rawCue : rawCues) {
            double cueTimeTicks = warp ? AfwClientAnimationRuntime.warpCueTick(rawCue.cue().timeTicks(), cycleTicks, offsetTicks) : rawCue.cue().timeTicks();
            long timeKey = AfwClientAnimationRuntime.soundCueTimeKey(cueTimeTicks);
            byTime.putIfAbsent(timeKey, new MergedSoundCue(rawCue.animationResource(), rawCue.animationKey(), rawCue.cue(), cueTimeTicks));
        }
        if (byTime.isEmpty()) {
            return null;
        }
        ArrayList cues = new ArrayList(byTime.values());
        cues.sort(Comparator.comparingDouble(MergedSoundCue::timeTicks));
        return new MergedSoundTrack(anchor, List.copyOf(cues), cycleTicks);
    }

    private static LivingEntity resolveSoundAnchor(ClientWorld world, InstanceState state) {
        Entity entity;
        if (world == null || state == null) {
            return null;
        }
        if (state.anchorUuid != null && (entity = findEntity(world, state.anchorUuid)) instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            return living;
        }
        for (UUID actorId : state.actorUuids) {
            Entity entity2 = findEntity(world, actorId);
            if (!(entity2 instanceof LivingEntity)) continue;
            LivingEntity living = (LivingEntity)entity2;
            return living;
        }
        return null;
    }

    private static List<Integer> soundActorOrder(InstanceState state) {
        int i;
        if (state == null || state.actorUuids.isEmpty()) {
            return List.of();
        }
        ArrayList<Integer> order = new ArrayList<Integer>(state.actorUuids.size());
        int anchorIndex = -1;
        if (state.anchorUuid != null) {
            for (i = 0; i < state.actorUuids.size(); ++i) {
                if (!state.actorUuids.get(i).equals(state.anchorUuid)) continue;
                anchorIndex = i;
                break;
            }
        }
        if (anchorIndex >= 0) {
            order.add(anchorIndex);
        }
        for (i = 0; i < state.actorUuids.size(); ++i) {
            if (i == anchorIndex) continue;
            order.add(i);
        }
        return order;
    }

    private static long soundCueTimeKey(double timeTicks) {
        if (!Double.isFinite(timeTicks)) {
            return Long.MIN_VALUE;
        }
        return Math.round(timeTicks * 1000.0);
    }

    private static double warpCueTick(double cueTick, double cycleTicks, double offsetTicks) {
        if (!AfwStageTimeWarp.hasWarp(cycleTicks, offsetTicks)) {
            return cueTick;
        }
        double warped = AfwStageTimeWarp.authoredToWarpedCycleTicks(cueTick, cycleTicks, offsetTicks);
        if (cueTick > 0.0 && Math.abs(cueTick - cycleTicks) < 1.0E-4) {
            return cycleTicks;
        }
        return warped;
    }

    private static SoundTrackCandidate resolveSoundCandidateForIndex(ClientWorld world, InstanceState state, Identifier stageAnimationId, int index) {
        Identifier playerType;
        if (world == null || state == null || stageAnimationId == null) {
            return null;
        }
        if (index < 0 || index >= state.actorUuids.size()) {
            return null;
        }
        UUID actorId = state.actorUuids.get(index);
        Entity entity = findEntity(world, actorId);
        if (!(entity instanceof LivingEntity)) {
            return null;
        }
        LivingEntity anchor = (LivingEntity)entity;
        Identifier typeId = Registries.ENTITY_TYPE.getId(anchor.getType());
        String actorKey = index < state.actorKeys.size() ? state.actorKeys.get(index) : null;
        SoundTrackCandidate candidate = AfwClientAnimationRuntime.resolveSoundCandidateForParams(stageAnimationId, actorKey, typeId, true);
        if (candidate != null) {
            return candidate;
        }
        if (anchor instanceof PlayerEntity && (candidate = AfwClientAnimationRuntime.resolveSoundCandidateForParams(stageAnimationId, "player", playerType = new Identifier((String)"minecraft", (String)"player"), false)) != null) {
            return candidate;
        }
        return null;
    }

    private static SoundTrackCandidate resolveSoundCandidateForParams(Identifier stageAnimationId, String actorKey, Identifier typeId, boolean warnMissing) {
        if (stageAnimationId == null) {
            return null;
        }
        Identifier animationResource = AfwGeckoResourceResolver.resolveAnimationResource(stageAnimationId, actorKey, typeId, warnMissing);
        if (animationResource == null) {
            return null;
        }
        String animationKey = AfwGeckoResourceResolver.resolveAnimationKey(animationResource);
        AfwSoundEffects.SoundAnimation soundAnimation = AfwSoundEffects.getSoundAnimation(animationResource, animationKey);
        if (soundAnimation == null || soundAnimation.cues().isEmpty()) {
            return null;
        }
        return new SoundTrackCandidate(animationResource, animationKey, soundAnimation);
    }

    private static void fireLinearCues(LivingEntity anchor, InstanceState state, List<MergedSoundCue> cues, Identifier stageAnimationId, double lastTicks, double animTicks, double cycleTicks) {
        for (MergedSoundCue mergedCue : cues) {
            double cueTick = mergedCue.timeTicks();
            if (cueTick > animTicks || lastTicks >= 0.0 && cueTick <= lastTicks) continue;
            AfwClientAnimationRuntime.playSoundCue(anchor, state, mergedCue, stageAnimationId, cues, false, cycleTicks);
        }
    }

    private static void fireLoopingCues(LivingEntity anchor, InstanceState state, List<MergedSoundCue> cues, Identifier stageAnimationId, double lastTicks, double animTicks, double cycleTicks) {
        long lastCycle = lastTicks < 0.0 ? -1L : (long)Math.floor(lastTicks / cycleTicks);
        long currentCycle = (long)Math.floor(animTicks / cycleTicks);
        double lastCyclePos = lastCycle < 0L ? -1.0 : lastTicks - (double)lastCycle * cycleTicks;
        double currentCyclePos = animTicks - (double)currentCycle * cycleTicks;
        if (lastCycle < 0L) {
            lastCycle = currentCycle;
        }
        if (currentCycle == lastCycle) {
            AfwClientAnimationRuntime.fireLoopingWindow(anchor, state, cues, stageAnimationId, lastCyclePos, currentCyclePos, cycleTicks);
            return;
        }
        AfwClientAnimationRuntime.fireLoopingWindow(anchor, state, cues, stageAnimationId, lastCyclePos, cycleTicks, cycleTicks);
        long maxExtraCycles = Math.min(2L, currentCycle - lastCycle - 1L);
        for (long i = 0L; i < maxExtraCycles; ++i) {
            AfwClientAnimationRuntime.fireLoopingWindow(anchor, state, cues, stageAnimationId, -1.0, cycleTicks, cycleTicks);
        }
        AfwClientAnimationRuntime.fireLoopingWindow(anchor, state, cues, stageAnimationId, -1.0, currentCyclePos, cycleTicks);
    }

    private static void fireLoopingWindow(LivingEntity anchor, InstanceState state, List<MergedSoundCue> cues, Identifier stageAnimationId, double fromTick, double toTick, double cycleTicks) {
        for (MergedSoundCue mergedCue : cues) {
            double cueTick = mergedCue.timeTicks();
            if (cueTick > toTick || fromTick >= 0.0 && cueTick <= fromTick) continue;
            AfwClientAnimationRuntime.playSoundCue(anchor, state, mergedCue, stageAnimationId, cues, true, cycleTicks);
        }
    }

    private static void playSoundCue(LivingEntity anchor, InstanceState state, MergedSoundCue mergedCue, Identifier stageAnimationId, List<MergedSoundCue> cues, boolean loop, double cycleTicks) {
        Identifier fallback;
        if (mergedCue == null || mergedCue.cue() == null) {
            return;
        }
        AfwSoundEffects.SoundCue cue = mergedCue.cue();
        double nextDelayTicks = AfwClientAnimationRuntime.nextSameEffectCueDelayTicks(cues, mergedCue, loop, cycleTicks);
        double safeSpeed = Math.max(1.0E-4, AfwClientAnimationRuntime.sanitizeSpeed(state.speed));
        double nextDelayMs = nextDelayTicks > 0.0 ? nextDelayTicks / safeSpeed * 50.0 : 0.0;
        AfwSoundCueEvents.SoundContext context = new AfwSoundCueEvents.SoundContext(anchor, stageAnimationId, mergedCue.animationResource(), mergedCue.animationKey(), cue.effect(), mergedCue.timeTicks() / 20.0, cue.volume(), cue.pitch(), state.instanceId, state.stageIndex, loop, mergedCue.timeTicks(), cycleTicks, safeSpeed, nextDelayTicks, nextDelayMs);
        AfwSoundCueEvents.SoundOverride override = ((AfwSoundCueEvents.SoundResolver)AfwSoundCueEvents.RESOLVE.invoker()).resolve(context);
        if (override != null) {
            if (override.cancel()) {
                return;
            }
            Identifier overrideId = override.soundId();
            if (overrideId != null) {
                AfwClientAnimationRuntime.playSound(anchor, overrideId, override.volume(), override.pitch());
                return;
            }
        }
        if ((fallback = AfwClientAnimationRuntime.resolveEffectAsSoundId(cue.effect(), stageAnimationId, mergedCue.animationResource())) == null) {
            return;
        }
        AfwClientAnimationRuntime.playSound(anchor, fallback, cue.volume(), cue.pitch());
    }

    private static double nextSameEffectCueDelayTicks(List<MergedSoundCue> cues, MergedSoundCue currentCue, boolean loop, double cycleTicks) {
        if (cues == null || cues.isEmpty() || currentCue == null) {
            return 0.0;
        }
        if (currentCue.cue() == null) {
            return 0.0;
        }
        String effect = currentCue.cue().effect();
        if (effect == null || effect.isBlank()) {
            return 0.0;
        }
        double currentTick = currentCue.timeTicks();
        double epsilon = 1.0E-4;
        MergedSoundCue firstSame = null;
        for (MergedSoundCue candidate : cues) {
            AfwSoundEffects.SoundCue cue;
            if (candidate == null || candidate.cue() == null || !effect.equalsIgnoreCase((cue = candidate.cue()).effect())) continue;
            if (firstSame == null || candidate.timeTicks() < firstSame.timeTicks()) {
                firstSame = candidate;
            }
            if (!(candidate.timeTicks() > currentTick + 1.0E-4)) continue;
            return Math.max(0.0, candidate.timeTicks() - currentTick);
        }
        if (!loop || cycleTicks <= 0.0 || firstSame == null) {
            return 0.0;
        }
        double wrappedDelay = cycleTicks - currentTick + firstSame.timeTicks();
        if (wrappedDelay <= 1.0E-4) {
            wrappedDelay = cycleTicks;
        }
        return Math.max(0.0, wrappedDelay);
    }

    private static Identifier resolveEffectAsSoundId(String effect, Identifier stageAnimationId, Identifier animationResource) {
        if (effect == null || effect.isBlank()) {
            return null;
        }
        Identifier direct = Identifier.tryParse((String)effect);
        if (direct != null) {
            return direct;
        }
        String namespace = null;
        if (stageAnimationId != null) {
            namespace = stageAnimationId.getNamespace();
        } else if (animationResource != null) {
            namespace = animationResource.getNamespace();
        }
        if (namespace == null || namespace.isBlank()) {
            return null;
        }
        return Identifier.tryParse((String)(namespace + ":" + effect));
    }

    private static void playSound(LivingEntity anchor, Identifier soundId, float volume, float pitch) {
        ClientWorld clientWorld;
        ClientWorld world;
        if (anchor == null || soundId == null) {
            return;
        }
        World class_19372 = anchor.getEntityWorld();
        ClientWorld NarrationMessageBuilder = world = class_19372 instanceof ClientWorld ? (clientWorld = (ClientWorld)class_19372) : null;
        if (world == null) {
            return;
        }
        SoundEvent soundEvent = Registries.SOUND_EVENT.containsId(soundId) ? (SoundEvent)Registries.SOUND_EVENT.get(soundId) : SoundEvent.of((Identifier)soundId);
        SoundCategory category = anchor instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.NEUTRAL;
        world.playSound(anchor.getX(), anchor.getY(), anchor.getZ(), soundEvent,
                category, volume, pitch, false);
    }

    private static Entity findEntity(ClientWorld world, UUID uuid) {
        if (world == null || uuid == null) {
            return null;
        }
        for (Entity entity : world.getEntities()) {
            if (uuid.equals(entity.getUuid())) {
                return entity;
            }
        }
        return null;
    }

    public record LockedOrientation(float bodyYaw, float headYaw, float pitch) {
    }

    private record PendingStart(UUID instanceId, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, long startTick, double speed, UUID anchorUuid, boolean lockOrientation, LockedOrientation lockedOrientation, Vec3d cameraOrbitTarget) {
    }

    private static final class InstanceState {
        final UUID instanceId;
        final Identifier animationId;
        final List<UUID> actorUuids;
        final Set<UUID> actorUuidSet;
        final List<String> actorKeys;
        final UUID anchorUuid;
        LockedOrientation anchorOrientation;
        final Vec3d cameraOrbitTarget;
        final long startTick;
        final List<AnimationStageInfo> stages;
        int stageIndex = 0;
        long stageStartTick;
        int manualAdvanceRequests = 0;
        double speed;
        int soundStageIndex = -1;
        double lastSoundTick = -1.0;
        final Map<UUID, EnumMap<PropSlot, ItemStack>> propOverridesByActor = new HashMap<UUID, EnumMap<PropSlot, ItemStack>>();
        Long stopAtTick;

        InstanceState(UUID instanceId, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, long startTick, double speed, UUID anchorUuid, LockedOrientation anchorOrientation, Vec3d cameraOrbitTarget) {
            this.instanceId = instanceId;
            this.animationId = animationId;
            this.actorUuids = List.copyOf(actorUuids);
            this.actorUuidSet = Set.copyOf(this.actorUuids);
            this.actorKeys = actorKeys == null ? List.of() : List.copyOf(actorKeys);
            this.startTick = startTick;
            this.stages = AfwClientAnimationRuntime.normalizeStages(animationId, stages);
            this.stageStartTick = startTick;
            this.anchorUuid = anchorUuid;
            this.anchorOrientation = anchorOrientation;
            this.cameraOrbitTarget = cameraOrbitTarget;
            this.speed = AfwClientAnimationRuntime.sanitizeSpeed(speed);
        }

        long effectiveStopTick() {
            return this.stopAtTick != null ? this.stopAtTick : Long.MAX_VALUE;
        }

        boolean isActive(long nowTick) {
            if (nowTick < this.startTick) {
                return false;
            }
            return nowTick < this.effectiveStopTick();
        }

        boolean containsActor(UUID actorUuid) {
            return this.actorUuidSet.contains(actorUuid);
        }

        AnimationStageInfo currentStage() {
            if (this.stages.isEmpty()) {
                return new AnimationStageInfo(this.animationId, true, 0L, true, 1.0);
            }
            return this.stages.get(this.stageIndex);
        }

        Identifier currentAnimationId() {
            return this.currentStage().effectiveAnimationId();
        }

        boolean advanceStage(long nextStageStartTick) {
            if (this.stageIndex + 1 >= this.stages.size()) {
                return false;
            }
            ++this.stageIndex;
            this.stageStartTick = nextStageStartTick;
            this.speed = AfwClientAnimationRuntime.sanitizeSpeed(this.stages.get(this.stageIndex).speed());
            this.soundStageIndex = this.stageIndex;
            this.lastSoundTick = -1.0;
            return true;
        }

        boolean setStageIndex(int targetStageIndex, long nextStageStartTick) {
            if (targetStageIndex < 0 || targetStageIndex >= this.stages.size()) {
                return false;
            }
            this.stageIndex = targetStageIndex;
            this.stageStartTick = nextStageStartTick;
            this.speed = AfwClientAnimationRuntime.sanitizeSpeed(this.stages.get(this.stageIndex).speed());
            this.soundStageIndex = this.stageIndex;
            this.lastSoundTick = -1.0;
            return true;
        }
    }

    private record PendingStageAdvance(UUID instanceId, long advanceTick, int stageIndex) {
    }

    private record RenderHold(UUID instanceId, Identifier animationId, String actorKey, double speed, Map<String, AfwGeckoModelEvents.BoneItemProp> boneItems, LockedOrientation lockedOrientation, long expiresAtTick) {
    }

    public static enum PropSlot {
        LEFT("propleft", ModelTransformationMode.THIRD_PERSON_LEFT_HAND),
        RIGHT("propright", ModelTransformationMode.THIRD_PERSON_RIGHT_HAND);

        private final String boneName;
        private final ModelTransformationMode displayContext;

        private PropSlot(String boneName, ModelTransformationMode displayContext) {
            this.boneName = boneName;
            this.displayContext = displayContext;
        }

        public String boneName() {
            return this.boneName;
        }

        public ModelTransformationMode displayContext() {
            return this.displayContext;
        }
    }

    private record MergedSoundTrack(LivingEntity anchor, List<MergedSoundCue> cues, double cycleTicks) {
    }

    private record SoundTrackCandidate(Identifier animationResource, String animationKey, AfwSoundEffects.SoundAnimation soundAnimation) {
    }

    private record MergedSoundCue(Identifier animationResource, String animationKey, AfwSoundEffects.SoundCue cue, double timeTicks) {
    }
}

