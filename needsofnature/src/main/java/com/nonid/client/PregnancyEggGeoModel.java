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
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.model.GeoModel;

public final class PregnancyEggGeoModel
extends GeoModel<PregnancyEggEntity> {
    public static final Identifier DEFAULT_MODEL_ID = new Identifier("needsofnature", "entity/pregnancy_egg/default");
    public static final Identifier DEFAULT_TEXTURE_ID = new Identifier("needsofnature", "textures/entity/pregnancy_egg/default.png");
    private static final Identifier PLACEHOLDER_ANIMATION_ID = new Identifier("animationframework", "afw/placeholder");
    private static final Map<Identifier, Boolean> TEXTURE_EXISTS_CACHE = new ConcurrentHashMap<Identifier, Boolean>();

    public Identifier getModelResource(PregnancyEggEntity animatable) {
        Identifier specific = PregnancyEggGeoModel.resolveSpecificModelId(animatable);
        if (specific != null && GeckoLibCache.getBakedModels().containsKey(specific)) {
            return specific;
        }
        return DEFAULT_MODEL_ID;
    }

    public Identifier getTextureResource(PregnancyEggEntity animatable) {
        Identifier configured = animatable.getEggTextureId();
        if (configured != null) {
            return PregnancyEggGeoModel.hasTexture(configured) ? configured : DEFAULT_TEXTURE_ID;
        }
        Identifier specific = PregnancyEggGeoModel.resolveSpecificTextureId(animatable);
        if (specific != null && PregnancyEggGeoModel.hasTexture(specific)) {
            return specific;
        }
        return DEFAULT_TEXTURE_ID;
    }

    public Identifier getAnimationResource(PregnancyEggEntity animatable) {
        return PLACEHOLDER_ANIMATION_ID;
    }

    private static Identifier resolveSpecificModelId(PregnancyEggEntity animatable) {
        Identifier targetType = animatable.getTargetEntityTypeId();
        if (targetType == null) {
            return null;
        }
        return new Identifier("needsofnature", "entity/pregnancy_egg/" + PregnancyEggGeoModel.toFileStem(targetType));
    }

    private static Identifier resolveSpecificTextureId(PregnancyEggEntity animatable) {
        Identifier targetType = animatable.getTargetEntityTypeId();
        if (targetType == null) {
            return null;
        }
        return new Identifier("needsofnature", "textures/entity/pregnancy_egg/" + PregnancyEggGeoModel.toFileStem(targetType) + ".png");
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

