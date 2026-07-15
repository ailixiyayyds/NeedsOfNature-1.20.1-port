/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.recipe.display.RecipeDisplay
 *  net.minecraft.recipe.display.ShapedCraftingRecipeDisplay
 *  net.minecraft.recipe.display.SlotDisplay
 *  net.minecraft.recipe.display.SlotDisplay$ItemSlotDisplay
 *  net.minecraft.recipe.display.SlotDisplay$StackSlotDisplay
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.recipe.Ingredient
 *  net.minecraft.recipe.RecipeSerializer
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.world.World
 *  net.minecraft.util.collection.DefaultedList
 *  net.minecraft.util.Identifier
 *  net.minecraft.recipe.CraftingRecipe
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.RegistryWrapper$WrapperLookup
 *  net.minecraft.recipe.book.CraftingRecipeCategory
 *  net.minecraft.registry.Registries
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.recipe.input.CraftingRecipeInput
 *  net.minecraft.recipe.IngredientPlacement
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.recipe;

import com.nonid.NeedsOfNature;
import com.nonid.item.EnergyStabilizerItem;
import com.nonid.potion.NonPotions;
import com.nonid.recipe.NonRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import org.jetbrains.annotations.Nullable;

public final class EnergyStabilizerRecipe
extends SpecialCraftingRecipe {

    public EnergyStabilizerRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    public boolean matches(RecipeInputInventory input, World world) {
        return EnergyStabilizerRecipe.findTargetEntity(input) != null;
    }

    public ItemStack craft(RecipeInputInventory input, DynamicRegistryManager registries) {
        Identifier target = EnergyStabilizerRecipe.findTargetEntity(input);
        if (target == null) {
            return ItemStack.EMPTY;
        }
        return EnergyStabilizerItem.createStackForEntity(target);
    }

    public boolean fits(int width, int height) {
        return width == 3 && height == 3;
    }

    public RecipeSerializer<?> getSerializer() {
        return NonRecipeSerializers.ENERGY_STABILIZER_RECIPE;
    }

    public ItemStack getOutput(DynamicRegistryManager registries) {
        return EnergyStabilizerItem.createStackForEntity(null);
    }

    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory input) {
        DefaultedList remainders = DefaultedList.ofSize((int)input.size(), (Object)ItemStack.EMPTY);
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStack(i);
            if (stack.isEmpty()) continue;
            if (EnergyStabilizerRecipe.isCorner(i, input.getWidth()) && NonPotions.isLiquidBottle(stack)) {
                remainders.set(i, (Object)new ItemStack((ItemConvertible)Items.GLASS_BOTTLE));
                continue;
            }
            if (stack.getItem().hasRecipeRemainder()) {
                remainders.set(i, new ItemStack(stack.getItem().getRecipeRemainder()));
            }
        }
        return remainders;
    }

    @Nullable
    private static Identifier findTargetEntity(RecipeInputInventory input) {
        if (input.getWidth() != 3 || input.getHeight() != 3) {
            return null;
        }
        int[] corners = new int[]{0, 2, 6, 8};
        Identifier entityTypeId = null;
        for (int corner : corners) {
            ItemStack liquidStack = input.getStack(corner);
            Identifier current = NonPotions.getLiquidBottleEntityTypeId(liquidStack);
            if (current == null) {
                return null;
            }
            if (entityTypeId == null) {
                entityTypeId = current;
                continue;
            }
            if (entityTypeId.equals((Object)current)) continue;
            return null;
        }
        if (!input.getStack(1).isOf(Items.WARPED_FUNGUS)) {
            return null;
        }
        if (!input.getStack(7).isOf(Items.WARPED_FUNGUS)) {
            return null;
        }
        if (!input.getStack(3).isOf(NeedsOfNature.ENERGY_DIMINISHER)) {
            return null;
        }
        if (!input.getStack(5).isOf(NeedsOfNature.ENERGY_DIMINISHER)) {
            return null;
        }
        if (!input.getStack(4).isOf(Items.GLOW_BERRIES)) {
            return null;
        }
        return entityTypeId;
    }

    private static boolean isCorner(int slot, int width) {
        if (width != 3) {
            return false;
        }
        return slot == 0 || slot == 2 || slot == 6 || slot == 8;
    }

}

