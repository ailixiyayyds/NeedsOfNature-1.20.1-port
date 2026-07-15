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
import java.util.List;
import java.util.Optional;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapedCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.IngredientPlacement;
import org.jetbrains.annotations.Nullable;

public final class EnergyStabilizerRecipe
implements CraftingRecipe {
    private final CraftingRecipeCategory category;
    private IngredientPlacement ingredientPlacement;

    public EnergyStabilizerRecipe(CraftingRecipeCategory category) {
        this.category = category;
    }

    public boolean matches(CraftingRecipeInput input, World world) {
        return EnergyStabilizerRecipe.findTargetEntity(input) != null;
    }

    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        Identifier target = EnergyStabilizerRecipe.findTargetEntity(input);
        if (target == null) {
            return ItemStack.EMPTY;
        }
        return EnergyStabilizerItem.createStackForEntity(target);
    }

    public CraftingRecipeCategory getCategory() {
        return this.category;
    }

    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return NonRecipeSerializers.ENERGY_STABILIZER_RECIPE;
    }

    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forMultipleSlots(List.of(Optional.of(Ingredient.ofItem((ItemConvertible)Items.POTION)), Optional.of(Ingredient.ofItem((ItemConvertible)Items.WARPED_FUNGUS)), Optional.of(Ingredient.ofItem((ItemConvertible)Items.POTION)), Optional.of(Ingredient.ofItem((ItemConvertible)NeedsOfNature.ENERGY_DIMINISHER)), Optional.of(Ingredient.ofItem((ItemConvertible)Items.GLOW_BERRIES)), Optional.of(Ingredient.ofItem((ItemConvertible)NeedsOfNature.ENERGY_DIMINISHER)), Optional.of(Ingredient.ofItem((ItemConvertible)Items.POTION)), Optional.of(Ingredient.ofItem((ItemConvertible)Items.WARPED_FUNGUS)), Optional.of(Ingredient.ofItem((ItemConvertible)Items.POTION))));
        }
        return this.ingredientPlacement;
    }

    public List<RecipeDisplay> getDisplays() {
        SlotDisplay.StackSlotDisplay liquidDisplay = new SlotDisplay.StackSlotDisplay(EnergyStabilizerRecipe.createLiquidDisplayStack());
        SlotDisplay fungusDisplay = Ingredient.ofItem((ItemConvertible)Items.WARPED_FUNGUS).toDisplay();
        SlotDisplay diminisherDisplay = Ingredient.ofItem((ItemConvertible)NeedsOfNature.ENERGY_DIMINISHER).toDisplay();
        SlotDisplay glowBerryDisplay = Ingredient.ofItem((ItemConvertible)Items.GLOW_BERRIES).toDisplay();
        SlotDisplay.StackSlotDisplay resultDisplay = new SlotDisplay.StackSlotDisplay(EnergyStabilizerItem.createStackForEntity(null));
        return List.of(new ShapedCraftingRecipeDisplay(3, 3, List.of(liquidDisplay, fungusDisplay, liquidDisplay, diminisherDisplay, glowBerryDisplay, diminisherDisplay, liquidDisplay, fungusDisplay, liquidDisplay), (SlotDisplay)resultDisplay, (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }

    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        DefaultedList remainders = DefaultedList.ofSize((int)input.size(), (Object)ItemStack.EMPTY);
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (EnergyStabilizerRecipe.isCorner(i, input.getWidth()) && NonPotions.isLiquidBottle(stack)) {
                remainders.set(i, (Object)new ItemStack((ItemConvertible)Items.GLASS_BOTTLE));
                continue;
            }
            remainders.set(i, (Object)stack.getItem().getRecipeRemainder());
        }
        return remainders;
    }

    @Nullable
    private static Identifier findTargetEntity(CraftingRecipeInput input) {
        if (input.getWidth() != 3 || input.getHeight() != 3) {
            return null;
        }
        int[] corners = new int[]{0, 2, 6, 8};
        Identifier entityTypeId = null;
        for (int corner : corners) {
            ItemStack liquidStack = input.getStackInSlot(corner);
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
        if (!input.getStackInSlot(1).isOf(Items.WARPED_FUNGUS)) {
            return null;
        }
        if (!input.getStackInSlot(7).isOf(Items.WARPED_FUNGUS)) {
            return null;
        }
        if (!input.getStackInSlot(3).isOf(NeedsOfNature.ENERGY_DIMINISHER)) {
            return null;
        }
        if (!input.getStackInSlot(5).isOf(NeedsOfNature.ENERGY_DIMINISHER)) {
            return null;
        }
        if (!input.getStackInSlot(4).isOf(Items.GLOW_BERRIES)) {
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

    private static ItemStack createLiquidDisplayStack() {
        RegistryEntry liquidPotion = Registries.POTION.getEntry((Object)NonPotions.LIQUID);
        ItemStack stack = new ItemStack((ItemConvertible)Items.POTION);
        stack.set(DataComponentTypes.POTION_CONTENTS, (Object)new PotionContentsComponent(liquidPotion));
        return stack;
    }
}

