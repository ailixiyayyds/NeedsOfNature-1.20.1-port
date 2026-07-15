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
package com.nonid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record AccessoryShedStateS2CPayload(UUID playerUuid, boolean shedV, boolean shedA, boolean forcedV, boolean forcedA) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"accessory_shed_state");
    public static final CustomPayload.Id<AccessoryShedStateS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, AccessoryShedStateS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, AccessoryShedStateS2CPayload::playerUuid, (PacketCodec)PacketCodecs.BOOLEAN, AccessoryShedStateS2CPayload::shedV, (PacketCodec)PacketCodecs.BOOLEAN, AccessoryShedStateS2CPayload::shedA, (PacketCodec)PacketCodecs.BOOLEAN, AccessoryShedStateS2CPayload::forcedV, (PacketCodec)PacketCodecs.BOOLEAN, AccessoryShedStateS2CPayload::forcedA, AccessoryShedStateS2CPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

