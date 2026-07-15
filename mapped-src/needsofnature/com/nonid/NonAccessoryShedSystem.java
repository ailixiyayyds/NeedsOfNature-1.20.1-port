/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.network.packet.CustomPayload
 */
package com.nonid;

import com.afwid.api.AfwAnimationApi;
import com.nonid.NonTrinketsIntegration;
import com.nonid.data.NonAccessoryDefinitions;
import com.nonid.data.NonLiquidRoles;
import com.nonid.network.AccessoryShedStateS2CPayload;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.packet.CustomPayload;

public final class NonAccessoryShedSystem {
    private static final String V_SLOT = "legs/v";
    private static final String A_SLOT = "legs/a";
    private static final Map<UUID, ShedState> SHED_STATES = new ConcurrentHashMap<UUID, ShedState>();

    private NonAccessoryShedSystem() {
    }

    public static ShedState resolveShedStateForAnimation(Identifier animationId) {
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null || roles.injectorRoles().isEmpty()) {
            return ShedState.NONE;
        }
        boolean shedV = false;
        boolean shedA = false;
        for (NonLiquidRoles.InjectorRole role : roles.injectorRoles().values()) {
            if (role == NonLiquidRoles.InjectorRole.V) {
                shedV = true;
                continue;
            }
            if (role != NonLiquidRoles.InjectorRole.A) continue;
            shedA = true;
        }
        return new ShedState(shedV, shedA, false, false);
    }

    public static ShedState forSlot(String slotId) {
        if (slotId == null) {
            return ShedState.NONE;
        }
        String slot = slotId.toLowerCase(Locale.ROOT);
        if (slot.equals(V_SLOT) || slot.startsWith("legs/v/")) {
            return new ShedState(true, false, true, false);
        }
        if (slot.equals(A_SLOT) || slot.startsWith("legs/a/")) {
            return new ShedState(false, true, false, true);
        }
        return ShedState.NONE;
    }

    public static void addServerShedState(ServerWorld world, ServerPlayerEntity player, ShedState state) {
        if (world == null || player == null || state == null || state.isEmpty()) {
            return;
        }
        UUID uuid = player.getUuid();
        ShedState merged = SHED_STATES.getOrDefault(uuid, ShedState.NONE).merge(state);
        SHED_STATES.put(uuid, merged);
        NonAccessoryShedSystem.syncToWorld(world, uuid, merged);
        NonTrinketsIntegration.forceAccessorySync(player);
    }

    public static void clearServerShedState(ServerWorld world, UUID playerUuid) {
        if (world == null || playerUuid == null) {
            return;
        }
        ShedState removed = SHED_STATES.remove(playerUuid);
        if (removed == null || removed.isEmpty()) {
            return;
        }
        NonAccessoryShedSystem.syncToWorld(world, playerUuid, ShedState.NONE);
        Entity class_12972 = world.getEntity(playerUuid);
        if (class_12972 instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)class_12972;
            NonTrinketsIntegration.forceAccessorySync(player);
        }
    }

    public static void syncAllToViewer(ServerPlayerEntity viewer) {
        if (viewer == null || SHED_STATES.isEmpty()) {
            return;
        }
        for (Map.Entry<UUID, ShedState> entry : SHED_STATES.entrySet()) {
            ShedState state = entry.getValue();
            if (state == null || state.isEmpty()) continue;
            ServerPlayNetworking.send((ServerPlayerEntity)viewer, (CustomPayload)new AccessoryShedStateS2CPayload(entry.getKey(), state.shedV(), state.shedA(), state.forcedV(), state.forcedA()));
        }
    }

    public static void setClientShedState(UUID playerUuid, boolean shedV, boolean shedA, boolean forcedV, boolean forcedA) {
        if (playerUuid == null) {
            return;
        }
        ShedState state = new ShedState(shedV, shedA, forcedV, forcedA);
        if (state.isEmpty()) {
            SHED_STATES.remove(playerUuid);
        } else {
            SHED_STATES.put(playerUuid, state);
        }
    }

    public static void clearAll() {
        SHED_STATES.clear();
    }

    public static void tickServerWorld(ServerWorld world) {
        if (world == null || SHED_STATES.isEmpty()) {
            return;
        }
        for (UUID playerUuid : List.copyOf(SHED_STATES.keySet())) {
            Entity class_12972 = world.getEntity(playerUuid);
            if (!(class_12972 instanceof ServerPlayerEntity)) continue;
            ServerPlayerEntity player = (ServerPlayerEntity)class_12972;
            if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)playerUuid)) continue;
            NonAccessoryShedSystem.clearServerShedState(world, player.getUuid());
        }
    }

    public static ShedState getShedState(UUID playerUuid) {
        if (playerUuid == null) {
            return ShedState.NONE;
        }
        return SHED_STATES.getOrDefault(playerUuid, ShedState.NONE);
    }

    public static boolean isSlotShed(UUID playerUuid, String slotId) {
        return NonAccessoryShedSystem.getShedState(playerUuid).isSlotShed(slotId);
    }

    public static boolean isSlotVisuallyShed(UUID playerUuid, String slotId, Identifier itemId) {
        ShedState state = NonAccessoryShedSystem.getShedState(playerUuid);
        if (!state.isSlotShed(slotId)) {
            return false;
        }
        if (state.isSlotForcedShed(slotId)) {
            return true;
        }
        return !NonAccessoryDefinitions.getBehavior(itemId).ignoreInjectorSlotVisualShedding();
    }

    public static boolean isSlotEffectSuppressed(UUID playerUuid, String slotId, Identifier itemId) {
        ShedState state = NonAccessoryShedSystem.getShedState(playerUuid);
        if (!state.isSlotShed(slotId)) {
            return false;
        }
        if (state.isSlotForcedShed(slotId)) {
            return true;
        }
        return !NonAccessoryDefinitions.getBehavior(itemId).ignoreInjectorSlotEffectSuppression();
    }

    private static void syncToWorld(ServerWorld world, UUID playerUuid, ShedState state) {
        AccessoryShedStateS2CPayload payload = new AccessoryShedStateS2CPayload(playerUuid, state.shedV(), state.shedA(), state.forcedV(), state.forcedA());
        for (ServerPlayerEntity viewer : world.getPlayers()) {
            ServerPlayNetworking.send((ServerPlayerEntity)viewer, (CustomPayload)payload);
        }
    }

    public record ShedState(boolean shedV, boolean shedA, boolean forcedV, boolean forcedA) {
        public static final ShedState NONE = new ShedState(false, false, false, false);

        public boolean isEmpty() {
            return !this.shedV && !this.shedA;
        }

        public ShedState merge(ShedState other) {
            if (other == null || other.isEmpty()) {
                return this;
            }
            return new ShedState(this.shedV || other.shedV, this.shedA || other.shedA, this.forcedV || other.forcedV, this.forcedA || other.forcedA);
        }

        public boolean isSlotShed(String slotId) {
            if (slotId == null) {
                return false;
            }
            String slot = slotId.toLowerCase(Locale.ROOT);
            if (slot.equals(NonAccessoryShedSystem.V_SLOT) || slot.startsWith("legs/v/")) {
                return this.shedV;
            }
            if (slot.equals(NonAccessoryShedSystem.A_SLOT) || slot.startsWith("legs/a/")) {
                return this.shedA;
            }
            return false;
        }

        public boolean isSlotForcedShed(String slotId) {
            if (slotId == null) {
                return false;
            }
            String slot = slotId.toLowerCase(Locale.ROOT);
            if (slot.equals(NonAccessoryShedSystem.V_SLOT) || slot.startsWith("legs/v/")) {
                return this.forcedV;
            }
            if (slot.equals(NonAccessoryShedSystem.A_SLOT) || slot.startsWith("legs/a/")) {
                return this.forcedA;
            }
            return false;
        }
    }
}

