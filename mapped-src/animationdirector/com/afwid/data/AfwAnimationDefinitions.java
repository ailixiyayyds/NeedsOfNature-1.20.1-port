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
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.resource.ResourcePack
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.Resource
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.resource.ResourceReloader
 *  net.minecraft.resource.SynchronousResourceReloader
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.resource.InputSupplier
 *  net.minecraft.registry.Registries
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.data;

import com.afwid.AnimationFramework;
import com.afwid.network.AnimationStageInfo;
import com.afwid.util.AfwEntityVariants;
import com.afwid.util.AfwStageTimeWarp;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.math.random.Random;
import net.minecraft.resource.InputSupplier;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

public final class AfwAnimationDefinitions {
    private static final String NORMAL_MATCH_TAG = "normal_match";
    private static final Identifier PLAYER_ID = Identifier.of((String)"minecraft", (String)"player");
    private static final Identifier PLAYER_SLIM_ID = Identifier.of((String)"minecraft", (String)"player_slim");
    private static final String INVALID_ENTITY_VARIANT = "\u0000";
    private static final Set<String> KNOWN_DEFINITION_KEYS = Set.of("actors", "animation_tags", "content_tags", "required_union_tags", "weight", "speed", "block_requirements", "water", "position_anchor_actor", "stages", "manual_peak", "liquid_gain_multiplier", "stage_seconds", "escapable");
    private static final Set<String> KNOWN_ACTOR_KEYS = Set.of("label", "entity_types", "entity_variant", "actor_tags", "actor_tags_any", "age", "activity", "prop_left", "prop_right", "injector", "receiver");
    private static final Set<String> KNOWN_STAGE_KEYS = Set.of("stage", "use_stage", "loop", "cycle_seconds", "speed", "allow_join", "cycle_midpoint_offset_seconds", "props", "stage_seconds", "stage_duration_multiplier", "escapable", "non_peak");
    private static final Set<String> KNOWN_BLOCK_REQUIREMENT_KEYS = Set.of("type", "height", "clearance", "support", "placement", "surface_radius", "surface_footprint", "blocks");
    private static final Set<String> KNOWN_WALL_HEIGHT_KEYS = Set.of("min", "max");
    private static final Set<String> KNOWN_CLEARANCE_KEYS = Set.of("width", "height", "depth");
    private static final Set<String> KNOWN_SURFACE_FOOTPRINT_KEYS = Set.of("width", "height", "depth", "margin");
    private static final Set<String> KNOWN_STAGE_ACTOR_PROP_KEYS = Set.of("prop_left", "prop_right");
    private static volatile List<Definition> DEFINITIONS = List.of();
    private static volatile Set<Identifier> DISABLED_ANIMATION_IDS = Set.of();
    private static volatile Set<String> DISABLED_ANIMATION_PACK_IDS = Set.of();

