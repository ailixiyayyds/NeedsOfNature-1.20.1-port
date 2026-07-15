/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1937
 *  net.minecraft.class_2940
 *  net.minecraft.class_2941
 *  net.minecraft.class_2943
 *  net.minecraft.class_2945
 *  net.minecraft.class_2945$class_9222
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.EnergyHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonTrinketsIntegration;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2940;
import net.minecraft.class_2941;
import net.minecraft.class_2943;
import net.minecraft.class_2945;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1657.class})
public abstract class PlayerEntityEnergyMixin
implements EnergyHolder {
    @Unique
    private static final class_2940<Integer> ENERGY = class_2945.method_12791(class_1657.class, (class_2941)class_2943.field_13327);
    @Unique
    private static final int DEFAULT_TICKS_PER_ENERGY = 120;
    @Unique
    private static final int WATER_ENERGY_DECAY_TICKS = 10;
    @Unique
    private int non$energyTickCounter;
    @Unique
    private int non$waterEnergyDecayTickCounter;
    @Unique
    private boolean non$energyInitialized;
    @Unique
    private float non$energyGainMult = 1.0f;
    @Unique
    private float non$energyGainDrift = 1.0f;

    @Inject(method={"method_5693"}, at={@At(value="TAIL")})
    private void needsOfNature$initEnergyTracker(class_2945.class_9222 builder, CallbackInfo ci) {
        builder.method_56912(ENERGY, (Object)0);
    }

    @Inject(method={"method_5773"}, at={@At(value="TAIL")})
    private void needsOfNature$tickEnergy(CallbackInfo ci) {
        class_1657 self = (class_1657)this;
        if (self.method_73183().method_8608()) {
            return;
        }
        class_1937 class_19372 = self.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            return;
        }
        class_3218 serverWorld = (class_3218)class_19372;
        this.ensureEnergyInitialized(serverWorld);
        if (self instanceof class_3222) {
            class_3222 serverPlayer = (class_3222)self;
            NonTrinketsIntegration.syncAccessoryStateIfChanged(serverPlayer);
        }
        int energy = this.getEnergy();
        energy = this.non$applyWaterEnergyDecay(self, energy);
        int ticksPerGain = this.non$getTicksPerGain();
        if (energy >= 200) {
            this.non$energyTickCounter = 0;
            if (self instanceof class_3222) {
                class_3222 serverPlayer = (class_3222)self;
                NeedsOfNature.updateEnergizedEffect(serverPlayer, this);
                NeedsOfNature.tickPlayerEnergyAura(serverWorld, serverPlayer);
            }
            return;
        }
        ++this.non$energyTickCounter;
        if (this.non$energyTickCounter >= ticksPerGain) {
            this.non$energyTickCounter = 0;
            this.setEnergy(Math.min(200, energy + 1));
        }
        if (self instanceof class_3222) {
            class_3222 serverPlayer = (class_3222)self;
            NeedsOfNature.updateEnergizedEffect(serverPlayer, this);
            NeedsOfNature.tickPlayerEnergyAura(serverWorld, serverPlayer);
        }
    }

    @Unique
    private int non$applyWaterEnergyDecay(class_1657 self, int energy) {
        if (!self.method_5799() || energy <= 0) {
            this.non$waterEnergyDecayTickCounter = 0;
            return energy;
        }
        ++this.non$waterEnergyDecayTickCounter;
        if (this.non$waterEnergyDecayTickCounter < 10) {
            return energy;
        }
        this.non$waterEnergyDecayTickCounter = 0;
        int next = Math.max(0, energy - 1);
        this.setEnergy(next);
        return next;
    }

    @Override
    public int getEnergy() {
        class_1657 self = (class_1657)this;
        return (Integer)self.method_5841().method_12789(ENERGY);
    }

    @Override
    public int getMaxEnergy() {
        return 200;
    }

    @Override
    public void setEnergy(int value) {
        class_1657 self = (class_1657)this;
        self.method_5841().method_12778(ENERGY, (Object)Math.min(200, Math.max(0, value)));
    }

    @Override
    public float getEnergyGainMultiplier() {
        return this.non$energyGainMult <= 0.0f ? 1.0f : this.non$energyGainMult;
    }

    @Override
    public void setEnergyGainMultiplier(float value) {
        if (!Float.isFinite(value)) {
            return;
        }
        this.non$energyGainMult = Math.max(0.1f, Math.min(10.0f, value));
    }

    @Override
    public float getEnergyGainDrift() {
        return this.non$energyGainDrift <= 0.0f ? 1.0f : this.non$energyGainDrift;
    }

    @Override
    public void setEnergyGainDrift(float value) {
        if (!Float.isFinite(value)) {
            return;
        }
        this.non$energyGainDrift = Math.max(0.1f, Math.min(10.0f, value));
    }

    @Override
    public boolean isEnergyInitialized() {
        return this.non$energyInitialized;
    }

    @Override
    public void markEnergyInitialized() {
        this.non$energyInitialized = true;
    }

    @Override
    public void ensureEnergyInitialized(class_3218 world) {
        if (this.non$energyInitialized) {
            return;
        }
        this.non$energyInitialized = true;
        if (this.non$energyGainMult <= 0.0f) {
            this.non$energyGainMult = 1.0f;
        }
        if (this.non$energyGainDrift <= 0.0f) {
            this.non$energyGainDrift = 1.0f;
        }
        if (this.getEnergy() < 0) {
            this.setEnergy(0);
        }
    }

    @Unique
    private int non$getTicksPerGain() {
        double rate = NeedsOfNature.getConfig().getEnergyGainRate();
        float scale = this.getEnergyGainMultiplier() * this.getEnergyGainDrift() * (float)rate;
        class_1657 self = (class_1657)this;
        if (self instanceof class_3222) {
            class_3222 serverPlayer = (class_3222)self;
            scale = (float)((double)scale * NonTrinketsIntegration.getAccessoryEffects((class_1309)serverPlayer).playerEnergyGainMultiplier());
        }
        if (!Float.isFinite(scale) || scale <= 0.0f) {
            return 120;
        }
        int effective = Math.round(120.0f / scale);
        return Math.max(1, effective);
    }
}

