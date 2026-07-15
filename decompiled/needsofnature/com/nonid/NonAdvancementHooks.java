/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_8779
 *  net.minecraft.server.MinecraftServer
 */
package com.nonid;

import com.nonid.EnergyHolder;
import com.nonid.LiquidHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonStats;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_8779;
import net.minecraft.server.MinecraftServer;

final class NonAdvancementHooks {
    private static final int MOTHER_NATURE_PREGNANCIES = 25;
    private static final int COLLECTORS_HABIT_BOTTLES = 25;
    private static final int FIELD_RESEARCH_TYPES = 5;
    private static final int TOSSED_AROUND_CHAIN = 5;
    private static final int STABLE_DIET_CHAIN = 3;
    private static final String ROOT = "root";
    private static final String NATURAL_CURIOSITY = "natural_curiosity";
    private static final String FILLED_TO_THE_BRIM = "filled_to_the_brim";
    private static final String FAVOURITISM = "favouritism";
    private static final String FIRST_PREGNANCY = "first_pregnancy";
    private static final String MOTHER_NATURE = "mother_nature";
    private static final String MESSED_UP = "messed_up";
    private static final String GROUP_ACTIVITY = "group_activity";
    private static final String TOSSED_AROUND = "tossed_around";
    private static final String COLLECTORS_HABIT = "collectors_habit";
    private static final String FIELD_RESEARCH = "field_research";
    private static final String BAD_IDEA_BEACON = "bad_idea_beacon";
    private static final String PRODUCTIVE_PREGNANCY = "productive_pregnancy";
    private static final String EMERGENCY_SNACK = "emergency_snack";
    private static final String STABLE_DIET = "stable_diet";
    private static final String UDDERLY_UNFORTUNATE = "udderly_unfortunate";
    private static final String ACQUIRE_HARDWARE = "acquire_hardware";
    private static final Map<UUID, FieldResearchState> FIELD_RESEARCH_STATES = new ConcurrentHashMap<UUID, FieldResearchState>();
    private static final Map<UUID, Integer> TOSSED_AROUND_STREAKS = new ConcurrentHashMap<UUID, Integer>();
    private static final Map<UUID, Integer> STABLE_DIET_STREAKS = new ConcurrentHashMap<UUID, Integer>();

    private NonAdvancementHooks() {
    }

    static void clearPlayerState(UUID playerUuid) {
        if (playerUuid == null) {
            return;
        }
        FIELD_RESEARCH_STATES.remove(playerUuid);
        TOSSED_AROUND_STREAKS.remove(playerUuid);
        STABLE_DIET_STREAKS.remove(playerUuid);
    }

