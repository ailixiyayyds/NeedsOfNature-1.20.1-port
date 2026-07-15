/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.minecraft.util.Identifier
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.Resource
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.resource.ResourceReloader
 *  net.minecraft.resource.SynchronousResourceReloader
 */
package com.afwid.client.sound;

import com.afwid.AnimationFramework;
import com.afwid.AnimationFrameworkClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;

public final class AfwSoundEffects {
    private static volatile Map<Identifier, Map<String, SoundAnimation>> SOUND_BY_ANIMATION = Map.of();
    private static final Identifier RELOADER_ID = Identifier.of((String)"animationframework", (String)"afw_sound_effects");

    private AfwSoundEffects() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.CLIENT_RESOURCES).registerReloader(RELOADER_ID, (ResourceReloader)new Reloader());
    }

    public static boolean hasAny() {
        return !SOUND_BY_ANIMATION.isEmpty();
    }

    public static SoundAnimation getSoundAnimation(Identifier animationResource, String animationKey) {
        if (animationResource == null || animationKey == null || animationKey.isBlank()) {
            return null;
        }
        Map<String, SoundAnimation> byKey = SOUND_BY_ANIMATION.get(animationResource);
        if (byKey == null) {
            return null;
        }
        return byKey.get(animationKey);
    }

    private static void reload(ResourceManager manager) {
        Map resources = manager.findResources("geckolib/animations", id -> id.getPath().endsWith(".animation.json"));
        ArrayList entries = new ArrayList(resources.entrySet());
        entries.sort(Comparator.comparing(e -> ((Identifier)e.getKey()).toString()));
        LinkedHashMap<Identifier, Map> loaded = new LinkedHashMap<Identifier, Map>();
        int cueCount = 0;
        for (Map.Entry entry : entries) {
            Identifier fileId = (Identifier)entry.getKey();
            Identifier animationId = AfwSoundEffects.animationIdFromPath(fileId);
            if (animationId == null) continue;
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry.getValue()).getInputStream(), StandardCharsets.UTF_8);){
                JsonObject animationsObj;
                JsonObject root = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                JsonObject jsonObject = animationsObj = root.has("animations") && root.get("animations").isJsonObject() ? root.getAsJsonObject("animations") : null;
                if (animationsObj == null) continue;
                for (Map.Entry animEntry : animationsObj.entrySet()) {
                    String animKey = (String)animEntry.getKey();
                    JsonElement animEl = (JsonElement)animEntry.getValue();
                    if (animKey == null || animKey.isBlank() || animEl == null || !animEl.isJsonObject()) continue;
                    JsonObject animObj = animEl.getAsJsonObject();
                    double lengthSeconds = AfwSoundEffects.readAnimationLength(animObj);
                    List<SoundCue> cues = AfwSoundEffects.readSoundCues(animObj, fileId);
                    if (cues.isEmpty()) continue;
                    cues.sort(Comparator.comparingDouble(SoundCue::timeTicks));
                    SoundAnimation soundAnim = new SoundAnimation(lengthSeconds, List.copyOf(cues));
                    loaded.computeIfAbsent(animationId, id -> new LinkedHashMap()).put(animKey, soundAnim);
                    cueCount += cues.size();
                }
            }
            catch (IOException | RuntimeException e2) {
                AfwSoundEffects.setupError("[AFW] Failed to load sound cues {}", fileId, e2);
            }
        }
        LinkedHashMap finalized = new LinkedHashMap();
        for (Map.Entry entry : loaded.entrySet()) {
            finalized.put((Identifier)entry.getKey(), Map.copyOf((Map)entry.getValue()));
        }
        SOUND_BY_ANIMATION = Map.copyOf(finalized);
        AnimationFramework.LOGGER.info("[AFW] Loaded {} sound cue animation(s), {} cue(s)", (Object)SOUND_BY_ANIMATION.size(), (Object)cueCount);
    }

    private static double readAnimationLength(JsonObject animObj) {
        Double animationLength = AfwSoundEffects.readFiniteDouble(animObj.get("animation_length"));
        if (animationLength != null) {
            return animationLength;
        }
        Double length = AfwSoundEffects.readFiniteDouble(animObj.get("length"));
        if (length != null) {
            return length;
        }
        return -1.0;
    }

    private static List<SoundCue> readSoundCues(JsonObject animObj, Identifier fileId) {
        if (!animObj.has("sound_effects")) {
            return List.of();
        }
        JsonElement soundEl = animObj.get("sound_effects");
        if (soundEl == null || !soundEl.isJsonObject()) {
            AfwSoundEffects.setupWarn("[AFW] Skipping {}: 'sound_effects' must be an object", fileId);
            return List.of();
        }
        JsonObject soundObj = soundEl.getAsJsonObject();
        ArrayList<SoundCue> cues = new ArrayList<SoundCue>();
        for (Map.Entry entry : soundObj.entrySet()) {
            double timeSeconds;
            String timeKey = (String)entry.getKey();
            if (timeKey == null || timeKey.isBlank()) continue;
            try {
                timeSeconds = Double.parseDouble(timeKey);
            }
            catch (NumberFormatException e) {
                AfwSoundEffects.setupWarn("[AFW] Skipping {}: invalid sound time '{}'", fileId, timeKey);
                continue;
            }
            if (!Double.isFinite(timeSeconds) || timeSeconds < 0.0) continue;
            JsonElement cueEl = (JsonElement)entry.getValue();
            String effect = null;
            float volume = 1.0f;
            float pitch = 1.0f;
            if (cueEl != null && cueEl.isJsonObject()) {
                JsonObject cueObj = cueEl.getAsJsonObject();
                effect = AfwSoundEffects.readString(cueObj, "effect");
                if (effect == null) {
                    effect = AfwSoundEffects.readString(cueObj, "sound");
                }
                volume = AfwSoundEffects.readFloat(cueObj, "volume", 1.0f);
                pitch = AfwSoundEffects.readFloat(cueObj, "pitch", 1.0f);
            } else if (cueEl != null && cueEl.isJsonPrimitive()) {
                effect = cueEl.getAsString();
            }
            if (effect == null || effect.isBlank()) {
                AfwSoundEffects.setupWarn("[AFW] Skipping {}: sound cue at {} missing 'effect'", fileId, String.format(Locale.ROOT, "%.3f", timeSeconds));
                continue;
            }
            double timeTicks = timeSeconds * 20.0;
            cues.add(new SoundCue(timeSeconds, timeTicks, effect, volume, pitch));
        }
        return cues;
    }

    private static String readString(JsonObject obj, String key) {
        if (!obj.has(key) || !obj.get(key).isJsonPrimitive()) {
            return null;
        }
        return obj.get(key).getAsString();
    }

    private static float readFloat(JsonObject obj, String key, float fallback) {
        Double value = AfwSoundEffects.readFiniteDouble(obj.get(key));
        return value == null ? fallback : value.floatValue();
    }

    private static Double readFiniteDouble(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            double value = primitive.getAsDouble();
            return Double.isFinite(value) ? Double.valueOf(value) : null;
        }
        if (primitive.isString()) {
            String raw = primitive.getAsString();
            if (raw == null) {
                return null;
            }
            String normalized = raw.trim();
            if (normalized.isEmpty()) {
                return null;
            }
            try {
                double value = Double.parseDouble(normalized);
                return Double.isFinite(value) ? Double.valueOf(value) : null;
            }
            catch (NumberFormatException ex) {
                return null;
            }
        }
        return null;
    }

    private static Identifier animationIdFromPath(Identifier fileId) {
        String path = fileId.getPath();
        String prefix = "geckolib/animations/";
        String suffix = ".animation.json";
        if (!path.startsWith(prefix) || !path.endsWith(suffix)) {
            return null;
        }
        String animPath = path.substring(prefix.length(), path.length() - suffix.length());
        return Identifier.of((String)fileId.getNamespace(), (String)animPath);
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
    implements SynchronousResourceReloader {
        public void reload(ResourceManager manager) {
            AfwSoundEffects.reload(manager);
        }
    }

    public record SoundAnimation(double lengthSeconds, List<SoundCue> cues) {
        public double lengthTicks() {
            if (this.lengthSeconds <= 0.0) {
                return 0.0;
            }
            return this.lengthSeconds * 20.0;
        }
    }

    public record SoundCue(double timeSeconds, double timeTicks, String effect, float volume, float pitch) {
    }
}

