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
import software.bernie.geckolib.model.GeoModel;

public class HorseLiquidCollectorItemGeoModel
extends GeoModel<HorseLiquidCollectorItem> {
    private static final Identifier PLACEHOLDER_ANIMATION_ID = new Identifier("animationframework", "afw/placeholder");

    public Identifier getModelResource(HorseLiquidCollectorItem animatable) {
        return HorseLiquidCollectorGeoModel.MODEL_ID;
    }

    public Identifier getTextureResource(HorseLiquidCollectorItem animatable) {
        return HorseLiquidCollectorGeoModel.EMPTY_TEXTURE_ID;
    }

    public Identifier getAnimationResource(HorseLiquidCollectorItem animatable) {
        return PLACEHOLDER_ANIMATION_ID;
    }
}

