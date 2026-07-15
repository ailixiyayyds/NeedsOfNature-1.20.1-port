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

import java.util.List;
import java.util.UUID;
import net.minecraft.class_2960;
import net.minecraft.class_4844;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record DebugJoinAnimationC2SPayload(UUID instanceId, List<Integer> actorEntityIds) implements class_8710
{
    public static final class_2960 DEBUG_JOIN_ANIMATION_ID = class_2960.method_60655((String)"animationframework", (String)"debug_join_animation");
    public static final class_8710.class_9154<DebugJoinAnimationC2SPayload> ID = new class_8710.class_9154(DEBUG_JOIN_ANIMATION_ID);
    public static final class_9139<class_9129, DebugJoinAnimationC2SPayload> CODEC = class_9139.method_56435((class_9139)class_4844.field_48453, DebugJoinAnimationC2SPayload::instanceId, (class_9139)class_9135.field_49675.method_56433(class_9135.method_58000((int)32)), DebugJoinAnimationC2SPayload::actorEntityIds, DebugJoinAnimationC2SPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

