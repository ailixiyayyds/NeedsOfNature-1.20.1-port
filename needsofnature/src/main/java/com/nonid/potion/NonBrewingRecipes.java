/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder
 *  net.minecraft.item.Item
 *  net.minecraft.item.Items
 *  net.minecraft.util.Identifier
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.registry.tag.TagKey
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.Registries
 *  net.minecraft.registry.RegistryKeys
 */
package com.nonid.potion;

import com.nonid.potion.NonPotions;
import java.util.LinkedHashSet;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;

public final class NonBrewingRecipes {
    private static final TagKey<Item> EGGS_TAG = TagKey.of((RegistryKey)RegistryKeys.ITEM, new Identifier("c", "eggs"));

    private NonBrewingRecipes() {
    }

    public static void registerAll() {
        BrewingRecipeRegistry.registerPotionRecipe(NonPotions.LIQUID, Items.CRIMSON_FUNGUS, NonPotions.ENERGY);
        BrewingRecipeRegistry.registerPotionRecipe(NonPotions.LIQUID, Items.WARPED_FUNGUS, NonPotions.ENERGY_RELIEF);
        BrewingRecipeRegistry.registerPotionRecipe(NonPotions.ENERGY, Items.GLOWSTONE_DUST, NonPotions.ENERGY_II);
        BrewingRecipeRegistry.registerPotionRecipe(NonPotions.ENERGY, Items.REDSTONE, NonPotions.ENERGY_LONG);
        BrewingRecipeRegistry.registerPotionRecipe(NonPotions.ENERGY_RELIEF, Items.GLOWSTONE_DUST, NonPotions.ENERGY_RELIEF_II);
        BrewingRecipeRegistry.registerPotionRecipe(NonPotions.ENERGY_RELIEF, Items.REDSTONE, NonPotions.ENERGY_RELIEF_LONG);
            LinkedHashSet<Item> eggItems = new LinkedHashSet<Item>();
        Registries.ITEM.iterateEntries(EGGS_TAG).forEach(entry -> eggItems.add(entry.value()));
            if (eggItems.isEmpty()) {
                eggItems.add(Items.EGG);
            }
            for (Item egg : eggItems) {
            BrewingRecipeRegistry.registerPotionRecipe(NonPotions.FERTILE_NECTAR, egg, NonPotions.FERTILITY_INCREASER);
            }
        BrewingRecipeRegistry.registerPotionRecipe(NonPotions.FERTILITY_INCREASER, Items.GLOWSTONE_DUST, NonPotions.FERTILITY_INCREASER_II);
        BrewingRecipeRegistry.registerPotionRecipe(NonPotions.FERTILITY_INCREASER, Items.REDSTONE, NonPotions.FERTILITY_INCREASER_LONG);
    }
}

