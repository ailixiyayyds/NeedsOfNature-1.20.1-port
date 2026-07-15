/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.mob.SlimeEntity
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class AfwEntityVariants {
    private AfwEntityVariants() {
    }

    @Nullable
    public static String resolveVariant(Entity entity) {
        if (entity instanceof SlimeEntity) {
            SlimeEntity slime = (SlimeEntity)entity;
            return "size_" + Math.max(0, slime.getSize() - 1);
        }
        return null;
    }

    @Nullable
    public static Identifier resolveVariantModel(LivingEntity entity, @Nullable Identifier baseModelId) {
        String variant = AfwEntityVariants.resolveVariant((Entity)entity);
        if (variant == null) {
            return null;
        }
        return AfwEntityVariants.variantModelId(baseModelId, variant);
    }

    @Nullable
    public static Identifier variantModelId(@Nullable Identifier baseModelId, @Nullable String variant) {
        if (baseModelId == null || variant == null || variant.isBlank()) {
            return null;
        }
        return Identifier.of((String)baseModelId.getNamespace(), (String)(baseModelId.getPath() + "_" + variant));
    }
}

