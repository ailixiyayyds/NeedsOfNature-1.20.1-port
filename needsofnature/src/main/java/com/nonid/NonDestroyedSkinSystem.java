/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.world.World
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.sound.SoundEvent
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.registry.Registries
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.server.MinecraftServer
 */
package com.nonid;

import com.nonid.DestroyedSkinHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonApiInternals;
import com.nonid.NonSkinRepairBlocks;
import com.nonid.NonTrinketsIntegration;
import com.nonid.network.DestroyedSkinMaskSyncS2CPayload;
import com.nonid.network.DestroyedSkinMaskUploadC2SPayload;
import com.nonid.network.DestroyedSkinParticlesS2CPayload;
import com.nonid.network.DestroyedSkinStateS2CPayload;
import com.nonid.network.DestroyedSkinSyncS2CPayload;
import com.nonid.network.DestroyedSkinUploadC2SPayload;
import com.nonid.network.ManualDamageDestroyedSkinC2SPayload;
import com.nonid.network.NonServerNetworking;
import com.nonid.network.RepairDestroyedSkinC2SPayload;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;

final class NonDestroyedSkinSystem {
    private static final int DESTROYED_SKIN_MAX_DAMAGE = 10;
    private static final int DESTROYED_SKIN_STAGE_1_DAMAGE = 1;
    private static final int DESTROYED_SKIN_STAGE_2_DAMAGE = 4;
    private static final int DESTROYED_SKIN_STAGE_3_DAMAGE = 7;
    private static final int DESTROYED_SKIN_STAGE_4_DAMAGE = 10;
    private static final int DESTROYED_SKIN_REPAIR_COOLDOWN_TICKS = 7;
    private static final Map<UUID, byte[]> DESTROYED_SKINS_BY_PLAYER = new ConcurrentHashMap<UUID, byte[]>();
    private static final Map<UUID, byte[][]> DESTROYED_SKIN_MASKS_BY_PLAYER = new ConcurrentHashMap<UUID, byte[][]>();
    private static final Map<UUID, Integer> DESTROYED_SKIN_STAGE_BY_PLAYER = new ConcurrentHashMap<UUID, Integer>();
    private static final Map<UUID, Long> DESTROYED_SKIN_REPAIR_COOLDOWNS = new ConcurrentHashMap<UUID, Long>();

    private NonDestroyedSkinSystem() {
    }

