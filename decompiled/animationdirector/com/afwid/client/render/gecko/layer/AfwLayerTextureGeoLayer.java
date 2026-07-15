/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11659
 *  net.minecraft.class_12249
 *  net.minecraft.class_1921
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.renderer.base.GeoRenderer
 *  software.bernie.geckolib.renderer.base.RenderPassInfo
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 */
package com.afwid.client.render.gecko.layer;

import com.afwid.client.render.gecko.AfwGeckoTickets;
import java.util.List;
import net.minecraft.class_11659;
import net.minecraft.class_12249;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public final class AfwLayerTextureGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState>
extends GeoRenderLayer<T, O, R> {
    public AfwLayerTextureGeoLayer(GeoRenderer<@NotNull T, @NotNull O, @NotNull R> renderer) {
        super(renderer);
    }

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull class_11659 renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }
        List layerTextures = (List)renderPassInfo.renderState().getOrDefaultGeckolibData(AfwGeckoTickets.LAYER_TEXTURES, List.of());
        if (layerTextures.isEmpty()) {
            return;
        }
        int packedLight = renderPassInfo.packedLight();
        int packedOverlay = renderPassInfo.packedOverlay();
        int renderColor = -1;
        for (class_2960 texture : layerTextures) {
            class_1921 renderLayer;
            if (texture == null || (renderLayer = class_12249.method_76000((class_2960)texture)) == null) continue;
            renderTasks.method_73483(renderPassInfo.poseStack(), renderLayer, (pose, vertexConsumer) -> {
                renderPassInfo.poseStack().method_22903();
                renderPassInfo.poseStack().method_23760().method_66521(pose);
                renderPassInfo.renderPosed(() -> renderPassInfo.model().render(renderPassInfo, vertexConsumer, packedLight, packedOverlay, renderColor));
                renderPassInfo.poseStack().method_22909();
            });
        }
    }
}

