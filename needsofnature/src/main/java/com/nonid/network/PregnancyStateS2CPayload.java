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
package com.nonid.network;

import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;

public record PregnancyStateS2CPayload(String pregnantEntityTypeId) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "pregnancy_state");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(pregnantEntityTypeId);
    }

    public static PregnancyStateS2CPayload read(PacketByteBuf buf) {
        return new PregnancyStateS2CPayload(buf.readString());
    }
}

