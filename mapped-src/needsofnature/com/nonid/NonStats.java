/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.registry.Registry
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.stat.StatFormatter
 *  net.minecraft.stat.Stats
 *  net.minecraft.registry.Registries
 */
package com.nonid;

import com.nonid.NeedsOfNature;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.registry.Registries;

public final class NonStats {
    private static final String STAT_PATH_PEAKED_TOTAL = "peaked_animations_total";
    private static final String STAT_PATH_LIQUID_GAINED_TOTAL = "liquid_gained_ml_total";
    private static final String STAT_PATH_MULTI_ACTOR_TOTAL = "multi_actor_animations_total";
    private static final String STAT_PATH_WATER_TOTAL = "water_animations_total";
    private static final String STAT_PATH_ANIMATION_START_TOTAL = "animations_started_total";
    private static final String STAT_PATH_ATTACK_ANIMATION_START_TOTAL = "attack_animations_started_total";
    private static final String STAT_PATH_ATTACK_ESCAPE_SUCCESS_TOTAL = "attack_escapes_success_total";
    private static final String STAT_PATH_M_INJECTOR_PEAKS_ML_TOTAL = "m_injector_peaks_ml_total";
    private static final String STAT_PATH_MANUAL_PEAK_TOTAL = "manual_peaks_total";
    private static final String STAT_PATH_FILL_BOTTLE_TOTAL = "bottles_filled_total";
    private static final String STAT_PATH_PREGNANCY_STARTED_TOTAL = "pregnancies_started_total";
    private static final String STAT_PATH_PREGNANCY_COMPLETED_TOTAL = "pregnancies_completed_total";
    private static final String STAT_PATH_OFFSPRING_SPAWNED_TOTAL = "offspring_spawned_total";
    private static final Identifier STAT_PEAKED_TOTAL = NeedsOfNature.id("peaked_animations_total");
    private static final Identifier STAT_LIQUID_GAINED_TOTAL = NeedsOfNature.id("liquid_gained_ml_total");
    private static final Identifier STAT_MULTI_ACTOR_TOTAL = NeedsOfNature.id("multi_actor_animations_total");
    private static final Identifier STAT_WATER_TOTAL = NeedsOfNature.id("water_animations_total");
    private static final Identifier STAT_ANIMATION_START_TOTAL = NeedsOfNature.id("animations_started_total");
    private static final Identifier STAT_ATTACK_ANIMATION_START_TOTAL = NeedsOfNature.id("attack_animations_started_total");
    private static final Identifier STAT_ATTACK_ESCAPE_SUCCESS_TOTAL = NeedsOfNature.id("attack_escapes_success_total");
    private static final Identifier STAT_M_INJECTOR_PEAKS_ML_TOTAL = NeedsOfNature.id("m_injector_peaks_ml_total");
    private static final Identifier STAT_MANUAL_PEAK_TOTAL = NeedsOfNature.id("manual_peaks_total");
    private static final Identifier STAT_FILL_BOTTLE_TOTAL = NeedsOfNature.id("bottles_filled_total");
    private static final Identifier STAT_PREGNANCY_STARTED_TOTAL = NeedsOfNature.id("pregnancies_started_total");
    private static final Identifier STAT_PREGNANCY_COMPLETED_TOTAL = NeedsOfNature.id("pregnancies_completed_total");
    private static final Identifier STAT_OFFSPRING_SPAWNED_TOTAL = NeedsOfNature.id("offspring_spawned_total");
    private static final Map<Identifier, Identifier> PEAKED_BY_ENTITY = new LinkedHashMap<Identifier, Identifier>();
    private static final Map<Identifier, Identifier> LIQUID_GAINED_BY_ENTITY = new LinkedHashMap<Identifier, Identifier>();
    private static final Map<Identifier, Identifier> OFFSPRING_SPAWNED_BY_ENTITY = new LinkedHashMap<Identifier, Identifier>();

    private NonStats() {
    }

