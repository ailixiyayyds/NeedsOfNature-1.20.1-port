/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_4844
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9129
 *  net.minecraft.class_9135
 *  net.minecraft.class_9139
 */
package com.afwid.network;

import java.util.UUID;
import net.minecraft.class_2960;
import net.minecraft.class_4844;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record AdjustAnimationSpeedC2SPayload(UUID instanceId, double multiplier) implements class_8710
{
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"animationframework", (String)"adjust_animation_speed");
    public static final class_8710.class_9154<AdjustAnimationSpeedC2SPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, AdjustAnimationSpeedC2SPayload> CODEC = class_9139.method_56435((class_9139)class_4844.field_48453, AdjustAnimationSpeedC2SPayload::instanceId, (class_9139)class_9135.field_48553, AdjustAnimationSpeedC2SPayload::multiplier, AdjustAnimationSpeedC2SPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

