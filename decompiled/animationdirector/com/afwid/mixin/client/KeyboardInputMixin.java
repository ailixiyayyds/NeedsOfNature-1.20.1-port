/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10185
 *  net.minecraft.class_241
 *  net.minecraft.class_310
 *  net.minecraft.class_743
 *  net.minecraft.class_744
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import com.afwid.mixin.client.InputAccessor;
import net.minecraft.class_10185;
import net.minecraft.class_241;
import net.minecraft.class_310;
import net.minecraft.class_743;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_743.class})
public abstract class KeyboardInputMixin {
    @Inject(method={"method_3129"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$lockKeyboardInput(CallbackInfo ci) {
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return;
        }
        if (AfwClientAnimationRuntime.isActorPendingOrActive(client.field_1724.method_5667())) {
            class_744 self = (class_744)this;
            self.field_54155 = new class_10185(false, false, false, false, false, false, false);
            if (self instanceof InputAccessor) {
                InputAccessor accessor = (InputAccessor)self;
                accessor.afw$setMovementVector(class_241.field_1340);
            }
            ci.cancel();
        }
    }
}

