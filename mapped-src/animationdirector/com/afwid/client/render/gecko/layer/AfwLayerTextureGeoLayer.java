/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.client.render.RenderLayers
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.util.Identifier
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
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
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

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull OrderedRenderCommandQueue renderTasks) {
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
        for (Identifier texture : layerTextures) {
            RenderLayer renderLayer;
            if (texture == null || (renderLayer = RenderLayers.entityTranslucent((Identifier)texture)) == null) continue;
            renderTasks.submitCustom(renderPassInfo.poseStack(), renderLayer, (pose, vertexConsumer) -> {
                renderPassInfo.poseStack().push();
                renderPassInfo.poseStack().peek().copy(pose);
                renderPassInfo.renderPosed(() -> renderPassInfo.model().render(renderPassInfo, vertexConsumer, packedLight, packedOverlay, renderColor));
                renderPassInfo.poseStack().pop();
            });
        }
    }
}

