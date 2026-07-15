/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nonid.NeedsOfNature;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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

public final class NonAnimationEligibility {
    private static volatile Set<class_2960> ENTITY_TYPES_WITH_ADULT_ANIMATIONS = Set.of();
    private static volatile Set<class_2960> ENTITY_TYPES_WITH_BABY_ANIMATIONS = Set.of();
    private static volatile Set<ActorKey> ENTITY_TYPES_WITH_MOB_PAIR_ANIMATIONS = Set.of();
    private static volatile Map<ActorKey, Set<ActorKey>> MOB_PAIR_COMPATIBILITY = Map.of();

    private NonAnimationEligibility() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((class_3264)class_3264.field_14190).registerReloader(Reloader.RELOADER_ID, (class_3302)new Reloader());
    }

    public static boolean hasAnimationForEntityType(class_2960 entityTypeId, boolean isBaby) {
        if (entityTypeId == null) {
            return false;
        }
        if (isBaby) {
            return ENTITY_TYPES_WITH_BABY_ANIMATIONS.contains(entityTypeId);
        }
        return ENTITY_TYPES_WITH_ADULT_ANIMATIONS.contains(entityTypeId);
    }

    public static boolean hasMobPairAnimationForEntityType(class_2960 entityTypeId, boolean isBaby) {
        ActorKey key = NonAnimationEligibility.actorKeyOf(entityTypeId, isBaby);
        return key != null && ENTITY_TYPES_WITH_MOB_PAIR_ANIMATIONS.contains(key);
    }

    public static boolean isMobPairCompatible(class_2960 selfEntityTypeId, boolean selfIsBaby, class_2960 partnerEntityTypeId, boolean partnerIsBaby) {
        ActorKey self = NonAnimationEligibility.actorKeyOf(selfEntityTypeId, selfIsBaby);
        ActorKey partner = NonAnimationEligibility.actorKeyOf(partnerEntityTypeId, partnerIsBaby);
        if (self == null || partner == null) {
            return false;
        }
        Set<ActorKey> allowedPartners = MOB_PAIR_COMPATIBILITY.get(self);
        return allowedPartners != null && allowedPartners.contains(partner);
    }

    private static void reload(class_3300 resourceManager) {
        Map resources = resourceManager.method_14488("afw_animdefs", id -> id.method_12832().endsWith(".json"));
        LinkedHashSet eligibleAdultTypes = new LinkedHashSet();
        LinkedHashSet eligibleBabyTypes = new LinkedHashSet();
        LinkedHashSet<ActorKey> eligibleMobPairTypes = new LinkedHashSet<ActorKey>();
        LinkedHashMap<ActorKey, LinkedHashSet<ActorKey>> mobPairCompatibility = new LinkedHashMap<ActorKey, LinkedHashSet<ActorKey>>();
        for (Map.Entry entry : resources.entrySet()) {
            try (InputStreamReader reader = new InputStreamReader(((class_3298)entry.getValue()).method_14482(), StandardCharsets.UTF_8);){
                JsonArray actors;
                JsonArray tags;
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                if (obj.has("animation_tags") && (tags = obj.getAsJsonArray("animation_tags")) != null && !tags.isEmpty() || !obj.has("actors") || (actors = obj.getAsJsonArray("actors")) == null || actors.size() < 2) continue;
                LinkedHashSet<class_2960> fileAdultTypes = new LinkedHashSet<class_2960>();
                LinkedHashSet<class_2960> fileBabyTypes = new LinkedHashSet<class_2960>();
                ArrayList actorKeysPerActor = new ArrayList(actors.size());
                boolean invalidAge = false;
                for (int i = 0; i < actors.size(); ++i) {
                    JsonArray entityTypes;
                    JsonObject actorObj;
                    if (!actors.get(i).isJsonObject() || !(actorObj = actors.get(i).getAsJsonObject()).has("entity_types") || (entityTypes = actorObj.getAsJsonArray("entity_types")) == null || entityTypes.isEmpty()) continue;
                    AgeBucket ageBucket = NonAnimationEligibility.readAgeBucket(actorObj);
                    if (ageBucket == AgeBucket.INVALID) {
                        invalidAge = true;
                        break;
                    }
                    LinkedHashSet<ActorKey> actorKeys = new LinkedHashSet<ActorKey>();
                    for (int j = 0; j < entityTypes.size(); ++j) {
                        String raw = entityTypes.get(j).getAsString();
                        class_2960 parsed = class_2960.method_12829((String)raw);
                        if (parsed == null) continue;
                        if (ageBucket == AgeBucket.BABY) {
                            fileBabyTypes.add(parsed);
                        } else {
                            fileAdultTypes.add(parsed);
                        }
                        actorKeys.add(new ActorKey(parsed, ageBucket));
                    }
                    if (actorKeys.isEmpty()) continue;
                    actorKeysPerActor.add(actorKeys);
                }
                if (invalidAge) continue;
                eligibleAdultTypes.addAll(fileAdultTypes);
                eligibleBabyTypes.addAll(fileBabyTypes);
                if (actors.size() != 2 || actorKeysPerActor.size() != 2) continue;
                Set left = (Set)actorKeysPerActor.get(0);
                Set right = (Set)actorKeysPerActor.get(1);
                for (ActorKey leftKey : left) {
                    for (ActorKey rightKey : right) {
                        NonAnimationEligibility.addPairCompatibility(mobPairCompatibility, leftKey, rightKey);
                        NonAnimationEligibility.addPairCompatibility(mobPairCompatibility, rightKey, leftKey);
                        eligibleMobPairTypes.add(leftKey);
                        eligibleMobPairTypes.add(rightKey);
                    }
                }
            }
            catch (IOException | RuntimeException e) {
                class_2960 fileId = (class_2960)entry.getKey();
                NeedsOfNature.LOGGER.debug("[NoN] Failed to read animation eligibility from {}", (Object)fileId, (Object)e);
            }
        }
        ENTITY_TYPES_WITH_ADULT_ANIMATIONS = Set.copyOf(eligibleAdultTypes);
        ENTITY_TYPES_WITH_BABY_ANIMATIONS = Set.copyOf(eligibleBabyTypes);
        ENTITY_TYPES_WITH_MOB_PAIR_ANIMATIONS = Set.copyOf(eligibleMobPairTypes);
        LinkedHashMap immutableCompatibility = new LinkedHashMap();
        for (Map.Entry pairEntry : mobPairCompatibility.entrySet()) {
            immutableCompatibility.put((ActorKey)pairEntry.getKey(), Set.copyOf((Collection)pairEntry.getValue()));
        }
        MOB_PAIR_COMPATIBILITY = Map.copyOf(immutableCompatibility);
    }

    private static void addPairCompatibility(Map<ActorKey, LinkedHashSet<ActorKey>> compatibility, ActorKey source, ActorKey target) {
        compatibility.computeIfAbsent(source, k -> new LinkedHashSet()).add(target);
    }

    private static ActorKey actorKeyOf(class_2960 entityTypeId, boolean isBaby) {
        if (entityTypeId == null) {
            return null;
        }
        return new ActorKey(entityTypeId, isBaby ? AgeBucket.BABY : AgeBucket.ADULT);
    }

    private static AgeBucket readAgeBucket(JsonObject actorObj) {
        if (!actorObj.has("age")) {
            return AgeBucket.ADULT;
        }
        String raw = actorObj.get("age").getAsString();
        if (raw == null || raw.isBlank()) {
            return AgeBucket.ADULT;
        }
        return switch (raw.toLowerCase(Locale.ROOT).trim()) {
            case "adult" -> AgeBucket.ADULT;
            case "baby" -> AgeBucket.BABY;
            default -> AgeBucket.INVALID;
        };
    }

    public static final class Reloader
    implements class_4013 {
        static final class_2960 RELOADER_ID = class_2960.method_60655((String)"needsofnature", (String)"afw_animdefs_eligibility");

        public void method_14491(class_3300 manager) {
            NonAnimationEligibility.reload(manager);
        }
    }

    private record ActorKey(class_2960 entityTypeId, AgeBucket ageBucket) {
    }

    private static enum AgeBucket {
        ADULT,
        BABY,
        INVALID;

    }
}

