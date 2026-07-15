/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_2960
 */
package com.nonid.client;

import com.nonid.NonTrinketsIntegration;
import java.util.List;
import net.minecraft.class_1309;
import net.minecraft.class_2960;

public final class NonAccessorySkinOverlays {
    private NonAccessorySkinOverlays() {
    }

    public static List<class_2960> resolveEquippedOverlays(class_1309 entity) {
        return NonTrinketsIntegration.resolveEquippedSkinOverlays(entity);
    }
}

