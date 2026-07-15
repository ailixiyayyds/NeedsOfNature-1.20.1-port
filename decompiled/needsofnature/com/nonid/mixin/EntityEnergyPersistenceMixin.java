/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.class_11368
 *  net.minecraft.class_11372
 *  net.minecraft.class_1297
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.mojang.serialization.Codec;
import com.nonid.EnergyHolder;
import java.util.Optional;
import net.minecraft.class_11368;
import net.minecraft.class_11372;
import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1297.class})
public abstract class EntityEnergyPersistenceMixin {
    @Inject(method={"method_5647"}, at={@At(value="TAIL")})
    private void needsOfNature$writeEnergy(class_11372 storage, CallbackInfo ci) {
        EntityEnergyPersistenceMixin entityEnergyPersistenceMixin = this;
        if (entityEnergyPersistenceMixin instanceof EnergyHolder) {
            EnergyHolder holder = (EnergyHolder)((Object)entityEnergyPersistenceMixin);
            storage.method_71465("NeedsOfNatureEnergy", holder.getEnergy());
            storage.method_71464("NeedsOfNatureEnergyGainMult", holder.getEnergyGainMultiplier());
            storage.method_71464("NeedsOfNatureEnergyGainDrift", holder.getEnergyGainDrift());
        }
    }

    @Inject(method={"method_5651"}, at={@At(value="TAIL")})
    private void needsOfNature$readEnergy(class_11368 storage, CallbackInfo ci) {
        EntityEnergyPersistenceMixin entityEnergyPersistenceMixin = this;
        if (entityEnergyPersistenceMixin instanceof EnergyHolder) {
            Optional driftOpt;
            Optional multOpt;
            EnergyHolder holder = (EnergyHolder)((Object)entityEnergyPersistenceMixin);
            boolean loaded = false;
            Optional energyOpt = storage.method_71439("NeedsOfNatureEnergy");
            if (energyOpt.isPresent()) {
                holder.setEnergy((Integer)energyOpt.get());
                loaded = true;
            }
            if ((multOpt = storage.method_71426("NeedsOfNatureEnergyGainMult", (Codec)Codec.FLOAT)).isPresent()) {
                holder.setEnergyGainMultiplier(((Float)multOpt.get()).floatValue());
                loaded = true;
            }
            if ((driftOpt = storage.method_71426("NeedsOfNatureEnergyGainDrift", (Codec)Codec.FLOAT)).isPresent()) {
                holder.setEnergyGainDrift(((Float)driftOpt.get()).floatValue());
                loaded = true;
            }
            if (loaded) {
                holder.markEnergyInitialized();
            }
        }
    }
}

