/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityJumpMixin {
    @Inject(method={"jump"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockJumpWhileAnimating(CallbackInfo ci) {
        ClientPlayerEntity player;
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof ClientPlayerEntity && AfwClientAnimationRuntime.isActorPendingOrActive((player = (ClientPlayerEntity)self).getUuid())) {
            ci.cancel();
        }
    }
}

