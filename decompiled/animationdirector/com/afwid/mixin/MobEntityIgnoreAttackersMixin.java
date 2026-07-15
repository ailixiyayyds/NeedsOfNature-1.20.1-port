/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_3218
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1308.class})
public abstract class MobEntityIgnoreAttackersMixin {
    @Inject(method={"method_6121"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockDirectAttackOnIgnoredActors(class_3218 world, class_1297 target, CallbackInfoReturnable<Boolean> cir) {
        if (!(target instanceof class_1309)) {
            return;
        }
        class_1309 livingTarget = (class_1309)target;
        if (!AfwServerAnimationController.shouldIgnoreAttackers(world, livingTarget)) {
            return;
        }
        cir.setReturnValue((Object)false);
    }
}

