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

import java.util.List;
import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record DebugJoinAnimationC2SPayload(UUID instanceId, List<Integer> actorEntityIds) implements CustomPayload
{
    public static final Identifier DEBUG_JOIN_ANIMATION_ID = Identifier.of((String)"animationframework", (String)"debug_join_animation");
    public static final CustomPayload.Id<DebugJoinAnimationC2SPayload> ID = new CustomPayload.Id(DEBUG_JOIN_ANIMATION_ID);
    public static final PacketCodec<RegistryByteBuf, DebugJoinAnimationC2SPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, DebugJoinAnimationC2SPayload::instanceId, (PacketCodec)PacketCodecs.INTEGER.collect(PacketCodecs.toList((int)32)), DebugJoinAnimationC2SPayload::actorEntityIds, DebugJoinAnimationC2SPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

