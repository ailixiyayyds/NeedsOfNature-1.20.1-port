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
import net.minecraft.network.PacketByteBuf;

public record MessStateS2CPayload(UUID playerUuid, int vMess, int aMess, int mMess, int tintRgb, int liquidStored, int liquidCapacity) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "mess_state");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeUuid(playerUuid);
        buf.writeVarInt(vMess);
        buf.writeVarInt(aMess);
        buf.writeVarInt(mMess);
        buf.writeVarInt(tintRgb);
        buf.writeVarInt(liquidStored);
        buf.writeVarInt(liquidCapacity);
    }

    public static MessStateS2CPayload read(PacketByteBuf buf) {
        return new MessStateS2CPayload(buf.readUuid(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
    }
}

