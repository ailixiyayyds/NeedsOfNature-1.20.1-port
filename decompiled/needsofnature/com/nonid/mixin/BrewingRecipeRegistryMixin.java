/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1845
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin;

import com.nonid.potion.NonPotions;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1845;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1845.class})
public abstract class BrewingRecipeRegistryMixin {
    @Inject(method={"method_8070"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$blockSplashConversionsForLiquidBottles(class_1799 input, class_1799 ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (!NonPotions.isLiquidBottle(input)) {
            return;
        }
        if (ingredient == null || ingredient.method_7960()) {
            return;
        }
        if (!ingredient.method_31574(class_1802.field_8054) && !ingredient.method_31574(class_1802.field_8613)) {
            return;
        }
        cir.setReturnValue((Object)false);
    }
}

