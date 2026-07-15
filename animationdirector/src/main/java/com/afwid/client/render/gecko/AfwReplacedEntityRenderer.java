package com.afwid.client.render.gecko;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

/** Direct-entity GeckoLib 4 renderer used on Minecraft 1.20.1. */
public final class AfwReplacedEntityRenderer
        extends GeoReplacedEntityRenderer<LivingEntity, AfwActorAnimatable> {

    public AfwReplacedEntityRenderer(EntityRendererFactory.Context context,
                                     AfwActorGeoModel model,
                                     AfwActorAnimatable animatable) {
        super(context, model, animatable);
    }

    @Override
    public long getInstanceId(AfwActorAnimatable animatable) {
        AfwActorAnimatable.RenderContext context = animatable.context();
        if (context == null || context.actorUuid() == null) {
            return super.getInstanceId(animatable);
        }
        return context.actorUuid().getMostSignificantBits()
                ^ context.actorUuid().getLeastSignificantBits();
    }

    @Override
    public RenderLayer getRenderType(AfwActorAnimatable animatable, Identifier texture,
                                     VertexConsumerProvider bufferSource, float partialTick) {
        AfwActorAnimatable.RenderContext context = animatable.context();
        if (context != null && context.translucent()) {
            return RenderLayer.getEntityTranslucent(texture);
        }
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
