/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.minecraft.class_2960
 *  net.minecraft.class_3264
 *  net.minecraft.class_3298
 *  net.minecraft.class_3300
 *  net.minecraft.class_3302
 *  net.minecraft.class_4013
 */
package com.afwid.client.render.gecko;

import com.afwid.AnimationFramework;
import com.afwid.AnimationFrameworkClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_4013;

public final class AfwBoneTextureOverrides {
    private static volatile Map<class_2960, Map<String, class_2960>> BONE_TEXTURES_BY_MODEL = Map.of();
    private static volatile Map<class_2960, List<class_2960>> EMISSIVE_TEXTURES_BY_MODEL = Map.of();
    private static volatile Map<class_2960, Map<String, ModelLocator>> LOCATORS_BY_MODEL = Map.of();
    private static volatile Map<class_2960, RenderSettings> RENDER_SETTINGS_BY_MODEL = Map.of();
    private static final class_2960 RELOADER_ID = class_2960.method_60655((String)"animationframework", (String)"afw_bone_textures");

    private AfwBoneTextureOverrides() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((class_3264)class_3264.field_14188).registerReloader(RELOADER_ID, (class_3302)new Reloader());
    }

    public static Map<String, class_2960> getBoneTextures(class_2960 modelId) {
        if (modelId == null) {
            return null;
        }
        return BONE_TEXTURES_BY_MODEL.get(modelId);
    }

    public static List<class_2960> getEmissiveTextures(class_2960 modelId) {
        if (modelId == null) {
            return null;
        }
        return EMISSIVE_TEXTURES_BY_MODEL.get(modelId);
    }

    public static Map<String, ModelLocator> getLocators(class_2960 modelId) {
        if (modelId == null) {
            return null;
        }
        return LOCATORS_BY_MODEL.get(modelId);
    }

    public static boolean isTranslucent(class_2960 modelId) {
        if (modelId == null) {
            return false;
        }
        RenderSettings settings = RENDER_SETTINGS_BY_MODEL.get(modelId);
        return settings != null && settings.translucent();
    }

    private static void reload(class_3300 manager) {
        Map resources = manager.method_14488("geckolib/models", id -> id.method_12832().endsWith(".geo.json"));
        ArrayList entries = new ArrayList(resources.entrySet());
        entries.sort(Comparator.comparing(e -> ((class_2960)e.getKey()).toString()));
        LinkedHashMap<class_2960, Map<String, class_2960>> loaded = new LinkedHashMap<class_2960, Map<String, class_2960>>();
        LinkedHashMap<class_2960, List<class_2960>> loadedEmissive = new LinkedHashMap<class_2960, List<class_2960>>();
        LinkedHashMap<class_2960, Map<String, ModelLocator>> loadedLocators = new LinkedHashMap<class_2960, Map<String, ModelLocator>>();
        LinkedHashMap<class_2960, RenderSettings> loadedRenderSettings = new LinkedHashMap<class_2960, RenderSettings>();
        for (Map.Entry entry : entries) {
            class_2960 fileId = (class_2960)entry.getKey();
            class_2960 modelId = AfwBoneTextureOverrides.modelIdFromGeoPath(fileId);
            if (modelId == null) continue;
            try (InputStreamReader reader = new InputStreamReader(((class_3298)entry.getValue()).method_14482(), StandardCharsets.UTF_8);){
                RenderSettings renderSettings;
                Map<String, ModelLocator> locators;
                List<class_2960> emissiveTextures;
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                Map<String, class_2960> boneTextures = AfwBoneTextureOverrides.readBoneTextures(obj, fileId);
                if (boneTextures != null && !boneTextures.isEmpty()) {
                    loaded.put(modelId, Map.copyOf(boneTextures));
                }
                if ((emissiveTextures = AfwBoneTextureOverrides.readEmissiveTextures(obj, fileId)) != null && !emissiveTextures.isEmpty()) {
                    loadedEmissive.put(modelId, List.copyOf(emissiveTextures));
                }
                if ((locators = AfwBoneTextureOverrides.readLocators(obj)) != null && !locators.isEmpty()) {
                    loadedLocators.put(modelId, Map.copyOf(locators));
                }
                if ((renderSettings = AfwBoneTextureOverrides.readRenderSettings(obj, fileId)) == null || renderSettings.isDefault()) continue;
                loadedRenderSettings.put(modelId, renderSettings);
            }
            catch (IOException | RuntimeException e2) {
                AfwBoneTextureOverrides.setupError("[AFW] Failed to load Gecko model render metadata {}", fileId, e2);
            }
        }
        BONE_TEXTURES_BY_MODEL = Map.copyOf(loaded);
        EMISSIVE_TEXTURES_BY_MODEL = Map.copyOf(loadedEmissive);
        LOCATORS_BY_MODEL = Map.copyOf(loadedLocators);
        RENDER_SETTINGS_BY_MODEL = Map.copyOf(loadedRenderSettings);
    }

    private static Map<String, class_2960> readBoneTextures(JsonObject obj, class_2960 fileId) {
        if (!obj.has("afw_bone_textures")) {
            return null;
        }
        JsonElement el = obj.get("afw_bone_textures");
        if (el == null || !el.isJsonObject()) {
            AfwBoneTextureOverrides.setupWarn("[AFW] Skipping {}: 'afw_bone_textures' must be an object", fileId);
            return null;
        }
        JsonObject bonesObj = el.getAsJsonObject();
        LinkedHashMap<String, class_2960> out = new LinkedHashMap<String, class_2960>();
        for (Map.Entry boneEntry : bonesObj.entrySet()) {
            String boneName = (String)boneEntry.getKey();
            JsonElement textureEl = (JsonElement)boneEntry.getValue();
            if (boneName == null || boneName.isBlank()) continue;
            if (textureEl == null || !textureEl.isJsonPrimitive()) {
                AfwBoneTextureOverrides.setupWarn("[AFW] Skipping {}: invalid texture for bone '{}'", fileId, boneName);
                continue;
            }
            String textureRaw = textureEl.getAsString();
            class_2960 textureId = class_2960.method_12829((String)textureRaw);
            if (textureId == null) {
                AfwBoneTextureOverrides.setupWarn("[AFW] Skipping {}: invalid texture id '{}' for bone '{}'", fileId, textureRaw, boneName);
                continue;
            }
            out.put(boneName, textureId);
        }
        return out;
    }

    private static List<class_2960> readEmissiveTextures(JsonObject obj, class_2960 fileId) {
        if (!obj.has("afw_emissive_textures")) {
            return null;
        }
        JsonElement el = obj.get("afw_emissive_textures");
        if (el == null || !el.isJsonArray()) {
            AfwBoneTextureOverrides.setupWarn("[AFW] Skipping {}: 'afw_emissive_textures' must be an array", fileId);
            return null;
        }
        ArrayList<class_2960> out = new ArrayList<class_2960>();
        for (JsonElement textureEl : el.getAsJsonArray()) {
            if (textureEl == null || !textureEl.isJsonPrimitive()) {
                AfwBoneTextureOverrides.setupWarn("[AFW] Skipping {}: invalid emissive texture entry", fileId);
                continue;
            }
            String textureRaw = textureEl.getAsString();
            class_2960 textureId = class_2960.method_12829((String)textureRaw);
            if (textureId == null) {
                AfwBoneTextureOverrides.setupWarn("[AFW] Skipping {}: invalid emissive texture id '{}'", fileId, textureRaw);
                continue;
            }
            out.add(textureId);
        }
        return out;
    }

    private static RenderSettings readRenderSettings(JsonObject obj, class_2960 fileId) {
        if (!obj.has("afw_render")) {
            return null;
        }
        JsonElement el = obj.get("afw_render");
        if (el == null || !el.isJsonObject()) {
            AfwBoneTextureOverrides.setupWarn("[AFW] Skipping {}: 'afw_render' must be an object", fileId);
            return null;
        }
        JsonObject renderObj = el.getAsJsonObject();
        boolean translucent = AfwBoneTextureOverrides.readOptionalBoolean(renderObj, "translucent", fileId, "afw_render", false);
        return new RenderSettings(translucent);
    }

    private static boolean readOptionalBoolean(JsonObject obj, String key, class_2960 fileId, String context, boolean fallback) {
        if (obj == null || !obj.has(key)) {
            return fallback;
        }
        JsonElement el = obj.get(key);
        if (el == null || !el.isJsonPrimitive() || !el.getAsJsonPrimitive().isBoolean()) {
            AfwBoneTextureOverrides.setupWarn("[AFW] Skipping {}: '{}.{}' must be a boolean", fileId, context, key);
            return fallback;
        }
        return el.getAsBoolean();
    }

    private static Map<String, ModelLocator> readLocators(JsonObject obj) {
        JsonElement geometryEl = obj.get("minecraft:geometry");
        if (geometryEl == null || !geometryEl.isJsonArray()) {
            return null;
        }
        LinkedHashMap<String, ModelLocator> out = new LinkedHashMap<String, ModelLocator>();
        for (JsonElement geometryEntry : geometryEl.getAsJsonArray()) {
            JsonElement bonesEl;
            if (geometryEntry == null || !geometryEntry.isJsonObject() || (bonesEl = geometryEntry.getAsJsonObject().get("bones")) == null || !bonesEl.isJsonArray()) continue;
            for (JsonElement boneEntry : bonesEl.getAsJsonArray()) {
                JsonElement locatorsEl;
                String boneName;
                JsonObject boneObj;
                JsonElement nameEl;
                if (boneEntry == null || !boneEntry.isJsonObject() || (nameEl = (boneObj = boneEntry.getAsJsonObject()).get("name")) == null || !nameEl.isJsonPrimitive() || (boneName = nameEl.getAsString()) == null || boneName.isBlank()) continue;
                double[] pivot = AfwBoneTextureOverrides.readVec3(boneObj.get("pivot"));
                if (pivot == null) {
                    pivot = new double[]{0.0, 0.0, 0.0};
                }
                if ((locatorsEl = boneObj.get("locators")) == null || !locatorsEl.isJsonObject()) continue;
                for (Map.Entry locatorEntry : locatorsEl.getAsJsonObject().entrySet()) {
                    double[] locator;
                    String locatorName = (String)locatorEntry.getKey();
                    if (locatorName == null || locatorName.isBlank() || (locator = AfwBoneTextureOverrides.readLocatorVec3((JsonElement)locatorEntry.getValue())) == null) continue;
                    out.put(locatorName, new ModelLocator(boneName, locator[0], locator[1], locator[2], pivot[0], pivot[1], pivot[2]));
                }
            }
        }
        return out;
    }

    private static double[] readLocatorVec3(JsonElement el) {
        if (el == null) {
            return null;
        }
        if (el.isJsonArray()) {
            return AfwBoneTextureOverrides.readVec3(el);
        }
        if (el.isJsonObject()) {
            return AfwBoneTextureOverrides.readVec3(el.getAsJsonObject().get("offset"));
        }
        return null;
    }

    private static double[] readVec3(JsonElement el) {
        if (el == null || !el.isJsonArray()) {
            return null;
        }
        JsonArray array = el.getAsJsonArray();
        if (array.size() < 3) {
            return null;
        }
        return new double[]{array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble()};
    }

    private static class_2960 modelIdFromGeoPath(class_2960 fileId) {
        String path = fileId.method_12832();
        String prefix = "geckolib/models/";
        String suffix = ".geo.json";
        if (!path.startsWith(prefix) || !path.endsWith(suffix)) {
            return null;
        }
        String modelPath = path.substring(prefix.length(), path.length() - suffix.length());
        return class_2960.method_60655((String)fileId.method_12836(), (String)modelPath);
    }

    private static void setupWarn(String template, Object ... args) {
        AnimationFramework.LOGGER.warn(template, args);
        AnimationFrameworkClient.sendClientSetupWarning(AnimationFramework.formatLogTemplate(template, args));
    }

    private static void setupError(String template, Object ... args) {
        AnimationFramework.LOGGER.error(template, args);
        AnimationFrameworkClient.sendClientSetupWarning(AnimationFramework.formatLogTemplate(template, args));
    }

    public static final class Reloader
    implements class_4013 {
        public void method_14491(class_3300 manager) {
            AfwBoneTextureOverrides.reload(manager);
        }
    }

    private record RenderSettings(boolean translucent) {
        private boolean isDefault() {
            return !this.translucent;
        }
    }

    public record ModelLocator(String parentBone, double x, double y, double z, double parentPivotX, double parentPivotY, double parentPivotZ) {
    }
}

