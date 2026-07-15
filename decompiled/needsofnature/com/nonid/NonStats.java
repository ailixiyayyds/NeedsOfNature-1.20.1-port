/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2378
 *  net.minecraft.class_2960
 *  net.minecraft.class_3222
 *  net.minecraft.class_3446
 *  net.minecraft.class_3468
 *  net.minecraft.class_7923
 */
package com.nonid;

import com.nonid.NeedsOfNature;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_3446;
import net.minecraft.class_3468;
import net.minecraft.class_7923;

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
    private static final class_2960 STAT_PEAKED_TOTAL = NeedsOfNature.id("peaked_animations_total");
    private static final class_2960 STAT_LIQUID_GAINED_TOTAL = NeedsOfNature.id("liquid_gained_ml_total");
    private static final class_2960 STAT_MULTI_ACTOR_TOTAL = NeedsOfNature.id("multi_actor_animations_total");
    private static final class_2960 STAT_WATER_TOTAL = NeedsOfNature.id("water_animations_total");
    private static final class_2960 STAT_ANIMATION_START_TOTAL = NeedsOfNature.id("animations_started_total");
    private static final class_2960 STAT_ATTACK_ANIMATION_START_TOTAL = NeedsOfNature.id("attack_animations_started_total");
    private static final class_2960 STAT_ATTACK_ESCAPE_SUCCESS_TOTAL = NeedsOfNature.id("attack_escapes_success_total");
    private static final class_2960 STAT_M_INJECTOR_PEAKS_ML_TOTAL = NeedsOfNature.id("m_injector_peaks_ml_total");
    private static final class_2960 STAT_MANUAL_PEAK_TOTAL = NeedsOfNature.id("manual_peaks_total");
    private static final class_2960 STAT_FILL_BOTTLE_TOTAL = NeedsOfNature.id("bottles_filled_total");
    private static final class_2960 STAT_PREGNANCY_STARTED_TOTAL = NeedsOfNature.id("pregnancies_started_total");
    private static final class_2960 STAT_PREGNANCY_COMPLETED_TOTAL = NeedsOfNature.id("pregnancies_completed_total");
    private static final class_2960 STAT_OFFSPRING_SPAWNED_TOTAL = NeedsOfNature.id("offspring_spawned_total");
    private static final Map<class_2960, class_2960> PEAKED_BY_ENTITY = new LinkedHashMap<class_2960, class_2960>();
    private static final Map<class_2960, class_2960> LIQUID_GAINED_BY_ENTITY = new LinkedHashMap<class_2960, class_2960>();
    private static final Map<class_2960, class_2960> OFFSPRING_SPAWNED_BY_ENTITY = new LinkedHashMap<class_2960, class_2960>();

    private NonStats() {
    }

    public static void registerBaseStats() {
        NonStats.registerStat(STAT_PEAKED_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_LIQUID_GAINED_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_MULTI_ACTOR_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_WATER_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_ANIMATION_START_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_ATTACK_ANIMATION_START_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_ATTACK_ESCAPE_SUCCESS_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_M_INJECTOR_PEAKS_ML_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_MANUAL_PEAK_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_FILL_BOTTLE_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_PREGNANCY_STARTED_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_PREGNANCY_COMPLETED_TOTAL, class_3446.field_16975);
        NonStats.registerStat(STAT_OFFSPRING_SPAWNED_TOTAL, class_3446.field_16975);
    }

    public static void registerKnownEntityStats() {
        for (class_2960 entityId : class_7923.field_41177.method_10235()) {
            NonStats.ensureEntityStats(entityId);
        }
    }

    public static void incrementAnimationStarted(class_3222 player) {
        NonStats.increase(player, STAT_ANIMATION_START_TOTAL, 1);
    }

    public static void incrementMultiActorAnimation(class_3222 player) {
        NonStats.increase(player, STAT_MULTI_ACTOR_TOTAL, 1);
    }

    public static void incrementWaterAnimation(class_3222 player) {
        NonStats.increase(player, STAT_WATER_TOTAL, 1);
    }

    public static void incrementAttackAnimationStarted(class_3222 player) {
        NonStats.increase(player, STAT_ATTACK_ANIMATION_START_TOTAL, 1);
    }

    public static void incrementAttackEscapeSuccess(class_3222 player) {
        NonStats.increase(player, STAT_ATTACK_ESCAPE_SUCCESS_TOTAL, 1);
    }

    public static void addMInjectorPeaksMl(class_3222 player, int amount) {
        NonStats.increase(player, STAT_M_INJECTOR_PEAKS_ML_TOTAL, amount);
    }

    public static void incrementManualPeak(class_3222 player) {
        NonStats.increase(player, STAT_MANUAL_PEAK_TOTAL, 1);
    }

    public static void incrementFillBottle(class_3222 player) {
        NonStats.increase(player, STAT_FILL_BOTTLE_TOTAL, 1);
    }

    public static int getBottlesFilled(class_3222 player) {
        return NonStats.get(player, STAT_FILL_BOTTLE_TOTAL);
    }

    public static void incrementPregnancyStarted(class_3222 player) {
        NonStats.increase(player, STAT_PREGNANCY_STARTED_TOTAL, 1);
    }

    public static void incrementPregnancyCompleted(class_3222 player) {
        NonStats.increase(player, STAT_PREGNANCY_COMPLETED_TOTAL, 1);
    }

    public static int getPregnanciesCompleted(class_3222 player) {
        return NonStats.get(player, STAT_PREGNANCY_COMPLETED_TOTAL);
    }

    public static void addOffspringSpawned(class_3222 player, class_2960 entityTypeId, int amount) {
        if (player == null || amount <= 0) {
            return;
        }
        NonStats.increase(player, STAT_OFFSPRING_SPAWNED_TOTAL, amount);
        if (entityTypeId == null) {
            return;
        }
        NonStats.ensureEntityStats(entityTypeId);
        class_2960 stat = OFFSPRING_SPAWNED_BY_ENTITY.get(entityTypeId);
        if (stat != null) {
            NonStats.increase(player, stat, amount);
        }
    }

    public static void incrementPeakedAnimations(class_3222 player, Set<class_2960> entityTypes) {
        NonStats.increase(player, STAT_PEAKED_TOTAL, 1);
        if (entityTypes == null || entityTypes.isEmpty()) {
            return;
        }
        for (class_2960 entityTypeId : entityTypes) {
            if (entityTypeId == null) continue;
            NonStats.ensureEntityStats(entityTypeId);
            class_2960 stat = PEAKED_BY_ENTITY.get(entityTypeId);
            if (stat == null) continue;
            NonStats.increase(player, stat, 1);
        }
    }

    public static void addLiquidGained(class_3222 player, int amount, Map<class_2960, Integer> perEntityAmounts) {
        if (amount <= 0) {
            return;
        }
        NonStats.increase(player, STAT_LIQUID_GAINED_TOTAL, amount);
        if (perEntityAmounts == null || perEntityAmounts.isEmpty()) {
            return;
        }
        for (Map.Entry<class_2960, Integer> entry : perEntityAmounts.entrySet()) {
            int entityAmount;
            class_2960 entityId = entry.getKey();
            int n = entityAmount = entry.getValue() == null ? 0 : entry.getValue();
            if (entityId == null || entityAmount <= 0) continue;
            NonStats.ensureEntityStats(entityId);
            class_2960 stat = LIQUID_GAINED_BY_ENTITY.get(entityId);
            if (stat == null) continue;
            NonStats.increase(player, stat, entityAmount);
        }
    }

    private static void increase(class_3222 player, class_2960 statId, int amount) {
        if (player == null || statId == null || amount <= 0) {
            return;
        }
        if (!NonStats.registerStat(statId, class_3446.field_16975)) {
            return;
        }
        player.method_7339(statId, amount);
    }

    private static int get(class_3222 player, class_2960 statId) {
        if (player == null || statId == null) {
            return 0;
        }
        if (!NonStats.registerStat(statId, class_3446.field_16975)) {
            return 0;
        }
        return player.method_14248().method_15025(class_3468.field_15419.method_14956((Object)statId));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void ensureEntityStats(class_2960 entityId) {
        Class<NonStats> clazz = NonStats.class;
        synchronized (NonStats.class) {
            class_2960 offspringStat;
            class_2960 liquidStat;
            class_2960 peakedStat;
            if (!PEAKED_BY_ENTITY.containsKey(entityId) && NonStats.registerStat(peakedStat = NeedsOfNature.id("peaked_animations_entity_" + NonStats.sanitizeEntityId(entityId)), class_3446.field_16975)) {
                PEAKED_BY_ENTITY.put(entityId, peakedStat);
            }
            if (!LIQUID_GAINED_BY_ENTITY.containsKey(entityId) && NonStats.registerStat(liquidStat = NeedsOfNature.id("liquid_gained_ml_entity_" + NonStats.sanitizeEntityId(entityId)), class_3446.field_16975)) {
                LIQUID_GAINED_BY_ENTITY.put(entityId, liquidStat);
            }
            if (!OFFSPRING_SPAWNED_BY_ENTITY.containsKey(entityId) && NonStats.registerStat(offspringStat = NeedsOfNature.id("offspring_spawned_entity_" + NonStats.sanitizeEntityId(entityId)), class_3446.field_16975)) {
                OFFSPRING_SPAWNED_BY_ENTITY.put(entityId, offspringStat);
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return;
        }
    }

    private static boolean registerStat(class_2960 statId, class_3446 formatter) {
        if (statId == null) {
            return false;
        }
        if (!class_7923.field_41183.method_10250(statId)) {
            try {
                class_2378.method_10230((class_2378)class_7923.field_41183, (class_2960)statId, (Object)statId);
            }
            catch (RuntimeException ex) {
                return false;
            }
        }
        class_3468.field_15419.method_14955((Object)statId, formatter);
        return true;
    }

    private static String sanitizeEntityId(class_2960 entityId) {
        return NonStats.sanitizePathPart(entityId.method_12836()) + "_" + NonStats.sanitizePathPart(entityId.method_12832());
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

