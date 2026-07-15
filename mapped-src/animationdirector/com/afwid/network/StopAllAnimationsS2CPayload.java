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
package com.afwid.network;

import net.minecraft.util.Identifier;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record StopAllAnimationsS2CPayload(long stopTick) implements CustomPayload
{
    public static final Identifier STOP_ALL_ID = Identifier.of((String)"animationframework", (String)"stop_all_animations");
    public static final CustomPayload.Id<StopAllAnimationsS2CPayload> ID = new CustomPayload.Id(STOP_ALL_ID);
    public static final PacketCodec<RegistryByteBuf, StopAllAnimationsS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)PacketCodecs.LONG, StopAllAnimationsS2CPayload::stopTick, StopAllAnimationsS2CPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

