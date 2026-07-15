/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.client.camera;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public final class AfwAnimatedCameraPoseTracker {
    private static final long HOLD_TICKS = 20L;
    private static final Map<UUID, PoseSample> SAMPLES = new ConcurrentHashMap<UUID, PoseSample>();

    private AfwAnimatedCameraPoseTracker() {
    }

    public static void capture(UUID actorUuid, UUID instanceId, class_2960 animationId, class_243 worldPos, class_243 bodyWorldPos, class_243 headForward, class_243 headUp, class_243 headRight, class_243 bodyForward, class_243 bodyUp, class_243 bodyRight, long tick) {
        if (actorUuid == null || worldPos == null) {
            return;
        }
        SAMPLES.put(actorUuid, new PoseSample(instanceId, animationId, worldPos, bodyWorldPos, headForward, headUp, headRight, bodyForward, bodyUp, bodyRight, tick));
    }

    @Nullable
    public static PoseSample resolve(UUID actorUuid, long nowTick) {
        PoseSample sample = SAMPLES.get(actorUuid);
        if (sample == null) {
            return null;
        }
        if (nowTick - sample.tick() > 20L) {
            SAMPLES.remove(actorUuid, sample);
            return null;
        }
        return sample;
    }

    public static void prune(long nowTick) {
        SAMPLES.entrySet().removeIf(entry -> nowTick - ((PoseSample)entry.getValue()).tick() > 20L);
    }

    public static void clear() {
        SAMPLES.clear();
    }

    public record PoseSample(UUID instanceId, class_2960 animationId, class_243 worldPos, class_243 bodyWorldPos, class_243 headForward, class_243 headUp, class_243 headRight, class_243 bodyForward, class_243 bodyUp, class_243 bodyRight, long tick) {
    }
}

