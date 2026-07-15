/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.world.ClientWorld
 */
package com.nonid.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

public final class NonPuddleSizing {
    private static final int MAX_TICKS = 400;
    private static final float MAX_SCALE = 3.0f;
    private static final double MOVE_EPSILON_SQ = 1.0E-8;
    private static final Map<UUID, StationaryState> STATES = new HashMap<UUID, StationaryState>();

    private NonPuddleSizing() {
    }

    public static void clientTick(MinecraftClient client) {
        if (client == null || client.world == null) {
            STATES.clear();
            return;
        }
        ClientWorld world = client.world;
        HashSet<UUID> seen = new HashSet<UUID>();
        for (PlayerEntity player : world.getPlayers()) {
            UUID uuid2 = player.getUuid();
            seen.add(uuid2);
            Vec3d pos = player.getPos();
            StationaryState state = STATES.get(uuid2);
            if (state == null) {
                STATES.put(uuid2, new StationaryState(pos, 0));
                continue;
            }
            if (pos.squaredDistanceTo(state.lastPos) > 1.0E-8) {
                STATES.put(uuid2, new StationaryState(pos, 0));
                continue;
            }
            int next = Math.min(400, state.ticks + 1);
            STATES.put(uuid2, new StationaryState(state.lastPos, next));
        }
        STATES.keySet().removeIf(uuid -> !seen.contains(uuid));
    }

    public static float scaleForPosition(ClientWorld world, double x, double y, double z) {
        if (world == null || world.getPlayers().isEmpty()) {
            return 1.0f;
        }
        PlayerEntity nearest = null;
        double bestSq = Double.MAX_VALUE;
        for (PlayerEntity player : world.getPlayers()) {
            double dz;
            double dy;
            double dx = player.getX() - x;
            double dist = dx * dx + (dy = player.getY() - y) * dy + (dz = player.getZ() - z) * dz;
            if (!(dist < bestSq)) continue;
            bestSq = dist;
            nearest = player;
        }
        if (nearest == null) {
            return 1.0f;
        }
        StationaryState state = STATES.get(nearest.getUuid());
        if (state == null || state.ticks <= 0) {
            return 1.0f;
        }
        float t = Math.min(400, state.ticks);
        float ratio = t / 400.0f;
        return 1.0f + ratio * 2.0f;
    }

    private record StationaryState(Vec3d lastPos, int ticks) {
    }
}

