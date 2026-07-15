/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1865
 *  net.minecraft.class_1935
 *  net.minecraft.class_7710
 */
package com.nonid.recipe;

import com.nonid.NeedsOfNature;
import com.nonid.recipe.AbstractEnergyAdjustRecipe;
import com.nonid.recipe.NonRecipeSerializers;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1865;
import net.minecraft.class_1935;
import net.minecraft.class_7710;

public final class EnergyAugmenterRecipe
extends AbstractEnergyAdjustRecipe {
    public EnergyAugmenterRecipe(class_7710 category) {
        super(category);
    }

    @Override
    protected boolean isRequiredFungus(class_1799 stack) {
        return stack.method_31574(class_1802.field_21987);
    }

    @Override
    protected class_1792 getRequiredFungusItem() {
        return class_1802.field_21987;
    }

    @Override
    protected class_1799 getResultStack() {
        return new class_1799((class_1935)NeedsOfNature.ENERGY_AUGMENTER);
    }

    @Override
    public class_1865<? extends AbstractEnergyAdjustRecipe> method_8119() {
        return NonRecipeSerializers.ENERGY_AUGMENTER_RECIPE;
    }
}

