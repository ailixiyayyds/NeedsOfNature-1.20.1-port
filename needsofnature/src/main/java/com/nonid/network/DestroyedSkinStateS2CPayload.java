/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodec
 */
package com.nonid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;

public record DestroyedSkinStateS2CPayload(UUID playerUuid, int stage) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "destroyed_skin_state");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeUuid(playerUuid);
        buf.writeVarInt(stage);
    }

    public static DestroyedSkinStateS2CPayload read(PacketByteBuf buf) {
        return new DestroyedSkinStateS2CPayload(buf.readUuid(), buf.readVarInt());
    }
}