    public static void registerBaseStats() {
        NonStats.registerStat(STAT_PEAKED_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_LIQUID_GAINED_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_MULTI_ACTOR_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_WATER_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_ANIMATION_START_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_ATTACK_ANIMATION_START_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_ATTACK_ESCAPE_SUCCESS_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_M_INJECTOR_PEAKS_ML_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_MANUAL_PEAK_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_FILL_BOTTLE_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_PREGNANCY_STARTED_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_PREGNANCY_COMPLETED_TOTAL, StatFormatter.DEFAULT);
        NonStats.registerStat(STAT_OFFSPRING_SPAWNED_TOTAL, StatFormatter.DEFAULT);
    }

    public static void registerKnownEntityStats() {
        for (Identifier entityId : Registries.ENTITY_TYPE.getIds()) {
            NonStats.ensureEntityStats(entityId);
        }
    }

    public static void incrementAnimationStarted(ServerPlayerEntity player) {
        NonStats.increase(player, STAT_ANIMATION_START_TOTAL, 1);
    }

    public static void incrementMultiActorAnimation(ServerPlayerEntity player) {
        NonStats.increase(player, STAT_MULTI_ACTOR_TOTAL, 1);
    }

    public static void incrementWaterAnimation(ServerPlayerEntity player) {
        NonStats.increase(player, STAT_WATER_TOTAL, 1);
    }

    public static void incrementAttackAnimationStarted(ServerPlayerEntity player) {
        NonStats.increase(player, STAT_ATTACK_ANIMATION_START_TOTAL, 1);
    }

    public static void incrementAttackEscapeSuccess(ServerPlayerEntity player) {
        NonStats.increase(player, STAT_ATTACK_ESCAPE_SUCCESS_TOTAL, 1);
    }

    public static void addMInjectorPeaksMl(ServerPlayerEntity player, int amount) {
        NonStats.increase(player, STAT_M_INJECTOR_PEAKS_ML_TOTAL, amount);
    }

    public static void incrementManualPeak(ServerPlayerEntity player) {
        NonStats.increase(player, STAT_MANUAL_PEAK_TOTAL, 1);
    }

    public static void incrementFillBottle(ServerPlayerEntity player) {
        NonStats.increase(player, STAT_FILL_BOTTLE_TOTAL, 1);
    }

    public static int getBottlesFilled(ServerPlayerEntity player) {
        return NonStats.get(player, STAT_FILL_BOTTLE_TOTAL);
    }

    public static void incrementPregnancyStarted(ServerPlayerEntity player) {
        NonStats.increase(player, STAT_PREGNANCY_STARTED_TOTAL, 1);
    }

    public static void incrementPregnancyCompleted(ServerPlayerEntity player) {
        NonStats.increase(player, STAT_PREGNANCY_COMPLETED_TOTAL, 1);
    }

    public static int getPregnanciesCompleted(ServerPlayerEntity player) {
        return NonStats.get(player, STAT_PREGNANCY_COMPLETED_TOTAL);
    }

    public static void addOffspringSpawned(ServerPlayerEntity player, Identifier entityTypeId, int amount) {
        if (player == null || amount <= 0) {
            return;
        }
        NonStats.increase(player, STAT_OFFSPRING_SPAWNED_TOTAL, amount);
        if (entityTypeId == null) {
            return;
        }
        NonStats.ensureEntityStats(entityTypeId);
        Identifier stat = OFFSPRING_SPAWNED_BY_ENTITY.get(entityTypeId);
        if (stat != null) {
            NonStats.increase(player, stat, amount);
        }
    }

    public static void incrementPeakedAnimations(ServerPlayerEntity player, Set<Identifier> entityTypes) {
        NonStats.increase(player, STAT_PEAKED_TOTAL, 1);
        if (entityTypes == null || entityTypes.isEmpty()) {
            return;
        }
        for (Identifier entityTypeId : entityTypes) {
            if (entityTypeId == null) continue;
            NonStats.ensureEntityStats(entityTypeId);
            Identifier stat = PEAKED_BY_ENTITY.get(entityTypeId);
            if (stat == null) continue;
            NonStats.increase(player, stat, 1);
        }
    }

