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

public record AccessoryShedStateS2CPayload(UUID playerUuid, boolean shedV, boolean shedA, boolean forcedV, boolean forcedA) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "accessory_shed_state");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeUuid(playerUuid);
        buf.writeBoolean(shedV);
        buf.writeBoolean(shedA);
        buf.writeBoolean(forcedV);
        buf.writeBoolean(forcedA);
    }

    public static AccessoryShedStateS2CPayload read(PacketByteBuf buf) {
        return new AccessoryShedStateS2CPayload(buf.readUuid(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }
}

