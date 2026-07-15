/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.registry.Registries
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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityLiquidJumpMixin {
    @Inject(method={"jump"}, at={@At(value="TAIL")})
    private void needsOfNature$applyLiquidJumpSlowdown(CallbackInfo ci) {
        StatusEffectInstance pregnancy;
        LivingEntity self = (LivingEntity)this;
        if (!(self instanceof PlayerEntity)) {
            return;
        }
        if (!(self instanceof LiquidHolder)) {
            return;
        }
        float multiplier = 1.0f;
        StatusEffectInstance effect = self.getStatusEffect(Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.FILLED));
        if (effect != null) {
            int amplifier = effect.getAmplifier();
            float liquidMultiplier = amplifier >= 2 ? NeedsOfNature.getConfig().getFilledStageThreeJumpMult() : (amplifier >= 1 ? NeedsOfNature.getConfig().getFilledStageTwoJumpMult() : NeedsOfNature.getConfig().getFilledStageOneJumpMult());
            liquidMultiplier = LivingEntityLiquidJumpMixin.non$applyFilledAccessoryMultiplier(self, liquidMultiplier);
            multiplier *= liquidMultiplier;
        }
        if ((pregnancy = self.getStatusEffect(Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.PREGNANT))) != null) {
            multiplier *= NeedsOfNature.getPregnancyMovementMultiplier(pregnancy.getDuration());
        }
        if (Math.abs(multiplier - 1.0f) < 1.0E-4f) {
            return;
        }
        Vec3d velocity = self.getVelocity();
        self.setVelocity(velocity.x * (double)multiplier, velocity.y * (double)multiplier, velocity.z * (double)multiplier);
    }

    private static float non$applyFilledAccessoryMultiplier(LivingEntity entity, float multiplier) {
        double effectMultiplier = NonTrinketsIntegration.getActiveTankAccessoryEffects(entity).filledEffectMultiplier();
        if (!Double.isFinite(effectMultiplier)) {
            return multiplier;
        }
        double penalty = 1.0 - (double)multiplier;
        return (float)Math.max(0.0, Math.min(2.0, 1.0 - penalty * effectMultiplier));
    }
}

