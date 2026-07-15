/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10124
 *  net.minecraft.class_10128
 *  net.minecraft.class_1293
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1842
 *  net.minecraft.class_1844
 *  net.minecraft.class_2378
 *  net.minecraft.class_2487
 *  net.minecraft.class_2960
 *  net.minecraft.class_3417
 *  net.minecraft.class_6880
 *  net.minecraft.class_7923
 *  net.minecraft.class_9279
 *  net.minecraft.class_9331
 *  net.minecraft.class_9334
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.potion;

import com.nonid.NeedsOfNature;
import com.nonid.effect.NonStatusEffects;
import java.util.Optional;
import net.minecraft.class_10124;
import net.minecraft.class_10128;
import net.minecraft.class_1293;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_2378;
import net.minecraft.class_2487;
import net.minecraft.class_2960;
import net.minecraft.class_3417;
import net.minecraft.class_6880;
import net.minecraft.class_7923;
import net.minecraft.class_9279;
import net.minecraft.class_9331;
import net.minecraft.class_9334;
import org.jetbrains.annotations.Nullable;

public final class NonPotions {
    public static final String LIQUID_ENTITY_DATA_KEY = "liquid_entity";
    private static final class_10124 HONEY_BOTTLE_DRINK_CONSUMABLE = class_10128.method_62859().method_62855((class_6880)class_3417.field_20615).method_62851();
    private static final int ENERGY_BASE_DURATION_TICKS = 800;
    private static final int ENERGY_STRONG_DURATION_TICKS = 400;
    private static final int ENERGY_LONG_DURATION_TICKS = 1600;
    private static final int FERTILITY_BASE_DURATION_TICKS = 9600;
    private static final int FERTILITY_STRONG_DURATION_TICKS = 4800;
    private static final int FERTILITY_LONG_DURATION_TICKS = 19200;
    public static final class_1842 LIQUID = NonPotions.register("liquid", new class_1842("needsofnature.liquid", new class_1293[0]));
    public static final class_1842 ENERGY = NonPotions.register("energy", new class_1842("needsofnature.energy", new class_1293[]{new class_1293(class_7923.field_41174.method_47983((Object)NonStatusEffects.ENERGY), 800)}));
    public static final class_1842 ENERGY_II = NonPotions.register("energy_ii", new class_1842("needsofnature.energy_ii", new class_1293[]{new class_1293(class_7923.field_41174.method_47983((Object)NonStatusEffects.ENERGY), 400, 1)}));
    public static final class_1842 ENERGY_LONG = NonPotions.register("energy_long", new class_1842("needsofnature.energy_long", new class_1293[]{new class_1293(class_7923.field_41174.method_47983((Object)NonStatusEffects.ENERGY), 1600)}));
    public static final class_1842 ENERGY_RELIEF = NonPotions.register("energy_relief", new class_1842("needsofnature.energy_relief", new class_1293[]{new class_1293(class_7923.field_41174.method_47983((Object)NonStatusEffects.ENERGY_RELIEF), 800)}));
    public static final class_1842 ENERGY_RELIEF_II = NonPotions.register("energy_relief_ii", new class_1842("needsofnature.energy_relief_ii", new class_1293[]{new class_1293(class_7923.field_41174.method_47983((Object)NonStatusEffects.ENERGY_RELIEF), 400, 1)}));
    public static final class_1842 ENERGY_RELIEF_LONG = NonPotions.register("energy_relief_long", new class_1842("needsofnature.energy_relief_long", new class_1293[]{new class_1293(class_7923.field_41174.method_47983((Object)NonStatusEffects.ENERGY_RELIEF), 1600)}));
    public static final class_1842 FERTILE_NECTAR = NonPotions.register("fertile_nectar", new class_1842("needsofnature.fertile_nectar", new class_1293[0]));
    public static final class_1842 FERTILITY_INCREASER = NonPotions.register("fertility_increaser", new class_1842("needsofnature.fertility_increaser", new class_1293[]{new class_1293(class_7923.field_41174.method_47983((Object)NonStatusEffects.FERTILITY_INCREASER), 9600)}));
    public static final class_1842 FERTILITY_INCREASER_II = NonPotions.register("fertility_increaser_ii", new class_1842("needsofnature.fertility_increaser_ii", new class_1293[]{new class_1293(class_7923.field_41174.method_47983((Object)NonStatusEffects.FERTILITY_INCREASER), 4800, 1)}));
    public static final class_1842 FERTILITY_INCREASER_LONG = NonPotions.register("fertility_increaser_long", new class_1842("needsofnature.fertility_increaser_long", new class_1293[]{new class_1293(class_7923.field_41174.method_47983((Object)NonStatusEffects.FERTILITY_INCREASER), 19200)}));

    private NonPotions() {
    }

    public static void registerAll() {
    }

    public static boolean isLiquidPotion(Optional<class_6880<class_1842>> potionEntry) {
        return potionEntry != null && potionEntry.isPresent() && potionEntry.get().comp_349() == LIQUID;
    }

    public static boolean isLiquidBottle(class_1799 stack) {
        if (stack == null || stack.method_7960() || !stack.method_31574(class_1802.field_8574)) {
            return false;
        }
        class_1844 contents = (class_1844)stack.method_58695(class_9334.field_49651, (Object)class_1844.field_49274);
        return NonPotions.isLiquidPotion(contents.comp_2378());
    }

    public static void applyHoneyBottleDrinkSound(class_1799 stack) {
        if (stack == null || stack.method_7960()) {
            return;
        }
        stack.method_57379(class_9334.field_53964, (Object)HONEY_BOTTLE_DRINK_CONSUMABLE);
    }

    public static void setLiquidBottleEntityTypeId(class_1799 stack, @Nullable class_2960 entityTypeId) {
        if (stack == null || stack.method_7960()) {
            return;
        }
        class_9279.method_57452((class_9331)class_9334.field_49628, (class_1799)stack, nbt -> {
            String value = entityTypeId == null ? "" : entityTypeId.toString();
            nbt.method_10582(LIQUID_ENTITY_DATA_KEY, value);
        });
    }

    @Nullable
    public static class_2960 getLiquidBottleEntityTypeId(class_1799 stack) {
        if (!NonPotions.isLiquidBottle(stack)) {
            return null;
        }
        class_9279 custom = (class_9279)stack.method_58694(class_9334.field_49628);
        if (custom == null || custom.method_57458()) {
            return null;
        }
        class_2487 nbt = custom.method_57461();
        if (!nbt.method_10545(LIQUID_ENTITY_DATA_KEY)) {
            return null;
        }
        String raw = nbt.method_68564(LIQUID_ENTITY_DATA_KEY, "");
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return class_2960.method_12829((String)raw);
    }

    private static class_1842 register(String path, class_1842 potion) {
        return (class_1842)class_2378.method_10230((class_2378)class_7923.field_41179, (class_2960)NeedsOfNature.id(path), (Object)potion);
    }
}

