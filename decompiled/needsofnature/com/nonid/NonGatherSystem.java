/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  com.afwid.api.AfwDamageBehavior
 *  com.afwid.data.AfwAnimationDefinitions
 *  com.afwid.data.AfwAnimationDefinitions$Definition
 *  com.afwid.network.AnimationStageInfo
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1937
 *  net.minecraft.class_238
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_5250
 *  net.minecraft.class_5321
 *  net.minecraft.class_7923
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
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_238;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5250;
import net.minecraft.class_5321;
import net.minecraft.class_7923;
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
    public static GatherCandidate findGatherCandidate(class_3218 world, List<class_1297> actors, boolean isAttack, class_1297 attacker, @Nullable class_1297 gatherAnchor) {
        if (world == null || actors == null || actors.isEmpty() || attacker == null) {
            return null;
        }
        ArrayList<class_1297> sortedActors = new ArrayList<class_1297>(actors);
        sortedActors.sort(Comparator.comparingInt(class_1297::method_5628));
        NeedsOfNature.AfwMatchedAnimation matched = NeedsOfNature.findAnimationForActors(world, sortedActors, Set.of(), gatherAnchor, true, (class_1297)(isAttack ? attacker : null), Set.of(), NonInjectorMatchPolicy::allowsAutomaticNonMatch);
        return NonGatherSystem.toGatherCandidate(matched);
    }

    @Nullable
    public static GatherCandidate findUnprotectedGatherCandidate(class_3218 world, List<class_1297> actors, boolean isAttack, class_1297 attacker, @Nullable class_1297 gatherAnchor) {
        GatherCandidate candidate = NonGatherSystem.findGatherCandidate(world, actors, isAttack, attacker, gatherAnchor);
        if (candidate == null) {
            return null;
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((class_2960)candidate.animationId());
        if (definition == null) {
            return null;
        }
        ArrayList<class_1297> sortedActors = new ArrayList<class_1297>(actors);
        sortedActors.sort(Comparator.comparingInt(class_1297::method_5628));
        if (!NeedsOfNature.isProtectedByAccessory(world, definition, sortedActors)) {
            return candidate;
        }
        return NonGatherSystem.findUnprotectedFallbackCandidate(world, sortedActors, isAttack, attacker, gatherAnchor, true, candidate.animationId());
    }

    @Nullable
    static GatherCandidate findUnprotectedFallbackCandidate(class_3218 world, List<class_1297> actors, boolean isAttack, class_1297 attacker, @Nullable class_1297 gatherAnchor, boolean requireStartEligibility, class_2960 blockedAnimationId) {
        if (blockedAnimationId == null) {
            return null;
        }
        ArrayList<class_1297> sortedActors = new ArrayList<class_1297>(actors);
        sortedActors.sort(Comparator.comparingInt(class_1297::method_5628));
        HashSet<class_2960> excluded = new HashSet<class_2960>();
        excluded.add(blockedAnimationId);
        NeedsOfNature.AfwMatchedAnimation matched;
        GatherCandidate candidate;
        while ((candidate = NonGatherSystem.toGatherCandidate(matched = NeedsOfNature.findAnimationForActors(world, sortedActors, Set.of(), gatherAnchor, requireStartEligibility, (class_1297)(isAttack ? attacker : null), excluded, NonInjectorMatchPolicy::allowsAutomaticNonMatch))) != null) {
            AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((class_2960)candidate.animationId());
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

    public static boolean startMobPlayerGather(class_3218 world, class_1308 mob, class_3222 player, class_2960 animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
        if (world == null || mob == null || player == null || animationId == null) {
            return false;
        }
        if (mob.method_73183() != world || player.method_51469() != world) {
            return false;
        }
        if (!NonMobGatherEligibility.canAutoGather(mob)) {
            return false;
        }
        if (NonGatherSystem.isAutomaticMobPlayerStartBlockedByMount(player)) {
            NonGatherSystem.reportAutomaticMobPlayerMountBlock(world, mob, player, animationId, "gather_start");
            return false;
        }
        Session session = new Session(SessionType.MOB_PLAYER, mob.method_5667(), player.method_5667(), animationId, List.copyOf(actorKeys), stages == null ? List.of() : List.copyOf(stages), true, null, (class_5321<class_1937>)world.method_27983(), world.method_75260(), 0, 0, 0);
        SESSIONS_BY_INITIATOR.put(mob.method_5667(), session);
        NeedsOfNature.trackNoNGatherPair(world, List.of(mob, player));
        return true;
    }

    public static boolean isAutomaticMobPlayerStartBlockedByMount(class_3222 player) {
        if (player == null || !player.method_5765()) {
            return false;
        }
        class_1297 vehicle = player.method_5854();
        if (vehicle == null || vehicle.method_31481()) {
            return false;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return false;
        }
        return vehicle instanceof class_1309 ? config.blockAnimationsWhileRidingLivingEntities() : config.blockAnimationsWhileRidingVehicles();
    }

    public static void reportAutomaticMobPlayerMountBlock(class_3218 world, class_1308 mob, class_3222 player, @Nullable class_2960 animationId, String phase) {
        if (world == null || mob == null || player == null) {
            return;
        }
        class_1297 vehicle = player.method_5854();
        class_2960 mobType = class_7923.field_41177.method_10221((Object)mob.method_5864());
        class_2960 vehicleType = vehicle == null ? null : class_7923.field_41177.method_10221((Object)vehicle.method_5864());
        NeedsOfNature.LOGGER.info("NoN automatic mob-player start blocked by mounted-player protection: phase={} mobType={} player={} vehicle={} anim={}", new Object[]{phase, mobType, player.method_5477().getString(), vehicleType, animationId});
        class_5250 vehicleName = vehicle == null ? class_2561.method_43470((String)"a vehicle") : vehicle.method_5476();
        NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.WARNING, (class_2561)class_2561.method_43469((String)"debug.needsofnature.mounted_animation_blocked", (Object[])new Object[]{vehicleName}));
    }

    public static void startMobMobGather(class_3218 world, class_1308 initiator, class_1308 partner, class_2960 animationId, List<String> actorKeys, List<AnimationStageInfo> stages, boolean attackMode) {
        if (world == null || initiator == null || partner == null || animationId == null) {
            return;
        }
        if (initiator.method_73183() != world || partner.method_73183() != world) {
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(initiator) || !NonMobGatherEligibility.canAutoGather(partner)) {
            return;
        }
        Session session = new Session(SessionType.MOB_MOB, initiator.method_5667(), partner.method_5667(), animationId, List.copyOf(actorKeys), stages == null ? List.of() : List.copyOf(stages), attackMode, null, (class_5321<class_1937>)world.method_27983(), world.method_75260(), 0, 0, 0);
        SESSIONS_BY_INITIATOR.put(initiator.method_5667(), session);
        NeedsOfNature.trackNoNGatherPair(world, List.of(initiator, partner));
    }

    public static void startJoinGather(class_3218 world, class_1308 joiningMob, class_3222 requester, UUID activeInstanceId, class_2960 animationId) {
        if (world == null || joiningMob == null || requester == null || activeInstanceId == null) {
            return;
        }
        if (joiningMob.method_73183() != world || requester.method_51469() != world) {
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(joiningMob)) {
            return;
        }
        Session session = new Session(SessionType.MOB_JOIN_ACTIVE_ANIMATION, joiningMob.method_5667(), requester.method_5667(), animationId, List.of(), List.of(), false, activeInstanceId, (class_5321<class_1937>)world.method_27983(), world.method_75260(), 0, 0, 0);
        SESSIONS_BY_INITIATOR.put(joiningMob.method_5667(), session);
        NeedsOfNature.trackNoNGatherPair(world, List.of(joiningMob, requester));
    }

    public static void tickServerWorld(class_3218 world) {
        if (world == null || SESSIONS_BY_INITIATOR.isEmpty()) {
            return;
        }
        for (Session session : List.copyOf(SESSIONS_BY_INITIATOR.values())) {
            class_1308 mob;
            if (session == null || !world.method_27983().equals(session.worldKey())) continue;
            class_1297 initiator = world.method_66347(session.initiatorUuid());
            if (!(initiator instanceof class_1308) || (mob = (class_1308)initiator).method_73183() != world) {
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

    private static void tickMobPlayer(class_3218 world, Session session, class_1308 mob) {
        int tooFarTicks;
        class_3222 player = world.method_8503().method_3760().method_14602(session.targetUuid());
        if (player == null || player.method_51469() != world || !player.method_5805() || player.method_31481() || mob.method_31481()) {
            NonGatherSystem.remove(session, world, false, null, "invalid_actor");
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(mob)) {
            NonGatherSystem.remove(session, world, false, null, "mob_not_auto_gatherable");
            return;
        }
        if (!NeedsOfNature.canMobAttackPlayer(player) || NeedsOfNature.isMobAttackFailsafeActive(world, mob.method_5667())) {
            NonGatherSystem.remove(session, world, false, null, "player_not_attackable_or_failsafe");
            return;
        }
        if (NonGatherSystem.isAutomaticMobPlayerStartBlockedByMount(player)) {
            NonGatherSystem.reportAutomaticMobPlayerMountBlock(world, mob, player, session.animationId(), "close_range_start");
            NonGatherSystem.remove(session, world, false, null, "player_mounted_protected");
            return;
        }
        if (AfwAnimationApi.isActorPendingOrActive((class_3218)world, (UUID)mob.method_5667()) || AfwAnimationApi.isActorPendingOrActive((class_3218)world, (UUID)player.method_5667())) {
            NonGatherSystem.remove(session, world, false, null, "afw_actor_pending_or_active");
            return;
        }
        if (NonGatherSystem.timedOut(world, session)) {
            NonGatherSystem.remove(session, world, true, NonGatherCancelReason.TIMEOUT, "timeout");
            return;
        }
        int noLosTicks = mob.method_6057((class_1297)player) ? 0 : session.noLosTicks() + 1;
        int n = tooFarTicks = mob.method_5858((class_1297)player) > 100.0 ? session.tooFarTicks() + 1 : 0;
        if (NonGatherSystem.cancelledForDistance(session, world, noLosTicks, tooFarTicks)) {
            return;
        }
        mob.method_5988().method_6226((class_1297)player, 30.0f, 30.0f);
        if (NonAttackSystem.hasAnimationStartProtection(world, player)) {
            int repathCooldown = Math.max(0, session.repathCooldown() - 1);
            if (repathCooldown <= 0) {
                NonGatherSystem.startMovingIntoTarget(mob, (class_1297)player);
                repathCooldown = 5;
            }
            NonGatherSystem.update(session, repathCooldown, noLosTicks, tooFarTicks);
            return;
        }
        if (NonGatherSystem.closeEnoughToStart((class_1297)mob, (class_1297)player)) {
            List<class_1297> actors = NonGatherSystem.sortedActors((class_1297)mob, (class_1297)player);
            StartCandidate startCandidate = NonGatherSystem.resolveProtectedStartCandidate(world, session, actors, true, (class_1297)mob, (class_1297)player);
            if (startCandidate == null) {
                NeedsOfNature.clearNoNGatherPair(world, List.of(mob.method_5667(), player.method_5667()));
                SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
                return;
            }
            UUID instanceId = NeedsOfNature.startAnimationNowWithMetadata(world, startCandidate.animationId(), actors, startCandidate.actorKeys(), startCandidate.stages(), AfwDamageBehavior.STOP_ON_DAMAGE, false, (class_1297)player, NeedsOfNature.buildModeMetadata(true));
            NeedsOfNature.clearNoNGatherPair(world, List.of(mob.method_5667(), player.method_5667()));
            SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
            if (instanceId == null) {
                NeedsOfNature.LOGGER.info("NoN gather start failed at close range: mobType={} player={} anim={}", new Object[]{class_7923.field_41177.method_10221((Object)mob.method_5864()), player.method_5477().getString(), startCandidate.animationId()});
            }
            return;
        }
        int repathCooldown = Math.max(0, session.repathCooldown() - 1);
        if (repathCooldown <= 0) {
            NonGatherSystem.startMovingIntoTarget(mob, (class_1297)player);
            repathCooldown = 5;
        }
        NonGatherSystem.update(session, repathCooldown, noLosTicks, tooFarTicks);
    }

    private static void tickMobMob(class_3218 world, Session session, class_1308 initiator) {
        int tooFarTicks;
        class_1308 partner;
        class_1297 rawPartner = world.method_66347(session.targetUuid());
        if (!(rawPartner instanceof class_1308) || (partner = (class_1308)rawPartner).method_73183() != world || !partner.method_5805() || partner.method_31481() || initiator.method_31481()) {
            NonGatherSystem.remove(session, world, false, null, "invalid_actor");
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(initiator) || !NonMobGatherEligibility.canAutoGather(partner)) {
            NonGatherSystem.remove(session, world, false, null, "mob_not_auto_gatherable");
            return;
        }
        if (AfwAnimationApi.isActorPendingOrActive((class_3218)world, (UUID)initiator.method_5667()) || AfwAnimationApi.isActorPendingOrActive((class_3218)world, (UUID)partner.method_5667())) {
            NonGatherSystem.remove(session, world, false, null, "afw_actor_pending_or_active");
            return;
        }
        if (NonGatherSystem.timedOut(world, session)) {
            NonGatherSystem.remove(session, world, true, NonGatherCancelReason.TIMEOUT, "timeout");
            return;
        }
        boolean hasLos = initiator.method_6057((class_1297)partner) && partner.method_6057((class_1297)initiator);
        int noLosTicks = hasLos ? 0 : session.noLosTicks() + 1;
        int n = tooFarTicks = initiator.method_5858((class_1297)partner) > 100.0 ? session.tooFarTicks() + 1 : 0;
        if (NonGatherSystem.cancelledForDistance(session, world, noLosTicks, tooFarTicks)) {
            return;
        }
        initiator.method_5988().method_6226((class_1297)partner, 30.0f, 30.0f);
        partner.method_5988().method_6226((class_1297)initiator, 30.0f, 30.0f);
        if (NonGatherSystem.closeEnoughToStart((class_1297)initiator, (class_1297)partner)) {
            List<class_1297> actors = NonGatherSystem.sortedActors((class_1297)initiator, (class_1297)partner);
            StartCandidate startCandidate = NonGatherSystem.resolveProtectedStartCandidate(world, session, actors, session.attackMode(), (class_1297)initiator, (class_1297)(session.attackMode() ? partner : null));
            if (startCandidate == null) {
                NeedsOfNature.clearNoNGatherPair(world, List.of(initiator.method_5667(), partner.method_5667()));
                SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
                return;
            }
            UUID instanceId = NeedsOfNature.startAnimationNowWithMetadata(world, startCandidate.animationId(), actors, startCandidate.actorKeys(), startCandidate.stages(), AfwDamageBehavior.STOP_ON_DAMAGE, false, (class_1297)(session.attackMode() ? partner : null), NeedsOfNature.buildModeMetadata(session.attackMode()));
            NeedsOfNature.clearNoNGatherPair(world, List.of(initiator.method_5667(), partner.method_5667()));
            SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
            if (instanceId == null) {
                NeedsOfNature.LOGGER.info("NoN mob gather start failed at close range: first={} second={} anim={}", new Object[]{class_7923.field_41177.method_10221((Object)initiator.method_5864()), class_7923.field_41177.method_10221((Object)partner.method_5864()), startCandidate.animationId()});
            }
            return;
        }
        int repathCooldown = Math.max(0, session.repathCooldown() - 1);
        if (repathCooldown <= 0) {
            NonGatherSystem.startMovingIntoTarget(initiator, (class_1297)partner);
            NonGatherSystem.startMovingIntoTarget(partner, (class_1297)initiator);
            repathCooldown = 5;
        }
        NonGatherSystem.update(session, repathCooldown, noLosTicks, tooFarTicks);
    }

    private static void tickJoin(class_3218 world, Session session, class_1308 joiningMob) {
        int tooFarTicks;
        class_3222 requester = world.method_8503().method_3760().method_14602(session.targetUuid());
        if (requester == null || requester.method_51469() != world || !requester.method_5805() || requester.method_31481() || joiningMob.method_31481() || session.activeInstanceUuid() == null) {
            NonGatherSystem.remove(session, world, false, null, "invalid_join_actor");
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather(joiningMob)) {
            NonGatherSystem.remove(session, world, false, null, "mob_not_auto_gatherable");
            return;
        }
        if (AfwAnimationApi.isActorPendingOrActive((class_3218)world, (UUID)joiningMob.method_5667())) {
            NonGatherSystem.remove(session, world, false, null, "afw_actor_pending_or_active");
            return;
        }
        AnimationStageInfo stage = AfwAnimationApi.getCurrentStage((class_3218)world, (UUID)session.activeInstanceUuid());
        if (stage == null) {
            NonGatherSystem.remove(session, world, false, null, "join_stage_unavailable");
            return;
        }
        if (NonGatherSystem.timedOut(world, session)) {
            NonGatherSystem.remove(session, world, true, NonGatherCancelReason.TIMEOUT, "timeout");
            return;
        }
        int noLosTicks = joiningMob.method_6057((class_1297)requester) ? 0 : session.noLosTicks() + 1;
        int n = tooFarTicks = joiningMob.method_5858((class_1297)requester) > 100.0 ? session.tooFarTicks() + 1 : 0;
        if (NonGatherSystem.cancelledForDistance(session, world, noLosTicks, tooFarTicks)) {
            return;
        }
        joiningMob.method_5988().method_6226((class_1297)requester, 30.0f, 30.0f);
        if (NonGatherSystem.closeEnoughToJoin((class_1297)joiningMob, (class_1297)requester)) {
            class_2960 joinAnimationId = NonGatherSystem.resolveProtectedJoinAnimationId(world, session, joiningMob, requester);
            if (joinAnimationId == null) {
                NeedsOfNature.clearNoNGatherPair(world, List.of(joiningMob.method_5667(), requester.method_5667()));
                SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
                return;
            }
            boolean voluntaryManualPeakJoin = NeedsOfNature.isVoluntaryManualPeakInstance(world, session.activeInstanceUuid());
            boolean accepted = NeedsOfNature.joinAnimationNow(world, session.activeInstanceUuid(), List.of(joiningMob), requester, joinAnimationId);
            NeedsOfNature.clearNoNGatherPair(world, List.of(joiningMob.method_5667(), requester.method_5667()));
            SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
            if (!accepted) {
                NeedsOfNature.LOGGER.info("NoN join gather failed at close range: mobType={} player={} instance={} anim={}", new Object[]{class_7923.field_41177.method_10221((Object)joiningMob.method_5864()), requester.method_5477().getString(), session.activeInstanceUuid(), session.animationId()});
            } else if (voluntaryManualPeakJoin) {
                NonDestroyedSkinSystem.applyDestroyedSkinVoluntaryJoinInitialDamage(world, requester);
            }
            return;
        }
        int repathCooldown = Math.max(0, session.repathCooldown() - 1);
        if (repathCooldown <= 0) {
            NonGatherSystem.startMovingIntoTarget(joiningMob, (class_1297)requester);
            repathCooldown = 5;
        }
        NonGatherSystem.update(session, repathCooldown, noLosTicks, tooFarTicks);
    }

    private static void startMovingIntoTarget(class_1308 mob, class_1297 target) {
        if (mob == null || target == null) {
            return;
        }
        double dx = target.method_23317() - mob.method_23317();
        double dz = target.method_23321() - mob.method_23321();
        double length = Math.sqrt(dx * dx + dz * dz);
        double targetX = target.method_23317();
        double targetZ = target.method_23321();
        if (length > 1.0E-4) {
            targetX += dx / length * 1.0;
            targetZ += dz / length * 1.0;
        }
        mob.method_5942().method_6337(targetX, target.method_23318(), targetZ, 1.15);
    }

    @Nullable
    private static class_2960 resolveProtectedJoinAnimationId(class_3218 world, Session session, class_1308 joiningMob, class_3222 requester) {
        List<class_1297> combinedActors = NonGatherSystem.resolveJoinActors(world, session.activeInstanceUuid(), joiningMob);
        if (combinedActors.isEmpty()) {
            return session.animationId();
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((class_2960)session.animationId());
        if (definition == null) {
            return session.animationId();
        }
        if (!NonInjectorMatchPolicy.allowsAutomaticNonMatch(world, definition, combinedActors)) {
            boolean isAttack = NeedsOfNature.isInstanceAttack(session.activeInstanceUuid());
            GatherCandidate fallback = NonGatherSystem.findUnprotectedFallbackCandidate(world, combinedActors, isAttack, (class_1297)joiningMob, (class_1297)requester, true, session.animationId());
            return fallback == null ? null : fallback.animationId();
        }
        if (!NeedsOfNature.isProtectedByAccessory(world, definition, combinedActors)) {
            return session.animationId();
        }
        if (!NeedsOfNature.consumeProtectorForAnimation(world, definition, combinedActors)) {
            return session.animationId();
        }
        boolean isAttack = NeedsOfNature.isInstanceAttack(session.activeInstanceUuid());
        GatherCandidate fallback = NonGatherSystem.findUnprotectedFallbackCandidate(world, combinedActors, isAttack, (class_1297)joiningMob, (class_1297)requester, true, session.animationId());
        return fallback == null ? null : fallback.animationId();
    }

    private static List<class_1297> resolveJoinActors(class_3218 world, @Nullable UUID activeInstanceUuid, class_1308 joiningMob) {
        if (world == null || activeInstanceUuid == null || joiningMob == null) {
            return List.of();
        }
        ArrayList<class_1297> actors = new ArrayList<class_1297>();
        for (UUID actorUuid : NeedsOfNature.getInstanceActors(activeInstanceUuid)) {
            class_1297 actor = world.method_66347(actorUuid);
            if (actor == null) {
                actor = world.method_8503().method_3760().method_14602(actorUuid);
            }
            if (actor == null || actor.method_73183() != world || actor.method_31481()) continue;
            actors.add(actor);
        }
        if (joiningMob.method_73183() == world && !joiningMob.method_31481()) {
            actors.add((class_1297)joiningMob);
        }
        actors.sort(Comparator.comparingInt(class_1297::method_5628));
        return actors;
    }

    private static boolean timedOut(class_3218 world, Session session) {
        return world.method_75260() - session.startTick() > 3600L;
    }

    @Nullable
    private static StartCandidate resolveProtectedStartCandidate(class_3218 world, Session session, List<class_1297> actors, boolean isAttack, class_1297 attacker, @Nullable class_1297 gatherAnchor) {
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((class_2960)session.animationId());
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

    private static boolean cancelledForDistance(Session session, class_3218 world, int noLosTicks, int tooFarTicks) {
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

    private static void remove(Session session, class_3218 world, boolean applyCooldown, @Nullable NonGatherCancelReason reason) {
        NonGatherSystem.remove(session, world, applyCooldown, reason, "unspecified");
    }

    private static void remove(Session session, class_3218 world, boolean applyCooldown, @Nullable NonGatherCancelReason reason, String detail) {
        class_1308 mob;
        if (session == null || world == null) {
            return;
        }
        class_1297 initiator = world.method_66347(session.initiatorUuid());
        class_1297 target = world.method_66347(session.targetUuid());
        if (initiator instanceof class_1308) {
            mob = (class_1308)initiator;
            mob.method_5942().method_6340();
        }
        if (target instanceof class_1308) {
            mob = (class_1308)target;
            mob.method_5942().method_6340();
        }
        List<UUID> actorUuids = List.of(session.initiatorUuid(), session.targetUuid());
        if (applyCooldown && reason != null) {
            NeedsOfNature.applyNoNGatherCancellation(world, session.animationId(), actorUuids, reason);
        } else {
            NeedsOfNature.clearNoNGatherPair(world, actorUuids);
        }
        SESSIONS_BY_INITIATOR.remove(session.initiatorUuid(), session);
    }

    private static boolean closeEnoughToStart(class_1297 a, class_1297 b) {
        if (a == null || b == null) {
            return false;
        }
        if (Math.abs(a.method_23318() - b.method_23318()) > 2.5) {
            return false;
        }
        return NonGatherSystem.horizontalBoxDistanceSq(a.method_5829(), b.method_5829()) <= 0.0625;
    }

    private static boolean closeEnoughToJoin(class_1297 a, class_1297 b) {
        return NonGatherSystem.closeEnoughToStart(a, b);
    }

    private static double horizontalBoxDistanceSq(class_238 a, class_238 b) {
        double dx = NonGatherSystem.axisGap(a.field_1323, a.field_1320, b.field_1323, b.field_1320);
        double dz = NonGatherSystem.axisGap(a.field_1321, a.field_1324, b.field_1321, b.field_1324);
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

    private static List<class_1297> sortedActors(class_1297 first, class_1297 second) {
        ArrayList<class_1297> actors = new ArrayList<class_1297>(2);
        actors.add(first);
        actors.add(second);
        actors.sort(Comparator.comparingInt(class_1297::method_5628));
        return actors;
    }

    public record GatherCandidate(class_2960 animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
    }

    private record Session(SessionType type, UUID initiatorUuid, UUID targetUuid, class_2960 animationId, List<String> actorKeys, List<AnimationStageInfo> stages, boolean attackMode, @Nullable UUID activeInstanceUuid, class_5321<class_1937> worldKey, long startTick, int repathCooldown, int noLosTicks, int tooFarTicks) {
    }

    private static enum SessionType {
        MOB_PLAYER,
        MOB_MOB,
        MOB_JOIN_ACTIVE_ANIMATION;

    }

    private record StartCandidate(class_2960 animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
    }
}

