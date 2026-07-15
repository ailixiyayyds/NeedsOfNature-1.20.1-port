/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  net.minecraft.class_124
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.nonid.NeedsOfNature;
import com.nonid.NonAccessoryEffects;
import com.nonid.data.NonAccessoryBehavior;
import com.nonid.data.NonAccessoryItemRegistry;
import com.nonid.data.NonLiquidRoles;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_124;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public final class NonAccessoryDefinitions {
    private static volatile Map<class_2960, NonAccessoryItemRegistry.AccessoryItemDefinition> DEFINITIONS_BY_ITEM = Map.of();

    private NonAccessoryDefinitions() {
    }

    public static void loadStartupDefinitions() {
        DEFINITIONS_BY_ITEM = NonAccessoryItemRegistry.getDefinitionsByItem();
    }

    public static NonAccessoryEffects getEffects(class_2960 itemId) {
        if (itemId == null) {
            return NonAccessoryEffects.NEUTRAL;
        }
        NonAccessoryItemRegistry.AccessoryItemDefinition definition = DEFINITIONS_BY_ITEM.get(itemId);
        return definition == null ? NonAccessoryEffects.NEUTRAL : definition.effects();
    }

    public static boolean hasDefinition(class_2960 itemId) {
        return itemId != null && DEFINITIONS_BY_ITEM.containsKey(itemId);
    }

    public static NonAccessoryBehavior getBehavior(class_2960 itemId) {
        if (itemId == null) {
            return NonAccessoryBehavior.NONE;
        }
        NonAccessoryItemRegistry.AccessoryItemDefinition definition = DEFINITIONS_BY_ITEM.get(itemId);
        return definition == null ? NonAccessoryBehavior.NONE : definition.behavior();
    }

    @Nullable
    public static TooltipColor getTooltipColor(class_2960 itemId, String effectKey) {
        if (itemId == null || effectKey == null) {
            return null;
        }
        NonAccessoryItemRegistry.AccessoryItemDefinition definition = DEFINITIONS_BY_ITEM.get(itemId);
        Map<String, TooltipColor> colors = definition == null ? null : definition.tooltipColors();
        return colors == null ? null : colors.get(effectKey);
    }

    public static NonAccessoryEffects parseEffectsForStartup(class_2960 itemId, JsonObject root) {
        JsonObject effects = NonAccessoryDefinitions.objectOrSelf(root.get("effects"), root);
        return NonAccessoryDefinitions.parseEffectsObject(itemId, effects);
    }

    public static Map<String, TooltipColor> parseTooltipColorsForStartup(class_2960 itemId, JsonObject root) {
        JsonElement effectsElement = root.get("effects");
        if (effectsElement == null || !effectsElement.isJsonObject()) {
            return Map.of();
        }
        JsonObject effects = effectsElement.getAsJsonObject();
        return NonAccessoryDefinitions.parseInlineTooltipColors(itemId, effects);
    }

    public static NonAccessoryBehavior parseBehaviorForStartup(class_2960 itemId, JsonObject root) {
        return NonAccessoryDefinitions.parseBehavior(itemId, root);
    }

    private static NonAccessoryEffects parseEffectsObject(class_2960 fileId, JsonObject effects) {
        return new NonAccessoryEffects(NonAccessoryDefinitions.parseMultiplier(fileId, effects, "liquid_decay_multiplier", 1.0, 0.0, 20.0), NonAccessoryDefinitions.parseBoolean(effects, "equalize_liquid_decay_context", false), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "player_energy_gain_multiplier", 1.0, 0.0, 20.0), NonAccessoryDefinitions.parseInt(fileId, effects, "liquid_capacity_add", 0, -10000, 10000), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "liquid_gain_multiplier", 1.0, 0.0, 20.0), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "filled_effect_multiplier", 1.0, 0.0, 20.0), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "pregnancy_chance_multiplier", 1.0, 0.0, 20.0), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "pregnancy_duration_multiplier", 1.0, 0.01, 20.0), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "mess_gain_multiplier", 1.0, 0.0, 20.0), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "destroyed_skin_damage_multiplier", 1.0, 0.0, 20.0), NonAccessoryDefinitions.parseInt(fileId, effects, "attack_escape_hits_add", 0, -49, 49), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "attack_escape_damage_multiplier", 1.0, 0.0, 20.0), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "player_energy_aura_multiplier", 1.0, 0.0, 20.0), NonAccessoryDefinitions.parseMultiplier(fileId, effects, "near_animation_mob_energy_multiplier", 1.0, 0.0, 20.0));
    }

    private static JsonObject objectOrSelf(@Nullable JsonElement element, JsonObject fallback) {
        return element != null && element.isJsonObject() ? element.getAsJsonObject() : fallback;
    }

    private static NonAccessoryBehavior parseBehavior(class_2960 fileId, JsonObject root) {
        Set<String> occupiesSlots = NonAccessoryDefinitions.parseSlotSet(root.get("occupies_slots"));
        Set<NonLiquidRoles.InjectorRole> blockedRoles = NonAccessoryDefinitions.parseInjectorRoleSet(fileId, root.get("blocks_injector_types"));
        String exclusiveGroup = NonAccessoryDefinitions.parseOptionalString(root.get("exclusive_group"));
        int maxDurability = NonAccessoryDefinitions.parseInt(fileId, root, "max_durability", 0, 0, 100000);
        int durabilityCost = NonAccessoryDefinitions.parseInt(fileId, root, "protection_durability_cost", 1, 1, 100);
        boolean ignoreVisualShedding = NonAccessoryDefinitions.parseBoolean(root, "ignore_injector_slot_visual_shedding", false);
        boolean ignoreEffectSuppression = NonAccessoryDefinitions.parseBoolean(root, "ignore_injector_slot_effect_suppression", false);
        int vInjectionDurabilityCost = NonAccessoryDefinitions.parseInt(fileId, root, "v_injection_durability_cost", 0, 0, 100);
        int aInjectionDurabilityCost = NonAccessoryDefinitions.parseInt(fileId, root, "a_injection_durability_cost", 0, 0, 100);
        boolean blocksPregnancy = NonAccessoryDefinitions.parseBoolean(root, "blocks_pregnancy", false);
        return new NonAccessoryBehavior(occupiesSlots, blockedRoles, exclusiveGroup, maxDurability, durabilityCost, ignoreVisualShedding, ignoreEffectSuppression, vInjectionDurabilityCost, aInjectionDurabilityCost, blocksPregnancy);
    }

    private static Set<String> parseSlotSet(@Nullable JsonElement element) {
        if (element == null) {
            return Set.of();
        }
        LinkedHashSet<String> slots = new LinkedHashSet<String>();
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                NonAccessoryDefinitions.addSlot(slots, NonAccessoryDefinitions.parseOptionalString(child));
            }
        } else {
            NonAccessoryDefinitions.addSlot(slots, NonAccessoryDefinitions.parseOptionalString(element));
        }
        return Set.copyOf(slots);
    }

    private static void addSlot(Set<String> slots, @Nullable String raw) {
        if (raw == null || raw.isBlank()) {
            return;
        }
        String slot = NonAccessoryBehavior.normalizeSlot(raw);
        if ("legs/v".equals(slot) || "legs/a".equals(slot) || "legs/d".equals(slot)) {
            slots.add(slot);
        } else {
            NeedsOfNature.logSetupWarning("[NoN] Ignoring unknown accessory occupied slot '{}'.", raw);
        }
    }

    private static Set<NonLiquidRoles.InjectorRole> parseInjectorRoleSet(class_2960 fileId, @Nullable JsonElement element) {
        if (element == null) {
            return Set.of();
        }
        LinkedHashSet<NonLiquidRoles.InjectorRole> roles = new LinkedHashSet<NonLiquidRoles.InjectorRole>();
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                NonAccessoryDefinitions.addInjectorRole(fileId, roles, NonAccessoryDefinitions.parseOptionalString(child));
            }
        } else {
            NonAccessoryDefinitions.addInjectorRole(fileId, roles, NonAccessoryDefinitions.parseOptionalString(element));
        }
        return Set.copyOf(roles);
    }

    private static void addInjectorRole(class_2960 fileId, Set<NonLiquidRoles.InjectorRole> roles, @Nullable String raw) {
        if (raw == null || raw.isBlank()) {
            return;
        }
        String normalized = raw.trim().toUpperCase(Locale.ROOT);
        try {
            NonLiquidRoles.InjectorRole role = NonLiquidRoles.InjectorRole.valueOf(normalized);
            if (role != NonLiquidRoles.InjectorRole.NONE) {
                roles.add(role);
            }
        }
        catch (IllegalArgumentException ignored) {
            NonAccessoryDefinitions.warn(fileId, "Ignoring unknown blocked injector type '" + raw + "'.");
        }
    }

    @Nullable
    private static String parseOptionalString(@Nullable JsonElement element) {
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            return null;
        }
        String raw = element.getAsString();
        return raw == null || raw.isBlank() ? null : raw.trim();
    }

    private static double parseMultiplier(class_2960 fileId, JsonObject object, String key, double fallback, double min, double max) {
        Double value = NonAccessoryDefinitions.parseDouble(object.get(key));
        if (value == null) {
            return fallback;
        }
        if (!Double.isFinite(value)) {
            NonAccessoryDefinitions.warn(fileId, "Ignoring non-finite '" + key + "'.");
            return fallback;
        }
        return Math.max(min, Math.min(max, value));
    }

    private static int parseInt(class_2960 fileId, JsonObject object, String key, int fallback, int min, int max) {
        Double value = NonAccessoryDefinitions.parseDouble(object.get(key));
        if (value == null) {
            return fallback;
        }
        if (!Double.isFinite(value) || value != Math.rint(value)) {
            NonAccessoryDefinitions.warn(fileId, "Ignoring non-integer '" + key + "'.");
            return fallback;
        }
        return Math.max(min, Math.min(max, value.intValue()));
    }

    @Nullable
    private static Double parseDouble(@Nullable JsonElement element) {
        if ((element = NonAccessoryDefinitions.unwrapEffectValue(element)) == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            return primitive.getAsDouble();
        }
        return null;
    }

    private static boolean parseBoolean(JsonObject object, String key, boolean fallback) {
        JsonElement element = NonAccessoryDefinitions.unwrapEffectValue(object.get(key));
        if (element == null || !element.isJsonPrimitive()) {
            return fallback;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isBoolean()) {
            return primitive.getAsBoolean();
        }
        return fallback;
    }

    @Nullable
    private static JsonElement unwrapEffectValue(@Nullable JsonElement element) {
        if (element != null && element.isJsonObject()) {
            return element.getAsJsonObject().get("value");
        }
        return element;
    }

    private static Map<String, TooltipColor> parseInlineTooltipColors(class_2960 fileId, JsonObject effects) {
        LinkedHashMap<String, TooltipColor> colors = new LinkedHashMap<String, TooltipColor>();
        for (Map.Entry entry : effects.entrySet()) {
            JsonElement value = (JsonElement)entry.getValue();
            if (value == null || !value.isJsonObject()) continue;
            JsonElement tooltip = value.getAsJsonObject().get("tooltip");
            TooltipColor color = NonAccessoryDefinitions.parseTooltipColor(fileId, (String)entry.getKey(), tooltip);
            if (color == null) continue;
            colors.put((String)entry.getKey(), color);
        }
        return Map.copyOf(colors);
    }

    @Nullable
    private static TooltipColor parseTooltipColor(class_2960 fileId, String key, @Nullable JsonElement element) {
        String hex;
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            NonAccessoryDefinitions.warn(fileId, "Ignoring invalid tooltip color for '" + key + "'.");
            return null;
        }
        String raw = element.getAsString().trim();
        if (raw.isEmpty()) {
            return null;
        }
        if ("positive".equalsIgnoreCase(raw)) {
            return TooltipColor.formatting(class_124.field_1060);
        }
        if ("negative".equalsIgnoreCase(raw)) {
            return TooltipColor.formatting(class_124.field_1061);
        }
        if ("neutral".equalsIgnoreCase(raw)) {
            return TooltipColor.formatting(class_124.field_1068);
        }
        String string = hex = raw.startsWith("#") ? raw.substring(1) : raw;
        if (hex.length() == 6) {
            try {
                return TooltipColor.rgb(Integer.parseInt(hex, 16));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        NonAccessoryDefinitions.warn(fileId, "Ignoring unknown tooltip color '" + raw + "' for '" + key + "'.");
        return null;
    }

    private static void warn(class_2960 fileId, String reason) {
        NeedsOfNature.logSetupWarning("[NoN] Ignoring accessory definition {}: {}", fileId, reason);
    }

    public record TooltipColor(@Nullable class_124 formatting, @Nullable Integer rgb) {
        public static TooltipColor formatting(class_124 formatting) {
            return new TooltipColor(formatting, null);
        }

        public static TooltipColor rgb(int rgb) {
            return new TooltipColor(null, rgb & 0xFFFFFF);
        }
    }
}

