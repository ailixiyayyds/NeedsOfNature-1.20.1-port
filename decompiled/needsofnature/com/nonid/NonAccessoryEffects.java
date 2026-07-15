/*
 * Decompiled with CFR 0.152.
 */
package com.nonid;

public record NonAccessoryEffects(double liquidDecayMultiplier, boolean equalizeLiquidDecayContext, double playerEnergyGainMultiplier, int liquidCapacityAdd, double liquidGainMultiplier, double filledEffectMultiplier, double pregnancyChanceMultiplier, double pregnancyDurationMultiplier, double messGainMultiplier, double destroyedSkinDamageMultiplier, int attackEscapeHitsAdd, double attackEscapeDamageMultiplier, double playerEnergyAuraMultiplier, double nearAnimationMobEnergyMultiplier) {
    public static final NonAccessoryEffects NEUTRAL = new NonAccessoryEffects(1.0, false, 1.0, 0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0, 1.0, 1.0, 1.0);

    public NonAccessoryEffects combine(NonAccessoryEffects other) {
        if (other == null) {
            return this;
        }
        return new NonAccessoryEffects(this.liquidDecayMultiplier * other.liquidDecayMultiplier, this.equalizeLiquidDecayContext || other.equalizeLiquidDecayContext, this.playerEnergyGainMultiplier * other.playerEnergyGainMultiplier, this.liquidCapacityAdd + other.liquidCapacityAdd, this.liquidGainMultiplier * other.liquidGainMultiplier, this.filledEffectMultiplier * other.filledEffectMultiplier, this.pregnancyChanceMultiplier * other.pregnancyChanceMultiplier, this.pregnancyDurationMultiplier * other.pregnancyDurationMultiplier, this.messGainMultiplier * other.messGainMultiplier, this.destroyedSkinDamageMultiplier * other.destroyedSkinDamageMultiplier, this.attackEscapeHitsAdd + other.attackEscapeHitsAdd, this.attackEscapeDamageMultiplier * other.attackEscapeDamageMultiplier, this.playerEnergyAuraMultiplier * other.playerEnergyAuraMultiplier, this.nearAnimationMobEnergyMultiplier * other.nearAnimationMobEnergyMultiplier);
    }

    public NonAccessoryEffects globalOnly() {
        return new NonAccessoryEffects(1.0, false, this.playerEnergyGainMultiplier, 0, 1.0, 1.0, 1.0, 1.0, 1.0, this.destroyedSkinDamageMultiplier, this.attackEscapeHitsAdd, this.attackEscapeDamageMultiplier, this.playerEnergyAuraMultiplier, this.nearAnimationMobEnergyMultiplier);
    }

    public NonAccessoryEffects tankScopedOnly() {
        return new NonAccessoryEffects(this.liquidDecayMultiplier, this.equalizeLiquidDecayContext, 1.0, this.liquidCapacityAdd, this.liquidGainMultiplier, this.filledEffectMultiplier, this.pregnancyChanceMultiplier, this.pregnancyDurationMultiplier, this.messGainMultiplier, 1.0, 0, 1.0, 1.0, 1.0);
    }
}

