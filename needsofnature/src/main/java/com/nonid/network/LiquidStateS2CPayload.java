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

public record LiquidStateS2CPayload(int stored, int capacity, int tintRgb) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "liquid_state");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(stored);
        buf.writeInt(capacity);
        buf.writeInt(tintRgb);
    }

    public static LiquidStateS2CPayload read(PacketByteBuf buf) {
        return new LiquidStateS2CPayload(buf.readInt(), buf.readInt(), buf.readInt());
    }
}

