/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
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
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;

public final class NonAnimationEligibility {
    private static volatile Set<Identifier> ENTITY_TYPES_WITH_ADULT_ANIMATIONS = Set.of();
    private static volatile Set<Identifier> ENTITY_TYPES_WITH_BABY_ANIMATIONS = Set.of();
    private static volatile Set<ActorKey> ENTITY_TYPES_WITH_MOB_PAIR_ANIMATIONS = Set.of();
    private static volatile Map<ActorKey, Set<ActorKey>> MOB_PAIR_COMPATIBILITY = Map.of();

    private NonAnimationEligibility() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.SERVER_DATA).registerReloader(Reloader.RELOADER_ID, (ResourceReloader)new Reloader());
    }

    public static boolean hasAnimationForEntityType(Identifier entityTypeId, boolean isBaby) {
        if (entityTypeId == null) {
            return false;
        }
        if (isBaby) {
            return ENTITY_TYPES_WITH_BABY_ANIMATIONS.contains(entityTypeId);
        }
        return ENTITY_TYPES_WITH_ADULT_ANIMATIONS.contains(entityTypeId);
    }

    public static boolean hasMobPairAnimationForEntityType(Identifier entityTypeId, boolean isBaby) {
        ActorKey key = NonAnimationEligibility.actorKeyOf(entityTypeId, isBaby);
        return key != null && ENTITY_TYPES_WITH_MOB_PAIR_ANIMATIONS.contains(key);
    }

    public static boolean isMobPairCompatible(Identifier selfEntityTypeId, boolean selfIsBaby, Identifier partnerEntityTypeId, boolean partnerIsBaby) {
        ActorKey self = NonAnimationEligibility.actorKeyOf(selfEntityTypeId, selfIsBaby);
        ActorKey partner = NonAnimationEligibility.actorKeyOf(partnerEntityTypeId, partnerIsBaby);
        if (self == null || partner == null) {
            return false;
        }
        Set<ActorKey> allowedPartners = MOB_PAIR_COMPATIBILITY.get(self);
        return allowedPartners != null && allowedPartners.contains(partner);
    }

    private static void reload(ResourceManager resourceManager) {
        Map resources = resourceManager.findResources("afw_animdefs", id -> id.getPath().endsWith(".json"));
        LinkedHashSet eligibleAdultTypes = new LinkedHashSet();
        LinkedHashSet eligibleBabyTypes = new LinkedHashSet();
        LinkedHashSet<ActorKey> eligibleMobPairTypes = new LinkedHashSet<ActorKey>();
        LinkedHashMap<ActorKey, LinkedHashSet<ActorKey>> mobPairCompatibility = new LinkedHashMap<ActorKey, LinkedHashSet<ActorKey>>();
        for (Map.Entry entry : resources.entrySet()) {
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry.getValue()).getInputStream(), StandardCharsets.UTF_8);){
                JsonArray actors;
                JsonArray tags;
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                if (obj.has("animation_tags") && (tags = obj.getAsJsonArray("animation_tags")) != null && !tags.isEmpty() || !obj.has("actors") || (actors = obj.getAsJsonArray("actors")) == null || actors.size() < 2) continue;
                LinkedHashSet<Identifier> fileAdultTypes = new LinkedHashSet<Identifier>();
                LinkedHashSet<Identifier> fileBabyTypes = new LinkedHashSet<Identifier>();
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
                        Identifier parsed = Identifier.tryParse((String)raw);
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
                Identifier fileId = (Identifier)entry.getKey();
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

    private static ActorKey actorKeyOf(Identifier entityTypeId, boolean isBaby) {
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
    implements SynchronousResourceReloader {
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"afw_animdefs_eligibility");

        public void reload(ResourceManager manager) {
            NonAnimationEligibility.reload(manager);
        }
    }

    private record ActorKey(Identifier entityTypeId, AgeBucket ageBucket) {
    }

    private static enum AgeBucket {
        ADULT,
        BABY,
        INVALID;

    }
}

