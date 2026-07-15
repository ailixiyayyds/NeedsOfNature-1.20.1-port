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
import com.nonid.data.NonPeakStages;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;

public final class NonEscapableStages {
    private static final String ESCAPABLE_KEY = "escapable";
    private static volatile Map<Identifier, Boolean> ESCAPABLE_BY_ANIM = Map.of();

    private NonEscapableStages() {
    }

    public static void registerReloadListener() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new Reloader());
    }

    public static boolean isEscapable(Identifier animationId) {
        if (animationId == null) {
            return true;
        }
        Boolean direct = ESCAPABLE_BY_ANIM.get(animationId);
        if (direct != null) {
            return direct;
        }
        Identifier base = NonPeakStages.baseAnimationId(animationId);
        Boolean fallback = ESCAPABLE_BY_ANIM.get(base);
        return fallback == null || fallback != false;
    }

    private static void reload(ResourceManager resourceManager) {
        Map<Identifier, Resource> resources = resourceManager.findResources("afw_animdefs", id -> id.getPath().endsWith(".json"));
        HashMap<Identifier, Boolean> escapable = new HashMap<Identifier, Boolean>();
        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            Identifier fileId = (Identifier)entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry.getValue()).getInputStream(), StandardCharsets.UTF_8);){
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                Identifier defId = NonEscapableStages.definitionIdFromFile(fileId);
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
                    Identifier stageId = Identifier.of((String)defId.getNamespace(), (String)(defId.getPath() + ".p" + stageNumber));
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
    implements SimpleSynchronousResourceReloadListener {
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"afw_animdefs_escapable");

        @Override
        public Identifier getFabricId() {
            return RELOADER_ID;
        }

        public void reload(ResourceManager manager) {
            NonEscapableStages.reload(manager);
        }
    }
}

