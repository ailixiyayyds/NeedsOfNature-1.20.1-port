/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1291
 *  net.minecraft.class_2378
 *  net.minecraft.class_2960
 *  net.minecraft.class_7923
 */
package com.nonid.effect;

import com.nonid.NeedsOfNature;
import com.nonid.effect.EnergizedStatusEffect;
import com.nonid.effect.EnergyReliefStatusEffect;
import com.nonid.effect.EnergyStatusEffect;
import com.nonid.effect.FertilityIncreaserStatusEffect;
import com.nonid.effect.FilledStatusEffect;
import com.nonid.effect.PregnantStatusEffect;
import net.minecraft.class_1291;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public final class NonStatusEffects {
    public static final class_1291 ENERGY = NonStatusEffects.register("energy", new EnergyStatusEffect());
    public static final class_1291 ENERGY_RELIEF = NonStatusEffects.register("energy_relief", new EnergyReliefStatusEffect());
    public static final class_1291 FERTILITY_INCREASER = NonStatusEffects.register("fertility_increaser", new FertilityIncreaserStatusEffect());
    public static final class_1291 FILLED = NonStatusEffects.register("filled", new FilledStatusEffect());
    public static final class_1291 ENERGIZED = NonStatusEffects.register("energized", new EnergizedStatusEffect());
    public static final class_1291 PREGNANT = NonStatusEffects.register("pregnant", new PregnantStatusEffect());

    private NonStatusEffects() {
    }

    public static void registerAll() {
    }

    private static class_1291 register(String path, class_1291 effect) {
        return (class_1291)class_2378.method_10230((class_2378)class_7923.field_41174, (class_2960)NeedsOfNature.id(path), (Object)effect);
    }
}

