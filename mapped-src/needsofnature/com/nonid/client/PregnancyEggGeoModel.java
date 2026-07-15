/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.resource.ResourceManager
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
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.GeckoLibResources;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public final class PregnancyEggGeoModel
extends GeoModel<PregnancyEggEntity> {
    public static final DataTicket<@NotNull Identifier> TARGET_ENTITY_TYPE_TICKET = DataTicket.create((String)"non_pregnancy_egg_target_entity_type", Identifier.class);
    public static final DataTicket<@NotNull Float> GROWTH_SCALE_TICKET = DataTicket.create((String)"non_pregnancy_egg_growth_scale", Float.class);
    public static final DataTicket<@NotNull Float> HURT_SHAKE_TICKET = DataTicket.create((String)"non_pregnancy_egg_hurt_shake", Float.class);
    public static final DataTicket<@NotNull Identifier> EGG_TEXTURE_TICKET = DataTicket.create((String)"non_pregnancy_egg_texture", Identifier.class);
    public static final Identifier DEFAULT_MODEL_ID = Identifier.of((String)"needsofnature", (String)"entity/pregnancy_egg/default");
    public static final Identifier DEFAULT_TEXTURE_ID = Identifier.of((String)"needsofnature", (String)"textures/entity/pregnancy_egg/default.png");
    private static final Identifier PLACEHOLDER_ANIMATION_ID = Identifier.of((String)"animationframework", (String)"afw/placeholder");
    private static final Map<Identifier, Boolean> TEXTURE_EXISTS_CACHE = new ConcurrentHashMap<Identifier, Boolean>();

    @NotNull
    public Identifier getModelResource(@NotNull GeoRenderState renderState) {
        Identifier specific = PregnancyEggGeoModel.resolveSpecificModelId(renderState);
        if (specific != null && GeckoLibResources.getBakedModels().cache().containsKey(specific)) {
            return specific;
        }
        return DEFAULT_MODEL_ID;
    }

    @NotNull
    public Identifier getTextureResource(@NotNull GeoRenderState renderState) {
        Identifier configured = (Identifier)renderState.getGeckolibData(EGG_TEXTURE_TICKET);
        if (configured != null) {
            return PregnancyEggGeoModel.hasTexture(configured) ? configured : DEFAULT_TEXTURE_ID;
        }
        Identifier specific = PregnancyEggGeoModel.resolveSpecificTextureId(renderState);
        if (specific != null && PregnancyEggGeoModel.hasTexture(specific)) {
            return specific;
        }
        return DEFAULT_TEXTURE_ID;
    }

    @NotNull
    public Identifier getAnimationResource(PregnancyEggEntity animatable) {
        return PLACEHOLDER_ANIMATION_ID;
    }

    private static Identifier resolveSpecificModelId(GeoRenderState renderState) {
        Identifier targetType = (Identifier)renderState.getGeckolibData(TARGET_ENTITY_TYPE_TICKET);
        if (targetType == null) {
            return null;
        }
        return Identifier.of((String)"needsofnature", (String)("entity/pregnancy_egg/" + PregnancyEggGeoModel.toFileStem(targetType)));
    }

    private static Identifier resolveSpecificTextureId(GeoRenderState renderState) {
        Identifier targetType = (Identifier)renderState.getGeckolibData(TARGET_ENTITY_TYPE_TICKET);
        if (targetType == null) {
            return null;
        }
        return Identifier.of((String)"needsofnature", (String)("textures/entity/pregnancy_egg/" + PregnancyEggGeoModel.toFileStem(targetType) + ".png"));
    }

    private static String toFileStem(Identifier typeId) {
        String namespace = typeId.getNamespace();
        String path = typeId.getPath().replace('/', '_');
        return namespace + "_" + path;
    }

    private static boolean hasTexture(Identifier textureId) {
        if (textureId == null) {
            return false;
        }
        return TEXTURE_EXISTS_CACHE.computeIfAbsent(textureId, PregnancyEggGeoModel::findTexture);
    }

    private static boolean findTexture(Identifier textureId) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return false;
        }
        ResourceManager manager = client.getResourceManager();
        if (manager == null) {
            return false;
        }
        return manager.getResource(textureId).isPresent();
    }
}

