/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_5498
 */
package com.afwid.client.camera;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_5498;

public final class AfwAnimationCameraZoom {
    private static final float DEFAULT_DISTANCE = 4.0f;
    private static final float MIN_DISTANCE = 0.5f;
    private static final float MAX_DISTANCE = 8.0f;
    private static final float SCROLL_STEP = 0.25f;
    private static final float SMOOTHING_PER_TICK = 0.35f;
    private static final float SNAP_EPSILON = 0.005f;
    private static float previousDistance = 4.0f;
    private static float currentDistance = 4.0f;
    private static float targetDistance = 4.0f;

    private AfwAnimationCameraZoom() {
    }

    public static boolean handleScroll(class_310 client, double verticalScroll) {
        if (verticalScroll == 0.0 || !AfwAnimationCameraZoom.isThirdPersonZoomAvailable(client)) {
            return false;
        }
        float direction = verticalScroll > 0.0 ? -1.0f : 1.0f;
        targetDistance = class_3532.method_15363((float)(targetDistance + direction * 0.25f), (float)0.5f, (float)8.0f);
        return true;
    }

    public static void clientTick(class_310 client) {
        if (!AfwAnimationCameraZoom.isLocalPlayerAnimating(client)) {
            AfwAnimationCameraZoom.reset();
            return;
        }
        previousDistance = currentDistance;
        float delta = targetDistance - currentDistance;
        currentDistance = Math.abs(delta) <= 0.005f ? targetDistance : class_3532.method_16439((float)0.35f, (float)currentDistance, (float)targetDistance);
    }

    public static float distance(float tickDelta) {
        float progress = class_3532.method_15363((float)tickDelta, (float)0.0f, (float)1.0f);
        return class_3532.method_16439((float)progress, (float)previousDistance, (float)currentDistance);
    }

    public static void reset() {
        previousDistance = 4.0f;
        currentDistance = 4.0f;
        targetDistance = 4.0f;
    }

    public static boolean isLocalPlayerAnimating(class_310 client) {
        return client != null && client.field_1724 != null && AfwClientAnimationRuntime.isActorPendingOrActive(client.field_1724.method_5667());
    }

    private static boolean isThirdPersonZoomAvailable(class_310 client) {
        return AfwAnimationCameraZoom.isLocalPlayerAnimating(client) && client.field_1755 == null && client.field_1690.method_31044() != class_5498.field_26664;
    }
}

