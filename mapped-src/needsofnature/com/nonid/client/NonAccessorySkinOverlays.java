/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.Identifier
 */
package com.nonid.client;

import com.nonid.NonTrinketsIntegration;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public final class NonAccessorySkinOverlays {
    private NonAccessorySkinOverlays() {
    }

    public static List<Identifier> resolveEquippedOverlays(LivingEntity entity) {
        return NonTrinketsIntegration.resolveEquippedSkinOverlays(entity);
    }
}

