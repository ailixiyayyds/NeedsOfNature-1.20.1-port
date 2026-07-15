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

public record AnimationSpeedUpdateS2CPayload(UUID instanceId, double speed) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"animationframework", (String)"animation_speed_update");
    public static final CustomPayload.Id<AnimationSpeedUpdateS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, AnimationSpeedUpdateS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, AnimationSpeedUpdateS2CPayload::instanceId, (PacketCodec)PacketCodecs.DOUBLE, AnimationSpeedUpdateS2CPayload::speed, AnimationSpeedUpdateS2CPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

