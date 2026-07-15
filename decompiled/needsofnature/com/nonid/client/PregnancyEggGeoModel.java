/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3300
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.cache.GeckoLibResources
 *  software.bernie.geckolib.constant.dataticket.DataTicket
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 */
package com.nonid.client;

import com.nonid.entity.PregnancyEggEntity;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.GeckoLibResources;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public final class PregnancyEggGeoModel
extends GeoModel<PregnancyEggEntity> {
    public static final DataTicket<@NotNull class_2960> TARGET_ENTITY_TYPE_TICKET = DataTicket.create((String)"non_pregnancy_egg_target_entity_type", class_2960.class);
    public static final DataTicket<@NotNull Float> GROWTH_SCALE_TICKET = DataTicket.create((String)"non_pregnancy_egg_growth_scale", Float.class);
    public static final DataTicket<@NotNull Float> HURT_SHAKE_TICKET = DataTicket.create((String)"non_pregnancy_egg_hurt_shake", Float.class);
    public static final DataTicket<@NotNull class_2960> EGG_TEXTURE_TICKET = DataTicket.create((String)"non_pregnancy_egg_texture", class_2960.class);
    public static final class_2960 DEFAULT_MODEL_ID = class_2960.method_60655((String)"needsofnature", (String)"entity/pregnancy_egg/default");
    public static final class_2960 DEFAULT_TEXTURE_ID = class_2960.method_60655((String)"needsofnature", (String)"textures/entity/pregnancy_egg/default.png");
    private static final class_2960 PLACEHOLDER_ANIMATION_ID = class_2960.method_60655((String)"animationframework", (String)"afw/placeholder");
    private static final Map<class_2960, Boolean> TEXTURE_EXISTS_CACHE = new ConcurrentHashMap<class_2960, Boolean>();

    @NotNull
    public class_2960 getModelResource(@NotNull GeoRenderState renderState) {
        class_2960 specific = PregnancyEggGeoModel.resolveSpecificModelId(renderState);
        if (specific != null && GeckoLibResources.getBakedModels().cache().containsKey(specific)) {
            return specific;
        }
        return DEFAULT_MODEL_ID;
    }

    @NotNull
    public class_2960 getTextureResource(@NotNull GeoRenderState renderState) {
        class_2960 configured = (class_2960)renderState.getGeckolibData(EGG_TEXTURE_TICKET);
        if (configured != null) {
            return PregnancyEggGeoModel.hasTexture(configured) ? configured : DEFAULT_TEXTURE_ID;
        }
        class_2960 specific = PregnancyEggGeoModel.resolveSpecificTextureId(renderState);
        if (specific != null && PregnancyEggGeoModel.hasTexture(specific)) {
            return specific;
        }
        return DEFAULT_TEXTURE_ID;
    }

    @NotNull
    public class_2960 getAnimationResource(PregnancyEggEntity animatable) {
        return PLACEHOLDER_ANIMATION_ID;
    }

    private static class_2960 resolveSpecificModelId(GeoRenderState renderState) {
        class_2960 targetType = (class_2960)renderState.getGeckolibData(TARGET_ENTITY_TYPE_TICKET);
        if (targetType == null) {
            return null;
        }
        return class_2960.method_60655((String)"needsofnature", (String)("entity/pregnancy_egg/" + PregnancyEggGeoModel.toFileStem(targetType)));
    }

    private static class_2960 resolveSpecificTextureId(GeoRenderState renderState) {
        class_2960 targetType = (class_2960)renderState.getGeckolibData(TARGET_ENTITY_TYPE_TICKET);
        if (targetType == null) {
            return null;
        }
        return class_2960.method_60655((String)"needsofnature", (String)("textures/entity/pregnancy_egg/" + PregnancyEggGeoModel.toFileStem(targetType) + ".png"));
    }

    private static String toFileStem(class_2960 typeId) {
        String namespace = typeId.method_12836();
        String path = typeId.method_12832().replace('/', '_');
        return namespace + "_" + path;
    }

    private static boolean hasTexture(class_2960 textureId) {
        if (textureId == null) {
            return false;
        }
        return TEXTURE_EXISTS_CACHE.computeIfAbsent(textureId, PregnancyEggGeoModel::findTexture);
    }

    private static boolean findTexture(class_2960 textureId) {
        class_310 client = class_310.method_1551();
        if (client == null) {
            return false;
        }
        class_3300 manager = client.method_1478();
        if (manager == null) {
            return false;
        }
        return manager.method_14486(textureId).isPresent();
    }
}

