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
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;

public final class NonLiquidGainMultipliers {
    private static final String MULTIPLIER_KEY = "liquid_gain_multiplier";
    private static volatile Map<Identifier, Double> MULTIPLIER_BY_ANIM = Map.of();

    private NonLiquidGainMultipliers() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.SERVER_DATA).registerReloader(Reloader.RELOADER_ID, (ResourceReloader)new Reloader());
    }

    public static Double getMultiplier(Identifier animationId) {
        if (animationId == null) {
            return null;
        }
        return MULTIPLIER_BY_ANIM.get(NonPeakStages.baseAnimationId(animationId));
    }

    private static void reload(ResourceManager resourceManager) {
        Map resources = resourceManager.findResources("afw_animdefs", id -> id.getPath().endsWith(".json"));
        HashMap<Identifier, Double> multipliers = new HashMap<Identifier, Double>();
        for (Map.Entry entry : resources.entrySet()) {
            Identifier fileId = (Identifier)entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry.getValue()).getInputStream(), StandardCharsets.UTF_8);){
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

    private static Identifier definitionIdFromFile(Identifier fileId) {
        String prefix;
        String path = fileId.getPath();
        if (path.startsWith(prefix = "afw_animdefs/")) {
            path = path.substring(prefix.length());
        }
        if (path.endsWith(".json")) {
            path = path.substring(0, path.length() - 5);
        }
        return Identifier.of((String)fileId.getNamespace(), (String)path);
    }

    public static final class Reloader
    implements SynchronousResourceReloader {
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"afw_animdefs_liquid_gain_multiplier");

        public void reload(ResourceManager manager) {
            NonLiquidGainMultipliers.reload(manager);
        }
    }
}

