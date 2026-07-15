/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class EntityPushAwayMixin {
    @Inject(method={"pushAwayFrom"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$disablePushAwayDuringAnimation(Entity other, CallbackInfo ci) {
        if (other == null) {
            return;
        }
        Entity self = (Entity)(Object)this;
        if (AfwClientAnimationRuntime.isActorPendingOrActive(self.getUuid()) || AfwClientAnimationRuntime.isActorPendingOrActive(other.getUuid())) {
            ci.cancel();
        }
    }
}

