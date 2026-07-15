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
 */
package com.nonid.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.nonid.NeedsOfNature;
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

public final class NonLoopSecondsOverrides {
    public static final int INFINITE_SECONDS = -1;
    private static final String LOOP_SECONDS_KEY = "stage_seconds";
    private static final String CYCLE_SECONDS_KEY = "cycle_seconds";
    private static final String STAGE_DURATION_MULTIPLIER_KEY = "stage_duration_multiplier";
    private static final double MIN_STAGE_DURATION_MULTIPLIER = 0.05;
    private static final double MAX_STAGE_DURATION_MULTIPLIER = 20.0;
    private static volatile Map<Identifier, Integer> LOOP_SECONDS_BY_ANIM = Map.of();
    private static volatile Map<Identifier, Double> CYCLE_TICKS_BY_ANIM = Map.of();
    private static volatile Map<Identifier, Double> STAGE_DURATION_MULTIPLIER_BY_STAGE = Map.of();

    private NonLoopSecondsOverrides() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.SERVER_DATA).registerReloader(Reloader.RELOADER_ID, (ResourceReloader)new Reloader());
    }

    public static Integer getOverrideSeconds(Identifier animationId) {
        if (animationId == null) {
            return null;
        }
        return LOOP_SECONDS_BY_ANIM.get(animationId);
    }

    public static boolean isInfinite(Integer seconds) {
        return seconds != null && seconds < 0;
    }

    public static double getCycleTicks(Identifier animationId) {
        if (animationId == null) {
            return 0.0;
        }
        Double ticks = CYCLE_TICKS_BY_ANIM.get(animationId);
        return ticks == null || !Double.isFinite(ticks) || ticks <= 0.0 ? 0.0 : ticks;
    }

    public static double getStageDurationMultiplier(Identifier animationId) {
        if (animationId == null) {
            return 1.0;
        }
        Double multiplier = STAGE_DURATION_MULTIPLIER_BY_STAGE.get(animationId);
        return multiplier == null || !Double.isFinite(multiplier) || multiplier <= 0.0 ? 1.0 : multiplier;
    }

    private static void reload(ResourceManager resourceManager) {
        Map resources = resourceManager.findResources("afw_animdefs", id -> id.getPath().endsWith(".json"));
        HashMap<Identifier, Integer> overrides = new HashMap<Identifier, Integer>();
        HashMap<Identifier, Double> cycleTicks = new HashMap<Identifier, Double>();
        HashMap<Identifier, Double> stageDurationMultipliers = new HashMap<Identifier, Double>();
        for (Map.Entry entry : resources.entrySet()) {
            Identifier fileId = (Identifier)entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry.getValue()).getInputStream(), StandardCharsets.UTF_8);){
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                Identifier defId = NonLoopSecondsOverrides.definitionIdFromFile(fileId);
                Integer baseOverride = NonLoopSecondsOverrides.readOptionalInt(obj);
                if (baseOverride != null) {
                    overrides.put(defId, NonLoopSecondsOverrides.clampSeconds(baseOverride));
                }
                if (!obj.has("stages")) continue;
                JsonArray stagesArr = obj.getAsJsonArray("stages");
                for (int i = 0; i < stagesArr.size(); ++i) {
                    Integer stageOverride;
                    JsonObject stageObj = stagesArr.get(i).getAsJsonObject();
                    Integer stageNumber = NonLoopSecondsOverrides.parseStageNumber(stageObj);
                    if (stageNumber == null || stageNumber <= 0) continue;
                    Identifier stageId = Identifier.of((String)defId.getNamespace(), (String)(defId.getPath() + ".p" + stageNumber));
                    Double cycleSeconds = NonLoopSecondsOverrides.readOptionalDouble(stageObj.get(CYCLE_SECONDS_KEY));
                    if (cycleSeconds != null && cycleSeconds > 0.0) {
                        cycleTicks.put(stageId, cycleSeconds * 20.0);
                    }
                    if (stageObj.has(STAGE_DURATION_MULTIPLIER_KEY)) {
                        Double stageDurationMultiplier = NonLoopSecondsOverrides.readOptionalDouble(stageObj.get(STAGE_DURATION_MULTIPLIER_KEY));
                        Boolean loop = NonLoopSecondsOverrides.readOptionalBoolean(stageObj.get("loop"));
                        if (loop != null && !loop.booleanValue()) {
                            NeedsOfNature.logSetupWarning("[NoN] Ignoring stage_duration_multiplier in {} stage {} because the stage is not looping.", defId, stageNumber);
                        } else if (stageDurationMultiplier == null || stageDurationMultiplier < 0.05 || stageDurationMultiplier > 20.0 || !Double.isFinite(stageDurationMultiplier)) {
                            NeedsOfNature.logSetupWarning("[NoN] Ignoring invalid stage_duration_multiplier in {} stage {}. Expected a number from {} to {}.", defId, stageNumber, 0.05, 20.0);
                        } else {
                            stageDurationMultipliers.put(stageId, stageDurationMultiplier);
                        }
                    }
                    if ((stageOverride = NonLoopSecondsOverrides.readOptionalInt(stageObj)) == null) continue;
                    overrides.put(stageId, NonLoopSecondsOverrides.clampSeconds(stageOverride));
                }
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to read stage_seconds overrides from {}", (Object)fileId, (Object)e);
            }
        }
        LOOP_SECONDS_BY_ANIM = Map.copyOf(overrides);
        CYCLE_TICKS_BY_ANIM = Map.copyOf(cycleTicks);
        STAGE_DURATION_MULTIPLIER_BY_STAGE = Map.copyOf(stageDurationMultipliers);
    }

    private static int clampSeconds(int value) {
        if (value < 0) {
            return -1;
        }
        return Math.max(1, Math.min(300, value));
    }

    private static Integer readOptionalInt(JsonObject obj) {
        if (!obj.has(LOOP_SECONDS_KEY)) {
            return null;
        }
        return NonLoopSecondsOverrides.readOptionalInt(obj.get(LOOP_SECONDS_KEY));
    }

    private static Integer parseStageNumber(JsonObject obj) {
        if (!obj.has("stage")) {
            return null;
        }
        JsonElement el = obj.get("stage");
        if (el == null || !el.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive prim = el.getAsJsonPrimitive();
        if (prim.isNumber()) {
            return prim.getAsInt();
        }
        return null;
    }

    private static Integer readOptionalInt(JsonElement element) {
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

    private static Double readOptionalDouble(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            double value = primitive.getAsDouble();
            return Double.isFinite(value) ? Double.valueOf(value) : null;
        }
        return null;
    }

    private static Boolean readOptionalBoolean(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        return primitive.isBoolean() ? Boolean.valueOf(primitive.getAsBoolean()) : null;
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
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"afw_animdefs_loop_seconds");

        public void reload(ResourceManager manager) {
            NonLoopSecondsOverrides.reload(manager);
        }
    }
}

