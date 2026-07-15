/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1922
 *  net.minecraft.class_1937
 *  net.minecraft.class_1941
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_3726
 *  net.minecraft.class_4184
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.camera.AfwAnimatedCameraPoseTracker;
import com.afwid.client.camera.AfwAnimationCameraZoom;
import com.afwid.client.camera.AfwFirstPersonCameraCollision;
import com.afwid.client.camera.AfwFirstPersonLookConstraint;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1922;
import net.minecraft.class_1937;
import net.minecraft.class_1941;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3726;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_4184.class})
public abstract class CameraAnimationMixin {
    private static final double CAMERA_FORWARD_OFFSET = 0.2;
    private static final double THIRD_PERSON_ORBIT_EPSILON_SQ = 1.0E-8;
    private static final long FALLBACK_POSE_HOLD_TICKS = 20L;
    private static final long START_ALIGNMENT_TICKS = 8L;
    private static final long THIRD_PERSON_ORBIT_SMOOTH_TICKS = 6L;
    private static final long NEW_INSTANCE_ORBIT_SAMPLE_DELAY_TICKS = 4L;
    private static final double THIRD_PERSON_FOCUS_BOX_SIZE = 0.25;
    private static final double THIRD_PERSON_FOCUS_UP_STEP = 0.1;
    private static final int THIRD_PERSON_FOCUS_UP_STEPS = 16;
    private static final double THIRD_PERSON_FOCUS_HORIZONTAL_STEP = 0.25;
    private static final int THIRD_PERSON_FOCUS_HORIZONTAL_RINGS = 4;
    private static AfwAnimatedCameraPoseTracker.PoseSample afw$lastValidPosedPose = null;
    private static class_243 afw$lastConstrainedLook = null;
    private static UUID afw$lastActiveInstanceId = null;
    private static UUID afw$startAlignInstanceId = null;
    private static UUID afw$bodyOrbitInstanceId = null;
    private static class_2960 afw$bodyOrbitAnimationId = null;
    private static class_243 afw$bodyOrbitTarget = null;
    private static boolean afw$delayBodyOrbitForCurrentInstance = false;
    private static UUID afw$smoothedOrbitInstanceId = null;
    private static class_2960 afw$smoothedOrbitAnimationId = null;
    private static class_243 afw$orbitSmoothStart = null;
    private static class_243 afw$orbitSmoothTarget = null;
    private static class_243 afw$currentSmoothedOrbitTarget = null;
    private static long afw$orbitSmoothStartTick = 0L;
    private static boolean afw$wasFirstPersonLastFrame = false;
    private static boolean afw$startAlignPendingTarget = false;
    private static boolean afw$startAlignActive = false;
    private static float afw$startAlignFromYaw = 0.0f;
    private static float afw$startAlignFromPitch = 0.0f;
    private static float afw$startAlignTargetYaw = 0.0f;
    private static float afw$startAlignTargetPitch = 0.0f;
    private static long afw$startAlignTick = 0L;

    @Shadow
    protected abstract void method_19322(class_243 var1);

    @Shadow
    protected abstract void method_19325(float var1, float var2);

    @Shadow
    protected abstract void method_19324(float var1, float var2, float var3);

    @Shadow
    private float method_19318(float startingDistance) {
        return startingDistance;
    }

