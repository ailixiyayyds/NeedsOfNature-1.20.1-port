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

public record DestroyedSkinMaskUploadC2SPayload(int stage, byte[] pngBytes) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"destroyed_skin_mask_upload");
    public static final CustomPayload.Id<DestroyedSkinMaskUploadC2SPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, DestroyedSkinMaskUploadC2SPayload> CODEC = PacketCodec.tuple((PacketCodec)PacketCodecs.VAR_INT, DestroyedSkinMaskUploadC2SPayload::stage, (PacketCodec)PacketCodecs.byteArray((int)65536), DestroyedSkinMaskUploadC2SPayload::pngBytes, DestroyedSkinMaskUploadC2SPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

