/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.registry.Registry
 *  net.minecraft.util.Identifier
 *  net.minecraft.sound.SoundEvent
 *  net.minecraft.registry.Registries
 */
package com.nonid.sound;

import com.nonid.NeedsOfNature;
import java.util.Locale;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registries;

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
        Identifier id = NeedsOfNature.id(path);
        if (Registries.SOUND_EVENT.containsId(id)) {
            return;
        }
        Registry.register((Registry)Registries.SOUND_EVENT, (Identifier)id, (Object)SoundEvent.of((Identifier)id));
    }
}

