/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.constant.dataticket.DataTicket
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 */
package com.nonid.client;

import com.nonid.entity.HorseLiquidCollectorEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class HorseLiquidCollectorGeoModel
extends GeoModel<HorseLiquidCollectorEntity> {
    public static final Identifier MODEL_ID = new Identifier("needsofnature", "geo/entity/horse_liquid_collector.geo.json");
    public static final Identifier EMPTY_TEXTURE_ID = new Identifier("needsofnature", "textures/entity/horse_liquid_collector/horse_liquid_collector_empty.png");
    public static final Identifier FULL_TEXTURE_ID = new Identifier("needsofnature", "textures/entity/horse_liquid_collector/horse_liquid_collector_full.png");
    private static final Identifier PLACEHOLDER_ANIMATION_ID = new Identifier("animationframework", "animations/afw/placeholder.animation.json");

    public Identifier getModelResource(HorseLiquidCollectorEntity animatable) {
        return MODEL_ID;
    }

    public Identifier getTextureResource(HorseLiquidCollectorEntity animatable) {
        return animatable.isCollectorFull() ? FULL_TEXTURE_ID : EMPTY_TEXTURE_ID;
    }

    public Identifier getAnimationResource(HorseLiquidCollectorEntity animatable) {
        return PLACEHOLDER_ANIMATION_ID;
    }
}

