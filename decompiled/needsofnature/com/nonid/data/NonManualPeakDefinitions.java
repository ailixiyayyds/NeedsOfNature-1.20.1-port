/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.data.AfwAnimationDefinitions
 *  com.afwid.data.AfwAnimationDefinitions$Definition
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.minecraft.class_1297
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_3264
 *  net.minecraft.class_3298
 *  net.minecraft.class_3300
 *  net.minecraft.class_3302
 *  net.minecraft.class_4013
 *  net.minecraft.class_5819
 *  net.minecraft.class_7923
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.data;

import com.afwid.data.AfwAnimationDefinitions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nonid.NeedsOfNature;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_1297;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3264;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_4013;
import net.minecraft.class_5819;
import net.minecraft.class_7923;
import org.jetbrains.annotations.Nullable;

public final class NonManualPeakDefinitions {
    private static final String MANUAL_PEAK_TAG = "manualpeak";
    private static volatile Map<class_2960, ManualPeakRule> RULES_BY_ANIMATION = Map.of();

    private NonManualPeakDefinitions() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((class_3264)class_3264.field_14190).registerReloader(Reloader.RELOADER_ID, (class_3302)new Reloader());
    }

    @Nullable
    public static Selection select(class_3218 world, class_3222 player) {
        if (world == null || player == null) {
            return null;
        }
        List<class_3222> actors = List.of(player);
        ArrayList<AfwAnimationDefinitions.Definition> matching = new ArrayList<AfwAnimationDefinitions.Definition>();
        for (AfwAnimationDefinitions.Definition definition : AfwAnimationDefinitions.getLoadedDefinitionsSnapshot()) {
            if (definition == null || !definition.matches(actors, Set.of(MANUAL_PEAK_TAG))) continue;
            matching.add(definition);
        }
        if (matching.isEmpty()) {
            return null;
        }
        HeldItemMatch heldItemMatch = NonManualPeakDefinitions.findHeldItemMatch(matching, player);
        if (heldItemMatch != null) {
            return NonManualPeakDefinitions.buildSelection(world, actors, heldItemMatch.definition(), heldItemMatch.rule().propFromHeldItem() ? heldItemMatch.itemId() : null, heldItemMatch.itemId());
        }
        ArrayList<AfwAnimationDefinitions.Definition> fallback = new ArrayList<AfwAnimationDefinitions.Definition>();
        for (AfwAnimationDefinitions.Definition definition : matching) {
            ManualPeakRule rule = RULES_BY_ANIMATION.get(definition.id());
            if (rule != null && !rule.heldItems().isEmpty()) continue;
            fallback.add(definition);
        }
        AfwAnimationDefinitions.Definition picked = NonManualPeakDefinitions.pickWeighted(world, fallback);
        return picked == null ? null : NonManualPeakDefinitions.buildSelection(world, actors, picked, null, null);
    }

    @Nullable
    private static HeldItemMatch findHeldItemMatch(List<AfwAnimationDefinitions.Definition> definitions, class_3222 player) {
        ArrayList<HeldItemMatch> matches = new ArrayList<HeldItemMatch>();
        for (AfwAnimationDefinitions.Definition definition : definitions) {
            class_2960 itemId;
            ManualPeakRule rule = RULES_BY_ANIMATION.get(definition.id());
            if (rule == null || rule.heldItems().isEmpty() || (itemId = NonManualPeakDefinitions.firstMatchingHeldItem(player, rule.heldItems())) == null) continue;
            matches.add(new HeldItemMatch(definition, rule, itemId));
        }
        if (matches.isEmpty()) {
            return null;
        }
        matches.sort(Comparator.comparingInt(match -> match.rule().priority()).thenComparingInt(match -> match.definition().specificity()).reversed().thenComparing(match -> match.definition().id().toString()));
        return (HeldItemMatch)matches.getFirst();
    }

    @Nullable
    private static class_2960 firstMatchingHeldItem(class_3222 player, Set<class_2960> accepted) {
        class_2960 main = NonManualPeakDefinitions.itemId(player.method_6047());
        if (main != null && accepted.contains(main)) {
            return main;
        }
        class_2960 offhand = NonManualPeakDefinitions.itemId(player.method_6079());
        if (offhand != null && accepted.contains(offhand)) {
            return offhand;
        }
        return null;
    }

    @Nullable
    private static class_2960 itemId(class_1799 stack) {
        if (stack == null || stack.method_7960()) {
            return null;
        }
        return class_7923.field_41178.method_10221((Object)stack.method_7909());
    }

    @Nullable
    private static Selection buildSelection(class_3218 world, List<class_1297> actors, AfwAnimationDefinitions.Definition definition, @Nullable class_2960 rightHandPropItemId, @Nullable class_2960 matchedHeldItemId) {
        List actorKeys = AfwAnimationDefinitions.resolveActorKeys((AfwAnimationDefinitions.Definition)definition, actors, (class_5819)world.method_8409());
        if (actorKeys == null || actorKeys.size() != actors.size()) {
            return null;
        }
        return new Selection(definition, List.copyOf(actorKeys), rightHandPropItemId, matchedHeldItemId);
    }

    @Nullable
    private static AfwAnimationDefinitions.Definition pickWeighted(class_3218 world, List<AfwAnimationDefinitions.Definition> definitions) {
        if (world == null || definitions == null || definitions.isEmpty()) {
            return null;
        }
        definitions.sort(Comparator.comparingInt(AfwAnimationDefinitions.Definition::specificity).reversed().thenComparing(definition -> definition.id().toString()));
        int bestSpecificity = definitions.getFirst().specificity();
        ArrayList<AfwAnimationDefinitions.Definition> top = new ArrayList<AfwAnimationDefinitions.Definition>();
        for (AfwAnimationDefinitions.Definition definition2 : definitions) {
            if (definition2.specificity() != bestSpecificity) break;
            top.add(definition2);
        }
        if (top.isEmpty()) {
            return null;
        }
        if (top.size() == 1) {
            return (AfwAnimationDefinitions.Definition)top.getFirst();
        }
        double totalWeight = 0.0;
        for (AfwAnimationDefinitions.Definition definition3 : top) {
            totalWeight += NonManualPeakDefinitions.sanitizeWeight(definition3.weight());
        }
        if (!(totalWeight > 0.0) || !Double.isFinite(totalWeight)) {
            return (AfwAnimationDefinitions.Definition)top.get(world.method_8409().method_43048(top.size()));
        }
        double roll = world.method_8409().method_43058() * totalWeight;
        double running = 0.0;
        for (AfwAnimationDefinitions.Definition definition4 : top) {
            if (!(roll < (running += NonManualPeakDefinitions.sanitizeWeight(definition4.weight())))) continue;
            return definition4;
        }
        return (AfwAnimationDefinitions.Definition)top.getLast();
    }

    private static double sanitizeWeight(double weight) {
        return Double.isFinite(weight) && weight > 0.0 ? weight : 1.0;
    }

    private static void reload(class_3300 resourceManager) {
        Map resources = resourceManager.method_14488("afw_animdefs", id -> id.method_12832().endsWith(".json"));
        HashMap<class_2960, ManualPeakRule> rules = new HashMap<class_2960, ManualPeakRule>();
        for (Map.Entry entry : resources.entrySet()) {
            class_2960 fileId = (class_2960)entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(((class_3298)entry.getValue()).method_14482(), StandardCharsets.UTF_8);){
                ManualPeakRule rule;
                JsonObject root = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                if (!root.has("manual_peak") || !root.get("manual_peak").isJsonObject() || (rule = NonManualPeakDefinitions.parseRule(fileId, root.getAsJsonObject("manual_peak"))) == null) continue;
                rules.put(NonManualPeakDefinitions.definitionIdFromFile(fileId), rule);
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to read manual peak metadata from {}", (Object)fileId, (Object)e);
            }
        }
        RULES_BY_ANIMATION = Map.copyOf(rules);
    }

    @Nullable
    private static ManualPeakRule parseRule(class_2960 fileId, JsonObject object) {
        Set<class_2960> heldItems = NonManualPeakDefinitions.parseHeldItems(fileId, object);
        int priority = NonManualPeakDefinitions.parsePriority(object);
        boolean propFromHeldItem = NonManualPeakDefinitions.parseBoolean(object, "prop_from_held_item");
        return new ManualPeakRule(Set.copyOf(heldItems), priority, propFromHeldItem);
    }

    private static int parsePriority(JsonObject object) {
        if (!object.has("priority")) {
            return 0;
        }
        JsonElement element = object.get("priority");
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
            return 0;
        }
        return element.getAsInt();
    }

    private static boolean parseBoolean(JsonObject object, String key) {
        if (!object.has(key)) {
            return false;
        }
        JsonElement element = object.get(key);
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean()) {
            return false;
        }
        return element.getAsBoolean();
    }

    private static Set<class_2960> parseHeldItems(class_2960 fileId, JsonObject object) {
        LinkedHashSet<class_2960> out = new LinkedHashSet<class_2960>();
        if (object.has("held_item")) {
            NonManualPeakDefinitions.addItemId(fileId, out, object.get("held_item").getAsString());
        }
        if (object.has("held_items") && object.get("held_items").isJsonArray()) {
            JsonArray items = object.getAsJsonArray("held_items");
            for (int i = 0; i < items.size(); ++i) {
                if (!items.get(i).isJsonPrimitive()) continue;
                NonManualPeakDefinitions.addItemId(fileId, out, items.get(i).getAsString());
            }
        }
        return out;
    }

    private static void addItemId(class_2960 fileId, Set<class_2960> out, String raw) {
        if (raw == null || raw.isBlank()) {
            return;
        }
        class_2960 id = class_2960.method_12829((String)raw.trim());
        if (id == null || !class_7923.field_41178.method_10250(id)) {
            NeedsOfNature.logSetupWarning("[NoN] Ignoring invalid manual peak held item '{}' in {}.", raw, fileId);
            return;
        }
        out.add(id);
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
        static final class_2960 RELOADER_ID = class_2960.method_60655((String)"needsofnature", (String)"afw_animdefs_manual_peak");

        public void method_14491(class_3300 manager) {
            NonManualPeakDefinitions.reload(manager);
        }
    }

    private record HeldItemMatch(AfwAnimationDefinitions.Definition definition, ManualPeakRule rule, class_2960 itemId) {
    }

    private record ManualPeakRule(Set<class_2960> heldItems, int priority, boolean propFromHeldItem) {
    }

    public record Selection(AfwAnimationDefinitions.Definition definition, List<String> actorKeys, @Nullable class_2960 rightHandPropItemId, @Nullable class_2960 matchedHeldItemId) {
    }
}

