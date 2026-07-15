/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.SpawnEggItem
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.registry.Registries
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.client;

import com.nonid.PregnancyHolder;
import com.nonid.client.NonPregnancyClientState;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

public final class NonPregnantEffectTextures {
    private static final Identifier PREGNANT_SPRITE_ID = Identifier.of((String)"needsofnature", (String)"mob_effect/pregnant");
    private static final int ITEM_ICON_SIZE = 16;

    private NonPregnantEffectTextures() {
    }

    public static boolean isPregnantSprite(Identifier sprite) {
        return PREGNANT_SPRITE_ID.equals((Object)sprite);
    }

    public static ItemStack resolvePregnantEggStack(@Nullable ClientPlayerEntity player) {
        Identifier entityTypeId = NonPregnantEffectTextures.resolvePregnantEntityTypeId(player);
        if (entityTypeId == null) {
            return ItemStack.EMPTY;
        }
        EntityType entityType = Registries.ENTITY_TYPE.getOptionalValue(entityTypeId).orElse(null);
        if (entityType == null) {
            return ItemStack.EMPTY;
        }
        SpawnEggItem egg = SpawnEggItem.forEntity((EntityType)entityType);
        if (egg == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack((ItemConvertible)egg);
    }

    private static Identifier resolvePregnantEntityTypeId(@Nullable ClientPlayerEntity player) {
        Identifier synced = NonPregnancyClientState.getPregnantEntityTypeId();
        if (synced != null) {
            return synced;
        }
        if (player instanceof PregnancyHolder) {
            PregnancyHolder holder = (PregnancyHolder)player;
            return holder.getPregnantEntityTypeId();
        }
        return null;
    }

    public static void drawPregnantEggIcon(DrawContext context, ItemStack eggStack, int x, int y, int width, int height) {
        int drawX = x + Math.max(0, (width - 16) / 2);
        int drawY = y + Math.max(0, (height - 16) / 2);
        context.drawItemWithoutEntity(eggStack, drawX, drawY);
    }
}

