/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1621
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.util;

import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1621;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public final class AfwEntityVariants {
    private AfwEntityVariants() {
    }

    @Nullable
    public static String resolveVariant(class_1297 entity) {
        if (entity instanceof class_1621) {
            class_1621 slime = (class_1621)entity;
            return "size_" + Math.max(0, slime.method_7152() - 1);
        }
        return null;
    }

    @Nullable
    public static class_2960 resolveVariantModel(class_1309 entity, @Nullable class_2960 baseModelId) {
        String variant = AfwEntityVariants.resolveVariant((class_1297)entity);
        if (variant == null) {
            return null;
        }
        return AfwEntityVariants.variantModelId(baseModelId, variant);
    }

    @Nullable
    public static class_2960 variantModelId(@Nullable class_2960 baseModelId, @Nullable String variant) {
        if (baseModelId == null || variant == null || variant.isBlank()) {
            return null;
        }
        return class_2960.method_60655((String)baseModelId.method_12836(), (String)(baseModelId.method_12832() + "_" + variant));
    }
}

