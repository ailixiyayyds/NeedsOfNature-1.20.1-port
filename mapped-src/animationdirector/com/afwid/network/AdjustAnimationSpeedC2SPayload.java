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
package com.afwid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record AdjustAnimationSpeedC2SPayload(UUID instanceId, double multiplier) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"animationframework", (String)"adjust_animation_speed");
    public static final CustomPayload.Id<AdjustAnimationSpeedC2SPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, AdjustAnimationSpeedC2SPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, AdjustAnimationSpeedC2SPayload::instanceId, (PacketCodec)PacketCodecs.DOUBLE, AdjustAnimationSpeedC2SPayload::multiplier, AdjustAnimationSpeedC2SPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

