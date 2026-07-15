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

public record ManualPeakPropOverrideS2CPayload(UUID instanceId, UUID actorUuid, class_2960 itemId) implements class_8710
{
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"needsofnature", (String)"manual_peak_prop_override");
    public static final class_8710.class_9154<ManualPeakPropOverrideS2CPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, ManualPeakPropOverrideS2CPayload> CODEC = class_9139.method_56436((class_9139)class_4844.field_48453, ManualPeakPropOverrideS2CPayload::instanceId, (class_9139)class_4844.field_48453, ManualPeakPropOverrideS2CPayload::actorUuid, (class_9139)class_2960.field_48267.method_56430(), ManualPeakPropOverrideS2CPayload::itemId, ManualPeakPropOverrideS2CPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

