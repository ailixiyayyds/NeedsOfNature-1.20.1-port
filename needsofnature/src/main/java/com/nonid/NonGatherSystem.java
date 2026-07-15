/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  com.afwid.api.AfwDamageBehavior
 *  com.afwid.data.AfwAnimationDefinitions
 *  com.afwid.data.AfwAnimationDefinitions$Definition
 *  com.afwid.network.AnimationStageInfo
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.world.World
 *  net.minecraft.util.math.Box
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.text.MutableText
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.registry.Registries
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import com.afwid.api.AfwAnimationApi;
import com.afwid.api.AfwDamageBehavior;
import com.afwid.data.AfwAnimationDefinitions;
import com.afwid.network.AnimationStageInfo;
import com.nonid.NeedsOfNature;
import com.nonid.NonAttackSystem;
import com.nonid.NonConfig;
import com.nonid.NonDebugChatCategory;
import com.nonid.NonDestroyedSkinSystem;
import com.nonid.NonGatherCancelReason;
import com.nonid.NonInjectorMatchPolicy;
import com.nonid.NonMobGatherEligibility;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

public final class NonGatherSystem {
    private static final double MOVE_SPEED = 1.15;
    private static final int REPATH_EVERY_TICKS = 5;
    private static final int TIMEOUT_TICKS = 3600;
    private static final int NO_LOS_TIMEOUT_TICKS = 100;
    private static final int TOO_FAR_TIMEOUT_TICKS = 200;
    private static final double TIMEOUT_MAX_DIST_SQ = 100.0;
    private static final double GATHER_EXTRA_DIST = 0.25;
    private static final double GATHER_TARGET_OVERSHOOT = 1.0;
    private static final double START_MAX_VERTICAL_DELTA = 2.5;
    private static final float LOOK_YAW_SPEED = 30.0f;
    private static final float LOOK_PITCH_SPEED = 30.0f;
    private static final Map<UUID, Session> SESSIONS_BY_INITIATOR = new ConcurrentHashMap<UUID, Session>();

    private NonGatherSystem() {
    }

    @Nullable
    public static GatherCandidate findGatherCandidate(ServerWorld world, List<Entity> actors, boolean isAttack, Entity attacker, @Nullable Entity gatherAnchor) {
        if (world == null || actors == null || actors.isEmpty() || attacker == null) {
            return null;
        }
        ArrayList<Entity> sortedActors = new ArrayList<Entity>(actors);
        sortedActors.sort(Comparator.comparingInt(Entity::getId));
        NeedsOfNature.AfwMatchedAnimation matched = NeedsOfNature.findAnimationForActors(world, sortedActors, Set.of(), gatherAnchor, true, (Entity)(isAttack ? attacker : null), Set.of(), NonInjectorMatchPolicy::allowsAutomaticNonMatch);
        return NonGatherSystem.toGatherCandidate(matched);
    }

