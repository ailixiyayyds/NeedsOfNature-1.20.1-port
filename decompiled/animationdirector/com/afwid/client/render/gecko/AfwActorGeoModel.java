/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 */
package com.afwid.client.render.gecko;

import com.afwid.client.render.gecko.AfwActorAnimatable;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import net.minecraft.class_2960;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public final class AfwActorGeoModel
extends GeoModel<AfwActorAnimatable> {
    private static final ThreadLocal<class_2960> CURRENT_ANIMATION_RESOURCE = new ThreadLocal();
    private static final class_2960 DEFAULT_MODEL = class_2960.method_60655((String)"animationframework", (String)"entity/missingmodel");
    private static final class_2960 DEFAULT_TEXTURE = class_2960.method_60655((String)"animationframework", (String)"textures/missingmodel.png");
    private static final class_2960 PLACEHOLDER_ANIMATIONS = class_2960.method_60655((String)"animationframework", (String)"afw/placeholder");

    public static void setCurrentAnimationResource(class_2960 resource) {
        CURRENT_ANIMATION_RESOURCE.set(resource);
    }

    public static void clearCurrentAnimationResource() {
        CURRENT_ANIMATION_RESOURCE.remove();
    }

    @NotNull
    public class_2960 getModelResource(GeoRenderState renderState) {
        class_2960 resource = (class_2960)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.MODEL_ID, (Object)DEFAULT_MODEL);
        return resource;
    }

    @NotNull
    public class_2960 getTextureResource(GeoRenderState renderState) {
        class_2960 resource = (class_2960)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.TEXTURE_ID, (Object)DEFAULT_TEXTURE);
        return resource;
    }

    @NotNull
    public class_2960 getAnimationResource(AfwActorAnimatable animatable) {
        class_2960 resource = CURRENT_ANIMATION_RESOURCE.get();
        return resource != null ? resource : PLACEHOLDER_ANIMATIONS;
    }
}

