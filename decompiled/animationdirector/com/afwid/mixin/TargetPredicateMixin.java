/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_3218
 *  net.minecraft.class_4051
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.class_1309;
import net.minecraft.class_3218;
import net.minecraft.class_4051;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_4051.class})
public abstract class TargetPredicateMixin {
    @Inject(method={"method_18419"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$ignoreAfwActors(class_3218 world, class_1309 tester, class_1309 target, CallbackInfoReturnable<Boolean> cir) {
        if (!AfwServerAnimationController.shouldIgnoreAttackers(world, target)) {
            return;
        }
        cir.setReturnValue((Object)false);
    }
}

