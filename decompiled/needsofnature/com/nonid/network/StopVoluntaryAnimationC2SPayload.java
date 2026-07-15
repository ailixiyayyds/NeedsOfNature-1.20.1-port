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
package com.nonid.network;

import java.util.UUID;
import net.minecraft.class_2960;
import net.minecraft.class_4844;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9139;

public record StopVoluntaryAnimationC2SPayload(UUID instanceId) implements class_8710
{
    public static final class_2960 STOP_VOLUNTARY_ANIMATION_ID = class_2960.method_60655((String)"needsofnature", (String)"stop_voluntary_animation");
    public static final class_8710.class_9154<StopVoluntaryAnimationC2SPayload> ID = new class_8710.class_9154(STOP_VOLUNTARY_ANIMATION_ID);
    public static final class_9139<class_9129, StopVoluntaryAnimationC2SPayload> CODEC = class_9139.method_56434((class_9139)class_4844.field_48453, StopVoluntaryAnimationC2SPayload::instanceId, StopVoluntaryAnimationC2SPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

