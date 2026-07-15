/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.world.World
 *  net.minecraft.entity.data.TrackedData
 *  net.minecraft.entity.data.TrackedDataHandler
 *  net.minecraft.entity.data.TrackedDataHandlerRegistry
 *  net.minecraft.entity.data.DataTracker
 *  net.minecraft.entity.data.DataTracker$Builder
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerEntity.class})
public abstract class PlayerEntityEnergyMixin
implements EnergyHolder {
    @Unique
    private static final TrackedData<Integer> ENERGY = DataTracker.registerData(PlayerEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.INTEGER);
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

    @Inject(method={"initDataTracker"}, at={@At(value="TAIL")})
    private void needsOfNature$initEnergyTracker(CallbackInfo ci) {
        ((PlayerEntity)(Object)this).getDataTracker().startTracking(ENERGY, 0);
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void needsOfNature$tickEnergy(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        if (self.getEntityWorld().isClient()) {
            return;
        }
        World class_19372 = self.getEntityWorld();
        if (!(class_19372 instanceof ServerWorld)) {
            return;
        }
        ServerWorld serverWorld = (ServerWorld)class_19372;
        this.ensureEnergyInitialized(serverWorld);
        if (self instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)self;
            NonTrinketsIntegration.syncAccessoryStateIfChanged(serverPlayer);
        }
        int energy = this.getEnergy();
        energy = this.non$applyWaterEnergyDecay(self, energy);
        int ticksPerGain = this.non$getTicksPerGain();
        if (energy >= 200) {
            this.non$energyTickCounter = 0;
            if (self instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity)self;
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
        if (self instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)self;
            NeedsOfNature.updateEnergizedEffect(serverPlayer, this);
            NeedsOfNature.tickPlayerEnergyAura(serverWorld, serverPlayer);
        }
    }

    @Unique
    private int non$applyWaterEnergyDecay(PlayerEntity self, int energy) {
        if (!self.isTouchingWater() || energy <= 0) {
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
        PlayerEntity self = (PlayerEntity)(Object)this;
        return (Integer)self.getDataTracker().get(ENERGY);
    }

    @Override
    public int getMaxEnergy() {
        return 200;
    }

    @Override
    public void setEnergy(int value) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        self.getDataTracker().set(ENERGY, Math.min(200, Math.max(0, value)));
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
    public void ensureEnergyInitialized(ServerWorld world) {
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
        PlayerEntity self = (PlayerEntity)(Object)this;
        if (self instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)self;
            scale = (float)((double)scale * NonTrinketsIntegration.getAccessoryEffects((LivingEntity)serverPlayer).playerEnergyGainMultiplier());
        }
        if (!Float.isFinite(scale) || scale <= 0.0f) {
            return 120;
        }
        int effective = Math.round(120.0f / scale);
        return Math.max(1, effective);
    }
}

