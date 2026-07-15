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

public record AnimationSpeedUpdateS2CPayload(UUID instanceId, double speed) implements class_8710
{
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"animationframework", (String)"animation_speed_update");
    public static final class_8710.class_9154<AnimationSpeedUpdateS2CPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, AnimationSpeedUpdateS2CPayload> CODEC = class_9139.method_56435((class_9139)class_4844.field_48453, AnimationSpeedUpdateS2CPayload::instanceId, (class_9139)class_9135.field_48553, AnimationSpeedUpdateS2CPayload::speed, AnimationSpeedUpdateS2CPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

