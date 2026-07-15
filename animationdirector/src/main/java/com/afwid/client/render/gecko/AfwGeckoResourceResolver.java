/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  software.bernie.geckolib.GeckoLibConstants
 *  software.bernie.geckolib.cache.GeckoLibResources
 *  software.bernie.geckolib.loading.object.BakedAnimations
 */
package com.afwid.client.render.gecko;

import com.afwid.AfwDebugChatCategory;
import com.afwid.AnimationFramework;
import com.afwid.AnimationFrameworkClient;
import com.afwid.client.config.AfwClientConfig;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.loading.object.BakedAnimations;

public final class AfwGeckoResourceResolver {
    private static final String SAFETY_FALLBACK_ANIMATION_KEY = "animation";
    private static final Identifier DEFAULT_MISSING_MODEL = new Identifier((String)"animationframework", (String)"geo/entity/missingmodel.geo.json");
    private static final Identifier DEFAULT_MISSING_TEXTURE = new Identifier((String)"animationframework", (String)"textures/missingmodel.png");
    private static final Set<String> LOGGED_MISSING_TYPES = Collections.newSetFromMap(new ConcurrentHashMap());
    private static final Set<String> LOGGED_MISSING_ANIMATIONS = Collections.newSetFromMap(new ConcurrentHashMap());
    private static final Set<String> LOGGED_RESOURCE_LOOKUP_ERRORS = Collections.newSetFromMap(new ConcurrentHashMap());

    private AfwGeckoResourceResolver() {
    }

