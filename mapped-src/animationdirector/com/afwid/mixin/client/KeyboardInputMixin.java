/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.PlayerInput
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.input.KeyboardInput
 *  net.minecraft.client.input.Input
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.input.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={KeyboardInput.class})
public abstract class KeyboardInputMixin {
    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$lockKeyboardInput(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return;
        }
        if (AfwClientAnimationRuntime.isActorPendingOrActive(client.player.getUuid())) {
            Input self = (Input)this;
            self.playerInput = new PlayerInput(false, false, false, false, false, false, false);
            if (self instanceof InputAccessor) {
                InputAccessor accessor = (InputAccessor)self;
                accessor.afw$setMovementVector(Vec2f.ZERO);
            }
            ci.cancel();
        }
    }
}

