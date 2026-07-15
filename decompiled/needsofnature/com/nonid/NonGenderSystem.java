/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
 *  net.fabricmc.fabric.api.event.player.UseEntityCallback
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.class_10730
 *  net.minecraft.class_1269
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1308
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2561
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_8710
 */
package com.nonid;

import com.nonid.EnergyHolder;
import com.nonid.GenderHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonApiInternals;
import com.nonid.NonConfig;
import com.nonid.NonLiquidTankType;
import com.nonid.NonMessSystem;
import com.nonid.api.NonChangeSource;
import com.nonid.data.NonEntityProfiles;
import com.nonid.network.GenderSelectionPromptS2CPayload;
import com.nonid.network.SetPlayerGenderC2SPayload;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_10730;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1308;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_8710;

final class NonGenderSystem {
    private static final String GENDER_TAG_MALE = "gender.male";
    private static final String GENDER_TAG_FEMALE = "gender.female";
    private static final String GENDER_TAG_SELECTED = "gender.selected";
    private static final int DEFAULT_PLAYER_GENDER_MASK = 2;

    private NonGenderSystem() {
    }

    static void registerMobGenderTags() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            Object holder;
            if (!(entity instanceof class_1308)) {
                return;
            }
            if (entity.method_5864() == NeedsOfNature.HORSE_LIQUID_COLLECTOR_ENTITY_TYPE) {
                return;
            }
            if (entity.method_5864() == NeedsOfNature.PREGNANCY_EGG_ENTITY_TYPE) {
                return;
            }
            Set tags = entity.method_5752();
            boolean hasMale = tags.contains(GENDER_TAG_MALE);
            boolean hasFemale = tags.contains(GENDER_TAG_FEMALE);
            if (!hasMale && !hasFemale) {
                NonConfig.GenderSpawnChances chances = NonGenderSystem.resolveGenderSpawnChances(entity);
                int roll = world.method_8409().method_43048(100);
                if (roll < chances.male()) {
                    entity.method_5780(GENDER_TAG_MALE);
                    hasMale = true;
                } else if (roll < chances.male() + chances.female()) {
                    entity.method_5780(GENDER_TAG_FEMALE);
                    hasFemale = true;
                } else {
                    entity.method_5780(GENDER_TAG_MALE);
                    entity.method_5780(GENDER_TAG_FEMALE);
                    hasMale = true;
                    hasFemale = true;
                }
            }
            if (entity instanceof GenderHolder) {
                holder = (GenderHolder)entity;
                int mask = 0;
                if (hasMale) {
                    mask |= 1;
                }
                if (hasFemale) {
                    mask |= 2;
                }
                holder.setGenderMask(mask);
            }
            if (entity instanceof EnergyHolder) {
                holder = (EnergyHolder)entity;
                if (world instanceof class_3218) {
                    class_3218 serverWorld = world;
                    holder.ensureEnergyInitialized(serverWorld);
                }
            }
        });
    }

    static void registerCowMilkingGenderGate() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            NonConfig config = NeedsOfNature.getConfig();
            if (config == null || !config.femaleCowOnlyMilking()) {
                return class_1269.field_5811;
            }
            if (!(entity instanceof class_10730)) {
                return class_1269.field_5811;
            }
            class_10730 cow = (class_10730)entity;
            if (cow.method_5864() != class_1299.field_6085) {
                return class_1269.field_5811;
            }
            class_1799 stack = player.method_5998(hand);
            if (stack.method_7960() || !stack.method_31574(class_1802.field_8550)) {
                return class_1269.field_5811;
            }
            return NonGenderSystem.canCowBeMilked(cow) ? class_1269.field_5811 : class_1269.field_5814;
        });
    }

    static void registerPlayerGenderRequests() {
        ServerPlayNetworking.registerGlobalReceiver(SetPlayerGenderC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            class_3222 player = context.player();
            if (player == null) {
                return;
            }
            NonGenderSystem.handleClientGenderRequest(player, payload.mask(), payload.initialSelection());
        }));
    }

    static void ensurePlayerGenderInitialized(class_3222 player) {
        if (player == null) {
            return;
        }
        int mask = 0;
        if (player instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)player;
            mask = holder.getGenderMask() & 3;
        }
        if (mask == 0) {
            mask = NonGenderSystem.resolveGenderMaskFromTags((class_1297)player);
        }
        if (mask != 0) {
            NonGenderSystem.markPlayerGenderSelected(player);
        }
        if (mask == 0) {
            NonConfig config = NeedsOfNature.getConfig();
            if (config.requirePlayerGenderSelectionOnJoin()) {
                NonGenderSystem.sendGenderSelectionPrompt(player);
                return;
            }
            mask = 2;
            NonGenderSystem.applyPlayerGenderMask(player, mask, true, NonChangeSource.SYSTEM);
            return;
        }
        NonGenderSystem.applyGenderMask((class_1297)player, mask);
        if (NeedsOfNature.getConfig().requirePlayerGenderSelectionOnJoin() && !NonGenderSystem.hasPlayerGenderSelected(player)) {
            NonGenderSystem.sendGenderSelectionPrompt(player);
        }
    }

    static int setPlayerGenderFromCommand(class_3222 player, int mask) {
        return NonGenderSystem.setPlayerGenderFromCommand(player, mask, NonChangeSource.COMMAND);
    }

    static int setPlayerGenderFromCommand(class_3222 player, int mask, NonChangeSource source) {
        int normalized = NonGenderSystem.normalizeGenderMask(mask);
        NonGenderSystem.applyPlayerGenderMask(player, normalized, true, source == null ? NonChangeSource.COMMAND : source);
        return normalized;
    }

    static void resetPlayerGenderSelection(class_3222 player) {
        if (player == null) {
            return;
        }
        NonGenderSystem.clearPlayerGenderSelectionState(player);
        NonGenderSystem.sendGenderSelectionPrompt(player);
    }

    static boolean canClientChangeGenderFreely() {
        return NeedsOfNature.getConfig().allowPlayerGenderChangeAnytime();
    }

    static boolean isStartingGenderAllowed(int mask) {
        int normalized = NonGenderSystem.normalizeGenderMask(mask);
        int allowed = NeedsOfNature.getConfig().getAllowedStartingGenderMask();
        return (allowed & NonGenderSystem.startingGenderChoiceBit(normalized)) != 0;
    }

    private static void handleClientGenderRequest(class_3222 player, int requestedMask, boolean initialSelection) {
        NonConfig config = NeedsOfNature.getConfig();
        int normalized = NonGenderSystem.normalizeGenderMask(requestedMask);
        boolean selected = NonGenderSystem.hasPlayerGenderSelected(player);
        if (initialSelection) {
            if (selected && !config.allowPlayerGenderChangeAnytime()) {
                NonGenderSystem.sendCurrentGenderFeedback(player, "Gender has already been selected on this server.");
                return;
            }
            if (!NonGenderSystem.isStartingGenderAllowed(normalized)) {
                NonGenderSystem.sendGenderSelectionPrompt(player);
                return;
            }
            NonGenderSystem.applyPlayerGenderMask(player, normalized, true, NonChangeSource.JOIN_SELECTION);
            return;
        }
        if (!config.allowPlayerGenderChangeAnytime()) {
            if (!selected && config.requirePlayerGenderSelectionOnJoin()) {
                NonGenderSystem.sendGenderSelectionPrompt(player);
                return;
            }
            if (selected) {
                NonGenderSystem.sendCurrentGenderFeedback(player, "Gender changes are disabled by this server.");
                return;
            }
            if (!NonGenderSystem.isStartingGenderAllowed(normalized)) {
                NonGenderSystem.sendGenderSelectionPrompt(player);
                return;
            }
        }
        NonGenderSystem.applyPlayerGenderMask(player, normalized, true, NonChangeSource.CLIENT_PREFERENCE);
    }

    private static void applyPlayerGenderMask(class_3222 player, int mask, boolean markSelected) {
        NonGenderSystem.applyPlayerGenderMask(player, mask, markSelected, NonChangeSource.SYSTEM);
    }

    private static void applyPlayerGenderMask(class_3222 player, int mask, boolean markSelected, NonChangeSource source) {
        NonGenderSystem.applyGenderMask((class_1297)player, mask, source);
        if (markSelected) {
            NonGenderSystem.markPlayerGenderSelected(player);
        }
    }

    private static void markPlayerGenderSelected(class_3222 player) {
        if (player != null) {
            player.method_5780(GENDER_TAG_SELECTED);
        }
    }

    private static void clearPlayerGenderSelectionState(class_3222 player) {
        if (player == null) {
            return;
        }
        player.method_5738(GENDER_TAG_SELECTED);
        player.method_5738(GENDER_TAG_MALE);
        player.method_5738(GENDER_TAG_FEMALE);
        if (player instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)player;
            holder.setGenderMask(0);
        }
    }

    private static boolean hasPlayerGenderSelected(class_3222 player) {
        return player != null && player.method_5752().contains(GENDER_TAG_SELECTED);
    }

    private static void sendGenderSelectionPrompt(class_3222 player) {
        if (player == null) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        ServerPlayNetworking.send((class_3222)player, (class_8710)new GenderSelectionPromptS2CPayload(config.getAllowedStartingGenderMask(), NonGenderSystem.currentOrDefaultPlayerMask(player), !config.allowPlayerGenderChangeAnytime()));
    }

    private static void sendCurrentGenderFeedback(class_3222 player, String message) {
        if (player != null) {
            player.method_7353((class_2561)class_2561.method_43470((String)("[NoN] " + message)), false);
        }
    }

    private static int currentOrDefaultPlayerMask(class_3222 player) {
        GenderHolder holder;
        int mask;
        if (player instanceof GenderHolder && (mask = (holder = (GenderHolder)player).getGenderMask() & 3) != 0) {
            return mask;
        }
        return NonGenderSystem.firstAllowedStartingMask(NeedsOfNature.getConfig().getAllowedStartingGenderMask());
    }

    private static int firstAllowedStartingMask(int allowedMask) {
        int allowed = allowedMask & 3;
        if ((allowedMask & 2) != 0) {
            return 2;
        }
        if ((allowedMask & 1) != 0) {
            return 1;
        }
        if ((allowedMask & 4) != 0) {
            return 3;
        }
        return 3;
    }

    private static int startingGenderChoiceBit(int genderMask) {
        return switch (genderMask & 3) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            default -> 2;
        };
    }

    private static int normalizeGenderMask(int mask) {
        int normalized = mask & 3;
        return normalized == 0 ? 2 : normalized;
    }

    static NonConfig.GenderSpawnChances resolveGenderSpawnChances(class_1297 entity) {
        return NonEntityProfiles.resolve(entity).genderSpawn();
    }

    static Map<String, NonConfig.GenderSpawnChances> resolveEffectiveGenderSpawnMap() {
        return NonEntityProfiles.resolveEffectiveGenderSpawnMap();
    }

    static boolean hasMaleTag(class_1297 entity) {
        return entity != null && entity.method_5752().contains(GENDER_TAG_MALE);
    }

    static boolean hasFemaleTag(class_1297 entity) {
        return entity != null && entity.method_5752().contains(GENDER_TAG_FEMALE);
    }

    static boolean isOnlyMale(class_1297 entity) {
        if (entity instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)entity;
            return (holder.getGenderMask() & 3) == 1;
        }
        return false;
    }

    static boolean hasMaleGender(class_1297 entity) {
        GenderHolder holder;
        int mask;
        if (entity instanceof GenderHolder && (mask = (holder = (GenderHolder)entity).getGenderMask() & 3) != 0) {
            return (mask & 1) != 0;
        }
        return NonGenderSystem.hasMaleTag(entity);
    }

    static boolean hasFemaleGender(class_1297 entity) {
        GenderHolder holder;
        int mask;
        if (entity instanceof GenderHolder && (mask = (holder = (GenderHolder)entity).getGenderMask() & 3) != 0) {
            return (mask & 2) != 0;
        }
        return NonGenderSystem.hasFemaleTag(entity);
    }

    static NonLiquidTankType getLiquidTankType(class_1297 entity) {
        return NonGenderSystem.isOnlyMale(entity) ? NonLiquidTankType.A : NonLiquidTankType.V;
    }

    static void applyGenderMask(class_1297 entity, int mask) {
        NonGenderSystem.applyGenderMask(entity, mask, NonChangeSource.SYSTEM);
    }

    static void applyGenderMask(class_1297 entity, int mask, NonChangeSource source) {
        class_3222 player;
        int n;
        if (entity == null) {
            return;
        }
        if (entity instanceof GenderHolder) {
            GenderHolder existingHolder = (GenderHolder)entity;
            n = existingHolder.getGenderMask() & 3;
        } else {
            n = 0;
        }
        int beforeMask = n;
        int normalizedMask = mask & 3;
        if (normalizedMask == 0) {
            normalizedMask = 2;
        }
        entity.method_5738(GENDER_TAG_MALE);
        entity.method_5738(GENDER_TAG_FEMALE);
        if ((normalizedMask & 1) != 0) {
            entity.method_5780(GENDER_TAG_MALE);
        }
        if ((normalizedMask & 2) != 0) {
            entity.method_5780(GENDER_TAG_FEMALE);
        }
        if (entity instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)entity;
            holder.setGenderMask(normalizedMask);
        }
        if (normalizedMask == 1 && entity instanceof class_3222) {
            player = (class_3222)entity;
            if (NonMessSystem.normalizeMaleOnlyMess((class_1297)player, false)) {
                NonMessSystem.broadcastMessState(player);
            }
            NeedsOfNature.clearPregnancyState(player, true);
        }
        if (entity instanceof class_3222) {
            player = (class_3222)entity;
            if ((beforeMask & 3) != (normalizedMask & 3)) {
                NonApiInternals.fireGenderChanged(player, normalizedMask, source == null ? NonChangeSource.SYSTEM : source);
            }
        }
    }

    private static boolean canCowBeMilked(class_10730 cow) {
        if (cow == null) {
            return true;
        }
        if (cow instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)cow;
            return holder.hasGender(2);
        }
        return cow.method_5752().contains(GENDER_TAG_FEMALE);
    }

    private static int resolveGenderMaskFromTags(class_1297 entity) {
        if (entity == null) {
            return 0;
        }
        Set tags = entity.method_5752();
        int mask = 0;
        if (tags.contains(GENDER_TAG_MALE)) {
            mask |= 1;
        }
        if (tags.contains(GENDER_TAG_FEMALE)) {
            mask |= 2;
        }
        return mask;
    }
}

