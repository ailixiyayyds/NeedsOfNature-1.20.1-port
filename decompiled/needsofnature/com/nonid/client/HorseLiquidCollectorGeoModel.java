/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.constant.dataticket.DataTicket
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 */
package com.nonid.client;

import com.nonid.entity.HorseLiquidCollectorEntity;
import net.minecraft.class_2960;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class HorseLiquidCollectorGeoModel
extends GeoModel<HorseLiquidCollectorEntity> {
    public static final DataTicket<@NotNull Boolean> COLLECTOR_FULL_TICKET = DataTicket.create((String)"non_horse_liquid_collector_full", Boolean.class);
    public static final class_2960 MODEL_ID = class_2960.method_60655((String)"needsofnature", (String)"entity/horse_liquid_collector");
    public static final class_2960 EMPTY_TEXTURE_ID = class_2960.method_60655((String)"needsofnature", (String)"textures/entity/horse_liquid_collector/horse_liquid_collector_empty.png");
    public static final class_2960 FULL_TEXTURE_ID = class_2960.method_60655((String)"needsofnature", (String)"textures/entity/horse_liquid_collector/horse_liquid_collector_full.png");
    private static final class_2960 PLACEHOLDER_ANIMATION_ID = class_2960.method_60655((String)"animationframework", (String)"afw/placeholder");

    @NotNull
    public class_2960 getModelResource(@NotNull GeoRenderState renderState) {
        return MODEL_ID;
    }

    @NotNull
    public class_2960 getTextureResource(GeoRenderState renderState) {
        boolean full = (Boolean)renderState.getOrDefaultGeckolibData(COLLECTOR_FULL_TICKET, (Object)Boolean.FALSE);
        return full ? FULL_TEXTURE_ID : EMPTY_TEXTURE_ID;
    }

    @NotNull
    public class_2960 getAnimationResource(HorseLiquidCollectorEntity animatable) {
        return PLACEHOLDER_ANIMATION_ID;
    }
}

