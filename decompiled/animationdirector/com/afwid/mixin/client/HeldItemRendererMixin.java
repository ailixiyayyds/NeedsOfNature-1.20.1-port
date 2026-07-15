/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11659
 *  net.minecraft.class_4587
 *  net.minecraft.class_746
 *  net.minecraft.class_759
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.class_11659;
import net.minecraft.class_4587;
import net.minecraft.class_746;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_759.class})
public abstract class HeldItemRendererMixin {
    @Inject(method={"method_22976"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$hideFirstPersonHandsWhileAnimating(float tickProgress, class_4587 matrices, class_11659 commandQueue, class_746 player, int light, CallbackInfo ci) {
        if (player != null && AfwClientAnimationRuntime.isActorPendingOrActive(player.method_5667())) {
            ci.cancel();
        }
    }
}

