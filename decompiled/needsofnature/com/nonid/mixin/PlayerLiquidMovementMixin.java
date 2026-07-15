/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1293
 *  net.minecraft.class_1309
 *  net.minecraft.class_1322
 *  net.minecraft.class_1322$class_1323
 *  net.minecraft.class_1324
 *  net.minecraft.class_1657
 *  net.minecraft.class_2960
 *  net.minecraft.class_5134
 *  net.minecraft.class_7923
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
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
import net.minecraft.class_1322;
import net.minecraft.class_1324;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import net.minecraft.class_5134;
import net.minecraft.class_7923;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1657.class})
public abstract class PlayerLiquidMovementMixin {
    @Unique
    private static final class_2960 LIQUID_SPEED_MOD = NeedsOfNature.id("liquid_speed");
    @Unique
    private static final class_2960 PREGNANCY_SPEED_MOD = NeedsOfNature.id("pregnancy_speed");

    @Inject(method={"method_6007"}, at={@At(value="TAIL")})
    private void needsOfNature$applyLiquidMovementSlowdown(CallbackInfo ci) {
        class_1657 player = (class_1657)this;
        if (!(player instanceof LiquidHolder)) {
            return;
        }
        class_1324 attribute = player.method_5996(class_5134.field_23719);
        if (attribute == null) {
            return;
        }
        class_1293 effect = player.method_6112(class_7923.field_41174.method_47983((Object)NonStatusEffects.FILLED));
        if (effect == null) {
            attribute.method_6200(LIQUID_SPEED_MOD);
        } else {
            int amplifier = effect.method_5578();
            float multiplier = amplifier >= 2 ? NeedsOfNature.getConfig().getFilledStageThreeSpeedMult() : (amplifier >= 1 ? NeedsOfNature.getConfig().getFilledStageTwoSpeedMult() : NeedsOfNature.getConfig().getFilledStageOneSpeedMult());
            multiplier = PlayerLiquidMovementMixin.non$applyFilledAccessoryMultiplier(player, multiplier);
            double value = (double)multiplier - 1.0;
            attribute.method_55696(new class_1322(LIQUID_SPEED_MOD, value, class_1322.class_1323.field_6331));
        }
        if (player.method_73183().method_8608()) {
            return;
        }
        class_1293 pregnancy = player.method_6112(class_7923.field_41174.method_47983((Object)NonStatusEffects.PREGNANT));
        if (pregnancy == null) {
            attribute.method_6200(PREGNANCY_SPEED_MOD);
            return;
        }
        float pregnancyMultiplier = NeedsOfNature.getPregnancyMovementMultiplier(pregnancy.method_5584());
        attribute.method_55696(new class_1322(PREGNANCY_SPEED_MOD, (double)pregnancyMultiplier - 1.0, class_1322.class_1323.field_6331));
    }

    @Unique
    private static float non$applyFilledAccessoryMultiplier(class_1657 player, float multiplier) {
        double effectMultiplier = NonTrinketsIntegration.getActiveTankAccessoryEffects((class_1309)player).filledEffectMultiplier();
        if (!Double.isFinite(effectMultiplier)) {
            return multiplier;
        }
        double penalty = 1.0 - (double)multiplier;
        return (float)Math.max(0.0, Math.min(2.0, 1.0 - penalty * effectMultiplier));
    }
}

