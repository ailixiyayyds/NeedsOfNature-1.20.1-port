/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9129
 *  net.minecraft.class_9135
 *  net.minecraft.class_9139
 */
package com.afwid.network;

import net.minecraft.class_2960;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record StopAllAnimationsS2CPayload(long stopTick) implements class_8710
{
    public static final class_2960 STOP_ALL_ID = class_2960.method_60655((String)"animationframework", (String)"stop_all_animations");
    public static final class_8710.class_9154<StopAllAnimationsS2CPayload> ID = new class_8710.class_9154(STOP_ALL_ID);
    public static final class_9139<class_9129, StopAllAnimationsS2CPayload> CODEC = class_9139.method_56434((class_9139)class_9135.field_54505, StopAllAnimationsS2CPayload::stopTick, StopAllAnimationsS2CPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

