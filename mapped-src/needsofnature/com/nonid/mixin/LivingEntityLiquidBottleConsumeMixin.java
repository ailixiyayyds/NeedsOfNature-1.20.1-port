/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.component.DataComponentTypes
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.NeedsOfNature;
import com.nonid.potion.NonPotions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityLiquidBottleConsumeMixin {
    @Inject(method={"consumeItem"}, at={@At(value="HEAD")})
    private void non$grantEmergencySnackForEntityLiquidBottle(CallbackInfo ci) {
        boolean entityLiquidBottle;
        LivingEntityLiquidBottleConsumeMixin livingEntityLiquidBottleConsumeMixin = this;
        if (!(livingEntityLiquidBottleConsumeMixin instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity)livingEntityLiquidBottleConsumeMixin;
        ItemStack stack = player.getActiveItem();
        boolean bl = entityLiquidBottle = NonPotions.isLiquidBottle(stack) && NonPotions.getLiquidBottleEntityTypeId(stack) != null;
        if (entityLiquidBottle) {
            NeedsOfNature.grantEmergencySnackAdvancement(player);
            return;
        }
        if (stack.isOf(Items.MILK_BUCKET)) {
            NeedsOfNature.grantUdderlyUnfortunateAdvancementIfPregnant(player);
        }
        if (stack.contains(DataComponentTypes.FOOD) || stack.contains(DataComponentTypes.CONSUMABLE)) {
            NeedsOfNature.resetStableDietStreak(player);
        }
    }
}

