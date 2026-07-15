/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_1937
 *  net.minecraft.class_3218
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1309.class})
public abstract class LivingEntityPushableMixin {
    @Inject(method={"method_5810"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$disablePushDuringAnimation(CallbackInfoReturnable<Boolean> cir) {
        class_1309 self = (class_1309)this;
        class_1937 class_19372 = self.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            return;
        }
        class_3218 world = (class_3218)class_19372;
        if (AfwServerAnimationController.isActorPendingOrActive(world, self.method_5667())) {
            cir.setReturnValue((Object)false);
        }
    }
}

