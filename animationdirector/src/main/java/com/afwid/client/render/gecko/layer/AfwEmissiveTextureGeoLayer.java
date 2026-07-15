package com.afwid.client.render.gecko.layer;

import com.afwid.client.render.gecko.AfwActorAnimatable;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/** Full-bright overlay textures for GeckoLib 4. */
public final class AfwEmissiveTextureGeoLayer extends GeoRenderLayer<AfwActorAnimatable> {
    public AfwEmissiveTextureGeoLayer(GeoRenderer<AfwActorAnimatable> renderer) {
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
        for (Identifier texture : context.emissiveTextures()) {
            if (texture == null) {
                continue;
            }
            RenderLayer layer = RenderLayer.getEntityCutoutNoCullZOffset(texture);
            getRenderer().reRender(bakedModel, matrices, vertices, animatable, layer,
                    vertices.getBuffer(layer), tickDelta,
                    LightmapTextureManager.MAX_LIGHT_COORDINATE, packedOverlay,
                    1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
