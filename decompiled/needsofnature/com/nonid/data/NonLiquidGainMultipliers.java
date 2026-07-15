/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
 */
package com.nonid.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.nonid.NeedsOfNature;
import com.nonid.data.NonPeakStages;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_4013;

public final class NonLiquidGainMultipliers {
    private static final String MULTIPLIER_KEY = "liquid_gain_multiplier";
    private static volatile Map<class_2960, Double> MULTIPLIER_BY_ANIM = Map.of();

    private NonLiquidGainMultipliers() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((class_3264)class_3264.field_14190).registerReloader(Reloader.RELOADER_ID, (class_3302)new Reloader());
    }

    public static Double getMultiplier(class_2960 animationId) {
        if (animationId == null) {
            return null;
        }
        return MULTIPLIER_BY_ANIM.get(NonPeakStages.baseAnimationId(animationId));
    }

    private static void reload(class_3300 resourceManager) {
        Map resources = resourceManager.method_14488("afw_animdefs", id -> id.method_12832().endsWith(".json"));
        HashMap<class_2960, Double> multipliers = new HashMap<class_2960, Double>();
        for (Map.Entry entry : resources.entrySet()) {
            class_2960 fileId = (class_2960)entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(((class_3298)entry.getValue()).method_14482(), StandardCharsets.UTF_8);){
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                Double multiplier = NonLiquidGainMultipliers.readOptionalMultiplier(obj.get(MULTIPLIER_KEY));
                if (multiplier == null) continue;
                multipliers.put(NonLiquidGainMultipliers.definitionIdFromFile(fileId), multiplier);
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to read liquid_gain_multiplier from {}", (Object)fileId, (Object)e);
            }
        }
        MULTIPLIER_BY_ANIM = Map.copyOf(multipliers);
    }

    private static Double readOptionalMultiplier(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (!primitive.isNumber()) {
            return null;
        }
        Double value = primitive.getAsDouble();
        if (value == null || !Double.isFinite(value) || value < 0.0) {
            return null;
        }
        return value;
    }

    private static class_2960 definitionIdFromFile(class_2960 fileId) {
        String prefix;
        String path = fileId.method_12832();
        if (path.startsWith(prefix = "afw_animdefs/")) {
            path = path.substring(prefix.length());
        }
        if (path.endsWith(".json")) {
            path = path.substring(0, path.length() - 5);
        }
        return class_2960.method_60655((String)fileId.method_12836(), (String)path);
    }

    public static final class Reloader
    implements class_4013 {
        static final class_2960 RELOADER_ID = class_2960.method_60655((String)"needsofnature", (String)"afw_animdefs_liquid_gain_multiplier");

        public void method_14491(class_3300 manager) {
            NonLiquidGainMultipliers.reload(manager);
        }
    }
}

