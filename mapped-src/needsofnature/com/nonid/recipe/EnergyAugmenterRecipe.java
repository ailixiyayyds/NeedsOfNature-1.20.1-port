/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.recipe.RecipeSerializer
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.recipe.book.CraftingRecipeCategory
 */
package com.nonid.recipe;

import com.nonid.NeedsOfNature;
import com.nonid.recipe.AbstractEnergyAdjustRecipe;
import com.nonid.recipe.NonRecipeSerializers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.CraftingRecipeCategory;

public final class EnergyAugmenterRecipe
extends AbstractEnergyAdjustRecipe {
    public EnergyAugmenterRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    protected boolean isRequiredFungus(ItemStack stack) {
        return stack.isOf(Items.CRIMSON_FUNGUS);
    }

    @Override
    protected Item getRequiredFungusItem() {
        return Items.CRIMSON_FUNGUS;
    }

    @Override
    protected ItemStack getResultStack() {
        return new ItemStack((ItemConvertible)NeedsOfNature.ENERGY_AUGMENTER);
    }

    @Override
    public RecipeSerializer<? extends AbstractEnergyAdjustRecipe> getSerializer() {
        return NonRecipeSerializers.ENERGY_AUGMENTER_RECIPE;
    }
}

