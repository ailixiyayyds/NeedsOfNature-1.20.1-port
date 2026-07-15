/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.class_1792
 *  net.minecraft.class_1792$class_1793
 *  net.minecraft.class_2378
 *  net.minecraft.class_2960
 *  net.minecraft.class_5321
 *  net.minecraft.class_7923
 *  net.minecraft.class_9334
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.nonid.NeedsOfNature;
import com.nonid.NonAccessoryEffects;
import com.nonid.NonTrinketsIntegration;
import com.nonid.data.NonAccessoryBehavior;
import com.nonid.data.NonAccessoryDefinitions;
import com.nonid.pack.NonExternalPackProvider;
import com.nonid.pack.NonPackRootResolver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1792;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.minecraft.class_7923;
import net.minecraft.class_9334;
import org.jetbrains.annotations.Nullable;

public final class NonAccessoryItemRegistry {
    private static final String ITEM_ROOT = "assets/needsofnature/non_accessory_items";
    private static final List<class_1792> REGISTERED_ITEMS = new ArrayList<class_1792>();
    private static final Map<class_2960, AccessoryItemDefinition> DEFINITIONS_BY_ITEM = new LinkedHashMap<class_2960, AccessoryItemDefinition>();
    private static final Map<String, List<class_2960>> ITEM_IDS_BY_TRINKETS_SLOT = new LinkedHashMap<String, List<class_2960>>();

    private NonAccessoryItemRegistry() {
    }

    public static void registerStartupItems() {
        if (!REGISTERED_ITEMS.isEmpty()) {
            return;
        }
        if (!NonTrinketsIntegration.isTrinketsLoaded()) {
            NeedsOfNature.LOGGER.info("[NoN] Trinkets is not installed; skipping data-driven accessory item registration.");
            return;
        }
        LinkedHashMap<class_2960, AccessoryItemDefinition> definitions = new LinkedHashMap<class_2960, AccessoryItemDefinition>();
        NonAccessoryItemRegistry.loadBundledDefinitions(definitions);
        NonAccessoryItemRegistry.loadExternalDefinitions(definitions);
        definitions.values().forEach(NonAccessoryItemRegistry::register);
        NeedsOfNature.LOGGER.info("[NoN] Registered {} startup data-driven accessory items.", (Object)REGISTERED_ITEMS.size());
    }

    public static List<class_1792> getRegisteredItems() {
        return List.copyOf(REGISTERED_ITEMS);
    }

    public static Map<class_2960, AccessoryItemDefinition> getDefinitionsByItem() {
        return Map.copyOf(DEFINITIONS_BY_ITEM);
    }

    public static List<class_2960> getItemIdsForTrinketsSlot(String slotId) {
        if (slotId == null) {
            return List.of();
        }
        return ITEM_IDS_BY_TRINKETS_SLOT.getOrDefault(slotId, List.of());
    }

    public static SkinOverlays getSkinOverlays(class_2960 itemId) {
        if (itemId == null) {
            return SkinOverlays.EMPTY;
        }
        AccessoryItemDefinition definition = DEFINITIONS_BY_ITEM.get(itemId);
        return definition == null ? SkinOverlays.EMPTY : definition.skinOverlays();
    }

    public static Map<class_2960, ItemClientModel> getClientModelsByItem() {
        LinkedHashMap<class_2960, ItemClientModel> models = new LinkedHashMap<class_2960, ItemClientModel>();
        for (AccessoryItemDefinition definition : DEFINITIONS_BY_ITEM.values()) {
            if (definition.clientModel().isEmpty()) continue;
            models.put(definition.id(), definition.clientModel());
        }
        return Map.copyOf(models);
    }

    private static void loadBundledDefinitions(Map<class_2960, AccessoryItemDefinition> definitions) {
        FabricLoader.getInstance().getModContainer("needsofnature").ifPresent(container -> {
            for (Path root : container.getRootPaths()) {
                NonAccessoryItemRegistry.readDefinitionsFromDirectory(root.resolve(ITEM_ROOT), definitions);
            }
        });
    }

    private static void loadExternalDefinitions(Map<class_2960, AccessoryItemDefinition> definitions) {
        Path packsDir = NonExternalPackProvider.resolveDefaultPacksDir();
        if (!Files.isDirectory(packsDir, new LinkOption[0])) {
            return;
        }
        try (Stream<Path> paths = Files.list(packsDir);){
            paths.sorted(Comparator.comparing(path -> path.getFileName().toString(), String.CASE_INSENSITIVE_ORDER)).filter(NonPackRootResolver::isSupportedExternalPackPath).forEach(path -> NonAccessoryItemRegistry.readDefinitionsFromPack(path, definitions));
        }
        catch (IOException e) {
            NeedsOfNature.logSetupWarning("[NoN] Failed to scan external NoN accessory items in {}", packsDir, e);
        }
    }

