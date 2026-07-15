/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Box
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.util.math.MathHelper
 */
package com.nonid;

import com.nonid.EnergyHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.NonInjectorMatchPolicy;
import com.nonid.NonTrinketsIntegration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

final class NonAuraSystem {
    private static final int MESS_AURA_MAX_TOTAL = 15;
    private static final double MESS_AURA_MAX_MULTIPLIER = 10.0;

    private NonAuraSystem() {
    }

    static void tickPlayerEnergyAura(ServerWorld world, ServerPlayerEntity player) {
        double auraMultiplier;
        int messTotal;
        if (world == null || player == null || !player.isAlive() || player.isRemoved()) {
            return;
        }
        if (!(player instanceof EnergyHolder)) {
            return;
        }
        EnergyHolder playerEnergyHolder = (EnergyHolder)player;
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return;
        }
        int pulseTicks = config.getPlayerEnergyAuraPulseTicks();
        if (pulseTicks <= 0 || world.getTime() % (long)pulseTicks != 0L) {
            return;
        }
        int playerEnergy = playerEnergyHolder.getEnergy();
        int destroyedStage = config.isDestroyedSkinSystemEnabled() ? NeedsOfNature.getDestroyedSkinStageForPlayer(player) : 0;
        int n = messTotal = config.isMessSystemEnabled() ? NeedsOfNature.getTotalMessForPlayer(player) : 0;
        if (config.isMessSystemEnabled()) {
            NeedsOfNature.checkBadIdeaBeaconAdvancement(player);
        }
        if (!Double.isFinite(auraMultiplier = NonAuraSystem.getCombinedPlayerAuraMultiplier(config, playerEnergy, destroyedStage, messTotal) * NonTrinketsIntegration.getAccessoryEffects((LivingEntity)player).playerEnergyAuraMultiplier()) || auraMultiplier <= 1.0) {
            return;
        }
        double radius = NonAuraSystem.getCombinedPlayerAuraRadius(config, playerEnergy, destroyedStage, messTotal);
        if (!Double.isFinite(radius) || radius <= 0.0) {
            return;
        }
        double radiusSq = radius * radius;
        Box searchBox = player.getBoundingBox().expand(radius);
        List<MobEntity> mobs = world.getEntitiesByClass(MobEntity.class, searchBox, mob -> mob.isAlive() && !mob.isRemoved() && mob.getEntityWorld() == world && mob.squaredDistanceTo((Entity)player) <= radiusSq);
        if (mobs.isEmpty()) {
            return;
        }
        for (MobEntity mob2 : mobs) {
            double baseGain;
            if (!(mob2 instanceof EnergyHolder)) continue;
            EnergyHolder holder = (EnergyHolder)mob2;
            holder.ensureEnergyInitialized(world);
            int currentEnergy = holder.getEnergy();
            int maxEnergy = holder.getMaxEnergy();
            if (currentEnergy >= maxEnergy) {
                holder.setEnergyAuraCarry(0.0);
                continue;
            }
            if (!NonAuraSystem.hasPairAnimationWithPlayer(mob2, player) || !Double.isFinite(baseGain = NonAuraSystem.getExpectedMobEnergyGainPerPulse(world, mob2, holder, pulseTicks)) || baseGain <= 0.0) continue;
            double bonusGain = baseGain * (auraMultiplier - 1.0);
            double totalGain = holder.getEnergyAuraCarry() + bonusGain;
            int rawApplied = MathHelper.floor((double)totalGain);
            if (rawApplied <= 0) {
                holder.setEnergyAuraCarry(totalGain);
                continue;
            }
            int appliedGain = Math.min(rawApplied, maxEnergy - currentEnergy);
            if (appliedGain <= 0) {
                holder.setEnergyAuraCarry(0.0);
                continue;
            }
            holder.setEnergy(currentEnergy + appliedGain);
            holder.setEnergyAuraCarry(appliedGain < rawApplied ? 0.0 : totalGain - (double)appliedGain);
        }
    }

    static PlayerEnergyAuraDebug getPlayerEnergyAuraDebug(ServerWorld world, MobEntity mob, EnergyHolder holder) {
        if (world == null || mob == null || holder == null) {
            return null;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return null;
        }
        PlayerEnergyAuraDebug best = null;
        for (ServerPlayerEntity player : world.getPlayers()) {
            double totalMultiplier;
            int messTotal;
            int destroyedStage;
            EnergyHolder playerEnergyHolder;
            int playerEnergy;
            double radius;
            if (player == null || !player.isAlive() || player.isRemoved() || !(player instanceof EnergyHolder) || !Double.isFinite(radius = NonAuraSystem.getCombinedPlayerAuraRadius(config, playerEnergy = (playerEnergyHolder = (EnergyHolder)player).getEnergy(), destroyedStage = config.isDestroyedSkinSystemEnabled() ? NeedsOfNature.getDestroyedSkinStageForPlayer(player) : 0, messTotal = config.isMessSystemEnabled() ? NeedsOfNature.getTotalMessForPlayer(player) : 0)) || radius <= 0.0 || mob.squaredDistanceTo((Entity)player) > radius * radius || !NonAuraSystem.hasPairAnimationWithPlayer(mob, player) || !Double.isFinite(totalMultiplier = NonAuraSystem.getCombinedPlayerAuraMultiplier(config, playerEnergy, destroyedStage, messTotal) * NonTrinketsIntegration.getAccessoryEffects((LivingEntity)player).playerEnergyAuraMultiplier()) || totalMultiplier <= 1.0 || best != null && !(totalMultiplier > best.totalMultiplier())) continue;
            best = new PlayerEnergyAuraDebug(player, totalMultiplier, playerEnergy, destroyedStage, messTotal, radius);
        }
        return best;
    }

    private static double getCombinedPlayerAuraMultiplier(NonConfig config, int playerEnergy, int destroyedStage, int messTotal) {
        if (config == null) {
            return 1.0;
        }
        double energyBonus = Math.max(0.0, NonAuraSystem.getPlayerEnergyAuraMultiplier(config, playerEnergy) - 1.0);
        double destroyedBonus = Math.max(0.0, NonAuraSystem.getDestroyedSkinAuraMultiplier(config, destroyedStage) - 1.0);
        double messBonus = Math.max(0.0, NonAuraSystem.getMessAuraMultiplier(messTotal) - 1.0);
        return 1.0 + energyBonus + destroyedBonus + messBonus;
    }

    private static double getPlayerEnergyAuraMultiplier(NonConfig config, int playerEnergy) {
        if (config == null || playerEnergy < 100) {
            return 1.0;
        }
        if (playerEnergy >= 200) {
            return config.getPlayerEnergyAuraMultHigh();
        }
        if (playerEnergy >= 150) {
            return config.getPlayerEnergyAuraMultMid();
        }
        return config.getPlayerEnergyAuraMultLow();
    }

    private static double getDestroyedSkinAuraMultiplier(NonConfig config, int stage) {
        if (config == null) {
            return 1.0;
        }
        return switch (stage) {
            case 1 -> config.getDestroyedSkinAuraMultStage1();
            case 2 -> config.getDestroyedSkinAuraMultStage2();
            case 3 -> config.getDestroyedSkinAuraMultStage3();
            case 4 -> config.getDestroyedSkinAuraMultStage4();
            default -> 1.0;
        };
    }

    private static double getMessAuraMultiplier(int messTotal) {
        if (messTotal <= 0) {
            return 1.0;
        }
        double progress = MathHelper.clamp((double)((double)messTotal / 15.0), (double)0.0, (double)1.0);
        return MathHelper.lerp((double)progress, (double)1.0, (double)10.0);
    }

    private static double getPlayerEnergyAuraRadius(NonConfig config, int playerEnergy) {
        if (config == null || playerEnergy < 100) {
            return 0.0;
        }
        double minRadius = config.getPlayerEnergyAuraRadiusMin();
        double maxRadius = config.getPlayerEnergyAuraRadiusMax();
        double progress = MathHelper.clamp((double)((double)(playerEnergy - 100) / 100.0), (double)0.0, (double)1.0);
        return MathHelper.lerp((double)progress, (double)minRadius, (double)maxRadius);
    }

    private static double getCombinedPlayerAuraRadius(NonConfig config, int playerEnergy, int destroyedStage, int messTotal) {
        if (config == null) {
            return 0.0;
        }
        if (playerEnergy >= 100) {
            return NonAuraSystem.getPlayerEnergyAuraRadius(config, playerEnergy);
        }
        if (destroyedStage > 0 || messTotal > 0) {
            return config.getPlayerEnergyAuraRadiusMin();
        }
        return 0.0;
    }

    private static boolean hasPairAnimationWithPlayer(MobEntity mob, ServerPlayerEntity player) {
        if (mob == null || player == null) {
            return false;
        }
        ArrayList<Entity> pair = new ArrayList<Entity>(2);
        pair.add(mob);
        pair.add(player);
        pair.sort(Comparator.comparingInt(Entity::getId));
        return NeedsOfNature.findAnimationForActors((ServerWorld)player.getEntityWorld(), pair, Set.of(), null, false, null, Set.of(), NonInjectorMatchPolicy::allowsAutomaticNonMatch) != null;
    }

    private static double getExpectedMobEnergyGainPerPulse(ServerWorld world, MobEntity mob, EnergyHolder holder, int pulseTicks) {
        if (world == null || mob == null || holder == null || pulseTicks <= 0) {
            return 0.0;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return 0.0;
        }
        double scale = (double)(holder.getEnergyGainMultiplier() * holder.getEnergyGainDrift()) * config.getEnergyGainRate();
        if (!Double.isFinite(scale *= NeedsOfNature.getNearbyVisibleAnimationGainMultiplier(world, mob)) || scale <= 0.0) {
            return 0.0;
        }
        return (double)pulseTicks * scale / 120.0;
    }

    record PlayerEnergyAuraDebug(ServerPlayerEntity player, double totalMultiplier, int playerEnergy, int destroyedSkinStage, int messTotal, double radius) {
    }
}

