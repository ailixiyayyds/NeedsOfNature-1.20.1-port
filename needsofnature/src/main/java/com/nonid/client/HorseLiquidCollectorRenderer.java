/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.LivingEntityRenderState
 *  net.minecraft.client.render.OverlayTexture
 *  net.minecraft.client.render.entity.EntityRendererFactory$Context
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 */
package com.nonid.client;

import com.nonid.client.HorseLiquidCollectorGeoModel;
import com.nonid.entity.HorseLiquidCollectorEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HorseLiquidCollectorRenderer
extends GeoEntityRenderer<HorseLiquidCollectorEntity> {
    public HorseLiquidCollectorRenderer(EntityRendererFactory.Context context) {
        super(context, new HorseLiquidCollectorGeoModel());
    }

    public int getPackedOverlay(HorseLiquidCollectorEntity animatable, float u, float partialTick) {
        return OverlayTexture.DEFAULT_UV;
    }

    protected float getDeathMaxRotation(HorseLiquidCollectorEntity animatable) {
        return 0.0f;
    }
}

