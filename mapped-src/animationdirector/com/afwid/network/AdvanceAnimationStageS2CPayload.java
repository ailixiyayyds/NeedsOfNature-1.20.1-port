/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.Uuids
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodecs
 *  net.minecraft.network.codec.PacketCodec
 */
package com.afwid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record AdvanceAnimationStageS2CPayload(UUID instanceId, long advanceTick, int stageIndex) implements CustomPayload
{
    public static final Identifier ADVANCE_STAGE_ID = Identifier.of((String)"animationframework", (String)"advance_stage");
    public static final CustomPayload.Id<AdvanceAnimationStageS2CPayload> ID = new CustomPayload.Id(ADVANCE_STAGE_ID);
    public static final PacketCodec<RegistryByteBuf, AdvanceAnimationStageS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, AdvanceAnimationStageS2CPayload::instanceId, (PacketCodec)PacketCodecs.LONG, AdvanceAnimationStageS2CPayload::advanceTick, (PacketCodec)PacketCodecs.INTEGER, AdvanceAnimationStageS2CPayload::stageIndex, AdvanceAnimationStageS2CPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