    public static Identifier toAnimationResource(Identifier afwAnimationId) {
        if (afwAnimationId == null) {
            return null;
        }
        return new Identifier((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), null));
    }

    public static Identifier toAnimationResource(Identifier afwAnimationId, String actorKey) {
        if (afwAnimationId == null) {
            return null;
        }
        return new Identifier((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), actorKey));
    }

    public static Identifier resolveAnimationResource(Identifier afwAnimationId, String actorKey, Identifier entityTypeId) {
        return AfwGeckoResourceResolver.resolveAnimationResource(afwAnimationId, actorKey, entityTypeId, true);
    }

    public static Identifier resolveAnimationResource(Identifier afwAnimationId, String actorKey, Identifier entityTypeId, boolean warnMissing) {
        String logKey;
        if (afwAnimationId == null) {
            return null;
        }
        Identifier actorCandidate = null;
        if (actorKey != null && !actorKey.isBlank() && AfwGeckoResourceResolver.hasBakedAnimation(actorCandidate = new Identifier((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), actorKey)))) {
            return actorCandidate;
        }
        Identifier typeCandidate = null;
        if (entityTypeId != null) {
            Identifier namespacedCandidate;
            String typeKey = entityTypeId.getPath();
            typeCandidate = new Identifier((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), typeKey));
            if (AfwGeckoResourceResolver.hasBakedAnimation(typeCandidate)) {
                return typeCandidate;
            }
            String namespacedKey = entityTypeId.getNamespace() + "_" + entityTypeId.getPath();
            if (!namespacedKey.equals(typeKey) && AfwGeckoResourceResolver.hasBakedAnimation(namespacedCandidate = new Identifier((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), namespacedKey)))) {
                return namespacedCandidate;
            }
        }
        Identifier base = new Identifier((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), null));
        boolean baseBaked = AfwGeckoResourceResolver.hasBakedAnimation(base);
        if (warnMissing && !baseBaked && (actorCandidate != null || typeCandidate != null) && AfwClientConfig.get().allowsDebugChat(AfwDebugChatCategory.SETUP) && LOGGED_MISSING_ANIMATIONS.add(logKey = String.valueOf(afwAnimationId) + "|actor=" + actorKey + "|type=" + String.valueOf(entityTypeId))) {
            String message = AnimationFramework.formatLogTemplate("[AFW] Missing per-actor animation for {} (actorKey={}, typeId={}); no base animation found.", afwAnimationId, actorKey, entityTypeId);
            AnimationFramework.LOGGER.warn("{}", (Object)message);
            AnimationFrameworkClient.sendClientSetupWarning(message);
        }
        AfwGeckoResourceResolver.hasBakedAnimation(base);
        return base;
    }

    public static boolean hasBakedAnimation(Identifier animationResource) {
        return animationResource != null && GeckoLibCache.getBakedAnimations().containsKey(animationResource);
    }

    public static String resolveAnimationKey(Identifier animationResource) {
        if (animationResource == null) {
            return SAFETY_FALLBACK_ANIMATION_KEY;
        }
        BakedAnimations baked = GeckoLibCache.getBakedAnimations().get(animationResource);
        if (baked == null || baked.animations().isEmpty()) {
            return SAFETY_FALLBACK_ANIMATION_KEY;
        }
        return (String)baked.animations().keySet().iterator().next();
    }

    public static ModelAndTexture resolveModelAndTexture(Identifier entityTypeId) {
        Identifier derivedTexture;
        if (entityTypeId == null) {
            return AfwGeckoResourceResolver.missingWithLog(null);
        }
        String entityPath = entityTypeId.getPath();
        Identifier primaryModel = new Identifier((String)"animationframework", (String)("entity/" + entityPath));
        Identifier secondaryModel = new Identifier((String)"animationframework", (String)entityPath);
        Identifier resolvedModel = AfwGeckoResourceResolver.resolveGeoModelResource(primaryModel);
        boolean hasModel = resolvedModel != null;
        if (!hasModel) {
            resolvedModel = AfwGeckoResourceResolver.resolveGeoModelResource(secondaryModel);
            hasModel = resolvedModel != null;
        }
        if (resolvedModel == null) {
            resolvedModel = AfwGeckoResourceResolver.geoModelPath(primaryModel);
        }
        Identifier resolvedTexture = AfwGeckoResourceResolver.resourceExists(derivedTexture = new Identifier((String)entityTypeId.getNamespace(), (String)("textures/entity/" + entityPath + "/" + entityPath + ".png"))) ? derivedTexture : DEFAULT_MISSING_TEXTURE;
        return new ModelAndTexture(resolvedModel, resolvedTexture, !hasModel);
    }

    public static ModelAndTexture missingModelFallback(Identifier typeId) {
        return AfwGeckoResourceResolver.missingWithLog(typeId);
    }

    public static boolean hasGeoModel(Identifier modelId) {
        return AfwGeckoResourceResolver.resolveGeoModelResource(modelId) != null;
    }

    /** Converts GeckoLib 5 logical model IDs to the complete paths required by GeckoLib 4. */
    public static Identifier resolveGeoModelResource(Identifier modelId) {
        if (modelId == null) {
            return null;
        }
        if (GeckoLibCache.getBakedModels().containsKey(modelId)
                || AfwGeckoResourceResolver.resourceExists(modelId)) {
            return modelId;
        }
        String path = modelId.getPath();
        if (AfwGeckoResourceResolver.isCompleteModelPath(path)) {
            return null;
        }
        Identifier legacyPath = AfwGeckoResourceResolver.geoModelPath(modelId);
        if (GeckoLibCache.getBakedModels().containsKey(legacyPath)
                || AfwGeckoResourceResolver.resourceExists(legacyPath)) {
            return legacyPath;
        }
        return null;
    }

    private static String nestedAnimationPath(String animationPath, String suffix) {
        String folder = AfwGeckoResourceResolver.animationFolderPath(animationPath);
        String file = suffix == null || suffix.isBlank() ? animationPath : animationPath + "_" + suffix;
        return "animations/afw/" + folder + "/" + file + ".animation.json";
    }

    private static String animationFolderPath(String animationPath) {
        if (animationPath == null || animationPath.isEmpty()) {
            return animationPath;
        }
        int stageIndex = animationPath.lastIndexOf(".p");
        if (stageIndex <= 0 || stageIndex + 2 >= animationPath.length()) {
            return animationPath;
        }
        for (int i = stageIndex + 2; i < animationPath.length(); ++i) {
            char c = animationPath.charAt(i);
            if (c >= '0' && c <= '9') continue;
            return animationPath;
        }
        return animationPath.substring(0, stageIndex);
    }

    private static Identifier geoModelPath(Identifier modelId) {
        String path = modelId.getPath();
        if (AfwGeckoResourceResolver.isCompleteModelPath(path)) {
            return modelId;
        }
        return new Identifier((String)modelId.getNamespace(), (String)("geo/" + path + ".geo.json"));
    }

    private static boolean isCompleteModelPath(String path) {
        return path != null && path.endsWith(".geo.json")
                && path.startsWith("geo/");
    }

    private static boolean resourceExists(Identifier resourceId) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return false;
        }
        try {
            return client.getResourceManager().getResource(resourceId).isPresent();
        }
        catch (RuntimeException ex) {
            if (resourceId != null && LOGGED_RESOURCE_LOOKUP_ERRORS.add(resourceId.toString())) {
                AnimationFramework.LOGGER.debug("[AFW] Resource lookup failed for {}", (Object)resourceId, (Object)ex);
            }
            return false;
        }
    }

    private static ModelAndTexture missingWithLog(Identifier typeId) {
        if (typeId != null && LOGGED_MISSING_TYPES.add(typeId.toString())) {
            GeckoLib.LOGGER.info("[AFW] No specific gecko model for entity type {}, using missingmodel.", (Object)typeId);
        }
        return new ModelAndTexture(DEFAULT_MISSING_MODEL, DEFAULT_MISSING_TEXTURE, true);
    }

    public record ModelAndTexture(Identifier model, Identifier texture, boolean missingModel) {
    }
}

