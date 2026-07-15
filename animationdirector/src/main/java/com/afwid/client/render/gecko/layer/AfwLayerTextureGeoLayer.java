package com.afwid.client.render.gecko.layer;

import com.afwid.client.render.gecko.AfwActorAnimatable;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/** Additional translucent whole-model textures for GeckoLib 4. */
public final class AfwLayerTextureGeoLayer extends GeoRenderLayer<AfwActorAnimatable> {
    public AfwLayerTextureGeoLayer(GeoRenderer<AfwActorAnimatable> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrices, AfwActorAnimatable animatable,
                       BakedGeoModel bakedModel, RenderLayer baseRenderLayer,
                       VertexConsumerProvider vertices, VertexConsumer baseBuffer,
                       float tickDelta, int packedLight, int packedOverlay) {
        AfwActorAnimatable.RenderContext context = animatable.context();
        if (context == null) {
            return;
        }
        for (Identifier texture : context.layerTextures()) {
            if (texture == null) {
                continue;
            }
            RenderLayer layer = RenderLayer.getEntityTranslucent(texture);
            getRenderer().reRender(bakedModel, matrices, vertices, animatable, layer,
                    vertices.getBuffer(layer), tickDelta, packedLight, packedOverlay,
                    1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
