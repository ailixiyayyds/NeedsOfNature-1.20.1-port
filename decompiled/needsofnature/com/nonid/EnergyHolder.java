/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 */
package com.nonid;

import java.util.List;
import java.util.UUID;
import net.minecraft.class_1309;
import net.minecraft.class_3218;
import net.minecraft.class_3222;

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

    default public void ensureEnergyInitialized(class_3218 world) {
    }

    default public void onNonAnimationStarted() {
    }

    default public void onNonAttackEscaped(class_1309 player, int retryDelayTicks) {
    }

    default public void requestAttackOnPlayer(class_3218 world, class_3222 player) {
    }

    default public void applyPartnerCooldown(UUID partnerId, long nowTick, int cooldownTicks) {
    }

    default public List<String> getActiveCooldownDebugLines(class_3218 world) {
        return List.of();
    }

    default public double getEnergyAuraCarry() {
        return 0.0;
    }

    default public void setEnergyAuraCarry(double value) {
    }
}