    static void grant(class_3222 player, String path) {
        class_8779 advancement;
        if (player == null || path == null || path.isBlank()) {
            return;
        }
        class_3218 class_32182 = player.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            return;
        }
        class_3218 world = class_32182;
        MinecraftServer server = world.method_8503();
        if (server == null) {
            return;
        }
        if (!ROOT.equals(path)) {
            NonAdvancementHooks.grant(player, ROOT);
        }
        if ((advancement = server.method_3851().method_12896(NeedsOfNature.id(path))) == null) {
            return;
        }
        player.method_14236().method_12878(advancement, "done");
    }

    static void grantEmergencySnack(class_3222 player) {
        NonAdvancementHooks.grant(player, EMERGENCY_SNACK);
        NonAdvancementHooks.recordStableDietSnack(player);
    }

    static void resetStableDietStreak(class_3222 player) {
        if (player == null) {
            return;
        }
        STABLE_DIET_STREAKS.remove(player.method_5667());
    }

    private static void recordStableDietSnack(class_3222 player) {
        if (player == null) {
            return;
        }
        int streak = STABLE_DIET_STREAKS.merge(player.method_5667(), 1, Integer::sum);
        if (streak >= 3) {
            NonAdvancementHooks.grant(player, STABLE_DIET);
        }
    }

    static void checkLiquidAdvancements(class_3222 player, LiquidHolder holder) {
        if (player == null || holder == null) {
            return;
        }
        int capacity = holder.getLiquidCapacity();
        if (capacity <= 0 || holder.getLiquidStored() < capacity) {
            return;
        }
        NonAdvancementHooks.grant(player, FILLED_TO_THE_BRIM);
        if (holder.getLiquidComposition() == LiquidHolder.LiquidComposition.ENTITY && holder.getLiquidEntityTypeId() != null) {
            NonAdvancementHooks.grant(player, FAVOURITISM);
        }
    }

    static void trackFieldResearch(class_3218 world, class_3222 player, Map<class_2960, Integer> perEntityAmounts) {
        if (world == null || player == null || perEntityAmounts == null || perEntityAmounts.isEmpty()) {
            return;
        }
        LinkedHashSet<class_2960> gainedTypes = new LinkedHashSet<class_2960>();
        for (Map.Entry<class_2960, Integer> entry : perEntityAmounts.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null || entry.getValue() <= 0) continue;
            gainedTypes.add(entry.getKey());
        }
        if (gainedTypes.isEmpty()) {
            return;
        }
        long day = world.method_8532() / 24000L;
        UUID playerUuid = player.method_5667();
        FieldResearchState existing = FIELD_RESEARCH_STATES.get(playerUuid);
        LinkedHashSet<class_2960> entityTypes = new LinkedHashSet<class_2960>();
        if (existing != null && existing.day() == day) {
            entityTypes.addAll(existing.entityTypes());
        }
        entityTypes.addAll(gainedTypes);
        FIELD_RESEARCH_STATES.put(playerUuid, new FieldResearchState(day, Set.copyOf(entityTypes)));
        if (entityTypes.size() >= 5) {
            NonAdvancementHooks.grant(player, FIELD_RESEARCH);
        }
    }

    static void checkBadIdeaBeacon(class_3222 player) {
        if (player == null || !(player instanceof EnergyHolder)) {
            return;
        }
        EnergyHolder energyHolder = (EnergyHolder)player;
        if (energyHolder.getEnergy() < 200) {
            return;
        }
        if (NeedsOfNature.getDestroyedSkinStageForPlayer(player) < 4) {
            return;
        }
        NonAdvancementHooks.grant(player, BAD_IDEA_BEACON);
    }

    static void grantNaturalCuriosity(class_3222 player) {
        NonAdvancementHooks.grant(player, NATURAL_CURIOSITY);
    }

    static void grantGroupActivity(class_3222 player) {
        NonAdvancementHooks.grant(player, GROUP_ACTIVITY);
    }

    static void grantMessedUp(class_3222 player) {
        NonAdvancementHooks.grant(player, MESSED_UP);
    }

    static void checkCollectorsHabit(class_3222 player) {
        if (player == null) {
            return;
        }
        if (NonStats.getBottlesFilled(player) >= 25) {
            NonAdvancementHooks.grant(player, COLLECTORS_HABIT);
        }
    }

    static void resetTossedAround(class_3222 player) {
        if (player == null) {
            return;
        }
        TOSSED_AROUND_STREAKS.remove(player.method_5667());
    }

    static void recordTossedAround(class_3222 player) {
        if (player == null) {
            return;
        }
        int streak = TOSSED_AROUND_STREAKS.merge(player.method_5667(), 1, Integer::sum);
        if (streak >= 5) {
            NonAdvancementHooks.grant(player, TOSSED_AROUND);
        }
    }

    static void grantFirstPregnancy(class_3222 player) {
        NonAdvancementHooks.grant(player, FIRST_PREGNANCY);
    }

    static void checkMotherNature(class_3222 player) {
        if (player == null) {
            return;
        }
        if (NonStats.getPregnanciesCompleted(player) >= 25) {
            NonAdvancementHooks.grant(player, MOTHER_NATURE);
        }
    }

    static void grantProductivePregnancy(class_3222 player) {
        NonAdvancementHooks.grant(player, PRODUCTIVE_PREGNANCY);
    }

    static void grantUdderlyUnfortunate(class_3222 player) {
        NonAdvancementHooks.grant(player, UDDERLY_UNFORTUNATE);
    }

    static void grantAcquireHardware(class_3222 player) {
        NonAdvancementHooks.grant(player, ACQUIRE_HARDWARE);
    }

    private record FieldResearchState(long day, Set<class_2960> entityTypes) {
    }
}

