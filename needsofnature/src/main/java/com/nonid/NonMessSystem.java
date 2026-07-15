/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.network.packet.CustomPayload
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
import com.nonid.network.NonServerNetworking;
import com.nonid.particle.NonParticles;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

final class NonMessSystem {
    private static final Map<UUID, Long> MESS_CLEAN_NEXT_TICKS = new ConcurrentHashMap<UUID, Long>();

    private NonMessSystem() {
    }

    static void incrementMessOnPeak(ServerWorld world, List<UUID> actorUuids, Identifier animationId) {
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
        for (ServerPlayerEntity player : NeedsOfNature.collectPlayerActors(world, actorUuids)) {
            if (!(player instanceof MessHolder)) continue;
            MessHolder holder = (MessHolder)player;
            NonMessSystem.normalizeMaleOnlyMess((Entity)player, false);
            int messGain = NonMessSystem.rollMessGain(world, player);
            if (messGain <= 0) continue;
            if (hasV) {
                if (NonGenderSystem.isOnlyMale((Entity)player)) {
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

    private static int rollMessGain(ServerWorld world, ServerPlayerEntity player) {
        double multiplier = NonTrinketsIntegration.getActiveTankAccessoryEffects((LivingEntity)player).messGainMultiplier();
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

    static void clearMess(ServerPlayerEntity player) {
        if (player == null || !(player instanceof MessHolder)) {
            return;
        }
        MessHolder holder = (MessHolder)player;
        holder.setVMess(0);
        holder.setAMess(0);
        holder.setMMess(0);
        MESS_CLEAN_NEXT_TICKS.remove(player.getUuid());
        NonMessSystem.broadcastMessState(player);
    }

    static void clearMessStoredState(ServerPlayerEntity player) {
        if (player == null || !(player instanceof MessHolder)) {
            return;
        }
        MessHolder holder = (MessHolder)player;
        holder.setVMess(0);
        holder.setAMess(0);
        holder.setMMess(0);
        MESS_CLEAN_NEXT_TICKS.remove(player.getUuid());
    }

    static int clampMess(int value) {
        return Math.max(0, Math.min(10, value));
    }

    static void tickMessCleaning(ServerWorld world, long now) {
        if (!NeedsOfNature.isMessSystemEnabled()) {
            return;
        }
        if (world == null) {
            return;
        }
        for (ServerPlayerEntity player : world.getPlayers()) {
            boolean inRain;
            if (!(player instanceof MessHolder)) continue;
            MessHolder holder = (MessHolder)player;
            NonMessSystem.normalizeMaleOnlyMess((Entity)player, false);
            UUID playerUuid = player.getUuid();
            if (!NonMessSystem.hasAnyMess(holder)) {
                MESS_CLEAN_NEXT_TICKS.remove(playerUuid);
                continue;
            }
            boolean inWater = player.isTouchingWater() || player.isSubmergedInWater();
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

    static boolean normalizeMaleOnlyMess(Entity entity, boolean convertVToA) {
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

    static void broadcastMessState(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        NonApiInternals.fireMessChanged(player);
        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) {
            return;
        }
        for (ServerPlayerEntity target : server.getPlayerManager().getPlayerList()) {
            NonMessSystem.sendMessState(target, player);
        }
    }

    static void syncMessStates(ServerPlayerEntity target) {
        if (target == null) {
            return;
        }
        MinecraftServer server = target.getEntityWorld().getServer();
        if (server == null) {
            return;
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            NonMessSystem.sendMessState(target, player);
        }
    }

    private static void sendMessState(ServerPlayerEntity target, ServerPlayerEntity player) {
        if (target == null || player == null) {
            return;
        }
        if (!ServerPlayNetworking.canSend((ServerPlayerEntity)target, MessStateS2CPayload.ID)) {
            return;
        }
        int v = 0;
        int a = 0;
        int m = 0;
        if (NeedsOfNature.isMessSystemEnabled() && player instanceof MessHolder) {
            MessHolder holder = (MessHolder)player;
            NonMessSystem.normalizeMaleOnlyMess((Entity)player, false);
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
        NonServerNetworking.send(target, new MessStateS2CPayload(player.getUuid(), v, a, m, tint, stored, capacity));
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

    private static boolean isExposedToRain(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null || !world.isRaining()) {
            return false;
        }
        return world.hasRain(player.getBlockPos());
    }

    private static void spawnMessCleaningWaterParticles(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        int count = 2 + world.getRandom().nextInt(3);
        double y = player.getBodyY(0.45);
        for (int i = 0; i < count; ++i) {
            double ox = (world.getRandom().nextDouble() - 0.5) * (double)player.getWidth();
            double oy = (world.getRandom().nextDouble() - 0.5) * 0.45;
            double oz = (world.getRandom().nextDouble() - 0.5) * (double)player.getWidth();
            world.spawnParticles((ParticleEffect)NonParticles.LIQUID_PARTICLE_WATER, player.getX() + ox, y + oy, player.getZ() + oz, 1, 0.0, -0.01, 0.0, 0.0);
        }
    }
}

