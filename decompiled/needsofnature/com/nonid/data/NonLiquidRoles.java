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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_4013;

public final class NonLiquidRoles {
    private static final String INJECTOR_KEY = "injector";
    private static final String RECEIVER_KEY = "receiver";
    private static final class_2960 PLAYER_ID = class_2960.method_60655((String)"minecraft", (String)"player");
    private static volatile Map<class_2960, LiquidRoles> ROLES_BY_ANIM = Map.of();

    private NonLiquidRoles() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((class_3264)class_3264.field_14190).registerReloader(Reloader.RELOADER_ID, (class_3302)new Reloader());
    }

    public static LiquidRoles getRoles(class_2960 animationId) {
        if (animationId == null) {
            return null;
        }
        return ROLES_BY_ANIM.get(NonPeakStages.baseAnimationId(animationId));
    }

    private static void reload(class_3300 resourceManager) {
        Map resources = resourceManager.method_14488("afw_animdefs", id -> id.method_12832().endsWith(".json"));
        HashMap<class_2960, LiquidRoles> roles = new HashMap<class_2960, LiquidRoles>();
        for (Map.Entry entry : resources.entrySet()) {
            class_2960 fileId = (class_2960)entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(((class_3298)entry.getValue()).method_14482(), StandardCharsets.UTF_8);){
                int i;
                JsonArray actorsArr;
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                if (!obj.has("actors") || (actorsArr = obj.getAsJsonArray("actors")).isEmpty()) continue;
                ArrayList<ActorInfo> actors = new ArrayList<ActorInfo>(actorsArr.size());
                for (int i2 = 0; i2 < actorsArr.size(); ++i2) {
                    JsonObject actorObj = actorsArr.get(i2).getAsJsonObject();
                    Set<class_2960> types = NonLiquidRoles.readEntityTypes(actorObj);
                    Set<String> actorTags = NonLiquidRoles.readStringArray(actorObj, "actor_tags");
                    Set<String> actorTagsAny = NonLiquidRoles.readStringArray(actorObj, "actor_tags_any");
                    String label = NonLiquidRoles.readOptionalString(actorObj);
                    AgeRequirement ageRequirement = NonLiquidRoles.readAgeRequirement(actorObj);
                    InjectorRole injectorRole = NonLiquidRoles.readInjectorRole(actorObj, INJECTOR_KEY, fileId, i2);
                    boolean receiver = NonLiquidRoles.readOptionalBool(actorObj, RECEIVER_KEY);
                    boolean isPlayer = NonLiquidRoles.isPlayerType(types);
                    actors.add(new ActorInfo(types, actorTags, actorTagsAny, label, ageRequirement, injectorRole, receiver, isPlayer));
                }
                List<String> actorKeys = NonLiquidRoles.buildUniqueActorKeys(actors);
                LinkedHashSet<String> injectorKeys = new LinkedHashSet<String>();
                LinkedHashSet<String> receiverKeys = new LinkedHashSet<String>();
                LinkedHashMap<String, InjectorRole> injectorRoles = new LinkedHashMap<String, InjectorRole>();
                boolean hasExplicitReceiver = false;
                for (i = 0; i < actors.size(); ++i) {
                    ActorInfo info = (ActorInfo)actors.get(i);
                    String key = actorKeys.get(i);
                    if (info.isLiquidInjector()) {
                        injectorKeys.add(key);
                    }
                    if (info.isAnyInjector()) {
                        injectorRoles.put(key, info.injectorRole);
                    }
                    if (!info.receiver) continue;
                    receiverKeys.add(key);
                    hasExplicitReceiver = true;
                }
                if (!hasExplicitReceiver) {
                    for (i = 0; i < actors.size(); ++i) {
                        if (((ActorInfo)actors.get(i)).isAnyInjector()) continue;
                        receiverKeys.add(actorKeys.get(i));
                    }
                }
                if (!hasExplicitReceiver && receiverKeys.isEmpty()) {
                    for (i = 0; i < actors.size(); ++i) {
                        if (!((ActorInfo)actors.get((int)i)).isPlayer) continue;
                        receiverKeys.add(actorKeys.get(i));
                    }
                }
                roles.put(NonLiquidRoles.definitionIdFromFile(fileId), new LiquidRoles(Set.copyOf(injectorKeys), Set.copyOf(receiverKeys), Map.copyOf(injectorRoles)));
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to read liquid roles from {}", (Object)fileId, (Object)e);
            }
        }
        ROLES_BY_ANIM = Map.copyOf(roles);
    }

    private static Set<class_2960> readEntityTypes(JsonObject obj) {
        if (!obj.has("entity_types")) {
            return Set.of();
        }
        JsonArray arr = obj.getAsJsonArray("entity_types");
        LinkedHashSet<class_2960> out = new LinkedHashSet<class_2960>();
        for (int i = 0; i < arr.size(); ++i) {
            String raw = arr.get(i).getAsString();
            class_2960 id = class_2960.method_12829((String)raw);
            if (id == null) continue;
            out.add(id);
        }
        return out;
    }

    private static Set<String> readStringArray(JsonObject obj, String key) {
        if (!obj.has(key)) {
            return new LinkedHashSet<String>();
        }
        JsonArray arr = obj.getAsJsonArray(key);
        LinkedHashSet<String> out = new LinkedHashSet<String>();
        for (int i = 0; i < arr.size(); ++i) {
            out.add(arr.get(i).getAsString());
        }
        return out;
    }

    private static boolean readOptionalBool(JsonObject obj, String key) {
        if (!obj.has(key)) {
            return false;
        }
        JsonElement el = obj.get(key);
        if (el == null || !el.isJsonPrimitive()) {
            return false;
        }
        JsonPrimitive prim = el.getAsJsonPrimitive();
        if (prim.isBoolean()) {
            return prim.getAsBoolean();
        }
        return false;
    }

    private static InjectorRole readInjectorRole(JsonObject obj, String key, class_2960 fileId, int actorIndex) {
        String normalized;
        if (!obj.has(key)) {
            return InjectorRole.NONE;
        }
        JsonElement el = obj.get(key);
        if (el == null || !el.isJsonPrimitive()) {
            NonLiquidRoles.warnInvalidInjector(fileId, actorIndex, "value must be true/false or V/A/M");
            return InjectorRole.NONE;
        }
        JsonPrimitive prim = el.getAsJsonPrimitive();
        if (prim.isBoolean()) {
            return prim.getAsBoolean() ? InjectorRole.V : InjectorRole.NONE;
        }
        if (!prim.isString()) {
            NonLiquidRoles.warnInvalidInjector(fileId, actorIndex, "value must be true/false or V/A/M");
            return InjectorRole.NONE;
        }
        String value = prim.getAsString();
        if (value == null || value.isBlank()) {
            return InjectorRole.NONE;
        }
        return switch (normalized = value.trim().toUpperCase(Locale.ROOT)) {
            case "V" -> InjectorRole.V;
            case "A" -> InjectorRole.A;
            case "M" -> InjectorRole.M;
            default -> {
                NonLiquidRoles.warnInvalidInjector(fileId, actorIndex, "unknown injector role '" + value + "'");
                yield InjectorRole.NONE;
            }
        };
    }

    private static void warnInvalidInjector(class_2960 fileId, int actorIndex, String reason) {
        NeedsOfNature.logSetupWarning("[NoN] Ignoring invalid injector in {} actor {}: {}. Expected true/false or V/A/M.", fileId, actorIndex, reason);
    }

    private static String readOptionalString(JsonObject obj) {
        if (!obj.has("label")) {
            return null;
        }
        JsonElement el = obj.get("label");
        if (el == null || !el.isJsonPrimitive()) {
            return null;
        }
        return el.getAsString();
    }

    private static AgeRequirement readAgeRequirement(JsonObject obj) {
        String value;
        if (!obj.has("age")) {
            return AgeRequirement.ADULT;
        }
        String raw = obj.get("age").getAsString();
        if (raw == null || raw.isBlank()) {
            return AgeRequirement.ADULT;
        }
        return switch (value = raw.toLowerCase(Locale.ROOT).trim()) {
            case "baby" -> AgeRequirement.BABY;
            default -> AgeRequirement.ADULT;
        };
    }

    private static boolean isPlayerType(Set<class_2960> types) {
        if (types == null || types.isEmpty()) {
            return false;
        }
        for (class_2960 id : types) {
            if (!PLAYER_ID.equals((Object)id)) continue;
            return true;
        }
        return false;
    }

    private static List<String> buildUniqueActorKeys(List<ActorInfo> actors) {
        String[] out = new String[actors.size()];
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < actors.size(); ++i) {
            String base = actors.get(i).derivedKey();
            int count = counts.merge(base, 1, Integer::sum);
            out[i] = count == 1 ? base : base + "_" + count;
        }
        return List.of(out);
    }

    private static String sanitizeKeyPart(String value) {
        if (value == null || value.isEmpty()) {
            return "actor";
        }
        String lower = value.toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder(lower.length());
        for (int i = 0; i < lower.length(); ++i) {
            char c = lower.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '_') {
                sb.append(c);
                continue;
            }
            sb.append('_');
        }
        return sb.toString();
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
        static final class_2960 RELOADER_ID = class_2960.method_60655((String)"needsofnature", (String)"afw_animdefs_liquid_roles");

        public void method_14491(class_3300 manager) {
            NonLiquidRoles.reload(manager);
        }
    }

    public record LiquidRoles(Set<String> injectorKeys, Set<String> receiverKeys, Map<String, InjectorRole> injectorRoles) {
    }

    private static enum AgeRequirement {
        ADULT,
        BABY;

    }

    public static enum InjectorRole {
        NONE,
        V,
        A,
        M;

    }

    private record ActorInfo(Set<class_2960> entityTypes, Set<String> actorTags, Set<String> actorTagsAny, String label, AgeRequirement ageRequirement, InjectorRole injectorRole, boolean receiver, boolean isPlayer) {
        boolean isAnyInjector() {
            return this.injectorRole != null && this.injectorRole != InjectorRole.NONE;
        }

        boolean isLiquidInjector() {
            return this.injectorRole == InjectorRole.V || this.injectorRole == InjectorRole.A;
        }

        String derivedKey() {
            if (this.label != null && !this.label.isBlank()) {
                return NonLiquidRoles.sanitizeKeyPart(this.label);
            }
            Object base = "actor";
            if (!this.entityTypes.isEmpty()) {
                if (this.entityTypes.size() == 1) {
                    base = this.entityTypes.iterator().next().method_12832();
                } else {
                    List<String> paths = this.entityTypes.stream().map(class_2960::method_12832).sorted().toList();
                    base = String.join((CharSequence)"_", paths);
                }
            }
            if (!this.actorTags.isEmpty()) {
                List<String> tags = this.actorTags.stream().sorted().map(NonLiquidRoles::sanitizeKeyPart).toList();
                base = (String)base + "_" + String.join((CharSequence)"_", tags);
            }
            if (!this.actorTagsAny.isEmpty()) {
                List<String> tagsAny = this.actorTagsAny.stream().sorted().map(NonLiquidRoles::sanitizeKeyPart).toList();
                base = (String)base + "_any_" + String.join((CharSequence)"_", tagsAny);
            }
            if (this.ageRequirement == AgeRequirement.BABY) {
                base = (String)base + "_baby";
            }
            return NonLiquidRoles.sanitizeKeyPart((String)base);
        }
    }
}

