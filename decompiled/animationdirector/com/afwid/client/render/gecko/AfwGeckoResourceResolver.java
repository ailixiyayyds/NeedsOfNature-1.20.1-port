/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
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
import net.minecraft.class_2960;
import net.minecraft.class_310;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.cache.GeckoLibResources;
import software.bernie.geckolib.loading.object.BakedAnimations;

public final class AfwGeckoResourceResolver {
    private static final String SAFETY_FALLBACK_ANIMATION_KEY = "animation";
    private static final class_2960 DEFAULT_MISSING_MODEL = class_2960.method_60655((String)"animationframework", (String)"entity/missingmodel");
    private static final class_2960 DEFAULT_MISSING_TEXTURE = class_2960.method_60655((String)"animationframework", (String)"textures/missingmodel.png");
    private static final Set<String> LOGGED_MISSING_TYPES = Collections.newSetFromMap(new ConcurrentHashMap());
    private static final Set<String> LOGGED_MISSING_ANIMATIONS = Collections.newSetFromMap(new ConcurrentHashMap());
    private static final Set<String> LOGGED_RESOURCE_LOOKUP_ERRORS = Collections.newSetFromMap(new ConcurrentHashMap());

    private AfwGeckoResourceResolver() {
    }

    public static class_2960 toAnimationResource(class_2960 afwAnimationId) {
        if (afwAnimationId == null) {
            return null;
        }
        return class_2960.method_60655((String)afwAnimationId.method_12836(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.method_12832(), null));
    }

    public static class_2960 toAnimationResource(class_2960 afwAnimationId, String actorKey) {
        if (afwAnimationId == null) {
            return null;
        }
        return class_2960.method_60655((String)afwAnimationId.method_12836(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.method_12832(), actorKey));
    }

    public static class_2960 resolveAnimationResource(class_2960 afwAnimationId, String actorKey, class_2960 entityTypeId) {
        return AfwGeckoResourceResolver.resolveAnimationResource(afwAnimationId, actorKey, entityTypeId, true);
    }

    public static class_2960 resolveAnimationResource(class_2960 afwAnimationId, String actorKey, class_2960 entityTypeId, boolean warnMissing) {
        String logKey;
        if (afwAnimationId == null) {
            return null;
        }
        class_2960 actorCandidate = null;
        if (actorKey != null && !actorKey.isBlank() && AfwGeckoResourceResolver.hasBakedAnimation(actorCandidate = class_2960.method_60655((String)afwAnimationId.method_12836(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.method_12832(), actorKey)))) {
            return actorCandidate;
        }
        class_2960 typeCandidate = null;
        if (entityTypeId != null) {
            class_2960 namespacedCandidate;
            String typeKey = entityTypeId.method_12832();
            typeCandidate = class_2960.method_60655((String)afwAnimationId.method_12836(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.method_12832(), typeKey));
            if (AfwGeckoResourceResolver.hasBakedAnimation(typeCandidate)) {
                return typeCandidate;
            }
            String namespacedKey = entityTypeId.method_12836() + "_" + entityTypeId.method_12832();
            if (!namespacedKey.equals(typeKey) && AfwGeckoResourceResolver.hasBakedAnimation(namespacedCandidate = class_2960.method_60655((String)afwAnimationId.method_12836(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.method_12832(), namespacedKey)))) {
                return namespacedCandidate;
            }
        }
        class_2960 base = class_2960.method_60655((String)afwAnimationId.method_12836(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.method_12832(), null));
        boolean baseBaked = AfwGeckoResourceResolver.hasBakedAnimation(base);
        if (warnMissing && !baseBaked && (actorCandidate != null || typeCandidate != null) && AfwClientConfig.get().allowsDebugChat(AfwDebugChatCategory.SETUP) && LOGGED_MISSING_ANIMATIONS.add(logKey = String.valueOf(afwAnimationId) + "|actor=" + actorKey + "|type=" + String.valueOf(entityTypeId))) {
            String message = AnimationFramework.formatLogTemplate("[AFW] Missing per-actor animation for {} (actorKey={}, typeId={}); no base animation found.", afwAnimationId, actorKey, entityTypeId);
            AnimationFramework.LOGGER.warn("{}", (Object)message);
            AnimationFrameworkClient.sendClientSetupWarning(message);
        }
        AfwGeckoResourceResolver.hasBakedAnimation(base);
        return base;
    }

