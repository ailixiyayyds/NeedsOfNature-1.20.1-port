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
 *  net.minecraft.util.Identifier
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.Resource
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.resource.ResourceReloader
 *  net.minecraft.resource.SynchronousResourceReloader
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
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;
import org.jetbrains.annotations.Nullable;

public final class NonLiquidGainOverrides {
    private static final String ROOT_PATH = "non_liquid_gains";
    private static final String ENTRIES_KEY = "entries";
    private static final String ENTITY_KEY = "entity";
    private static final String GAIN_ML_KEY = "gain_ml";
    private static final String COLOR_KEY = "color";
    private static final String MIXED_COLOR_KEY = "mixed_color";
    private static volatile Map<Identifier, Integer> GAIN_BY_ENTITY = Map.of();
    private static volatile Map<Identifier, String> COLOR_HEX_BY_ENTITY = Map.of();
    @Nullable
    private static volatile String MIXED_COLOR_HEX = null;

    private NonLiquidGainOverrides() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.SERVER_DATA).registerReloader(Reloader.RELOADER_ID, (ResourceReloader)new Reloader());
    }

    @Nullable
    public static Integer getGain(@Nullable Identifier entityTypeId) {
        if (entityTypeId == null) {
            return null;
        }
        return GAIN_BY_ENTITY.get(entityTypeId);
    }

    @Nullable
    public static Integer getColorRgb(@Nullable Identifier entityTypeId) {
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
        for (Map.Entry<Identifier, Integer> entry : GAIN_BY_ENTITY.entrySet()) {
            out.put(entry.getKey().toString(), entry.getValue());
        }
        return Map.copyOf(out);
    }

    public static Map<String, String> getColorByEntity() {
        if (COLOR_HEX_BY_ENTITY.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, String> out = new LinkedHashMap<String, String>();
        for (Map.Entry<Identifier, String> entry : COLOR_HEX_BY_ENTITY.entrySet()) {
            out.put(entry.getKey().toString(), entry.getValue());
        }
        return Map.copyOf(out);
    }

    private static void reload(ResourceManager resourceManager) {
        Map resources = resourceManager.findResources(ROOT_PATH, id -> id.getPath().endsWith(".json"));
        LinkedHashMap<Identifier, Integer> gains = new LinkedHashMap<Identifier, Integer>();
        LinkedHashMap<Identifier, String> colors = new LinkedHashMap<Identifier, String>();
        String mixedColorHex = null;
        ArrayList ordered = new ArrayList(resources.entrySet());
        ordered.sort(Comparator.comparing(entry -> ((Identifier)entry.getKey()).toString()));
        for (Map.Entry entry2 : ordered) {
            Identifier fileId = (Identifier)entry2.getKey();
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry2.getValue()).getInputStream(), StandardCharsets.UTF_8);){
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

    private static void parseEntries(Identifier fileId, JsonArray entries, Map<Identifier, Integer> gains, Map<Identifier, String> colors) {
        for (int i = 0; i < entries.size(); ++i) {
            String normalizedHex;
            JsonObject obj = NonLiquidGainOverrides.asObject(entries.get(i));
            if (obj == null) {
                NonLiquidGainOverrides.warnMalformed(fileId, i, "Entry is not an object.");
                continue;
            }
            Identifier entityId = NonLiquidGainOverrides.parseEntityId(obj.get(ENTITY_KEY));
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
    private static Identifier parseEntityId(@Nullable JsonElement element) {
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
        return Identifier.tryParse((String)raw.trim());
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

    private static void warnMalformed(Identifier fileId, int index, String reason) {
        NeedsOfNature.logSetupWarning("[NoN] Ignoring malformed liquid gain entry in {} at index {}: {}", fileId, index, reason);
    }

    public static final class Reloader
    implements SynchronousResourceReloader {
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"liquid_gain_overrides");

        public void reload(ResourceManager manager) {
            NonLiquidGainOverrides.reload(manager);
        }
    }
}

