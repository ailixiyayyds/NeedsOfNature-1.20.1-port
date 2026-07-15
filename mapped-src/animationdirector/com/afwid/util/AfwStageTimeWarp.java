/*
 * Decompiled with CFR 0.152.
 */
package com.afwid.util;

public final class AfwStageTimeWarp {
    private static final double EPSILON_TICKS = 1.0E-4;

    private AfwStageTimeWarp() {
    }

    public static double offsetSecondsToTicks(double offsetSeconds) {
        if (!Double.isFinite(offsetSeconds)) {
            return 0.0;
        }
        return offsetSeconds * 20.0;
    }

    public static double clampOffsetSeconds(double cycleTicks, double offsetSeconds) {
        return AfwStageTimeWarp.clampOffsetTicks(cycleTicks, AfwStageTimeWarp.offsetSecondsToTicks(offsetSeconds)) / 20.0;
    }

    public static double clampOffsetTicks(double cycleTicks, double offsetTicks) {
        if (!Double.isFinite(cycleTicks) || cycleTicks <= 0.0) {
            return 0.0;
        }
        if (!Double.isFinite(offsetTicks)) {
            return 0.0;
        }
        double midpoint = cycleTicks * 0.5;
        double minOffset = -midpoint + 1.0E-4;
        double maxOffset = midpoint - 1.0E-4;
        return Math.max(minOffset, Math.min(maxOffset, offsetTicks));
    }

    public static boolean hasWarp(double cycleTicks, double offsetTicks) {
        if (!Double.isFinite(cycleTicks) || cycleTicks <= 0.0) {
            return false;
        }
        return Math.abs(AfwStageTimeWarp.clampOffsetTicks(cycleTicks, offsetTicks)) > 1.0E-4;
    }

    public static double authoredToWarpedCycleTicks(double authoredTicks, double cycleTicks, double offsetTicks) {
        if (!AfwStageTimeWarp.hasWarp(cycleTicks, offsetTicks)) {
            return AfwStageTimeWarp.clampCyclePosition(authoredTicks, cycleTicks);
        }
        double position = AfwStageTimeWarp.clampCyclePosition(authoredTicks, cycleTicks);
        double midpoint = cycleTicks * 0.5;
        double shiftedMidpoint = midpoint + AfwStageTimeWarp.clampOffsetTicks(cycleTicks, offsetTicks);
        if (position <= midpoint) {
            return position * (shiftedMidpoint / midpoint);
        }
        return shiftedMidpoint + (position - midpoint) * ((cycleTicks - shiftedMidpoint) / (cycleTicks - midpoint));
    }

    public static double warpedToAuthoredCycleTicks(double warpedTicks, double cycleTicks, double offsetTicks) {
        double midpoint;
        double shiftedMidpoint;
        if (!AfwStageTimeWarp.hasWarp(cycleTicks, offsetTicks)) {
            return AfwStageTimeWarp.clampCyclePosition(warpedTicks, cycleTicks);
        }
        double position = AfwStageTimeWarp.clampCyclePosition(warpedTicks, cycleTicks);
        if (position <= (shiftedMidpoint = (midpoint = cycleTicks * 0.5) + AfwStageTimeWarp.clampOffsetTicks(cycleTicks, offsetTicks))) {
            return position * (midpoint / shiftedMidpoint);
        }
        return midpoint + (position - shiftedMidpoint) * ((cycleTicks - midpoint) / (cycleTicks - shiftedMidpoint));
    }

    private static double clampCyclePosition(double ticks, double cycleTicks) {
        if (!Double.isFinite(cycleTicks) || cycleTicks <= 0.0) {
            return 0.0;
        }
        if (!Double.isFinite(ticks)) {
            return 0.0;
        }
        double position = ticks % cycleTicks;
        if (position < 0.0) {
            position += cycleTicks;
        }
        if (position > cycleTicks) {
            return cycleTicks;
        }
        return position;
    }
}

