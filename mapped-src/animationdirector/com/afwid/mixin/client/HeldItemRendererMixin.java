/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.client.render.item.HeldItemRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={HeldItemRenderer.class})
public abstract class HeldItemRendererMixin {
    @Inject(method={"renderItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$hideFirstPersonHandsWhileAnimating(float tickProgress, MatrixStack matrices, OrderedRenderCommandQueue commandQueue, ClientPlayerEntity player, int light, CallbackInfo ci) {
        if (player != null && AfwClientAnimationRuntime.isActorPendingOrActive(player.getUuid())) {
            ci.cancel();
        }
    }
}