    private static void readDefinitionsFromPack(Path path, Map<class_2960, AccessoryItemDefinition> definitions) {
        Optional<NonPackRootResolver.ResolvedRoot> resolved = NonPackRootResolver.resolve(path);
        if (resolved.isEmpty()) {
            return;
        }
        NonPackRootResolver.ResolvedRoot root = resolved.get();
        if (!root.isZip()) {
            NonAccessoryItemRegistry.readDefinitionsFromDirectory(root.rootPath().resolve(ITEM_ROOT), definitions);
            return;
        }
        try (ZipFile zip = new ZipFile(path.toFile());){
            zip.stream().filter(entry -> !entry.isDirectory()).filter(entry -> {
                String name = root.stripZipPrefix(entry.getName());
                return name != null && name.startsWith("assets/needsofnature/non_accessory_items/") && name.endsWith(".json");
            }).sorted(Comparator.comparing(ZipEntry::getName, String.CASE_INSENSITIVE_ORDER)).forEach(entry -> NonAccessoryItemRegistry.readDefinitionFromZip(path, root, zip, entry, definitions));
        }
        catch (IOException e) {
            NeedsOfNature.logSetupWarning("[NoN] Failed to scan accessory item pack {}", path, e);
        }
    }

    private static void readDefinitionsFromDirectory(Path dir, Map<class_2960, AccessoryItemDefinition> definitions) {
        if (!Files.isDirectory(dir, new LinkOption[0])) {
            return;
        }
        try (Stream<Path> paths = Files.list(dir);){
            paths.filter(path -> path.getFileName().toString().endsWith(".json")).sorted(Comparator.comparing(path -> path.getFileName().toString(), String.CASE_INSENSITIVE_ORDER)).forEach(path -> NonAccessoryItemRegistry.readDefinitionFromPath(path, definitions));
        }
        catch (IOException e) {
            NeedsOfNature.logSetupWarning("[NoN] Failed to scan accessory item definitions in {}", dir, e);
        }
    }

