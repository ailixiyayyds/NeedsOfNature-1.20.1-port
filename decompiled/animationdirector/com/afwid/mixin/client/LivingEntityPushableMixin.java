/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1309.class})
public abstract class LivingEntityPushableMixin {
    @Inject(method={"method_5810"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$disablePushDuringAnimation(CallbackInfoReturnable<Boolean> cir) {
        class_1309 self = (class_1309)this;
        if (AfwClientAnimationRuntime.isActorPendingOrActive(self.method_5667())) {
            cir.setReturnValue((Object)false);
        }
    }
}

