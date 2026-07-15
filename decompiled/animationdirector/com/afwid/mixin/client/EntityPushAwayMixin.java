/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1297.class})
public abstract class EntityPushAwayMixin {
    @Inject(method={"method_5697"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$disablePushAwayDuringAnimation(class_1297 other, CallbackInfo ci) {
        if (other == null) {
            return;
        }
        class_1297 self = (class_1297)this;
        if (AfwClientAnimationRuntime.isActorPendingOrActive(self.method_5667()) || AfwClientAnimationRuntime.isActorPendingOrActive(other.method_5667())) {
            ci.cancel();
        }
    }
}

