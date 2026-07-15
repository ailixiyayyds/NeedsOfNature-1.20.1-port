/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 */
package com.nonid.client;

import com.nonid.client.HorseLiquidCollectorGeoModel;
import com.nonid.item.HorseLiquidCollectorItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class HorseLiquidCollectorItemGeoModel
extends GeoModel<HorseLiquidCollectorItem> {
    private static final Identifier PLACEHOLDER_ANIMATION_ID = Identifier.of((String)"animationframework", (String)"afw/placeholder");

    @NotNull
    public Identifier getModelResource(@NotNull GeoRenderState renderState) {
        return HorseLiquidCollectorGeoModel.MODEL_ID;
    }

    @NotNull
    public Identifier getTextureResource(@NotNull GeoRenderState renderState) {
        return HorseLiquidCollectorGeoModel.EMPTY_TEXTURE_ID;
    }

    @NotNull
    public Identifier getAnimationResource(HorseLiquidCollectorItem animatable) {
        return PLACEHOLDER_ANIMATION_ID;
    }
}

