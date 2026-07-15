/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1937
 *  net.minecraft.class_3218
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1297.class})
public abstract class EntityPushAwayMixin {
    @Inject(method={"method_5697"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$disablePushAwayDuringAnimation(class_1297 other, CallbackInfo ci) {
        if (other == null) {
            return;
        }
        class_1297 self = (class_1297)this;
        class_1937 class_19372 = self.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            return;
        }
        class_3218 world = (class_3218)class_19372;
        if (AfwServerAnimationController.isActorPendingOrActive(world, self.method_5667()) || AfwServerAnimationController.isActorPendingOrActive(world, other.method_5667())) {
            ci.cancel();
        }
    }
}

