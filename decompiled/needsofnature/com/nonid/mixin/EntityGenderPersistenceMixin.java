/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11368
 *  net.minecraft.class_11372
 *  net.minecraft.class_1297
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.DestroyedSkinHolder;
import com.nonid.GenderHolder;
import com.nonid.MessHolder;
import net.minecraft.class_11368;
import net.minecraft.class_11372;
import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1297.class})
public abstract class EntityGenderPersistenceMixin {
    @Inject(method={"method_5647"}, at={@At(value="TAIL")})
    private void needsOfNature$writeGender(class_11372 storage, CallbackInfo ci) {
        Object holder;
        EntityGenderPersistenceMixin entityGenderPersistenceMixin = this;
        if (entityGenderPersistenceMixin instanceof GenderHolder) {
            holder = (GenderHolder)((Object)entityGenderPersistenceMixin);
            storage.method_71465("NeedsOfNatureGender", holder.getGenderMask());
        }
        if ((entityGenderPersistenceMixin = this) instanceof DestroyedSkinHolder) {
            holder = (DestroyedSkinHolder)((Object)entityGenderPersistenceMixin);
            storage.method_71465("NeedsOfNatureDestroyedSkinStage", holder.getDestroyedSkinStage());
            storage.method_71465("NeedsOfNatureDestroyedSkinDamage", holder.getDestroyedSkinDamage());
        }
        if ((entityGenderPersistenceMixin = this) instanceof MessHolder) {
            holder = (MessHolder)((Object)entityGenderPersistenceMixin);
            storage.method_71465("NeedsOfNatureMessV", holder.getVMess());
            storage.method_71465("NeedsOfNatureMessA", holder.getAMess());
            storage.method_71465("NeedsOfNatureMessM", holder.getMMess());
        }
    }

    @Inject(method={"method_5651"}, at={@At(value="TAIL")})
    private void needsOfNature$readGender(class_11368 storage, CallbackInfo ci) {
        Object holder;
        EntityGenderPersistenceMixin entityGenderPersistenceMixin = this;
        if (entityGenderPersistenceMixin instanceof GenderHolder) {
            holder = (GenderHolder)((Object)entityGenderPersistenceMixin);
            storage.method_71439("NeedsOfNatureGender").ifPresent(((GenderHolder)holder)::setGenderMask);
        }
        if ((entityGenderPersistenceMixin = this) instanceof DestroyedSkinHolder) {
            holder = (DestroyedSkinHolder)((Object)entityGenderPersistenceMixin);
            storage.method_71439("NeedsOfNatureDestroyedSkinStage").ifPresent(((DestroyedSkinHolder)holder)::setDestroyedSkinStage);
            storage.method_71439("NeedsOfNatureDestroyedSkinDamage").ifPresent(((DestroyedSkinHolder)holder)::setDestroyedSkinDamage);
        }
        if ((entityGenderPersistenceMixin = this) instanceof MessHolder) {
            holder = (MessHolder)((Object)entityGenderPersistenceMixin);
            storage.method_71439("NeedsOfNatureMessV").ifPresent(((MessHolder)holder)::setVMess);
            storage.method_71439("NeedsOfNatureMessA").ifPresent(((MessHolder)holder)::setAMess);
            storage.method_71439("NeedsOfNatureMessM").ifPresent(((MessHolder)holder)::setMMess);
        }
    }
}

