/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityPushableMixin {
    @Inject(method={"isPushable"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$disablePushDuringAnimation(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)this;
        if (AfwClientAnimationRuntime.isActorPendingOrActive(self.getUuid())) {
            cir.setReturnValue((Object)false);
        }
    }
}

