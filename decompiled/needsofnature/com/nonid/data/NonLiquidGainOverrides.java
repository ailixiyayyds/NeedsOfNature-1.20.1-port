/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.minecraft.class_2960
 *  net.minecraft.class_3264
 *  net.minecraft.class_3298
 *  net.minecraft.class_3300
 *  net.minecraft.class_3302
 *  net.minecraft.class_4013
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.nonid.NeedsOfNature;
import com.nonid.client.NonLiquidColors;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_4013;
import org.jetbrains.annotations.Nullable;

public final class NonLiquidGainOverrides {
    private static final String ROOT_PATH = "non_liquid_gains";
    private static final String ENTRIES_KEY = "entries";
    private static final String ENTITY_KEY = "entity";
    private static final String GAIN_ML_KEY = "gain_ml";
    private static final String COLOR_KEY = "color";
    private static final String MIXED_COLOR_KEY = "mixed_color";
    private static volatile Map<class_2960, Integer> GAIN_BY_ENTITY = Map.of();
    private static volatile Map<class_2960, String> COLOR_HEX_BY_ENTITY = Map.of();
    @Nullable
    private static volatile String MIXED_COLOR_HEX = null;

    private NonLiquidGainOverrides() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((class_3264)class_3264.field_14190).registerReloader(Reloader.RELOADER_ID, (class_3302)new Reloader());
    }

    @Nullable
    public static Integer getGain(@Nullable class_2960 entityTypeId) {
        if (entityTypeId == null) {
            return null;
        }
        return GAIN_BY_ENTITY.get(entityTypeId);
    }

    @Nullable
    public static Integer getColorRgb(@Nullable class_2960 entityTypeId) {
        if (entityTypeId == null) {
            return null;
        }
        String hex = COLOR_HEX_BY_ENTITY.get(entityTypeId);
        return NonLiquidColors.parseHexColor(hex);
    }

    @Nullable
    public static Integer getMixedColorRgb() {
        return NonLiquidColors.parseHexColor(MIXED_COLOR_HEX);
    }

    @Nullable
    public static String getMixedColorHex() {
        return MIXED_COLOR_HEX;
    }

    public static Map<String, Integer> getGainByEntity() {
        if (GAIN_BY_ENTITY.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, Integer> out = new LinkedHashMap<String, Integer>();
        for (Map.Entry<class_2960, Integer> entry : GAIN_BY_ENTITY.entrySet()) {
            out.put(entry.getKey().toString(), entry.getValue());
        }
        return Map.copyOf(out);
    }

    public static Map<String, String> getColorByEntity() {
        if (COLOR_HEX_BY_ENTITY.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, String> out = new LinkedHashMap<String, String>();
        for (Map.Entry<class_2960, String> entry : COLOR_HEX_BY_ENTITY.entrySet()) {
            out.put(entry.getKey().toString(), entry.getValue());
        }
        return Map.copyOf(out);
    }

    private static void reload(class_3300 resourceManager) {
        Map resources = resourceManager.method_14488(ROOT_PATH, id -> id.method_12832().endsWith(".json"));
        LinkedHashMap<class_2960, Integer> gains = new LinkedHashMap<class_2960, Integer>();
        LinkedHashMap<class_2960, String> colors = new LinkedHashMap<class_2960, String>();
        String mixedColorHex = null;
        ArrayList ordered = new ArrayList(resources.entrySet());
        ordered.sort(Comparator.comparing(entry -> ((class_2960)entry.getKey()).toString()));
        for (Map.Entry entry2 : ordered) {
            class_2960 fileId = (class_2960)entry2.getKey();
            try (InputStreamReader reader = new InputStreamReader(((class_3298)entry2.getValue()).method_14482(), StandardCharsets.UTF_8);){
                JsonArray entries;
                JsonObject root = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                String mixedColor = NonLiquidGainOverrides.parseOptionalColor(root.get(MIXED_COLOR_KEY));
                if (mixedColor != null) {
                    mixedColorHex = mixedColor;
                }
                if ((entries = NonLiquidGainOverrides.asArray(root.get(ENTRIES_KEY))) == null || entries.isEmpty()) continue;
                NonLiquidGainOverrides.parseEntries(fileId, entries, gains, colors);
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to read liquid gain override file {}", (Object)fileId, (Object)e);
            }
        }
        GAIN_BY_ENTITY = Map.copyOf(gains);
        COLOR_HEX_BY_ENTITY = Map.copyOf(colors);
        MIXED_COLOR_HEX = NonLiquidColors.normalizeHexColor(mixedColorHex);
    }

    private static void parseEntries(class_2960 fileId, JsonArray entries, Map<class_2960, Integer> gains, Map<class_2960, String> colors) {
        for (int i = 0; i < entries.size(); ++i) {
            String normalizedHex;
            JsonObject obj = NonLiquidGainOverrides.asObject(entries.get(i));
            if (obj == null) {
                NonLiquidGainOverrides.warnMalformed(fileId, i, "Entry is not an object.");
                continue;
            }
            class_2960 entityId = NonLiquidGainOverrides.parseEntityId(obj.get(ENTITY_KEY));
            if (entityId == null) {
                NonLiquidGainOverrides.warnMalformed(fileId, i, "Missing or invalid 'entity'.");
                continue;
            }
            Integer gain = NonLiquidGainOverrides.parseOptionalInt(obj.get(GAIN_ML_KEY));
            if (gain != null) {
                gains.put(entityId, Math.max(0, Math.min(1000, gain)));
            }
            if ((normalizedHex = NonLiquidGainOverrides.parseOptionalColor(obj.get(COLOR_KEY))) == null) continue;
            colors.put(entityId, normalizedHex);
        }
    }

    @Nullable
    private static class_2960 parseEntityId(@Nullable JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        if (!element.getAsJsonPrimitive().isString()) {
            return null;
        }
        String raw = element.getAsString();
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return class_2960.method_12829((String)raw.trim());
    }

    @Nullable
    private static Integer parseOptionalInt(@Nullable JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            double value = primitive.getAsDouble();
            if (!Double.isFinite(value) || value != Math.rint(value)) {
                return null;
            }
            if (value < -2.147483648E9 || value > 2.147483647E9) {
                return null;
            }
            return (int)value;
        }
        return null;
    }

    @Nullable
    private static String parseOptionalColor(@Nullable JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        if (!element.getAsJsonPrimitive().isString()) {
            return null;
        }
        return NonLiquidColors.normalizeHexColor(element.getAsString());
    }

    @Nullable
    private static JsonObject asObject(@Nullable JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        return element.getAsJsonObject();
    }

    @Nullable
    private static JsonArray asArray(@Nullable JsonElement element) {
        if (element == null || !element.isJsonArray()) {
            return null;
        }
        return element.getAsJsonArray();
    }

    private static void warnMalformed(class_2960 fileId, int index, String reason) {
        NeedsOfNature.logSetupWarning("[NoN] Ignoring malformed liquid gain entry in {} at index {}: {}", fileId, index, reason);
    }

    public static final class Reloader
    implements class_4013 {
        static final class_2960 RELOADER_ID = class_2960.method_60655((String)"needsofnature", (String)"liquid_gain_overrides");

        public void method_14491(class_3300 manager) {
            NonLiquidGainOverrides.reload(manager);
        }
    }
}

