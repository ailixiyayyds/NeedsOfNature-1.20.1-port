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
import net.minecraft.util.Identifier;

public final class EnergyDiminisherRecipe
extends AbstractEnergyAdjustRecipe {
    public EnergyDiminisherRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    protected boolean isRequiredFungus(ItemStack stack) {
        return stack.isOf(Items.WARPED_FUNGUS);
    }

    @Override
    protected Item getRequiredFungusItem() {
        return Items.WARPED_FUNGUS;
    }

    @Override
    protected ItemStack getResultStack() {
        return new ItemStack((ItemConvertible)NeedsOfNature.ENERGY_DIMINISHER);
    }

    @Override
    public RecipeSerializer<? extends AbstractEnergyAdjustRecipe> getSerializer() {
        return NonRecipeSerializers.ENERGY_DIMINISHER_RECIPE;
    }
}

