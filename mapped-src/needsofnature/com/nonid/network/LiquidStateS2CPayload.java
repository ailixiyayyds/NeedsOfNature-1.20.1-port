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

public record LiquidStateS2CPayload(int stored, int capacity, int tintRgb) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"liquid_state");
    public static final CustomPayload.Id<LiquidStateS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, LiquidStateS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)PacketCodecs.INTEGER, LiquidStateS2CPayload::stored, (PacketCodec)PacketCodecs.INTEGER, LiquidStateS2CPayload::capacity, (PacketCodec)PacketCodecs.INTEGER, LiquidStateS2CPayload::tintRgb, LiquidStateS2CPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

