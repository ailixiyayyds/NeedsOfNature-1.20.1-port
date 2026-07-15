/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.registry.Registry
 *  net.minecraft.util.Identifier
 *  net.minecraft.registry.Registries
 */
package com.nonid.effect;

import com.nonid.NeedsOfNature;
import com.nonid.effect.EnergizedStatusEffect;
import com.nonid.effect.EnergyReliefStatusEffect;
import com.nonid.effect.EnergyStatusEffect;
import com.nonid.effect.FertilityIncreaserStatusEffect;
import com.nonid.effect.FilledStatusEffect;
import com.nonid.effect.PregnantStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

public final class NonStatusEffects {
    public static final StatusEffect ENERGY = NonStatusEffects.register("energy", new EnergyStatusEffect());
    public static final StatusEffect ENERGY_RELIEF = NonStatusEffects.register("energy_relief", new EnergyReliefStatusEffect());
    public static final StatusEffect FERTILITY_INCREASER = NonStatusEffects.register("fertility_increaser", new FertilityIncreaserStatusEffect());
    public static final StatusEffect FILLED = NonStatusEffects.register("filled", new FilledStatusEffect());
    public static final StatusEffect ENERGIZED = NonStatusEffects.register("energized", new EnergizedStatusEffect());
    public static final StatusEffect PREGNANT = NonStatusEffects.register("pregnant", new PregnantStatusEffect());

    private NonStatusEffects() {
    }

    public static void registerAll() {
    }

    private static StatusEffect register(String path, StatusEffect effect) {
        return (StatusEffect)Registry.register((Registry)Registries.STATUS_EFFECT, (Identifier)NeedsOfNature.id(path), (Object)effect);
    }
}

