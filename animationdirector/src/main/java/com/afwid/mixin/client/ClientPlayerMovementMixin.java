/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.PlayerInput
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.client.input.Input
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayerEntity.class})
public abstract class ClientPlayerMovementMixin {
    @Inject(method={"tickMovement"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/input/Input;tick(ZF)V", shift=At.Shift.AFTER)})
    private void afw$lockMovementWhileAnimating(CallbackInfo ci) {
        ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
        if (AfwClientAnimationRuntime.isActorPendingOrActive(self.getUuid())) {
            self.sidewaysSpeed = 0.0f;
            self.forwardSpeed = 0.0f;
            self.setJumping(false);
            self.setSprinting(false);
            Input input = self.input;
            if (input != null) {
                input.movementForward = 0.0f;
                input.movementSideways = 0.0f;
                input.pressingForward = false;
                input.pressingBack = false;
                input.pressingLeft = false;
                input.pressingRight = false;
                input.jumping = false;
                input.sneaking = false;
            }
        }
    }
}

