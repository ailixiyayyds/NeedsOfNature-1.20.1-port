/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10042
 *  net.minecraft.class_11659
 *  net.minecraft.class_2960
 *  net.minecraft.class_3532
 *  net.minecraft.class_4608
 *  net.minecraft.class_5617$class_5618
 *  net.minecraft.class_7833
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaternionfc
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.renderer.base.RenderPassInfo
 */
package com.nonid.client;

import com.nonid.client.PregnancyEggGeoModel;
import com.nonid.entity.PregnancyEggEntity;
import java.util.Objects;
import net.minecraft.class_10042;
import net.minecraft.class_11659;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_4608;
import net.minecraft.class_5617;
import net.minecraft.class_7833;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionfc;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

public final class PregnancyEggRenderer<R extends class_10042>
extends GeoEntityRenderer<PregnancyEggEntity, R> {
    public PregnancyEggRenderer(class_5617.class_5618 context) {
        super(context, (GeoModel)new PregnancyEggGeoModel());
    }

    public void addRenderData(PregnancyEggEntity animatable, Void relatedObject, R renderState, float partialTick) {
        super.addRenderData((GeoAnimatable)animatable, (Object)relatedObject, renderState, partialTick);
        renderState.addGeckolibData(PregnancyEggGeoModel.TARGET_ENTITY_TYPE_TICKET, (Object)Objects.requireNonNull(animatable.getTargetEntityTypeId()));
        renderState.addGeckolibData(PregnancyEggGeoModel.GROWTH_SCALE_TICKET, (Object)Float.valueOf(animatable.getGrowthScale(partialTick)));
        renderState.addGeckolibData(PregnancyEggGeoModel.HURT_SHAKE_TICKET, (Object)Float.valueOf(Math.max(0.0f, (float)animatable.field_6235 - partialTick)));
        class_2960 eggTextureId = animatable.getEggTextureId();
        renderState.addGeckolibData(PregnancyEggGeoModel.EGG_TEXTURE_TICKET, (Object)(eggTextureId == null ? PregnancyEggGeoModel.DEFAULT_TEXTURE_ID : eggTextureId));
    }

    public void scaleModelForRender(RenderPassInfo<@NotNull R> renderPassInfo, float widthScale, float heightScale) {
        float growth = ((Float)renderPassInfo.getOrDefaultGeckolibData(PregnancyEggGeoModel.GROWTH_SCALE_TICKET, (Object)Float.valueOf(1.0f))).floatValue();
        float clamped = class_3532.method_15363((float)growth, (float)0.05f, (float)4.0f);
        super.scaleModelForRender(renderPassInfo, widthScale * clamped, heightScale * clamped);
    }

    public void preRenderPass(RenderPassInfo<@NotNull R> renderPassInfo, class_11659 renderTasks) {
        super.preRenderPass(renderPassInfo, renderTasks);
        float hurtShake = ((Float)renderPassInfo.getOrDefaultGeckolibData(PregnancyEggGeoModel.HURT_SHAKE_TICKET, (Object)Float.valueOf(0.0f))).floatValue();
        if (hurtShake <= 0.0f) {
            return;
        }
        float intensity = class_3532.method_15363((float)((hurtShake - 7.0f) / 3.0f), (float)0.0f, (float)1.0f);
        if (intensity <= 0.0f) {
            return;
        }
        double wobble = 0.004 * (double)intensity;
        float roll = 2.0f * intensity;
        renderPassInfo.poseStack().method_22904(wobble, 0.0, 0.0);
        renderPassInfo.poseStack().method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees(roll));
    }

    public int getPackedOverlay(PregnancyEggEntity animatable, Void relatedObject, float u, float partialTick) {
        return class_4608.field_21444;
    }

    protected float getDeathMaxRotation(@NotNull GeoRenderState renderState) {
        return 0.0f;
    }
}

