/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.recipe.SpecialCraftingRecipe$SpecialRecipeSerializer
 *  net.minecraft.recipe.RecipeSerializer
 *  net.minecraft.registry.Registry
 *  net.minecraft.util.Identifier
 *  net.minecraft.registry.Registries
 */
package com.nonid.recipe;

import com.nonid.NeedsOfNature;
import com.nonid.recipe.EnergyAugmenterRecipe;
import com.nonid.recipe.EnergyDiminisherRecipe;
import com.nonid.recipe.EnergyStabilizerRecipe;
import com.nonid.recipe.FertileNectarRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

public final class NonRecipeSerializers {
    public static final RecipeSerializer<EnergyAugmenterRecipe> ENERGY_AUGMENTER_RECIPE = (RecipeSerializer)Registry.register((Registry)Registries.RECIPE_SERIALIZER, (Identifier)NeedsOfNature.id("crafting_energy_augmenter"), (Object)new SpecialRecipeSerializer<>(EnergyAugmenterRecipe::new));
    public static final RecipeSerializer<EnergyDiminisherRecipe> ENERGY_DIMINISHER_RECIPE = (RecipeSerializer)Registry.register((Registry)Registries.RECIPE_SERIALIZER, (Identifier)NeedsOfNature.id("crafting_energy_diminisher"), (Object)new SpecialRecipeSerializer<>(EnergyDiminisherRecipe::new));
    public static final RecipeSerializer<EnergyStabilizerRecipe> ENERGY_STABILIZER_RECIPE = (RecipeSerializer)Registry.register((Registry)Registries.RECIPE_SERIALIZER, (Identifier)NeedsOfNature.id("crafting_energy_stabilizer"), (Object)new SpecialRecipeSerializer<>(EnergyStabilizerRecipe::new));
    public static final RecipeSerializer<FertileNectarRecipe> FERTILE_NECTAR_RECIPE = (RecipeSerializer)Registry.register((Registry)Registries.RECIPE_SERIALIZER, (Identifier)NeedsOfNature.id("crafting_fertile_nectar"), (Object)new SpecialRecipeSerializer<>(FertileNectarRecipe::new));

    private NonRecipeSerializers() {
    }

    public static void registerAll() {
    }
}

