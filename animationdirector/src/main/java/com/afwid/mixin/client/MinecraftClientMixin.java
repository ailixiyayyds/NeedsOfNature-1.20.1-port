/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.screen.ingame.HandledScreen
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={MinecraftClient.class})
public abstract class MinecraftClientMixin {
    @Inject(method={"setScreen"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockHandledScreens(Screen screen, CallbackInfo ci) {
        if (!(screen instanceof HandledScreen)) {
            return;
        }
        MinecraftClient client = (MinecraftClient)(Object)this;
        if (client.player == null) {
            return;
        }
        if (AfwClientAnimationRuntime.isActorPendingOrActive(client.player.getUuid())) {
            ci.cancel();
        }
    }
}

