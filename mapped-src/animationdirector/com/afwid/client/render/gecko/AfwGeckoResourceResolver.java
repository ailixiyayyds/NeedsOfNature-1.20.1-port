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
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.cache.GeckoLibResources;
import software.bernie.geckolib.loading.object.BakedAnimations;

public final class AfwGeckoResourceResolver {
    private static final String SAFETY_FALLBACK_ANIMATION_KEY = "animation";
    private static final Identifier DEFAULT_MISSING_MODEL = Identifier.of((String)"animationframework", (String)"entity/missingmodel");
    private static final Identifier DEFAULT_MISSING_TEXTURE = Identifier.of((String)"animationframework", (String)"textures/missingmodel.png");
    private static final Set<String> LOGGED_MISSING_TYPES = Collections.newSetFromMap(new ConcurrentHashMap());
    private static final Set<String> LOGGED_MISSING_ANIMATIONS = Collections.newSetFromMap(new ConcurrentHashMap());
    private static final Set<String> LOGGED_RESOURCE_LOOKUP_ERRORS = Collections.newSetFromMap(new ConcurrentHashMap());

    private AfwGeckoResourceResolver() {
    }

    public static Identifier toAnimationResource(Identifier afwAnimationId) {
        if (afwAnimationId == null) {
            return null;
        }
        return Identifier.of((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), null));
    }

    public static Identifier toAnimationResource(Identifier afwAnimationId, String actorKey) {
        if (afwAnimationId == null) {
            return null;
        }
        return Identifier.of((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), actorKey));
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
        if (actorKey != null && !actorKey.isBlank() && AfwGeckoResourceResolver.hasBakedAnimation(actorCandidate = Identifier.of((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), actorKey)))) {
            return actorCandidate;
        }
        Identifier typeCandidate = null;
        if (entityTypeId != null) {
            Identifier namespacedCandidate;
            String typeKey = entityTypeId.getPath();
            typeCandidate = Identifier.of((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), typeKey));
            if (AfwGeckoResourceResolver.hasBakedAnimation(typeCandidate)) {
                return typeCandidate;
            }
            String namespacedKey = entityTypeId.getNamespace() + "_" + entityTypeId.getPath();
            if (!namespacedKey.equals(typeKey) && AfwGeckoResourceResolver.hasBakedAnimation(namespacedCandidate = Identifier.of((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), namespacedKey)))) {
                return namespacedCandidate;
            }
        }
        Identifier base = Identifier.of((String)afwAnimationId.getNamespace(), (String)AfwGeckoResourceResolver.nestedAnimationPath(afwAnimationId.getPath(), null));
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
        return GeckoLibResources.getBakedAnimations().cache().containsKey(animationResource);
    }

    public static String resolveAnimationKey(Identifier animationResource) {
        if (animationResource == null) {
            return SAFETY_FALLBACK_ANIMATION_KEY;
        }
        BakedAnimations baked = (BakedAnimations)GeckoLibResources.getBakedAnimations().cache().get(animationResource);
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
        Identifier primaryModel = Identifier.of((String)"animationframework", (String)("entity/" + entityPath));
        Identifier secondaryModel = Identifier.of((String)"animationframework", (String)entityPath);
        Identifier resolvedModel = primaryModel;
        boolean hasModel = AfwGeckoResourceResolver.hasGeoModel(primaryModel);
        if (!hasModel && AfwGeckoResourceResolver.hasGeoModel(secondaryModel)) {
            resolvedModel = secondaryModel;
            hasModel = true;
        }
        Identifier resolvedTexture = AfwGeckoResourceResolver.resourceExists(derivedTexture = Identifier.of((String)entityTypeId.getNamespace(), (String)("textures/entity/" + entityPath + "/" + entityPath + ".png"))) ? derivedTexture : DEFAULT_MISSING_TEXTURE;
        return new ModelAndTexture(resolvedModel, resolvedTexture, !hasModel);
    }

    public static ModelAndTexture missingModelFallback(Identifier typeId) {
        return AfwGeckoResourceResolver.missingWithLog(typeId);
    }

    public static boolean hasGeoModel(Identifier modelId) {
        if (GeckoLibResources.getBakedModels().cache().containsKey(modelId)) {
            return true;
        }
        Identifier geoPath = AfwGeckoResourceResolver.geoModelPath(modelId);
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

    private static Identifier geoModelPath(Identifier modelId) {
        return Identifier.of((String)modelId.getNamespace(), (String)("geckolib/models/" + modelId.getPath() + ".geo.json"));
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
            GeckoLibConstants.LOGGER.info("[AFW] No specific gecko model for entity type {}, using missingmodel.", (Object)typeId);
        }
        return new ModelAndTexture(DEFAULT_MISSING_MODEL, DEFAULT_MISSING_TEXTURE, true);
    }

    public record ModelAndTexture(Identifier model, Identifier texture, boolean missingModel) {
    }
}

