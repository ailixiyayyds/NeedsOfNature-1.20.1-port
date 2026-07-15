/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1299
 *  net.minecraft.class_1799
 *  net.minecraft.class_1826
 *  net.minecraft.class_1935
 *  net.minecraft.class_2960
 *  net.minecraft.class_332
 *  net.minecraft.class_746
 *  net.minecraft.class_7923
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.client;

import com.nonid.PregnancyHolder;
import com.nonid.client.NonPregnancyClientState;
import net.minecraft.class_1299;
import net.minecraft.class_1799;
import net.minecraft.class_1826;
import net.minecraft.class_1935;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import org.jetbrains.annotations.Nullable;

public final class NonPregnantEffectTextures {
    private static final class_2960 PREGNANT_SPRITE_ID = class_2960.method_60655((String)"needsofnature", (String)"mob_effect/pregnant");
    private static final int ITEM_ICON_SIZE = 16;

    private NonPregnantEffectTextures() {
    }

    public static boolean isPregnantSprite(class_2960 sprite) {
        return PREGNANT_SPRITE_ID.equals((Object)sprite);
    }

    public static class_1799 resolvePregnantEggStack(@Nullable class_746 player) {
        class_2960 entityTypeId = NonPregnantEffectTextures.resolvePregnantEntityTypeId(player);
        if (entityTypeId == null) {
            return class_1799.field_8037;
        }
        class_1299 entityType = class_7923.field_41177.method_17966(entityTypeId).orElse(null);
        if (entityType == null) {
            return class_1799.field_8037;
        }
        class_1826 egg = class_1826.method_8019((class_1299)entityType);
        if (egg == null) {
            return class_1799.field_8037;
        }
        return new class_1799((class_1935)egg);
    }

    private static class_2960 resolvePregnantEntityTypeId(@Nullable class_746 player) {
        class_2960 synced = NonPregnancyClientState.getPregnantEntityTypeId();
        if (synced != null) {
            return synced;
        }
        if (player instanceof PregnancyHolder) {
            PregnancyHolder holder = (PregnancyHolder)player;
            return holder.getPregnantEntityTypeId();
        }
        return null;
    }

    public static void drawPregnantEggIcon(class_332 context, class_1799 eggStack, int x, int y, int width, int height) {
        int drawX = x + Math.max(0, (width - 16) / 2);
        int drawY = y + Math.max(0, (height - 16) / 2);
        context.method_51445(eggStack, drawX, drawY);
    }
}

