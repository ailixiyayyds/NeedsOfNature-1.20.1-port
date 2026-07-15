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
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class EntityGenderPersistenceMixin {
    @Inject(method={"writeData"}, at={@At(value="TAIL")})
    private void needsOfNature$writeGender(WriteView storage, CallbackInfo ci) {
        Object holder;
        EntityGenderPersistenceMixin entityGenderPersistenceMixin = this;
        if (entityGenderPersistenceMixin instanceof GenderHolder) {
            holder = (GenderHolder)((Object)entityGenderPersistenceMixin);
            storage.putInt("NeedsOfNatureGender", holder.getGenderMask());
        }
        if ((entityGenderPersistenceMixin = this) instanceof DestroyedSkinHolder) {
            holder = (DestroyedSkinHolder)((Object)entityGenderPersistenceMixin);
            storage.putInt("NeedsOfNatureDestroyedSkinStage", holder.getDestroyedSkinStage());
            storage.putInt("NeedsOfNatureDestroyedSkinDamage", holder.getDestroyedSkinDamage());
        }
        if ((entityGenderPersistenceMixin = this) instanceof MessHolder) {
            holder = (MessHolder)((Object)entityGenderPersistenceMixin);
            storage.putInt("NeedsOfNatureMessV", holder.getVMess());
            storage.putInt("NeedsOfNatureMessA", holder.getAMess());
            storage.putInt("NeedsOfNatureMessM", holder.getMMess());
        }
    }

    @Inject(method={"readData"}, at={@At(value="TAIL")})
    private void needsOfNature$readGender(ReadView storage, CallbackInfo ci) {
        Object holder;
        EntityGenderPersistenceMixin entityGenderPersistenceMixin = this;
        if (entityGenderPersistenceMixin instanceof GenderHolder) {
            holder = (GenderHolder)((Object)entityGenderPersistenceMixin);
            storage.getOptionalInt("NeedsOfNatureGender").ifPresent(((GenderHolder)holder)::setGenderMask);
        }
        if ((entityGenderPersistenceMixin = this) instanceof DestroyedSkinHolder) {
            holder = (DestroyedSkinHolder)((Object)entityGenderPersistenceMixin);
            storage.getOptionalInt("NeedsOfNatureDestroyedSkinStage").ifPresent(((DestroyedSkinHolder)holder)::setDestroyedSkinStage);
            storage.getOptionalInt("NeedsOfNatureDestroyedSkinDamage").ifPresent(((DestroyedSkinHolder)holder)::setDestroyedSkinDamage);
        }
        if ((entityGenderPersistenceMixin = this) instanceof MessHolder) {
            holder = (MessHolder)((Object)entityGenderPersistenceMixin);
            storage.getOptionalInt("NeedsOfNatureMessV").ifPresent(((MessHolder)holder)::setVMess);
            storage.getOptionalInt("NeedsOfNatureMessA").ifPresent(((MessHolder)holder)::setAMess);
            storage.getOptionalInt("NeedsOfNatureMessM").ifPresent(((MessHolder)holder)::setMMess);
        }
    }
}