    private static void readDefinitionFromPath(Path path, Map<class_2960, AccessoryItemDefinition> definitions) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);){
            NonAccessoryItemRegistry.readDefinition(path.toString(), JsonParser.parseReader((Reader)reader).getAsJsonObject(), definitions);
        }
        catch (IOException | RuntimeException e) {
            NeedsOfNature.logSetupWarning("[NoN] Failed to read accessory item definition {}", path, e);
        }
    }

    private static void readDefinitionFromZip(Path zipPath, NonPackRootResolver.ResolvedRoot root, ZipFile zip, ZipEntry entry, Map<class_2960, AccessoryItemDefinition> definitions) {
        String logicalPath = root.stripZipPrefix(entry.getName());
        if (logicalPath == null) {
            return;
        }
        try (InputStreamReader reader = new InputStreamReader(zip.getInputStream(entry), StandardCharsets.UTF_8);){
            NonAccessoryItemRegistry.readDefinition(String.valueOf(zipPath) + "!" + logicalPath, JsonParser.parseReader((Reader)reader).getAsJsonObject(), definitions);
        }
        catch (IOException | RuntimeException e) {
            NeedsOfNature.logSetupWarning("[NoN] Failed to read accessory item definition {}!{}", zipPath, logicalPath, e);
        }
    }

    private static void readDefinition(String source, JsonObject root, Map<class_2960, AccessoryItemDefinition> definitions) {
        class_2960 id = NonAccessoryItemRegistry.parseItemId(root);
        if (id == null) {
            NeedsOfNature.logSetupWarning("[NoN] Ignoring accessory item definition {}: missing or invalid id.", source);
            return;
        }
        if (!"needsofnature".equals(id.method_12836())) {
            NeedsOfNature.logSetupWarning("[NoN] Ignoring accessory item definition {}: namespace must be '{}'.", source, "needsofnature");
            return;
        }
        if (definitions.containsKey(id)) {
            NeedsOfNature.logSetupWarning("[NoN] Ignoring duplicate accessory item definition {} for {}.", source, id);
            return;
        }
        int maxCount = NonAccessoryItemRegistry.parseInt(root.get("max_count"), 1, 1, 64);
        int maxDurability = NonAccessoryItemRegistry.parseInt(root.get("max_durability"), 0, 0, 100000);
        if (maxDurability > 0) {
            maxCount = 1;
        }
        boolean showInItemGroup = NonAccessoryItemRegistry.parseBoolean(root.get("show_in_item_group"), true);
        Set<String> trinketsSlots = NonAccessoryItemRegistry.parseTrinketsSlots(root.get("trinkets_slots"));
        if (trinketsSlots.isEmpty()) {
            trinketsSlots = Set.of("legs/v", "legs/a");
        }
        NonAccessoryEffects effects = root.has("effects") ? NonAccessoryDefinitions.parseEffectsForStartup(id, root) : NonAccessoryEffects.NEUTRAL;
        Map<String, NonAccessoryDefinitions.TooltipColor> tooltipColors = NonAccessoryDefinitions.parseTooltipColorsForStartup(id, root);
        NonAccessoryBehavior behavior = NonAccessoryDefinitions.parseBehaviorForStartup(id, root);
        SkinOverlays skinOverlays = NonAccessoryItemRegistry.parseSkinOverlays(id, root.get("skin_overlays"));
        ItemClientModel clientModel = NonAccessoryItemRegistry.parseItemClientModel(id, root);
        definitions.put(id, new AccessoryItemDefinition(id, maxCount, maxDurability, showInItemGroup, trinketsSlots, effects, tooltipColors, behavior, skinOverlays, clientModel));
    }

    private static void register(AccessoryItemDefinition definition) {
        class_5321 key = class_5321.method_29179((class_5321)class_7923.field_41178.method_46765(), (class_2960)definition.id());
        class_1792.class_1793 settings = new class_1792.class_1793().method_63686(key).method_7889(definition.maxCount());
        if (definition.maxDurability() > 0) {
            settings.method_7895(definition.maxDurability());
            settings.method_57349(class_9334.field_50072, (Object)definition.maxDurability());
            settings.method_57349(class_9334.field_49629, (Object)0);
        }
        class_1792 item = new class_1792(settings);
        class_2378.method_39197((class_2378)class_7923.field_41178, (class_5321)key, (Object)item);
        NonTrinketsIntegration.registerTrinketItem(item);
        if (definition.showInItemGroup()) {
            REGISTERED_ITEMS.add(item);
        }
        DEFINITIONS_BY_ITEM.put(definition.id(), definition);
        for (String slot : definition.trinketsSlots()) {
            ArrayList<class_2960> ids = new ArrayList<class_2960>(ITEM_IDS_BY_TRINKETS_SLOT.getOrDefault(slot, List.of()));
            ids.add(definition.id());
            ITEM_IDS_BY_TRINKETS_SLOT.put(slot, List.copyOf(ids));
        }
    }

    private static SkinOverlays parseSkinOverlays(class_2960 itemId, @Nullable JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return SkinOverlays.EMPTY;
        }
        JsonObject object = element.getAsJsonObject();
        class_2960 v = NonAccessoryItemRegistry.parseTextureIdentifier(itemId, "v", object.get("v"));
        class_2960 a = NonAccessoryItemRegistry.parseTextureIdentifier(itemId, "a", object.get("a"));
        class_2960 d = NonAccessoryItemRegistry.parseTextureIdentifier(itemId, "d", object.get("d"));
        return new SkinOverlays(v, a, d);
    }

    private static ItemClientModel parseItemClientModel(class_2960 itemId, JsonObject root) {
        class_2960 itemTexture = NonAccessoryItemRegistry.parseAssetIdentifier(itemId, root.get("item_texture"), "item/");
        class_2960 itemModel = NonAccessoryItemRegistry.parseAssetIdentifier(itemId, root.get("item_model"), "item/");
        if (itemModel == null && itemTexture != null) {
            itemModel = class_2960.method_60655((String)itemId.method_12836(), (String)("item/" + itemId.method_12832()));
        }
        return new ItemClientModel(itemModel, itemTexture);
    }

    @Nullable
    private static class_2960 parseAssetIdentifier(class_2960 itemId, @Nullable JsonElement element, String defaultPathPrefix) {
        class_2960 id;
        Object raw = NonAccessoryItemRegistry.parseString(element);
        if (raw == null || ((String)raw).isBlank()) {
            return null;
        }
        if (!((String)(raw = ((String)raw).trim())).contains(":")) {
            raw = itemId.method_12836() + ":" + defaultPathPrefix + (String)raw;
        }
        if ((id = class_2960.method_12829((String)raw)) == null) {
            NeedsOfNature.logSetupWarning("[NoN] Ignoring invalid accessory asset identifier '{}' for {}.", raw, itemId);
        }
        return id;
    }

    @Nullable
    private static class_2960 parseTextureIdentifier(class_2960 itemId, String key, @Nullable JsonElement element) {
        class_2960 id;
        Object raw = NonAccessoryItemRegistry.parseString(element);
        if (raw == null || ((String)raw).isBlank()) {
            return null;
        }
        if (!((String)(raw = ((String)raw).trim())).contains(":")) {
            raw = itemId.method_12836() + ":" + (String)raw;
        }
        if ((id = class_2960.method_12829((String)raw)) == null) {
            NeedsOfNature.logSetupWarning("[NoN] Ignoring invalid accessory skin overlay '{}' for {}.", key, itemId);
            return null;
        }
        return NonAccessoryItemRegistry.toTexturePath(id);
    }

    private static class_2960 toTexturePath(class_2960 id) {
        Object path = id.method_12832();
        if (!((String)path).startsWith("textures/")) {
            path = "textures/" + (String)path;
        }
        if (!((String)path).endsWith(".png")) {
            path = (String)path + ".png";
        }
        return class_2960.method_60655((String)id.method_12836(), (String)path);
    }

    private static Set<String> parseTrinketsSlots(@Nullable JsonElement element) {
        if (element == null) {
            return Set.of();
        }
        LinkedHashSet<String> slots = new LinkedHashSet<String>();
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                NonAccessoryItemRegistry.addTrinketsSlot(slots, NonAccessoryItemRegistry.parseString(child));
            }
        } else {
            NonAccessoryItemRegistry.addTrinketsSlot(slots, NonAccessoryItemRegistry.parseString(element));
        }
        return Set.copyOf(slots);
    }

    private static void addTrinketsSlot(Set<String> slots, @Nullable String raw) {
        if (raw == null || raw.isBlank()) {
            return;
        }
        String slot = raw.trim().toLowerCase(Locale.ROOT);
        if ("v".equals(slot)) {
            slot = "legs/v";
        }
        if ("a".equals(slot)) {
            slot = "legs/a";
        }
        if ("d".equals(slot)) {
            slot = "legs/d";
        }
        if ("legs/v".equals(slot) || "legs/a".equals(slot) || "legs/d".equals(slot)) {
            slots.add(slot);
        } else {
            NeedsOfNature.logSetupWarning("[NoN] Ignoring unknown accessory Trinkets slot '{}'.", raw);
        }
    }

    @Nullable
    private static class_2960 parseItemId(JsonObject root) {
        Object raw = NonAccessoryItemRegistry.parseString(root.get("id"));
        if (raw == null || ((String)raw).isBlank()) {
            return null;
        }
        if (!((String)(raw = ((String)raw).trim())).contains(":")) {
            raw = "needsofnature:" + (String)raw;
        }
        return class_2960.method_12829((String)raw);
    }

    @Nullable
    private static String parseString(@Nullable JsonElement element) {
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            return null;
        }
        return element.getAsString();
    }

    private static int parseInt(@Nullable JsonElement element, int fallback, int min, int max) {
        if (element == null || !element.isJsonPrimitive()) {
            return fallback;
        }
        if (!element.getAsJsonPrimitive().isNumber()) {
            return fallback;
        }
        try {
            int value = element.getAsInt();
            return Math.max(min, Math.min(max, value));
        }
        catch (RuntimeException ignored) {
            return fallback;
        }
    }

    private static boolean parseBoolean(@Nullable JsonElement element, boolean fallback) {
        if (element == null || !element.isJsonPrimitive()) {
            return fallback;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isBoolean()) {
            return primitive.getAsBoolean();
        }
        return fallback;
    }

    public record SkinOverlays(@Nullable class_2960 v, @Nullable class_2960 a, @Nullable class_2960 d) {
        public static final SkinOverlays EMPTY = new SkinOverlays(null, null, null);

        public boolean isEmpty() {
            return this.v == null && this.a == null && this.d == null;
        }

        @Nullable
        public class_2960 forSlot(String slotId) {
            if (slotId == null) {
                return null;
            }
            String slot = slotId.toLowerCase(Locale.ROOT);
            if (slot.equals("legs/v") || slot.startsWith("legs/v/")) {
                return this.v;
            }
            if (slot.equals("legs/a") || slot.startsWith("legs/a/")) {
                return this.a;
            }
            if (slot.equals("legs/d") || slot.startsWith("legs/d/")) {
                return this.d;
            }
            return null;
        }
    }

    public record AccessoryItemDefinition(class_2960 id, int maxCount, int maxDurability, boolean showInItemGroup, Set<String> trinketsSlots, NonAccessoryEffects effects, Map<String, NonAccessoryDefinitions.TooltipColor> tooltipColors, NonAccessoryBehavior behavior, SkinOverlays skinOverlays, ItemClientModel clientModel) {
    }

    public record ItemClientModel(@Nullable class_2960 itemModel, @Nullable class_2960 itemTexture) {
        public static final ItemClientModel EMPTY = new ItemClientModel(null, null);

        public boolean isEmpty() {
            return this.itemModel == null && this.itemTexture == null;
        }
    }
}

