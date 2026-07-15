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

import com.nonid.EnergyHolder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public abstract class EntityEnergyPersistenceMixin {
    @Inject(method={"writeNbt"}, at={@At(value="TAIL")})
    private void needsOfNature$writeEnergy(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if ((Object)this instanceof EnergyHolder holder) {
            nbt.putInt("NeedsOfNatureEnergy", holder.getEnergy());
            nbt.putFloat("NeedsOfNatureEnergyGainMult", holder.getEnergyGainMultiplier());
            nbt.putFloat("NeedsOfNatureEnergyGainDrift", holder.getEnergyGainDrift());
        }
    }

    @Inject(method={"readNbt"}, at={@At(value="TAIL")})
    private void needsOfNature$readEnergy(NbtCompound nbt, CallbackInfo ci) {
        if ((Object)this instanceof EnergyHolder holder) {
            boolean loaded = false;
            if (nbt.contains("NeedsOfNatureEnergy", NbtElement.NUMBER_TYPE)) {
                holder.setEnergy(nbt.getInt("NeedsOfNatureEnergy"));
                loaded = true;
            }
            if (nbt.contains("NeedsOfNatureEnergyGainMult", NbtElement.NUMBER_TYPE)) {
                holder.setEnergyGainMultiplier(nbt.getFloat("NeedsOfNatureEnergyGainMult"));
                loaded = true;
            }
            if (nbt.contains("NeedsOfNatureEnergyGainDrift", NbtElement.NUMBER_TYPE)) {
                holder.setEnergyGainDrift(nbt.getFloat("NeedsOfNatureEnergyGainDrift"));
                loaded = true;
            }
            if (loaded) {
                holder.markEnergyInitialized();
            }
        }
    }
}

