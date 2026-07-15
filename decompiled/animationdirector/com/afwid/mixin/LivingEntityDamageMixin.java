/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1282
 *  net.minecraft.class_1309
 *  net.minecraft.class_3218
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.class_1282;
import net.minecraft.class_1309;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1309.class})
public abstract class LivingEntityDamageMixin {
    @Inject(method={"method_64397"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$handleDamage(class_3218 world, class_1282 source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!AfwServerAnimationController.handleActorDamage(world, (class_1309)this, source)) {
            cir.setReturnValue((Object)false);
        }
    }
}

