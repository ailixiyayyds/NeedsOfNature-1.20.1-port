/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.registry.Registries
 */
package com.nonid.client;

import com.nonid.effect.NonStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.Registries;

public final class NonEnergizedEffectTextures {
    private static final Identifier ENERGIZED_SPRITE_ID = Identifier.of((String)"needsofnature", (String)"mob_effect/energized");
    private static final Identifier ENERGIZED_STAGE_ONE_TEXTURE = Identifier.of((String)"needsofnature", (String)"textures/mob_effects/energized1.png");
    private static final Identifier ENERGIZED_STAGE_TWO_TEXTURE = Identifier.of((String)"needsofnature", (String)"textures/mob_effects/energized2.png");
    private static final Identifier ENERGIZED_STAGE_THREE_TEXTURE = Identifier.of((String)"needsofnature", (String)"textures/mob_effects/energized3.png");

    private NonEnergizedEffectTextures() {
    }

    public static boolean isEnergizedSprite(Identifier sprite) {
        return ENERGIZED_SPRITE_ID.equals((Object)sprite);
    }

    public static Identifier resolveTexture(ClientPlayerEntity player) {
        if (player == null) {
            return ENERGIZED_STAGE_ONE_TEXTURE;
        }
        StatusEffectInstance effect = player.getStatusEffect(NonStatusEffects.ENERGIZED);
        if (effect == null) {
            return ENERGIZED_STAGE_ONE_TEXTURE;
        }
        int amplifier = Math.max(0, effect.getAmplifier());
        if (amplifier >= 2) {
            return ENERGIZED_STAGE_THREE_TEXTURE;
        }
        if (amplifier == 1) {
            return ENERGIZED_STAGE_TWO_TEXTURE;
        }
        return ENERGIZED_STAGE_ONE_TEXTURE;
    }
}

