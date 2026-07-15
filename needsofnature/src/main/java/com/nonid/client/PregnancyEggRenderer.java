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
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class PregnancyEggRenderer
extends GeoEntityRenderer<PregnancyEggEntity> {
    public PregnancyEggRenderer(EntityRendererFactory.Context context) {
        super(context, new PregnancyEggGeoModel());
    }

    public void preRender(MatrixStack matrices, PregnancyEggEntity animatable, BakedGeoModel model,
                          VertexConsumerProvider buffers, VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        float growth = animatable.getGrowthScale(partialTick);
        float clamped = MathHelper.clamp((float)growth, (float)0.05f, (float)4.0f);
        matrices.scale(clamped, clamped, clamped);
        float hurtShake = Math.max(0.0f, (float)animatable.hurtTime - partialTick);
        float intensity = MathHelper.clamp((hurtShake - 7.0f) / 3.0f, 0.0f, 1.0f);
        if (intensity > 0.0f) {
            matrices.translate(0.004 * intensity, 0.0, 0.0);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(2.0f * intensity));
        }
        super.preRender(matrices, animatable, model, buffers, buffer, isReRender, partialTick,
                packedLight, packedOverlay, red, green, blue, alpha);
    }

    public int getPackedOverlay(PregnancyEggEntity animatable, float u, float partialTick) {
        return OverlayTexture.DEFAULT_UV;
    }

    protected float getDeathMaxRotation(PregnancyEggEntity animatable) {
        return 0.0f;
    }
}

