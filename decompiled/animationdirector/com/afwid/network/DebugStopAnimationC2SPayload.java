/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_4844
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9129
 *  net.minecraft.class_9139
 */
package com.afwid.network;

import java.util.UUID;
import net.minecraft.class_2960;
import net.minecraft.class_4844;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9139;

public record DebugStopAnimationC2SPayload(UUID instanceId) implements class_8710
{
    public static final class_2960 DEBUG_STOP_ANIMATION_ID = class_2960.method_60655((String)"animationframework", (String)"debug_stop_animation");
    public static final class_8710.class_9154<DebugStopAnimationC2SPayload> ID = new class_8710.class_9154(DEBUG_STOP_ANIMATION_ID);
    public static final class_9139<class_9129, DebugStopAnimationC2SPayload> CODEC = class_9139.method_56434((class_9139)class_4844.field_48453, DebugStopAnimationC2SPayload::instanceId, DebugStopAnimationC2SPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

