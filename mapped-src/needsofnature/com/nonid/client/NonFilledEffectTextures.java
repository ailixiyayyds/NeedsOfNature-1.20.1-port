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

public final class NonFilledEffectTextures {
    private static final Identifier FILLED_SPRITE_ID = Identifier.of((String)"needsofnature", (String)"mob_effect/filled");
    private static final Identifier FILLED_STAGE_ONE_TEXTURE = Identifier.of((String)"needsofnature", (String)"textures/mob_effects/filled1.png");
    private static final Identifier FILLED_STAGE_TWO_TEXTURE = Identifier.of((String)"needsofnature", (String)"textures/mob_effects/filled2.png");
    private static final Identifier FILLED_STAGE_THREE_TEXTURE = Identifier.of((String)"needsofnature", (String)"textures/mob_effects/filled3.png");

    private NonFilledEffectTextures() {
    }

    public static boolean isFilledSprite(Identifier sprite) {
        return FILLED_SPRITE_ID.equals((Object)sprite);
    }

    public static Identifier resolveTexture(ClientPlayerEntity player) {
        if (player == null) {
            return FILLED_STAGE_ONE_TEXTURE;
        }
        StatusEffectInstance effect = player.getStatusEffect(Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.FILLED));
        if (effect == null) {
            return FILLED_STAGE_ONE_TEXTURE;
        }
        int amplifier = Math.max(0, effect.getAmplifier());
        if (amplifier >= 2) {
            return FILLED_STAGE_THREE_TEXTURE;
        }
        if (amplifier == 1) {
            return FILLED_STAGE_TWO_TEXTURE;
        }
        return FILLED_STAGE_ONE_TEXTURE;
    }
}

