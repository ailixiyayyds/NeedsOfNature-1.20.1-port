/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.recipe.BrewingRecipeRegistry
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin;

import com.nonid.potion.NonPotions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BrewingRecipeRegistry.class})
public abstract class BrewingRecipeRegistryMixin {
    @Inject(method={"hasItemRecipe"}, at={@At(value="HEAD")}, cancellable=true)
    private static void non$blockSplashConversionsForLiquidBottles(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (!NonPotions.isLiquidBottle(input)) {
            return;
        }
        if (ingredient == null || ingredient.isEmpty()) {
            return;
        }
        if (!ingredient.isOf(Items.GUNPOWDER) && !ingredient.isOf(Items.DRAGON_BREATH)) {
            return;
        }
        cir.setReturnValue(false);
    }
}

