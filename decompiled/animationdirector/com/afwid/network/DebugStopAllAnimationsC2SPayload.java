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
package com.afwid.network;

import net.minecraft.class_2960;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9139;

public record DebugStopAllAnimationsC2SPayload() implements class_8710
{
    public static final class_2960 DEBUG_STOP_ALL_ID = class_2960.method_60655((String)"animationframework", (String)"debug_stop_all");
    public static final class_8710.class_9154<DebugStopAllAnimationsC2SPayload> ID = new class_8710.class_9154(DEBUG_STOP_ALL_ID);
    public static final class_9139<class_9129, DebugStopAllAnimationsC2SPayload> CODEC = class_9139.method_56431((Object)new DebugStopAllAnimationsC2SPayload());

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

