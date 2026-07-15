/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.advancement.AdvancementEntry
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
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.advancement.Advancement;
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

    static void grant(ServerPlayerEntity player, String path) {
        Advancement advancement;
        if (player == null || path == null || path.isBlank()) {
            return;
        }
        if (!(player.getEntityWorld() instanceof ServerWorld world)) {
            return;
        }
        MinecraftServer server = world.getServer();
        if (server == null) {
            return;
        }
        if (!ROOT.equals(path)) {
            NonAdvancementHooks.grant(player, ROOT);
        }
        if ((advancement = server.getAdvancementLoader().get(NeedsOfNature.id(path))) == null) {
            return;
        }
        player.getAdvancementTracker().grantCriterion(advancement, "done");
    }

    static void grantEmergencySnack(ServerPlayerEntity player) {
        NonAdvancementHooks.grant(player, EMERGENCY_SNACK);
        NonAdvancementHooks.recordStableDietSnack(player);
    }

    static void resetStableDietStreak(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        STABLE_DIET_STREAKS.remove(player.getUuid());
    }

    private static void recordStableDietSnack(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        int streak = STABLE_DIET_STREAKS.merge(player.getUuid(), 1, Integer::sum);
        if (streak >= 3) {
            NonAdvancementHooks.grant(player, STABLE_DIET);
        }
    }

    static void checkLiquidAdvancements(ServerPlayerEntity player, LiquidHolder holder) {
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

    static void trackFieldResearch(ServerWorld world, ServerPlayerEntity player, Map<Identifier, Integer> perEntityAmounts) {
        if (world == null || player == null || perEntityAmounts == null || perEntityAmounts.isEmpty()) {
            return;
        }
        LinkedHashSet<Identifier> gainedTypes = new LinkedHashSet<Identifier>();
        for (Map.Entry<Identifier, Integer> entry : perEntityAmounts.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null || entry.getValue() <= 0) continue;
            gainedTypes.add(entry.getKey());
        }
        if (gainedTypes.isEmpty()) {
            return;
        }
        long day = world.getTimeOfDay() / 24000L;
        UUID playerUuid = player.getUuid();
        FieldResearchState existing = FIELD_RESEARCH_STATES.get(playerUuid);
        LinkedHashSet<Identifier> entityTypes = new LinkedHashSet<Identifier>();
        if (existing != null && existing.day() == day) {
            entityTypes.addAll(existing.entityTypes());
        }
        entityTypes.addAll(gainedTypes);
        FIELD_RESEARCH_STATES.put(playerUuid, new FieldResearchState(day, Set.copyOf(entityTypes)));
        if (entityTypes.size() >= 5) {
            NonAdvancementHooks.grant(player, FIELD_RESEARCH);
        }
    }

    static void checkBadIdeaBeacon(ServerPlayerEntity player) {
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

    static void grantNaturalCuriosity(ServerPlayerEntity player) {
        NonAdvancementHooks.grant(player, NATURAL_CURIOSITY);
    }

    static void grantGroupActivity(ServerPlayerEntity player) {
        NonAdvancementHooks.grant(player, GROUP_ACTIVITY);
    }

    static void grantMessedUp(ServerPlayerEntity player) {
        NonAdvancementHooks.grant(player, MESSED_UP);
    }

    static void checkCollectorsHabit(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        if (NonStats.getBottlesFilled(player) >= 25) {
            NonAdvancementHooks.grant(player, COLLECTORS_HABIT);
        }
    }

    static void resetTossedAround(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        TOSSED_AROUND_STREAKS.remove(player.getUuid());
    }

    static void recordTossedAround(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        int streak = TOSSED_AROUND_STREAKS.merge(player.getUuid(), 1, Integer::sum);
        if (streak >= 5) {
            NonAdvancementHooks.grant(player, TOSSED_AROUND);
        }
    }

    static void grantFirstPregnancy(ServerPlayerEntity player) {
        NonAdvancementHooks.grant(player, FIRST_PREGNANCY);
    }

    static void checkMotherNature(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        if (NonStats.getPregnanciesCompleted(player) >= 25) {
            NonAdvancementHooks.grant(player, MOTHER_NATURE);
        }
    }

    static void grantProductivePregnancy(ServerPlayerEntity player) {
        NonAdvancementHooks.grant(player, PRODUCTIVE_PREGNANCY);
    }

    static void grantUdderlyUnfortunate(ServerPlayerEntity player) {
        NonAdvancementHooks.grant(player, UDDERLY_UNFORTUNATE);
    }

    static void grantAcquireHardware(ServerPlayerEntity player) {
        NonAdvancementHooks.grant(player, ACQUIRE_HARDWARE);
    }

    private record FieldResearchState(long day, Set<Identifier> entityTypes) {
    }
}

