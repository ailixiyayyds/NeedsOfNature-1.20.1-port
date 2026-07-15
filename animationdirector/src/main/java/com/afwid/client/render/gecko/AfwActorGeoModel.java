package com.afwid.client.render.gecko;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/** GeckoLib 4 model adapter; resources come from the current direct-render context. */
public final class AfwActorGeoModel extends GeoModel<AfwActorAnimatable> {
    @Override
    public Identifier getModelResource(AfwActorAnimatable animatable) {
        return animatable.modelResource();
    }

    @Override
    public Identifier getTextureResource(AfwActorAnimatable animatable) {
        return animatable.textureResource();
    }

    @Override
    public Identifier getAnimationResource(AfwActorAnimatable animatable) {
        return animatable.animationResource();
    }
}
