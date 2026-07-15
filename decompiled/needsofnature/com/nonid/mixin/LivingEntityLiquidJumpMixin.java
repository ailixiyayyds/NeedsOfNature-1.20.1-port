/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1293
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_243
 *  net.minecraft.class_7923
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.LiquidHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonTrinketsIntegration;
import com.nonid.effect.NonStatusEffects;
import net.minecraft.class_1293;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_7923;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1309.class})
public abstract class LivingEntityLiquidJumpMixin {
    @Inject(method={"method_6043"}, at={@At(value="TAIL")})
    private void needsOfNature$applyLiquidJumpSlowdown(CallbackInfo ci) {
        class_1293 pregnancy;
        class_1309 self = (class_1309)this;
        if (!(self instanceof class_1657)) {
            return;
        }
        if (!(self instanceof LiquidHolder)) {
            return;
        }
        float multiplier = 1.0f;
        class_1293 effect = self.method_6112(class_7923.field_41174.method_47983((Object)NonStatusEffects.FILLED));
        if (effect != null) {
            int amplifier = effect.method_5578();
            float liquidMultiplier = amplifier >= 2 ? NeedsOfNature.getConfig().getFilledStageThreeJumpMult() : (amplifier >= 1 ? NeedsOfNature.getConfig().getFilledStageTwoJumpMult() : NeedsOfNature.getConfig().getFilledStageOneJumpMult());
            liquidMultiplier = LivingEntityLiquidJumpMixin.non$applyFilledAccessoryMultiplier(self, liquidMultiplier);
            multiplier *= liquidMultiplier;
        }
        if ((pregnancy = self.method_6112(class_7923.field_41174.method_47983((Object)NonStatusEffects.PREGNANT))) != null) {
            multiplier *= NeedsOfNature.getPregnancyMovementMultiplier(pregnancy.method_5584());
        }
        if (Math.abs(multiplier - 1.0f) < 1.0E-4f) {
            return;
        }
        class_243 velocity = self.method_18798();
        self.method_18800(velocity.field_1352 * (double)multiplier, velocity.field_1351 * (double)multiplier, velocity.field_1350 * (double)multiplier);
    }

    private static float non$applyFilledAccessoryMultiplier(class_1309 entity, float multiplier) {
        double effectMultiplier = NonTrinketsIntegration.getActiveTankAccessoryEffects(entity).filledEffectMultiplier();
        if (!Double.isFinite(effectMultiplier)) {
            return multiplier;
        }
        double penalty = 1.0 - (double)multiplier;
        return (float)Math.max(0.0, Math.min(2.0, 1.0 - penalty * effectMultiplier));
    }
}

