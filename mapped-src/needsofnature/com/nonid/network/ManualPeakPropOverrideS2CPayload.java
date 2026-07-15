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
package com.nonid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record ManualPeakPropOverrideS2CPayload(UUID instanceId, UUID actorUuid, Identifier itemId) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"manual_peak_prop_override");
    public static final CustomPayload.Id<ManualPeakPropOverrideS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, ManualPeakPropOverrideS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, ManualPeakPropOverrideS2CPayload::instanceId, (PacketCodec)Uuids.PACKET_CODEC, ManualPeakPropOverrideS2CPayload::actorUuid, (PacketCodec)Identifier.PACKET_CODEC.cast(), ManualPeakPropOverrideS2CPayload::itemId, ManualPeakPropOverrideS2CPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

