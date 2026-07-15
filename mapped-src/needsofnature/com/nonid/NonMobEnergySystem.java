/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.data.AfwAnimationDefinitions
 *  com.afwid.data.AfwAnimationDefinitions$ActorConstraint
 *  com.afwid.data.AfwAnimationDefinitions$AgeRequirement
 *  com.afwid.data.AfwAnimationDefinitions$Definition
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.passive.HorseEntity
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.registry.Registries
 */
package com.nonid;

import com.afwid.data.AfwAnimationDefinitions;
import com.nonid.NeedsOfNature;
import com.nonid.NonDebugChatCategory;
import com.nonid.data.NonAnimationEligibility;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.registry.Registries;

public final class NonMobEnergySystem {
    public static final int DEFAULT_TICKS_PER_ENERGY = 120;
    public static final int PARTNER_SCAN_INTERVAL_TICKS = 40;
    public static final int POST_ATTEMPT_COOLDOWN_TICKS = 100;
    public static final int ROLL_FAIL_COOLDOWN_TICKS = 1200;
    public static final int ENERGY_JOIN_MIN = 70;
    public static final int ENERGY_MOB_MATCH_MIN = 120;
    public static final int ENERGY_ATTACK = 200;
    public static final double PARTNER_SCAN_RADIUS = 20.0;
    public static final double PLAYER_COOLDOWN_BYPASS_DIST_SQ = 64.0;
    public static final double PLAYER_ANIMATION_LINGER_RADIUS = 3.0;
    public static final double PLAYER_ANIMATION_LINGER_RADIUS_SQ = 9.0;
    public static final double PLAYER_ANIMATION_LINGER_MIN_DISTANCE = 1.5;
    public static final double PLAYER_ANIMATION_LINGER_MIN_DISTANCE_SQ = 2.25;
    public static final double PLAYER_ANIMATION_LINGER_MOVE_SPEED = 1.1;
    private static final int GENDER_MIXED = 0;
    private static final int GENDER_MALE = 1;
    private static final int GENDER_FEMALE = 2;
    private static final String AFW_NORMAL_MATCH_TAG = "normal_match";

    private NonMobEnergySystem() {
    }

    public static int ticksPerGain(float multiplier, float drift, boolean nearAnimation, double nearAnimationMultiplier) {
        double rate = NeedsOfNature.getConfig().getEnergyGainRate();
        float scale = (float)((double)(multiplier * drift) * rate);
        if (nearAnimation) {
            scale *= (float)Math.max(1.0, nearAnimationMultiplier);
        }
        if (!Float.isFinite(scale) || scale <= 0.0f) {
            return 120;
        }
        return Math.max(1, Math.round(120.0f / scale));
    }

    public static float randomGainMultiplier(ServerWorld world) {
        float a = world.getRandom().nextFloat();
        float b = world.getRandom().nextFloat();
        float tri = (a + b) * 0.5f;
        return 0.4f + tri * 0.6f;
    }

    public static float randomGainDrift(ServerWorld world) {
        return 0.9f + world.getRandom().nextFloat() * 0.2f;
    }

    public static boolean hasGenericAnimationSupport(EntityType<?> type, boolean baby) {
        Identifier entityTypeId = Registries.ENTITY_TYPE.getId(type);
        return NonAnimationEligibility.hasAnimationForEntityType(entityTypeId, baby) && NonMobEnergySystem.hasEnabledGenericAnimationForEntityType(entityTypeId, baby);
    }

