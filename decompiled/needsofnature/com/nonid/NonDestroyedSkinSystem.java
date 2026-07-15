/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1937
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_3414
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 *  net.minecraft.class_7923
 *  net.minecraft.class_8710
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
import com.nonid.network.RepairDestroyedSkinC2SPayload;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_7923;
import net.minecraft.class_8710;
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

    static void syncDestroyedSkins(class_3222 player) {
        UUID uuid;
        if (player == null) {
            return;
        }
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            MinecraftServer server = player.method_51469().method_8503();
            if (server != null) {
                for (class_3222 listedPlayer : server.method_3760().method_14571()) {
                    NonDestroyedSkinSystem.sendDestroyedSkinStage(player, listedPlayer.method_5667(), 0);
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
        for (Map.Entry<UUID, byte[]> entry : DESTROYED_SKIN_MASKS_BY_PLAYER.entrySet()) {
            uuid = entry.getKey();
            byte[][] masks = (byte[][])entry.getValue();
            if (uuid == null || masks == null) continue;
            for (int stage = 1; stage < Math.min(masks.length, 4); ++stage) {
                byte[] bytes = masks[stage];
                if (bytes == null || bytes.length == 0) continue;
                NonDestroyedSkinSystem.sendDestroyedSkinMask(player, uuid, stage, bytes);
            }
        }
        for (Map.Entry<UUID, Object> entry : DESTROYED_SKIN_STAGE_BY_PLAYER.entrySet()) {
            uuid = entry.getKey();
            if (uuid == null || DESTROYED_SKINS_BY_PLAYER.containsKey(uuid)) continue;
            NonDestroyedSkinSystem.sendDestroyedSkinStage(player, uuid, (Integer)entry.getValue());
        }
    }

    static void registerDestroyedSkinNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(DestroyedSkinUploadC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            class_3222 player = context.player();
            if (player == null) {
                return;
            }
            if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
                return;
            }
            byte[] bytes = payload.pngBytes();
            if (!NonDestroyedSkinSystem.isValidDestroyedSkinPayload(bytes)) {
                NeedsOfNature.LOGGER.warn("[NoN] Ignoring invalid destroyed skin upload from {} ({} bytes).", (Object)player.method_5477().getString(), (Object)(bytes == null ? 0 : bytes.length));
                return;
            }
            UUID playerUuid = player.method_5667();
            DESTROYED_SKINS_BY_PLAYER.put(playerUuid, (byte[])bytes.clone());
            NeedsOfNature.LOGGER.info("[NoN] Received destroyed skin upload from {} ({} bytes, stage {}).", new Object[]{player.method_5477().getString(), bytes.length, NonDestroyedSkinSystem.getDestroyedSkinStage(playerUuid)});
            NonDestroyedSkinSystem.broadcastDestroyedSkin(context.server(), playerUuid, NonDestroyedSkinSystem.getDestroyedSkinStage(playerUuid), bytes);
        }));
        ServerPlayNetworking.registerGlobalReceiver(DestroyedSkinMaskUploadC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            class_3222 player = context.player();
            if (player == null) {
                return;
            }
            if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
                return;
            }
            int stage = payload.stage();
            byte[] bytes = payload.pngBytes();
            if (stage <= 0 || stage >= 4 || !NonDestroyedSkinSystem.isValidDestroyedSkinPayload(bytes)) {
                NeedsOfNature.LOGGER.warn("[NoN] Ignoring invalid destroyed skin mask upload from {} (stage {}, {} bytes).", new Object[]{player.method_5477().getString(), stage, bytes == null ? 0 : bytes.length});
                return;
            }
            UUID playerUuid = player.method_5667();
            byte[][] masks = DESTROYED_SKIN_MASKS_BY_PLAYER.computeIfAbsent(playerUuid, ignored -> new byte[4][]);
            masks[stage] = (byte[])bytes.clone();
            NeedsOfNature.LOGGER.info("[NoN] Received destroyed skin mask upload from {} (stage {}, {} bytes).", new Object[]{player.method_5477().getString(), stage, bytes.length});
            NonDestroyedSkinSystem.broadcastDestroyedSkinMask(context.server(), playerUuid, stage, bytes);
        }));
    }

    static void registerDestroyedSkinRepairNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(RepairDestroyedSkinC2SPayload.ID, (payload, context) -> context.server().execute(() -> NonDestroyedSkinSystem.repairDestroyedSkin(context.player())));
        ServerPlayNetworking.registerGlobalReceiver(ManualDamageDestroyedSkinC2SPayload.ID, (payload, context) -> context.server().execute(() -> NonDestroyedSkinSystem.manuallyDamageDestroyedSkin(context.player())));
    }

    private static void repairDestroyedSkin(class_3222 player) {
        long blockedUntil;
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            return;
        }
        if (player == null || !player.method_5805() || !(player instanceof DestroyedSkinHolder)) {
            return;
        }
        DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
        class_3218 class_32182 = player.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            return;
        }
        class_3218 world = class_32182;
        if (!NonSkinRepairBlocks.isNearRepairBlock((class_1937)world, player.method_24515(), NeedsOfNature.getConfig())) {
            return;
        }
        UUID uuid = player.method_5667();
        long now = world.method_75260();
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

    private static void manuallyDamageDestroyedSkin(class_3222 player) {
        int afterDamage;
        int n;
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            return;
        }
        if (player == null || !player.method_5805()) {
            return;
        }
        class_3218 class_32182 = player.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            return;
        }
        class_3218 world = class_32182;
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
            v1 = NonDestroyedSkinSystem.clampDestroyedSkinDamage(holder.getDestroyedSkinDamage());
        } else {
            v1 = afterDamage = beforeDamage;
        }
        if (afterDamage > beforeDamage) {
            NonDestroyedSkinSystem.playDestroyedSkinShearSound(world, player);
        }
    }

    private static boolean hasShearsForManualDestroyedSkinDamage(class_3222 player) {
        if (player == null) {
            return false;
        }
        class_1799 cursorStack = player.field_7512 != null ? player.field_7512.method_34255() : class_1799.field_8037;
        return cursorStack.method_31574(class_1802.field_8868) || player.method_6047().method_31574(class_1802.field_8868) || player.method_6079().method_31574(class_1802.field_8868);
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

    static int getDestroyedSkinStageForPlayer(class_3222 player) {
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
        return NonDestroyedSkinSystem.getDestroyedSkinStage(player.method_5667());
    }

    static void broadcastDestroyedSkinStage(class_3222 player) {
        if (player == null) {
            return;
        }
        MinecraftServer server = player.method_51469().method_8503();
        if (server == null) {
            return;
        }
        NonDestroyedSkinSystem.broadcastDestroyedSkinStage(server, player.method_5667(), NonDestroyedSkinSystem.getDestroyedSkinStageForPlayer(player));
    }

    static void cacheDestroyedSkinStage(class_3222 player) {
        int stage;
        if (player == null) {
            return;
        }
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            DESTROYED_SKIN_STAGE_BY_PLAYER.remove(player.method_5667());
            return;
        }
        if (!(player instanceof DestroyedSkinHolder)) {
            return;
        }
        DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
        int damage = NonDestroyedSkinSystem.clampDestroyedSkinDamage(holder.getDestroyedSkinDamage());
        int n = stage = damage > 0 ? NonDestroyedSkinSystem.destroyedSkinStageForDamage(damage) : Math.max(0, Math.min(4, holder.getDestroyedSkinStage()));
        if (stage == 0) {
            DESTROYED_SKIN_STAGE_BY_PLAYER.remove(player.method_5667());
        } else {
            DESTROYED_SKIN_STAGE_BY_PLAYER.put(player.method_5667(), stage);
        }
    }

    static void setDestroyedSkinStage(class_3222 player, int stage) {
        if (player == null) {
            return;
        }
        UUID uuid = player.method_5667();
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            NonDestroyedSkinSystem.clearDestroyedSkinStoredState(player);
            class_3218 class_32182 = player.method_51469();
            if (class_32182 instanceof class_3218) {
                class_3218 world = class_32182;
                NonDestroyedSkinSystem.broadcastDestroyedSkinStage(world.method_8503(), uuid, 0);
            }
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
        class_3218 class_32183 = player.method_51469();
        if (class_32183 instanceof class_3218) {
            class_3218 world = class_32183;
            NonDestroyedSkinSystem.broadcastDestroyedSkinStage(world.method_8503(), uuid, clamped);
        }
        NonApiInternals.fireDestroyedSkinChanged(player);
        NeedsOfNature.checkBadIdeaBeaconAdvancement(player);
    }

    static void applyDestroyedSkinAttackDamage(class_3218 world, class_3222 player) {
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

    static void applyDestroyedSkinVoluntaryJoinInitialDamage(class_3218 world, class_3222 player) {
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

    private static void applyDestroyedSkinDamage(class_3218 world, class_3222 player, DestroyedSkinHolder holder, int damageGain) {
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
            NeedsOfNature.LOGGER.info("[NoN] {} destroyed skin advanced to stage {} ({}/{}).", new Object[]{player.method_5477().getString(), afterStage, afterDamage, 10});
        }
    }

    private static int rollDestroyedSkinDamage(class_3218 world, class_3222 player) {
        double multiplier = NonTrinketsIntegration.getAccessoryEffects((class_1309)player).destroyedSkinDamageMultiplier();
        if (!Double.isFinite(multiplier) || multiplier <= 0.0) {
            return 0;
        }
        int whole = (int)Math.floor(multiplier);
        double fractional = multiplier - (double)whole;
        if (fractional > 0.0 && world.method_8409().method_43058() < fractional) {
            ++whole;
        }
        return Math.max(0, whole);
    }

    static void resetDestroyedSkinDamage(class_3222 player) {
        if (player == null) {
            return;
        }
        if (player instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
            holder.setDestroyedSkinDamage(0);
        }
        NonDestroyedSkinSystem.setDestroyedSkinStage(player, 0);
    }

    static void clearDestroyedSkinStoredState(class_3222 player) {
        if (player == null) {
            return;
        }
        UUID uuid = player.method_5667();
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

    private static void playDestroyedSkinRipSound(class_3218 world, class_3222 player) {
        if (world == null || player == null) {
            return;
        }
        int index = 1 + world.method_8409().method_43048(3);
        String soundPath = "rip" + String.format(Locale.ROOT, "%02d", index);
        float pitch = 0.94f + world.method_8409().method_43057() * 0.12f;
        world.method_43128(null, player.method_23317(), player.method_23318() + (double)player.method_5751() * 0.5, player.method_23321(), (class_3414)class_7923.field_41172.method_63535(NeedsOfNature.id(soundPath)), class_3419.field_15248, 0.75f, pitch);
    }

    private static void playDestroyedSkinShearSound(class_3218 world, class_3222 player) {
        if (world == null || player == null) {
            return;
        }
        world.method_43128(null, player.method_23317(), player.method_23318() + (double)player.method_5751() * 0.5, player.method_23321(), class_3417.field_14975, class_3419.field_15248, 0.8f, 0.95f + world.method_8409().method_43057() * 0.1f);
    }

    private static void playDestroyedSkinRepairSound(class_3218 world, class_3222 player) {
        if (world == null || player == null) {
            return;
        }
        world.method_43128(null, player.method_23317(), player.method_23318() + (double)player.method_5751() * 0.5, player.method_23321(), class_3417.field_15226, class_3419.field_15248, 1.35f, 1.15f + world.method_8409().method_43057() * 0.15f);
    }

    private static void broadcastDestroyedSkinRipParticles(class_3218 world, class_3222 player) {
        if (world == null || player == null) {
            return;
        }
        int count = 3 + world.method_8409().method_43048(4);
        long seed = world.method_8409().method_43055();
        double x = player.method_23317();
        double y = player.method_23318() + (double)player.method_17682() * 0.55;
        double z = player.method_23321();
        DestroyedSkinParticlesS2CPayload payload = new DestroyedSkinParticlesS2CPayload(player.method_5667(), x, y, z, count, seed);
        for (class_3222 target : world.method_18456()) {
            if (target.method_5858((class_1297)player) > 9216.0 || !ServerPlayNetworking.canSend((class_3222)target, DestroyedSkinParticlesS2CPayload.ID)) continue;
            ServerPlayNetworking.send((class_3222)target, (class_8710)payload);
        }
    }

    private static void broadcastDestroyedSkin(MinecraftServer server, UUID playerUuid, int stage, byte[] bytes) {
        if (server == null || playerUuid == null || bytes == null || bytes.length == 0) {
            return;
        }
        for (class_3222 target : server.method_3760().method_14571()) {
            NonDestroyedSkinSystem.sendDestroyedSkin(target, playerUuid, stage, bytes);
        }
    }

    private static void broadcastDestroyedSkinMask(MinecraftServer server, UUID playerUuid, int stage, byte[] bytes) {
        if (server == null || playerUuid == null || bytes == null || bytes.length == 0) {
            return;
        }
        for (class_3222 target : server.method_3760().method_14571()) {
            NonDestroyedSkinSystem.sendDestroyedSkinMask(target, playerUuid, stage, bytes);
        }
    }

    private static void broadcastDestroyedSkinStage(MinecraftServer server, UUID playerUuid, int stage) {
        if (server == null || playerUuid == null) {
            return;
        }
        for (class_3222 target : server.method_3760().method_14571()) {
            NonDestroyedSkinSystem.sendDestroyedSkinStage(target, playerUuid, stage);
        }
    }

    private static void sendDestroyedSkin(class_3222 target, UUID playerUuid, int stage, byte[] bytes) {
        if (target == null || playerUuid == null || bytes == null || bytes.length == 0) {
            return;
        }
        if (!ServerPlayNetworking.canSend((class_3222)target, DestroyedSkinSyncS2CPayload.ID)) {
            return;
        }
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            stage = 0;
        }
        ServerPlayNetworking.send((class_3222)target, (class_8710)new DestroyedSkinSyncS2CPayload(playerUuid, stage, bytes));
    }

    private static void sendDestroyedSkinMask(class_3222 target, UUID playerUuid, int stage, byte[] bytes) {
        if (target == null || playerUuid == null || bytes == null || bytes.length == 0) {
            return;
        }
        if (stage <= 0 || stage >= 4) {
            return;
        }
        if (!ServerPlayNetworking.canSend((class_3222)target, DestroyedSkinMaskSyncS2CPayload.ID)) {
            return;
        }
        ServerPlayNetworking.send((class_3222)target, (class_8710)new DestroyedSkinMaskSyncS2CPayload(playerUuid, stage, bytes));
    }

    private static void sendDestroyedSkinStage(class_3222 target, UUID playerUuid, int stage) {
        if (target == null || playerUuid == null) {
            return;
        }
        if (!ServerPlayNetworking.canSend((class_3222)target, DestroyedSkinStateS2CPayload.ID)) {
            return;
        }
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            stage = 0;
        }
        ServerPlayNetworking.send((class_3222)target, (class_8710)new DestroyedSkinStateS2CPayload(playerUuid, stage));
    }
}

