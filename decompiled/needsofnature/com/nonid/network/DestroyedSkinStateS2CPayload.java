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

import java.util.UUID;
import net.minecraft.class_2960;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9139;

public record DestroyedSkinStateS2CPayload(UUID playerUuid, int stage) implements class_8710
{
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"needsofnature", (String)"destroyed_skin_state");
    public static final class_8710.class_9154<DestroyedSkinStateS2CPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, DestroyedSkinStateS2CPayload> CODEC = class_9139.method_56437((buf, payload) -> {
        buf.method_10797(payload.playerUuid());
        buf.method_10804(payload.stage());
    }, buf -> new DestroyedSkinStateS2CPayload(buf.method_10790(), buf.method_10816()));

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