    public static void addLiquidGained(ServerPlayerEntity player, int amount, Map<Identifier, Integer> perEntityAmounts) {
        if (amount <= 0) {
            return;
        }
        NonStats.increase(player, STAT_LIQUID_GAINED_TOTAL, amount);
        if (perEntityAmounts == null || perEntityAmounts.isEmpty()) {
            return;
        }
        for (Map.Entry<Identifier, Integer> entry : perEntityAmounts.entrySet()) {
            int entityAmount;
            Identifier entityId = entry.getKey();
            int n = entityAmount = entry.getValue() == null ? 0 : entry.getValue();
            if (entityId == null || entityAmount <= 0) continue;
            NonStats.ensureEntityStats(entityId);
            Identifier stat = LIQUID_GAINED_BY_ENTITY.get(entityId);
            if (stat == null) continue;
            NonStats.increase(player, stat, entityAmount);
        }
    }

    private static void increase(ServerPlayerEntity player, Identifier statId, int amount) {
        if (player == null || statId == null || amount <= 0) {
            return;
        }
        if (!NonStats.registerStat(statId, StatFormatter.DEFAULT)) {
            return;
        }
        player.increaseStat(statId, amount);
    }

    private static int get(ServerPlayerEntity player, Identifier statId) {
        if (player == null || statId == null) {
            return 0;
        }
        if (!NonStats.registerStat(statId, StatFormatter.DEFAULT)) {
            return 0;
        }
        return player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat((Object)statId));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void ensureEntityStats(Identifier entityId) {
        Class<NonStats> clazz = NonStats.class;
        synchronized (NonStats.class) {
            Identifier offspringStat;
            Identifier liquidStat;
            Identifier peakedStat;
            if (!PEAKED_BY_ENTITY.containsKey(entityId) && NonStats.registerStat(peakedStat = NeedsOfNature.id("peaked_animations_entity_" + NonStats.sanitizeEntityId(entityId)), StatFormatter.DEFAULT)) {
                PEAKED_BY_ENTITY.put(entityId, peakedStat);
            }
            if (!LIQUID_GAINED_BY_ENTITY.containsKey(entityId) && NonStats.registerStat(liquidStat = NeedsOfNature.id("liquid_gained_ml_entity_" + NonStats.sanitizeEntityId(entityId)), StatFormatter.DEFAULT)) {
                LIQUID_GAINED_BY_ENTITY.put(entityId, liquidStat);
            }
            if (!OFFSPRING_SPAWNED_BY_ENTITY.containsKey(entityId) && NonStats.registerStat(offspringStat = NeedsOfNature.id("offspring_spawned_entity_" + NonStats.sanitizeEntityId(entityId)), StatFormatter.DEFAULT)) {
                OFFSPRING_SPAWNED_BY_ENTITY.put(entityId, offspringStat);
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return;
        }
    }

    private static boolean registerStat(Identifier statId, StatFormatter formatter) {
        if (statId == null) {
            return false;
        }
        if (!Registries.CUSTOM_STAT.containsId(statId)) {
            try {
                Registry.register((Registry)Registries.CUSTOM_STAT, (Identifier)statId, (Object)statId);
            }
            catch (RuntimeException ex) {
                return false;
            }
        }
        Stats.CUSTOM.getOrCreateStat((Object)statId, formatter);
        return true;
    }

    private static String sanitizeEntityId(Identifier entityId) {
        return NonStats.sanitizePathPart(entityId.getNamespace()) + "_" + NonStats.sanitizePathPart(entityId.getPath());
    }

    private static String sanitizePathPart(String value) {
        if (value == null || value.isBlank()) {
            return "unknown";
        }
        StringBuilder out = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (c >= 'a' && c <= 'z') {
                out.append(c);
                continue;
            }
            if (c >= '0' && c <= '9') {
                out.append(c);
                continue;
            }
            out.append('_');
        }
        if (out.isEmpty()) {
            return "unknown";
        }
        return out.toString();
    }
}

