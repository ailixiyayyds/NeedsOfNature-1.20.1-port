/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 */
package com.nonid;

import java.util.List;
import java.util.UUID;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;

public interface EnergyHolder {
    public int getEnergy();

    public int getMaxEnergy();

    public void setEnergy(int var1);

    default public float getEnergyGainMultiplier() {
        return 1.0f;
    }

    default public void setEnergyGainMultiplier(float value) {
    }

    default public float getEnergyGainDrift() {
        return 1.0f;
    }

    default public void setEnergyGainDrift(float value) {
    }

    default public boolean isEnergyInitialized() {
        return false;
    }

    default public void markEnergyInitialized() {
    }

    default public void ensureEnergyInitialized(ServerWorld world) {
    }

    default public void onNonAnimationStarted() {
    }

    default public void onNonAttackEscaped(LivingEntity player, int retryDelayTicks) {
    }

    default public void requestAttackOnPlayer(ServerWorld world, ServerPlayerEntity player) {
    }

    default public void applyPartnerCooldown(UUID partnerId, long nowTick, int cooldownTicks) {
    }

    default public List<String> getActiveCooldownDebugLines(ServerWorld world) {
        return List.of();
    }

    default public double getEnergyAuraCarry() {
        return 0.0;
    }

    default public void setEnergyAuraCarry(double value) {
    }
}

