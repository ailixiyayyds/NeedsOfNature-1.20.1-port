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

public record SkinRippedInfoRequestS2CPayload() implements class_8710
{
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"needsofnature", (String)"skin_ripped_info_request");
    public static final class_8710.class_9154<SkinRippedInfoRequestS2CPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, SkinRippedInfoRequestS2CPayload> CODEC = class_9139.method_56431((Object)new SkinRippedInfoRequestS2CPayload());

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

