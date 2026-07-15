/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  net.minecraft.class_10583
 *  net.minecraft.class_1282
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1324
 *  net.minecraft.class_1569
 *  net.minecraft.class_1937
 *  net.minecraft.class_238
 *  net.minecraft.class_2561
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_5134
 *  net.minecraft.class_5250
 *  net.minecraft.class_5321
 *  net.minecraft.class_5354
 *  net.minecraft.class_7923
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import com.afwid.api.AfwAnimationApi;
import com.nonid.EnergyHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.NonDebugChatCategory;
import com.nonid.NonGatherSystem;
import com.nonid.NonMobEnergySystem;
import com.nonid.NonMobGatherEligibility;
import com.nonid.NonTrinketsIntegration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_10583;
import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1324;
import net.minecraft.class_1569;
import net.minecraft.class_1937;
import net.minecraft.class_238;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5134;
import net.minecraft.class_5250;
import net.minecraft.class_5321;
import net.minecraft.class_5354;
import net.minecraft.class_7923;
import org.jetbrains.annotations.Nullable;

final class NonAttackSystem {
    private static final float ATTACK_ESCAPE_DAMAGE = 4.0f;
    private static final float ATTACK_ESCAPE_MIN_DAMAGE = 1.0f;
    private static final float ATTACK_ESCAPE_MIN_HEALTH = 1.0f;
    private static final int ATTACK_OUTCOME_FAILSAFE_WINDOW_TICKS = 12000;
    private static final int ATTACK_OUTCOME_FAILSAFE_COOLDOWN_TICKS = 12000;
    private static final int ATTACK_BLOCK_DEBUG_COOLDOWN_TICKS = 400;
    private static final double ATTACK_BLOCK_DEBUG_RADIUS = 24.0;
    private static final double POST_ESCAPE_GATHER_RADIUS = 10.0;
    private static final Map<UUID, AttackOutcomeFailsafeState> ATTACK_OUTCOME_FAILSAFE = new ConcurrentHashMap<UUID, AttackOutcomeFailsafeState>();
    private static final Map<UUID, AttackBlockDebugState> ATTACK_BLOCK_DEBUG = new ConcurrentHashMap<UUID, AttackBlockDebugState>();

    private NonAttackSystem() {
    }

    static boolean canMobAttackPlayer(class_3222 player) {
        if (player == null) {
            return false;
        }
        if (player.method_7325()) {
            return false;
        }
        if (!player.method_68878()) {
            return true;
        }
        NonConfig config = NeedsOfNature.getConfig();
        return config != null && config.canAttackCreativePlayers();
    }

    static void setAnimationStartProtection(class_3222 player, int ticks) {
        if (player == null) {
            return;
        }
        AfwAnimationApi.setAnimationStartProtection((class_3222)player, (int)ticks);
    }

    static boolean hasAnimationStartProtection(class_3218 world, class_3222 player) {
        if (world == null || player == null) {
            return false;
        }
        return AfwAnimationApi.hasAnimationStartProtection((class_3218)world, (class_3222)player);
    }

    static boolean isMobAttackFailsafeActive(class_3218 world, UUID mobUuid) {
        if (world == null || mobUuid == null) {
            return false;
        }
        AttackOutcomeFailsafeState state = ATTACK_OUTCOME_FAILSAFE.get(mobUuid);
        if (state == null) {
            return false;
        }
        if (!state.worldKey.equals((Object)world.method_27983())) {
            return false;
        }
        long nowTick = world.method_75260();
        NonAttackSystem.pruneAttackOutcomeWindow(state, nowTick);
        if (state.cooldownUntilTick <= nowTick) {
            if (state.recentAttackOutcomeTicks.isEmpty()) {
                ATTACK_OUTCOME_FAILSAFE.remove(mobUuid, state);
            }
            return false;
        }
        return true;
    }

    static int clearAttackOutcomeFailsafeEntries() {
        int count = ATTACK_OUTCOME_FAILSAFE.size();
        ATTACK_OUTCOME_FAILSAFE.clear();
        return count;
    }