    @Nullable
    public static GatherCandidate findUnprotectedGatherCandidate(ServerWorld world, List<Entity> actors, boolean isAttack, Entity attacker, @Nullable Entity gatherAnchor) {
        GatherCandidate candidate = NonGatherSystem.findGatherCandidate(world, actors, isAttack, attacker, gatherAnchor);
        if (candidate == null) {
            return null;
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)candidate.animationId());
        if (definition == null) {
            return null;
        }
        ArrayList<Entity> sortedActors = new ArrayList<Entity>(actors);
        sortedActors.sort(Comparator.comparingInt(Entity::getId));
        if (!NeedsOfNature.isProtectedByAccessory(world, definition, sortedActors)) {
            return candidate;
        }
        return NonGatherSystem.findUnprotectedFallbackCandidate(world, sortedActors, isAttack, attacker, gatherAnchor, true, candidate.animationId());
    }

    @Nullable
    static GatherCandidate findUnprotectedFallbackCandidate(ServerWorld world, List<Entity> actors, boolean isAttack, Entity attacker, @Nullable Entity gatherAnchor, boolean requireStartEligibility, Identifier blockedAnimationId) {
        if (blockedAnimationId == null) {
            return null;
        }
        ArrayList<Entity> sortedActors = new ArrayList<Entity>(actors);
        sortedActors.sort(Comparator.comparingInt(Entity::getId));
        HashSet<Identifier> excluded = new HashSet<Identifier>();
        excluded.add(blockedAnimationId);
        NeedsOfNature.AfwMatchedAnimation matched;
        GatherCandidate candidate;
        while ((candidate = NonGatherSystem.toGatherCandidate(matched = NeedsOfNature.findAnimationForActors(world, sortedActors, Set.of(), gatherAnchor, requireStartEligibility, (Entity)(isAttack ? attacker : null), excluded, NonInjectorMatchPolicy::allowsAutomaticNonMatch))) != null) {
            AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)candidate.animationId());
            if (definition == null) {
                return null;
            }
            if (!NeedsOfNature.isProtectedByAccessory(world, definition, sortedActors)) {
                return candidate;
            }
            excluded.add(candidate.animationId());
        }
        return null;
    }

    @Nullable
    private static GatherCandidate toGatherCandidate(@Nullable NeedsOfNature.AfwMatchedAnimation matched) {
        if (matched == null) {
            return null;
        }
        return new GatherCandidate(matched.animationId(), matched.actorKeys(), matched.stages());
    }

    public static boolean startMobPlayerGather(ServerWorld world, MobEntity mob, ServerPlayerEntity player, Identifier animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
        if (world == null || mob == null || player == null || animationId == null) {
            return false;
        }
        if (mob.getEntityWorld() != world || player.getEntityWorld() != world) {
            return false;
        }
        if (!NonMobGatherEligibility.canAutoGather(mob)) {
            return false;
        }
        if (NonGatherSystem.isAutomaticMobPlayerStartBlockedByMount(player)) {
            NonGatherSystem.reportAutomaticMobPlayerMountBlock(world, mob, player, animationId, "gather_start");
            return false;
        }
        Session session = new Session(SessionType.MOB_PLAYER, mob.getUuid(), player.getUuid(), animationId, List.copyOf(actorKeys), stages == null ? List.of() : List.copyOf(stages), true, null, (RegistryKey<World>)world.getRegistryKey(), world.getTime(), 0, 0, 0);
        SESSIONS_BY_INITIATOR.put(mob.getUuid(), session);
        NeedsOfNature.trackNoNGatherPair(world, List.of(mob, player));
        return true;
    }

    public static boolean isAutomaticMobPlayerStartBlockedByMount(ServerPlayerEntity player) {
        if (player == null || !player.hasVehicle()) {
            return false;
        }
        Entity vehicle = player.getVehicle();
        if (vehicle == null || vehicle.isRemoved()) {
            return false;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return false;
        }
        return vehicle instanceof LivingEntity ? config.blockAnimationsWhileRidingLivingEntities() : config.blockAnimationsWhileRidingVehicles();
    }

    public static void reportAutomaticMobPlayerMountBlock(ServerWorld world, MobEntity mob, ServerPlayerEntity player, @Nullable Identifier animationId, String phase) {
        if (world == null || mob == null || player == null) {
            return;
        }
        Entity vehicle = player.getVehicle();
        Identifier mobType = Registries.ENTITY_TYPE.getId(mob.getType());
        Identifier vehicleType = vehicle == null ? null : Registries.ENTITY_TYPE.getId(vehicle.getType());
        NeedsOfNature.LOGGER.info("NoN automatic mob-player start blocked by mounted-player protection: phase={} mobType={} player={} vehicle={} anim={}", new Object[]{phase, mobType, player.getName().getString(), vehicleType, animationId});
        Text vehicleName = vehicle == null ? Text.literal("a vehicle") : vehicle.getDisplayName();
        NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.WARNING, (Text)Text.translatable((String)"debug.needsofnature.mounted_animation_blocked", (Object[])new Object[]{vehicleName}));
    }

    public static void startMobMobGather(ServerWorld world, MobEntity initiator, MobEntity partner, Identifier animationId, List<String> actorKeys, List<AnimationStageInfo> stages, boolean attackMode) {
        if (world == null || initiator == null || partner == null || animationId == null) {
            return;
        }
        if (initiator.getEntityWorld() != world || partner.getEntityWorld() != world) {
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(initiator) || !NonMobGatherEligibility.canAutoGather(partner)) {
            return;
        }
        Session session = new Session(SessionType.MOB_MOB, initiator.getUuid(), partner.getUuid(), animationId, List.copyOf(actorKeys), stages == null ? List.of() : List.copyOf(stages), attackMode, null, (RegistryKey<World>)world.getRegistryKey(), world.getTime(), 0, 0, 0);
        SESSIONS_BY_INITIATOR.put(initiator.getUuid(), session);
        NeedsOfNature.trackNoNGatherPair(world, List.of(initiator, partner));
    }

    public static void startJoinGather(ServerWorld world, MobEntity joiningMob, ServerPlayerEntity requester, UUID activeInstanceId, Identifier animationId) {
        if (world == null || joiningMob == null || requester == null || activeInstanceId == null) {
            return;
        }
        if (joiningMob.getEntityWorld() != world || requester.getEntityWorld() != world) {
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(joiningMob)) {
            return;
        }
        Session session = new Session(SessionType.MOB_JOIN_ACTIVE_ANIMATION, joiningMob.getUuid(), requester.getUuid(), animationId, List.of(), List.of(), false, activeInstanceId, (RegistryKey<World>)world.getRegistryKey(), world.getTime(), 0, 0, 0);
        SESSIONS_BY_INITIATOR.put(joiningMob.getUuid(), session);
        NeedsOfNature.trackNoNGatherPair(world, List.of(joiningMob, requester));
    }

    public static void tickServerWorld(ServerWorld world) {
        if (world == null || SESSIONS_BY_INITIATOR.isEmpty()) {
            return;
        }
        for (Session session : List.copyOf(SESSIONS_BY_INITIATOR.values())) {
            MobEntity mob;
            if (session == null || !world.getRegistryKey().equals(session.worldKey())) continue;
            Entity initiator = world.getEntity(session.initiatorUuid());
            if (!(initiator instanceof MobEntity) || (mob = (MobEntity)initiator).getEntityWorld() != world) {
                NonGatherSystem.remove(session, world, false, null, "initiator_missing");
                continue;
            }
            if (session.type() == SessionType.MOB_PLAYER) {
                NonGatherSystem.tickMobPlayer(world, session, mob);
                continue;
            }
            if (session.type() == SessionType.MOB_MOB) {
                NonGatherSystem.tickMobMob(world, session, mob);
                continue;
            }
            NonGatherSystem.tickJoin(world, session, mob);
        }
    }

    public static boolean isActorGathering(UUID actorUuid) {
        if (actorUuid == null || SESSIONS_BY_INITIATOR.isEmpty()) {
            return false;
        }
        for (Session session : SESSIONS_BY_INITIATOR.values()) {
            if (session == null || !actorUuid.equals(session.initiatorUuid()) && !actorUuid.equals(session.targetUuid())) continue;
            return true;
        }
        return false;
    }

    private static void tickMobPlayer(ServerWorld world, Session session, MobEntity mob) {
        int tooFarTicks;
        ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(session.targetUuid());
        if (player == null || player.getEntityWorld() != world || !player.isAlive() || player.isRemoved() || mob.isRemoved()) {
            NonGatherSystem.remove(session, world, false, null, "invalid_actor");
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(mob)) {
            NonGatherSystem.remove(session, world, false, null, "mob_not_auto_gatherable");
            return;
        }
        if (!NeedsOfNature.canMobAttackPlayer(player) || NeedsOfNature.isMobAttackFailsafeActive(world, mob.getUuid())) {
            NonGatherSystem.remove(session, world, false, null, "player_not_attackable_or_failsafe");
            return;
        }
        if (NonGatherSystem.isAutomaticMobPlayerStartBlockedByMount(player)) {
            NonGatherSystem.reportAutomaticMobPlayerMountBlock(world, mob, player, session.animationId(), "close_range_start");
            NonGatherSystem.remove(session, world, false, null, "player_mounted_protected");
            return;
        }
        if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)mob.getUuid()) || AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)player.getUuid())) {
            NonGatherSystem.remove(session, world, false, null, "afw_actor_pending_or_active");
            return;
        }
        if (NonGatherSystem.timedOut(world, session)) {
            NonGatherSystem.remove(session, world, true, NonGatherCancelReason.TIMEOUT, "timeout");
            return;
        }
        int noLosTicks = mob.canSee((Entity)player) ? 0 : session.noLosTicks() + 1;
        int n = tooFarTicks = mob.squaredDistanceTo((Entity)player) > 100.0 ? session.tooFarTicks() + 1 : 0;
        if (NonGatherSystem.cancelledForDistance(session, world, noLosTicks, tooFarTicks)) {
            return;
        }
        mob.getLookControl().lookAt((Entity)player, 30.0f, 30.0f);
        if (NonAttackSystem.hasAnimationStartProtection(world, player)) {
            int repathCooldown = Math.max(0, session.repathCooldown() - 1);
            if (repathCooldown <= 0) {
                NonGatherSystem.startMovingIntoTarget(mob, (Entity)player);
                repathCooldown = 5;
            }
            NonGatherSystem.update(session, repathCooldown, noLosTicks, tooFarTicks);
            return;
        }
        if (NonGatherSystem.closeEnoughToStart((Entity)mob, (Entity)player)) {
            List<Entity> actors = NonGatherSystem.sortedActors((Entity)mob, (Entity)player);
            StartCandidate startCandidate = NonGatherSystem.resolveProtectedStartCandidate(world, session, actors, true, (Entity)mob, (Entity)player);
            if (startCandidate == null) {
                NeedsOfNature.clearNoNGatherPair(world, List.of(mob.getUuid(), player.getUuid()));
                SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
                return;
            }
            UUID instanceId = NeedsOfNature.startAnimationNowWithMetadata(world, startCandidate.animationId(), actors, startCandidate.actorKeys(), startCandidate.stages(), AfwDamageBehavior.STOP_ON_DAMAGE, false, (Entity)player, NeedsOfNature.buildModeMetadata(true));
            NeedsOfNature.clearNoNGatherPair(world, List.of(mob.getUuid(), player.getUuid()));
            SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
            if (instanceId == null) {
                NeedsOfNature.LOGGER.info("NoN gather start failed at close range: mobType={} player={} anim={}", new Object[]{Registries.ENTITY_TYPE.getId(mob.getType()), player.getName().getString(), startCandidate.animationId()});
            }
            return;
        }
        int repathCooldown = Math.max(0, session.repathCooldown() - 1);
        if (repathCooldown <= 0) {
            NonGatherSystem.startMovingIntoTarget(mob, (Entity)player);
            repathCooldown = 5;
        }
        NonGatherSystem.update(session, repathCooldown, noLosTicks, tooFarTicks);
    }

    private static void tickMobMob(ServerWorld world, Session session, MobEntity initiator) {
        int tooFarTicks;
        MobEntity partner;
        Entity rawPartner = world.getEntity(session.targetUuid());
        if (!(rawPartner instanceof MobEntity) || (partner = (MobEntity)rawPartner).getEntityWorld() != world || !partner.isAlive() || partner.isRemoved() || initiator.isRemoved()) {
            NonGatherSystem.remove(session, world, false, null, "invalid_actor");
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(initiator) || !NonMobGatherEligibility.canAutoGather(partner)) {
            NonGatherSystem.remove(session, world, false, null, "mob_not_auto_gatherable");
            return;
        }
        if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)initiator.getUuid()) || AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)partner.getUuid())) {
            NonGatherSystem.remove(session, world, false, null, "afw_actor_pending_or_active");
            return;
        }
        if (NonGatherSystem.timedOut(world, session)) {
            NonGatherSystem.remove(session, world, true, NonGatherCancelReason.TIMEOUT, "timeout");
            return;
        }
        boolean hasLos = initiator.canSee((Entity)partner) && partner.canSee((Entity)initiator);
        int noLosTicks = hasLos ? 0 : session.noLosTicks() + 1;
        int n = tooFarTicks = initiator.squaredDistanceTo((Entity)partner) > 100.0 ? session.tooFarTicks() + 1 : 0;
        if (NonGatherSystem.cancelledForDistance(session, world, noLosTicks, tooFarTicks)) {
            return;
        }
        initiator.getLookControl().lookAt((Entity)partner, 30.0f, 30.0f);
        partner.getLookControl().lookAt((Entity)initiator, 30.0f, 30.0f);
        if (NonGatherSystem.closeEnoughToStart((Entity)initiator, (Entity)partner)) {
            List<Entity> actors = NonGatherSystem.sortedActors((Entity)initiator, (Entity)partner);
            StartCandidate startCandidate = NonGatherSystem.resolveProtectedStartCandidate(world, session, actors, session.attackMode(), (Entity)initiator, (Entity)(session.attackMode() ? partner : null));
            if (startCandidate == null) {
                NeedsOfNature.clearNoNGatherPair(world, List.of(initiator.getUuid(), partner.getUuid()));
                SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
                return;
            }
            UUID instanceId = NeedsOfNature.startAnimationNowWithMetadata(world, startCandidate.animationId(), actors, startCandidate.actorKeys(), startCandidate.stages(), AfwDamageBehavior.STOP_ON_DAMAGE, false, (Entity)(session.attackMode() ? partner : null), NeedsOfNature.buildModeMetadata(session.attackMode()));
            NeedsOfNature.clearNoNGatherPair(world, List.of(initiator.getUuid(), partner.getUuid()));
            SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
            if (instanceId == null) {
                NeedsOfNature.LOGGER.info("NoN mob gather start failed at close range: first={} second={} anim={}", new Object[]{Registries.ENTITY_TYPE.getId(initiator.getType()), Registries.ENTITY_TYPE.getId(partner.getType()), startCandidate.animationId()});
            }
            return;
        }
        int repathCooldown = Math.max(0, session.repathCooldown() - 1);
        if (repathCooldown <= 0) {
            NonGatherSystem.startMovingIntoTarget(initiator, (Entity)partner);
            NonGatherSystem.startMovingIntoTarget(partner, (Entity)initiator);
            repathCooldown = 5;
        }
        NonGatherSystem.update(session, repathCooldown, noLosTicks, tooFarTicks);
    }

    private static void tickJoin(ServerWorld world, Session session, MobEntity joiningMob) {
        int tooFarTicks;
        ServerPlayerEntity requester = world.getServer().getPlayerManager().getPlayer(session.targetUuid());
        if (requester == null || requester.getEntityWorld() != world || !requester.isAlive() || requester.isRemoved() || joiningMob.isRemoved() || session.activeInstanceUuid() == null) {
            NonGatherSystem.remove(session, world, false, null, "invalid_join_actor");
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(joiningMob)) {
            NonGatherSystem.remove(session, world, false, null, "mob_not_auto_gatherable");
            return;
        }
        if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)joiningMob.getUuid())) {
            NonGatherSystem.remove(session, world, false, null, "afw_actor_pending_or_active");
            return;
        }
        AnimationStageInfo stage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)session.activeInstanceUuid());
        if (stage == null) {
            NonGatherSystem.remove(session, world, false, null, "join_stage_unavailable");
            return;
        }
        if (NonGatherSystem.timedOut(world, session)) {
            NonGatherSystem.remove(session, world, true, NonGatherCancelReason.TIMEOUT, "timeout");
            return;
        }
        int noLosTicks = joiningMob.canSee((Entity)requester) ? 0 : session.noLosTicks() + 1;
        int n = tooFarTicks = joiningMob.squaredDistanceTo((Entity)requester) > 100.0 ? session.tooFarTicks() + 1 : 0;
        if (NonGatherSystem.cancelledForDistance(session, world, noLosTicks, tooFarTicks)) {
            return;
        }
        joiningMob.getLookControl().lookAt((Entity)requester, 30.0f, 30.0f);
        if (NonGatherSystem.closeEnoughToJoin((Entity)joiningMob, (Entity)requester)) {
            Identifier joinAnimationId = NonGatherSystem.resolveProtectedJoinAnimationId(world, session, joiningMob, requester);
            if (joinAnimationId == null) {
                NeedsOfNature.clearNoNGatherPair(world, List.of(joiningMob.getUuid(), requester.getUuid()));
                SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
                return;
            }
            boolean voluntaryManualPeakJoin = NeedsOfNature.isVoluntaryManualPeakInstance(world, session.activeInstanceUuid());
            boolean accepted = NeedsOfNature.joinAnimationNow(world, session.activeInstanceUuid(), List.of(joiningMob), requester, joinAnimationId);
            NeedsOfNature.clearNoNGatherPair(world, List.of(joiningMob.getUuid(), requester.getUuid()));
            SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
            if (!accepted) {
                NeedsOfNature.LOGGER.info("NoN join gather failed at close range: mobType={} player={} instance={} anim={}", new Object[]{Registries.ENTITY_TYPE.getId(joiningMob.getType()), requester.getName().getString(), session.activeInstanceUuid(), session.animationId()});
            } else if (voluntaryManualPeakJoin) {
                NonDestroyedSkinSystem.applyDestroyedSkinVoluntaryJoinInitialDamage(world, requester);
            }
            return;
        }
        int repathCooldown = Math.max(0, session.repathCooldown() - 1);
        if (repathCooldown <= 0) {
            NonGatherSystem.startMovingIntoTarget(joiningMob, (Entity)requester);
            repathCooldown = 5;
        }
        NonGatherSystem.update(session, repathCooldown, noLosTicks, tooFarTicks);
    }

    private static void startMovingIntoTarget(MobEntity mob, Entity target) {
        if (mob == null || target == null) {
            return;
        }
        double dx = target.getX() - mob.getX();
        double dz = target.getZ() - mob.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        double targetX = target.getX();
        double targetZ = target.getZ();
        if (length > 1.0E-4) {
            targetX += dx / length * 1.0;
            targetZ += dz / length * 1.0;
        }
        mob.getNavigation().startMovingTo(targetX, target.getY(), targetZ, 1.15);
    }

    @Nullable
    private static Identifier resolveProtectedJoinAnimationId(ServerWorld world, Session session, MobEntity joiningMob, ServerPlayerEntity requester) {
        List<Entity> combinedActors = NonGatherSystem.resolveJoinActors(world, session.activeInstanceUuid(), joiningMob);
        if (combinedActors.isEmpty()) {
            return session.animationId();
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)session.animationId());
        if (definition == null) {
            return session.animationId();
        }
        if (!NonInjectorMatchPolicy.allowsAutomaticNonMatch(world, definition, combinedActors)) {
            boolean isAttack = NeedsOfNature.isInstanceAttack(session.activeInstanceUuid());
            GatherCandidate fallback = NonGatherSystem.findUnprotectedFallbackCandidate(world, combinedActors, isAttack, (Entity)joiningMob, (Entity)requester, true, session.animationId());
            return fallback == null ? null : fallback.animationId();
        }
        if (!NeedsOfNature.isProtectedByAccessory(world, definition, combinedActors)) {
            return session.animationId();
        }
        if (!NeedsOfNature.consumeProtectorForAnimation(world, definition, combinedActors)) {
            return session.animationId();
        }
        boolean isAttack = NeedsOfNature.isInstanceAttack(session.activeInstanceUuid());
        GatherCandidate fallback = NonGatherSystem.findUnprotectedFallbackCandidate(world, combinedActors, isAttack, (Entity)joiningMob, (Entity)requester, true, session.animationId());
        return fallback == null ? null : fallback.animationId();
    }

    private static List<Entity> resolveJoinActors(ServerWorld world, @Nullable UUID activeInstanceUuid, MobEntity joiningMob) {
        if (world == null || activeInstanceUuid == null || joiningMob == null) {
            return List.of();
        }
        ArrayList<Entity> actors = new ArrayList<Entity>();
        for (UUID actorUuid : NeedsOfNature.getInstanceActors(activeInstanceUuid)) {
            Entity actor = world.getEntity(actorUuid);
            if (actor == null) {
                actor = world.getServer().getPlayerManager().getPlayer(actorUuid);
            }
            if (actor == null || actor.getEntityWorld() != world || actor.isRemoved()) continue;
            actors.add(actor);
        }
        if (joiningMob.getEntityWorld() == world && !joiningMob.isRemoved()) {
            actors.add((Entity)joiningMob);
        }
        actors.sort(Comparator.comparingInt(Entity::getId));
        return actors;
    }

    private static boolean timedOut(ServerWorld world, Session session) {
        return world.getTime() - session.startTick() > 3600L;
    }

    @Nullable
    private static StartCandidate resolveProtectedStartCandidate(ServerWorld world, Session session, List<Entity> actors, boolean isAttack, Entity attacker, @Nullable Entity gatherAnchor) {
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)session.animationId());
        if (definition == null) {
            return new StartCandidate(session.animationId(), session.actorKeys(), session.stages());
        }
        if (!NonInjectorMatchPolicy.allowsAutomaticNonMatch(world, definition, actors)) {
            GatherCandidate fallback = NonGatherSystem.findUnprotectedFallbackCandidate(world, actors, isAttack, attacker, gatherAnchor, true, session.animationId());
            return fallback == null ? null : new StartCandidate(fallback.animationId(), fallback.actorKeys(), fallback.stages());
        }
        if (!NeedsOfNature.isProtectedByAccessory(world, definition, actors)) {
            return new StartCandidate(session.animationId(), session.actorKeys(), session.stages());
        }
        if (!NeedsOfNature.consumeProtectorForAnimation(world, definition, actors)) {
            return new StartCandidate(session.animationId(), session.actorKeys(), session.stages());
        }
        GatherCandidate fallback = NonGatherSystem.findUnprotectedFallbackCandidate(world, actors, isAttack, attacker, gatherAnchor, true, session.animationId());
        if (fallback == null) {
            return null;
        }
        return new StartCandidate(fallback.animationId(), fallback.actorKeys(), fallback.stages());
    }

    private static boolean cancelledForDistance(Session session, ServerWorld world, int noLosTicks, int tooFarTicks) {
        if (noLosTicks > 100) {
            NonGatherSystem.remove(session, world, true, NonGatherCancelReason.NO_LOS, "no_los");
            return true;
        }
        if (tooFarTicks > 200) {
            NonGatherSystem.remove(session, world, true, NonGatherCancelReason.TOO_FAR, "too_far");
            return true;
        }
        return false;
    }

    private static void update(Session session, int repathCooldown, int noLosTicks, int tooFarTicks) {
        SESSIONS_BY_INITIATOR.put(session.initiatorUuid(), new Session(session.type(), session.initiatorUuid(), session.targetUuid(), session.animationId(), session.actorKeys(), session.stages(), session.attackMode(), session.activeInstanceUuid(), session.worldKey(), session.startTick(), repathCooldown, noLosTicks, tooFarTicks));
    }

    private static void remove(Session session, ServerWorld world, boolean applyCooldown, @Nullable NonGatherCancelReason reason) {
        NonGatherSystem.remove(session, world, applyCooldown, reason, "unspecified");
    }

    private static void remove(Session session, ServerWorld world, boolean applyCooldown, @Nullable NonGatherCancelReason reason, String detail) {
        MobEntity mob;
        if (session == null || world == null) {
            return;
        }
        Entity initiator = world.getEntity(session.initiatorUuid());
        Entity target = world.getEntity(session.targetUuid());
        if (initiator instanceof MobEntity) {
            mob = (MobEntity)initiator;
            mob.getNavigation().stop();
        }
        if (target instanceof MobEntity) {
            mob = (MobEntity)target;
            mob.getNavigation().stop();
        }
        List<UUID> actorUuids = List.of(session.initiatorUuid(), session.targetUuid());
        if (applyCooldown && reason != null) {
            NeedsOfNature.applyNoNGatherCancellation(world, session.animationId(), actorUuids, reason);
        } else {
            NeedsOfNature.clearNoNGatherPair(world, actorUuids);
        }
        SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
    }

    private static boolean closeEnoughToStart(Entity a, Entity b) {
        if (a == null || b == null) {
            return false;
        }
        if (Math.abs(a.getY() - b.getY()) > 2.5) {
            return false;
        }
        return NonGatherSystem.horizontalBoxDistanceSq(a.getBoundingBox(), b.getBoundingBox()) <= 0.0625;
    }

    private static boolean closeEnoughToJoin(Entity a, Entity b) {
        return NonGatherSystem.closeEnoughToStart(a, b);
    }

    private static double horizontalBoxDistanceSq(Box a, Box b) {
        double dx = NonGatherSystem.axisGap(a.minX, a.maxX, b.minX, b.maxX);
        double dz = NonGatherSystem.axisGap(a.minZ, a.maxZ, b.minZ, b.maxZ);
        return dx * dx + dz * dz;
    }

    private static double axisGap(double aMin, double aMax, double bMin, double bMax) {
        if (aMax < bMin) {
            return bMin - aMax;
        }
        if (bMax < aMin) {
            return aMin - bMax;
        }
        return 0.0;
    }

    private static List<Entity> sortedActors(Entity first, Entity second) {
        ArrayList<Entity> actors = new ArrayList<Entity>(2);
        actors.add(first);
        actors.add(second);
        actors.sort(Comparator.comparingInt(Entity::getId));
        return actors;
    }

    public record GatherCandidate(Identifier animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
    }

    private record Session(SessionType type, UUID initiatorUuid, UUID targetUuid, Identifier animationId, List<String> actorKeys, List<AnimationStageInfo> stages, boolean attackMode, @Nullable UUID activeInstanceUuid, RegistryKey<World> worldKey, long startTick, int repathCooldown, int noLosTicks, int tooFarTicks) {
    }

    private static enum SessionType {
        MOB_PLAYER,
        MOB_MOB,
        MOB_JOIN_ACTIVE_ANIMATION;

    }

    private record StartCandidate(Identifier animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
    }
}

