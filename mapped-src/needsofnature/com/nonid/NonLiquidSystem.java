/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerInventory
 *  net.minecraft.item.ItemStack
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.Registries
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.server.MinecraftServer
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import com.nonid.LiquidHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonAccessoryEffects;
import com.nonid.NonAdvancementHooks;
import com.nonid.NonApiInternals;
import com.nonid.NonConfig;
import com.nonid.NonMessSystem;
import com.nonid.NonTrinketsIntegration;
import com.nonid.client.NonLiquidColors;
import com.nonid.data.NonLiquidGainOverrides;
import com.nonid.effect.NonStatusEffects;
import com.nonid.network.LiquidStateS2CPayload;
import com.nonid.particle.NonParticles;
import com.nonid.potion.NonPotions;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.Registries;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

final class NonLiquidSystem {
    private static final double LIQUID_SNEAK_DECAY_MULTIPLIER = 16.0;
    private static final double LIQUID_WATER_DECAY_MULTIPLIER = 2.0;
    private static final double LIQUID_DECAY_MIN_MULT = 0.1;
    private static final double LIQUID_DECAY_MAX_MULT = 1.5;
    private static final double LIQUID_SNEAK_PARTICLE_BACK_OFFSET = 0.28;
    private static final Map<UUID, Double> LIQUID_DECAY_REMAINDER = new ConcurrentHashMap<UUID, Double>();

    private NonLiquidSystem() {
    }

    static void syncLiquidState(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        if (!(player instanceof LiquidHolder)) {
            return;
        }
        LiquidHolder holder = (LiquidHolder)player;
        NonLiquidSystem.enforceLiquidCapacity(holder);
        int stored = holder.getLiquidStored();
        int capacity = holder.getLiquidCapacity();
        NonLiquidSystem.updateFilledEffect(player, holder);
        int tint = NonLiquidSystem.resolveLiquidTintRgb(holder);
        ServerPlayNetworking.send((ServerPlayerEntity)player, (CustomPayload)new LiquidStateS2CPayload(stored, capacity, tint));
        NonApiInternals.fireLiquidChanged(player);
        NonMessSystem.broadcastMessState(player);
        NonAdvancementHooks.checkLiquidAdvancements(player, holder);
    }

    private static void enforceLiquidCapacity(LiquidHolder holder) {
        if (holder == null) {
            return;
        }
        int capacity = holder.getLiquidCapacity();
        int stored = holder.getLiquidStored();
        if (capacity <= 0) {
            if (stored > 0) {
                holder.setLiquidStored(0);
            }
            return;
        }
        if (stored > capacity) {
            holder.setLiquidStored(capacity);
        }
    }

    static void syncLiquidStateToAll(@Nullable MinecraftServer server) {
        if (server == null) {
            return;
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            NonLiquidSystem.syncLiquidState(player);
        }
    }

    static void refreshLiquidBottleTintsToAll(@Nullable MinecraftServer server) {
        if (server == null) {
            return;
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            NonLiquidSystem.refreshLiquidBottleTints(player);
        }
    }

