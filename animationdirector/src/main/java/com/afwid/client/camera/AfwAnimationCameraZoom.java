/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.option.Perspective
 */
package com.afwid.client.camera;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.option.Perspective;

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

    public static boolean handleScroll(MinecraftClient client, double verticalScroll) {
        if (verticalScroll == 0.0 || !AfwAnimationCameraZoom.isThirdPersonZoomAvailable(client)) {
            return false;
        }
        float direction = verticalScroll > 0.0 ? -1.0f : 1.0f;
        targetDistance = MathHelper.clamp((float)(targetDistance + direction * 0.25f), (float)0.5f, (float)8.0f);
        return true;
    }

    public static void clientTick(MinecraftClient client) {
        if (!AfwAnimationCameraZoom.isLocalPlayerAnimating(client)) {
            AfwAnimationCameraZoom.reset();
            return;
        }
        previousDistance = currentDistance;
        float delta = targetDistance - currentDistance;
        currentDistance = Math.abs(delta) <= 0.005f ? targetDistance : MathHelper.lerp((float)0.35f, (float)currentDistance, (float)targetDistance);
    }

    public static float distance(float tickDelta) {
        float progress = MathHelper.clamp((float)tickDelta, (float)0.0f, (float)1.0f);
        return MathHelper.lerp((float)progress, (float)previousDistance, (float)currentDistance);
    }

    public static void reset() {
        previousDistance = 4.0f;
        currentDistance = 4.0f;
        targetDistance = 4.0f;
    }

    public static boolean isLocalPlayerAnimating(MinecraftClient client) {
        return client != null && client.player != null && AfwClientAnimationRuntime.isActorPendingOrActive(client.player.getUuid());
    }

    private static boolean isThirdPersonZoomAvailable(MinecraftClient client) {
        return AfwAnimationCameraZoom.isLocalPlayerAnimating(client) && client.currentScreen == null && client.options.getPerspective() != Perspective.FIRST_PERSON;
    }
}

