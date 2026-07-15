/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.component.type.ConsumableComponent
 *  net.minecraft.component.type.ConsumableComponents
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.potion.Potion
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.registry.Registry
 *  net.minecraft.nbt.NbtCompound
 *  net.minecraft.util.Identifier
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.Registries
 *  net.minecraft.component.type.NbtComponent
 *  net.minecraft.component.ComponentType
 *  net.minecraft.component.DataComponentTypes
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.potion;

import com.nonid.NeedsOfNature;
import com.nonid.effect.NonStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

public final class NonPotions {
    public static final String LIQUID_ENTITY_DATA_KEY = "liquid_entity";
    private static final int ENERGY_BASE_DURATION_TICKS = 800;
    private static final int ENERGY_STRONG_DURATION_TICKS = 400;
    private static final int ENERGY_LONG_DURATION_TICKS = 1600;
    private static final int FERTILITY_BASE_DURATION_TICKS = 9600;
    private static final int FERTILITY_STRONG_DURATION_TICKS = 4800;
    private static final int FERTILITY_LONG_DURATION_TICKS = 19200;
    public static final Potion LIQUID = NonPotions.register("liquid", new Potion());
    public static final Potion ENERGY = NonPotions.register("energy", new Potion(new StatusEffectInstance(NonStatusEffects.ENERGY, 800)));
    public static final Potion ENERGY_II = NonPotions.register("energy_ii", new Potion(new StatusEffectInstance(NonStatusEffects.ENERGY, 400, 1)));
    public static final Potion ENERGY_LONG = NonPotions.register("energy_long", new Potion(new StatusEffectInstance(NonStatusEffects.ENERGY, 1600)));
    public static final Potion ENERGY_RELIEF = NonPotions.register("energy_relief", new Potion(new StatusEffectInstance(NonStatusEffects.ENERGY_RELIEF, 800)));
    public static final Potion ENERGY_RELIEF_II = NonPotions.register("energy_relief_ii", new Potion(new StatusEffectInstance(NonStatusEffects.ENERGY_RELIEF, 400, 1)));
    public static final Potion ENERGY_RELIEF_LONG = NonPotions.register("energy_relief_long", new Potion(new StatusEffectInstance(NonStatusEffects.ENERGY_RELIEF, 1600)));
    public static final Potion FERTILE_NECTAR = NonPotions.register("fertile_nectar", new Potion());
    public static final Potion FERTILITY_INCREASER = NonPotions.register("fertility_increaser", new Potion(new StatusEffectInstance(NonStatusEffects.FERTILITY_INCREASER, 9600)));
    public static final Potion FERTILITY_INCREASER_II = NonPotions.register("fertility_increaser_ii", new Potion(new StatusEffectInstance(NonStatusEffects.FERTILITY_INCREASER, 4800, 1)));
    public static final Potion FERTILITY_INCREASER_LONG = NonPotions.register("fertility_increaser_long", new Potion(new StatusEffectInstance(NonStatusEffects.FERTILITY_INCREASER, 19200)));

    private NonPotions() {
    }

    public static void registerAll() {
    }

    public static boolean isLiquidBottle(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.isOf(Items.POTION)) {
            return false;
        }
        return PotionUtil.getPotion(stack) == LIQUID;
    }

    public static void applyHoneyBottleDrinkSound(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        // Per-stack consumable sounds are not available before data components.
    }

    public static void setLiquidBottleEntityTypeId(ItemStack stack, @Nullable Identifier entityTypeId) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        stack.getOrCreateNbt().putString(LIQUID_ENTITY_DATA_KEY, entityTypeId == null ? "" : entityTypeId.toString());
    }

    @Nullable
    public static Identifier getLiquidBottleEntityTypeId(ItemStack stack) {
        if (!NonPotions.isLiquidBottle(stack)) {
            return null;
        }
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) {
            return null;
        }
        if (!nbt.contains(LIQUID_ENTITY_DATA_KEY)) {
            return null;
        }
        String raw = nbt.getString(LIQUID_ENTITY_DATA_KEY);
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return Identifier.tryParse((String)raw);
    }

    private static Potion register(String path, Potion potion) {
        return (Potion)Registry.register((Registry)Registries.POTION, (Identifier)NeedsOfNature.id(path), (Object)potion);
    }
}