    static String getMobAttackFailsafeDebug(class_3218 world, UUID mobUuid) {
        if (world == null || mobUuid == null) {
            return null;
        }
        AttackOutcomeFailsafeState state = ATTACK_OUTCOME_FAILSAFE.get(mobUuid);
        if (state == null || !state.worldKey.equals((Object)world.method_27983())) {
            return null;
        }
        long nowTick = world.method_75260();
        NonAttackSystem.pruneAttackOutcomeWindow(state, nowTick);
        long remaining = Math.max(0L, state.cooldownUntilTick - nowTick);
        int recent = state.recentAttackOutcomeTicks.size();
        if (remaining <= 0L && recent <= 0) {
            ATTACK_OUTCOME_FAILSAFE.remove(mobUuid, state);
            return null;
        }
        if (remaining > 0L) {
            return "attack failsafe: " + NeedsOfNature.formatTicksForDebug(remaining) + " remaining; peaks " + recent + "/" + NonAttackSystem.attackOutcomeFailsafeThreshold();
        }
        return "attack failsafe: peaks " + recent + "/" + NonAttackSystem.attackOutcomeFailsafeThreshold() + " in 10m window";
    }

    static NeedsOfNature.AttackBlockReason getMobAttackBlockReason(class_3218 world, class_1308 mob, class_3222 player) {
        EnergyHolder holder;
        if (world == null || mob == null || player == null) {
            return null;
        }
        if (mob.method_73183() != world || player.method_51469() != world) {
            return null;
        }
        if (NeedsOfNature.isPregnancyFriendlyMob(mob)) {
            return NeedsOfNature.AttackBlockReason.PREGNANCY_FRIENDLY;
        }
        if (!NonAttackSystem.isAggressiveThreat(mob, player)) {
            return null;
        }
        boolean mobBusy = NeedsOfNature.isMobGatheringOrActiveWithPlayer(world, mob, player);
        boolean playerBusy = NeedsOfNature.hasActivePlayerAnimation(player);
        if (mobBusy) {
            return NeedsOfNature.AttackBlockReason.GATHERING_PAIR;
        }
        if (playerBusy && mob instanceof EnergyHolder && (holder = (EnergyHolder)mob).getEnergy() > 70) {
            return NeedsOfNature.AttackBlockReason.ENERGY_ABOVE_70_PLAYER_BUSY;
        }
        return null;
    }

    static void onMobAttackBlocked(class_3218 world, class_1308 mob, class_3222 player, NeedsOfNature.AttackBlockReason reason) {
        if (world == null || mob == null || player == null || reason == null) {
            return;
        }
        if (reason == NeedsOfNature.AttackBlockReason.PREGNANCY_FRIENDLY) {
            if (mob.method_5968() == player) {
                mob.method_5980(null);
            }
            mob.method_5942().method_6340();
            if (mob instanceof class_5354) {
                UUID angryAtUuid;
                class_5354 angerable = (class_5354)mob;
                class_10583 angryAt = angerable.method_29508();
                UUID uUID = angryAtUuid = angryAt == null ? null : angryAt.method_66263();
                if (angryAtUuid == null || angryAtUuid.equals(player.method_5667())) {
                    angerable.method_29922();
                }
            }
        }
        if (!NeedsOfNature.getConfig().allowsDebugChat(NonDebugChatCategory.WARNING)) {
            return;
        }
        long now = world.method_75260();
        class_5321 worldKey = world.method_27983();
        UUID mobUuid = mob.method_5667();
        AttackBlockDebugState current = ATTACK_BLOCK_DEBUG.get(mobUuid);
        if (current != null && current.worldKey.equals((Object)worldKey) && now < current.nextTick) {
            return;
        }
        ATTACK_BLOCK_DEBUG.put(mobUuid, new AttackBlockDebugState((class_5321<class_1937>)worldKey, now + 400L));
        class_5250 mobName = class_2561.method_43471((String)mob.method_5864().method_5882());
        class_2561 playerName = player.method_5477();
        class_5250 message = switch (reason) {
            default -> throw new MatchException(null, null);
            case NeedsOfNature.AttackBlockReason.PREGNANCY_FRIENDLY -> class_2561.method_43469((String)"debug.needsofnature.attack_blocked_pregnancy_friendly", (Object[])new Object[]{mobName, playerName});
            case NeedsOfNature.AttackBlockReason.GATHERING_PAIR -> class_2561.method_43469((String)"debug.needsofnature.attack_blocked_gathering_pair", (Object[])new Object[]{mobName, playerName});
            case NeedsOfNature.AttackBlockReason.ENERGY_ABOVE_70_PLAYER_BUSY -> class_2561.method_43469((String)"debug.needsofnature.attack_blocked_energy_busy", (Object[])new Object[]{mobName, playerName});
        };
        NeedsOfNature.sendDebugChatToNearby(world, (class_1297)mob, 24.0, NonDebugChatCategory.WARNING, (class_2561)message);
    }

