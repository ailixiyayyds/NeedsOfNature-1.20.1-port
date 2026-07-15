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
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.Resource
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.resource.ResourceReloader
 *  net.minecraft.resource.SynchronousResourceReloader
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.registry.Registries
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
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.math.random.Random;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

public final class NonManualPeakDefinitions {
    private static final String MANUAL_PEAK_TAG = "manualpeak";
    private static volatile Map<Identifier, ManualPeakRule> RULES_BY_ANIMATION = Map.of();

    private NonManualPeakDefinitions() {
    }

    public static void registerReloadListener() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new Reloader());
    }

    @Nullable
    public static Selection select(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return null;
        }
        List<Entity> actors = List.of(player);
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
    private static HeldItemMatch findHeldItemMatch(List<AfwAnimationDefinitions.Definition> definitions, ServerPlayerEntity player) {
        ArrayList<HeldItemMatch> matches = new ArrayList<HeldItemMatch>();
        for (AfwAnimationDefinitions.Definition definition : definitions) {
            Identifier itemId;
            ManualPeakRule rule = RULES_BY_ANIMATION.get(definition.id());
            if (rule == null || rule.heldItems().isEmpty() || (itemId = NonManualPeakDefinitions.firstMatchingHeldItem(player, rule.heldItems())) == null) continue;
            matches.add(new HeldItemMatch(definition, rule, itemId));
        }
        if (matches.isEmpty()) {
            return null;
        }
        matches.sort(Comparator.<HeldItemMatch>comparingInt(match -> match.rule().priority()).thenComparingInt(match -> match.definition().specificity()).reversed().thenComparing(match -> match.definition().id().toString()));
        return matches.get(0);
    }

    @Nullable
    private static Identifier firstMatchingHeldItem(ServerPlayerEntity player, Set<Identifier> accepted) {
        Identifier main = NonManualPeakDefinitions.itemId(player.getMainHandStack());
        if (main != null && accepted.contains(main)) {
            return main;
        }
        Identifier offhand = NonManualPeakDefinitions.itemId(player.getOffHandStack());
        if (offhand != null && accepted.contains(offhand)) {
            return offhand;
        }
        return null;
    }

    @Nullable
    private static Identifier itemId(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return Registries.ITEM.getId(stack.getItem());
    }

    @Nullable
    private static Selection buildSelection(ServerWorld world, List<Entity> actors, AfwAnimationDefinitions.Definition definition, @Nullable Identifier rightHandPropItemId, @Nullable Identifier matchedHeldItemId) {
        List actorKeys = AfwAnimationDefinitions.resolveActorKeys((AfwAnimationDefinitions.Definition)definition, actors, (Random)world.getRandom());
        if (actorKeys == null || actorKeys.size() != actors.size()) {
            return null;
        }
        return new Selection(definition, List.copyOf(actorKeys), rightHandPropItemId, matchedHeldItemId);
    }

    @Nullable
    private static AfwAnimationDefinitions.Definition pickWeighted(ServerWorld world, List<AfwAnimationDefinitions.Definition> definitions) {
        if (world == null || definitions == null || definitions.isEmpty()) {
            return null;
        }
        definitions.sort(Comparator.comparingInt(AfwAnimationDefinitions.Definition::specificity).reversed().thenComparing(definition -> definition.id().toString()));
        int bestSpecificity = definitions.get(0).specificity();
        ArrayList<AfwAnimationDefinitions.Definition> top = new ArrayList<AfwAnimationDefinitions.Definition>();
        for (AfwAnimationDefinitions.Definition definition2 : definitions) {
            if (definition2.specificity() != bestSpecificity) break;
            top.add(definition2);
        }
        if (top.isEmpty()) {
            return null;
        }
        if (top.size() == 1) {
            return top.get(0);
        }
        double totalWeight = 0.0;
        for (AfwAnimationDefinitions.Definition definition3 : top) {
            totalWeight += NonManualPeakDefinitions.sanitizeWeight(definition3.weight());
        }
        if (!(totalWeight > 0.0) || !Double.isFinite(totalWeight)) {
            return (AfwAnimationDefinitions.Definition)top.get(world.getRandom().nextInt(top.size()));
        }
        double roll = world.getRandom().nextDouble() * totalWeight;
        double running = 0.0;
        for (AfwAnimationDefinitions.Definition definition4 : top) {
            if (!(roll < (running += NonManualPeakDefinitions.sanitizeWeight(definition4.weight())))) continue;
            return definition4;
        }
        return top.get(top.size() - 1);
    }

    private static double sanitizeWeight(double weight) {
        return Double.isFinite(weight) && weight > 0.0 ? weight : 1.0;
    }

    private static void reload(ResourceManager resourceManager) {
        Map<Identifier, Resource> resources = resourceManager.findResources("afw_animdefs", id -> id.getPath().endsWith(".json"));
        HashMap<Identifier, ManualPeakRule> rules = new HashMap<Identifier, ManualPeakRule>();
        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            Identifier fileId = (Identifier)entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry.getValue()).getInputStream(), StandardCharsets.UTF_8);){
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
    private static ManualPeakRule parseRule(Identifier fileId, JsonObject object) {
        Set<Identifier> heldItems = NonManualPeakDefinitions.parseHeldItems(fileId, object);
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

    private static Set<Identifier> parseHeldItems(Identifier fileId, JsonObject object) {
        LinkedHashSet<Identifier> out = new LinkedHashSet<Identifier>();
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

    private static void addItemId(Identifier fileId, Set<Identifier> out, String raw) {
        if (raw == null || raw.isBlank()) {
            return;
        }
        Identifier id = Identifier.tryParse((String)raw.trim());
        if (id == null || !Registries.ITEM.containsId(id)) {
            NeedsOfNature.logSetupWarning("[NoN] Ignoring invalid manual peak held item '{}' in {}.", raw, fileId);
            return;
        }
        out.add(id);
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
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"afw_animdefs_manual_peak");

        @Override
        public Identifier getFabricId() {
            return RELOADER_ID;
        }

        public void reload(ResourceManager manager) {
            NonManualPeakDefinitions.reload(manager);
        }
    }

    private record HeldItemMatch(AfwAnimationDefinitions.Definition definition, ManualPeakRule rule, Identifier itemId) {
    }

    private record ManualPeakRule(Set<Identifier> heldItems, int priority, boolean propFromHeldItem) {
    }

    public record Selection(AfwAnimationDefinitions.Definition definition, List<String> actorKeys, @Nullable Identifier rightHandPropItemId, @Nullable Identifier matchedHeldItemId) {
    }
}

