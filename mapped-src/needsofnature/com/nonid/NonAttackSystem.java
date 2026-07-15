/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  net.minecraft.entity.LazyEntityReference
 *  net.minecraft.entity.damage.DamageSource
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.attribute.EntityAttributeInstance
 *  net.minecraft.entity.mob.Monster
 *  net.minecraft.world.World
 *  net.minecraft.util.math.Box
 *  net.minecraft.text.Text
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.entity.attribute.EntityAttributes
 *  net.minecraft.text.MutableText
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.entity.mob.Angerable
 *  net.minecraft.registry.Registries
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
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.mob.Monster;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;
import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.MutableText;
import net.minecraft.registry.RegistryKey;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.registry.Registries;
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

    static boolean canMobAttackPlayer(ServerPlayerEntity player) {
        if (player == null) {
            return false;
        }
        if (player.isSpectator()) {
            return false;
        }
        if (!player.isCreative()) {
            return true;
        }
        NonConfig config = NeedsOfNature.getConfig();
        return config != null && config.canAttackCreativePlayers();
    }

    static void setAnimationStartProtection(ServerPlayerEntity player, int ticks) {
        if (player == null) {
            return;
        }
        AfwAnimationApi.setAnimationStartProtection((ServerPlayerEntity)player, (int)ticks);
    }

    static boolean hasAnimationStartProtection(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return false;
        }
        return AfwAnimationApi.hasAnimationStartProtection((ServerWorld)world, (ServerPlayerEntity)player);
    }

    static boolean isMobAttackFailsafeActive(ServerWorld world, UUID mobUuid) {
        if (world == null || mobUuid == null) {
            return false;
        }
        AttackOutcomeFailsafeState state = ATTACK_OUTCOME_FAILSAFE.get(mobUuid);
        if (state == null) {
            return false;
        }
        if (!state.worldKey.equals((Object)world.getRegistryKey())) {
            return false;
        }
        long nowTick = world.getTime();
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

    static String getMobAttackFailsafeDebug(ServerWorld world, UUID mobUuid) {
        if (world == null || mobUuid == null) {
            return null;
        }
        AttackOutcomeFailsafeState state = ATTACK_OUTCOME_FAILSAFE.get(mobUuid);
        if (state == null || !state.worldKey.equals((Object)world.getRegistryKey())) {
            return null;
        }
        long nowTick = world.getTime();
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

    static NeedsOfNature.AttackBlockReason getMobAttackBlockReason(ServerWorld world, MobEntity mob, ServerPlayerEntity player) {
        EnergyHolder holder;
        if (world == null || mob == null || player == null) {
            return null;
        }
        if (mob.getEntityWorld() != world || player.getEntityWorld() != world) {
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

    static void onMobAttackBlocked(ServerWorld world, MobEntity mob, ServerPlayerEntity player, NeedsOfNature.AttackBlockReason reason) {
        if (world == null || mob == null || player == null || reason == null) {
            return;
        }
        if (reason == NeedsOfNature.AttackBlockReason.PREGNANCY_FRIENDLY) {
            if (mob.getTarget() == player) {
                mob.setTarget(null);
            }
            mob.getNavigation().stop();
            if (mob instanceof Angerable) {
                UUID angryAtUuid;
                Angerable angerable = (Angerable)mob;
                LazyEntityReference angryAt = angerable.getAngryAt();
                UUID uUID = angryAtUuid = angryAt == null ? null : angryAt.getUuid();
                if (angryAtUuid == null || angryAtUuid.equals(player.getUuid())) {
                    angerable.stopAnger();
                }
            }
        }
        if (!NeedsOfNature.getConfig().allowsDebugChat(NonDebugChatCategory.WARNING)) {
            return;
        }
        long now = world.getTime();
        RegistryKey worldKey = world.getRegistryKey();
        UUID mobUuid = mob.getUuid();
        AttackBlockDebugState current = ATTACK_BLOCK_DEBUG.get(mobUuid);
        if (current != null && current.worldKey.equals((Object)worldKey) && now < current.nextTick) {
            return;
        }
        ATTACK_BLOCK_DEBUG.put(mobUuid, new AttackBlockDebugState((RegistryKey<World>)worldKey, now + 400L));
        MutableText mobName = Text.translatable((String)mob.getType().getTranslationKey());
        Text playerName = player.getName();
        MutableText message = switch (reason) {
            default -> throw new MatchException(null, null);
            case NeedsOfNature.AttackBlockReason.PREGNANCY_FRIENDLY -> Text.translatable((String)"debug.needsofnature.attack_blocked_pregnancy_friendly", (Object[])new Object[]{mobName, playerName});
            case NeedsOfNature.AttackBlockReason.GATHERING_PAIR -> Text.translatable((String)"debug.needsofnature.attack_blocked_gathering_pair", (Object[])new Object[]{mobName, playerName});
            case NeedsOfNature.AttackBlockReason.ENERGY_ABOVE_70_PLAYER_BUSY -> Text.translatable((String)"debug.needsofnature.attack_blocked_energy_busy", (Object[])new Object[]{mobName, playerName});
        };
        NeedsOfNature.sendDebugChatToNearby(world, (Entity)mob, 24.0, NonDebugChatCategory.WARNING, (Text)message);
    }

    private static boolean isAggressiveThreat(MobEntity mob, ServerPlayerEntity player) {
        LivingEntity target;
        if (mob == null || player == null) {
            return false;
        }
        if (mob instanceof Monster) {
            return true;
        }
        if (mob instanceof Angerable) {
            UUID angryAtUuid;
            Angerable angerable = (Angerable)mob;
            LazyEntityReference angryAt = angerable.getAngryAt();
            UUID uUID = angryAtUuid = angryAt == null ? null : angryAt.getUuid();
            if (angryAtUuid != null && angryAtUuid.equals(player.getUuid())) {
                return true;
            }
        }
        return (target = mob.getTarget()) != null && target.getUuid().equals(player.getUuid());
    }

    static boolean hasPlayerAndHostileActor(List<? extends Entity> actors) {
        if (actors == null || actors.isEmpty()) {
            return false;
        }
        boolean hasPlayer = false;
        boolean hasHostile = false;
        for (Entity class_12972 : actors) {
            if (class_12972 instanceof ServerPlayerEntity) {
                hasPlayer = true;
            } else if (NonAttackSystem.isHostileOrAngryNeutral(class_12972)) {
                hasHostile = true;
            }
            if (!hasPlayer || !hasHostile) continue;
            return true;
        }
        return false;
    }

    private static boolean isHostileOrAngryNeutral(Entity entity) {
        Angerable angerable;
        block6: {
            block5: {
                if (!(entity instanceof LivingEntity)) {
                    return false;
                }
                if (entity instanceof Monster) {
                    return true;
                }
                if (!(entity instanceof Angerable)) break block5;
                angerable = (Angerable)entity;
                if (entity instanceof MobEntity) break block6;
            }
            return false;
        }
        MobEntity mob = (MobEntity)entity;
        return angerable.getAngryAt() != null || mob.getTarget() instanceof ServerPlayerEntity;
    }

    static void recordAttackOutcomeForMobs(ServerWorld world, List<UUID> actorUuids, String reason) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        long nowTick = world.getTime();
        for (UUID actorUuid : actorUuids) {
            MobEntity mob;
            Entity entity = world.getEntity(actorUuid);
            if (!(entity instanceof MobEntity) || !(mob = (MobEntity)entity).isAlive() || mob.isRemoved()) continue;
            AttackOutcomeFailsafeState state = ATTACK_OUTCOME_FAILSAFE.computeIfAbsent(actorUuid, unused -> new AttackOutcomeFailsafeState((RegistryKey<World>)worldKey));
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
            NeedsOfNature.LOGGER.info("[NoN] Attack peak failsafe armed for mob={} in world={} reason={} untilTick={}", new Object[]{mob.getUuid(), worldKey.getValue(), reason, state.cooldownUntilTick});
        }
    }

    static void startNearbyPostEscapeGathers(ServerWorld world, ServerPlayerEntity player, @Nullable List<UUID> escapedActorUuids) {
        if (world == null || player == null || player.getEntityWorld() != world) {
            return;
        }
        if (!NonAttackSystem.canMobAttackPlayer(player)) {
            return;
        }
        if (!player.isAlive() || player.isRemoved()) {
            return;
        }
        if (NonGatherSystem.isAutomaticMobPlayerStartBlockedByMount(player)) {
            return;
        }
        int maxMobs = NeedsOfNature.getConfig().getPostEscapeGatherMaxMobs();
        if (maxMobs <= 0) {
            return;
        }
        Box searchBox = player.getBoundingBox().expand(10.0);
        List candidates = world.getEntitiesByClass(MobEntity.class, searchBox, mob -> NonAttackSystem.isPostEscapeGatherCandidate(world, player, mob, escapedActorUuids));
        if (candidates.isEmpty()) {
            return;
        }
        candidates.sort(Comparator.comparingDouble(arg_0 -> ((ServerPlayerEntity)player).squaredDistanceTo(arg_0)));
        int started = 0;
        for (MobEntity mob2 : candidates) {
            boolean gatherStarted;
            if (started >= maxMobs) break;
            ArrayList<Entity> actors = new ArrayList<Entity>(2);
            actors.add((Entity)mob2);
            actors.add((Entity)player);
            actors.sort(Comparator.comparingInt(Entity::getId));
            NonGatherSystem.GatherCandidate gatherCandidate = NonGatherSystem.findGatherCandidate(world, actors, true, (Entity)mob2, (Entity)player);
            if (gatherCandidate == null || !(gatherStarted = NonGatherSystem.startMobPlayerGather(world, mob2, player, gatherCandidate.animationId(), gatherCandidate.actorKeys(), gatherCandidate.stages()))) continue;
            ++started;
            NeedsOfNature.LOGGER.info("Post-escape gather started: mobType={} player={} anim={} energy={}", new Object[]{Registries.ENTITY_TYPE.getId((Object)mob2.getType()), player.getName().getString(), gatherCandidate.animationId(), ((EnergyHolder)mob2).getEnergy()});
        }
        if (started > 0) {
            NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.INFO, (Text)Text.translatable((String)"debug.needsofnature.post_escape_gather_started", (Object[])new Object[]{started}));
        }
    }

    private static boolean isPostEscapeGatherCandidate(ServerWorld world, ServerPlayerEntity player, MobEntity mob, @Nullable List<UUID> escapedActorUuids) {
        if (mob == null || mob.getEntityWorld() != world) {
            return false;
        }
        if (!mob.isAlive() || mob.isRemoved()) {
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
        if (NonMobEnergySystem.isEnergyAttackStabilized((Entity)mob) && holder.getEnergy() >= 200) {
            return false;
        }
        if (!NonMobEnergySystem.hasGenericAnimationSupport(mob.getType(), mob.isBaby())) {
            return false;
        }
        if (NeedsOfNature.isActorPendingOrActive(world, mob.getUuid())) {
            return false;
        }
        if (NeedsOfNature.isMobAttackFailsafeActive(world, mob.getUuid())) {
            return false;
        }
        if (!mob.canSee((Entity)player)) {
            return false;
        }
        if (escapedActorUuids != null && escapedActorUuids.contains(mob.getUuid())) {
            return true;
        }
        return mob.squaredDistanceTo((Entity)player) <= 100.0;
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

    static void pruneAttackOutcomeFailsafe(ServerWorld world, long nowTick) {
        if (world == null || ATTACK_OUTCOME_FAILSAFE.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        ATTACK_OUTCOME_FAILSAFE.entrySet().removeIf(entry -> {
            AttackOutcomeFailsafeState state = (AttackOutcomeFailsafeState)entry.getValue();
            if (!state.worldKey.equals((Object)worldKey)) {
                return false;
            }
            NonAttackSystem.pruneAttackOutcomeWindow(state, nowTick);
            return state.cooldownUntilTick <= nowTick && state.recentAttackOutcomeTicks.isEmpty();
        });
    }

    static void pruneAttackBlockDebug(ServerWorld world, long nowTick) {
        if (world == null || ATTACK_BLOCK_DEBUG.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        ATTACK_BLOCK_DEBUG.entrySet().removeIf(entry -> {
            AttackBlockDebugState state = (AttackBlockDebugState)entry.getValue();
            if (!state.worldKey.equals((Object)worldKey)) {
                return false;
            }
            return state.nextTick <= nowTick;
        });
    }

    static void applyAttackEscapeDamage(ServerWorld world, ServerPlayerEntity player, @Nullable List<UUID> actorUuids) {
        float extra;
        if (world == null || player == null) {
            return;
        }
        if (!player.isAlive()) {
            return;
        }
        float health = player.getHealth();
        if (health <= 1.0f) {
            return;
        }
        float maxRaw = health - 1.0f;
        if (maxRaw <= 0.0f) {
            return;
        }
        LivingEntity attacker = NonAttackSystem.resolveAttackEscapeAttacker(world, player, actorUuids);
        float rawDamage = NonAttackSystem.resolveAttackEscapeRawDamage(attacker);
        rawDamage *= (float)NonTrinketsIntegration.getAccessoryEffects((LivingEntity)player).attackEscapeDamageMultiplier();
        if ((rawDamage = Math.min(rawDamage, maxRaw)) <= 0.0f) {
            return;
        }
        DamageSource source = NonAttackSystem.resolveAttackEscapeDamageSource(player, attacker);
        player.damage(world, source, rawDamage);
        float after = player.getHealth();
        float dealt = health - after;
        if (dealt < 1.0f && after > 1.0f && (extra = Math.min(1.0f - dealt, after - 1.0f)) > 0.0f) {
            player.setHealth(after - extra);
        }
    }

    @Nullable
    private static LivingEntity resolveAttackEscapeAttacker(ServerWorld world, ServerPlayerEntity player, @Nullable List<UUID> actorUuids) {
        if (world == null || player == null || actorUuids == null || actorUuids.isEmpty()) {
            return null;
        }
        for (UUID actorUuid : actorUuids) {
            LivingEntity living;
            Entity actor;
            if (actorUuid == null || actorUuid.equals(player.getUuid()) || !((actor = world.getEntity(actorUuid)) instanceof LivingEntity) || !(living = (LivingEntity)actor).isAlive() || living.isRemoved()) continue;
            return living;
        }
        return null;
    }

    private static float resolveAttackEscapeRawDamage(@Nullable LivingEntity attacker) {
        double attributeDamage;
        EntityAttributeInstance attackDamage;
        float baseDamage = 4.0f;
        if (attacker != null && (attackDamage = attacker.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)) != null && Double.isFinite(attributeDamage = attackDamage.getValue()) && attributeDamage > 0.0) {
            baseDamage = (float)attributeDamage;
        }
        if (!Float.isFinite(baseDamage)) {
            baseDamage = 4.0f;
        }
        return Math.max(1.0f, baseDamage);
    }

    private static DamageSource resolveAttackEscapeDamageSource(ServerPlayerEntity player, @Nullable LivingEntity attacker) {
        if (player == null) {
            throw new IllegalArgumentException("player");
        }
        if (attacker instanceof MobEntity) {
            MobEntity mob = (MobEntity)attacker;
            return player.getDamageSources().mobAttack((LivingEntity)mob);
        }
        return player.getDamageSources().generic();
    }

    private static final class AttackOutcomeFailsafeState {
        RegistryKey<World> worldKey;
        final ArrayDeque<Long> recentAttackOutcomeTicks = new ArrayDeque();
        long cooldownUntilTick = 0L;

        AttackOutcomeFailsafeState(RegistryKey<World> worldKey) {
            this.worldKey = worldKey;
        }
    }

    private record AttackBlockDebugState(RegistryKey<World> worldKey, long nextTick) {
    }
}

