package com.afwid.client.render.gecko.layer;

import com.afwid.api.AfwGeckoModelEvents;
import com.afwid.client.render.gecko.AfwActorAnimatable;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

/** Renders AFW stage/packet item props on the animated model's prop bones. */
public final class AfwBoneItemGeoLayer extends BlockAndItemGeoLayer<AfwActorAnimatable> {
    public AfwBoneItemGeoLayer(GeoRenderer<AfwActorAnimatable> renderer) {
        super(renderer);
    }

    @Override
    protected ItemStack getStackForBone(GeoBone bone, AfwActorAnimatable animatable) {
        AfwGeckoModelEvents.BoneItemProp prop = findProp(bone, animatable);
        return prop == null ? null : prop.stack();
    }

    @Override
    protected ModelTransformationMode getTransformTypeForStack(
            GeoBone bone, ItemStack stack, AfwActorAnimatable animatable) {
        AfwGeckoModelEvents.BoneItemProp prop = findProp(bone, animatable);
        return prop == null ? ModelTransformationMode.NONE : prop.displayContext();
    }

    private static AfwGeckoModelEvents.BoneItemProp findProp(
            GeoBone bone, AfwActorAnimatable animatable) {
        if (bone == null || animatable == null || animatable.context() == null) {
            return null;
        }
        return animatable.context().boneItems().get(bone.getName());
    }
}
