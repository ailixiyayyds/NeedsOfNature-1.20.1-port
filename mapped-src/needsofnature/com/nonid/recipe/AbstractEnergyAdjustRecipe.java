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
import java.util.List;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.IngredientPlacement;

abstract class AbstractEnergyAdjustRecipe
implements CraftingRecipe {
    private final CraftingRecipeCategory category;
    private IngredientPlacement ingredientPlacement;

    protected AbstractEnergyAdjustRecipe(CraftingRecipeCategory category) {
        this.category = category;
    }

    public boolean matches(CraftingRecipeInput input, World world) {
        int liquid = 0;
        int honeycomb = 0;
        int sugar = 0;
        int flowerMix = 0;
        int fungus = 0;
        int nonEmpty = 0;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
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

    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return this.getResultStack().copy();
    }

    public CraftingRecipeCategory getCategory() {
        return this.category;
    }

    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forShapeless(List.of(Ingredient.ofItem((ItemConvertible)Items.POTION), Ingredient.ofItem((ItemConvertible)Items.HONEYCOMB), Ingredient.ofItem((ItemConvertible)Items.SUGAR), Ingredient.ofItem((ItemConvertible)NeedsOfNature.FLOWER_MIX), Ingredient.ofItem((ItemConvertible)this.getRequiredFungusItem())));
        }
        return this.ingredientPlacement;
    }

    public List<RecipeDisplay> getDisplays() {
        SlotDisplay.StackSlotDisplay liquidDisplay = new SlotDisplay.StackSlotDisplay(AbstractEnergyAdjustRecipe.createLiquidDisplayStack());
        SlotDisplay honeycombDisplay = Ingredient.ofItem((ItemConvertible)Items.HONEYCOMB).toDisplay();
        SlotDisplay sugarDisplay = Ingredient.ofItem((ItemConvertible)Items.SUGAR).toDisplay();
        SlotDisplay flowerMixDisplay = Ingredient.ofItem((ItemConvertible)NeedsOfNature.FLOWER_MIX).toDisplay();
        SlotDisplay fungusDisplay = Ingredient.ofItem((ItemConvertible)this.getRequiredFungusItem()).toDisplay();
        return List.of(new ShapelessCraftingRecipeDisplay(List.of(liquidDisplay, honeycombDisplay, sugarDisplay, flowerMixDisplay, fungusDisplay), (SlotDisplay)new SlotDisplay.StackSlotDisplay(this.getResultStack()), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }

    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        DefaultedList remainders = DefaultedList.ofSize((int)input.size(), (Object)ItemStack.EMPTY);
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (NonPotions.isLiquidBottle(stack)) {
                remainders.set(i, (Object)new ItemStack((ItemConvertible)Items.GLASS_BOTTLE));
                continue;
            }
            remainders.set(i, (Object)stack.getItem().getRecipeRemainder());
        }
        return remainders;
    }

    private static ItemStack createLiquidDisplayStack() {
        RegistryEntry liquidPotion = Registries.POTION.getEntry((Object)NonPotions.LIQUID);
        ItemStack stack = new ItemStack((ItemConvertible)Items.POTION);
        stack.set(DataComponentTypes.POTION_CONTENTS, (Object)new PotionContentsComponent(liquidPotion));
        return stack;
    }

    protected abstract Item getRequiredFungusItem();

    protected abstract boolean isRequiredFungus(ItemStack var1);

    protected abstract ItemStack getResultStack();

    public abstract RecipeSerializer<? extends AbstractEnergyAdjustRecipe> getSerializer();
}

