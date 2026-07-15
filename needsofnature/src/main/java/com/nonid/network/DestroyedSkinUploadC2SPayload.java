/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodecs
 *  net.minecraft.network.codec.PacketCodec
 */
package com.nonid.network;

import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;

public record DestroyedSkinUploadC2SPayload(byte[] pngBytes) implements NonPacket
{
    public static final int MAX_BYTES = 65536;
    public static final Identifier ID_RAW = new Identifier("needsofnature", "destroyed_skin_upload");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeByteArray(pngBytes);
    }

    public static DestroyedSkinUploadC2SPayload read(PacketByteBuf buf) {
        return new DestroyedSkinUploadC2SPayload(buf.readByteArray(MAX_BYTES));
    }
}

