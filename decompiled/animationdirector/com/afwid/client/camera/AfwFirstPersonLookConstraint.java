/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.client.camera;

import com.afwid.client.camera.AfwAnimatedCameraPoseTracker;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import org.jetbrains.annotations.Nullable;

public final class AfwFirstPersonLookConstraint {
    private static final double BACKWARD_HARD_DEGREES = 160.0;
    private static final double DOWNWARD_HARD_DEGREES = 60.0;
    private static final double BACKWARD_FULL_LIMIT_UPWARD_PITCH_DEGREES = 20.0;
    private static final double BACKWARD_DISABLE_UPWARD_PITCH_DEGREES = 40.0;
    private static final double MIN_HORIZONTAL_LENGTH_FOR_YAW = 0.001;

    private AfwFirstPersonLookConstraint() {
    }

    public static class_243 constrain(class_243 desiredLook, @Nullable AfwAnimatedCameraPoseTracker.PoseSample pose, @Nullable class_243 previousConstrainedLook) {
        if (desiredLook == null) {
            return class_243.field_1353;
        }
        class_243 normalizedLook = desiredLook.method_1029();
        if (pose == null) {
            return normalizedLook;
        }
        class_243 bodyForward = AfwFirstPersonLookConstraint.normalize(pose.bodyForward());
        class_243 bodyUp = AfwFirstPersonLookConstraint.normalize(pose.bodyUp());
        class_243 bodyRight = AfwFirstPersonLookConstraint.normalize(pose.bodyRight());
        if (bodyForward == null || bodyUp == null || bodyRight == null) {
            return normalizedLook;
        }
        LocalAngles desiredAngles = AfwFirstPersonLookConstraint.toLocalAngles(normalizedLook, bodyForward, bodyUp, bodyRight);
        if (desiredAngles == null) {
            return normalizedLook;
        }
        double localYaw = desiredAngles.yawDegrees();
        double localPitch = desiredAngles.pitchDegrees();
        LocalAngles previousAngles = AfwFirstPersonLookConstraint.toLocalAngles(previousConstrainedLook, bodyForward, bodyUp, bodyRight);
        if (previousAngles != null) {
            localYaw = AfwFirstPersonLookConstraint.unwrapNear(localYaw, previousAngles.yawDegrees());
        }
        double constrainedPitch = AfwFirstPersonLookConstraint.constrainDownward(localPitch, 60.0);
        double effectiveBackwardLimit = AfwFirstPersonLookConstraint.computeEffectiveBackwardLimit(localPitch);
        double constrainedYaw = AfwFirstPersonLookConstraint.constrainSymmetric(localYaw, effectiveBackwardLimit);
        if (Math.abs(constrainedYaw - localYaw) < 1.0E-4 && Math.abs(constrainedPitch - localPitch) < 1.0E-4) {
            return normalizedLook;
        }
        double yawRad = Math.toRadians(constrainedYaw);
        double pitchRad = Math.toRadians(constrainedPitch);
        double correctedHorizontal = Math.cos(pitchRad);
        class_243 correctedLocal = new class_243(Math.sin(yawRad) * correctedHorizontal, Math.sin(pitchRad), Math.cos(yawRad) * correctedHorizontal);
        class_243 correctedWorld = bodyRight.method_1021(correctedLocal.field_1352).method_1019(bodyUp.method_1021(correctedLocal.field_1351)).method_1019(bodyForward.method_1021(correctedLocal.field_1350));
        return correctedWorld.method_1029();
    }

    public static float toMinecraftYaw(class_243 lookVector, float fallbackYaw) {
        class_243 normalized;
        class_243 class_2432 = normalized = lookVector == null ? class_243.field_1353 : lookVector.method_1029();
        if (normalized.method_37268() <= 0.001) {
            return fallbackYaw;
        }
        return (float)Math.toDegrees(Math.atan2(-normalized.field_1352, normalized.field_1350));
    }

    public static float toMinecraftPitch(class_243 lookVector) {
        class_243 normalized = lookVector == null ? class_243.field_1353 : lookVector.method_1029();
        double clampedY = class_3532.method_15350((double)normalized.field_1351, (double)-1.0, (double)1.0);
        return (float)(-Math.toDegrees(Math.asin(clampedY)));
    }

    private static double constrainSymmetric(double angleDegrees, double hardLimit) {
        double sign = Math.signum(angleDegrees);
        double absolute = Math.abs(angleDegrees);
        if (absolute <= hardLimit) {
            return angleDegrees;
        }
        return sign * hardLimit;
    }

    private static double constrainDownward(double pitchDegrees, double hardLimit) {
        double downward = Math.max(0.0, -pitchDegrees);
        if (downward <= hardLimit) {
            return pitchDegrees;
        }
        return -hardLimit;
    }

    private static double computeEffectiveBackwardLimit(double localPitchDegrees) {
        if (localPitchDegrees <= 20.0) {
            return 160.0;
        }
        if (localPitchDegrees >= 40.0) {
            return 180.0;
        }
        double t = (localPitchDegrees - 20.0) / Math.max(1.0E-6, 20.0);
        return class_3532.method_16436((double)t, (double)160.0, (double)180.0);
    }

    @Nullable
    private static class_243 normalize(@Nullable class_243 vector) {
        if (vector == null) {
            return null;
        }
        if (vector.method_1027() < 1.0E-8) {
            return null;
        }
        class_243 normalized = vector.method_1029();
        if (!normalized.method_76470()) {
            return null;
        }
        return normalized;
    }

    @Nullable
    private static LocalAngles toLocalAngles(@Nullable class_243 look, class_243 bodyForward, class_243 bodyUp, class_243 bodyRight) {
        class_243 normalizedLook = AfwFirstPersonLookConstraint.normalize(look);
        if (normalizedLook == null) {
            return null;
        }
        double localX = normalizedLook.method_1026(bodyRight);
        double localY = normalizedLook.method_1026(bodyUp);
        double localZ = normalizedLook.method_1026(bodyForward);
        double horizontalLength = Math.sqrt(localX * localX + localZ * localZ);
        double localYaw = Math.toDegrees(Math.atan2(localX, localZ));
        double localPitch = Math.toDegrees(Math.atan2(localY, Math.max(1.0E-6, horizontalLength)));
        return new LocalAngles(localYaw, localPitch);
    }

    private static double unwrapNear(double angleDegrees, double referenceDegrees) {
        double unwrapped = angleDegrees;
        while (unwrapped - referenceDegrees > 180.0) {
            unwrapped -= 360.0;
        }
        while (unwrapped - referenceDegrees < -180.0) {
            unwrapped += 360.0;
        }
        return unwrapped;
    }

    private record LocalAngles(double yawDegrees, double pitchDegrees) {
    }
}

