/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.class_1293
 *  net.minecraft.class_1309
 *  net.minecraft.class_1661
 *  net.minecraft.class_1799
 *  net.minecraft.class_1844
 *  net.minecraft.class_2394
 *  net.minecraft.class_243
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_6880
 *  net.minecraft.class_7923
 *  net.minecraft.class_8710
 *  net.minecraft.class_9334
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
import net.minecraft.class_1293;
import net.minecraft.class_1309;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_1844;
import net.minecraft.class_2394;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_6880;
import net.minecraft.class_7923;
import net.minecraft.class_8710;
import net.minecraft.class_9334;
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

    static void syncLiquidState(class_3222 player) {
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
        ServerPlayNetworking.send((class_3222)player, (class_8710)new LiquidStateS2CPayload(stored, capacity, tint));
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
        for (class_3222 player : server.method_3760().method_14571()) {
            NonLiquidSystem.syncLiquidState(player);
        }
    }

    static void refreshLiquidBottleTintsToAll(@Nullable MinecraftServer server) {
        if (server == null) {
            return;
        }
        for (class_3222 player : server.method_3760().method_14571()) {
            NonLiquidSystem.refreshLiquidBottleTints(player);
        }
    }

    private static void refreshLiquidBottleTints(class_3222 player) {
        if (player == null) {
            return;
        }
        class_1661 inventory = player.method_31548();
        for (int i = 0; i < inventory.method_5439(); ++i) {
            NonLiquidSystem.refreshLiquidBottleTint(inventory.method_5438(i));
        }
        NonLiquidSystem.refreshLiquidBottleTint(player.field_7512.method_34255());
    }

    private static void refreshLiquidBottleTint(class_1799 stack) {
        if (!NonPotions.isLiquidBottle(stack)) {
            return;
        }
        class_2960 entityTypeId = NonPotions.getLiquidBottleEntityTypeId(stack);
        int tint = NonLiquidSystem.resolveBottleTintRgb(entityTypeId);
        class_1844 current = (class_1844)stack.method_58695(class_9334.field_49651, (Object)class_1844.field_49274);
        class_1844 updated = new class_1844(current.comp_2378(), Optional.of(tint), List.of(), Optional.empty());
        stack.method_57379(class_9334.field_49651, (Object)updated);
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

    static int resolveBottleTintRgb(@Nullable class_2960 entityTypeId) {
        if (entityTypeId == null) {
            return NonLiquidSystem.resolveMixedLiquidColorRgb();
        }
        return NonLiquidSystem.resolveEntityLiquidColorRgb(entityTypeId);
    }

    private static int resolveEntityLiquidColorRgb(@Nullable class_2960 entityTypeId) {
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

    static void tickLiquidDecay(class_3218 world) {
        if (world == null) {
            return;
        }
        if (world.method_18456().isEmpty()) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        boolean liquidTankEnabled = config.isLiquidTankEnabled();
        double decayPerSecond = config.getLiquidDecayPerSecond();
        for (class_3222 player : world.method_18456()) {
            if (!(player instanceof LiquidHolder)) continue;
            LiquidHolder holder = (LiquidHolder)player;
            if (!liquidTankEnabled) {
                LIQUID_DECAY_REMAINDER.remove(player.method_5667());
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
                LIQUID_DECAY_REMAINDER.remove(player.method_5667());
                NonLiquidSystem.updateFilledEffect(player, holder);
                continue;
            }
            NonAccessoryEffects accessoryEffects = NonTrinketsIntegration.getActiveTankAccessoryEffects((class_1309)player);
            double decayPerTick = decayPerSecond / 20.0;
            boolean equalizedDecay = accessoryEffects.equalizeLiquidDecayContext();
            if (!equalizedDecay && player.method_5715()) {
                decayPerTick *= 16.0;
            }
            if (!equalizedDecay && (player.method_5799() || player.method_5869())) {
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
            double remainder = LIQUID_DECAY_REMAINDER.getOrDefault(player.method_5667(), 0.0);
            double accum = remainder + (decayPerTick *= accessoryEffects.liquidDecayMultiplier());
            int decay = (int)Math.floor(accum);
            remainder = accum - (double)decay;
            LIQUID_DECAY_REMAINDER.put(player.method_5667(), remainder);
            if (decay <= 0) continue;
            int next = Math.max(0, stored - decay);
            if (next != stored) {
                holder.setLiquidStored(next);
                NonLiquidSystem.syncLiquidState(player);
            }
            NonLiquidSystem.spawnLiquidDecayParticles(world, player);
        }
    }

    static void updateFilledEffect(class_3222 player, LiquidHolder holder) {
        if (player == null || holder == null) {
            return;
        }
        int amplifier = NonLiquidSystem.getAmplifier(holder);
        class_6880 entry = class_7923.field_41174.method_47983((Object)NonStatusEffects.FILLED);
        class_1293 current = player.method_6112(entry);
        if (amplifier < 0) {
            player.method_6016(entry);
            return;
        }
        if (current != null) {
            if (current.method_5578() != amplifier) {
                player.method_6016(entry);
            } else {
                return;
            }
        }
        player.method_6092(new class_1293(entry, -1, amplifier, false, false, true));
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

    private static void spawnLiquidDecayParticles(class_3218 world, class_3222 player) {
        if (world == null || player == null) {
            return;
        }
        class_243 spawnPos = NonLiquidSystem.resolveLiquidDecayParticlePos(player);
        world.method_65096((class_2394)NonParticles.LIQUID_PARTICLE_FALLING, spawnPos.field_1352, spawnPos.field_1351, spawnPos.field_1350, 1, 0.0, 0.0, 0.0, 0.0);
    }

    private static class_243 resolveLiquidDecayParticlePos(class_3222 player) {
        double x = player.method_23317();
        double z = player.method_23321();
        if (player.method_5715()) {
            double yawRad = Math.toRadians(player.field_6283);
            x += Math.sin(yawRad) * 0.28;
            z -= Math.cos(yawRad) * 0.28;
        }
        return new class_243(x, player.method_23323(0.4), z);
    }
}

