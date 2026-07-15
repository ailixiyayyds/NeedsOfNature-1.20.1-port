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
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;

public final class NonBrewingRecipes {
    private static final TagKey<Item> EGGS_TAG = TagKey.of((RegistryKey)RegistryKeys.ITEM, (Identifier)Identifier.of((String)"c", (String)"eggs"));

    private NonBrewingRecipes() {
    }

    public static void registerAll() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            RegistryEntry liquid = Registries.POTION.getEntry((Object)NonPotions.LIQUID);
            RegistryEntry fertileNectar = Registries.POTION.getEntry((Object)NonPotions.FERTILE_NECTAR);
            RegistryEntry energy = Registries.POTION.getEntry((Object)NonPotions.ENERGY);
            RegistryEntry energyStrong = Registries.POTION.getEntry((Object)NonPotions.ENERGY_II);
            RegistryEntry energyLong = Registries.POTION.getEntry((Object)NonPotions.ENERGY_LONG);
            RegistryEntry relief = Registries.POTION.getEntry((Object)NonPotions.ENERGY_RELIEF);
            RegistryEntry reliefStrong = Registries.POTION.getEntry((Object)NonPotions.ENERGY_RELIEF_II);
            RegistryEntry reliefLong = Registries.POTION.getEntry((Object)NonPotions.ENERGY_RELIEF_LONG);
            RegistryEntry fertility = Registries.POTION.getEntry((Object)NonPotions.FERTILITY_INCREASER);
            RegistryEntry fertilityStrong = Registries.POTION.getEntry((Object)NonPotions.FERTILITY_INCREASER_II);
            RegistryEntry fertilityLong = Registries.POTION.getEntry((Object)NonPotions.FERTILITY_INCREASER_LONG);
            builder.registerPotionRecipe(liquid, Items.CRIMSON_FUNGUS, energy);
            builder.registerPotionRecipe(liquid, Items.WARPED_FUNGUS, relief);
            builder.registerPotionRecipe(energy, Items.GLOWSTONE_DUST, energyStrong);
            builder.registerPotionRecipe(energy, Items.REDSTONE, energyLong);
            builder.registerPotionRecipe(relief, Items.GLOWSTONE_DUST, reliefStrong);
            builder.registerPotionRecipe(relief, Items.REDSTONE, reliefLong);
            LinkedHashSet<Item> eggItems = new LinkedHashSet<Item>();
            Registries.ITEM.iterateEntries(EGGS_TAG).forEach(entry -> eggItems.add((Item)entry.comp_349()));
            if (eggItems.isEmpty()) {
                eggItems.add(Items.EGG);
            }
            for (Item egg : eggItems) {
                builder.registerPotionRecipe(fertileNectar, egg, fertility);
            }
            builder.registerPotionRecipe(fertility, Items.GLOWSTONE_DUST, fertilityStrong);
            builder.registerPotionRecipe(fertility, Items.REDSTONE, fertilityLong);
        });
    }
}

