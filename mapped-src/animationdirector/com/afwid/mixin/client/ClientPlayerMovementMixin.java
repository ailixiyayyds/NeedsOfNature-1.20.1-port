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
import com.afwid.mixin.client.InputAccessor;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayerEntity.class})
public abstract class ClientPlayerMovementMixin {
    @Inject(method={"tickMovementInput"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$lockMovementWhileAnimating(CallbackInfo ci) {
        ClientPlayerEntity self = (ClientPlayerEntity)this;
        if (AfwClientAnimationRuntime.isActorPendingOrActive(self.getUuid())) {
            self.sidewaysSpeed = 0.0f;
            self.forwardSpeed = 0.0f;
            self.setJumping(false);
            self.setSprinting(false);
            Input input = self.input;
            if (input != null) {
                input.playerInput = new PlayerInput(false, false, false, false, false, false, false);
                if (input instanceof InputAccessor) {
                    InputAccessor accessor = (InputAccessor)input;
                    accessor.afw$setMovementVector(Vec2f.ZERO);
                }
            }
            ci.cancel();
        }
    }
}

