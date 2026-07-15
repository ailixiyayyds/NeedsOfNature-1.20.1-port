/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10017
 *  net.minecraft.class_10042
 *  net.minecraft.class_11659
 *  net.minecraft.class_12075
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_4587
 *  net.minecraft.class_5498
 *  net.minecraft.class_5617$class_5618
 *  net.minecraft.class_742
 *  net.minecraft.class_7920
 *  net.minecraft.class_8685
 *  net.minecraft.class_897
 *  net.minecraft.class_898
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.renderer.GeoReplacedEntityRenderer
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 */
package com.afwid.client.render.gecko;

import com.afwid.AnimationFramework;
import com.afwid.api.AfwGeckoModelEvents;
import com.afwid.client.compat.wildfire.AfwWildfireGenderCompat;
import com.afwid.client.compat.wildfire.AfwWildfireGenderState;
import com.afwid.client.render.AfwRenderStateAccess;
import com.afwid.client.render.gecko.AfwActorAnimatable;
import com.afwid.client.render.gecko.AfwActorGeoModel;
import com.afwid.client.render.gecko.AfwBoneTextureOverrides;
import com.afwid.client.render.gecko.AfwGeckoResourceResolver;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import com.afwid.client.render.gecko.AfwReplacedEntityRenderer;
import com.afwid.client.render.gecko.AfwVanillaTextureResolver;
import com.afwid.client.render.gecko.layer.AfwBoneItemGeoLayer;
import com.afwid.client.render.gecko.layer.AfwBoneTextureGeoLayer;
import com.afwid.client.render.gecko.layer.AfwCameraPoseGeoLayer;
import com.afwid.client.render.gecko.layer.AfwEmissiveTextureGeoLayer;
import com.afwid.client.render.gecko.layer.AfwLayerTextureGeoLayer;
import com.afwid.client.render.gecko.layer.AfwParticleKeyframeGeoLayer;
import com.afwid.client.render.gecko.layer.AfwWildfireGenderGeoLayer;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import com.afwid.mixin.client.EntityRenderManagerAccessor;
import com.afwid.util.AfwEntityVariants;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_10017;
import net.minecraft.class_10042;
import net.minecraft.class_11659;
import net.minecraft.class_12075;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_5498;
import net.minecraft.class_5617;
import net.minecraft.class_742;
import net.minecraft.class_7920;
import net.minecraft.class_8685;
import net.minecraft.class_897;
import net.minecraft.class_898;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public final class AfwGeckoReplacedRender {
    private static final ConcurrentHashMap<class_2960, GeoReplacedEntityRenderer> RENDERERS = new ConcurrentHashMap();
    private static final class_2960 UNKNOWN_RENDERER_KEY = class_2960.method_60655((String)"animationframework", (String)"unknown_renderer");
    private static final class_2960 PLAYER_TYPE_ID = class_2960.method_60655((String)"minecraft", (String)"player");
    private static final String PLAYER_HEAD_BONE = "head";
    private static final float AFW_ACTIVE_MODEL_SCALE = 0.96f;
    private static final ConcurrentHashMap<Class<?>, Method> TEXTURE_METHOD_CACHE = new ConcurrentHashMap();
    private static final ConcurrentHashMap<Class<?>, Field> TEXTURE_FIELD_CACHE = new ConcurrentHashMap();
    private static final Set<String> LOGGED_TEXTURE_RESOLUTION_ERRORS = Collections.newSetFromMap(new ConcurrentHashMap());

    private AfwGeckoReplacedRender() {
    }

    private static GeoReplacedEntityRenderer getOrCreateRenderer(class_2960 entityTypeId, class_2960 modelId) {
        class_2960 key = modelId != null ? modelId : (entityTypeId != null ? entityTypeId : UNKNOWN_RENDERER_KEY);
        GeoReplacedEntityRenderer existing = RENDERERS.get(key);
        if (existing != null) {
            return existing;
        }
        class_310 client = class_310.method_1551();
        if (client == null) {
            return null;
        }
        class_898 renderManager = client.method_1561();
        if (!(renderManager instanceof EntityRenderManagerAccessor)) {
            return null;
        }
        EntityRenderManagerAccessor accessor = (EntityRenderManagerAccessor)renderManager;
        class_5617.class_5618 context = new class_5617.class_5618(renderManager, client.method_65386(), client.method_61965(), client.method_1541(), client.method_1478(), client.method_31974(), accessor.afw$getEquipmentModelLoader(), client.method_72703(), client.field_1772, client.method_73362());
        AfwReplacedEntityRenderer created = new AfwReplacedEntityRenderer(context, new AfwActorGeoModel(), AfwActorAnimatable.INSTANCE);
        created.withRenderLayer(new AfwLayerTextureGeoLayer(created));
        created.withRenderLayer(new AfwBoneTextureGeoLayer(created));
        created.withRenderLayer(new AfwEmissiveTextureGeoLayer(created));
        created.withRenderLayer((GeoRenderLayer)new AfwBoneItemGeoLayer(created));
        created.withRenderLayer(new AfwParticleKeyframeGeoLayer(created));
        created.withRenderLayer(new AfwWildfireGenderGeoLayer(created));
        created.withRenderLayer(new AfwCameraPoseGeoLayer(created));
        created.fireCompileRenderLayersEvent();
        AfwReplacedEntityRenderer prior = RENDERERS.putIfAbsent(key, created);
        return prior != null ? prior : created;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void prepare(class_1309 entity, class_10042 state, float tickDelta) {
        AfwWildfireGenderState wildfireGenderState;
        GeoReplacedEntityRenderer renderer;
        Map<String, AfwGeckoModelEvents.BoneItemProp> runtimeBoneItems;
        AfwGeckoModelEvents.ModelOverride override;
        class_2960 variantModelId;
        class_742 playerEntity;
        if (!(state instanceof GeoRenderState)) {
            return;
        }
        class_10042 geoState = state;
        geoState.addGeckolibData(AfwGeckoTickets.ACTOR_UUID, (Object)entity.method_5667());
        class_2960 entityTypeId = ((AfwRenderStateAccess)state).afw$getEntityTypeId();
        class_2960 afwAnimationId = AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining(entity.method_5667());
        UUID afwInstanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(entity.method_5667());
        class_2960 animationResource = null;
        double animationSpeed = 1.0;
        if (afwAnimationId != null) {
            Double timelineSeconds;
            geoState.addGeckolibData(AfwGeckoTickets.AFW_ANIMATION_ID, (Object)afwAnimationId);
            if (afwInstanceId != null) {
                geoState.addGeckolibData(AfwGeckoTickets.AFW_INSTANCE_ID, (Object)afwInstanceId);
            }
            String actorKey = AfwClientAnimationRuntime.findLatestActiveActorKey(entity.method_5667());
            animationSpeed = AfwClientAnimationRuntime.findLatestSpeedForActor(entity.method_5667());
            animationResource = AfwGeckoResourceResolver.resolveAnimationResource(afwAnimationId, actorKey, entityTypeId);
            if (animationResource != null) {
                geoState.addGeckolibData(AfwGeckoTickets.AFW_ANIMATION_RESOURCE_ID, (Object)animationResource);
            }
            if ((timelineSeconds = AfwClientAnimationRuntime.findWarpedAuthoredTimelineSeconds(entity.method_5667(), tickDelta)) != null && Double.isFinite(timelineSeconds)) {
                geoState.addGeckolibData(AfwGeckoTickets.ANIMATION_TIMELINE_SECONDS, (Object)timelineSeconds);
            }
        }
        geoState.addGeckolibData(AfwGeckoTickets.ANIMATION_SPEED, (Object)animationSpeed);
        AfwGeckoResourceResolver.ModelAndTexture modelAndTex = AfwGeckoResourceResolver.resolveModelAndTexture(entityTypeId);
        class_2960 modelId = modelAndTex.model();
        class_2960 fallbackTexture = modelAndTex.texture();
        boolean missingModel = modelAndTex.missingModel();
        boolean explicitTextureOverride = false;
        if (entityTypeId != null && entityTypeId.equals((Object)PLAYER_TYPE_ID) && entity instanceof class_742 && AfwGeckoReplacedRender.isSlimPlayerModel(playerEntity = (class_742)entity)) {
            class_2960 slimModel = class_2960.method_60655((String)"animationframework", (String)"entity/player_slim");
            if (AfwGeckoReplacedRender.hasPlayerSlimModelAssets()) {
                modelId = slimModel;
                missingModel = false;
            }
        }
        if ((variantModelId = AfwEntityVariants.resolveVariantModel(entity, modelId)) != null && AfwGeckoReplacedRender.hasModelOrGenderedModel(variantModelId)) {
            modelId = variantModelId;
            boolean bl = missingModel = !AfwGeckoResourceResolver.hasGeoModel(variantModelId);
        }
        if ((override = ((AfwGeckoModelEvents.ModelResolver)AfwGeckoModelEvents.RESOLVE.invoker()).resolve(entity, entityTypeId, modelId, fallbackTexture)) != null) {
            class_2960 overrideModel = override.model();
            class_2960 overrideTexture = override.texture();
            if (overrideModel != null && AfwGeckoResourceResolver.hasGeoModel(overrideModel)) {
                modelId = overrideModel;
                missingModel = false;
            }
            if (overrideTexture != null) {
                fallbackTexture = overrideTexture;
                explicitTextureOverride = true;
            }
        }
        class_2960 vanillaTexture = AfwGeckoReplacedRender.resolveVanillaTexture(entity, state);
        class_2960 currentTexture = !missingModel && vanillaTexture != null && !explicitTextureOverride ? vanillaTexture : fallbackTexture;
        AfwGeckoModelEvents.RenderOverride renderOverride = ((AfwGeckoModelEvents.RenderResolver)AfwGeckoModelEvents.RESOLVE_RENDER.invoker()).resolve(entity, entityTypeId, modelId, currentTexture);
        List<class_2960> layerTextures = List.of();
        Map<String, class_2960> boneTextures = Map.of();
        Map<String, AfwGeckoModelEvents.BoneItemProp> boneItems = runtimeBoneItems = AfwClientAnimationRuntime.findLatestBoneItemsForActor(entity.method_5667());
        Map<String, Boolean> boneVisibility = Map.of();
        Map<String, Set<Integer>> hiddenBoneCubeIndices = Map.of();
        if (renderOverride != null) {
            class_2960 overrideModel = renderOverride.model();
            class_2960 overrideTexture = renderOverride.texture();
            if (overrideModel != null && AfwGeckoResourceResolver.hasGeoModel(overrideModel)) {
                modelId = overrideModel;
                missingModel = false;
            }
            if (overrideTexture != null) {
                fallbackTexture = overrideTexture;
                explicitTextureOverride = true;
            }
            layerTextures = renderOverride.layerTextures();
            boneTextures = renderOverride.boneTextures();
            boneItems = AfwGeckoReplacedRender.mergeBoneItems(runtimeBoneItems, renderOverride.boneItems());
            boneVisibility = renderOverride.boneVisibility();
            hiddenBoneCubeIndices = renderOverride.hiddenBoneCubeIndices();
        }
        if (modelId == null || !AfwGeckoResourceResolver.hasGeoModel(modelId)) {
            AfwGeckoResourceResolver.ModelAndTexture missing = AfwGeckoResourceResolver.missingModelFallback(entityTypeId);
            modelId = missing.model();
            fallbackTexture = missing.texture();
            missingModel = true;
        }
        Map<String, class_2960> resourceBoneTextures = AfwBoneTextureOverrides.getBoneTextures(modelId);
        boneTextures = AfwGeckoReplacedRender.mergeBoneTextures(resourceBoneTextures, boneTextures);
        List<class_2960> emissiveTextures = AfwGeckoReplacedRender.sanitizeLayerTextures(AfwBoneTextureOverrides.getEmissiveTextures(modelId));
        layerTextures = AfwGeckoReplacedRender.sanitizeLayerTextures(layerTextures);
        boneTextures = AfwGeckoReplacedRender.sanitizeBoneTextures(boneTextures);
        boneItems = AfwGeckoReplacedRender.sanitizeBoneItems(boneItems);
        boneVisibility = AfwGeckoReplacedRender.sanitizeBoneVisibility(boneVisibility);
        hiddenBoneCubeIndices = AfwGeckoReplacedRender.sanitizeHiddenBoneCubeIndices(hiddenBoneCubeIndices);
        if (AfwGeckoReplacedRender.shouldHideLocalPlayerHeadInFirstPerson(entity)) {
            boneVisibility = AfwGeckoReplacedRender.forceHiddenBone(boneVisibility, PLAYER_HEAD_BONE);
        }
        if ((renderer = AfwGeckoReplacedRender.getOrCreateRenderer(entityTypeId, modelId)) == null) {
            return;
        }
        geoState.addGeckolibData(AfwGeckoTickets.MODEL_ID, (Object)modelId);
        class_2960 finalTexture = explicitTextureOverride ? fallbackTexture : (!missingModel && vanillaTexture != null ? vanillaTexture : fallbackTexture);
        finalTexture = AfwVanillaTextureResolver.resolveTexture(finalTexture);
        geoState.addGeckolibData(AfwGeckoTickets.TEXTURE_ID, (Object)finalTexture);
        if (PLAYER_TYPE_ID.equals((Object)entityTypeId) && (wildfireGenderState = AfwWildfireGenderCompat.resolve(entity, state)) != null) {
            geoState.addGeckolibData(AfwGeckoTickets.WILDFIRE_GENDER_STATE, (Object)wildfireGenderState);
        }
        geoState.addGeckolibData(AfwGeckoTickets.LAYER_TEXTURES, layerTextures);
        geoState.addGeckolibData(AfwGeckoTickets.EMISSIVE_TEXTURES, emissiveTextures);
        geoState.addGeckolibData(AfwGeckoTickets.BONE_TEXTURES, boneTextures);
        geoState.addGeckolibData(AfwGeckoTickets.BONE_ITEMS, boneItems);
        geoState.addGeckolibData(AfwGeckoTickets.BONE_VISIBILITY, boneVisibility);
        geoState.addGeckolibData(AfwGeckoTickets.HIDDEN_BONE_CUBE_INDICES, hiddenBoneCubeIndices);
        geoState.addGeckolibData(AfwGeckoTickets.TRANSLUCENT_RENDER, (Object)AfwBoneTextureOverrides.isTranslucent(modelId));
        try {
            if (animationResource != null) {
                AfwActorGeoModel.setCurrentAnimationResource(animationResource);
            }
            renderer.fillRenderState((GeoAnimatable)AfwActorAnimatable.INSTANCE, (Object)entity, (GeoRenderState)geoState, tickDelta);
        }
        catch (RuntimeException e) {
            AnimationFramework.LOGGER.error("[AFW] GeckoLib prepare failed", (Throwable)e);
        }
        finally {
            AfwActorGeoModel.clearCurrentAnimationResource();
        }
    }

    private static List<class_2960> sanitizeLayerTextures(List<class_2960> textures) {
        if (textures == null || textures.isEmpty()) {
            return List.of();
        }
        ArrayList<class_2960> resolved = new ArrayList<class_2960>(textures.size());
        for (class_2960 texture : textures) {
            if (texture == null) continue;
            resolved.add(AfwVanillaTextureResolver.resolveTexture(texture));
        }
        return resolved.isEmpty() ? List.of() : List.copyOf(resolved);
    }

    private static Map<String, class_2960> sanitizeBoneTextures(Map<String, class_2960> textures) {
        if (textures == null || textures.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, class_2960> resolved = new LinkedHashMap<String, class_2960>();
        for (Map.Entry<String, class_2960> entry : textures.entrySet()) {
            String bone = entry.getKey();
            class_2960 texture = entry.getValue();
            if (bone == null || bone.isBlank() || texture == null) continue;
            resolved.put(bone, AfwVanillaTextureResolver.resolveTexture(texture));
        }
        return resolved.isEmpty() ? Map.of() : Map.copyOf(resolved);
    }

    private static Map<String, AfwGeckoModelEvents.BoneItemProp> sanitizeBoneItems(Map<String, AfwGeckoModelEvents.BoneItemProp> items) {
        if (items == null || items.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, AfwGeckoModelEvents.BoneItemProp> resolved = new LinkedHashMap<String, AfwGeckoModelEvents.BoneItemProp>();
        for (Map.Entry<String, AfwGeckoModelEvents.BoneItemProp> entry : items.entrySet()) {
            String bone = entry.getKey();
            AfwGeckoModelEvents.BoneItemProp prop = entry.getValue();
            if (bone == null || bone.isBlank() || prop == null || prop.stack().method_7960()) continue;
            resolved.put(bone, new AfwGeckoModelEvents.BoneItemProp(prop.stack(), prop.displayContext()));
        }
        return resolved.isEmpty() ? Map.of() : Map.copyOf(resolved);
    }

    private static Map<String, AfwGeckoModelEvents.BoneItemProp> mergeBoneItems(Map<String, AfwGeckoModelEvents.BoneItemProp> baseItems, Map<String, AfwGeckoModelEvents.BoneItemProp> overrideItems) {
        boolean overrideEmpty;
        boolean baseEmpty = baseItems == null || baseItems.isEmpty();
        boolean bl = overrideEmpty = overrideItems == null || overrideItems.isEmpty();
        if (baseEmpty && overrideEmpty) {
            return Map.of();
        }
        if (baseEmpty) {
            return Map.copyOf(overrideItems);
        }
        if (overrideEmpty) {
            return Map.copyOf(baseItems);
        }
        LinkedHashMap<String, AfwGeckoModelEvents.BoneItemProp> merged = new LinkedHashMap<String, AfwGeckoModelEvents.BoneItemProp>(baseItems);
        merged.putAll(overrideItems);
        return Map.copyOf(merged);
    }

    private static Map<String, class_2960> mergeBoneTextures(Map<String, class_2960> baseTextures, Map<String, class_2960> overrideTextures) {
        boolean overrideEmpty;
        boolean baseEmpty = baseTextures == null || baseTextures.isEmpty();
        boolean bl = overrideEmpty = overrideTextures == null || overrideTextures.isEmpty();
        if (baseEmpty && overrideEmpty) {
            return Map.of();
        }
        if (baseEmpty) {
            return Map.copyOf(overrideTextures);
        }
        if (overrideEmpty) {
            return Map.copyOf(baseTextures);
        }
        LinkedHashMap<String, class_2960> merged = new LinkedHashMap<String, class_2960>(baseTextures);
        merged.putAll(overrideTextures);
        return Map.copyOf(merged);
    }

    private static Map<String, Boolean> sanitizeBoneVisibility(Map<String, Boolean> visibility) {
        if (visibility == null || visibility.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, Boolean> resolved = new LinkedHashMap<String, Boolean>();
        for (Map.Entry<String, Boolean> entry : visibility.entrySet()) {
            String bone = entry.getKey();
            Boolean visible = entry.getValue();
            if (bone == null || bone.isBlank() || visible == null) continue;
            resolved.put(bone, visible);
        }
        return resolved.isEmpty() ? Map.of() : Map.copyOf(resolved);
    }

    private static Map<String, Set<Integer>> sanitizeHiddenBoneCubeIndices(Map<String, Set<Integer>> hiddenIndices) {
        if (hiddenIndices == null || hiddenIndices.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap resolved = new LinkedHashMap();
        for (Map.Entry<String, Set<Integer>> entry : hiddenIndices.entrySet()) {
            String bone = entry.getKey();
            Set<Integer> indices = entry.getValue();
            if (bone == null || bone.isBlank() || indices == null || indices.isEmpty()) continue;
            LinkedHashSet<Integer> cleanIndices = new LinkedHashSet<Integer>();
            for (Integer index : indices) {
                if (index == null || index < 0) continue;
                cleanIndices.add(index);
            }
            if (cleanIndices.isEmpty()) continue;
            resolved.put(bone, Set.copyOf(cleanIndices));
        }
        return resolved.isEmpty() ? Map.of() : Map.copyOf(resolved);
    }

    private static Map<String, Boolean> forceHiddenBone(Map<String, Boolean> visibility, String boneName) {
        if (boneName == null || boneName.isBlank()) {
            return visibility;
        }
        if (visibility == null || visibility.isEmpty()) {
            return Map.of(boneName, Boolean.FALSE);
        }
        LinkedHashMap<String, Boolean> merged = new LinkedHashMap<String, Boolean>(visibility);
        merged.put(boneName, Boolean.FALSE);
        return Map.copyOf(merged);
    }

    private static boolean hasModelOrGenderedModel(class_2960 modelId) {
        if (modelId == null) {
            return false;
        }
        if (AfwGeckoResourceResolver.hasGeoModel(modelId)) {
            return true;
        }
        String path = modelId.method_12832();
        class_2960 male = class_2960.method_60655((String)modelId.method_12836(), (String)(path + ".m"));
        if (AfwGeckoResourceResolver.hasGeoModel(male)) {
            return true;
        }
        class_2960 female = class_2960.method_60655((String)modelId.method_12836(), (String)(path + ".f"));
        return AfwGeckoResourceResolver.hasGeoModel(female);
    }

    private static boolean shouldHideLocalPlayerHeadInFirstPerson(class_1309 entity) {
        if (!(entity instanceof class_742)) {
            return false;
        }
        class_742 playerEntity = (class_742)entity;
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null || client.field_1690 == null) {
            return false;
        }
        if (!playerEntity.method_5667().equals(client.field_1724.method_5667())) {
            return false;
        }
        if (client.field_1690.method_31044() != class_5498.field_26664) {
            return false;
        }
        return AfwClientAnimationRuntime.isActorPendingOrActive(playerEntity.method_5667());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean render(class_10042 state, class_4587 matrices, class_11659 queue, class_12075 camera) {
        class_2960 modelId;
        GeoReplacedEntityRenderer renderer;
        if (!(state instanceof GeoRenderState)) {
            return false;
        }
        class_10042 geoState = state;
        class_2960 entityTypeId = null;
        if (state instanceof AfwRenderStateAccess) {
            AfwRenderStateAccess access = (AfwRenderStateAccess)state;
            entityTypeId = access.afw$getEntityTypeId();
        }
        if ((renderer = AfwGeckoReplacedRender.getOrCreateRenderer(entityTypeId, modelId = (class_2960)geoState.getGeckolibData(AfwGeckoTickets.MODEL_ID))) == null) {
            return false;
        }
        try {
            matrices.method_22903();
            try {
                matrices.method_22905(0.96f, 0.96f, 0.96f);
                renderer.performRenderPass((GeoRenderState)geoState, matrices, queue, camera);
            }
            finally {
                matrices.method_22909();
            }
            return true;
        }
        catch (RuntimeException e) {
            AnimationFramework.LOGGER.error("[AFW] GeckoLib render failed", (Throwable)e);
            return false;
        }
    }

    private static class_2960 resolveVanillaTexture(class_1309 entity, class_10042 renderState) {
        block17: {
            class_310 client;
            block16: {
                client = class_310.method_1551();
                if (client == null) {
                    return null;
                }
                if (entity instanceof class_742) {
                    class_742 playerEntity = (class_742)entity;
                    try {
                        class_2960 skinTexture;
                        class_8685 skin = playerEntity.method_52814();
                        if (skin != null && skin.comp_1626() != null && (skinTexture = skin.comp_1626().comp_3627()) != null) {
                            return skinTexture;
                        }
                    }
                    catch (RuntimeException ex) {
                        String key = "player|" + playerEntity.method_5845();
                        if (!LOGGED_TEXTURE_RESOLUTION_ERRORS.add(key)) break block16;
                        AnimationFramework.LOGGER.debug("[AFW] Failed to resolve player skin texture for {}", (Object)key, (Object)ex);
                    }
                }
            }
            try {
                class_2960 id;
                Object val;
                Field f;
                Method m;
                class_898 renderManager = client.method_1561();
                class_897<?, ?> vanillaRenderer = null;
                if (renderManager instanceof EntityRenderManagerAccessor) {
                    EntityRenderManagerAccessor accessor = (EntityRenderManagerAccessor)renderManager;
                    vanillaRenderer = accessor.afw$getRenderer((class_10017)renderState);
                }
                if (vanillaRenderer == null) {
                    vanillaRenderer = renderManager.method_3953((class_1297)entity);
                }
                if (vanillaRenderer != null && (m = TEXTURE_METHOD_CACHE.computeIfAbsent(vanillaRenderer.getClass(), cls -> {
                    Method found = AfwGeckoReplacedRender.findTextureMethod(cls.getDeclaredMethods(), renderState, entity);
                    if (found != null) {
                        return found;
                    }
                    return AfwGeckoReplacedRender.findTextureMethod(cls.getMethods(), renderState, entity);
                })) != null) {
                    Object result;
                    class_10042 arg = null;
                    if (m.getParameterCount() == 1) {
                        Class<?> param = m.getParameterTypes()[0];
                        if (param.isAssignableFrom(renderState.getClass())) {
                            arg = renderState;
                        } else if (param.isAssignableFrom(entity.getClass())) {
                            arg = entity;
                        }
                    }
                    Object object = result = m.getParameterCount() == 0 ? m.invoke(vanillaRenderer, new Object[0]) : m.invoke(vanillaRenderer, arg);
                    if (result instanceof class_2960) {
                        class_2960 id2 = (class_2960)result;
                        return id2;
                    }
                }
                if ((f = TEXTURE_FIELD_CACHE.computeIfAbsent(renderState.getClass(), cls -> {
                    for (Class current = cls; current != null; current = current.getSuperclass()) {
                        for (Field field : current.getDeclaredFields()) {
                            if (!class_2960.class.isAssignableFrom(field.getType()) || Modifier.isStatic(field.getModifiers())) continue;
                            field.setAccessible(true);
                            return field;
                        }
                    }
                    return null;
                })) != null && (val = f.get(renderState)) instanceof class_2960 && AfwGeckoReplacedRender.isUsableTextureId(client, id = (class_2960)val)) {
                    return id;
                }
            }
            catch (ReflectiveOperationException | RuntimeException ex) {
                String key = entity.method_5864().toString() + "|" + renderState.getClass().getName();
                if (!LOGGED_TEXTURE_RESOLUTION_ERRORS.add(key)) break block17;
                AnimationFramework.LOGGER.debug("[AFW] Failed to reflect vanilla texture for {}", (Object)key, (Object)ex);
            }
        }
        return null;
    }

    private static Method findTextureMethod(Method[] methods, class_10042 renderState, class_1309 entity) {
        block4: for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) || !class_2960.class.isAssignableFrom(method.getReturnType())) continue;
            int params = method.getParameterCount();
            switch (params) {
                case 1: {
                    Class<?> param = method.getParameterTypes()[0];
                    if (!param.isAssignableFrom(renderState.getClass()) && !param.isAssignableFrom(entity.getClass()) && !class_1309.class.isAssignableFrom(param) && !class_1297.class.isAssignableFrom(param)) continue block4;
                    method.setAccessible(true);
                    return method;
                }
                case 0: {
                    method.setAccessible(true);
                    return method;
                }
            }
        }
        return null;
    }

    private static boolean isUsableTextureId(class_310 client, class_2960 id) {
        if (id == null) {
            return false;
        }
        String path = id.method_12832();
        if (path != null && (path.contains("textures/") || path.endsWith(".png"))) {
            return true;
        }
        try {
            return client.method_1478().method_14486(id).isPresent();
        }
        catch (RuntimeException ex) {
            return false;
        }
    }

    private static boolean hasPlayerSlimModelAssets() {
        class_2960 base = class_2960.method_60655((String)"animationframework", (String)"entity/player_slim");
        if (AfwGeckoResourceResolver.hasGeoModel(base)) {
            return true;
        }
        class_2960 male = class_2960.method_60655((String)"animationframework", (String)"entity/player_slim.m");
        if (AfwGeckoResourceResolver.hasGeoModel(male)) {
            return true;
        }
        class_2960 female = class_2960.method_60655((String)"animationframework", (String)"entity/player_slim.f");
        return AfwGeckoResourceResolver.hasGeoModel(female);
    }

    private static boolean isSlimPlayerModel(class_742 playerEntity) {
        if (playerEntity == null) {
            return false;
        }
        try {
            class_8685 skin = playerEntity.method_52814();
            return skin != null && skin.comp_1629() == class_7920.field_41122;
        }
        catch (RuntimeException ex) {
            String key = "skin_model|" + playerEntity.method_5845();
            if (LOGGED_TEXTURE_RESOLUTION_ERRORS.add(key)) {
                AnimationFramework.LOGGER.debug("[AFW] Failed to resolve skin model from client API for {}", (Object)key, (Object)ex);
            }
            return false;
        }
    }
}

