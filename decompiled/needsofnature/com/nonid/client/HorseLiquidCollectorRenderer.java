/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10042
 *  net.minecraft.class_4608
 *  net.minecraft.class_5617$class_5618
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 */
package com.nonid.client;

import com.nonid.client.HorseLiquidCollectorGeoModel;
import com.nonid.entity.HorseLiquidCollectorEntity;
import net.minecraft.class_10042;
import net.minecraft.class_4608;
import net.minecraft.class_5617;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class HorseLiquidCollectorRenderer<R extends class_10042>
extends GeoEntityRenderer<HorseLiquidCollectorEntity, R> {
    public HorseLiquidCollectorRenderer(class_5617.class_5618 context) {
        super(context, (GeoModel)new HorseLiquidCollectorGeoModel());
    }

    public void addRenderData(HorseLiquidCollectorEntity animatable, Void relatedObject, R renderState, float partialTick) {
        super.addRenderData((GeoAnimatable)animatable, (Object)relatedObject, renderState, partialTick);
        renderState.addGeckolibData(HorseLiquidCollectorGeoModel.COLLECTOR_FULL_TICKET, (Object)animatable.isCollectorFull());
    }

    public int getPackedOverlay(HorseLiquidCollectorEntity animatable, Void relatedObject, float u, float partialTick) {
        return class_4608.field_21444;
    }

    protected float getDeathMaxRotation(@NotNull GeoRenderState renderState) {
        return 0.0f;
    }
}

