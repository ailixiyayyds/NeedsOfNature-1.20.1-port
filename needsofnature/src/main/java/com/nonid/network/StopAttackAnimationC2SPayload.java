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
import net.minecraft.network.PacketByteBuf;

public record StopAttackAnimationC2SPayload(UUID instanceId) implements NonPacket
{
    public static final Identifier STOP_ATTACK_ANIMATION_ID = new Identifier("needsofnature", "stop_attack_animation");
    public static final Identifier ID = STOP_ATTACK_ANIMATION_ID;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeUuid(instanceId);
    }

    public static StopAttackAnimationC2SPayload read(PacketByteBuf buf) {
        return new StopAttackAnimationC2SPayload(buf.readUuid());
    }
}