    private AfwAnimationDefinitions() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.SERVER_DATA).registerReloader(Reloader.RELOADER_ID, (ResourceReloader)new Reloader());
    }

    public static MatchResult match(List<Entity> selectedActorsSortedById) {
        return AfwAnimationDefinitions.match(selectedActorsSortedById, Set.of());
    }

    public static MatchResult match(List<Entity> selectedActorsSortedById, Set<String> requiredAnimationTags) {
        Set<String> normalizedRequiredTags = AfwAnimationDefinitions.normalizeTags(requiredAnimationTags);
        ArrayList<Definition> candidates = new ArrayList<Definition>();
        for (Definition d2 : DEFINITIONS) {
            if (!AfwAnimationDefinitions.isDefinitionEnabled(d2) || !d2.matches(selectedActorsSortedById, normalizedRequiredTags)) continue;
            candidates.add(d2);
        }
        candidates.sort(Comparator.comparingInt(Definition::specificity).reversed().thenComparing(d -> d.id().toString()));
        Definition chosen = null;
        if (!candidates.isEmpty()) {
            Definition d3;
            int bestSpecificity = ((Definition)candidates.getFirst()).specificity();
            ArrayList<Definition> topSpecificity = new ArrayList<Definition>();
            Iterator iterator = candidates.iterator();
            while (iterator.hasNext() && (d3 = (Definition)iterator.next()).specificity() == bestSpecificity) {
                topSpecificity.add(d3);
            }
            chosen = topSpecificity.size() == 1 ? (Definition)topSpecificity.getFirst() : AfwAnimationDefinitions.pickWeighted(topSpecificity, new Random());
        }
        return new MatchResult(List.copyOf(candidates), chosen);
    }

    private static Definition pickWeighted(List<Definition> definitions, Random random) {
        if (definitions == null || definitions.isEmpty()) {
            return null;
        }
        double totalWeight = 0.0;
        for (Definition definition : definitions) {
            double weight;
            if (definition == null || !((weight = AfwAnimationDefinitions.sanitizeWeight(definition.weight())) > 0.0)) continue;
            totalWeight += weight;
        }
        if (!(totalWeight > 0.0) || !Double.isFinite(totalWeight)) {
            return definitions.get(random.nextInt(definitions.size()));
        }
        double roll = random.nextDouble() * totalWeight;
        double running = 0.0;
        for (Definition definition : definitions) {
            if (definition == null || !(roll < (running += AfwAnimationDefinitions.sanitizeWeight(definition.weight())))) continue;
            return definition;
        }
        return definitions.getLast();
    }

    private static double sanitizeWeight(double raw) {
        if (!Double.isFinite(raw) || raw <= 0.0) {
            return 1.0;
        }
        return raw;
    }

    public static Definition getDefinition(Identifier id) {
        if (id == null) {
            return null;
        }
        for (Definition d : DEFINITIONS) {
            if (!d.id().equals((Object)id)) continue;
            return d;
        }
        return null;
    }

    public static boolean isDefinitionEnabled(@Nullable Definition definition) {
        if (definition == null) {
            return false;
        }
        if (DISABLED_ANIMATION_IDS.contains(definition.id())) {
            return false;
        }
        AnimationPackInfo packInfo = definition.packInfo();
        return packInfo == null || !DISABLED_ANIMATION_PACK_IDS.contains(packInfo.id());
    }

    public static boolean isAnimationEnabled(Identifier animationId) {
        Definition definition = AfwAnimationDefinitions.getDefinition(animationId);
        return definition != null && AfwAnimationDefinitions.isDefinitionEnabled(definition);
    }

    public static boolean hasEnabledGenericAnimationForEntityType(@Nullable Identifier entityTypeId, boolean baby) {
        if (entityTypeId == null) {
            return false;
        }
        for (Definition definition : DEFINITIONS) {
            List<ActorConstraint> actors;
            if (!AfwAnimationDefinitions.isDefinitionEnabled(definition) || !AfwAnimationDefinitions.isGenericMatchDefinition(definition) || (actors = definition.actors()) == null || actors.size() < 2) continue;
            for (ActorConstraint actor : actors) {
                if (!AfwAnimationDefinitions.actorCanMatchEntityType(actor, entityTypeId, baby)) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean isGenericMatchDefinition(Definition definition) {
        if (definition == null) {
            return false;
        }
        Set<String> tags = definition.animationTags();
        return tags == null || tags.isEmpty() || tags.contains(NORMAL_MATCH_TAG);
    }

    private static boolean actorCanMatchEntityType(@Nullable ActorConstraint actor, Identifier entityTypeId, boolean baby) {
        if (actor == null || entityTypeId == null) {
            return false;
        }
        Set<Identifier> entityTypes = actor.entityTypes();
        if (entityTypes == null || entityTypes.isEmpty() || !entityTypes.contains(entityTypeId)) {
            return false;
        }
        AgeRequirement requirement = actor.ageRequirement() == null ? AgeRequirement.ADULT : actor.ageRequirement();
        return requirement == AgeRequirement.BABY == baby;
    }

    public static void setDisabledAnimationFilters(@Nullable Collection<Identifier> animationIds, @Nullable Collection<String> packIds) {
        LinkedHashSet<Identifier> cleanedAnimations = new LinkedHashSet<Identifier>();
        if (animationIds != null) {
            for (Identifier id : animationIds) {
                if (id == null) continue;
                cleanedAnimations.add(id);
            }
        }
        LinkedHashSet<String> cleanedPacks = new LinkedHashSet<String>();
        if (packIds != null) {
            for (String raw : packIds) {
                String id = AfwAnimationDefinitions.sanitizeNullable(raw);
                if (id == null) continue;
                cleanedPacks.add(id);
            }
        }
        DISABLED_ANIMATION_IDS = Set.copyOf(cleanedAnimations);
        DISABLED_ANIMATION_PACK_IDS = Set.copyOf(cleanedPacks);
    }

    public static Set<Identifier> getDisabledAnimationIdsSnapshot() {
        return Set.copyOf(DISABLED_ANIMATION_IDS);
    }

    public static Set<String> getDisabledAnimationPackIdsSnapshot() {
        return Set.copyOf(DISABLED_ANIMATION_PACK_IDS);
    }

    public static List<Definition> getLoadedDefinitionsSnapshot() {
        return List.copyOf(DEFINITIONS);
    }

    public static boolean isEmpty() {
        return DEFINITIONS.isEmpty();
    }

    public static void reloadFromServerResourceManager(ResourceManager resourceManager, String reason) {
        if (resourceManager == null) {
            return;
        }
        AnimationFramework.LOGGER.info("[AFW] Reloading AFW definitions from server resources ({})", (Object)reason);
        AfwAnimationDefinitions.reload(resourceManager);
    }

    public static List<String> resolveActorKeys(Definition definition) {
        if (definition == null || definition.actors().isEmpty()) {
            return List.of();
        }
        return List.of(AfwAnimationDefinitions.buildUniqueActorKeys(definition.actors()));
    }

    public static List<String> resolveActorKeys(Definition definition, List<Entity> selectedActorsSortedById) {
        return AfwAnimationDefinitions.resolveActorKeys(definition, selectedActorsSortedById, Random.create());
    }

    @Nullable
    public static ActorConstraint resolveActorConstraintForEntity(Definition definition, List<Entity> selectedActorsSortedById, Entity actor) {
        return AfwAnimationDefinitions.resolveActorConstraintForEntity(definition, selectedActorsSortedById, actor, Random.create());
    }

    @Nullable
    public static ActorConstraint resolveActorConstraintForEntity(Definition definition, List<Entity> selectedActorsSortedById, Entity actor, Random rng) {
        if (definition == null || selectedActorsSortedById == null || actor == null) {
            return null;
        }
        if (selectedActorsSortedById.size() != definition.actors().size()) {
            return null;
        }
        int actorIndex = -1;
        for (int i = 0; i < selectedActorsSortedById.size(); ++i) {
            Entity candidate = selectedActorsSortedById.get(i);
            if (candidate != actor && !candidate.getUuid().equals(actor.getUuid())) continue;
            actorIndex = i;
            break;
        }
        if (actorIndex < 0) {
            return null;
        }
        int[] assignment = AfwAnimationDefinitions.matchUnorderedWithAssignment(selectedActorsSortedById, definition.actors(), rng);
        if (assignment == null) {
            return null;
        }
        for (int ci = 0; ci < assignment.length; ++ci) {
            if (assignment[ci] != actorIndex) continue;
            return definition.actors().get(ci);
        }
        return null;
    }

    public static boolean canResolveActorAsActivity(Definition definition, List<Entity> selectedActorsSortedById, Entity actor, ActorActivity activity) {
        if (definition == null || selectedActorsSortedById == null || actor == null || activity == null) {
            return false;
        }
        if (selectedActorsSortedById.size() != definition.actors().size()) {
            return false;
        }
        int actorIndex = -1;
        for (int i = 0; i < selectedActorsSortedById.size(); ++i) {
            Entity candidate = selectedActorsSortedById.get(i);
            if (candidate != actor && !candidate.getUuid().equals(actor.getUuid())) continue;
            actorIndex = i;
            break;
        }
        if (actorIndex < 0) {
            return false;
        }
        List<ActorConstraint> constraints = definition.actors();
        Entity actorEntity = selectedActorsSortedById.get(actorIndex);
        for (int ci = 0; ci < constraints.size(); ++ci) {
            ActorActivity forcedActivity;
            ActorConstraint forced = constraints.get(ci);
            ActorActivity actorActivity = forcedActivity = forced.activity() == null ? ActorActivity.ACTIVE : forced.activity();
            if (forcedActivity != activity || !forced.matches(actorEntity)) continue;
            boolean[] used = new boolean[selectedActorsSortedById.size()];
            used[actorIndex] = true;
            ArrayList<Integer> order = new ArrayList<Integer>(constraints.size() - 1);
            for (int i = 0; i < constraints.size(); ++i) {
                if (i == ci) continue;
                order.add(i);
            }
            order.sort((a, b) -> {
                ActorConstraint ca = (ActorConstraint)constraints.get((int)a);
                ActorConstraint cb = (ActorConstraint)constraints.get((int)b);
                int cmp = Integer.compare(cb.specificity(), ca.specificity());
                if (cmp != 0) {
                    return cmp;
                }
                return ca.stableKey().compareTo(cb.stableKey());
            });
            if (!AfwAnimationDefinitions.backtrackAssign(0, order, constraints, selectedActorsSortedById, AfwAnimationDefinitions.buildEntityOrder(selectedActorsSortedById.size(), null), used, null)) continue;
            return true;
        }
        return false;
    }

    public static List<String> resolveActorKeys(Definition definition, List<Entity> selectedActorsSortedById, Random rng) {
        if (definition == null) {
            return List.of();
        }
        if (selectedActorsSortedById.size() != definition.actors().size()) {
            return AfwAnimationDefinitions.fallbackActorKeys(selectedActorsSortedById);
        }
        int[] assignment = AfwAnimationDefinitions.matchUnorderedWithAssignment(selectedActorsSortedById, definition.actors(), rng);
        if (assignment == null) {
            return AfwAnimationDefinitions.fallbackActorKeys(selectedActorsSortedById);
        }
        String[] constraintKeys = AfwAnimationDefinitions.buildUniqueActorKeys(definition.actors());
        String[] keysByEntityIndex = new String[selectedActorsSortedById.size()];
        for (int ci = 0; ci < assignment.length; ++ci) {
            int entityIndex = assignment[ci];
            if (entityIndex < 0 || entityIndex >= keysByEntityIndex.length) continue;
            keysByEntityIndex[entityIndex] = constraintKeys[ci];
        }
        for (int i = 0; i < keysByEntityIndex.length; ++i) {
            if (keysByEntityIndex[i] != null) continue;
            keysByEntityIndex[i] = AfwAnimationDefinitions.fallbackActorKey(selectedActorsSortedById.get(i));
        }
        return List.of(keysByEntityIndex);
    }

    private static boolean matchesUnordered(List<Entity> entitiesSortedById, List<ActorConstraint> constraints) {
        int n = entitiesSortedById.size();
        boolean[] used = new boolean[n];
        ArrayList<Integer> order = new ArrayList<Integer>(constraints.size());
        for (int i = 0; i < constraints.size(); ++i) {
            order.add(i);
        }
        order.sort((a, b) -> {
            ActorConstraint ca = (ActorConstraint)constraints.get((int)a);
            ActorConstraint cb = (ActorConstraint)constraints.get((int)b);
            int cmp = Integer.compare(cb.specificity(), ca.specificity());
            if (cmp != 0) {
                return cmp;
            }
            return ca.stableKey().compareTo(cb.stableKey());
        });
        int[] entityOrder = AfwAnimationDefinitions.buildEntityOrder(entitiesSortedById.size(), null);
        return AfwAnimationDefinitions.backtrackAssign(0, order, constraints, entitiesSortedById, entityOrder, used, null);
    }

    private static int[] matchUnorderedWithAssignment(List<Entity> entitiesSortedById, List<ActorConstraint> constraints, Random rng) {
        int n = entitiesSortedById.size();
        boolean[] used = new boolean[n];
        int[] assignment = new int[constraints.size()];
        Arrays.fill(assignment, -1);
        int[] entityOrder = AfwAnimationDefinitions.buildEntityOrder(n, rng);
        ArrayList<Integer> order = new ArrayList<Integer>(constraints.size());
        for (int i = 0; i < constraints.size(); ++i) {
            order.add(i);
        }
        order.sort((a, b) -> {
            ActorConstraint ca = (ActorConstraint)constraints.get((int)a);
            ActorConstraint cb = (ActorConstraint)constraints.get((int)b);
            int cmp = Integer.compare(cb.specificity(), ca.specificity());
            if (cmp != 0) {
                return cmp;
            }
            return ca.stableKey().compareTo(cb.stableKey());
        });
        if (AfwAnimationDefinitions.backtrackAssign(0, order, constraints, entitiesSortedById, entityOrder, used, assignment)) {
            return assignment;
        }
        return null;
    }

    private static boolean backtrackAssign(int pos, List<Integer> constraintOrder, List<ActorConstraint> constraints, List<Entity> entitiesSortedById, int[] entityOrder, boolean[] used, int[] assignment) {
        if (pos >= constraintOrder.size()) {
            return true;
        }
        int constraintIndex = constraintOrder.get(pos);
        ActorConstraint c = constraints.get(constraintIndex);
        for (int ei : entityOrder) {
            Entity e;
            if (used[ei] || !c.matches(e = entitiesSortedById.get(ei))) continue;
            used[ei] = true;
            if (assignment != null) {
                assignment[constraintIndex] = ei;
            }
            if (AfwAnimationDefinitions.backtrackAssign(pos + 1, constraintOrder, constraints, entitiesSortedById, entityOrder, used, assignment)) {
                return true;
            }
            used[ei] = false;
            if (assignment == null) continue;
            assignment[constraintIndex] = -1;
        }
        return false;
    }

    private static int[] buildEntityOrder(int size, Random rng) {
        int i;
        int[] order = new int[size];
        for (i = 0; i < size; ++i) {
            order[i] = i;
        }
        if (rng == null || size <= 1) {
            return order;
        }
        for (i = size - 1; i > 0; --i) {
            int j = rng.nextInt(i + 1);
            int tmp = order[i];
            order[i] = order[j];
            order[j] = tmp;
        }
        return order;
    }

    private static List<String> fallbackActorKeys(List<Entity> selectedActorsSortedById) {
        if (selectedActorsSortedById.isEmpty()) {
            return List.of();
        }
        String[] out = new String[selectedActorsSortedById.size()];
        for (int i = 0; i < selectedActorsSortedById.size(); ++i) {
            out[i] = AfwAnimationDefinitions.fallbackActorKey(selectedActorsSortedById.get(i));
        }
        return List.of(out);
    }

    private static String fallbackActorKey(Entity entity) {
        Identifier typeId = Registries.ENTITY_TYPE.getId((Object)entity.getType());
        return typeId.getPath();
    }

    private static String[] buildUniqueActorKeys(List<ActorConstraint> constraints) {
        String[] out = new String[constraints.size()];
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < constraints.size(); ++i) {
            String base = constraints.get(i).derivedKey();
            int count = counts.merge(base, 1, Integer::sum);
            out[i] = count == 1 ? base : base + "_" + count;
        }
        return out;
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

    private static void reload(ResourceManager resourceManager) {
        Map<String, AnimationPackInfo> packInfoByResourcePackId = AfwAnimationDefinitions.readAnimationPackInfos(resourceManager);
        Map resources = resourceManager.findResources("afw_animdefs", id -> id.getPath().endsWith(".json"));
        ArrayList<Definition> loaded = new ArrayList<Definition>();
        for (Map.Entry entry : resources.entrySet()) {
            Identifier fileId = (Identifier)entry.getKey();
            String resourcePackId = ((Resource)entry.getValue()).getPackId();
            AnimationPackInfo packInfo = packInfoByResourcePackId.getOrDefault(resourcePackId, AfwAnimationDefinitions.fallbackPackInfo(resourcePackId));
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry.getValue()).getInputStream(), StandardCharsets.UTF_8);){
                JsonObject obj = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                AfwAnimationDefinitions.warnUnknownKeys(obj, fileId, "animdef", KNOWN_DEFINITION_KEYS);
                if (!obj.has("actors")) {
                    AnimationFramework.logSetupWarning("[AFW] Skipping {}: missing 'actors' array", fileId);
                    continue;
                }
                JsonArray actorsArr = obj.getAsJsonArray("actors");
                if (actorsArr.isEmpty()) {
                    AnimationFramework.logSetupWarning("[AFW] Skipping {}: 'actors' is empty", fileId);
                    continue;
                }
                ArrayList<ActorConstraint> actorConstraints = new ArrayList<ActorConstraint>();
                for (int i = 0; i < actorsArr.size(); ++i) {
                    JsonObject a = actorsArr.get(i).getAsJsonObject();
                    AfwAnimationDefinitions.warnUnknownKeys(a, fileId, "actor[" + i + "]", KNOWN_ACTOR_KEYS);
                    Set<Identifier> types = AfwAnimationDefinitions.readEntityTypes(a, fileId);
                    if (types == null) {
                        actorConstraints = null;
                        break;
                    }
                    String entityVariant = AfwAnimationDefinitions.readEntityVariant(a, fileId, "actor[" + i + "]");
                    if (INVALID_ENTITY_VARIANT.equals(entityVariant)) {
                        actorConstraints = null;
                        break;
                    }
                    Set<String> reqTags = AfwAnimationDefinitions.readStringArray(a, "actor_tags");
                    Set<String> reqTagsAny = AfwAnimationDefinitions.readStringArray(a, "actor_tags_any");
                    String label = a.has("label") ? a.get("label").getAsString() : null;
                    AgeRequirement ageRequirement = AfwAnimationDefinitions.readAgeRequirement(a, fileId);
                    if (ageRequirement == null) {
                        actorConstraints = null;
                        break;
                    }
                    ActorActivity activity = AfwAnimationDefinitions.readActorActivity(a, fileId);
                    Identifier propLeftItemId = AfwAnimationDefinitions.readOptionalItemId(a, "prop_left", fileId, "actor[" + i + "]");
                    Identifier propRightItemId = AfwAnimationDefinitions.readOptionalItemId(a, "prop_right", fileId, "actor[" + i + "]");
                    actorConstraints.add(new ActorConstraint(Set.copyOf(types), entityVariant, Set.copyOf(reqTags), Set.copyOf(reqTagsAny), label, ageRequirement, activity, propLeftItemId, propRightItemId));
                }
                if (actorConstraints == null) continue;
                Set<String> animationTags = AfwAnimationDefinitions.normalizeTags(AfwAnimationDefinitions.readStringArray(obj, "animation_tags"));
                List<String> contentTags = AfwAnimationDefinitions.normalizeTagList(AfwAnimationDefinitions.readStringArray(obj, "content_tags"));
                Set<String> requiredUnionTags = AfwAnimationDefinitions.readStringArray(obj, "required_union_tags");
                double weight = AfwAnimationDefinitions.readWeightOrDefault(obj, fileId);
                Identifier defId = AfwAnimationDefinitions.definitionIdFromFile(fileId);
                BlockRequirements blockRequirements = AfwAnimationDefinitions.readBlockRequirements(obj, fileId);
                WaterRequirement waterRequirement = AfwAnimationDefinitions.readWaterRequirement(obj, fileId);
                String positionAnchorActor = AfwAnimationDefinitions.readOptionalAnchorActorKey(obj, "position_anchor_actor");
                List<AnimationStageInfo> stages = AfwAnimationDefinitions.readStageInfos(obj, defId, fileId, actorConstraints);
                if (stages.isEmpty()) {
                    AnimationFramework.logSetupWarning("[AFW] Skipping {}: missing 'stages' array", fileId);
                    continue;
                }
                loaded.add(new Definition(defId, List.copyOf(actorConstraints), Set.copyOf(requiredUnionTags), Set.copyOf(animationTags), contentTags, stages, weight, blockRequirements, waterRequirement, positionAnchorActor, packInfo));
            }
            catch (IOException | RuntimeException e) {
                AnimationFramework.logSetupError("[AFW] Failed to load {}", fileId, e);
            }
        }
        loaded.sort(Comparator.comparing(d -> d.id().toString()));
        DEFINITIONS = List.copyOf(loaded);
        AnimationFramework.LOGGER.info("[AFW] Loaded {} AFW definition(s) from datapacks", (Object)DEFINITIONS.size());
    }

    private static Map<String, AnimationPackInfo> readAnimationPackInfos(ResourceManager resourceManager) {
        LinkedHashMap<String, AnimationPackInfo> out = new LinkedHashMap<String, AnimationPackInfo>();
        if (resourceManager == null) {
            return out;
        }
        try (Stream stream = resourceManager.streamResourcePacks();){
            stream.forEach(pack -> {
                if (pack == null) {
                    return;
                }
                String resourcePackId = pack.getId();
                if (resourcePackId == null || resourcePackId.isBlank()) {
                    return;
                }
                AnimationPackInfo info = AfwAnimationDefinitions.readAnimationPackInfo(pack, resourcePackId);
                out.put(resourcePackId, info == null ? AfwAnimationDefinitions.fallbackPackInfo(resourcePackId) : info);
            });
        }
        catch (RuntimeException e) {
            AnimationFramework.LOGGER.debug("[AFW] Failed to read animation pack metadata from pack.mcmeta.", (Throwable)e);
        }
        return out;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Nullable
    private static AnimationPackInfo readAnimationPackInfo(ResourcePack pack, String resourcePackId) {
        try (InputStream stream = AfwAnimationDefinitions.openRootResource(pack, "pack.mcmeta");){
            JsonObject root;
            if (stream == null) {
                AnimationPackInfo animationPackInfo = null;
                return animationPackInfo;
            }
            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);){
                root = JsonParser.parseReader((Reader)reader).getAsJsonObject();
            }
            JsonObject afw = null;
            if (root.has("animationframework") && root.get("animationframework").isJsonObject()) {
                afw = root.getAsJsonObject("animationframework");
            }
            String packDescription = AfwAnimationDefinitions.readPackDescription(root);
            if (afw == null) {
                AnimationPackInfo fallback = AfwAnimationDefinitions.fallbackPackInfo(resourcePackId);
                AnimationPackInfo animationPackInfo = new AnimationPackInfo(fallback.id(), resourcePackId, fallback.name(), fallback.author(), fallback.version(), packDescription);
                return animationPackInfo;
            }
            String rawId = AfwAnimationDefinitions.readOptionalString(afw, "id");
            String id = AfwAnimationDefinitions.sanitizePackMetadataId(rawId, resourcePackId);
            String name = AfwAnimationDefinitions.readOptionalString(afw, "name");
            String author = AfwAnimationDefinitions.readOptionalString(afw, "author");
            String version = AfwAnimationDefinitions.readOptionalString(afw, "version");
            String description = AfwAnimationDefinitions.readOptionalString(afw, "description");
            if (description == null) {
                description = packDescription;
            }
            AnimationPackInfo animationPackInfo = new AnimationPackInfo(id, resourcePackId, name, author, version, description);
            return animationPackInfo;
        }
        catch (IOException | RuntimeException e) {
            AnimationFramework.LOGGER.debug("[AFW] Failed to parse AFW pack metadata for {}", (Object)resourcePackId, (Object)e);
            return null;
        }
    }

    @Nullable
    private static InputStream openRootResource(ResourcePack pack, String fileName) {
        if (pack == null || fileName == null) {
            return null;
        }
        InputSupplier supplier = pack.openRoot(new String[]{fileName});
        if (supplier == null) {
            return null;
        }
        try {
            return (InputStream)supplier.get();
        }
        catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private static AnimationPackInfo fallbackPackInfo(String resourcePackId) {
        String safePackId = AfwAnimationDefinitions.sanitizeNonBlank(resourcePackId, "unknown");
        return new AnimationPackInfo(safePackId, safePackId, AfwAnimationDefinitions.displayNameFromPackId(safePackId), "Unknown", null, null);
    }

    private static String sanitizePackMetadataId(@Nullable String rawId, String resourcePackId) {
        String fallback = AfwAnimationDefinitions.sanitizeNonBlank(resourcePackId, "unknown");
        String cleaned = AfwAnimationDefinitions.sanitizeNullable(rawId);
        if (cleaned == null) {
            return fallback;
        }
        if (Identifier.tryParse((String)cleaned) == null) {
            AnimationFramework.logSetupWarning("[AFW] Ignoring invalid animationframework.id '{}' in pack.mcmeta for {}. Use a namespaced id like creator:pack_name.", cleaned, resourcePackId);
            return fallback;
        }
        return cleaned;
    }

    @Nullable
    private static String readPackDescription(JsonObject root) {
        if (root == null || !root.has("pack") || !root.get("pack").isJsonObject()) {
            return null;
        }
        JsonObject pack = root.getAsJsonObject("pack");
        return AfwAnimationDefinitions.readOptionalString(pack, "description");
    }

    @Nullable
    private static String readOptionalString(JsonObject object, String key) {
        if (object == null || key == null || !object.has(key)) {
            return null;
        }
        JsonElement element = object.get(key);
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        try {
            return AfwAnimationDefinitions.sanitizeNullable(element.getAsString());
        }
        catch (RuntimeException e) {
            return null;
        }
    }

    private static String displayNameFromPackId(String rawPackId) {
        int underscore;
        int colon;
        String id = AfwAnimationDefinitions.sanitizeNonBlank(rawPackId, "Unknown Pack");
        int slash = id.lastIndexOf(47);
        if (slash >= 0 && slash < id.length() - 1) {
            id = id.substring(slash + 1);
        }
        if ((colon = id.indexOf(58)) >= 0 && colon < id.length() - 1) {
            id = id.substring(colon + 1);
        }
        if ((underscore = id.indexOf(95)) > 0 && AfwAnimationDefinitions.isNumericPrefix(id, underscore)) {
            id = id.substring(underscore + 1);
        }
        return (id = id.replace('_', ' ').replace('-', ' ').trim()).isBlank() ? "Unknown Pack" : id;
    }

    private static boolean isNumericPrefix(String value, int endExclusive) {
        if (value == null || endExclusive <= 0 || endExclusive > value.length()) {
            return false;
        }
        for (int i = 0; i < endExclusive; ++i) {
            char c = value.charAt(i);
            if (c >= '0' && c <= '9') continue;
            return false;
        }
        return true;
    }

    private static String sanitizeNonBlank(@Nullable String value, String fallback) {
        String cleaned = AfwAnimationDefinitions.sanitizeNullable(value);
        return cleaned == null ? fallback : cleaned;
    }

    @Nullable
    private static String sanitizeNullable(@Nullable String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static void warnUnknownKeys(JsonObject obj, Identifier fileId, String context, Set<String> knownKeys) {
        if (obj == null || knownKeys == null || knownKeys.isEmpty()) {
            return;
        }
        String safeContext = context == null || context.isBlank() ? "object" : context;
        for (Map.Entry entry : obj.entrySet()) {
            String key = (String)entry.getKey();
            if (key == null || knownKeys.contains(key)) continue;
            AnimationFramework.logSetupWarning("[AFW] Unknown field '{}' in {} {}. It will be ignored; check for typos.", key, fileId == null ? "<unknown>" : fileId, safeContext);
        }
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

    private static double readWeightOrDefault(JsonObject obj, Identifier fileId) {
        double value;
        if (obj == null || !obj.has("weight")) {
            return 1.0;
        }
        JsonElement element = obj.get("weight");
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
            AnimationFramework.logSetupWarning("[AFW] Invalid weight in {} (must be a positive number). Using default 1.0.", fileId);
            return 1.0;
        }
        try {
            value = element.getAsDouble();
        }
        catch (RuntimeException ignored) {
            AnimationFramework.logSetupWarning("[AFW] Invalid weight in {} (must be a positive number). Using default 1.0.", fileId);
            return 1.0;
        }
        if (!Double.isFinite(value) || value <= 0.0) {
            AnimationFramework.logSetupWarning("[AFW] Invalid weight '{}' in {} (must be > 0). Using default 1.0.", value, fileId);
            return 1.0;
        }
        return value;
    }

    @Nullable
    private static String readOptionalAnchorActorKey(JsonObject obj, String key) {
        if (obj == null || key == null || key.isBlank()) {
            return null;
        }
        if (!obj.has(key)) {
            return null;
        }
        JsonElement element = obj.get(key);
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        String raw = element.getAsString();
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static Set<String> normalizeTags(Set<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Set.of();
        }
        LinkedHashSet<String> out = new LinkedHashSet<String>();
        for (String raw : tags) {
            String value;
            if (raw == null || (value = raw.trim().toLowerCase(Locale.ROOT)).isEmpty()) continue;
            out.add(value);
        }
        return out.isEmpty() ? Set.of() : Set.copyOf(out);
    }

    private static AgeRequirement readAgeRequirement(JsonObject obj, Identifier fileId) {
        String value;
        if (!obj.has("age")) {
            return AgeRequirement.ADULT;
        }
        String raw = obj.get("age").getAsString();
        if (raw == null || raw.isBlank()) {
            return AgeRequirement.ADULT;
        }
        return switch (value = raw.toLowerCase(Locale.ROOT).trim()) {
            case "adult" -> AgeRequirement.ADULT;
            case "baby" -> AgeRequirement.BABY;
            default -> {
                AnimationFramework.logSetupWarning("[AFW] Unknown age '{}' in {} (expected adult|baby). Skipping definition.", raw, fileId);
                yield null;
            }
        };
    }

    private static ActorActivity readActorActivity(JsonObject obj, Identifier fileId) {
        String value;
        if (!obj.has("activity")) {
            return ActorActivity.ACTIVE;
        }
        String raw = obj.get("activity").getAsString();
        if (raw == null || raw.isBlank()) {
            return ActorActivity.ACTIVE;
        }
        return switch (value = raw.toLowerCase(Locale.ROOT).trim()) {
            case "active" -> ActorActivity.ACTIVE;
            case "passive" -> ActorActivity.PASSIVE;
            default -> {
                AnimationFramework.logSetupWarning("[AFW] Unknown actor activity '{}' in {} (expected active|passive). Using active.", raw, fileId);
                yield ActorActivity.ACTIVE;
            }
        };
    }

    private static BlockRequirements readBlockRequirements(JsonObject obj, Identifier fileId) {
        if (!obj.has("block_requirements")) {
            return null;
        }
        JsonObject req = obj.getAsJsonObject("block_requirements");
        if (req == null) {
            return null;
        }
        AfwAnimationDefinitions.warnUnknownKeys(req, fileId, "block_requirements", KNOWN_BLOCK_REQUIREMENT_KEYS);
        String type = req.has("type") ? req.get("type").getAsString() : "wall";
        String normalizedType = type == null ? "wall" : type.trim().toLowerCase(Locale.ROOT);
        BlockPredicate blocks = AfwAnimationDefinitions.readBlockPredicate(req, fileId);
        if (req.has("blocks") && blocks == null) {
            return null;
        }
        if ("wall".equals(normalizedType)) {
            WallHeight height = AfwAnimationDefinitions.readWallHeight(req, fileId);
            if (height == null) {
                return null;
            }
            Clearance clearance = AfwAnimationDefinitions.readClearance(req, fileId);
            return new BlockRequirements("wall", height, clearance, null, null, 0, null, blocks);
        }
        if ("center_support".equals(normalizedType)) {
            CenterSupport support = AfwAnimationDefinitions.readCenterSupport(req, fileId);
            if (support == null) {
                return null;
            }
            if (support == CenterSupport.SURFACE && (blocks == null || blocks.isEmpty())) {
                AnimationFramework.logSetupWarning("[AFW] Skipping {}: center_support surface requires 'blocks'.", fileId);
                return null;
            }
            CenterPlacement placement = AfwAnimationDefinitions.readCenterPlacement(req, fileId);
            if (placement == null) {
                return null;
            }
            Clearance clearance = null;
            int surfaceRadius = 0;
            SurfaceFootprint surfaceFootprint = null;
            if (placement == CenterPlacement.SURFACE_FOOTPRINT) {
                surfaceFootprint = AfwAnimationDefinitions.readSurfaceFootprint(req, fileId);
                if (surfaceFootprint == null) {
                    return null;
                }
                if (req.has("clearance")) {
                    AnimationFramework.logSetupWarning("[AFW] {}: center_support surface_footprint ignores 'clearance'.", fileId);
                }
                if (req.has("surface_radius")) {
                    AnimationFramework.logSetupWarning("[AFW] {}: center_support surface_footprint ignores 'surface_radius'.", fileId);
                }
            } else {
                clearance = AfwAnimationDefinitions.readClearance(req, fileId);
                surfaceRadius = AfwAnimationDefinitions.readNonNegativeInt(req, "surface_radius", 0);
                if (req.has("surface_footprint")) {
                    AnimationFramework.logSetupWarning("[AFW] {}: center_support directional_clearance ignores 'surface_footprint'.", fileId);
                }
            }
            return new BlockRequirements("center_support", null, clearance, support, placement, surfaceRadius, surfaceFootprint, blocks);
        }
        AnimationFramework.logSetupWarning("[AFW] Skipping {}: unsupported block_requirements type '{}'", fileId, type);
        return null;
    }

    private static CenterPlacement readCenterPlacement(JsonObject req, Identifier fileId) {
        if (!req.has("placement")) {
            return CenterPlacement.DIRECTIONAL_CLEARANCE;
        }
        String raw = req.get("placement").getAsString();
        if (raw == null || raw.isBlank()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: center_support block_requirements has empty placement", fileId);
            return null;
        }
        String value = raw.trim().toLowerCase(Locale.ROOT);
        for (CenterPlacement placement : CenterPlacement.values()) {
            if (!placement.id().equals(value)) continue;
            return placement;
        }
        AnimationFramework.logSetupWarning("[AFW] Skipping {}: unsupported center_support placement '{}'", fileId, raw);
        return null;
    }

    private static CenterSupport readCenterSupport(JsonObject req, Identifier fileId) {
        if (!req.has("support")) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: center_support block_requirements missing support", fileId);
            return null;
        }
        String raw = req.get("support").getAsString();
        if (raw == null || raw.isBlank()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: center_support block_requirements has empty support", fileId);
            return null;
        }
        String value = raw.trim().toLowerCase(Locale.ROOT);
        for (CenterSupport support : CenterSupport.values()) {
            if (!support.id().equals(value)) continue;
            return support;
        }
        AnimationFramework.logSetupWarning("[AFW] Skipping {}: unsupported center_support support '{}'", fileId, raw);
        return null;
    }

    @Nullable
    private static SurfaceFootprint readSurfaceFootprint(JsonObject req, Identifier fileId) {
        if (!req.has("surface_footprint")) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: center_support surface_footprint placement requires 'surface_footprint'.", fileId);
            return null;
        }
        JsonElement element = req.get("surface_footprint");
        if (element == null || !element.isJsonObject()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: center_support surface_footprint must be an object.", fileId);
            return null;
        }
        JsonObject obj = element.getAsJsonObject();
        AfwAnimationDefinitions.warnUnknownKeys(obj, fileId, "block_requirements.surface_footprint", KNOWN_SURFACE_FOOTPRINT_KEYS);
        int width = AfwAnimationDefinitions.readPositiveInt(obj, "width", 1);
        int depth = AfwAnimationDefinitions.readPositiveInt(obj, "depth", 1);
        int height = AfwAnimationDefinitions.readPositiveInt(obj, "height", 2);
        int margin = AfwAnimationDefinitions.readNonNegativeInt(obj, "margin", 0);
        return new SurfaceFootprint(width, depth, height, margin);
    }

    @Nullable
    private static BlockPredicate readBlockPredicate(JsonObject req, Identifier fileId) {
        if (!req.has("blocks")) {
            return null;
        }
        JsonElement blocksElement = req.get("blocks");
        if (blocksElement == null || blocksElement.isJsonNull()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: block_requirements blocks must not be null.", fileId);
            return null;
        }
        LinkedHashSet<Identifier> blockIds = new LinkedHashSet<Identifier>();
        LinkedHashSet<Identifier> tagIds = new LinkedHashSet<Identifier>();
        if (blocksElement.isJsonArray()) {
            JsonArray arr = blocksElement.getAsJsonArray();
            if (arr.isEmpty()) {
                AnimationFramework.logSetupWarning("[AFW] Skipping {}: block_requirements blocks array is empty.", fileId);
                return null;
            }
            for (JsonElement element : arr) {
                if (AfwAnimationDefinitions.readBlockPredicateEntry(element, fileId, blockIds, tagIds)) continue;
                return null;
            }
        } else if (!AfwAnimationDefinitions.readBlockPredicateEntry(blocksElement, fileId, blockIds, tagIds)) {
            return null;
        }
        if (blockIds.isEmpty() && tagIds.isEmpty()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: block_requirements blocks did not contain any valid entries.", fileId);
            return null;
        }
        return new BlockPredicate(Set.copyOf(blockIds), Set.copyOf(tagIds));
    }

    private static boolean readBlockPredicateEntry(JsonElement element, Identifier fileId, Set<Identifier> blockIds, Set<Identifier> tagIds) {
        String idText;
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: block_requirements blocks entries must be strings.", fileId);
            return false;
        }
        String raw = element.getAsString();
        if (raw == null || raw.isBlank()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: block_requirements blocks contains an empty entry.", fileId);
            return false;
        }
        String trimmed = raw.trim();
        boolean tag = trimmed.startsWith("#");
        String string = idText = tag ? trimmed.substring(1) : trimmed;
        if (!idText.contains(":")) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: block_requirements blocks entry '{}' must use a namespaced id like 'minecraft:chest' or '#minecraft:beds'.", fileId, raw);
            return false;
        }
        Identifier id = Identifier.tryParse((String)idText);
        if (id == null) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: invalid block_requirements blocks identifier '{}'.", fileId, raw);
            return false;
        }
        if (tag) {
            tagIds.add(id);
        } else {
            if (!Registries.BLOCK.containsId(id)) {
                AnimationFramework.logSetupWarning("[AFW] Skipping {}: unknown block id '{}' in block_requirements blocks.", fileId, raw);
                return false;
            }
            blockIds.add(id);
        }
        return true;
    }

    private static WaterRequirement readWaterRequirement(JsonObject obj, Identifier fileId) {
        String value;
        if (!obj.has("water")) {
            return WaterRequirement.NONE;
        }
        JsonElement el = obj.get("water");
        if (el == null || !el.isJsonPrimitive()) {
            return WaterRequirement.NONE;
        }
        String raw = el.getAsString();
        if (raw == null || raw.isBlank()) {
            return WaterRequirement.NONE;
        }
        return switch (value = raw.toLowerCase(Locale.ROOT).trim()) {
            case "surface" -> WaterRequirement.SURFACE;
            case "underwater" -> WaterRequirement.UNDERWATER;
            case "none" -> WaterRequirement.NONE;
            default -> {
                AnimationFramework.logSetupWarning("[AFW] Unknown water requirement '{}' in {}; expected none|surface|underwater.", raw, fileId);
                yield WaterRequirement.NONE;
            }
        };
    }

    private static WallHeight readWallHeight(JsonObject req, Identifier fileId) {
        int min = 2;
        Integer max = null;
        boolean minSet = false;
        if (req.has("height")) {
            JsonElement heightEl = req.get("height");
            if (heightEl.isJsonObject()) {
                JsonObject heightObj = heightEl.getAsJsonObject();
                AfwAnimationDefinitions.warnUnknownKeys(heightObj, fileId, "block_requirements.height", KNOWN_WALL_HEIGHT_KEYS);
                Integer minVal = AfwAnimationDefinitions.readOptionalInt(heightObj, "min");
                Integer maxVal = AfwAnimationDefinitions.readOptionalInt(heightObj, "max");
                if (minVal != null) {
                    min = minVal;
                    minSet = true;
                }
                if (maxVal != null) {
                    max = maxVal;
                    if (!minSet) {
                        min = 1;
                    }
                }
            } else {
                Integer exact = AfwAnimationDefinitions.readOptionalInt(heightEl);
                if (exact != null) {
                    min = exact;
                    max = exact;
                }
            }
        }
        if (min < 1) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: invalid block_requirements height min '{}'", fileId, min);
            return null;
        }
        if (max != null) {
            if (max < 1) {
                AnimationFramework.logSetupWarning("[AFW] Skipping {}: invalid block_requirements height max '{}'", fileId, max);
                return null;
            }
            if (max < min) {
                AnimationFramework.logSetupWarning("[AFW] Skipping {}: block_requirements height max '{}' < min '{}'", fileId, max, min);
                return null;
            }
        }
        return new WallHeight(min, max);
    }

    private static Clearance readClearance(JsonObject req, Identifier fileId) {
        JsonObject clearance;
        int width = 1;
        int height = 2;
        int depth = 1;
        if (req.has("clearance") && (clearance = req.getAsJsonObject("clearance")) != null) {
            AfwAnimationDefinitions.warnUnknownKeys(clearance, fileId, "block_requirements.clearance", KNOWN_CLEARANCE_KEYS);
            width = AfwAnimationDefinitions.readPositiveInt(clearance, "width", width);
            height = AfwAnimationDefinitions.readPositiveInt(clearance, "height", height);
            depth = AfwAnimationDefinitions.readPositiveInt(clearance, "depth", depth);
        }
        return new Clearance(width, height, depth);
    }

    private static int readPositiveInt(JsonObject obj, String key, int fallback) {
        if (!obj.has(key)) {
            return fallback;
        }
        Integer value = AfwAnimationDefinitions.readOptionalInt(obj, key);
        if (value == null) {
            AnimationFramework.logSetupWarning("[AFW] Invalid integer value for {}", key);
            return fallback;
        }
        return Math.max(1, value);
    }

    private static int readNonNegativeInt(JsonObject obj, String key, int fallback) {
        if (!obj.has(key)) {
            return fallback;
        }
        Integer value = AfwAnimationDefinitions.readOptionalInt(obj, key);
        if (value == null) {
            AnimationFramework.logSetupWarning("[AFW] Invalid integer value for {}", key);
            return fallback;
        }
        return Math.max(0, value);
    }

    private static Integer readOptionalInt(JsonObject obj, String key) {
        if (!obj.has(key)) {
            return null;
        }
        return AfwAnimationDefinitions.readOptionalInt(obj.get(key));
    }

    private static Set<Identifier> readEntityTypes(JsonObject obj, Identifier fileId) {
        String key = "entity_types";
        if (!obj.has(key)) {
            return new LinkedHashSet<Identifier>();
        }
        JsonArray arr = obj.getAsJsonArray(key);
        LinkedHashSet<Identifier> out = new LinkedHashSet<Identifier>();
        for (int i = 0; i < arr.size(); ++i) {
            String s = arr.get(i).getAsString();
            Identifier id = Identifier.tryParse((String)s);
            if (id == null) {
                AnimationFramework.logSetupWarning("[AFW] Skipping {}: invalid identifier in {}: '{}'", fileId, key, s);
                return null;
            }
            if (PLAYER_SLIM_ID.equals((Object)id)) {
                AnimationFramework.logSetupWarning("[AFW] Skipping {}: '{}' is no longer supported in {}. Use '{}' only.", fileId, PLAYER_SLIM_ID, key, PLAYER_ID);
                return null;
            }
            out.add(id);
        }
        return out;
    }

    @Nullable
    private static String readEntityVariant(JsonObject obj, Identifier fileId, String contextLabel) {
        String key = "entity_variant";
        if (obj == null || !obj.has(key)) {
            return null;
        }
        JsonElement el = obj.get(key);
        if (el == null || el.isJsonNull()) {
            return null;
        }
        if (!el.isJsonPrimitive() || !el.getAsJsonPrimitive().isString()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: {} in {} must be a string.", fileId, key, contextLabel);
            return INVALID_ENTITY_VARIANT;
        }
        String value = el.getAsString();
        if (value == null || value.isBlank()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: {} in {} must not be blank.", fileId, key, contextLabel);
            return INVALID_ENTITY_VARIANT;
        }
        value = value.trim().toLowerCase(Locale.ROOT);
        for (int i = 0; i < value.length(); ++i) {
            boolean valid;
            char c = value.charAt(i);
            boolean bl = valid = c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '_';
            if (valid) continue;
            AnimationFramework.logSetupWarning("[AFW] Skipping {}: invalid {} '{}' in {}. Use lowercase letters, numbers, and underscores.", fileId, key, value, contextLabel);
            return INVALID_ENTITY_VARIANT;
        }
        return value;
    }

    @Nullable
    private static Identifier readOptionalItemId(JsonObject obj, String key, Identifier fileId, String contextLabel) {
        if (obj == null || !obj.has(key)) {
            return null;
        }
        JsonElement el = obj.get(key);
        if (el == null || el.isJsonNull()) {
            return null;
        }
        if (!el.isJsonPrimitive() || !el.getAsJsonPrimitive().isString()) {
            AnimationFramework.logSetupWarning("[AFW] Invalid {} in {} {}: expected item id string.", key, fileId, contextLabel);
            return null;
        }
        String raw = el.getAsString();
        if (raw == null || raw.isBlank()) {
            return null;
        }
        Identifier id = Identifier.tryParse((String)raw.trim());
        if (id == null || !Registries.ITEM.containsId(id)) {
            AnimationFramework.logSetupWarning("[AFW] Invalid item id '{}' for {} in {} {}.", raw, key, fileId, contextLabel);
            return null;
        }
        return id;
    }

    private static List<AnimationStageInfo> readStageInfos(JsonObject obj, Identifier defId, Identifier fileId, List<ActorConstraint> actorConstraints) {
        ArrayList stages = new ArrayList();
        double baseSpeed = AfwAnimationDefinitions.readSpeed(obj, 1.0);
        Map<String, AnimationStageInfo.ActorPropEntry> defaultActorProps = AfwAnimationDefinitions.buildDefaultActorPropMap(actorConstraints);
        Set<String> validActorKeys = AfwAnimationDefinitions.buildActorKeySet(actorConstraints);
        if (obj.has("stages")) {
            JsonArray stagesArr = obj.getAsJsonArray("stages");
            TreeMap<Integer, StageSpec> stageSpecs = new TreeMap<Integer, StageSpec>();
            TreeMap stagePropsByNumber = new TreeMap();
            for (int i = 0; i < stagesArr.size(); ++i) {
                JsonObject stageObj = stagesArr.get(i).getAsJsonObject();
                AfwAnimationDefinitions.warnUnknownKeys(stageObj, fileId, "stage[" + i + "]", KNOWN_STAGE_KEYS);
                Integer stageNumber = AfwAnimationDefinitions.parseStageNumber(stageObj, fileId);
                if (stageNumber == null || stageNumber <= 0) continue;
                if (stageSpecs.containsKey(stageNumber)) {
                    AnimationFramework.logSetupWarning("[AFW] Skipping {}: duplicate stage p{}", fileId, stageNumber);
                    continue;
                }
                boolean loop = AfwAnimationDefinitions.readLoopFlag(stageObj);
                Double cycleSeconds = AfwAnimationDefinitions.readCycleSeconds(stageObj);
                long lengthTicks = AfwAnimationDefinitions.secondsToTicks(cycleSeconds == null ? 0.0 : cycleSeconds);
                double cycleTicks = AfwAnimationDefinitions.secondsToExactTicks(cycleSeconds == null ? 0.0 : cycleSeconds);
                boolean allowJoin = AfwAnimationDefinitions.readAllowJoin(stageObj);
                double speed = AfwAnimationDefinitions.readSpeed(stageObj, baseSpeed);
                Integer useStage = AfwAnimationDefinitions.readOptionalStageReference(stageObj, "use_stage", fileId);
                double cycleMidpointOffsetSeconds = AfwAnimationDefinitions.readCycleMidpointOffsetSeconds(stageObj, fileId, stageNumber, loop, cycleTicks);
                if (useStage != null && useStage <= 0) {
                    AnimationFramework.logSetupWarning("[AFW] Invalid use_stage '{}' in {} stage p{}; falling back to stage itself.", useStage, fileId, stageNumber);
                    useStage = null;
                }
                if (!loop && lengthTicks <= 0L) {
                    AnimationFramework.logSetupWarning("[AFW] Stage p{} in {} is non-looping but has no cycle_seconds.", stageNumber, fileId);
                }
                stageSpecs.put(stageNumber, new StageSpec(loop, lengthTicks, cycleTicks, allowJoin, speed, useStage, cycleMidpointOffsetSeconds));
                stagePropsByNumber.put(stageNumber, AfwAnimationDefinitions.resolveStageActorProps(stageObj, fileId, "p" + stageNumber, validActorKeys, defaultActorProps));
            }
            TreeMap<Integer, AnimationStageInfo> stageMap = new TreeMap<Integer, AnimationStageInfo>();
            for (Map.Entry entry : stageSpecs.entrySet()) {
                int stageNumber = (Integer)entry.getKey();
                StageSpec spec = (StageSpec)entry.getValue();
                Identifier stageId = Identifier.of((String)defId.getNamespace(), (String)(defId.getPath() + ".p" + stageNumber));
                List<AnimationStageInfo.ActorPropEntry> stageProps = stagePropsByNumber.getOrDefault(stageNumber, List.of());
                Identifier playbackId = stageId;
                if (spec.useStage != null && spec.useStage != stageNumber) {
                    if (stageSpecs.containsKey(spec.useStage)) {
                        playbackId = Identifier.of((String)defId.getNamespace(), (String)(defId.getPath() + ".p" + spec.useStage));
                    } else {
                        AnimationFramework.logSetupWarning("[AFW] use_stage '{}' in {} stage p{} points to missing stage; falling back to p{}.", spec.useStage, fileId, stageNumber, stageNumber);
                    }
                }
                stageMap.put(stageNumber, new AnimationStageInfo(stageId, spec.loop, spec.lengthTicks, spec.allowJoin, spec.speed, playbackId, stageProps, spec.cycleMidpointOffsetSeconds, spec.cycleTicks));
            }
            stages.addAll(stageMap.values());
        }
        return List.copyOf(stages);
    }

    private static Map<String, AnimationStageInfo.ActorPropEntry> buildDefaultActorPropMap(List<ActorConstraint> actorConstraints) {
        if (actorConstraints == null || actorConstraints.isEmpty()) {
            return Map.of();
        }
        String[] actorKeys = AfwAnimationDefinitions.buildUniqueActorKeys(actorConstraints);
        LinkedHashMap<String, AnimationStageInfo.ActorPropEntry> defaults = new LinkedHashMap<String, AnimationStageInfo.ActorPropEntry>();
        int count = Math.min(actorConstraints.size(), actorKeys.length);
        for (int i = 0; i < count; ++i) {
            ActorConstraint constraint = actorConstraints.get(i);
            Identifier left = constraint.propLeftItemId();
            Identifier right = constraint.propRightItemId();
            if (left == null && right == null) continue;
            String actorKey = actorKeys[i];
            defaults.put(actorKey, new AnimationStageInfo.ActorPropEntry(actorKey, left, right));
        }
        return defaults.isEmpty() ? Map.of() : Map.copyOf(defaults);
    }

    private static Set<String> buildActorKeySet(List<ActorConstraint> actorConstraints) {
        if (actorConstraints == null || actorConstraints.isEmpty()) {
            return Set.of();
        }
        String[] actorKeys = AfwAnimationDefinitions.buildUniqueActorKeys(actorConstraints);
        LinkedHashSet out = new LinkedHashSet();
        Collections.addAll(out, actorKeys);
        return out.isEmpty() ? Set.of() : Set.copyOf(out);
    }

    private static List<String> normalizeTagList(Set<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        ArrayList<String> out = new ArrayList<String>();
        LinkedHashSet<String> seen = new LinkedHashSet<String>();
        for (String raw : tags) {
            String value;
            if (raw == null || (value = raw.trim().toLowerCase(Locale.ROOT)).isEmpty() || !seen.add(value)) continue;
            out.add(value);
        }
        return out.isEmpty() ? List.of() : List.copyOf(out);
    }

    private static List<AnimationStageInfo.ActorPropEntry> resolveStageActorProps(JsonObject stageObj, Identifier fileId, String stageLabel, Set<String> validActorKeys, Map<String, AnimationStageInfo.ActorPropEntry> defaultActorProps) {
        if (stageObj == null) {
            return List.of();
        }
        LinkedHashMap<String, AnimationStageInfo.ActorPropEntry> effective = new LinkedHashMap<String, AnimationStageInfo.ActorPropEntry>();
        if (defaultActorProps != null && !defaultActorProps.isEmpty()) {
            effective.putAll(defaultActorProps);
        }
        if (stageObj.has("props")) {
            JsonElement propsEl = stageObj.get("props");
            if (propsEl == null || propsEl.isJsonNull()) {
                effective.clear();
            } else if (!propsEl.isJsonObject()) {
                AnimationFramework.logSetupWarning("[AFW] Invalid props in {} {}: expected object.", fileId, stageLabel);
            } else {
                JsonObject propsObj = propsEl.getAsJsonObject();
                for (Map.Entry entry : propsObj.entrySet()) {
                    String actorKey;
                    String string = actorKey = entry.getKey() == null ? "" : ((String)entry.getKey()).trim();
                    if (actorKey.isBlank()) {
                        AnimationFramework.logSetupWarning("[AFW] Invalid props actor key in {} {}: blank key.", fileId, stageLabel);
                        continue;
                    }
                    if (validActorKeys != null && !validActorKeys.isEmpty() && !validActorKeys.contains(actorKey)) {
                        AnimationFramework.logSetupWarning("[AFW] Unknown props actor key '{}' in {} {}. Valid keys: {}", actorKey, fileId, stageLabel, validActorKeys);
                        continue;
                    }
                    JsonElement actorPropsEl = (JsonElement)entry.getValue();
                    if (actorPropsEl == null || actorPropsEl.isJsonNull()) {
                        effective.remove(actorKey);
                        continue;
                    }
                    if (!actorPropsEl.isJsonObject()) {
                        AnimationFramework.logSetupWarning("[AFW] Invalid props value for actor '{}' in {} {}: expected object.", actorKey, fileId, stageLabel);
                        continue;
                    }
                    JsonObject actorProps = actorPropsEl.getAsJsonObject();
                    AfwAnimationDefinitions.warnUnknownKeys(actorProps, fileId, stageLabel + ".props." + actorKey, KNOWN_STAGE_ACTOR_PROP_KEYS);
                    AnimationStageInfo.ActorPropEntry current = (AnimationStageInfo.ActorPropEntry)effective.get(actorKey);
                    Identifier left = current == null ? null : current.propLeftItemId();
                    Identifier right = current == null ? null : current.propRightItemId();
                    boolean touched = false;
                    if (actorProps.has("prop_left")) {
                        touched = true;
                        left = AfwAnimationDefinitions.parseStagePropItemId(actorProps.get("prop_left"), fileId, stageLabel, actorKey, "prop_left", left);
                    }
                    if (actorProps.has("prop_right")) {
                        touched = true;
                        right = AfwAnimationDefinitions.parseStagePropItemId(actorProps.get("prop_right"), fileId, stageLabel, actorKey, "prop_right", right);
                    }
                    if (!touched) continue;
                    if (left == null && right == null) {
                        effective.remove(actorKey);
                        continue;
                    }
                    effective.put(actorKey, new AnimationStageInfo.ActorPropEntry(actorKey, left, right));
                }
            }
        }
        if (effective.isEmpty()) {
            return List.of();
        }
        ArrayList out = new ArrayList(effective.values());
        out.sort(Comparator.comparing(AnimationStageInfo.ActorPropEntry::actorKey));
        return List.copyOf(out);
    }

    @Nullable
    private static Identifier parseStagePropItemId(JsonElement element, Identifier fileId, String stageLabel, String actorKey, String field, @Nullable Identifier fallback) {
        if (element == null || element.isJsonNull()) {
            return null;
        }
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            AnimationFramework.logSetupWarning("[AFW] Invalid {} for actor '{}' in {} {}: expected item id string or null.", field, actorKey, fileId, stageLabel);
            return fallback;
        }
        String raw = element.getAsString();
        if (raw == null || raw.isBlank()) {
            return null;
        }
        Identifier id = Identifier.tryParse((String)raw.trim());
        if (id == null || !Registries.ITEM.containsId(id)) {
            AnimationFramework.logSetupWarning("[AFW] Invalid item id '{}' for {} actor '{}' in {} {}.", raw, field, actorKey, fileId, stageLabel);
            return fallback;
        }
        return id;
    }

    private static Integer parseStageNumber(JsonObject obj, Identifier fileId) {
        if (!obj.has("stage")) {
            AnimationFramework.logSetupWarning("[AFW] Skipping stage in {}: missing 'stage' number", fileId);
            return null;
        }
        JsonElement el = obj.get("stage");
        if (el == null || !el.isJsonPrimitive()) {
            AnimationFramework.logSetupWarning("[AFW] Skipping stage in {}: invalid 'stage' value", fileId);
            return null;
        }
        JsonPrimitive prim = el.getAsJsonPrimitive();
        if (prim.isNumber()) {
            return prim.getAsInt();
        }
        AnimationFramework.logSetupWarning("[AFW] Skipping {}: 'stage' must be a number", fileId);
        return null;
    }

    private static Integer readOptionalStageReference(JsonObject obj, String key, Identifier fileId) {
        if (!obj.has(key)) {
            return null;
        }
        JsonElement el = obj.get(key);
        if (el == null || !el.isJsonPrimitive()) {
            AnimationFramework.logSetupWarning("[AFW] Invalid {} in {}: value is not a primitive", key, fileId);
            return null;
        }
        JsonPrimitive prim = el.getAsJsonPrimitive();
        if (prim.isNumber()) {
            return prim.getAsInt();
        }
        AnimationFramework.logSetupWarning("[AFW] Invalid {} in {}: value must be a number", key, fileId);
        return null;
    }

    private static boolean readLoopFlag(JsonObject obj) {
        if (!obj.has("loop")) {
            return true;
        }
        JsonElement el = obj.get("loop");
        if (el == null || !el.isJsonPrimitive()) {
            return true;
        }
        JsonPrimitive prim = el.getAsJsonPrimitive();
        if (prim.isBoolean()) {
            return prim.getAsBoolean();
        }
        return true;
    }

    private static boolean readAllowJoin(JsonObject obj) {
        if (!obj.has("allow_join")) {
            return true;
        }
        JsonElement el = obj.get("allow_join");
        if (el == null || !el.isJsonPrimitive()) {
            return true;
        }
        JsonPrimitive prim = el.getAsJsonPrimitive();
        if (prim.isBoolean()) {
            return prim.getAsBoolean();
        }
        return true;
    }

    private static Double readCycleSeconds(JsonObject obj) {
        Double seconds = AfwAnimationDefinitions.readDouble(obj, "cycle_seconds");
        return seconds == null ? null : seconds;
    }

    private static Double readDouble(JsonObject obj, String key) {
        if (!obj.has(key)) {
            return null;
        }
        return AfwAnimationDefinitions.readFiniteDouble(obj.get(key));
    }

    private static double readSpeed(JsonObject obj, double fallback) {
        Double value = AfwAnimationDefinitions.readDouble(obj, "speed");
        if (value == null || !Double.isFinite(value) || value <= 0.0) {
            return fallback;
        }
        return value;
    }

    private static double readCycleMidpointOffsetSeconds(JsonObject obj, Identifier fileId, int stageNumber, boolean loop, double cycleTicks) {
        if (!obj.has("cycle_midpoint_offset_seconds")) {
            return 0.0;
        }
        Double value = AfwAnimationDefinitions.readDouble(obj, "cycle_midpoint_offset_seconds");
        if (value == null || Math.abs(value) < 1.0E-6) {
            return 0.0;
        }
        if (!loop) {
            AnimationFramework.logSetupWarning("[AFW] Ignoring cycle_midpoint_offset_seconds in {} stage p{}: timing warp only applies to looping stages.", fileId, stageNumber);
            return 0.0;
        }
        if (cycleTicks <= 0.0) {
            AnimationFramework.logSetupWarning("[AFW] Ignoring cycle_midpoint_offset_seconds in {} stage p{}: stage has no positive cycle_seconds.", fileId, stageNumber);
            return 0.0;
        }
        double clamped = AfwStageTimeWarp.clampOffsetSeconds(cycleTicks, value);
        if (Math.abs(clamped - value) > 1.0E-6) {
            AnimationFramework.logSetupWarning("[AFW] Clamped cycle_midpoint_offset_seconds in {} stage p{} from {} to {}.", fileId, stageNumber, value, clamped);
        }
        return clamped;
    }

    private static long secondsToTicks(double lengthSeconds) {
        if (lengthSeconds <= 0.0) {
            return 0L;
        }
        return Math.max(1L, (long)Math.ceil(lengthSeconds * 20.0));
    }

    private static double secondsToExactTicks(double lengthSeconds) {
        if (!Double.isFinite(lengthSeconds) || lengthSeconds <= 0.0) {
            return 0.0;
        }
        return lengthSeconds * 20.0;
    }

    private static Integer readOptionalInt(JsonElement element) {
        Double value = AfwAnimationDefinitions.readFiniteDouble(element);
        if (value == null) {
            return null;
        }
        if (value < -2.147483648E9 || value > 2.147483647E9) {
            return null;
        }
        if (value != Math.rint(value)) {
            return null;
        }
        return value.intValue();
    }

    private static Double readFiniteDouble(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            double value = primitive.getAsDouble();
            return Double.isFinite(value) ? Double.valueOf(value) : null;
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
    implements SynchronousResourceReloader {
        static final Identifier RELOADER_ID = Identifier.of((String)"animationframework", (String)"afw_animdefs");

        public void reload(ResourceManager manager) {
            AfwAnimationDefinitions.reload(manager);
        }
    }

    public record MatchResult(List<Definition> candidatesSorted, Definition chosen) {
    }

    public record Definition(Identifier id, List<ActorConstraint> actors, Set<String> requiredUnionTags, Set<String> animationTags, List<String> contentTags, List<AnimationStageInfo> stages, double weight, BlockRequirements blockRequirements, WaterRequirement waterRequirement, @Nullable String positionAnchorActor, AnimationPackInfo packInfo) {
        public int specificity() {
            int s = this.requiredUnionTags.size();
            for (ActorConstraint c : this.actors) {
                s += c.specificity();
            }
            return s;
        }

        public boolean matches(List<Entity> selectedActorsSortedById, Set<String> requiredAnimationTags) {
            if (selectedActorsSortedById.size() != this.actors.size()) {
                return false;
            }
            if (!this.matchesAnimationTags(requiredAnimationTags)) {
                return false;
            }
            TreeSet union = new TreeSet();
            for (Entity e : selectedActorsSortedById) {
                union.addAll(e.getCommandTags());
            }
            if (!union.containsAll(this.requiredUnionTags)) {
                return false;
            }
            return AfwAnimationDefinitions.matchesUnordered(selectedActorsSortedById, this.actors);
        }

        private boolean matchesAnimationTags(Set<String> requiredAnimationTags) {
            Set<Object> required;
            Set<Object> set = required = requiredAnimationTags == null ? Set.of() : requiredAnimationTags;
            if (required.isEmpty()) {
                return this.animationTags == null || this.animationTags.isEmpty() || this.animationTags.contains(AfwAnimationDefinitions.NORMAL_MATCH_TAG);
            }
            if (this.animationTags == null || this.animationTags.isEmpty()) {
                return false;
            }
            return this.animationTags.containsAll(required);
        }
    }

    public record AnimationPackInfo(String id, String resourcePackId, String name, String author, String version, String description) {
        public AnimationPackInfo {
            id = AfwAnimationDefinitions.sanitizeNonBlank(id, "unknown");
            resourcePackId = AfwAnimationDefinitions.sanitizeNonBlank(resourcePackId, id);
            name = AfwAnimationDefinitions.sanitizeNonBlank(name, AfwAnimationDefinitions.displayNameFromPackId(id));
            author = AfwAnimationDefinitions.sanitizeNonBlank(author, "Unknown");
            version = AfwAnimationDefinitions.sanitizeNullable(version);
            description = AfwAnimationDefinitions.sanitizeNullable(description);
        }
    }

    public record ActorConstraint(Set<Identifier> entityTypes, @Nullable String entityVariant, Set<String> requiredTags, Set<String> requiredTagsAny, String label, AgeRequirement ageRequirement, ActorActivity activity, @Nullable Identifier propLeftItemId, @Nullable Identifier propRightItemId) {
        public boolean matches(Entity e) {
            Identifier typeId = Registries.ENTITY_TYPE.getId((Object)e.getType());
            if (!this.entityTypes.isEmpty() && !this.entityTypes.contains(typeId)) {
                return false;
            }
            if (this.entityVariant != null && !this.entityVariant.equals(AfwEntityVariants.resolveVariant(e))) {
                return false;
            }
            Set tags = e.getCommandTags();
            if (!this.requiredTags.isEmpty() && !tags.containsAll(this.requiredTags)) {
                return false;
            }
            if (!this.requiredTagsAny.isEmpty()) {
                if (this.requiredTagsAny.stream().noneMatch(tags::contains)) {
                    return false;
                }
            }
            return this.matchesAge(e);
        }

        public int specificity() {
            return this.entityTypes.size() + this.requiredTags.size() + this.requiredTagsAny.size() + (this.entityVariant == null ? 0 : 1);
        }

        public String stableKey() {
            List<String> types = this.entityTypes.stream().map(Identifier::toString).sorted().toList();
            List tagsAll = this.requiredTags.stream().sorted().toList();
            List tagsAny = this.requiredTagsAny.stream().sorted().toList();
            return "types=" + String.valueOf(types) + "|variant=" + this.entityVariant + "|tagsAll=" + String.valueOf(tagsAll) + "|tagsAny=" + String.valueOf(tagsAny) + "|age=" + String.valueOf((Object)this.ageRequirement) + "|activity=" + String.valueOf((Object)this.activity);
        }

        public String derivedKey() {
            if (this.label != null && !this.label.isBlank()) {
                return AfwAnimationDefinitions.sanitizeKeyPart(this.label);
            }
            Object base = "actor";
            if (!this.entityTypes.isEmpty()) {
                if (this.entityTypes.size() == 1) {
                    base = this.entityTypes.iterator().next().getPath();
                } else {
                    List<String> paths = this.entityTypes.stream().map(Identifier::getPath).sorted().toList();
                    base = String.join((CharSequence)"_", paths);
                }
            }
            if (!this.requiredTags.isEmpty()) {
                List<String> tags = this.requiredTags.stream().sorted().map(AfwAnimationDefinitions::sanitizeKeyPart).toList();
                base = (String)base + "_" + String.join((CharSequence)"_", tags);
            }
            if (!this.requiredTagsAny.isEmpty()) {
                List<String> tagsAny = this.requiredTagsAny.stream().sorted().map(AfwAnimationDefinitions::sanitizeKeyPart).toList();
                base = (String)base + "_any_" + String.join((CharSequence)"_", tagsAny);
            }
            if (this.entityVariant != null && !this.entityVariant.isBlank()) {
                base = (String)base + "_" + this.entityVariant;
            }
            if (this.ageRequirement == AgeRequirement.BABY) {
                base = (String)base + "_baby";
            }
            return AfwAnimationDefinitions.sanitizeKeyPart((String)base);
        }

        private boolean matchesAge(Entity e) {
            AgeRequirement req = this.ageRequirement == null ? AgeRequirement.ADULT : this.ageRequirement;
            boolean baby = false;
            if (e instanceof LivingEntity) {
                LivingEntity living = (LivingEntity)e;
                baby = living.isBaby();
            }
            return req == AgeRequirement.BABY == baby;
        }
    }

    public static enum AgeRequirement {
        ADULT,
        BABY;

    }

    public static enum ActorActivity {
        ACTIVE,
        PASSIVE;

    }

    public record BlockRequirements(String type, WallHeight height, Clearance clearance, CenterSupport support, CenterPlacement placement, int surfaceRadius, SurfaceFootprint surfaceFootprint, @Nullable BlockPredicate blocks) {
        public boolean isWall() {
            return "wall".equalsIgnoreCase(this.type);
        }

        public boolean isCenterSupport() {
            return "center_support".equalsIgnoreCase(this.type);
        }
    }

    public static enum WaterRequirement {
        NONE,
        SURFACE,
        UNDERWATER;

    }

    public record BlockPredicate(Set<Identifier> blockIds, Set<Identifier> tagIds) {
        public boolean isEmpty() {
            return this.blockIds.isEmpty() && this.tagIds.isEmpty();
        }
    }

    public record WallHeight(int min, Integer max) {
    }

    public record Clearance(int width, int height, int depth) {
    }

    public static enum CenterSupport {
        FULL_BLOCK("full_block"),
        SLAB("slab"),
        HALF_HEIGHT("half_height"),
        SURFACE("surface");

        private final String id;

        private CenterSupport(String id) {
            this.id = id;
        }

        public String id() {
            return this.id;
        }
    }

    public static enum CenterPlacement {
        DIRECTIONAL_CLEARANCE("directional_clearance"),
        SURFACE_FOOTPRINT("surface_footprint");

        private final String id;

        private CenterPlacement(String id) {
            this.id = id;
        }

        public String id() {
            return this.id;
        }
    }

    public record SurfaceFootprint(int width, int depth, int height, int margin) {
    }

    private record StageSpec(boolean loop, long lengthTicks, double cycleTicks, boolean allowJoin, double speed, Integer useStage, double cycleMidpointOffsetSeconds) {
    }
}

