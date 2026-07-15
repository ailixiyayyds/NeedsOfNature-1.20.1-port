/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1941
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_265
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.client.camera;

import net.minecraft.class_1941;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_265;
import org.jetbrains.annotations.Nullable;

public final class AfwFirstPersonCameraCollision {
    private static final double CAMERA_BOX_SIZE = 0.25;
    private static final double PUSH_EPSILON = 0.005;
    private static final int MAX_PUSH_ITERATIONS = 8;

    private AfwFirstPersonCameraCollision() {
    }

    public static class_243 resolve(class_1941 world, class_243 desiredPos) {
        if (world == null || desiredPos == null) {
            return desiredPos;
        }
        class_243 resolvedPos = desiredPos;
        for (int i = 0; i < 8; ++i) {
            class_238 cameraBox = class_238.method_30048((class_243)resolvedPos, (double)0.25, (double)0.25, (double)0.25);
            if (world.method_18026(cameraBox)) {
                return resolvedPos;
            }
            class_243 push = AfwFirstPersonCameraCollision.computeMinimalPush(world, cameraBox);
            if (push == null || push.method_1027() < 1.0E-10) {
                return resolvedPos;
            }
            resolvedPos = resolvedPos.method_1019(push);
        }
        return resolvedPos;
    }

    @Nullable
    private static class_243 computeMinimalPush(class_1941 world, class_238 cameraBox) {
        double bestDistanceSq = Double.POSITIVE_INFINITY;
        class_243 bestPush = null;
        for (class_265 shape : world.method_20812(null, cameraBox.method_1014(0.005))) {
            for (class_238 blockBox : shape.method_1090()) {
                double distanceSq;
                class_243 candidate;
                if (!cameraBox.method_994(blockBox) || (candidate = AfwFirstPersonCameraCollision.smallestSeparatingPush(cameraBox, blockBox)) == null || !((distanceSq = candidate.method_1027()) < bestDistanceSq)) continue;
                bestDistanceSq = distanceSq;
                bestPush = candidate;
            }
        }
        return bestPush;
    }

    @Nullable
    private static class_243 smallestSeparatingPush(class_238 cameraBox, class_238 blockBox) {
        double pushNegX = blockBox.field_1323 - cameraBox.field_1320 - 0.005;
        double pushPosX = blockBox.field_1320 - cameraBox.field_1323 + 0.005;
        double pushNegY = blockBox.field_1322 - cameraBox.field_1325 - 0.005;
        double pushPosY = blockBox.field_1325 - cameraBox.field_1322 + 0.005;
        double pushNegZ = blockBox.field_1321 - cameraBox.field_1324 - 0.005;
        double pushPosZ = blockBox.field_1324 - cameraBox.field_1321 + 0.005;
        class_243[] candidates = new class_243[]{new class_243(pushNegX, 0.0, 0.0), new class_243(pushPosX, 0.0, 0.0), new class_243(0.0, pushNegY, 0.0), new class_243(0.0, pushPosY, 0.0), new class_243(0.0, 0.0, pushNegZ), new class_243(0.0, 0.0, pushPosZ)};
        class_243 best = null;
        double bestDistanceSq = Double.POSITIVE_INFINITY;
        for (class_243 candidate : candidates) {
            double distanceSq;
            if (candidate.method_1027() <= 1.0E-10 || !((distanceSq = candidate.method_1027()) < bestDistanceSq)) continue;
            bestDistanceSq = distanceSq;
            best = candidate;
        }
        return best;
    }
}

