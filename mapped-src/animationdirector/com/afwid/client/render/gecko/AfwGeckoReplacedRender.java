/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.EntityRenderState
 *  net.minecraft.client.render.entity.state.LivingEntityRenderState
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.client.render.state.CameraRenderState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.option.Perspective
 *  net.minecraft.client.render.entity.EntityRendererFactory$Context
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.entity.player.PlayerSkinType
 *  net.minecraft.entity.player.SkinTextures
 *  net.minecraft.client.render.entity.EntityRenderer
 *  net.minecraft.client.render.entity.EntityRenderManager
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
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRenderManager;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public final class AfwGeckoReplacedRender {
    private static final ConcurrentHashMap<Identifier, GeoReplacedEntityRenderer> RENDERERS = new ConcurrentHashMap();
    private static final Identifier UNKNOWN_RENDERER_KEY = Identifier.of((String)"animationframework", (String)"unknown_renderer");
    private static final Identifier PLAYER_TYPE_ID = Identifier.of((String)"minecraft", (String)"player");
    private static final String PLAYER_HEAD_BONE = "head";
    private static final float AFW_ACTIVE_MODEL_SCALE = 0.96f;
    private static final ConcurrentHashMap<Class<?>, Method> TEXTURE_METHOD_CACHE = new ConcurrentHashMap();
    private static final ConcurrentHashMap<Class<?>, Field> TEXTURE_FIELD_CACHE = new ConcurrentHashMap();
    private static final Set<String> LOGGED_TEXTURE_RESOLUTION_ERRORS = Collections.newSetFromMap(new ConcurrentHashMap());

    private AfwGeckoReplacedRender() {
    }

    private static GeoReplacedEntityRenderer getOrCreateRenderer(Identifier entityTypeId, Identifier modelId) {
        Identifier key = modelId != null ? modelId : (entityTypeId != null ? entityTypeId : UNKNOWN_RENDERER_KEY);
        GeoReplacedEntityRenderer existing = RENDERERS.get(key);
        if (existing != null) {
            return existing;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return null;
        }
        EntityRenderManager renderManager = client.getEntityRenderDispatcher();
        if (!(renderManager instanceof EntityRenderManagerAccessor)) {
            return null;
        }
        EntityRenderManagerAccessor accessor = (EntityRenderManagerAccessor)renderManager;
        EntityRendererFactory.Context context = new EntityRendererFactory.Context(renderManager, client.getItemModelManager(), client.getMapRenderer(), client.getBlockRenderManager(), client.getResourceManager(), client.getLoadedEntityModels(), accessor.afw$getEquipmentModelLoader(), client.getAtlasManager(), client.textRenderer, client.getPlayerSkinCache());
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
    public static void prepare(LivingEntity entity, LivingEntityRenderState state, float tickDelta) {
        AfwWildfireGenderState wildfireGenderState;
        GeoReplacedEntityRenderer renderer;
        Map<String, AfwGeckoModelEvents.BoneItemProp> runtimeBoneItems;
        AfwGeckoModelEvents.ModelOverride override;
        Identifier variantModelId;
        AbstractClientPlayerEntity playerEntity;
        if (!(state instanceof GeoRenderState)) {
            return;
        }
        LivingEntityRenderState geoState = state;
        geoState.addGeckolibData(AfwGeckoTickets.ACTOR_UUID, (Object)entity.getUuid());
        Identifier entityTypeId = ((AfwRenderStateAccess)state).afw$getEntityTypeId();
        Identifier afwAnimationId = AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining(entity.getUuid());
        UUID afwInstanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(entity.getUuid());
        Identifier animationResource = null;
        double animationSpeed = 1.0;
        if (afwAnimationId != null) {
            Double timelineSeconds;
            geoState.addGeckolibData(AfwGeckoTickets.AFW_ANIMATION_ID, (Object)afwAnimationId);
            if (afwInstanceId != null) {
                geoState.addGeckolibData(AfwGeckoTickets.AFW_INSTANCE_ID, (Object)afwInstanceId);
            }
            String actorKey = AfwClientAnimationRuntime.findLatestActiveActorKey(entity.getUuid());
            animationSpeed = AfwClientAnimationRuntime.findLatestSpeedForActor(entity.getUuid());
            animationResource = AfwGeckoResourceResolver.resolveAnimationResource(afwAnimationId, actorKey, entityTypeId);
            if (animationResource != null) {
                geoState.addGeckolibData(AfwGeckoTickets.AFW_ANIMATION_RESOURCE_ID, (Object)animationResource);
            }
            if ((timelineSeconds = AfwClientAnimationRuntime.findWarpedAuthoredTimelineSeconds(entity.getUuid(), tickDelta)) != null && Double.isFinite(timelineSeconds)) {
                geoState.addGeckolibData(AfwGeckoTickets.ANIMATION_TIMELINE_SECONDS, (Object)timelineSeconds);
            }
        }
        geoState.addGeckolibData(AfwGeckoTickets.ANIMATION_SPEED, (Object)animationSpeed);
        AfwGeckoResourceResolver.ModelAndTexture modelAndTex = AfwGeckoResourceResolver.resolveModelAndTexture(entityTypeId);
        Identifier modelId = modelAndTex.model();
        Identifier fallbackTexture = modelAndTex.texture();
        boolean missingModel = modelAndTex.missingModel();
        boolean explicitTextureOverride = false;
        if (entityTypeId != null && entityTypeId.equals((Object)PLAYER_TYPE_ID) && entity instanceof AbstractClientPlayerEntity && AfwGeckoReplacedRender.isSlimPlayerModel(playerEntity = (AbstractClientPlayerEntity)entity)) {
            Identifier slimModel = Identifier.of((String)"animationframework", (String)"entity/player_slim");
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
            Identifier overrideModel = override.model();
            Identifier overrideTexture = override.texture();
            if (overrideModel != null && AfwGeckoResourceResolver.hasGeoModel(overrideModel)) {
                modelId = overrideModel;
                missingModel = false;
            }
            if (overrideTexture != null) {
                fallbackTexture = overrideTexture;
                explicitTextureOverride = true;
            }
        }
        Identifier vanillaTexture = AfwGeckoReplacedRender.resolveVanillaTexture(entity, state);
        Identifier currentTexture = !missingModel && vanillaTexture != null && !explicitTextureOverride ? vanillaTexture : fallbackTexture;
        AfwGeckoModelEvents.RenderOverride renderOverride = ((AfwGeckoModelEvents.RenderResolver)AfwGeckoModelEvents.RESOLVE_RENDER.invoker()).resolve(entity, entityTypeId, modelId, currentTexture);
        List<Identifier> layerTextures = List.of();
        Map<String, Identifier> boneTextures = Map.of();
        Map<String, AfwGeckoModelEvents.BoneItemProp> boneItems = runtimeBoneItems = AfwClientAnimationRuntime.findLatestBoneItemsForActor(entity.getUuid());
        Map<String, Boolean> boneVisibility = Map.of();
        Map<String, Set<Integer>> hiddenBoneCubeIndices = Map.of();
        if (renderOverride != null) {
            Identifier overrideModel = renderOverride.model();
            Identifier overrideTexture = renderOverride.texture();
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
        Map<String, Identifier> resourceBoneTextures = AfwBoneTextureOverrides.getBoneTextures(modelId);
        boneTextures = AfwGeckoReplacedRender.mergeBoneTextures(resourceBoneTextures, boneTextures);
        List<Identifier> emissiveTextures = AfwGeckoReplacedRender.sanitizeLayerTextures(AfwBoneTextureOverrides.getEmissiveTextures(modelId));
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
        Identifier finalTexture = explicitTextureOverride ? fallbackTexture : (!missingModel && vanillaTexture != null ? vanillaTexture : fallbackTexture);
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

    private static List<Identifier> sanitizeLayerTextures(List<Identifier> textures) {
        if (textures == null || textures.isEmpty()) {
            return List.of();
        }
        ArrayList<Identifier> resolved = new ArrayList<Identifier>(textures.size());
        for (Identifier texture : textures) {
            if (texture == null) continue;
            resolved.add(AfwVanillaTextureResolver.resolveTexture(texture));
        }
        return resolved.isEmpty() ? List.of() : List.copyOf(resolved);
    }

    private static Map<String, Identifier> sanitizeBoneTextures(Map<String, Identifier> textures) {
        if (textures == null || textures.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, Identifier> resolved = new LinkedHashMap<String, Identifier>();
        for (Map.Entry<String, Identifier> entry : textures.entrySet()) {
            String bone = entry.getKey();
            Identifier texture = entry.getValue();
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
            if (bone == null || bone.isBlank() || prop == null || prop.stack().isEmpty()) continue;
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

    private static Map<String, Identifier> mergeBoneTextures(Map<String, Identifier> baseTextures, Map<String, Identifier> overrideTextures) {
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
        LinkedHashMap<String, Identifier> merged = new LinkedHashMap<String, Identifier>(baseTextures);
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

    private static boolean hasModelOrGenderedModel(Identifier modelId) {
        if (modelId == null) {
            return false;
        }
        if (AfwGeckoResourceResolver.hasGeoModel(modelId)) {
            return true;
        }
        String path = modelId.getPath();
        Identifier male = Identifier.of((String)modelId.getNamespace(), (String)(path + ".m"));
        if (AfwGeckoResourceResolver.hasGeoModel(male)) {
            return true;
        }
        Identifier female = Identifier.of((String)modelId.getNamespace(), (String)(path + ".f"));
        return AfwGeckoResourceResolver.hasGeoModel(female);
    }

    private static boolean shouldHideLocalPlayerHeadInFirstPerson(LivingEntity entity) {
        if (!(entity instanceof AbstractClientPlayerEntity)) {
            return false;
        }
        AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity)entity;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.options == null) {
            return false;
        }
        if (!playerEntity.getUuid().equals(client.player.getUuid())) {
            return false;
        }
        if (client.options.getPerspective() != Perspective.FIRST_PERSON) {
            return false;
        }
        return AfwClientAnimationRuntime.isActorPendingOrActive(playerEntity.getUuid());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean render(LivingEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState camera) {
        Identifier modelId;
        GeoReplacedEntityRenderer renderer;
        if (!(state instanceof GeoRenderState)) {
            return false;
        }
        LivingEntityRenderState geoState = state;
        Identifier entityTypeId = null;
        if (state instanceof AfwRenderStateAccess) {
            AfwRenderStateAccess access = (AfwRenderStateAccess)state;
            entityTypeId = access.afw$getEntityTypeId();
        }
        if ((renderer = AfwGeckoReplacedRender.getOrCreateRenderer(entityTypeId, modelId = (Identifier)geoState.getGeckolibData(AfwGeckoTickets.MODEL_ID))) == null) {
            return false;
        }
        try {
            matrices.push();
            try {
                matrices.scale(0.96f, 0.96f, 0.96f);
                renderer.performRenderPass((GeoRenderState)geoState, matrices, queue, camera);
            }
            finally {
                matrices.pop();
            }
            return true;
        }
        catch (RuntimeException e) {
            AnimationFramework.LOGGER.error("[AFW] GeckoLib render failed", (Throwable)e);
            return false;
        }
    }

    private static Identifier resolveVanillaTexture(LivingEntity entity, LivingEntityRenderState renderState) {
        block17: {
            MinecraftClient client;
            block16: {
                client = MinecraftClient.getInstance();
                if (client == null) {
                    return null;
                }
                if (entity instanceof AbstractClientPlayerEntity) {
                    AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity)entity;
                    try {
                        Identifier skinTexture;
                        SkinTextures skin = playerEntity.getSkin();
                        if (skin != null && skin.comp_1626() != null && (skinTexture = skin.comp_1626().comp_3627()) != null) {
                            return skinTexture;
                        }
                    }
                    catch (RuntimeException ex) {
                        String key = "player|" + playerEntity.getUuidAsString();
                        if (!LOGGED_TEXTURE_RESOLUTION_ERRORS.add(key)) break block16;
                        AnimationFramework.LOGGER.debug("[AFW] Failed to resolve player skin texture for {}", (Object)key, (Object)ex);
                    }
                }
            }
            try {
                Identifier id;
                Object val;
                Field f;
                Method m;
                EntityRenderManager renderManager = client.getEntityRenderDispatcher();
                EntityRenderer<?, ?> vanillaRenderer = null;
                if (renderManager instanceof EntityRenderManagerAccessor) {
                    EntityRenderManagerAccessor accessor = (EntityRenderManagerAccessor)renderManager;
                    vanillaRenderer = accessor.afw$getRenderer((EntityRenderState)renderState);
                }
                if (vanillaRenderer == null) {
                    vanillaRenderer = renderManager.getRenderer((Entity)entity);
                }
                if (vanillaRenderer != null && (m = TEXTURE_METHOD_CACHE.computeIfAbsent(vanillaRenderer.getClass(), cls -> {
                    Method found = AfwGeckoReplacedRender.findTextureMethod(cls.getDeclaredMethods(), renderState, entity);
                    if (found != null) {
                        return found;
                    }
                    return AfwGeckoReplacedRender.findTextureMethod(cls.getMethods(), renderState, entity);
                })) != null) {
                    Object result;
                    LivingEntityRenderState arg = null;
                    if (m.getParameterCount() == 1) {
                        Class<?> param = m.getParameterTypes()[0];
                        if (param.isAssignableFrom(renderState.getClass())) {
                            arg = renderState;
                        } else if (param.isAssignableFrom(entity.getClass())) {
                            arg = entity;
                        }
                    }
                    Object object = result = m.getParameterCount() == 0 ? m.invoke(vanillaRenderer, new Object[0]) : m.invoke(vanillaRenderer, arg);
                    if (result instanceof Identifier) {
                        Identifier id2 = (Identifier)result;
                        return id2;
                    }
                }
                if ((f = TEXTURE_FIELD_CACHE.computeIfAbsent(renderState.getClass(), cls -> {
                    for (Class current = cls; current != null; current = current.getSuperclass()) {
                        for (Field field : current.getDeclaredFields()) {
                            if (!Identifier.class.isAssignableFrom(field.getType()) || Modifier.isStatic(field.getModifiers())) continue;
                            field.setAccessible(true);
                            return field;
                        }
                    }
                    return null;
                })) != null && (val = f.get(renderState)) instanceof Identifier && AfwGeckoReplacedRender.isUsableTextureId(client, id = (Identifier)val)) {
                    return id;
                }
            }
            catch (ReflectiveOperationException | RuntimeException ex) {
                String key = entity.getType().toString() + "|" + renderState.getClass().getName();
                if (!LOGGED_TEXTURE_RESOLUTION_ERRORS.add(key)) break block17;
                AnimationFramework.LOGGER.debug("[AFW] Failed to reflect vanilla texture for {}", (Object)key, (Object)ex);
            }
        }
        return null;
    }

    private static Method findTextureMethod(Method[] methods, LivingEntityRenderState renderState, LivingEntity entity) {
        block4: for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) || !Identifier.class.isAssignableFrom(method.getReturnType())) continue;
            int params = method.getParameterCount();
            switch (params) {
                case 1: {
                    Class<?> param = method.getParameterTypes()[0];
                    if (!param.isAssignableFrom(renderState.getClass()) && !param.isAssignableFrom(entity.getClass()) && !LivingEntity.class.isAssignableFrom(param) && !Entity.class.isAssignableFrom(param)) continue block4;
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

    private static boolean isUsableTextureId(MinecraftClient client, Identifier id) {
        if (id == null) {
            return false;
        }
        String path = id.getPath();
        if (path != null && (path.contains("textures/") || path.endsWith(".png"))) {
            return true;
        }
        try {
            return client.getResourceManager().getResource(id).isPresent();
        }
        catch (RuntimeException ex) {
            return false;
        }
    }

    private static boolean hasPlayerSlimModelAssets() {
        Identifier base = Identifier.of((String)"animationframework", (String)"entity/player_slim");
        if (AfwGeckoResourceResolver.hasGeoModel(base)) {
            return true;
        }
        Identifier male = Identifier.of((String)"animationframework", (String)"entity/player_slim.m");
        if (AfwGeckoResourceResolver.hasGeoModel(male)) {
            return true;
        }
        Identifier female = Identifier.of((String)"animationframework", (String)"entity/player_slim.f");
        return AfwGeckoResourceResolver.hasGeoModel(female);
    }

    private static boolean isSlimPlayerModel(AbstractClientPlayerEntity playerEntity) {
        if (playerEntity == null) {
            return false;
        }
        try {
            SkinTextures skin = playerEntity.getSkin();
            return skin != null && skin.comp_1629() == PlayerSkinType.SLIM;
        }
        catch (RuntimeException ex) {
            String key = "skin_model|" + playerEntity.getUuidAsString();
            if (LOGGED_TEXTURE_RESOLUTION_ERRORS.add(key)) {
                AnimationFramework.LOGGER.debug("[AFW] Failed to resolve skin model from client API for {}", (Object)key, (Object)ex);
            }
            return false;
        }
    }
}

