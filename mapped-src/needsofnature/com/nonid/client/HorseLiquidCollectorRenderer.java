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
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRendererFactory;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class HorseLiquidCollectorRenderer<R extends LivingEntityRenderState>
extends GeoEntityRenderer<HorseLiquidCollectorEntity, R> {
    public HorseLiquidCollectorRenderer(EntityRendererFactory.Context context) {
        super(context, (GeoModel)new HorseLiquidCollectorGeoModel());
    }

    public void addRenderData(HorseLiquidCollectorEntity animatable, Void relatedObject, R renderState, float partialTick) {
        super.addRenderData((GeoAnimatable)animatable, (Object)relatedObject, renderState, partialTick);
        renderState.addGeckolibData(HorseLiquidCollectorGeoModel.COLLECTOR_FULL_TICKET, (Object)animatable.isCollectorFull());
    }

    public int getPackedOverlay(HorseLiquidCollectorEntity animatable, Void relatedObject, float u, float partialTick) {
        return OverlayTexture.DEFAULT_UV;
    }

    protected float getDeathMaxRotation(@NotNull GeoRenderState renderState) {
        return 0.0f;
    }
}

