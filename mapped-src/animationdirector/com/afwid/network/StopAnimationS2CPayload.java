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

public record StopAnimationS2CPayload(UUID instanceId, long stopTick) implements CustomPayload
{
    public static final Identifier STOP_ANIMATION_ID = Identifier.of((String)"animationframework", (String)"stop_animation");
    public static final CustomPayload.Id<StopAnimationS2CPayload> ID = new CustomPayload.Id(STOP_ANIMATION_ID);
    public static final PacketCodec<RegistryByteBuf, StopAnimationS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, StopAnimationS2CPayload::instanceId, (PacketCodec)PacketCodecs.LONG, StopAnimationS2CPayload::stopTick, StopAnimationS2CPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

