/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.storage.ReadView
 *  net.minecraft.storage.WriteView
 *  net.minecraft.entity.Entity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.DestroyedSkinHolder;
import com.nonid.GenderHolder;
import com.nonid.MessHolder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public abstract class EntityGenderPersistenceMixin {
    @Inject(method={"writeNbt"}, at={@At(value="TAIL")})
    private void needsOfNature$writeGender(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if ((Object)this instanceof GenderHolder holder) {
            nbt.putInt("NeedsOfNatureGender", holder.getGenderMask());
        }
        if ((Object)this instanceof DestroyedSkinHolder holder) {
            nbt.putInt("NeedsOfNatureDestroyedSkinStage", holder.getDestroyedSkinStage());
            nbt.putInt("NeedsOfNatureDestroyedSkinDamage", holder.getDestroyedSkinDamage());
        }
        if ((Object)this instanceof MessHolder holder) {
            nbt.putInt("NeedsOfNatureMessV", holder.getVMess());
            nbt.putInt("NeedsOfNatureMessA", holder.getAMess());
            nbt.putInt("NeedsOfNatureMessM", holder.getMMess());
        }
    }

    @Inject(method={"readNbt"}, at={@At(value="TAIL")})
    private void needsOfNature$readGender(NbtCompound nbt, CallbackInfo ci) {
        if ((Object)this instanceof GenderHolder holder && nbt.contains("NeedsOfNatureGender", NbtElement.NUMBER_TYPE)) {
            holder.setGenderMask(nbt.getInt("NeedsOfNatureGender"));
        }
        if ((Object)this instanceof DestroyedSkinHolder holder) {
            if (nbt.contains("NeedsOfNatureDestroyedSkinStage", NbtElement.NUMBER_TYPE)) holder.setDestroyedSkinStage(nbt.getInt("NeedsOfNatureDestroyedSkinStage"));
            if (nbt.contains("NeedsOfNatureDestroyedSkinDamage", NbtElement.NUMBER_TYPE)) holder.setDestroyedSkinDamage(nbt.getInt("NeedsOfNatureDestroyedSkinDamage"));
        }
        if ((Object)this instanceof MessHolder holder) {
            if (nbt.contains("NeedsOfNatureMessV", NbtElement.NUMBER_TYPE)) holder.setVMess(nbt.getInt("NeedsOfNatureMessV"));
            if (nbt.contains("NeedsOfNatureMessA", NbtElement.NUMBER_TYPE)) holder.setAMess(nbt.getInt("NeedsOfNatureMessA"));
            if (nbt.contains("NeedsOfNatureMessM", NbtElement.NUMBER_TYPE)) holder.setMMess(nbt.getInt("NeedsOfNatureMessM"));
        }
    }
}