    private static void refreshLiquidBottleTints(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); ++i) {
            NonLiquidSystem.refreshLiquidBottleTint(inventory.getStack(i));
        }
        NonLiquidSystem.refreshLiquidBottleTint(player.currentScreenHandler.getCursorStack());
    }

    private static void refreshLiquidBottleTint(ItemStack stack) {
        if (!NonPotions.isLiquidBottle(stack)) {
            return;
        }
        Identifier entityTypeId = NonPotions.getLiquidBottleEntityTypeId(stack);
        int tint = NonLiquidSystem.resolveBottleTintRgb(entityTypeId);
        PotionContentsComponent current = (PotionContentsComponent)stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, (Object)PotionContentsComponent.DEFAULT);
        PotionContentsComponent updated = new PotionContentsComponent(current.comp_2378(), Optional.of(tint), List.of(), Optional.empty());
        stack.set(DataComponentTypes.POTION_CONTENTS, (Object)updated);
    }

    static int resolveLiquidTintRgb(LiquidHolder holder) {
        if (holder == null || holder.getLiquidStored() <= 0) {
            return NonLiquidSystem.resolveMixedLiquidColorRgb();
        }
        if (holder.getLiquidComposition() == LiquidHolder.LiquidComposition.ENTITY) {
            return NonLiquidSystem.resolveEntityLiquidColorRgb(holder.getLiquidEntityTypeId());
        }
        return NonLiquidSystem.resolveMixedLiquidColorRgb();
    }

    static int resolveBottleTintRgb(@Nullable Identifier entityTypeId) {
        if (entityTypeId == null) {
            return NonLiquidSystem.resolveMixedLiquidColorRgb();
        }
        return NonLiquidSystem.resolveEntityLiquidColorRgb(entityTypeId);
    }

    private static int resolveEntityLiquidColorRgb(@Nullable Identifier entityTypeId) {
        if (entityTypeId == null) {
            return 16776427;
        }
        String key = entityTypeId.toString();
        String configuredHex = NeedsOfNature.getConfig().getLiquidColorByEntity().get(key);
        Integer configuredRgb = NonLiquidColors.parseHexColor(configuredHex);
        if (configuredRgb != null) {
            return configuredRgb;
        }
        Integer packOverride = NonLiquidGainOverrides.getColorRgb(entityTypeId);
        if (packOverride != null) {
            return packOverride;
        }
        return 16776427;
    }

    private static int resolveMixedLiquidColorRgb() {
        String configuredHex = NeedsOfNature.getConfig().getLiquidColorByEntity().get("needsofnature:mixed");
        Integer configuredRgb = NonLiquidColors.parseHexColor(configuredHex);
        if (configuredRgb != null) {
            return configuredRgb;
        }
        Integer packOverride = NonLiquidGainOverrides.getMixedColorRgb();
        if (packOverride != null) {
            return packOverride;
        }
        return 15920063;
    }

    static Map<String, Integer> resolveEffectiveLiquidGainMap() {
        LinkedHashMap<String, Integer> merged = new LinkedHashMap<String, Integer>(NonLiquidGainOverrides.getGainByEntity());
        merged.putAll(NeedsOfNature.getConfig().getLiquidGainByEntity());
        return Map.copyOf(merged);
    }

    static Map<String, String> resolveEffectiveLiquidColorMap() {
        LinkedHashMap<String, String> merged = new LinkedHashMap<String, String>(NonLiquidGainOverrides.getColorByEntity());
        String packMixedHex = NonLiquidGainOverrides.getMixedColorHex();
        if (packMixedHex != null) {
            merged.put("needsofnature:mixed", packMixedHex);
        }
        merged.putAll(NeedsOfNature.getConfig().getLiquidColorByEntity());
        return Map.copyOf(merged);
    }

    static void tickLiquidDecay(ServerWorld world) {
        if (world == null) {
            return;
        }
        if (world.getPlayers().isEmpty()) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        boolean liquidTankEnabled = config.isLiquidTankEnabled();
        double decayPerSecond = config.getLiquidDecayPerSecond();
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!(player instanceof LiquidHolder)) continue;
            LiquidHolder holder = (LiquidHolder)player;
            if (!liquidTankEnabled) {
                LIQUID_DECAY_REMAINDER.remove(player.getUuid());
                if (holder.getLiquidStored() > 0) {
                    holder.setLiquidStored(0);
                    NonLiquidSystem.syncLiquidState(player);
                }
                NonLiquidSystem.updateFilledEffect(player, holder);
                continue;
            }
            NonLiquidSystem.updateFilledEffect(player, holder);
            if (decayPerSecond <= 0.0 || NeedsOfNature.getActivePlayerInstance(player) != null) continue;
            int stored = holder.getLiquidStored();
            if (stored <= 0) {
                LIQUID_DECAY_REMAINDER.remove(player.getUuid());
                NonLiquidSystem.updateFilledEffect(player, holder);
                continue;
            }
            NonAccessoryEffects accessoryEffects = NonTrinketsIntegration.getActiveTankAccessoryEffects((LivingEntity)player);
            double decayPerTick = decayPerSecond / 20.0;
            boolean equalizedDecay = accessoryEffects.equalizeLiquidDecayContext();
            if (!equalizedDecay && player.isSneaking()) {
                decayPerTick *= 16.0;
            }
            if (!equalizedDecay && (player.isTouchingWater() || player.isSubmergedInWater())) {
                decayPerTick *= 2.0;
            }
            int capacity = holder.getLiquidCapacity();
            double fillRatio = 0.0;
            if (capacity > 0) {
                fillRatio = Math.min(1.0, Math.max(0.0, (double)stored / (double)capacity));
            }
            double curve = 1.0 - Math.log1p((1.0 - fillRatio) * 9.0) / Math.log1p(9.0);
            double decayMultiplier = 0.1 + 1.4 * curve;
            decayPerTick *= decayMultiplier;
            double remainder = LIQUID_DECAY_REMAINDER.getOrDefault(player.getUuid(), 0.0);
            double accum = remainder + (decayPerTick *= accessoryEffects.liquidDecayMultiplier());
            int decay = (int)Math.floor(accum);
            remainder = accum - (double)decay;
            LIQUID_DECAY_REMAINDER.put(player.getUuid(), remainder);
            if (decay <= 0) continue;
            int next = Math.max(0, stored - decay);
            if (next != stored) {
                holder.setLiquidStored(next);
                NonLiquidSystem.syncLiquidState(player);
            }
            NonLiquidSystem.spawnLiquidDecayParticles(world, player);
        }
    }

    static void updateFilledEffect(ServerPlayerEntity player, LiquidHolder holder) {
        if (player == null || holder == null) {
            return;
        }
        int amplifier = NonLiquidSystem.getAmplifier(holder);
        RegistryEntry entry = Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.FILLED);
        StatusEffectInstance current = player.getStatusEffect(entry);
        if (amplifier < 0) {
            player.removeStatusEffect(entry);
            return;
        }
        if (current != null) {
            if (current.getAmplifier() != amplifier) {
                player.removeStatusEffect(entry);
            } else {
                return;
            }
        }
        player.addStatusEffect(new StatusEffectInstance(entry, -1, amplifier, false, false, true));
    }

    private static int getAmplifier(LiquidHolder holder) {
        int capacity = holder.getLiquidCapacity();
        int stored = holder.getLiquidStored();
        int amplifier = -1;
        if (capacity > 0) {
            float ratio = Math.max(0.0f, Math.min(1.0f, (float)stored / (float)capacity));
            if (ratio >= 0.8f) {
                amplifier = 2;
            } else if (ratio >= 0.5f) {
                amplifier = 1;
            } else if (ratio >= 0.3f) {
                amplifier = 0;
            }
        }
        return amplifier;
    }

    private static void spawnLiquidDecayParticles(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        Vec3d spawnPos = NonLiquidSystem.resolveLiquidDecayParticlePos(player);
        world.spawnParticles((ParticleEffect)NonParticles.LIQUID_PARTICLE_FALLING, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0.0);
    }

    private static Vec3d resolveLiquidDecayParticlePos(ServerPlayerEntity player) {
        double x = player.getX();
        double z = player.getZ();
        if (player.isSneaking()) {
            double yawRad = Math.toRadians(player.bodyYaw);
            x += Math.sin(yawRad) * 0.28;
            z -= Math.cos(yawRad) * 0.28;
        }
        return new Vec3d(x, player.getBodyY(0.4), z);
    }
}

