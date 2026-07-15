/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_2394
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_8710
 *  net.minecraft.server.MinecraftServer
 */
package com.nonid;

import com.nonid.LiquidHolder;
import com.nonid.MessHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonAdvancementHooks;
import com.nonid.NonApiInternals;
import com.nonid.NonGenderSystem;
import com.nonid.NonLiquidSystem;
import com.nonid.NonTrinketsIntegration;
import com.nonid.data.NonLiquidRoles;
import com.nonid.network.MessStateS2CPayload;
import com.nonid.particle.NonParticles;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2394;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_8710;
import net.minecraft.server.MinecraftServer;

final class NonMessSystem {
    private static final Map<UUID, Long> MESS_CLEAN_NEXT_TICKS = new ConcurrentHashMap<UUID, Long>();

    private NonMessSystem() {
    }

    static void incrementMessOnPeak(class_3218 world, List<UUID> actorUuids, class_2960 animationId) {
        if (!NeedsOfNature.isMessSystemEnabled()) {
            return;
        }
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null || roles.injectorRoles().isEmpty()) {
            return;
        }
        boolean hasV = roles.injectorRoles().containsValue((Object)NonLiquidRoles.InjectorRole.V);
        boolean hasA = roles.injectorRoles().containsValue((Object)NonLiquidRoles.InjectorRole.A);
        boolean hasM = roles.injectorRoles().containsValue((Object)NonLiquidRoles.InjectorRole.M);
        if (!(hasV || hasA || hasM)) {
            return;
        }
        for (class_3222 player : NeedsOfNature.collectPlayerActors(world, actorUuids)) {
            if (!(player instanceof MessHolder)) continue;
            MessHolder holder = (MessHolder)player;
            NonMessSystem.normalizeMaleOnlyMess((class_1297)player, false);
            int messGain = NonMessSystem.rollMessGain(world, player);
            if (messGain <= 0) continue;
            if (hasV) {
                if (NonGenderSystem.isOnlyMale((class_1297)player)) {
                    if (NeedsOfNature.getConfig().convertMaleOnlyVInjectionsToA()) {
                        holder.setAMess(NonMessSystem.clampMess(holder.getAMess() + messGain));
                    }
                } else {
                    holder.setVMess(NonMessSystem.clampMess(holder.getVMess() + messGain));
                }
            }
            if (hasA) {
                holder.setAMess(NonMessSystem.clampMess(holder.getAMess() + messGain));
            }
            if (hasM) {
                holder.setMMess(NonMessSystem.clampMess(holder.getMMess() + messGain));
            }
            NonMessSystem.broadcastMessState(player);
            if (holder.getVMess() < 10 || holder.getAMess() < 10 || holder.getMMess() < 10) continue;
            NonAdvancementHooks.grantMessedUp(player);
        }
    }

    private static int rollMessGain(class_3218 world, class_3222 player) {
        double multiplier = NonTrinketsIntegration.getActiveTankAccessoryEffects((class_1309)player).messGainMultiplier();
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

    static void clearMess(class_3222 player) {
        if (player == null || !(player instanceof MessHolder)) {
            return;
        }
        MessHolder holder = (MessHolder)player;
        holder.setVMess(0);
        holder.setAMess(0);
        holder.setMMess(0);
        MESS_CLEAN_NEXT_TICKS.remove(player.method_5667());
        NonMessSystem.broadcastMessState(player);
    }

    static void clearMessStoredState(class_3222 player) {
        if (player == null || !(player instanceof MessHolder)) {
            return;
        }
        MessHolder holder = (MessHolder)player;
        holder.setVMess(0);
        holder.setAMess(0);
        holder.setMMess(0);
        MESS_CLEAN_NEXT_TICKS.remove(player.method_5667());
    }

    static int clampMess(int value) {
        return Math.max(0, Math.min(10, value));
    }

    static void tickMessCleaning(class_3218 world, long now) {
        if (!NeedsOfNature.isMessSystemEnabled()) {
            return;
        }
        if (world == null) {
            return;
        }
        for (class_3222 player : world.method_18456()) {
            boolean inRain;
            if (!(player instanceof MessHolder)) continue;
            MessHolder holder = (MessHolder)player;
            NonMessSystem.normalizeMaleOnlyMess((class_1297)player, false);
            UUID playerUuid = player.method_5667();
            if (!NonMessSystem.hasAnyMess(holder)) {
                MESS_CLEAN_NEXT_TICKS.remove(playerUuid);
                continue;
            }
            boolean inWater = player.method_5799() || player.method_5869();
            boolean bl = inRain = !inWater && NonMessSystem.isExposedToRain(world, player);
            if (!inWater && !inRain) {
                MESS_CLEAN_NEXT_TICKS.remove(playerUuid);
                continue;
            }
            Long scheduledTick = MESS_CLEAN_NEXT_TICKS.get(playerUuid);
            if (scheduledTick == null) {
                MESS_CLEAN_NEXT_TICKS.put(playerUuid, now + 20L);
                continue;
            }
            long nextTick = scheduledTick;
            if (now < nextTick) continue;
            boolean changed = NonMessSystem.decrementMess(holder);
            if (changed) {
                NonMessSystem.broadcastMessState(player);
                if (inWater) {
                    NonMessSystem.spawnMessCleaningWaterParticles(world, player);
                }
            }
            if (NonMessSystem.hasAnyMess(holder)) {
                MESS_CLEAN_NEXT_TICKS.put(playerUuid, now + 20L);
                continue;
            }
            MESS_CLEAN_NEXT_TICKS.remove(playerUuid);
        }
    }

    static boolean normalizeMaleOnlyMess(class_1297 entity, boolean convertVToA) {
        if (!NonGenderSystem.isOnlyMale(entity) || !(entity instanceof MessHolder)) {
            return false;
        }
        MessHolder holder = (MessHolder)entity;
        int v = NonMessSystem.clampMess(holder.getVMess());
        if (v <= 0) {
            return false;
        }
        holder.setVMess(0);
        if (convertVToA) {
            holder.setAMess(NonMessSystem.clampMess(holder.getAMess() + v));
        }
        return true;
    }

    static void broadcastMessState(class_3222 player) {
        if (player == null) {
            return;
        }
        NonApiInternals.fireMessChanged(player);
        MinecraftServer server = player.method_51469().method_8503();
        if (server == null) {
            return;
        }
        for (class_3222 target : server.method_3760().method_14571()) {
            NonMessSystem.sendMessState(target, player);
        }
    }

    static void syncMessStates(class_3222 target) {
        if (target == null) {
            return;
        }
        MinecraftServer server = target.method_51469().method_8503();
        if (server == null) {
            return;
        }
        for (class_3222 player : server.method_3760().method_14571()) {
            NonMessSystem.sendMessState(target, player);
        }
    }

    private static void sendMessState(class_3222 target, class_3222 player) {
        if (target == null || player == null) {
            return;
        }
        if (!ServerPlayNetworking.canSend((class_3222)target, MessStateS2CPayload.ID)) {
            return;
        }
        int v = 0;
        int a = 0;
        int m = 0;
        if (NeedsOfNature.isMessSystemEnabled() && player instanceof MessHolder) {
            MessHolder holder = (MessHolder)player;
            NonMessSystem.normalizeMaleOnlyMess((class_1297)player, false);
            v = NonMessSystem.clampMess(holder.getVMess());
            a = NonMessSystem.clampMess(holder.getAMess());
            m = NonMessSystem.clampMess(holder.getMMess());
        }
        int tint = 0xFFFFFF;
        int stored = 0;
        int capacity = 0;
        if (player instanceof LiquidHolder) {
            LiquidHolder liquidHolder = (LiquidHolder)player;
            tint = NonLiquidSystem.resolveLiquidTintRgb(liquidHolder);
            stored = Math.max(0, liquidHolder.getLiquidStored());
            capacity = Math.max(0, liquidHolder.getLiquidCapacity());
        }
        ServerPlayNetworking.send((class_3222)target, (class_8710)new MessStateS2CPayload(player.method_5667(), v, a, m, tint, stored, capacity));
    }

    private static boolean hasAnyMess(MessHolder holder) {
        return holder != null && (holder.getVMess() > 0 || holder.getAMess() > 0 || holder.getMMess() > 0);
    }

    private static boolean decrementMess(MessHolder holder) {
        if (holder == null) {
            return false;
        }
        int beforeV = NonMessSystem.clampMess(holder.getVMess());
        int beforeA = NonMessSystem.clampMess(holder.getAMess());
        int beforeM = NonMessSystem.clampMess(holder.getMMess());
        if (beforeV <= 0 && beforeA <= 0 && beforeM <= 0) {
            return false;
        }
        holder.setVMess(Math.max(0, beforeV - 1));
        holder.setAMess(Math.max(0, beforeA - 1));
        holder.setMMess(Math.max(0, beforeM - 1));
        return true;
    }

    private static boolean isExposedToRain(class_3218 world, class_3222 player) {
        if (world == null || player == null || !world.method_8419()) {
            return false;
        }
        return world.method_8520(player.method_24515());
    }

    private static void spawnMessCleaningWaterParticles(class_3218 world, class_3222 player) {
        if (world == null || player == null) {
            return;
        }
        int count = 2 + world.method_8409().method_43048(3);
        double y = player.method_23323(0.45);
        for (int i = 0; i < count; ++i) {
            double ox = (world.method_8409().method_43058() - 0.5) * (double)player.method_17681();
            double oy = (world.method_8409().method_43058() - 0.5) * 0.45;
            double oz = (world.method_8409().method_43058() - 0.5) * (double)player.method_17681();
            world.method_65096((class_2394)NonParticles.LIQUID_PARTICLE_WATER, player.method_23317() + ox, y + oy, player.method_23321() + oz, 1, 0.0, -0.01, 0.0, 0.0);
        }
    }
}

