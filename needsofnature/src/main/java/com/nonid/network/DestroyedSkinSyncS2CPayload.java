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

public record DestroyedSkinSyncS2CPayload(UUID playerUuid, int stage, byte[] pngBytes) implements NonPacket
{
    public static final int MAX_BYTES = 65536;
    public static final Identifier ID_RAW = new Identifier("needsofnature", "destroyed_skin_sync");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeUuid(playerUuid);
        buf.writeVarInt(stage);
        buf.writeByteArray(pngBytes);
    }

    public static DestroyedSkinSyncS2CPayload read(PacketByteBuf buf) {
        return new DestroyedSkinSyncS2CPayload(buf.readUuid(), buf.readVarInt(), buf.readByteArray(MAX_BYTES));
    }
}