    private static boolean isAggressiveThreat(class_1308 mob, class_3222 player) {
        class_1309 target;
        if (mob == null || player == null) {
            return false;
        }
        if (mob instanceof class_1569) {
            return true;
        }
        if (mob instanceof class_5354) {
            UUID angryAtUuid;
            class_5354 angerable = (class_5354)mob;
            class_10583 angryAt = angerable.method_29508();
            UUID uUID = angryAtUuid = angryAt == null ? null : angryAt.method_66263();
            if (angryAtUuid != null && angryAtUuid.equals(player.method_5667())) {
                return true;
            }
        }
        return (target = mob.method_5968()) != null && target.method_5667().equals(player.method_5667());
    }

    static boolean hasPlayerAndHostileActor(List<? extends class_1297> actors) {
        if (actors == null || actors.isEmpty()) {
            return false;
        }
        boolean hasPlayer = false;
        boolean hasHostile = false;
        for (class_1297 class_12972 : actors) {
            if (class_12972 instanceof class_3222) {
                hasPlayer = true;
            } else if (NonAttackSystem.isHostileOrAngryNeutral(class_12972)) {
                hasHostile = true;
            }
            if (!hasPlayer || !hasHostile) continue;
            return true;
        }
        return false;
    }

    private static boolean isHostileOrAngryNeutral(class_1297 entity) {
        class_5354 angerable;
        block6: {
            block5: {
                if (!(entity instanceof class_1309)) {
                    return false;
                }
                if (entity instanceof class_1569) {
                    return true;
                }
                if (!(entity instanceof class_5354)) break block5;
                angerable = (class_5354)entity;
                if (entity instanceof class_1308) break block6;
            }
            return false;
        }
        class_1308 mob = (class_1308)entity;
        return angerable.method_29508() != null || mob.method_5968() instanceof class_3222;
    }