    private static boolean hasEnabledGenericAnimationForEntityType(Identifier entityTypeId, boolean baby) {
        if (entityTypeId == null) {
            return false;
        }
        for (AfwAnimationDefinitions.Definition definition : AfwAnimationDefinitions.getLoadedDefinitionsSnapshot()) {
            List actors;
            if (!AfwAnimationDefinitions.isDefinitionEnabled((AfwAnimationDefinitions.Definition)definition) || !NonMobEnergySystem.isGenericMatchDefinition(definition) || (actors = definition.actors()) == null || actors.size() < 2) continue;
            for (AfwAnimationDefinitions.ActorConstraint actor : actors) {
                if (!NonMobEnergySystem.actorCanMatchEntityType(actor, entityTypeId, baby)) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean isGenericMatchDefinition(AfwAnimationDefinitions.Definition definition) {
        if (definition == null) {
            return false;
        }
        Set tags = definition.animationTags();
        return tags == null || tags.isEmpty() || tags.contains(AFW_NORMAL_MATCH_TAG);
    }

    private static boolean actorCanMatchEntityType(AfwAnimationDefinitions.ActorConstraint actor, Identifier entityTypeId, boolean baby) {
        if (actor == null || entityTypeId == null) {
            return false;
        }
        Set entityTypes = actor.entityTypes();
        if (entityTypes == null || entityTypes.isEmpty() || !entityTypes.contains(entityTypeId)) {
            return false;
        }
        AfwAnimationDefinitions.AgeRequirement requirement = actor.ageRequirement() == null ? AfwAnimationDefinitions.AgeRequirement.ADULT : actor.ageRequirement();
        return requirement == AfwAnimationDefinitions.AgeRequirement.BABY == baby;
    }

    public static boolean isEnergyAttackStabilized(Entity entity) {
        return entity != null && entity.getCommandTags().contains("non.energy_stabilized");
    }

    public static boolean bypassesEntityEntityRoll(Entity self, Entity candidate) {
        if (self == null || candidate == null) {
            return false;
        }
        return self instanceof HorseEntity && candidate.getType() == NeedsOfNature.HORSE_LIQUID_COLLECTOR_ENTITY_TYPE || self.getType() == NeedsOfNature.HORSE_LIQUID_COLLECTOR_ENTITY_TYPE && candidate instanceof HorseEntity;
    }

    public static boolean passesSameGenderRoll(ServerWorld world, Entity debugSource, List<? extends Entity> actors) {
        String label;
        int percent;
        if (world == null) {
            return true;
        }
        int group = NonMobEnergySystem.resolveGenderGroup(actors);
        if (group == 1) {
            percent = NeedsOfNature.getConfig().getMaleMaleChancePercent();
            label = "male+male";
        } else if (group == 2) {
            percent = NeedsOfNature.getConfig().getFemaleFemaleChancePercent();
            label = "female+female";
        } else {
            percent = 100;
            label = "mixed";
        }
        return NonMobEnergySystem.rollPercent(world, debugSource, percent, label);
    }

    public static boolean passesMultiActorJoinRoll(ServerWorld world, Entity debugSource, int totalActors) {
        if (world == null || totalActors <= 2) {
            return true;
        }
        int percent = NeedsOfNature.getConfig().getMultiActorJoinChancePercent();
        return NonMobEnergySystem.rollPercent(world, debugSource, percent, "join 3+ actors (total=" + totalActors + ")");
    }

    public static boolean passesEntityEntityRoll(ServerWorld world, Entity debugSource) {
        if (world == null) {
            return true;
        }
        int percent = NeedsOfNature.getConfig().getEntityEntityChancePercent();
        return NonMobEnergySystem.rollPercent(world, debugSource, percent, "entity+entity");
    }

    public static void appendCooldownMapDebug(List<String> lines, ServerWorld world, long nowTick, String label, Map<UUID, Long> cooldowns, Map<UUID, Long> bypassableCooldowns, boolean includeBypassState) {
        if (lines == null || world == null || cooldowns == null || cooldowns.isEmpty()) {
            return;
        }
        int shown = 0;
        for (Map.Entry<UUID, Long> entry : cooldowns.entrySet()) {
            UUID partnerId = entry.getKey();
            long remaining = entry.getValue() - nowTick;
            if (partnerId == null || remaining <= 0L) continue;
            String suffix = "";
            if (includeBypassState && bypassableCooldowns != null && bypassableCooldowns.containsKey(partnerId)) {
                suffix = " (bypass: close+LOS)";
            }
            lines.add(label + ": " + NonMobEnergySystem.debugEntityName(world, partnerId) + " " + NeedsOfNature.formatTicksForDebug(remaining) + suffix);
            if (++shown < 5) continue;
            int extra = cooldowns.size() - shown;
            if (extra <= 0) break;
            lines.add(label + ": +" + extra + " more");
            break;
        }
    }

    public static String debugEntityName(ServerWorld world, UUID uuid) {
        if (uuid == null) {
            return "unknown";
        }
        Entity entity = world.getEntity(uuid);
        if (entity != null) {
            return entity.getDisplayName().getString();
        }
        String raw = uuid.toString();
        return raw.length() <= 8 ? raw : raw.substring(0, 8);
    }

    private static int resolveGenderGroup(List<? extends Entity> actors) {
        int group = 0;
        if (actors == null || actors.isEmpty()) {
            return 0;
        }
        for (Entity class_12972 : actors) {
            int actorGroup;
            if (class_12972 == null) {
                return 0;
            }
            boolean hasMale = class_12972.getCommandTags().contains("gender.male");
            boolean hasFemale = class_12972.getCommandTags().contains("gender.female");
            if (hasMale && !hasFemale) {
                actorGroup = 1;
            } else if (hasFemale && !hasMale) {
                actorGroup = 2;
            } else {
                return 0;
            }
            if (group == 0) {
                group = actorGroup;
                continue;
            }
            if (group == actorGroup) continue;
            return 0;
        }
        return group;
    }

    private static boolean rollPercent(ServerWorld world, Entity debugSource, int percent, String label) {
        if (percent >= 100) {
            return true;
        }
        if (percent <= 0) {
            return false;
        }
        boolean success = world.getRandom().nextInt(100) < percent;
        NonMobEnergySystem.sendRollDebug(world, debugSource, label, percent, success);
        return success;
    }

    private static void sendRollDebug(ServerWorld world, Entity debugSource, String label, int percent, boolean success) {
        if (world == null || debugSource == null) {
            return;
        }
        if (!NeedsOfNature.getConfig().allowsDebugChat(NonDebugChatCategory.INFO)) {
            return;
        }
        String result = success ? "success" : "fail";
        NeedsOfNature.sendDebugChatToNearby(world, debugSource, 20.0, NonDebugChatCategory.INFO, (Text)Text.translatable((String)"debug.needsofnature.roll_result", (Object[])new Object[]{label, percent, result}));
    }
}

