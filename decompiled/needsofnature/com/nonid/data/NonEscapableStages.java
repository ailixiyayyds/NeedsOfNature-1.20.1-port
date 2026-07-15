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
 */
package com.nonid.data;

import com.google.gson.JsonArray;
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

public final class NonEscapableStages {
    private static final String ESCAPABLE_KEY = "escapable";
    private static volatile Map<class_2960, Boolean> ESCAPABLE_BY_ANIM = Map.of();

    private NonEscapableStages() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((class_3264)class_3264.field_14190).registerReloader(Reloader.RELOADER_ID, (class_3302)new Reloader());
    }

    public static boolean isEscapable(class_2960 animationId) {
        if (animationId == null) {
            return true;
        }
        Boolean direct = ESCAPABLE_BY_ANIM.get(animationId);
        if (direct != null) {
            return direct;
        }
        class_2960 base = NonPeakStages.baseAnimationId(animationId);
        Boolean fallback = ESCAPABLE_BY_ANIM.get(base);
        return fallback == null || fallback != false;
    }

    private static void reload(class_3300 resourceManager) {
        Map resources = resourceManager.method_14488("afw_animdefs", id -> id.method_12832().endsWith(".json"));
        HashMap<class_2960, Boolean> escapable = new HashMap<class_2960, Boolean>();
        for (Map.Entry entry : resources.entrySet()) {
            class_2960 fileId = (class_2960)entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(((class_3298)entry.getValue()).method_14482(), StandardCharsets.UTF_8);){
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                class_2960 defId = NonEscapableStages.definitionIdFromFile(fileId);
                Boolean defaultEscapable = NonEscapableStages.readOptionalBool(obj);
                boolean defaultValue = defaultEscapable == null || defaultEscapable != false;
                escapable.put(defId, defaultValue);
                if (!obj.has("stages")) continue;
                JsonArray stagesArr = obj.getAsJsonArray("stages");
                for (int i = 0; i < stagesArr.size(); ++i) {
                    JsonObject stageObj = stagesArr.get(i).getAsJsonObject();
                    Integer stageNumber = NonEscapableStages.parseStageNumber(stageObj);
                    if (stageNumber == null || stageNumber <= 0) continue;
                    Boolean stageEscapable = NonEscapableStages.readOptionalBool(stageObj);
                    boolean stageValue = stageEscapable == null ? defaultValue : stageEscapable;
                    class_2960 stageId = class_2960.method_60655((String)defId.method_12836(), (String)(defId.method_12832() + ".p" + stageNumber));
                    escapable.put(stageId, stageValue);
                }
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to read escapable stages from {}", (Object)fileId, (Object)e);
            }
        }
        ESCAPABLE_BY_ANIM = Map.copyOf(escapable);
    }

    private static Boolean readOptionalBool(JsonObject obj) {
        if (!obj.has(ESCAPABLE_KEY)) {
            return null;
        }
        JsonElement el = obj.get(ESCAPABLE_KEY);
        if (el == null || !el.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive prim = el.getAsJsonPrimitive();
        if (prim.isBoolean()) {
            return prim.getAsBoolean();
        }
        return null;
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
        static final class_2960 RELOADER_ID = class_2960.method_60655((String)"needsofnature", (String)"afw_animdefs_escapable");

        public void method_14491(class_3300 manager) {
            NonEscapableStages.reload(manager);
        }
    }
}

