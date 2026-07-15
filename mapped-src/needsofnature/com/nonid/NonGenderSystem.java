/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
 *  net.fabricmc.fabric.api.event.player.UseEntityCallback
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.entity.passive.CowEntity
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.text.Text
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.network.packet.CustomPayload
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
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.packet.CustomPayload;

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
            if (!(entity instanceof MobEntity)) {
                return;
            }
            if (entity.getType() == NeedsOfNature.HORSE_LIQUID_COLLECTOR_ENTITY_TYPE) {
                return;
            }
            if (entity.getType() == NeedsOfNature.PREGNANCY_EGG_ENTITY_TYPE) {
                return;
            }
            Set tags = entity.getCommandTags();
            boolean hasMale = tags.contains(GENDER_TAG_MALE);
            boolean hasFemale = tags.contains(GENDER_TAG_FEMALE);
            if (!hasMale && !hasFemale) {
                NonConfig.GenderSpawnChances chances = NonGenderSystem.resolveGenderSpawnChances(entity);
                int roll = world.getRandom().nextInt(100);
                if (roll < chances.male()) {
                    entity.addCommandTag(GENDER_TAG_MALE);
                    hasMale = true;
                } else if (roll < chances.male() + chances.female()) {
                    entity.addCommandTag(GENDER_TAG_FEMALE);
                    hasFemale = true;
                } else {
                    entity.addCommandTag(GENDER_TAG_MALE);
                    entity.addCommandTag(GENDER_TAG_FEMALE);
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
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = world;
                    holder.ensureEnergyInitialized(serverWorld);
                }
            }
        });
    }

    static void registerCowMilkingGenderGate() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            NonConfig config = NeedsOfNature.getConfig();
            if (config == null || !config.femaleCowOnlyMilking()) {
                return ActionResult.PASS;
            }
            if (!(entity instanceof CowEntity)) {
                return ActionResult.PASS;
            }
            CowEntity cow = (CowEntity)entity;
            if (cow.getType() != EntityType.COW) {
                return ActionResult.PASS;
            }
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isEmpty() || !stack.isOf(Items.BUCKET)) {
                return ActionResult.PASS;
            }
            return NonGenderSystem.canCowBeMilked(cow) ? ActionResult.PASS : ActionResult.FAIL;
        });
    }

    static void registerPlayerGenderRequests() {
        ServerPlayNetworking.registerGlobalReceiver(SetPlayerGenderC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            ServerPlayerEntity player = context.player();
            if (player == null) {
                return;
            }
            NonGenderSystem.handleClientGenderRequest(player, payload.mask(), payload.initialSelection());
        }));
    }

    static void ensurePlayerGenderInitialized(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        int mask = 0;
        if (player instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)player;
            mask = holder.getGenderMask() & 3;
        }
        if (mask == 0) {
            mask = NonGenderSystem.resolveGenderMaskFromTags((Entity)player);
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
        NonGenderSystem.applyGenderMask((Entity)player, mask);
        if (NeedsOfNature.getConfig().requirePlayerGenderSelectionOnJoin() && !NonGenderSystem.hasPlayerGenderSelected(player)) {
            NonGenderSystem.sendGenderSelectionPrompt(player);
        }
    }

    static int setPlayerGenderFromCommand(ServerPlayerEntity player, int mask) {
        return NonGenderSystem.setPlayerGenderFromCommand(player, mask, NonChangeSource.COMMAND);
    }

    static int setPlayerGenderFromCommand(ServerPlayerEntity player, int mask, NonChangeSource source) {
        int normalized = NonGenderSystem.normalizeGenderMask(mask);
        NonGenderSystem.applyPlayerGenderMask(player, normalized, true, source == null ? NonChangeSource.COMMAND : source);
        return normalized;
    }

    static void resetPlayerGenderSelection(ServerPlayerEntity player) {
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

    private static void handleClientGenderRequest(ServerPlayerEntity player, int requestedMask, boolean initialSelection) {
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

    private static void applyPlayerGenderMask(ServerPlayerEntity player, int mask, boolean markSelected) {
        NonGenderSystem.applyPlayerGenderMask(player, mask, markSelected, NonChangeSource.SYSTEM);
    }

    private static void applyPlayerGenderMask(ServerPlayerEntity player, int mask, boolean markSelected, NonChangeSource source) {
        NonGenderSystem.applyGenderMask((Entity)player, mask, source);
        if (markSelected) {
            NonGenderSystem.markPlayerGenderSelected(player);
        }
    }

    private static void markPlayerGenderSelected(ServerPlayerEntity player) {
        if (player != null) {
            player.addCommandTag(GENDER_TAG_SELECTED);
        }
    }

    private static void clearPlayerGenderSelectionState(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        player.removeCommandTag(GENDER_TAG_SELECTED);
        player.removeCommandTag(GENDER_TAG_MALE);
        player.removeCommandTag(GENDER_TAG_FEMALE);
        if (player instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)player;
            holder.setGenderMask(0);
        }
    }

    private static boolean hasPlayerGenderSelected(ServerPlayerEntity player) {
        return player != null && player.getCommandTags().contains(GENDER_TAG_SELECTED);
    }

    private static void sendGenderSelectionPrompt(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        ServerPlayNetworking.send((ServerPlayerEntity)player, (CustomPayload)new GenderSelectionPromptS2CPayload(config.getAllowedStartingGenderMask(), NonGenderSystem.currentOrDefaultPlayerMask(player), !config.allowPlayerGenderChangeAnytime()));
    }

    private static void sendCurrentGenderFeedback(ServerPlayerEntity player, String message) {
        if (player != null) {
            player.sendMessage((Text)Text.literal((String)("[NoN] " + message)), false);
        }
    }

    private static int currentOrDefaultPlayerMask(ServerPlayerEntity player) {
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

    static NonConfig.GenderSpawnChances resolveGenderSpawnChances(Entity entity) {
        return NonEntityProfiles.resolve(entity).genderSpawn();
    }

    static Map<String, NonConfig.GenderSpawnChances> resolveEffectiveGenderSpawnMap() {
        return NonEntityProfiles.resolveEffectiveGenderSpawnMap();
    }

    static boolean hasMaleTag(Entity entity) {
        return entity != null && entity.getCommandTags().contains(GENDER_TAG_MALE);
    }

    static boolean hasFemaleTag(Entity entity) {
        return entity != null && entity.getCommandTags().contains(GENDER_TAG_FEMALE);
    }

    static boolean isOnlyMale(Entity entity) {
        if (entity instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)entity;
            return (holder.getGenderMask() & 3) == 1;
        }
        return false;
    }

    static boolean hasMaleGender(Entity entity) {
        GenderHolder holder;
        int mask;
        if (entity instanceof GenderHolder && (mask = (holder = (GenderHolder)entity).getGenderMask() & 3) != 0) {
            return (mask & 1) != 0;
        }
        return NonGenderSystem.hasMaleTag(entity);
    }

    static boolean hasFemaleGender(Entity entity) {
        GenderHolder holder;
        int mask;
        if (entity instanceof GenderHolder && (mask = (holder = (GenderHolder)entity).getGenderMask() & 3) != 0) {
            return (mask & 2) != 0;
        }
        return NonGenderSystem.hasFemaleTag(entity);
    }

    static NonLiquidTankType getLiquidTankType(Entity entity) {
        return NonGenderSystem.isOnlyMale(entity) ? NonLiquidTankType.A : NonLiquidTankType.V;
    }

    static void applyGenderMask(Entity entity, int mask) {
        NonGenderSystem.applyGenderMask(entity, mask, NonChangeSource.SYSTEM);
    }

    static void applyGenderMask(Entity entity, int mask, NonChangeSource source) {
        ServerPlayerEntity player;
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
        entity.removeCommandTag(GENDER_TAG_MALE);
        entity.removeCommandTag(GENDER_TAG_FEMALE);
        if ((normalizedMask & 1) != 0) {
            entity.addCommandTag(GENDER_TAG_MALE);
        }
        if ((normalizedMask & 2) != 0) {
            entity.addCommandTag(GENDER_TAG_FEMALE);
        }
        if (entity instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)entity;
            holder.setGenderMask(normalizedMask);
        }
        if (normalizedMask == 1 && entity instanceof ServerPlayerEntity) {
            player = (ServerPlayerEntity)entity;
            if (NonMessSystem.normalizeMaleOnlyMess((Entity)player, false)) {
                NonMessSystem.broadcastMessState(player);
            }
            NeedsOfNature.clearPregnancyState(player, true);
        }
        if (entity instanceof ServerPlayerEntity) {
            player = (ServerPlayerEntity)entity;
            if ((beforeMask & 3) != (normalizedMask & 3)) {
                NonApiInternals.fireGenderChanged(player, normalizedMask, source == null ? NonChangeSource.SYSTEM : source);
            }
        }
    }

    private static boolean canCowBeMilked(CowEntity cow) {
        if (cow == null) {
            return true;
        }
        if (cow instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)cow;
            return holder.hasGender(2);
        }
        return cow.getCommandTags().contains(GENDER_TAG_FEMALE);
    }

    private static int resolveGenderMaskFromTags(Entity entity) {
        if (entity == null) {
            return 0;
        }
        Set tags = entity.getCommandTags();
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

