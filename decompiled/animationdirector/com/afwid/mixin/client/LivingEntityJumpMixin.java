/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.class_1309;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1309.class})
public abstract class LivingEntityJumpMixin {
    @Inject(method={"method_6043"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockJumpWhileAnimating(CallbackInfo ci) {
        class_746 player;
        class_1309 self = (class_1309)this;
        if (self instanceof class_746 && AfwClientAnimationRuntime.isActorPendingOrActive((player = (class_746)self).method_5667())) {
            ci.cancel();
        }
    }
}

