/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_310.class})
public abstract class MinecraftClientMixin {
    @Inject(method={"method_1507"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockHandledScreens(class_437 screen, CallbackInfo ci) {
        if (!(screen instanceof class_465)) {
            return;
        }
        class_310 client = (class_310)this;
        if (client.field_1724 == null) {
            return;
        }
        if (AfwClientAnimationRuntime.isActorPendingOrActive(client.field_1724.method_5667())) {
            ci.cancel();
        }
    }
}

