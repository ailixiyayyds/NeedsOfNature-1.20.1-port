/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.recipe.display.RecipeDisplay
 *  net.minecraft.recipe.display.ShapelessCraftingRecipeDisplay
 *  net.minecraft.recipe.display.SlotDisplay
 *  net.minecraft.recipe.display.SlotDisplay$ItemSlotDisplay
 *  net.minecraft.recipe.display.SlotDisplay$StackSlotDisplay
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.recipe.Ingredient
 *  net.minecraft.recipe.RecipeSerializer
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.world.World
 *  net.minecraft.util.collection.DefaultedList
 *  net.minecraft.recipe.CraftingRecipe
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.RegistryWrapper$WrapperLookup
 *  net.minecraft.recipe.book.CraftingRecipeCategory
 *  net.minecraft.registry.Registries
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.recipe.input.CraftingRecipeInput
 *  net.minecraft.recipe.IngredientPlacement
 */
package com.nonid.recipe;

import com.nonid.NeedsOfNature;
import com.nonid.potion.NonPotions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

abstract class AbstractEnergyAdjustRecipe
extends SpecialCraftingRecipe {

    protected AbstractEnergyAdjustRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    public boolean matches(RecipeInputInventory input, World world) {
        int liquid = 0;
        int honeycomb = 0;
        int sugar = 0;
        int flowerMix = 0;
        int fungus = 0;
        int nonEmpty = 0;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStack(i);
            if (stack.isEmpty()) continue;
            ++nonEmpty;
            if (NonPotions.isLiquidBottle(stack)) {
                ++liquid;
                continue;
            }
            if (stack.isOf(Items.HONEYCOMB)) {
                ++honeycomb;
                continue;
            }
            if (stack.isOf(Items.SUGAR)) {
                ++sugar;
                continue;
            }
            if (stack.isOf(NeedsOfNature.FLOWER_MIX)) {
                ++flowerMix;
                continue;
            }
            if (this.isRequiredFungus(stack)) {
                ++fungus;
                continue;
            }
            return false;
        }
        return nonEmpty == 5 && liquid == 1 && honeycomb == 1 && sugar == 1 && flowerMix == 1 && fungus == 1;
    }

    public ItemStack craft(RecipeInputInventory input, DynamicRegistryManager registries) {
        return this.getResultStack().copy();
    }

    public boolean fits(int width, int height) {
        return width * height >= 5;
    }

    public ItemStack getOutput(DynamicRegistryManager registries) {
        return this.getResultStack();
    }

    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory input) {
        DefaultedList remainders = DefaultedList.ofSize((int)input.size(), (Object)ItemStack.EMPTY);
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStack(i);
            if (stack.isEmpty()) continue;
            if (NonPotions.isLiquidBottle(stack)) {
                remainders.set(i, (Object)new ItemStack((ItemConvertible)Items.GLASS_BOTTLE));
                continue;
            }
            if (stack.getItem().hasRecipeRemainder()) {
                remainders.set(i, new ItemStack(stack.getItem().getRecipeRemainder()));
            }
        }
        return remainders;
    }

    protected abstract Item getRequiredFungusItem();

    protected abstract boolean isRequiredFungus(ItemStack var1);

    protected abstract ItemStack getResultStack();

    public abstract RecipeSerializer<? extends AbstractEnergyAdjustRecipe> getSerializer();
}

