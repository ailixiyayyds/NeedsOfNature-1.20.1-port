/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.storage.ReadView
 *  net.minecraft.storage.WriteView
 *  net.minecraft.entity.Entity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.mojang.serialization.Codec;
import com.nonid.EnergyHolder;
import java.util.Optional;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class EntityEnergyPersistenceMixin {
    @Inject(method={"writeData"}, at={@At(value="TAIL")})
    private void needsOfNature$writeEnergy(WriteView storage, CallbackInfo ci) {
        EntityEnergyPersistenceMixin entityEnergyPersistenceMixin = this;
        if (entityEnergyPersistenceMixin instanceof EnergyHolder) {
            EnergyHolder holder = (EnergyHolder)((Object)entityEnergyPersistenceMixin);
            storage.putInt("NeedsOfNatureEnergy", holder.getEnergy());
            storage.putFloat("NeedsOfNatureEnergyGainMult", holder.getEnergyGainMultiplier());
            storage.putFloat("NeedsOfNatureEnergyGainDrift", holder.getEnergyGainDrift());
        }
    }

    @Inject(method={"readData"}, at={@At(value="TAIL")})
    private void needsOfNature$readEnergy(ReadView storage, CallbackInfo ci) {
        EntityEnergyPersistenceMixin entityEnergyPersistenceMixin = this;
        if (entityEnergyPersistenceMixin instanceof EnergyHolder) {
            Optional driftOpt;
            Optional multOpt;
            EnergyHolder holder = (EnergyHolder)((Object)entityEnergyPersistenceMixin);
            boolean loaded = false;
            Optional energyOpt = storage.getOptionalInt("NeedsOfNatureEnergy");
            if (energyOpt.isPresent()) {
                holder.setEnergy((Integer)energyOpt.get());
                loaded = true;
            }
            if ((multOpt = storage.read("NeedsOfNatureEnergyGainMult", (Codec)Codec.FLOAT)).isPresent()) {
                holder.setEnergyGainMultiplier(((Float)multOpt.get()).floatValue());
                loaded = true;
            }
            if ((driftOpt = storage.read("NeedsOfNatureEnergyGainDrift", (Codec)Codec.FLOAT)).isPresent()) {
                holder.setEnergyGainDrift(((Float)driftOpt.get()).floatValue());
                loaded = true;
            }
            if (loaded) {
                holder.markEnergyInitialized();
            }
        }
    }
}

