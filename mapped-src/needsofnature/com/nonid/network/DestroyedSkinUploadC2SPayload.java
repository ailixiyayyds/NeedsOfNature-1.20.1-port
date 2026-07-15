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
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record DestroyedSkinUploadC2SPayload(byte[] pngBytes) implements CustomPayload
{
    public static final int MAX_BYTES = 65536;
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"destroyed_skin_upload");
    public static final CustomPayload.Id<DestroyedSkinUploadC2SPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, DestroyedSkinUploadC2SPayload> CODEC = PacketCodec.tuple((PacketCodec)PacketCodecs.byteArray((int)65536), DestroyedSkinUploadC2SPayload::pngBytes, DestroyedSkinUploadC2SPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

