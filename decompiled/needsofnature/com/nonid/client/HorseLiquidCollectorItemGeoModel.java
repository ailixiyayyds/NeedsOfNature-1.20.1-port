/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 */
package com.nonid.client;

import com.nonid.client.HorseLiquidCollectorGeoModel;
import com.nonid.item.HorseLiquidCollectorItem;
import net.minecraft.class_2960;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class HorseLiquidCollectorItemGeoModel
extends GeoModel<HorseLiquidCollectorItem> {
    private static final class_2960 PLACEHOLDER_ANIMATION_ID = class_2960.method_60655((String)"animationframework", (String)"afw/placeholder");

    @NotNull
    public class_2960 getModelResource(@NotNull GeoRenderState renderState) {
        return HorseLiquidCollectorGeoModel.MODEL_ID;
    }

    @NotNull
    public class_2960 getTextureResource(@NotNull GeoRenderState renderState) {
        return HorseLiquidCollectorGeoModel.EMPTY_TEXTURE_ID;
    }

    @NotNull
    public class_2960 getAnimationResource(HorseLiquidCollectorItem animatable) {
        return PLACEHOLDER_ANIMATION_ID;
    }
}

