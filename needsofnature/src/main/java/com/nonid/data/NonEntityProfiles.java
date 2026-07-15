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
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.Identifier
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.Resource
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.resource.ResourceReloader
 *  net.minecraft.resource.SynchronousResourceReloader
 *  net.minecraft.registry.Registries
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

public final class NonEntityProfiles {
    private static final String ROOT_PATH = "non_entity_profiles";
    private static final String ENTRIES_KEY = "entries";
    private static final String ENTITY_KEY = "entity";
    private static final String PREGNANCY_CHANCE_KEY = "pregnancy_chance_percent";
    private static final String OFFSPRING_MIN_KEY = "offspring_min";
    private static final String OFFSPRING_MAX_KEY = "offspring_max";
    private static final String BIRTH_ENTITY_KEY = "birth_entity";
    private static final String BIRTH_MODE_KEY = "birth_mode";
    private static final String EGG_KEY = "egg";
    private static final String EGG_START_SIZE_KEY = "start_size";
    private static final String EGG_END_SIZE_KEY = "end_size";
    private static final String EGG_TEXTURE_KEY = "texture";
    private static final String EGG_HEALTH_KEY = "health";
    private static final String GENDER_SPAWN_KEY = "gender_spawn";
    private static final String MALE_CHANCE_KEY = "male_chance";
    private static final String FEMALE_CHANCE_KEY = "female_chance";
    private static final String BOTH_CHANCE_KEY = "both_chance";
    private static volatile Map<Identifier, NonConfig.EntityProfile> PACK_PROFILES_BY_ENTITY = Map.of();

    private NonEntityProfiles() {
    }

