/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1293
 *  net.minecraft.class_2960
 *  net.minecraft.class_746
 *  net.minecraft.class_7923
 */
package com.nonid.client;

import com.nonid.effect.NonStatusEffects;
import net.minecraft.class_1293;
import net.minecraft.class_2960;
import net.minecraft.class_746;
import net.minecraft.class_7923;

public final class NonEnergizedEffectTextures {
    private static final class_2960 ENERGIZED_SPRITE_ID = class_2960.method_60655((String)"needsofnature", (String)"mob_effect/energized");
    private static final class_2960 ENERGIZED_STAGE_ONE_TEXTURE = class_2960.method_60655((String)"needsofnature", (String)"textures/mob_effects/energized1.png");
    private static final class_2960 ENERGIZED_STAGE_TWO_TEXTURE = class_2960.method_60655((String)"needsofnature", (String)"textures/mob_effects/energized2.png");
    private static final class_2960 ENERGIZED_STAGE_THREE_TEXTURE = class_2960.method_60655((String)"needsofnature", (String)"textures/mob_effects/energized3.png");

    private NonEnergizedEffectTextures() {
    }

    public static boolean isEnergizedSprite(class_2960 sprite) {
        return ENERGIZED_SPRITE_ID.equals((Object)sprite);
    }

    public static class_2960 resolveTexture(class_746 player) {
        if (player == null) {
            return ENERGIZED_STAGE_ONE_TEXTURE;
        }
        class_1293 effect = player.method_6112(class_7923.field_41174.method_47983((Object)NonStatusEffects.ENERGIZED));
        if (effect == null) {
            return ENERGIZED_STAGE_ONE_TEXTURE;
        }
        int amplifier = Math.max(0, effect.method_5578());
        if (amplifier >= 2) {
            return ENERGIZED_STAGE_THREE_TEXTURE;
        }
        if (amplifier == 1) {
            return ENERGIZED_STAGE_TWO_TEXTURE;
        }
        return ENERGIZED_STAGE_ONE_TEXTURE;
    }
}

