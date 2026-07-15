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
import net.minecraft.network.PacketByteBuf;

public record AttackStateS2CPayload(UUID instanceId, boolean attack, boolean escapable, EscapeProfile escapeProfile) implements NonPacket
{
    public static final Identifier ATTACK_STATE_ID = new Identifier("needsofnature", "attack_state");
    public static final Identifier ID = ATTACK_STATE_ID;

    public AttackStateS2CPayload {
        if (escapeProfile == null) {
            escapeProfile = EscapeProfile.NONE;
        }
    }

    private static AttackStateS2CPayload fromPacket(UUID instanceId, boolean attack, boolean escapable, int profileId) {
        return new AttackStateS2CPayload(instanceId, attack, escapable, EscapeProfile.fromId(profileId));
    }

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeUuid(instanceId);
        buf.writeBoolean(attack);
        buf.writeBoolean(escapable);
        buf.writeInt(escapeProfile.ordinal());
    }

    public static AttackStateS2CPayload read(PacketByteBuf buf) {
        return fromPacket(buf.readUuid(), buf.readBoolean(), buf.readBoolean(), buf.readInt());
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

