/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.Uuids
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodec
 */
package com.nonid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record StopAttackAnimationC2SPayload(UUID instanceId) implements CustomPayload
{
    public static final Identifier STOP_ATTACK_ANIMATION_ID = Identifier.of((String)"needsofnature", (String)"stop_attack_animation");
    public static final CustomPayload.Id<StopAttackAnimationC2SPayload> ID = new CustomPayload.Id(STOP_ATTACK_ANIMATION_ID);
    public static final PacketCodec<RegistryByteBuf, StopAttackAnimationC2SPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, StopAttackAnimationC2SPayload::instanceId, StopAttackAnimationC2SPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

