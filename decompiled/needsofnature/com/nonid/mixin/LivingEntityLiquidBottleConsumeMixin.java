/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_3222
 *  net.minecraft.class_9334
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.NeedsOfNature;
import com.nonid.potion.NonPotions;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_3222;
import net.minecraft.class_9334;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1309.class})
public abstract class LivingEntityLiquidBottleConsumeMixin {
    @Inject(method={"method_6040"}, at={@At(value="HEAD")})
    private void non$grantEmergencySnackForEntityLiquidBottle(CallbackInfo ci) {
        boolean entityLiquidBottle;
        LivingEntityLiquidBottleConsumeMixin livingEntityLiquidBottleConsumeMixin = this;
        if (!(livingEntityLiquidBottleConsumeMixin instanceof class_3222)) {
            return;
        }
        class_3222 player = (class_3222)livingEntityLiquidBottleConsumeMixin;
        class_1799 stack = player.method_6030();
        boolean bl = entityLiquidBottle = NonPotions.isLiquidBottle(stack) && NonPotions.getLiquidBottleEntityTypeId(stack) != null;
        if (entityLiquidBottle) {
            NeedsOfNature.grantEmergencySnackAdvancement(player);
            return;
        }
        if (stack.method_31574(class_1802.field_8103)) {
            NeedsOfNature.grantUdderlyUnfortunateAdvancementIfPregnant(player);
        }
        if (stack.method_57826(class_9334.field_50075) || stack.method_57826(class_9334.field_53964)) {
            NeedsOfNature.resetStableDietStreak(player);
        }
    }
}

