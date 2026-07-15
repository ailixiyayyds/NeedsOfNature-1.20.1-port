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
package com.nonid.network;

import java.util.UUID;
import net.minecraft.class_2960;
import net.minecraft.class_4844;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record StageProgressS2CPayload(UUID instanceId, class_2960 stageAnimationId, long startTick, long endTick, Mode mode) implements class_8710
{
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"needsofnature", (String)"stage_progress");
    public static final class_8710.class_9154<StageProgressS2CPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, StageProgressS2CPayload> CODEC = class_9139.method_56906((class_9139)class_4844.field_48453, StageProgressS2CPayload::instanceId, (class_9139)class_2960.field_48267.method_56430(), StageProgressS2CPayload::stageAnimationId, (class_9139)class_9135.field_54505, StageProgressS2CPayload::startTick, (class_9139)class_9135.field_54505, StageProgressS2CPayload::endTick, (class_9139)class_9135.field_49675, payload -> payload.mode().ordinal(), StageProgressS2CPayload::fromPacket);

    public StageProgressS2CPayload {
        mode = mode == null ? Mode.HIDDEN : mode;
    }

    private static StageProgressS2CPayload fromPacket(UUID instanceId, class_2960 stageAnimationId, long startTick, long endTick, int modeId) {
        return new StageProgressS2CPayload(instanceId, stageAnimationId, startTick, endTick, Mode.fromId(modeId));
    }

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }

    public static enum Mode {
        HIDDEN,
        SERVER_WINDOW,
        AFW_TIMELINE;


        private static Mode fromId(int id) {
            Mode[] values = Mode.values();
            return id >= 0 && id < values.length ? values[id] : HIDDEN;
        }
    }
}

