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

import java.util.List;
import net.minecraft.util.Identifier;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record DebugStartAnimationC2SPayload(List<Integer> actorEntityIds, String damageBehaviorId, boolean ignoreAttackers, int anchorEntityId) implements CustomPayload
{
    public static final Identifier DEBUG_START_ANIMATION_ID = Identifier.of((String)"animationframework", (String)"debug_start_animation");
    public static final CustomPayload.Id<DebugStartAnimationC2SPayload> ID = new CustomPayload.Id(DEBUG_START_ANIMATION_ID);
    public static final PacketCodec<RegistryByteBuf, DebugStartAnimationC2SPayload> CODEC = PacketCodec.tuple((PacketCodec)PacketCodecs.INTEGER.collect(PacketCodecs.toList((int)32)), DebugStartAnimationC2SPayload::actorEntityIds, (PacketCodec)PacketCodecs.STRING, DebugStartAnimationC2SPayload::damageBehaviorId, (PacketCodec)PacketCodecs.BOOLEAN, DebugStartAnimationC2SPayload::ignoreAttackers, (PacketCodec)PacketCodecs.INTEGER, DebugStartAnimationC2SPayload::anchorEntityId, DebugStartAnimationC2SPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

