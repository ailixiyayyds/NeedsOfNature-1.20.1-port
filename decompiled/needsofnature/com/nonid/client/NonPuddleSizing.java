/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_243
 *  net.minecraft.class_310
 *  net.minecraft.class_638
 */
package com.nonid.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_638;

public final class NonPuddleSizing {
    private static final int MAX_TICKS = 400;
    private static final float MAX_SCALE = 3.0f;
    private static final double MOVE_EPSILON_SQ = 1.0E-8;
    private static final Map<UUID, StationaryState> STATES = new HashMap<UUID, StationaryState>();

    private NonPuddleSizing() {
    }

    public static void clientTick(class_310 client) {
        if (client == null || client.field_1687 == null) {
            STATES.clear();
            return;
        }
        class_638 world = client.field_1687;
        HashSet<UUID> seen = new HashSet<UUID>();
        for (class_1657 player : world.method_18456()) {
            UUID uuid2 = player.method_5667();
            seen.add(uuid2);
            class_243 pos = player.method_73189();
            StationaryState state = STATES.get(uuid2);
            if (state == null) {
                STATES.put(uuid2, new StationaryState(pos, 0));
                continue;
            }
            if (pos.method_1025(state.lastPos) > 1.0E-8) {
                STATES.put(uuid2, new StationaryState(pos, 0));
                continue;
            }
            int next = Math.min(400, state.ticks + 1);
            STATES.put(uuid2, new StationaryState(state.lastPos, next));
        }
        STATES.keySet().removeIf(uuid -> !seen.contains(uuid));
    }

    public static float scaleForPosition(class_638 world, double x, double y, double z) {
        if (world == null || world.method_18456().isEmpty()) {
            return 1.0f;
        }
        class_1657 nearest = null;
        double bestSq = Double.MAX_VALUE;
        for (class_1657 player : world.method_18456()) {
            double dz;
            double dy;
            double dx = player.method_23317() - x;
            double dist = dx * dx + (dy = player.method_23318() - y) * dy + (dz = player.method_23321() - z) * dz;
            if (!(dist < bestSq)) continue;
            bestSq = dist;
            nearest = player;
        }
        if (nearest == null) {
            return 1.0f;
        }
        StationaryState state = STATES.get(nearest.method_5667());
        if (state == null || state.ticks <= 0) {
            return 1.0f;
        }
        float t = Math.min(400, state.ticks);
        float ratio = t / 400.0f;
        return 1.0f + ratio * 2.0f;
    }

    private record StationaryState(class_243 lastPos, int ticks) {
    }
}

