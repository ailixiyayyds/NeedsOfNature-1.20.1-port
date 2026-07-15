/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 */
package com.afwid.client.render.gecko;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_243;

public final class AfwParticleKeyframes {
    private static final ConcurrentHashMap<UUID, Map<String, class_243>> LOCATOR_POSITIONS = new ConcurrentHashMap();

    private AfwParticleKeyframes() {
    }

    public static void updateLocatorPositions(UUID actorUuid, Map<String, class_243> positions) {
        if (actorUuid == null) {
            return;
        }
        if (positions == null || positions.isEmpty()) {
            LOCATOR_POSITIONS.remove(actorUuid);
            return;
        }
        LinkedHashMap<String, class_243> clean = new LinkedHashMap<String, class_243>();
        for (Map.Entry<String, class_243> entry : positions.entrySet()) {
            String locator = AfwParticleKeyframes.cleanLocator(entry.getKey());
            class_243 position = entry.getValue();
            if (locator.isEmpty() || position == null || !position.method_76470()) continue;
            clean.put(locator, position);
        }
        if (clean.isEmpty()) {
            LOCATOR_POSITIONS.remove(actorUuid);
        } else {
            LOCATOR_POSITIONS.put(actorUuid, Map.copyOf(clean));
        }
    }

    public static class_243 findLocatorPosition(UUID actorUuid, String locator) {
        if (actorUuid == null) {
            return null;
        }
        String cleanLocator = AfwParticleKeyframes.cleanLocator(locator);
        if (cleanLocator.isEmpty()) {
            return null;
        }
        Map<String, class_243> positions = LOCATOR_POSITIONS.get(actorUuid);
        return positions == null ? null : positions.get(cleanLocator);
    }

    private static String cleanLocator(String locator) {
        return locator == null ? "" : locator.trim();
    }
}