    static void syncDestroyedSkins(ServerPlayerEntity player) {
        UUID uuid;
        if (player == null) {
            return;
        }
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            MinecraftServer server = player.getEntityWorld().getServer();
            if (server != null) {
                for (ServerPlayerEntity listedPlayer : server.getPlayerManager().getPlayerList()) {
                    NonDestroyedSkinSystem.sendDestroyedSkinStage(player, listedPlayer.getUuid(), 0);
                }
            }
            for (UUID uuid2 : DESTROYED_SKINS_BY_PLAYER.keySet()) {
                if (uuid2 == null) continue;
                NonDestroyedSkinSystem.sendDestroyedSkinStage(player, uuid2, 0);
            }
            for (UUID uuid2 : DESTROYED_SKIN_STAGE_BY_PLAYER.keySet()) {
                if (uuid2 == null) continue;
                NonDestroyedSkinSystem.sendDestroyedSkinStage(player, uuid2, 0);
            }
            return;
        }
        for (Map.Entry<UUID, byte[]> entry : DESTROYED_SKINS_BY_PLAYER.entrySet()) {
            uuid = entry.getKey();
            byte[] bytes = entry.getValue();
            if (uuid == null || bytes == null || bytes.length == 0) continue;
            NonDestroyedSkinSystem.sendDestroyedSkin(player, uuid, NonDestroyedSkinSystem.getDestroyedSkinStage(uuid), bytes);
        }
        for (Map.Entry<UUID, byte[][]> entry : DESTROYED_SKIN_MASKS_BY_PLAYER.entrySet()) {
            uuid = entry.getKey();
            byte[][] masks = entry.getValue();
            if (uuid == null || masks == null) continue;
            for (int stage = 1; stage < Math.min(masks.length, 4); ++stage) {
                byte[] bytes = masks[stage];
                if (bytes == null || bytes.length == 0) continue;
                NonDestroyedSkinSystem.sendDestroyedSkinMask(player, uuid, stage, bytes);
            }
        }
        for (Map.Entry<UUID, Integer> entry : DESTROYED_SKIN_STAGE_BY_PLAYER.entrySet()) {
            uuid = entry.getKey();
            if (uuid == null || DESTROYED_SKINS_BY_PLAYER.containsKey(uuid)) continue;
            NonDestroyedSkinSystem.sendDestroyedSkinStage(player, uuid, (Integer)entry.getValue());
        }
    }

    static void registerDestroyedSkinNetworking() {
        NonServerNetworking.register(DestroyedSkinUploadC2SPayload.ID, DestroyedSkinUploadC2SPayload::read, (server, player, payload) -> {
            if (player == null) {
                return;
            }
            if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
                return;
            }
            byte[] bytes = payload.pngBytes();
            if (!NonDestroyedSkinSystem.isValidDestroyedSkinPayload(bytes)) {
                NeedsOfNature.LOGGER.warn("[NoN] Ignoring invalid destroyed skin upload from {} ({} bytes).", (Object)player.getName().getString(), (Object)(bytes == null ? 0 : bytes.length));
                return;
            }
            UUID playerUuid = player.getUuid();
            DESTROYED_SKINS_BY_PLAYER.put(playerUuid, (byte[])bytes.clone());
            NeedsOfNature.LOGGER.info("[NoN] Received destroyed skin upload from {} ({} bytes, stage {}).", new Object[]{player.getName().getString(), bytes.length, NonDestroyedSkinSystem.getDestroyedSkinStage(playerUuid)});
            NonDestroyedSkinSystem.broadcastDestroyedSkin(server, playerUuid, NonDestroyedSkinSystem.getDestroyedSkinStage(playerUuid), bytes);
        });
        NonServerNetworking.register(DestroyedSkinMaskUploadC2SPayload.ID, DestroyedSkinMaskUploadC2SPayload::read, (server, player, payload) -> {
            if (player == null) {
                return;
            }
            if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
                return;
            }
            int stage = payload.stage();
            byte[] bytes = payload.pngBytes();
            if (stage <= 0 || stage >= 4 || !NonDestroyedSkinSystem.isValidDestroyedSkinPayload(bytes)) {
                NeedsOfNature.LOGGER.warn("[NoN] Ignoring invalid destroyed skin mask upload from {} (stage {}, {} bytes).", new Object[]{player.getName().getString(), stage, bytes == null ? 0 : bytes.length});
                return;
            }
            UUID playerUuid = player.getUuid();
            byte[][] masks = DESTROYED_SKIN_MASKS_BY_PLAYER.computeIfAbsent(playerUuid, ignored -> new byte[4][]);
            masks[stage] = (byte[])bytes.clone();
            NeedsOfNature.LOGGER.info("[NoN] Received destroyed skin mask upload from {} (stage {}, {} bytes).", new Object[]{player.getName().getString(), stage, bytes.length});
            NonDestroyedSkinSystem.broadcastDestroyedSkinMask(server, playerUuid, stage, bytes);
        });
    }

    static void registerDestroyedSkinRepairNetworking() {
        NonServerNetworking.register(RepairDestroyedSkinC2SPayload.ID, RepairDestroyedSkinC2SPayload::read, (server, player, payload) -> NonDestroyedSkinSystem.repairDestroyedSkin(player));
        NonServerNetworking.register(ManualDamageDestroyedSkinC2SPayload.ID, ManualDamageDestroyedSkinC2SPayload::read, (server, player, payload) -> NonDestroyedSkinSystem.manuallyDamageDestroyedSkin(player));
    }

    private static void repairDestroyedSkin(ServerPlayerEntity player) {
        long blockedUntil;
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            return;
        }
        if (player == null || !player.isAlive() || !(player instanceof DestroyedSkinHolder)) {
            return;
        }
        DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
        ServerWorld world = player.getServerWorld();
        if (!NonSkinRepairBlocks.isNearRepairBlock((World)world, player.getBlockPos(), NeedsOfNature.getConfig())) {
            return;
        }
        UUID uuid = player.getUuid();
        long now = world.getTime();
        if (now < (blockedUntil = DESTROYED_SKIN_REPAIR_COOLDOWNS.getOrDefault(uuid, 0L).longValue())) {
            return;
        }
        int beforeDamage = NonDestroyedSkinSystem.clampDestroyedSkinDamage(holder.getDestroyedSkinDamage());
        if (beforeDamage <= 0) {
            DESTROYED_SKIN_REPAIR_COOLDOWNS.remove(uuid);
            return;
        }
        int afterDamage = NonDestroyedSkinSystem.clampDestroyedSkinDamage(beforeDamage - 1);
        holder.setDestroyedSkinDamage(afterDamage);
        NonDestroyedSkinSystem.setDestroyedSkinStage(player, NonDestroyedSkinSystem.destroyedSkinStageForDamage(afterDamage));
        NonDestroyedSkinSystem.playDestroyedSkinRepairSound(world, player);
        if (afterDamage > 0) {
            DESTROYED_SKIN_REPAIR_COOLDOWNS.put(uuid, now + 7L);
        } else {
            DESTROYED_SKIN_REPAIR_COOLDOWNS.remove(uuid);
        }
    }

    private static void manuallyDamageDestroyedSkin(ServerPlayerEntity player) {
        int afterDamage;
        int n;
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            return;
        }
        if (player == null || !player.isAlive()) {
            return;
        }
        ServerWorld world = player.getServerWorld();
        if (!NonDestroyedSkinSystem.hasShearsForManualDestroyedSkinDamage(player)) {
            return;
        }
        if (player instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
            n = NonDestroyedSkinSystem.clampDestroyedSkinDamage(holder.getDestroyedSkinDamage());
        } else {
            n = 10;
        }
        int beforeDamage = n;
        NonDestroyedSkinSystem.applyDestroyedSkinAttackDamage(world, player);
        if (player instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
            afterDamage = NonDestroyedSkinSystem.clampDestroyedSkinDamage(holder.getDestroyedSkinDamage());
        } else {
            afterDamage = beforeDamage;
        }
        if (afterDamage > beforeDamage) {
            NonDestroyedSkinSystem.playDestroyedSkinShearSound(world, player);
        }
    }

    private static boolean hasShearsForManualDestroyedSkinDamage(ServerPlayerEntity player) {
        if (player == null) {
            return false;
        }
        ItemStack cursorStack = player.currentScreenHandler != null ? player.currentScreenHandler.getCursorStack() : ItemStack.EMPTY;
        return cursorStack.isOf(Items.SHEARS) || player.getMainHandStack().isOf(Items.SHEARS) || player.getOffHandStack().isOf(Items.SHEARS);
    }

    private static boolean isValidDestroyedSkinPayload(byte[] bytes) {
        if (bytes == null || bytes.length <= 0 || bytes.length > 65536) {
            return false;
        }
        return bytes.length >= 8 && (bytes[0] & 0xFF) == 137 && bytes[1] == 80 && bytes[2] == 78 && bytes[3] == 71 && bytes[4] == 13 && bytes[5] == 10 && bytes[6] == 26 && bytes[7] == 10;
    }

    private static int getDestroyedSkinStage(UUID playerUuid) {
        if (playerUuid == null) {
            return 0;
        }
        return Math.max(0, Math.min(4, DESTROYED_SKIN_STAGE_BY_PLAYER.getOrDefault(playerUuid, 0)));
    }

    static int getDestroyedSkinStageForPlayer(ServerPlayerEntity player) {
        if (player == null) {
            return 0;
        }
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            return 0;
        }
        if (player instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
            return Math.max(0, Math.min(4, holder.getDestroyedSkinStage()));
        }
        return NonDestroyedSkinSystem.getDestroyedSkinStage(player.getUuid());
    }

    static void broadcastDestroyedSkinStage(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) {
            return;
        }
        NonDestroyedSkinSystem.broadcastDestroyedSkinStage(server, player.getUuid(), NonDestroyedSkinSystem.getDestroyedSkinStageForPlayer(player));
    }

    static void cacheDestroyedSkinStage(ServerPlayerEntity player) {
        int stage;
        if (player == null) {
            return;
        }
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            DESTROYED_SKIN_STAGE_BY_PLAYER.remove(player.getUuid());
            return;
        }
        if (!(player instanceof DestroyedSkinHolder)) {
            return;
        }
        DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
        int damage = NonDestroyedSkinSystem.clampDestroyedSkinDamage(holder.getDestroyedSkinDamage());
        int n = stage = damage > 0 ? NonDestroyedSkinSystem.destroyedSkinStageForDamage(damage) : Math.max(0, Math.min(4, holder.getDestroyedSkinStage()));
        if (stage == 0) {
            DESTROYED_SKIN_STAGE_BY_PLAYER.remove(player.getUuid());
        } else {
            DESTROYED_SKIN_STAGE_BY_PLAYER.put(player.getUuid(), stage);
        }
    }

    static void setDestroyedSkinStage(ServerPlayerEntity player, int stage) {
        if (player == null) {
            return;
        }
        UUID uuid = player.getUuid();
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            NonDestroyedSkinSystem.clearDestroyedSkinStoredState(player);
            ServerWorld world = player.getServerWorld();
            NonDestroyedSkinSystem.broadcastDestroyedSkinStage(world.getServer(), uuid, 0);
            return;
        }
        int clamped = Math.max(0, Math.min(4, stage));
        if (player instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
            holder.setDestroyedSkinStage(clamped);
        }
        if (clamped == 0) {
            DESTROYED_SKIN_STAGE_BY_PLAYER.remove(uuid);
        } else {
            DESTROYED_SKIN_STAGE_BY_PLAYER.put(uuid, clamped);
        }
        ServerWorld world = player.getServerWorld();
        NonDestroyedSkinSystem.broadcastDestroyedSkinStage(world.getServer(), uuid, clamped);
        NonApiInternals.fireDestroyedSkinChanged(player);
        NeedsOfNature.checkBadIdeaBeaconAdvancement(player);
    }

    static void applyDestroyedSkinAttackDamage(ServerWorld world, ServerPlayerEntity player) {
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            return;
        }
        if (world == null || player == null || !(player instanceof DestroyedSkinHolder)) {
            return;
        }
        DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
        int damageGain = NonDestroyedSkinSystem.rollDestroyedSkinDamage(world, player);
        NonDestroyedSkinSystem.applyDestroyedSkinDamage(world, player, holder, damageGain);
    }

    static void applyDestroyedSkinVoluntaryJoinInitialDamage(ServerWorld world, ServerPlayerEntity player) {
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            return;
        }
        if (world == null || player == null || !(player instanceof DestroyedSkinHolder)) {
            return;
        }
        DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
        int beforeDamage = NonDestroyedSkinSystem.clampDestroyedSkinDamage(holder.getDestroyedSkinDamage());
        if (beforeDamage != 0) {
            return;
        }
        NonDestroyedSkinSystem.applyDestroyedSkinDamage(world, player, holder, 1);
    }

    private static void applyDestroyedSkinDamage(ServerWorld world, ServerPlayerEntity player, DestroyedSkinHolder holder, int damageGain) {
        if (world == null || player == null || holder == null || damageGain <= 0) {
            return;
        }
        int beforeDamage = NonDestroyedSkinSystem.clampDestroyedSkinDamage(holder.getDestroyedSkinDamage());
        int afterDamage = NonDestroyedSkinSystem.clampDestroyedSkinDamage(beforeDamage + damageGain);
        if (afterDamage == beforeDamage) {
            return;
        }
        holder.setDestroyedSkinDamage(afterDamage);
        int beforeStage = NonDestroyedSkinSystem.destroyedSkinStageForDamage(beforeDamage);
        int afterStage = NonDestroyedSkinSystem.destroyedSkinStageForDamage(afterDamage);
        NonDestroyedSkinSystem.setDestroyedSkinStage(player, afterStage);
        NonDestroyedSkinSystem.playDestroyedSkinRipSound(world, player);
        NonDestroyedSkinSystem.broadcastDestroyedSkinRipParticles(world, player);
        if (afterStage != beforeStage) {
            NeedsOfNature.LOGGER.info("[NoN] {} destroyed skin advanced to stage {} ({}/{}).", new Object[]{player.getName().getString(), afterStage, afterDamage, 10});
        }
    }

    private static int rollDestroyedSkinDamage(ServerWorld world, ServerPlayerEntity player) {
        double multiplier = NonTrinketsIntegration.getAccessoryEffects((LivingEntity)player).destroyedSkinDamageMultiplier();
        if (!Double.isFinite(multiplier) || multiplier <= 0.0) {
            return 0;
        }
        int whole = (int)Math.floor(multiplier);
        double fractional = multiplier - (double)whole;
        if (fractional > 0.0 && world.getRandom().nextDouble() < fractional) {
            ++whole;
        }
        return Math.max(0, whole);
    }

    static void resetDestroyedSkinDamage(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        if (player instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
            holder.setDestroyedSkinDamage(0);
        }
        NonDestroyedSkinSystem.setDestroyedSkinStage(player, 0);
    }

    static void clearDestroyedSkinStoredState(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        UUID uuid = player.getUuid();
        if (player instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
            holder.setDestroyedSkinDamage(0);
            holder.setDestroyedSkinStage(0);
        }
        DESTROYED_SKIN_STAGE_BY_PLAYER.remove(uuid);
        DESTROYED_SKIN_REPAIR_COOLDOWNS.remove(uuid);
    }

    private static int clampDestroyedSkinDamage(int damage) {
        return Math.max(0, Math.min(10, damage));
    }

    private static int destroyedSkinStageForDamage(int damage) {
        int clamped = NonDestroyedSkinSystem.clampDestroyedSkinDamage(damage);
        if (clamped >= 10) {
            return 4;
        }
        if (clamped >= 7) {
            return 3;
        }
        if (clamped >= 4) {
            return 2;
        }
        if (clamped >= 1) {
            return 1;
        }
        return 0;
    }

    static int destroyedSkinDamageForStage(int stage) {
        return switch (Math.max(0, Math.min(4, stage))) {
            case 1 -> 1;
            case 2 -> 4;
            case 3 -> 7;
            case 4 -> 10;
            default -> 0;
        };
    }

    private static void playDestroyedSkinRipSound(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        int index = 1 + world.getRandom().nextInt(3);
        String soundPath = "rip" + String.format(Locale.ROOT, "%02d", index);
        float pitch = 0.94f + world.getRandom().nextFloat() * 0.12f;
        world.playSound(null, player.getX(), player.getY() + (double)player.getStandingEyeHeight() * 0.5, player.getZ(), (SoundEvent)Registries.SOUND_EVENT.get(NeedsOfNature.id(soundPath)), SoundCategory.PLAYERS, 0.75f, pitch);
    }

    private static void playDestroyedSkinShearSound(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        world.playSound(null, player.getX(), player.getY() + (double)player.getStandingEyeHeight() * 0.5, player.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 0.8f, 0.95f + world.getRandom().nextFloat() * 0.1f);
    }

    private static void playDestroyedSkinRepairSound(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        world.playSound(null, player.getX(), player.getY() + (double)player.getStandingEyeHeight() * 0.5, player.getZ(), SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.PLAYERS, 1.35f, 1.15f + world.getRandom().nextFloat() * 0.15f);
    }

    private static void broadcastDestroyedSkinRipParticles(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        int count = 3 + world.getRandom().nextInt(4);
        long seed = world.getRandom().nextLong();
        double x = player.getX();
        double y = player.getY() + (double)player.getHeight() * 0.55;
        double z = player.getZ();
        DestroyedSkinParticlesS2CPayload payload = new DestroyedSkinParticlesS2CPayload(player.getUuid(), x, y, z, count, seed);
        for (ServerPlayerEntity target : world.getPlayers()) {
            if (target.squaredDistanceTo((Entity)player) > 9216.0 || !ServerPlayNetworking.canSend((ServerPlayerEntity)target, DestroyedSkinParticlesS2CPayload.ID)) continue;
            NonServerNetworking.send(target, payload);
        }
    }

    private static void broadcastDestroyedSkin(MinecraftServer server, UUID playerUuid, int stage, byte[] bytes) {
        if (server == null || playerUuid == null || bytes == null || bytes.length == 0) {
            return;
        }
        for (ServerPlayerEntity target : server.getPlayerManager().getPlayerList()) {
            NonDestroyedSkinSystem.sendDestroyedSkin(target, playerUuid, stage, bytes);
        }
    }

    private static void broadcastDestroyedSkinMask(MinecraftServer server, UUID playerUuid, int stage, byte[] bytes) {
        if (server == null || playerUuid == null || bytes == null || bytes.length == 0) {
            return;
        }
        for (ServerPlayerEntity target : server.getPlayerManager().getPlayerList()) {
            NonDestroyedSkinSystem.sendDestroyedSkinMask(target, playerUuid, stage, bytes);
        }
    }

    private static void broadcastDestroyedSkinStage(MinecraftServer server, UUID playerUuid, int stage) {
        if (server == null || playerUuid == null) {
            return;
        }
        for (ServerPlayerEntity target : server.getPlayerManager().getPlayerList()) {
            NonDestroyedSkinSystem.sendDestroyedSkinStage(target, playerUuid, stage);
        }
    }

    private static void sendDestroyedSkin(ServerPlayerEntity target, UUID playerUuid, int stage, byte[] bytes) {
        if (target == null || playerUuid == null || bytes == null || bytes.length == 0) {
            return;
        }
        if (!ServerPlayNetworking.canSend((ServerPlayerEntity)target, DestroyedSkinSyncS2CPayload.ID)) {
            return;
        }
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            stage = 0;
        }
        NonServerNetworking.send(target, new DestroyedSkinSyncS2CPayload(playerUuid, stage, bytes));
    }

    private static void sendDestroyedSkinMask(ServerPlayerEntity target, UUID playerUuid, int stage, byte[] bytes) {
        if (target == null || playerUuid == null || bytes == null || bytes.length == 0) {
            return;
        }
        if (stage <= 0 || stage >= 4) {
            return;
        }
        if (!ServerPlayNetworking.canSend((ServerPlayerEntity)target, DestroyedSkinMaskSyncS2CPayload.ID)) {
            return;
        }
        NonServerNetworking.send(target, new DestroyedSkinMaskSyncS2CPayload(playerUuid, stage, bytes));
    }

    private static void sendDestroyedSkinStage(ServerPlayerEntity target, UUID playerUuid, int stage) {
        if (target == null || playerUuid == null) {
            return;
        }
        if (!ServerPlayNetworking.canSend((ServerPlayerEntity)target, DestroyedSkinStateS2CPayload.ID)) {
            return;
        }
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            stage = 0;
        }
        NonServerNetworking.send(target, new DestroyedSkinStateS2CPayload(playerUuid, stage));
    }
}

