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

public record RepairDestroyedSkinC2SPayload() implements class_8710
{
    public static final class_2960 REPAIR_DESTROYED_SKIN_ID = class_2960.method_60655((String)"needsofnature", (String)"repair_destroyed_skin");
    public static final class_8710.class_9154<RepairDestroyedSkinC2SPayload> ID = new class_8710.class_9154(REPAIR_DESTROYED_SKIN_ID);
    public static final class_9139<class_9129, RepairDestroyedSkinC2SPayload> CODEC = class_9139.method_56431((Object)new RepairDestroyedSkinC2SPayload());

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

