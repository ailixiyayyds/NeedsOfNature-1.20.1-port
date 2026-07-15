/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.recipe.display.RecipeDisplay
 *  net.minecraft.recipe.display.ShapelessCraftingRecipeDisplay
 *  net.minecraft.recipe.display.SlotDisplay
 *  net.minecraft.recipe.display.SlotDisplay$ItemSlotDisplay
 *  net.minecraft.recipe.display.SlotDisplay$StackSlotDisplay
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.potion.Potion
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.recipe.Ingredient
 *  net.minecraft.recipe.RecipeSerializer
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.world.World
 *  net.minecraft.util.collection.DefaultedList
 *  net.minecraft.recipe.CraftingRecipe
 *  net.minecraft.component.type.FoodComponent
 *  net.minecraft.component.type.FoodComponent$Builder
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.RegistryWrapper$WrapperLookup
 *  net.minecraft.recipe.book.CraftingRecipeCategory
 *  net.minecraft.registry.Registries
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.recipe.input.CraftingRecipeInput
 *  net.minecraft.recipe.IngredientPlacement
 */
package com.nonid.recipe;

import com.nonid.potion.NonPotions;
import com.nonid.recipe.NonRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

public final class FertileNectarRecipe
extends SpecialCraftingRecipe {

    public FertileNectarRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    public boolean matches(RecipeInputInventory input, World world) {
        int liquid = 0;
        int honeyBottle = 0;
        int nonEmpty = 0;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStack(i);
            if (stack.isEmpty()) continue;
            ++nonEmpty;
            if (NonPotions.isLiquidBottle(stack)) {
                ++liquid;
                continue;
            }
            if (stack.isOf(Items.HONEY_BOTTLE)) {
                ++honeyBottle;
                continue;
            }
            return false;
        }
        return nonEmpty == 2 && liquid == 1 && honeyBottle == 1;
    }

    public ItemStack craft(RecipeInputInventory input, DynamicRegistryManager registries) {
        return FertileNectarRecipe.createPotionStack(NonPotions.FERTILE_NECTAR);
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public ItemStack getOutput(DynamicRegistryManager registries) {
        return FertileNectarRecipe.createPotionStack(NonPotions.FERTILE_NECTAR);
    }

    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory input) {
        DefaultedList remainders = DefaultedList.ofSize((int)input.size(), (Object)ItemStack.EMPTY);
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStack(i);
            if (stack.isEmpty() || NonPotions.isLiquidBottle(stack)) continue;
            if (stack.getItem().hasRecipeRemainder()) {
                remainders.set(i, new ItemStack(stack.getItem().getRecipeRemainder()));
            }
        }
        return remainders;
    }

    public RecipeSerializer<?> getSerializer() {
        return NonRecipeSerializers.FERTILE_NECTAR_RECIPE;
    }

    private static ItemStack createPotionStack(Potion potion) {
        ItemStack stack = new ItemStack((ItemConvertible)Items.POTION);
        return PotionUtil.setPotion(stack, potion);
    }
}