    @Inject(method={"method_19321"}, at={@At(value="TAIL")})
    private void afw$lockFirstPersonCameraToAnimatedPose(class_1937 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        class_243 targetLook;
        boolean replacingActiveInstance;
        if (!(focusedEntity instanceof class_1657)) {
            CameraAnimationMixin.afw$resetAnimationCameraState(false);
            return;
        }
        class_1657 focusedPlayer = (class_1657)focusedEntity;
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1687 == null || client.field_1724 == null) {
            CameraAnimationMixin.afw$resetAnimationCameraState(!thirdPerson);
            return;
        }
        if (!focusedPlayer.method_5667().equals(client.field_1724.method_5667())) {
            CameraAnimationMixin.afw$resetAnimationCameraState(!thirdPerson);
            return;
        }
        UUID actorUuid = focusedPlayer.method_5667();
        boolean pendingOrActive = AfwClientAnimationRuntime.isActorPendingOrActive(actorUuid);
        UUID activeInstanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(actorUuid);
        if (!pendingOrActive) {
            CameraAnimationMixin.afw$resetAnimationCameraState(!thirdPerson);
            return;
        }
        if (thirdPerson) {
            afw$lastActiveInstanceId = activeInstanceId;
            afw$wasFirstPersonLastFrame = false;
            class_2960 activeAnimationId = AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining(actorUuid);
            this.afw$applyThirdPersonCameraAdjustments(actorUuid, activeInstanceId, activeAnimationId, focusedPlayer, tickDelta, client.field_1687.method_75260());
            return;
        }
        boolean enteredFirstPerson = !afw$wasFirstPersonLastFrame;
        boolean instanceChanged = !Objects.equals(activeInstanceId, afw$lastActiveInstanceId);
        boolean bl = replacingActiveInstance = instanceChanged && afw$lastActiveInstanceId != null;
        if (activeInstanceId != null && (enteredFirstPerson || instanceChanged && !replacingActiveInstance)) {
            CameraAnimationMixin.afw$beginStartAlignment(activeInstanceId, focusedPlayer.method_5705(tickDelta), focusedPlayer.method_5695(tickDelta));
        } else if (replacingActiveInstance) {
            CameraAnimationMixin.afw$clearStartAlignment();
        }
        afw$lastActiveInstanceId = activeInstanceId;
        afw$wasFirstPersonLastFrame = true;
        long nowTick = client.field_1687.method_75260();
        AfwAnimatedCameraPoseTracker.PoseSample posedPose = AfwAnimatedCameraPoseTracker.resolve(actorUuid, nowTick);
        if (posedPose != null) {
            afw$lastValidPosedPose = posedPose;
        } else if (afw$lastValidPosedPose != null && nowTick - afw$lastValidPosedPose.tick() <= 20L) {
            posedPose = afw$lastValidPosedPose;
        } else {
            return;
        }
        if (afw$startAlignPendingTarget && Objects.equals(activeInstanceId, afw$startAlignInstanceId) && (targetLook = CameraAnimationMixin.afw$resolveStartAlignmentTargetLook(posedPose)) != null) {
            afw$startAlignTargetYaw = AfwFirstPersonLookConstraint.toMinecraftYaw(targetLook, afw$startAlignFromYaw);
            afw$startAlignTargetPitch = AfwFirstPersonLookConstraint.toMinecraftPitch(targetLook);
            afw$startAlignTick = nowTick;
            afw$startAlignPendingTarget = false;
            afw$startAlignActive = true;
        }
        float desiredYaw = focusedPlayer.method_5705(tickDelta);
        float desiredPitch = focusedPlayer.method_5695(tickDelta);
        if (afw$startAlignActive && Objects.equals(activeInstanceId, afw$startAlignInstanceId)) {
            float progress = class_3532.method_15363((float)(((float)(nowTick - afw$startAlignTick) + tickDelta) / 8.0f), (float)0.0f, (float)1.0f);
            float eased = CameraAnimationMixin.afw$smootherStep(progress);
            desiredYaw = CameraAnimationMixin.afw$lerpAngleDegrees(eased, afw$startAlignFromYaw, afw$startAlignTargetYaw);
            desiredPitch = class_3532.method_16439((float)eased, (float)afw$startAlignFromPitch, (float)afw$startAlignTargetPitch);
            if (progress >= 1.0f) {
                afw$startAlignActive = false;
            }
        }
        class_243 desiredLook = class_243.method_1030((float)desiredPitch, (float)desiredYaw);
        class_243 constrainedLook = AfwFirstPersonLookConstraint.constrain(desiredLook, posedPose, afw$lastConstrainedLook);
        float constrainedYaw = AfwFirstPersonLookConstraint.toMinecraftYaw(constrainedLook, desiredYaw);
        float constrainedPitch = AfwFirstPersonLookConstraint.toMinecraftPitch(constrainedLook);
        focusedPlayer.field_5982 = constrainedYaw;
        focusedPlayer.field_6004 = constrainedPitch;
        focusedPlayer.method_36456(constrainedYaw);
        focusedPlayer.method_36457(constrainedPitch);
        focusedPlayer.field_6259 = constrainedYaw;
        focusedPlayer.field_6241 = constrainedYaw;
        focusedPlayer.method_5847(constrainedYaw);
        focusedPlayer.field_6220 = constrainedYaw;
        focusedPlayer.field_6283 = constrainedYaw;
        focusedPlayer.method_5636(constrainedYaw);
        this.method_19325(constrainedYaw, constrainedPitch);
        class_243 desiredCameraPos = posedPose.worldPos().method_1019(constrainedLook.method_1021(0.2));
        this.method_19322(AfwFirstPersonCameraCollision.resolve((class_1941)area, desiredCameraPos));
        afw$lastConstrainedLook = constrainedLook;
    }

    private static void afw$beginStartAlignment(UUID instanceId, float startYaw, float startPitch) {
        afw$startAlignInstanceId = instanceId;
        afw$startAlignPendingTarget = true;
        afw$startAlignActive = false;
        afw$startAlignFromYaw = startYaw;
        afw$startAlignFromPitch = startPitch;
        afw$startAlignTargetYaw = startYaw;
        afw$startAlignTargetPitch = startPitch;
        afw$startAlignTick = 0L;
    }

    private static void afw$clearStartAlignment() {
        afw$startAlignInstanceId = null;
        afw$startAlignPendingTarget = false;
        afw$startAlignActive = false;
    }

    private static class_243 afw$resolveStartAlignmentTargetLook(AfwAnimatedCameraPoseTracker.PoseSample pose) {
        class_243 targetLook = pose.headForward();
        if (targetLook == null || targetLook.method_1027() < 1.0E-8 || !targetLook.method_76470()) {
            targetLook = pose.bodyForward();
        }
        if (targetLook == null || targetLook.method_1027() < 1.0E-8 || !targetLook.method_76470()) {
            return null;
        }
        return targetLook.method_1029();
    }

    private void afw$applyThirdPersonCameraAdjustments(UUID actorUuid, UUID activeInstanceId, class_2960 activeAnimationId, class_1657 focusedPlayer, float tickDelta, long nowTick) {
        class_243 focusPos = focusedPlayer.method_5836(tickDelta);
        if (focusPos == null) {
            return;
        }
        class_243 adjustedFocus = focusPos;
        class_243 orbitTarget = AfwClientAnimationRuntime.findCameraOrbitTargetForActor(actorUuid);
        if (orbitTarget != null) {
            CameraAnimationMixin.afw$clearBodyOrbitTarget();
            class_243 horizontalDelta = new class_243(orbitTarget.field_1352 - focusPos.field_1352, 0.0, orbitTarget.field_1350 - focusPos.field_1350);
            if (horizontalDelta.method_76470() && horizontalDelta.method_1027() >= 1.0E-8) {
                adjustedFocus = focusPos.method_1019(horizontalDelta);
            }
        } else {
            class_243 bodyTarget = CameraAnimationMixin.afw$resolveBodyOrbitTarget(actorUuid, activeInstanceId, activeAnimationId, nowTick);
            if (bodyTarget != null) {
                adjustedFocus = CameraAnimationMixin.afw$resolveSmoothedOrbitTarget(activeInstanceId, activeAnimationId, focusPos, bodyTarget, nowTick, tickDelta);
            } else if (afw$currentSmoothedOrbitTarget != null && afw$currentSmoothedOrbitTarget.method_76470()) {
                adjustedFocus = afw$currentSmoothedOrbitTarget;
            } else if (activeInstanceId == null) {
                CameraAnimationMixin.afw$clearOrbitSmoothing();
            }
        }
        adjustedFocus = CameraAnimationMixin.afw$resolveClearThirdPersonFocus(focusedPlayer.method_73183(), adjustedFocus);
        float zoomDistance = AfwAnimationCameraZoom.distance(tickDelta);
        this.method_19322(adjustedFocus);
        this.method_19324(-this.method_19318(zoomDistance), 0.0f, 0.0f);
    }

    private static class_243 afw$resolveClearThirdPersonFocus(class_1937 world, class_243 desiredFocus) {
        if (world == null || desiredFocus == null || !desiredFocus.method_76470()) {
            return desiredFocus;
        }
        if (CameraAnimationMixin.afw$isThirdPersonFocusClear(world, desiredFocus)) {
            return desiredFocus;
        }
        for (int step = 1; step <= 16; ++step) {
            class_243 candidate = desiredFocus.method_1031(0.0, (double)step * 0.1, 0.0);
            if (!CameraAnimationMixin.afw$isThirdPersonFocusClear(world, candidate)) continue;
            return candidate;
        }
        for (int ring = 1; ring <= 4; ++ring) {
            double distance = (double)ring * 0.25;
            for (int i = 0; i < 8; ++i) {
                double angle = Math.PI * 2 * (double)i / 8.0;
                class_243 candidate = desiredFocus.method_1031(Math.cos(angle) * distance, 0.0, Math.sin(angle) * distance);
                if (!CameraAnimationMixin.afw$isThirdPersonFocusClear(world, candidate)) continue;
                return candidate;
            }
        }
        return desiredFocus;
    }

    private static boolean afw$isThirdPersonFocusClear(class_1937 world, class_243 focus) {
        if (world == null || focus == null || !focus.method_76470()) {
            return true;
        }
        class_238 focusBox = class_238.method_30048((class_243)focus, (double)0.25, (double)0.25, (double)0.25);
        return world.method_18026(focusBox) && !CameraAnimationMixin.afw$intersectsCameraCollisionShape(world, focusBox);
    }

    private static boolean afw$intersectsCameraCollisionShape(class_1937 world, class_238 focusBox) {
        int minX = class_3532.method_15357((double)focusBox.field_1323);
        int maxX = class_3532.method_15357((double)focusBox.field_1320);
        int minY = class_3532.method_15357((double)focusBox.field_1322);
        int maxY = class_3532.method_15357((double)focusBox.field_1325);
        int minZ = class_3532.method_15357((double)focusBox.field_1321);
        int maxZ = class_3532.method_15357((double)focusBox.field_1324);
        class_3726 shapeContext = class_3726.method_16194();
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    class_265 shape;
                    class_2338 pos = new class_2338(x, y, z);
                    class_2680 state = world.method_8320(pos);
                    if (state.method_26215() || (shape = state.method_26202((class_1922)world, pos, shapeContext)).method_1110()) continue;
                    for (class_238 shapeBox : shape.method_1090()) {
                        if (!focusBox.method_994(shapeBox.method_996(pos))) continue;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static class_243 afw$resolveBodyOrbitTarget(UUID actorUuid, UUID activeInstanceId, class_2960 activeAnimationId, long nowTick) {
        boolean animationChanged;
        if (actorUuid == null || activeInstanceId == null || activeAnimationId == null) {
            CameraAnimationMixin.afw$clearBodyOrbitTarget();
            return null;
        }
        boolean instanceChanged = !Objects.equals(activeInstanceId, afw$bodyOrbitInstanceId);
        boolean bl = animationChanged = !Objects.equals(activeAnimationId, afw$bodyOrbitAnimationId);
        if (instanceChanged || animationChanged) {
            if (instanceChanged) {
                afw$delayBodyOrbitForCurrentInstance = afw$currentSmoothedOrbitTarget == null;
            }
            afw$bodyOrbitInstanceId = activeInstanceId;
            afw$bodyOrbitAnimationId = activeAnimationId;
            afw$bodyOrbitTarget = null;
        }
        if (afw$bodyOrbitTarget != null) {
            return afw$bodyOrbitTarget;
        }
        Long startTick = AfwClientAnimationRuntime.findStartTickForActor(actorUuid);
        if (afw$delayBodyOrbitForCurrentInstance && startTick != null && nowTick - startTick < 4L) {
            return null;
        }
        AfwAnimatedCameraPoseTracker.PoseSample sample = AfwAnimatedCameraPoseTracker.resolve(actorUuid, nowTick);
        if (sample == null || !Objects.equals(sample.instanceId(), activeInstanceId) || !Objects.equals(sample.animationId(), activeAnimationId)) {
            return null;
        }
        if (afw$delayBodyOrbitForCurrentInstance && startTick != null && sample.tick() - startTick < 4L) {
            return null;
        }
        class_243 bodyPos = sample.bodyWorldPos();
        if (bodyPos == null || !bodyPos.method_76470()) {
            return null;
        }
        afw$bodyOrbitTarget = bodyPos;
        return afw$bodyOrbitTarget;
    }

    private static class_243 afw$resolveSmoothedOrbitTarget(UUID activeInstanceId, class_2960 activeAnimationId, class_243 fallbackStart, class_243 desiredTarget, long nowTick, float tickDelta) {
        boolean targetChanged;
        if (desiredTarget == null || !desiredTarget.method_76470()) {
            return fallbackStart;
        }
        boolean bl = targetChanged = afw$orbitSmoothTarget == null || afw$orbitSmoothTarget.method_1025(desiredTarget) >= 1.0E-6;
        if (!Objects.equals(activeInstanceId, afw$smoothedOrbitInstanceId) || !Objects.equals(activeAnimationId, afw$smoothedOrbitAnimationId) || targetChanged) {
            afw$smoothedOrbitInstanceId = activeInstanceId;
            afw$smoothedOrbitAnimationId = activeAnimationId;
            afw$orbitSmoothStart = afw$currentSmoothedOrbitTarget != null ? afw$currentSmoothedOrbitTarget : fallbackStart;
            afw$orbitSmoothTarget = desiredTarget;
            afw$orbitSmoothStartTick = nowTick;
        }
        if (afw$orbitSmoothStart == null || afw$orbitSmoothTarget == null) {
            afw$currentSmoothedOrbitTarget = desiredTarget;
            return desiredTarget;
        }
        float progress = class_3532.method_15363((float)(((float)(nowTick - afw$orbitSmoothStartTick) + tickDelta) / 6.0f), (float)0.0f, (float)1.0f);
        float eased = CameraAnimationMixin.afw$smootherStep(progress);
        afw$currentSmoothedOrbitTarget = new class_243(class_3532.method_16436((double)eased, (double)CameraAnimationMixin.afw$orbitSmoothStart.field_1352, (double)CameraAnimationMixin.afw$orbitSmoothTarget.field_1352), class_3532.method_16436((double)eased, (double)CameraAnimationMixin.afw$orbitSmoothStart.field_1351, (double)CameraAnimationMixin.afw$orbitSmoothTarget.field_1351), class_3532.method_16436((double)eased, (double)CameraAnimationMixin.afw$orbitSmoothStart.field_1350, (double)CameraAnimationMixin.afw$orbitSmoothTarget.field_1350));
        if (progress >= 1.0f) {
            afw$orbitSmoothStart = afw$orbitSmoothTarget;
        }
        return afw$currentSmoothedOrbitTarget;
    }

    private static void afw$clearBodyOrbitTarget() {
        afw$bodyOrbitInstanceId = null;
        afw$bodyOrbitAnimationId = null;
        afw$bodyOrbitTarget = null;
        afw$delayBodyOrbitForCurrentInstance = false;
    }

    private static void afw$clearOrbitSmoothing() {
        afw$smoothedOrbitInstanceId = null;
        afw$smoothedOrbitAnimationId = null;
        afw$orbitSmoothStart = null;
        afw$orbitSmoothTarget = null;
        afw$currentSmoothedOrbitTarget = null;
        afw$orbitSmoothStartTick = 0L;
    }

    private static float afw$lerpAngleDegrees(float delta, float start, float end) {
        return start + class_3532.method_15393((float)(end - start)) * delta;
    }

    private static float afw$smootherStep(float value) {
        float clamped = class_3532.method_15363((float)value, (float)0.0f, (float)1.0f);
        return clamped * clamped * clamped * (clamped * (clamped * 6.0f - 15.0f) + 10.0f);
    }

    private static void afw$resetAnimationCameraState(boolean firstPerson) {
        afw$lastValidPosedPose = null;
        afw$lastConstrainedLook = null;
        afw$lastActiveInstanceId = null;
        afw$startAlignInstanceId = null;
        afw$startAlignPendingTarget = false;
        afw$startAlignActive = false;
        CameraAnimationMixin.afw$clearBodyOrbitTarget();
        CameraAnimationMixin.afw$clearOrbitSmoothing();
        afw$wasFirstPersonLastFrame = firstPerson;
    }
}

