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
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;

public final class NonPeakStages {
    private static final String STAGE_PEAK_KEY = "non_peak";
    private static volatile Map<Identifier, Integer> PEAK_STAGE_BY_ANIM = Map.of();
    private static volatile Map<Identifier, Integer> SYNCED_PEAK_STAGE_BY_ANIM = Map.of();

    private NonPeakStages() {
    }

    public static void registerReloadListener() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new Reloader());
    }

    public static Integer getPeakStage(Identifier animationId) {
        if (animationId == null) {
            return null;
        }
        Identifier base = NonPeakStages.baseAnimationId(animationId);
        Integer synced = SYNCED_PEAK_STAGE_BY_ANIM.get(base);
        return synced != null ? synced : PEAK_STAGE_BY_ANIM.get(base);
    }

    public static Map<String, Integer> snapshotPeakStagesForSync() {
        LinkedHashMap<String, Integer> out = new LinkedHashMap<String, Integer>();
        for (Map.Entry<Identifier, Integer> entry : PEAK_STAGE_BY_ANIM.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null || entry.getValue() <= 0) continue;
            out.put(entry.getKey().toString(), entry.getValue());
        }
        return out;
    }

    public static void applySyncedPeakStages(Map<String, Integer> synced) {
        if (synced == null || synced.isEmpty()) {
            SYNCED_PEAK_STAGE_BY_ANIM = Map.of();
            return;
        }
        HashMap<Identifier, Integer> parsed = new HashMap<Identifier, Integer>();
        for (Map.Entry<String, Integer> entry : synced.entrySet()) {
            Identifier id;
            if (entry == null || entry.getKey() == null || entry.getValue() == null || entry.getValue() <= 0 || (id = Identifier.tryParse((String)entry.getKey())) == null) continue;
            parsed.put(NonPeakStages.baseAnimationId(id), entry.getValue());
        }
        SYNCED_PEAK_STAGE_BY_ANIM = Map.copyOf(parsed);
    }

    public static void clearSyncedPeakStages() {
        SYNCED_PEAK_STAGE_BY_ANIM = Map.of();
    }

    private static void reload(ResourceManager resourceManager) {
        Map<Identifier, Resource> resources = resourceManager.findResources("afw_animdefs", id -> id.getPath().endsWith(".json"));
        HashMap<Identifier, Integer> peaks = new HashMap<Identifier, Integer>();
        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            Identifier fileId = (Identifier)entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry.getValue()).getInputStream(), StandardCharsets.UTF_8);){
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                if (!obj.has("stages")) continue;
                Integer peakStage = null;
                if (obj.has("stages")) {
                    JsonArray stagesArr = obj.getAsJsonArray("stages");
                    for (int i = 0; i < stagesArr.size(); ++i) {
                        JsonObject stageObj = stagesArr.get(i).getAsJsonObject();
                        Integer stageNumber = NonPeakStages.parseStageNumber(stageObj);
                        if (stageNumber == null || stageNumber <= 0 || !NonPeakStages.isPeakStage(stageObj) || peakStage != null && stageNumber >= peakStage) continue;
                        peakStage = stageNumber;
                    }
                }
                if (peakStage == null) continue;
                peaks.put(NonPeakStages.definitionIdFromFile(fileId), peakStage);
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to read peak stage from {}", (Object)fileId, (Object)e);
            }
        }
        PEAK_STAGE_BY_ANIM = Map.copyOf(peaks);
    }

    private static boolean isPeakStage(JsonObject obj) {
        if (!obj.has(STAGE_PEAK_KEY)) {
            return false;
        }
        JsonElement el = obj.get(STAGE_PEAK_KEY);
        if (el == null || !el.isJsonPrimitive()) {
            return false;
        }
        JsonPrimitive prim = el.getAsJsonPrimitive();
        if (prim.isBoolean()) {
            return prim.getAsBoolean();
        }
        return false;
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

    public static Identifier baseAnimationId(Identifier id) {
        String path = id.getPath();
        int stageIndex = path.lastIndexOf(".p");
        if (stageIndex <= 0 || stageIndex + 2 >= path.length()) {
            return id;
        }
        for (int i = stageIndex + 2; i < path.length(); ++i) {
            char c = path.charAt(i);
            if (c >= '0' && c <= '9') continue;
            return id;
        }
        return Identifier.of((String)id.getNamespace(), (String)path.substring(0, stageIndex));
    }

    public static Integer stageNumberFromId(Identifier stageId) {
        if (stageId == null) {
            return null;
        }
        String path = stageId.getPath();
        int stageIndex = path.lastIndexOf(".p");
        if (stageIndex <= 0 || stageIndex + 2 >= path.length()) {
            return 1;
        }
        String number = path.substring(stageIndex + 2);
        Integer parsed = NonPeakStages.parsePositiveInt(number);
        return parsed == null || parsed <= 0 ? 1 : parsed;
    }

    private static Integer parsePositiveInt(String raw) {
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        int value = 0;
        for (int i = 0; i < raw.length(); ++i) {
            char c = raw.charAt(i);
            if (c < '0' || c > '9') {
                return null;
            }
            int digit = c - 48;
            if (value > (Integer.MAX_VALUE - digit) / 10) {
                return null;
            }
            value = value * 10 + digit;
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
    implements SimpleSynchronousResourceReloadListener {
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"afw_animdefs_peak");

        @Override
        public Identifier getFabricId() {
            return RELOADER_ID;
        }

        public void reload(ResourceManager manager) {
            NonPeakStages.reload(manager);
        }
    }
}

