/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10185
 *  net.minecraft.class_241
 *  net.minecraft.class_744
 *  net.minecraft.class_746
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
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_746.class})
public abstract class ClientPlayerMovementMixin {
    @Inject(method={"method_66282"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$lockMovementWhileAnimating(CallbackInfo ci) {
        class_746 self = (class_746)this;
        if (AfwClientAnimationRuntime.isActorPendingOrActive(self.method_5667())) {
            self.field_6212 = 0.0f;
            self.field_6250 = 0.0f;
            self.method_6100(false);
            self.method_5728(false);
            class_744 input = self.field_3913;
            if (input != null) {
                input.field_54155 = new class_10185(false, false, false, false, false, false, false);
                if (input instanceof InputAccessor) {
                    InputAccessor accessor = (InputAccessor)input;
                    accessor.afw$setMovementVector(class_241.field_1340);
                }
            }
            ci.cancel();
        }
    }
}

