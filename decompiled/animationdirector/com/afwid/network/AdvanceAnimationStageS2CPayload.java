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

public record AdvanceAnimationStageS2CPayload(UUID instanceId, long advanceTick, int stageIndex) implements class_8710
{
    public static final class_2960 ADVANCE_STAGE_ID = class_2960.method_60655((String)"animationframework", (String)"advance_stage");
    public static final class_8710.class_9154<AdvanceAnimationStageS2CPayload> ID = new class_8710.class_9154(ADVANCE_STAGE_ID);
    public static final class_9139<class_9129, AdvanceAnimationStageS2CPayload> CODEC = class_9139.method_56436((class_9139)class_4844.field_48453, AdvanceAnimationStageS2CPayload::instanceId, (class_9139)class_9135.field_54505, AdvanceAnimationStageS2CPayload::advanceTick, (class_9139)class_9135.field_49675, AdvanceAnimationStageS2CPayload::stageIndex, AdvanceAnimationStageS2CPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

