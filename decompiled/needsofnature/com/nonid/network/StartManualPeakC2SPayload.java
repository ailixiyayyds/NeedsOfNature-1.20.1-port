/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9129
 *  net.minecraft.class_9139
 */
package com.nonid.network;

import net.minecraft.class_2960;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9139;

public record StartManualPeakC2SPayload() implements class_8710
{
    public static final class_2960 START_MANUAL_PEAK_ID = class_2960.method_60655((String)"needsofnature", (String)"start_manual_peak");
    public static final class_8710.class_9154<StartManualPeakC2SPayload> ID = new class_8710.class_9154(START_MANUAL_PEAK_ID);
    public static final class_9139<class_9129, StartManualPeakC2SPayload> CODEC = class_9139.method_56431((Object)new StartManualPeakC2SPayload());

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

