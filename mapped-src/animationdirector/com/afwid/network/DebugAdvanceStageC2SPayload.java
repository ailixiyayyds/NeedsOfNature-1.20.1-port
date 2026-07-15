/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.Uuids
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodec
 */
package com.afwid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record DebugAdvanceStageC2SPayload(UUID instanceId) implements CustomPayload
{
    public static final Identifier DEBUG_ADVANCE_STAGE_ID = Identifier.of((String)"animationframework", (String)"debug_advance_stage");
    public static final CustomPayload.Id<DebugAdvanceStageC2SPayload> ID = new CustomPayload.Id(DEBUG_ADVANCE_STAGE_ID);
    public static final PacketCodec<RegistryByteBuf, DebugAdvanceStageC2SPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, DebugAdvanceStageC2SPayload::instanceId, DebugAdvanceStageC2SPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

