/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodec
 */
package com.nonid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record MessStateS2CPayload(UUID playerUuid, int vMess, int aMess, int mMess, int tintRgb, int liquidStored, int liquidCapacity) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"mess_state");
    public static final CustomPayload.Id<MessStateS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, MessStateS2CPayload> CODEC = PacketCodec.ofStatic((buf, payload) -> {
        buf.writeUuid(payload.playerUuid);
        buf.writeVarInt(payload.vMess);
        buf.writeVarInt(payload.aMess);
        buf.writeVarInt(payload.mMess);
        buf.writeVarInt(payload.tintRgb);
        buf.writeVarInt(payload.liquidStored);
        buf.writeVarInt(payload.liquidCapacity);
    }, buf -> new MessStateS2CPayload(buf.readUuid(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt()));

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

