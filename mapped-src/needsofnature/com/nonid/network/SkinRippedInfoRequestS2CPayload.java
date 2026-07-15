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

import net.minecraft.util.Identifier;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record SkinRippedInfoRequestS2CPayload() implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"skin_ripped_info_request");
    public static final CustomPayload.Id<SkinRippedInfoRequestS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, SkinRippedInfoRequestS2CPayload> CODEC = PacketCodec.unit((Object)new SkinRippedInfoRequestS2CPayload());

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