    static void recordAttackOutcomeForMobs(class_3218 world, List<UUID> actorUuids, String reason) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        class_5321 worldKey = world.method_27983();
        long nowTick = world.method_75260();
        for (UUID actorUuid : actorUuids) {
            class_1308 mob;
            class_1297 entity = world.method_66347(actorUuid);
            if (!(entity instanceof class_1308) || !(mob = (class_1308)entity).method_5805() || mob.method_31481()) continue;
            AttackOutcomeFailsafeState state = ATTACK_OUTCOME_FAILSAFE.computeIfAbsent(actorUuid, unused -> new AttackOutcomeFailsafeState((class_5321<class_1937>)worldKey));
            if (!state.worldKey.equals((Object)worldKey)) {
                state.worldKey = worldKey;
                state.recentAttackOutcomeTicks.clear();
                state.cooldownUntilTick = 0L;
            }
            if (state.cooldownUntilTick > nowTick) continue;
            NonAttackSystem.pruneAttackOutcomeWindow(state, nowTick);
            state.recentAttackOutcomeTicks.addLast(nowTick);
            if (state.recentAttackOutcomeTicks.size() < NonAttackSystem.attackOutcomeFailsafeThreshold()) continue;
            state.recentAttackOutcomeTicks.clear();
            state.cooldownUntilTick = nowTick + 12000L;
            NeedsOfNature.LOGGER.info("[NoN] Attack peak failsafe armed for mob={} in world={} reason={} untilTick={}", new Object[]{mob.method_5667(), worldKey.method_29177(), reason, state.cooldownUntilTick});
        }
    }

    static void startNearbyPostEscapeGathers(class_3218 world, class_3222 player, @Nullable List<UUID> escapedActorUuids) {
        if (world == null || player == null || player.method_51469() != world) {
            return;
        }
        if (!NonAttackSystem.canMobAttackPlayer(player)) {
            return;
        }
        if (!player.method_5805() || player.method_31481()) {
            return;
        }
        if (NonGatherSystem.isAutomaticMobPlayerStartBlockedByMount(player)) {
            return;
        }
        int maxMobs = NeedsOfNature.getConfig().getPostEscapeGatherMaxMobs();
        if (maxMobs <= 0) {
            return;
        }
        class_238 searchBox = player.method_5829().method_1014(10.0);
        List candidates = world.method_8390(class_1308.class, searchBox, mob -> NonAttackSystem.isPostEscapeGatherCandidate(world, player, mob, escapedActorUuids));
        if (candidates.isEmpty()) {
            return;
        }
        candidates.sort(Comparator.comparingDouble(arg_0 -> ((class_3222)player).method_5858(arg_0)));
        int started = 0;
        for (class_1308 mob2 : candidates) {
            boolean gatherStarted;
            if (started >= maxMobs) break;
            ArrayList<class_1297> actors = new ArrayList<class_1297>(2);
            actors.add((class_1297)mob2);
            actors.add((class_1297)player);
            actors.sort(Comparator.comparingInt(class_1297::method_5628));
            NonGatherSystem.GatherCandidate gatherCandidate = NonGatherSystem.findGatherCandidate(world, actors, true, (class_1297)mob2, (class_1297)player);
            if (gatherCandidate == null || !(gatherStarted = NonGatherSystem.startMobPlayerGather(world, mob2, player, gatherCandidate.animationId(), gatherCandidate.actorKeys(), gatherCandidate.stages()))) continue;
            ++started;
            NeedsOfNature.LOGGER.info("Post-escape gather started: mobType={} player={} anim={} energy={}", new Object[]{class_7923.field_41177.method_10221((Object)mob2.method_5864()), player.method_5477().getString(), gatherCandidate.animationId(), ((EnergyHolder)mob2).getEnergy()});
        }
        if (started > 0) {
            NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.INFO, (class_2561)class_2561.method_43469((String)"debug.needsofnature.post_escape_gather_started", (Object[])new Object[]{started}));
        }
    }

    private static boolean isPostEscapeGatherCandidate(class_3218 world, class_3222 player, class_1308 mob, @Nullable List<UUID> escapedActorUuids) {
        if (mob == null || mob.method_73183() != world) {
            return false;
        }
        if (!mob.method_5805() || mob.method_31481()) {
            return false;
        }
        if (!NonMobGatherEligibility.canAutoGather(mob)) {
            return false;
        }
        if (!(mob instanceof EnergyHolder)) {
            return false;
        }
        EnergyHolder holder = (EnergyHolder)mob;
        if (holder.getEnergy() < 70) {
            return false;
        }
        if (NonMobEnergySystem.isEnergyAttackStabilized((class_1297)mob) && holder.getEnergy() >= 200) {
            return false;
        }
        if (!NonMobEnergySystem.hasGenericAnimationSupport(mob.method_5864(), mob.method_6109())) {
            return false;
        }
        if (NeedsOfNature.isActorPendingOrActive(world, mob.method_5667())) {
            return false;
        }
        if (NeedsOfNature.isMobAttackFailsafeActive(world, mob.method_5667())) {
            return false;
        }
        if (!mob.method_6057((class_1297)player)) {
            return false;
        }
        if (escapedActorUuids != null && escapedActorUuids.contains(mob.method_5667())) {
            return true;
        }
        return mob.method_5858((class_1297)player) <= 100.0;
    }

    private static int attackOutcomeFailsafeThreshold() {
        NonConfig config = NeedsOfNature.getConfig();
        return config == null ? 3 : config.getAttackOutcomeFailsafeThreshold();
    }

    private static void pruneAttackOutcomeWindow(AttackOutcomeFailsafeState state, long nowTick) {
        if (state == null) {
            return;
        }
        long oldestAllowedTick = nowTick - 12000L;
        while (!state.recentAttackOutcomeTicks.isEmpty() && state.recentAttackOutcomeTicks.peekFirst() <= oldestAllowedTick) {
            state.recentAttackOutcomeTicks.removeFirst();
        }
    }

    static void pruneAttackOutcomeFailsafe(class_3218 world, long nowTick) {
        if (world == null || ATTACK_OUTCOME_FAILSAFE.isEmpty()) {
            return;
        }
        class_5321 worldKey = world.method_27983();
        ATTACK_OUTCOME_FAILSAFE.entrySet().removeIf(entry -> {
            AttackOutcomeFailsafeState state = (AttackOutcomeFailsafeState)entry.getValue();
            if (!state.worldKey.equals((Object)worldKey)) {
                return false;
            }
            NonAttackSystem.pruneAttackOutcomeWindow(state, nowTick);
            return state.cooldownUntilTick <= nowTick && state.recentAttackOutcomeTicks.isEmpty();
        });
    }

    static void pruneAttackBlockDebug(class_3218 world, long nowTick) {
        if (world == null || ATTACK_BLOCK_DEBUG.isEmpty()) {
            return;
        }
        class_5321 worldKey = world.method_27983();
        ATTACK_BLOCK_DEBUG.entrySet().removeIf(entry -> {
            AttackBlockDebugState state = (AttackBlockDebugState)entry.getValue();
            if (!state.worldKey.equals((Object)worldKey)) {
                return false;
            }
            return state.nextTick <= nowTick;
        });
    }

    static void applyAttackEscapeDamage(class_3218 world, class_3222 player, @Nullable List<UUID> actorUuids) {
        float extra;
        if (world == null || player == null) {
            return;
        }
        if (!player.method_5805()) {
            return;
        }
        float health = player.method_6032();
        if (health <= 1.0f) {
            return;
        }
        float maxRaw = health - 1.0f;
        if (maxRaw <= 0.0f) {
            return;
        }
        class_1309 attacker = NonAttackSystem.resolveAttackEscapeAttacker(world, player, actorUuids);
        float rawDamage = NonAttackSystem.resolveAttackEscapeRawDamage(attacker);
        rawDamage *= (float)NonTrinketsIntegration.getAccessoryEffects((class_1309)player).attackEscapeDamageMultiplier();
        if ((rawDamage = Math.min(rawDamage, maxRaw)) <= 0.0f) {
            return;
        }
        class_1282 source = NonAttackSystem.resolveAttackEscapeDamageSource(player, attacker);
        player.method_64397(world, source, rawDamage);
        float after = player.method_6032();
        float dealt = health - after;
        if (dealt < 1.0f && after > 1.0f && (extra = Math.min(1.0f - dealt, after - 1.0f)) > 0.0f) {
            player.method_6033(after - extra);
        }
    }

    @Nullable
    private static class_1309 resolveAttackEscapeAttacker(class_3218 world, class_3222 player, @Nullable List<UUID> actorUuids) {
        if (world == null || player == null || actorUuids == null || actorUuids.isEmpty()) {
            return null;
        }
        for (UUID actorUuid : actorUuids) {
            class_1309 living;
            class_1297 actor;
            if (actorUuid == null || actorUuid.equals(player.method_5667()) || !((actor = world.method_66347(actorUuid)) instanceof class_1309) || !(living = (class_1309)actor).method_5805() || living.method_31481()) continue;
            return living;
        }
        return null;
    }

    private static float resolveAttackEscapeRawDamage(@Nullable class_1309 attacker) {
        double attributeDamage;
        class_1324 attackDamage;
        float baseDamage = 4.0f;
        if (attacker != null && (attackDamage = attacker.method_5996(class_5134.field_23721)) != null && Double.isFinite(attributeDamage = attackDamage.method_6194()) && attributeDamage > 0.0) {
            baseDamage = (float)attributeDamage;
        }
        if (!Float.isFinite(baseDamage)) {
            baseDamage = 4.0f;
        }
        return Math.max(1.0f, baseDamage);
    }

    private static class_1282 resolveAttackEscapeDamageSource(class_3222 player, @Nullable class_1309 attacker) {
        if (player == null) {
            throw new IllegalArgumentException("player");
        }
        if (attacker instanceof class_1308) {
            class_1308 mob = (class_1308)attacker;
            return player.method_48923().method_48812((class_1309)mob);
        }
        return player.method_48923().method_48830();
    }

    private static final class AttackOutcomeFailsafeState {
        class_5321<class_1937> worldKey;
        final ArrayDeque<Long> recentAttackOutcomeTicks = new ArrayDeque();
        long cooldownUntilTick = 0L;

        AttackOutcomeFailsafeState(class_5321<class_1937> worldKey) {
            this.worldKey = worldKey;
        }
    }

    private record AttackBlockDebugState(class_5321<class_1937> worldKey, long nextTick) {
    }
}

