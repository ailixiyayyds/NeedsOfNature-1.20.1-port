/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder
 *  net.minecraft.class_1792
 *  net.minecraft.class_1802
 *  net.minecraft.class_2960
 *  net.minecraft.class_5321
 *  net.minecraft.class_6862
 *  net.minecraft.class_6880
 *  net.minecraft.class_7923
 *  net.minecraft.class_7924
 */
package com.nonid.potion;

import com.nonid.potion.NonPotions;
import java.util.LinkedHashSet;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.minecraft.class_6862;
import net.minecraft.class_6880;
import net.minecraft.class_7923;
import net.minecraft.class_7924;

public final class NonBrewingRecipes {
    private static final class_6862<class_1792> EGGS_TAG = class_6862.method_40092((class_5321)class_7924.field_41197, (class_2960)class_2960.method_60655((String)"c", (String)"eggs"));

    private NonBrewingRecipes() {
    }

    public static void registerAll() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            class_6880 liquid = class_7923.field_41179.method_47983((Object)NonPotions.LIQUID);
            class_6880 fertileNectar = class_7923.field_41179.method_47983((Object)NonPotions.FERTILE_NECTAR);
            class_6880 energy = class_7923.field_41179.method_47983((Object)NonPotions.ENERGY);
            class_6880 energyStrong = class_7923.field_41179.method_47983((Object)NonPotions.ENERGY_II);
            class_6880 energyLong = class_7923.field_41179.method_47983((Object)NonPotions.ENERGY_LONG);
            class_6880 relief = class_7923.field_41179.method_47983((Object)NonPotions.ENERGY_RELIEF);
            class_6880 reliefStrong = class_7923.field_41179.method_47983((Object)NonPotions.ENERGY_RELIEF_II);
            class_6880 reliefLong = class_7923.field_41179.method_47983((Object)NonPotions.ENERGY_RELIEF_LONG);
            class_6880 fertility = class_7923.field_41179.method_47983((Object)NonPotions.FERTILITY_INCREASER);
            class_6880 fertilityStrong = class_7923.field_41179.method_47983((Object)NonPotions.FERTILITY_INCREASER_II);
            class_6880 fertilityLong = class_7923.field_41179.method_47983((Object)NonPotions.FERTILITY_INCREASER_LONG);
            builder.method_59705(liquid, class_1802.field_21987, energy);
            builder.method_59705(liquid, class_1802.field_21988, relief);
            builder.method_59705(energy, class_1802.field_8601, energyStrong);
            builder.method_59705(energy, class_1802.field_8725, energyLong);
            builder.method_59705(relief, class_1802.field_8601, reliefStrong);
            builder.method_59705(relief, class_1802.field_8725, reliefLong);
            LinkedHashSet<class_1792> eggItems = new LinkedHashSet<class_1792>();
            class_7923.field_41178.method_40286(EGGS_TAG).forEach(entry -> eggItems.add((class_1792)entry.comp_349()));
            if (eggItems.isEmpty()) {
                eggItems.add(class_1802.field_8803);
            }
            for (class_1792 egg : eggItems) {
                builder.method_59705(fertileNectar, egg, fertility);
            }
            builder.method_59705(fertility, class_1802.field_8601, fertilityStrong);
            builder.method_59705(fertility, class_1802.field_8725, fertilityLong);
        });
    }
}

