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
package com.nonid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record AttackStateS2CPayload(UUID instanceId, boolean attack, boolean escapable, EscapeProfile escapeProfile) implements CustomPayload
{
    public static final Identifier ATTACK_STATE_ID = Identifier.of((String)"needsofnature", (String)"attack_state");
    public static final CustomPayload.Id<AttackStateS2CPayload> ID = new CustomPayload.Id(ATTACK_STATE_ID);
    public static final PacketCodec<RegistryByteBuf, AttackStateS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, AttackStateS2CPayload::instanceId, (PacketCodec)PacketCodecs.BOOLEAN, AttackStateS2CPayload::attack, (PacketCodec)PacketCodecs.BOOLEAN, AttackStateS2CPayload::escapable, (PacketCodec)PacketCodecs.INTEGER, payload -> payload.escapeProfile().ordinal(), AttackStateS2CPayload::fromPacket);

    public AttackStateS2CPayload {
        if (escapeProfile == null) {
            escapeProfile = EscapeProfile.NONE;
        }
    }

    private static AttackStateS2CPayload fromPacket(UUID instanceId, boolean attack, boolean escapable, int profileId) {
        return new AttackStateS2CPayload(instanceId, attack, escapable, EscapeProfile.fromId(profileId));
    }

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static enum EscapeProfile {
        NONE,
        NORMAL,
        DEFEATED;


        public static EscapeProfile fromId(int id) {
            EscapeProfile[] values = EscapeProfile.values();
            if (id < 0 || id >= values.length) {
                return NONE;
            }
            return values[id];
        }
    }
}

