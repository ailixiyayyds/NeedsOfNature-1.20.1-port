/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.attribute.EntityAttributeModifier
 *  net.minecraft.entity.attribute.EntityAttributeModifier$Operation
 *  net.minecraft.entity.attribute.EntityAttributeInstance
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.entity.attribute.EntityAttributes
 *  net.minecraft.registry.Registries
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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerEntity.class})
public abstract class PlayerLiquidMovementMixin {
    @Unique
    private static final UUID LIQUID_SPEED_MOD = UUID.nameUUIDFromBytes("needsofnature:liquid_speed".getBytes(StandardCharsets.UTF_8));
    @Unique
    private static final UUID PREGNANCY_SPEED_MOD = UUID.nameUUIDFromBytes("needsofnature:pregnancy_speed".getBytes(StandardCharsets.UTF_8));

    @Inject(method={"tickMovement"}, at={@At(value="TAIL")})
    private void needsOfNature$applyLiquidMovementSlowdown(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (!(player instanceof LiquidHolder)) {
            return;
        }
        EntityAttributeInstance attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attribute == null) {
            return;
        }
        StatusEffectInstance effect = player.getStatusEffect(NonStatusEffects.FILLED);
        if (effect == null) {
            attribute.removeModifier(LIQUID_SPEED_MOD);
        } else {
            int amplifier = effect.getAmplifier();
            float multiplier = amplifier >= 2 ? NeedsOfNature.getConfig().getFilledStageThreeSpeedMult() : (amplifier >= 1 ? NeedsOfNature.getConfig().getFilledStageTwoSpeedMult() : NeedsOfNature.getConfig().getFilledStageOneSpeedMult());
            multiplier = PlayerLiquidMovementMixin.non$applyFilledAccessoryMultiplier(player, multiplier);
            double value = (double)multiplier - 1.0;
            attribute.removeModifier(LIQUID_SPEED_MOD);
            attribute.addTemporaryModifier(new EntityAttributeModifier(LIQUID_SPEED_MOD, "NeedsOfNature liquid speed", value, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (player.getEntityWorld().isClient()) {
            return;
        }
        StatusEffectInstance pregnancy = player.getStatusEffect(NonStatusEffects.PREGNANT);
        if (pregnancy == null) {
            attribute.removeModifier(PREGNANCY_SPEED_MOD);
            return;
        }
        float pregnancyMultiplier = NeedsOfNature.getPregnancyMovementMultiplier(pregnancy.getDuration());
        attribute.removeModifier(PREGNANCY_SPEED_MOD);
        attribute.addTemporaryModifier(new EntityAttributeModifier(PREGNANCY_SPEED_MOD, "NeedsOfNature pregnancy speed", (double)pregnancyMultiplier - 1.0, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    @Unique
    private static float non$applyFilledAccessoryMultiplier(PlayerEntity player, float multiplier) {
        double effectMultiplier = NonTrinketsIntegration.getActiveTankAccessoryEffects((LivingEntity)player).filledEffectMultiplier();
        if (!Double.isFinite(effectMultiplier)) {
            return multiplier;
        }
        double penalty = 1.0 - (double)multiplier;
        return (float)Math.max(0.0, Math.min(2.0, 1.0 - penalty * effectMultiplier));
    }
}

