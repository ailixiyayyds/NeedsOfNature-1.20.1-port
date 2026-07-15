/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package com.afwid.client.render.gecko;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.math.Vec3d;

public final class AfwParticleKeyframes {
    private static final ConcurrentHashMap<UUID, Map<String, Vec3d>> LOCATOR_POSITIONS = new ConcurrentHashMap();

    private AfwParticleKeyframes() {
    }

    public static void updateLocatorPositions(UUID actorUuid, Map<String, Vec3d> positions) {
        if (actorUuid == null) {
            return;
        }
        if (positions == null || positions.isEmpty()) {
            LOCATOR_POSITIONS.remove(actorUuid);
            return;
        }
        LinkedHashMap<String, Vec3d> clean = new LinkedHashMap<String, Vec3d>();
        for (Map.Entry<String, Vec3d> entry : positions.entrySet()) {
            String locator = AfwParticleKeyframes.cleanLocator(entry.getKey());
            Vec3d position = entry.getValue();
            if (locator.isEmpty() || position == null || !position.isFinite()) continue;
            clean.put(locator, position);
        }
        if (clean.isEmpty()) {
            LOCATOR_POSITIONS.remove(actorUuid);
        } else {
            LOCATOR_POSITIONS.put(actorUuid, Map.copyOf(clean));
        }
    }

    public static Vec3d findLocatorPosition(UUID actorUuid, String locator) {
        if (actorUuid == null) {
            return null;
        }
        String cleanLocator = AfwParticleKeyframes.cleanLocator(locator);
        if (cleanLocator.isEmpty()) {
            return null;
        }
        Map<String, Vec3d> positions = LOCATOR_POSITIONS.get(actorUuid);
        return positions == null ? null : positions.get(cleanLocator);
    }

    private static String cleanLocator(String locator) {
        return locator == null ? "" : locator.trim();
    }
}

