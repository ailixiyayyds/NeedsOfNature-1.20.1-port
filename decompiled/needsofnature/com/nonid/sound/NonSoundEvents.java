/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2378
 *  net.minecraft.class_2960
 *  net.minecraft.class_3414
 *  net.minecraft.class_7923
 */
package com.nonid.sound;

import com.nonid.NeedsOfNature;
import java.util.Locale;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_7923;

public final class NonSoundEvents {
    private static final int DRY_IMPACT_COUNT = 11;
    private static final int WET_IMPACT_COUNT = 16;
    private static final int WET_COUNT = 12;
    private static final int SHOT_IN_COUNT = 3;
    private static final int SHOT_OUT_COUNT = 6;
    private static final int MOTION_COUNT = 10;
    private static final int RETRACT_COUNT = 3;
    private static final int RIP_COUNT = 3;

    private NonSoundEvents() {
    }

    public static void registerAll() {
        NonSoundEvents.registerSeries("impactdry", 11);
        NonSoundEvents.registerSeries("impactwet", 16);
        NonSoundEvents.registerSeries("wet", 12);
        NonSoundEvents.registerSeries("shot_in", 3);
        NonSoundEvents.registerSeries("shot_out", 6);
        NonSoundEvents.registerSeries("motion", 10);
        NonSoundEvents.registerSeries("retract", 3);
        NonSoundEvents.registerSeries("rip", 3);
        NonSoundEvents.register("funkytown");
    }

    private static void registerSeries(String prefix, int count) {
        for (int i = 1; i <= count; ++i) {
            String name = prefix + String.format(Locale.ROOT, "%02d", i);
            NonSoundEvents.register(name);
        }
    }

    private static void register(String path) {
        class_2960 id = NeedsOfNature.id(path);
        if (class_7923.field_41172.method_10250(id)) {
            return;
        }
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)id, (Object)class_3414.method_47908((class_2960)id));
    }
}