    public static void registerReloadListener() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new Reloader());
    }

    public static ResolvedProfile resolve(@Nullable Entity entity) {
        if (entity == null) {
            return NonEntityProfiles.fallbackProfile(null);
        }
        return NonEntityProfiles.resolve(Registries.ENTITY_TYPE.getId(entity.getType()));
    }

    public static ResolvedProfile resolve(@Nullable Identifier entityTypeId) {
        NonConfig.EntityProfile configProfile;
        NonConfig.EntityProfile packProfile;
        ResolvedProfile profile = NonEntityProfiles.fallbackProfile(entityTypeId);
        if (entityTypeId == null) {
            return profile;
        }
        NonConfig.EntityProfile hardcodedDefault = NonEntityProfiles.hardcodedDefaultProfile(entityTypeId);
        if (hardcodedDefault != null) {
            profile = NonEntityProfiles.apply(profile, hardcodedDefault, entityTypeId);
        }
        if ((packProfile = PACK_PROFILES_BY_ENTITY.get(entityTypeId)) != null) {
            profile = NonEntityProfiles.apply(profile, packProfile, entityTypeId);
        }
        if ((configProfile = NeedsOfNature.getConfig().getEntityProfilesByEntity().get(entityTypeId.toString())) != null) {
            profile = NonEntityProfiles.apply(profile, configProfile, entityTypeId);
        }
        return profile;
    }

    public static Map<String, NonConfig.EntityProfile> getPackProfilesByEntity() {
        if (PACK_PROFILES_BY_ENTITY.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, NonConfig.EntityProfile> out = new LinkedHashMap<String, NonConfig.EntityProfile>();
        for (Map.Entry<Identifier, NonConfig.EntityProfile> entry : PACK_PROFILES_BY_ENTITY.entrySet()) {
            out.put(entry.getKey().toString(), entry.getValue().sanitized());
        }
        return Map.copyOf(out);
    }

    public static Map<String, NonConfig.EntityProfile> resolveEffectiveProfilesMap() {
        LinkedHashSet<Identifier> ids = new LinkedHashSet<Identifier>();
        ids.addAll(NonEntityProfiles.hardcodedDefaultProfileIds());
        ids.addAll(PACK_PROFILES_BY_ENTITY.keySet());
        for (String raw : NeedsOfNature.getConfig().getEntityProfilesByEntity().keySet()) {
            Identifier id = Identifier.tryParse((String)raw);
            if (id == null) continue;
            ids.add(id);
        }
        LinkedHashMap<String, NonConfig.EntityProfile> out = new LinkedHashMap<String, NonConfig.EntityProfile>();
        for (Identifier id : ids) {
            out.put(id.toString(), NonEntityProfiles.resolve(id).toConfigProfile());
        }
        return Map.copyOf(out);
    }

    public static Map<String, NonConfig.EntityProfile> resolveDefaultProfilesMap() {
        LinkedHashSet<Identifier> ids = new LinkedHashSet<Identifier>();
        ids.addAll(NonEntityProfiles.hardcodedDefaultProfileIds());
        ids.addAll(PACK_PROFILES_BY_ENTITY.keySet());
        LinkedHashMap<String, NonConfig.EntityProfile> out = new LinkedHashMap<String, NonConfig.EntityProfile>();
        for (Identifier id : ids) {
            NonConfig.EntityProfile packProfile;
            ResolvedProfile profile = NonEntityProfiles.fallbackProfile(id);
            NonConfig.EntityProfile hardcodedDefault = NonEntityProfiles.hardcodedDefaultProfile(id);
            if (hardcodedDefault != null) {
                profile = NonEntityProfiles.apply(profile, hardcodedDefault, id);
            }
            if ((packProfile = PACK_PROFILES_BY_ENTITY.get(id)) != null) {
                profile = NonEntityProfiles.apply(profile, packProfile, id);
            }
            out.put(id.toString(), profile.toConfigProfile());
        }
        return Map.copyOf(out);
    }

    public static ResolvedProfile resolveDefaultProfile(@Nullable Identifier entityTypeId) {
        NonConfig.EntityProfile packProfile;
        ResolvedProfile profile = NonEntityProfiles.fallbackProfile(entityTypeId);
        if (entityTypeId == null) {
            return profile;
        }
        NonConfig.EntityProfile hardcodedDefault = NonEntityProfiles.hardcodedDefaultProfile(entityTypeId);
        if (hardcodedDefault != null) {
            profile = NonEntityProfiles.apply(profile, hardcodedDefault, entityTypeId);
        }
        if ((packProfile = PACK_PROFILES_BY_ENTITY.get(entityTypeId)) != null) {
            profile = NonEntityProfiles.apply(profile, packProfile, entityTypeId);
        }
        return profile;
    }

    public static Map<String, NonConfig.GenderSpawnChances> resolveEffectiveGenderSpawnMap() {
        LinkedHashMap<String, NonConfig.GenderSpawnChances> out = new LinkedHashMap<String, NonConfig.GenderSpawnChances>();
        for (Map.Entry<String, NonConfig.EntityProfile> entry : NonEntityProfiles.resolveEffectiveProfilesMap().entrySet()) {
            NonConfig.GenderSpawnChances chances = entry.getValue().genderSpawn();
            if (chances == null) continue;
            out.put(entry.getKey(), chances);
        }
        return Map.copyOf(out);
    }

    private static ResolvedProfile fallbackProfile(@Nullable Identifier entityTypeId) {
        String entity = entityTypeId == null ? "minecraft:pig" : entityTypeId.toString();
        return new ResolvedProfile(NeedsOfNature.getConfig().getPregnancyChancePercent(), 1, 1, Identifier.tryParse((String)entity) == null ? Identifier.of((String)"minecraft", (String)"pig") : Identifier.tryParse((String)entity), "direct", NeedsOfNature.getConfig().getGlobalGenderSpawnChances(), NonConfig.EggProfile.defaults());
    }

    private static ResolvedProfile apply(ResolvedProfile base, NonConfig.EntityProfile patch, Identifier sourceEntityId) {
        int max;
        if (patch == null) {
            return base;
        }
        NonConfig.EntityProfile safe = patch.sanitized();
        int chance = safe.pregnancyChancePercent() == null ? base.pregnancyChancePercent() : safe.pregnancyChancePercent().intValue();
        int min = safe.offspringMin() == null ? base.offspringMin() : safe.offspringMin().intValue();
        int n = max = safe.offspringMax() == null ? base.offspringMax() : safe.offspringMax().intValue();
        if (max < min) {
            max = min;
        }
        Identifier birthEntity = base.birthEntityTypeId();
        String rawBirthEntity = safe.birthEntity();
        if (rawBirthEntity != null) {
            Identifier parsed = Identifier.tryParse((String)rawBirthEntity);
            if (parsed != null && Registries.ENTITY_TYPE.containsId(parsed)) {
                birthEntity = parsed;
            } else {
                NeedsOfNature.logSetupWarning("[NoN setup] Ignoring invalid birth_entity '{}' for entity profile {}.", rawBirthEntity, sourceEntityId);
            }
        }
        String birthMode = safe.birthMode() == null ? base.birthMode() : safe.birthMode();
        NonConfig.GenderSpawnChances genderSpawn = safe.genderSpawn() == null ? base.genderSpawn() : safe.genderSpawn();
        NonConfig.EggProfile eggProfile = safe.egg() == null ? base.eggProfile() : safe.egg().resolvedOver(base.eggProfile());
        return new ResolvedProfile(chance, min, max, birthEntity, birthMode, genderSpawn, eggProfile);
    }

    @Nullable
    private static NonConfig.EntityProfile hardcodedDefaultProfile(Identifier entityTypeId) {
        return null;
    }

    private static Set<Identifier> hardcodedDefaultProfileIds() {
        return Set.of();
    }

    private static void reload(ResourceManager resourceManager) {
        Map<Identifier, Resource> resources = resourceManager.findResources(ROOT_PATH, id -> id.getPath().endsWith(".json"));
        ArrayList<Map.Entry<Identifier, Resource>> ordered = new ArrayList<>(resources.entrySet());
        ordered.sort(Comparator.comparing(entry -> ((Identifier)entry.getKey()).toString()));
        LinkedHashMap<Identifier, NonConfig.EntityProfile> profiles = new LinkedHashMap<Identifier, NonConfig.EntityProfile>();
        for (Map.Entry<Identifier, Resource> entry2 : ordered) {
            Identifier fileId = (Identifier)entry2.getKey();
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry2.getValue()).getInputStream(), StandardCharsets.UTF_8);){
                JsonObject root = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                JsonArray entries = NonEntityProfiles.asArray(root.get(ENTRIES_KEY));
                if (entries == null || entries.isEmpty()) continue;
                NonEntityProfiles.parseEntries(fileId, entries, profiles);
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to read entity profile file {}", (Object)fileId, (Object)e);
            }
        }
        PACK_PROFILES_BY_ENTITY = Map.copyOf(profiles);
    }

    private static void parseEntries(Identifier fileId, JsonArray entries, Map<Identifier, NonConfig.EntityProfile> profiles) {
        for (int i = 0; i < entries.size(); ++i) {
            JsonObject obj = NonEntityProfiles.asObject(entries.get(i));
            if (obj == null) {
                NonEntityProfiles.warnMalformed(fileId, i, "Entry is not an object.");
                continue;
            }
            Identifier entityId = NonEntityProfiles.parseEntityId(obj.get(ENTITY_KEY));
            if (entityId == null) {
                NonEntityProfiles.warnMalformed(fileId, i, "Missing or invalid 'entity'.");
                continue;
            }
            NonConfig.EntityProfile profile = NonEntityProfiles.parseProfile(fileId, i, obj, entityId);
            if (profile == null || profile.isEmpty()) {
                NonEntityProfiles.warnMalformed(fileId, i, "Entry has no valid profile fields.");
                continue;
            }
            profiles.put(entityId, profile);
        }
    }

    @Nullable
    private static NonConfig.EntityProfile parseProfile(Identifier fileId, int index, JsonObject obj, Identifier entityId) {
        Integer chance = NonEntityProfiles.parseOptionalInt(obj.get(PREGNANCY_CHANCE_KEY));
        Integer offspringMin = NonEntityProfiles.parseOptionalInt(obj.get(OFFSPRING_MIN_KEY));
        Integer offspringMax = NonEntityProfiles.parseOptionalInt(obj.get(OFFSPRING_MAX_KEY));
        String birthEntity = null;
        if (obj.has(BIRTH_ENTITY_KEY)) {
            Identifier parsed = NonEntityProfiles.parseEntityId(obj.get(BIRTH_ENTITY_KEY));
            if (parsed == null || !Registries.ENTITY_TYPE.containsId(parsed)) {
                NonEntityProfiles.warnMalformed(fileId, index, "Invalid 'birth_entity'.");
            } else {
                birthEntity = parsed.toString();
            }
        }
        String birthMode = null;
        if (obj.has(BIRTH_MODE_KEY)) {
            birthMode = NonEntityProfiles.parseOptionalString(obj.get(BIRTH_MODE_KEY));
            if ((birthMode = NonConfig.normalizeBirthMode(birthMode)) == null) {
                NonEntityProfiles.warnMalformed(fileId, index, "Invalid 'birth_mode'. Use 'egg' or 'direct'.");
            }
        }
        NonConfig.GenderSpawnChances genderSpawn = null;
        JsonObject genderObj = NonEntityProfiles.asObject(obj.get(GENDER_SPAWN_KEY));
        if (genderObj != null) {
            genderSpawn = NonEntityProfiles.parseGenderSpawn(fileId, index, genderObj);
        }
        NonConfig.EggProfile eggProfile = null;
        JsonObject eggObj = NonEntityProfiles.asObject(obj.get(EGG_KEY));
        if (eggObj != null) {
            eggProfile = NonEntityProfiles.parseEggProfile(fileId, index, eggObj);
        }
        if (chance != null && (chance < 0 || chance > 100)) {
            NonEntityProfiles.warnMalformed(fileId, index, "'pregnancy_chance_percent' must be 0-100.");
            chance = null;
        }
        if (offspringMin != null && (offspringMin < 1 || offspringMin > 16)) {
            NonEntityProfiles.warnMalformed(fileId, index, "'offspring_min' is out of range.");
            offspringMin = null;
        }
        if (offspringMax != null && (offspringMax < 1 || offspringMax > 16)) {
            NonEntityProfiles.warnMalformed(fileId, index, "'offspring_max' is out of range.");
            offspringMax = null;
        }
        if (offspringMin != null && offspringMax != null && offspringMax < offspringMin) {
            NonEntityProfiles.warnMalformed(fileId, index, "'offspring_max' must be at least 'offspring_min'.");
            offspringMax = offspringMin;
        }
        return new NonConfig.EntityProfile(chance, offspringMin, offspringMax, birthEntity, birthMode, genderSpawn, eggProfile).sanitized();
    }

    @Nullable
    private static NonConfig.EggProfile parseEggProfile(Identifier fileId, int index, JsonObject obj) {
        NonConfig.EggProfile profile;
        Float startSize = NonEntityProfiles.parseOptionalFloat(obj.get(EGG_START_SIZE_KEY));
        Float endSize = NonEntityProfiles.parseOptionalFloat(obj.get(EGG_END_SIZE_KEY));
        Float health = NonEntityProfiles.parseOptionalFloat(obj.get(EGG_HEALTH_KEY));
        String texture = null;
        if (obj.has(EGG_TEXTURE_KEY) && (texture = NonEntityProfiles.parseOptionalString(obj.get(EGG_TEXTURE_KEY))) != null && NonConfig.normalizeTextureIdString(texture) == null) {
            NonEntityProfiles.warnMalformed(fileId, index, "Invalid egg 'texture'.");
            texture = null;
        }
        if (obj.has(EGG_START_SIZE_KEY) && startSize == null) {
            NonEntityProfiles.warnMalformed(fileId, index, "Egg 'start_size' must be a number.");
        }
        if (obj.has(EGG_END_SIZE_KEY) && endSize == null) {
            NonEntityProfiles.warnMalformed(fileId, index, "Egg 'end_size' must be a number.");
        }
        if (obj.has(EGG_HEALTH_KEY) && health == null) {
            NonEntityProfiles.warnMalformed(fileId, index, "Egg 'health' must be a number.");
        }
        if (startSize != null && (startSize.floatValue() < 0.05f || startSize.floatValue() > 4.0f)) {
            NonEntityProfiles.warnMalformed(fileId, index, "Egg 'start_size' is out of range.");
            startSize = null;
        }
        if (endSize != null && (endSize.floatValue() < 0.05f || endSize.floatValue() > 4.0f)) {
            NonEntityProfiles.warnMalformed(fileId, index, "Egg 'end_size' is out of range.");
            endSize = null;
        }
        if (health != null && (health.floatValue() < 0.5f || health.floatValue() > 100.0f)) {
            NonEntityProfiles.warnMalformed(fileId, index, "Egg 'health' is out of range.");
            health = null;
        }
        return (profile = new NonConfig.EggProfile(startSize, endSize, texture, health).sanitized()).isEmpty() ? null : profile;
    }

    @Nullable
    private static NonConfig.GenderSpawnChances parseGenderSpawn(Identifier fileId, int index, JsonObject obj) {
        Integer male = NonEntityProfiles.parseOptionalInt(obj.get(MALE_CHANCE_KEY));
        Integer female = NonEntityProfiles.parseOptionalInt(obj.get(FEMALE_CHANCE_KEY));
        Integer both = NonEntityProfiles.parseOptionalInt(obj.get(BOTH_CHANCE_KEY));
        if (male == null || female == null || both == null) {
            NonEntityProfiles.warnMalformed(fileId, index, "Gender spawn needs male_chance, female_chance, and both_chance.");
            return null;
        }
        NonConfig.GenderSpawnChances chances = NonConfig.sanitizeGenderSpawnChances(new NonConfig.GenderSpawnChances(male, female, both));
        if (chances == null) {
            NonEntityProfiles.warnMalformed(fileId, index, "Gender spawn percentages must be 0-100 and total exactly 100.");
        }
        return chances;
    }

    @Nullable
    private static Identifier parseEntityId(@Nullable JsonElement element) {
        String raw = NonEntityProfiles.parseOptionalString(element);
        if (raw == null) {
            return null;
        }
        return Identifier.tryParse((String)raw);
    }

    @Nullable
    private static String parseOptionalString(@Nullable JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        if (!element.getAsJsonPrimitive().isString()) {
            return null;
        }
        String raw = element.getAsString();
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return raw.trim();
    }

    @Nullable
    private static Integer parseOptionalInt(@Nullable JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            double value = primitive.getAsDouble();
            if (!Double.isFinite(value) || value != Math.rint(value)) {
                return null;
            }
            if (value < -2.147483648E9 || value > 2.147483647E9) {
                return null;
            }
            return (int)value;
        }
        return null;
    }

    @Nullable
    private static Float parseOptionalFloat(@Nullable JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (!primitive.isNumber()) {
            return null;
        }
        double value = primitive.getAsDouble();
        if (!Double.isFinite(value)) {
            return null;
        }
        if (value < -3.4028234663852886E38 || value > 3.4028234663852886E38) {
            return null;
        }
        return Float.valueOf((float)value);
    }

    @Nullable
    private static JsonObject asObject(@Nullable JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        return element.getAsJsonObject();
    }

    @Nullable
    private static JsonArray asArray(@Nullable JsonElement element) {
        if (element == null || !element.isJsonArray()) {
            return null;
        }
        return element.getAsJsonArray();
    }

    private static void warnMalformed(Identifier fileId, int index, String reason) {
        NeedsOfNature.logSetupWarning("[NoN setup] Ignoring malformed entity profile entry in {} at index {}: {}", fileId, index, reason);
    }

    public static final class Reloader
    implements SimpleSynchronousResourceReloadListener {
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"entity_profiles");

        @Override
        public Identifier getFabricId() {
            return RELOADER_ID;
        }

        public void reload(ResourceManager manager) {
            NonEntityProfiles.reload(manager);
        }
    }

    public record ResolvedProfile(int pregnancyChancePercent, int offspringMin, int offspringMax, Identifier birthEntityTypeId, String birthMode, NonConfig.GenderSpawnChances genderSpawn, NonConfig.EggProfile eggProfile) {
        public ResolvedProfile {
            pregnancyChancePercent = Math.max(0, Math.min(100, pregnancyChancePercent));
            offspringMin = Math.max(1, Math.min(16, offspringMin));
            offspringMax = Math.max(offspringMin, Math.min(16, offspringMax));
            if (birthEntityTypeId == null) {
                birthEntityTypeId = Identifier.of((String)"minecraft", (String)"pig");
            }
            if ((birthMode = NonConfig.normalizeBirthMode(birthMode)) == null) {
                birthMode = "direct";
            }
            genderSpawn = NonConfig.validOrDefault(genderSpawn);
            eggProfile = eggProfile == null ? NonConfig.EggProfile.defaults() : eggProfile.resolvedOver(NonConfig.EggProfile.defaults());
        }

        public NonConfig.OffspringCountRange offspringRange() {
            return new NonConfig.OffspringCountRange(this.offspringMin, this.offspringMax);
        }

        public NonConfig.EntityProfile toConfigProfile() {
            return NonConfig.EntityProfile.full(this.pregnancyChancePercent, this.offspringMin, this.offspringMax, this.birthEntityTypeId.toString(), this.birthMode, this.genderSpawn, this.eggProfile);
        }
    }
}

