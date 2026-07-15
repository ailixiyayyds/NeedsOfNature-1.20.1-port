/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1852$class_1866
 *  net.minecraft.class_1865
 *  net.minecraft.class_2378
 *  net.minecraft.class_2960
 *  net.minecraft.class_7923
 */
package com.nonid.recipe;

import com.nonid.NeedsOfNature;
import com.nonid.recipe.EnergyAugmenterRecipe;
import com.nonid.recipe.EnergyDiminisherRecipe;
import com.nonid.recipe.EnergyStabilizerRecipe;
import com.nonid.recipe.FertileNectarRecipe;
import net.minecraft.class_1852;
import net.minecraft.class_1865;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public final class NonRecipeSerializers {
    public static final class_1865<EnergyAugmenterRecipe> ENERGY_AUGMENTER_RECIPE = (class_1865)class_2378.method_10230((class_2378)class_7923.field_41189, (class_2960)NeedsOfNature.id("crafting_energy_augmenter"), (Object)new class_1852.class_1866(EnergyAugmenterRecipe::new));
    public static final class_1865<EnergyDiminisherRecipe> ENERGY_DIMINISHER_RECIPE = (class_1865)class_2378.method_10230((class_2378)class_7923.field_41189, (class_2960)NeedsOfNature.id("crafting_energy_diminisher"), (Object)new class_1852.class_1866(EnergyDiminisherRecipe::new));
    public static final class_1865<EnergyStabilizerRecipe> ENERGY_STABILIZER_RECIPE = (class_1865)class_2378.method_10230((class_2378)class_7923.field_41189, (class_2960)NeedsOfNature.id("crafting_energy_stabilizer"), (Object)new class_1852.class_1866(EnergyStabilizerRecipe::new));
    public static final class_1865<FertileNectarRecipe> FERTILE_NECTAR_RECIPE = (class_1865)class_2378.method_10230((class_2378)class_7923.field_41189, (class_2960)NeedsOfNature.id("crafting_fertile_nectar"), (Object)new class_1852.class_1866(FertileNectarRecipe::new));

    private NonRecipeSerializers() {
    }

    public static void registerAll() {
    }
}

