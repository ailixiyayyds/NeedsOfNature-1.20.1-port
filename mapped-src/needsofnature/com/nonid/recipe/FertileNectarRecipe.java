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
import java.util.List;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.IngredientPlacement;

public final class FertileNectarRecipe
implements CraftingRecipe {
    private static final FoodComponent FERTILE_NECTAR_FOOD = new FoodComponent.Builder().nutrition(7).saturationModifier(0.0f).alwaysEdible().build();
    private final CraftingRecipeCategory category;
    private IngredientPlacement ingredientPlacement;

    public FertileNectarRecipe(CraftingRecipeCategory category) {
        this.category = category;
    }

    public boolean matches(CraftingRecipeInput input, World world) {
        int liquid = 0;
        int honeyBottle = 0;
        int nonEmpty = 0;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
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

    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return FertileNectarRecipe.createPotionStack(NonPotions.FERTILE_NECTAR);
    }

    public CraftingRecipeCategory getCategory() {
        return this.category;
    }

    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forShapeless(List.of(Ingredient.ofItem((ItemConvertible)Items.POTION), Ingredient.ofItem((ItemConvertible)Items.HONEY_BOTTLE)));
        }
        return this.ingredientPlacement;
    }

    public List<RecipeDisplay> getDisplays() {
        SlotDisplay.StackSlotDisplay liquidDisplay = new SlotDisplay.StackSlotDisplay(FertileNectarRecipe.createPotionStack(NonPotions.LIQUID));
        SlotDisplay honeyDisplay = Ingredient.ofItem((ItemConvertible)Items.HONEY_BOTTLE).toDisplay();
        SlotDisplay.StackSlotDisplay resultDisplay = new SlotDisplay.StackSlotDisplay(FertileNectarRecipe.createPotionStack(NonPotions.FERTILE_NECTAR));
        return List.of(new ShapelessCraftingRecipeDisplay(List.of(liquidDisplay, honeyDisplay), (SlotDisplay)resultDisplay, (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }

    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        DefaultedList remainders = DefaultedList.ofSize((int)input.size(), (Object)ItemStack.EMPTY);
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty() || NonPotions.isLiquidBottle(stack)) continue;
            remainders.set(i, (Object)stack.getItem().getRecipeRemainder());
        }
        return remainders;
    }

    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return NonRecipeSerializers.FERTILE_NECTAR_RECIPE;
    }

    private static ItemStack createPotionStack(Potion potion) {
        RegistryEntry potionEntry = Registries.POTION.getEntry((Object)potion);
        ItemStack stack = new ItemStack((ItemConvertible)Items.POTION);
        stack.set(DataComponentTypes.POTION_CONTENTS, (Object)new PotionContentsComponent(potionEntry));
        if (potion == NonPotions.FERTILE_NECTAR) {
            stack.set(DataComponentTypes.FOOD, (Object)FERTILE_NECTAR_FOOD);
            stack.set(DataComponentTypes.MAX_STACK_SIZE, (Object)16);
            NonPotions.applyHoneyBottleDrinkSound(stack);
        }
        return stack;
    }
}

