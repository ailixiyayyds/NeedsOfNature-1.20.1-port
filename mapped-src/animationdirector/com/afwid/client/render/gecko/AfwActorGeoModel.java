/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 */
package com.afwid.client.render.gecko;

import com.afwid.client.render.gecko.AfwActorAnimatable;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public final class AfwActorGeoModel
extends GeoModel<AfwActorAnimatable> {
    private static final ThreadLocal<Identifier> CURRENT_ANIMATION_RESOURCE = new ThreadLocal();
    private static final Identifier DEFAULT_MODEL = Identifier.of((String)"animationframework", (String)"entity/missingmodel");
    private static final Identifier DEFAULT_TEXTURE = Identifier.of((String)"animationframework", (String)"textures/missingmodel.png");
    private static final Identifier PLACEHOLDER_ANIMATIONS = Identifier.of((String)"animationframework", (String)"afw/placeholder");

    public static void setCurrentAnimationResource(Identifier resource) {
        CURRENT_ANIMATION_RESOURCE.set(resource);
    }

    public static void clearCurrentAnimationResource() {
        CURRENT_ANIMATION_RESOURCE.remove();
    }

    @NotNull
    public Identifier getModelResource(GeoRenderState renderState) {
        Identifier resource = (Identifier)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.MODEL_ID, (Object)DEFAULT_MODEL);
        return resource;
    }

    @NotNull
    public Identifier getTextureResource(GeoRenderState renderState) {
        Identifier resource = (Identifier)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.TEXTURE_ID, (Object)DEFAULT_TEXTURE);
        return resource;
    }

    @NotNull
    public Identifier getAnimationResource(AfwActorAnimatable animatable) {
        Identifier resource = CURRENT_ANIMATION_RESOURCE.get();
        return resource != null ? resource : PLACEHOLDER_ANIMATIONS;
    }
}