    public static boolean hasBakedAnimation(class_2960 animationResource) {
        return GeckoLibResources.getBakedAnimations().cache().containsKey(animationResource);
    }

    public static String resolveAnimationKey(class_2960 animationResource) {
        if (animationResource == null) {
            return SAFETY_FALLBACK_ANIMATION_KEY;
        }
        BakedAnimations baked = (BakedAnimations)GeckoLibResources.getBakedAnimations().cache().get(animationResource);
        if (baked == null || baked.animations().isEmpty()) {
            return SAFETY_FALLBACK_ANIMATION_KEY;
        }
        return (String)baked.animations().keySet().iterator().next();
    }

    public static ModelAndTexture resolveModelAndTexture(class_2960 entityTypeId) {
        class_2960 derivedTexture;
        if (entityTypeId == null) {
            return AfwGeckoResourceResolver.missingWithLog(null);
        }
        String entityPath = entityTypeId.method_12832();
        class_2960 primaryModel = class_2960.method_60655((String)"animationframework", (String)("entity/" + entityPath));
        class_2960 secondaryModel = class_2960.method_60655((String)"animationframework", (String)entityPath);
        class_2960 resolvedModel = primaryModel;
        boolean hasModel = AfwGeckoResourceResolver.hasGeoModel(primaryModel);
        if (!hasModel && AfwGeckoResourceResolver.hasGeoModel(secondaryModel)) {
            resolvedModel = secondaryModel;
            hasModel = true;
        }
        class_2960 resolvedTexture = AfwGeckoResourceResolver.resourceExists(derivedTexture = class_2960.method_60655((String)entityTypeId.method_12836(), (String)("textures/entity/" + entityPath + "/" + entityPath + ".png"))) ? derivedTexture : DEFAULT_MISSING_TEXTURE;
        return new ModelAndTexture(resolvedModel, resolvedTexture, !hasModel);
    }

    public static ModelAndTexture missingModelFallback(class_2960 typeId) {
        return AfwGeckoResourceResolver.missingWithLog(typeId);
    }

    public static boolean hasGeoModel(class_2960 modelId) {
        if (GeckoLibResources.getBakedModels().cache().containsKey(modelId)) {
            return true;
        }
        class_2960 geoPath = AfwGeckoResourceResolver.geoModelPath(modelId);
        return AfwGeckoResourceResolver.resourceExists(geoPath);
    }

    private static String nestedAnimationPath(String animationPath, String suffix) {
        String folder = AfwGeckoResourceResolver.animationFolderPath(animationPath);
        String file = suffix == null || suffix.isBlank() ? animationPath : animationPath + "_" + suffix;
        return "afw/" + folder + "/" + file;
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

    private static class_2960 geoModelPath(class_2960 modelId) {
        return class_2960.method_60655((String)modelId.method_12836(), (String)("geckolib/models/" + modelId.method_12832() + ".geo.json"));
    }

    private static boolean resourceExists(class_2960 resourceId) {
        class_310 client = class_310.method_1551();
        if (client == null) {
            return false;
        }
        try {
            return client.method_1478().method_14486(resourceId).isPresent();
        }
        catch (RuntimeException ex) {
            if (resourceId != null && LOGGED_RESOURCE_LOOKUP_ERRORS.add(resourceId.toString())) {
                AnimationFramework.LOGGER.debug("[AFW] Resource lookup failed for {}", (Object)resourceId, (Object)ex);
            }
            return false;
        }
    }

    private static ModelAndTexture missingWithLog(class_2960 typeId) {
        if (typeId != null && LOGGED_MISSING_TYPES.add(typeId.toString())) {
            GeckoLibConstants.LOGGER.info("[AFW] No specific gecko model for entity type {}, using missingmodel.", (Object)typeId);
        }
        return new ModelAndTexture(DEFAULT_MISSING_MODEL, DEFAULT_MISSING_TEXTURE, true);
    }

    public record ModelAndTexture(class_2960 model, class_2960 texture, boolean missingModel) {
    }
}

