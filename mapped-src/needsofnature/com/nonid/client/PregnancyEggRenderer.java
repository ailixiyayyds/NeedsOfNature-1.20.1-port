/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.LivingEntityRenderState
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.render.OverlayTexture
 *  net.minecraft.client.render.entity.EntityRendererFactory$Context
 *  net.minecraft.util.math.RotationAxis
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
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionfc;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

public final class PregnancyEggRenderer<R extends LivingEntityRenderState>
extends GeoEntityRenderer<PregnancyEggEntity, R> {
    public PregnancyEggRenderer(EntityRendererFactory.Context context) {
        super(context, (GeoModel)new PregnancyEggGeoModel());
    }

    public void addRenderData(PregnancyEggEntity animatable, Void relatedObject, R renderState, float partialTick) {
        super.addRenderData((GeoAnimatable)animatable, (Object)relatedObject, renderState, partialTick);
        renderState.addGeckolibData(PregnancyEggGeoModel.TARGET_ENTITY_TYPE_TICKET, (Object)Objects.requireNonNull(animatable.getTargetEntityTypeId()));
        renderState.addGeckolibData(PregnancyEggGeoModel.GROWTH_SCALE_TICKET, (Object)Float.valueOf(animatable.getGrowthScale(partialTick)));
        renderState.addGeckolibData(PregnancyEggGeoModel.HURT_SHAKE_TICKET, (Object)Float.valueOf(Math.max(0.0f, (float)animatable.hurtTime - partialTick)));
        Identifier eggTextureId = animatable.getEggTextureId();
        renderState.addGeckolibData(PregnancyEggGeoModel.EGG_TEXTURE_TICKET, (Object)(eggTextureId == null ? PregnancyEggGeoModel.DEFAULT_TEXTURE_ID : eggTextureId));
    }

    public void scaleModelForRender(RenderPassInfo<@NotNull R> renderPassInfo, float widthScale, float heightScale) {
        float growth = ((Float)renderPassInfo.getOrDefaultGeckolibData(PregnancyEggGeoModel.GROWTH_SCALE_TICKET, (Object)Float.valueOf(1.0f))).floatValue();
        float clamped = MathHelper.clamp((float)growth, (float)0.05f, (float)4.0f);
        super.scaleModelForRender(renderPassInfo, widthScale * clamped, heightScale * clamped);
    }

    public void preRenderPass(RenderPassInfo<@NotNull R> renderPassInfo, OrderedRenderCommandQueue renderTasks) {
        super.preRenderPass(renderPassInfo, renderTasks);
        float hurtShake = ((Float)renderPassInfo.getOrDefaultGeckolibData(PregnancyEggGeoModel.HURT_SHAKE_TICKET, (Object)Float.valueOf(0.0f))).floatValue();
        if (hurtShake <= 0.0f) {
            return;
        }
        float intensity = MathHelper.clamp((float)((hurtShake - 7.0f) / 3.0f), (float)0.0f, (float)1.0f);
        if (intensity <= 0.0f) {
            return;
        }
        double wobble = 0.004 * (double)intensity;
        float roll = 2.0f * intensity;
        renderPassInfo.poseStack().translate(wobble, 0.0, 0.0);
        renderPassInfo.poseStack().multiply((Quaternionfc)RotationAxis.POSITIVE_Z.rotationDegrees(roll));
    }

    public int getPackedOverlay(PregnancyEggEntity animatable, Void relatedObject, float u, float partialTick) {
        return OverlayTexture.DEFAULT_UV;
    }

    protected float getDeathMaxRotation(@NotNull GeoRenderState renderState) {
        return 0.0f;
    }
}

