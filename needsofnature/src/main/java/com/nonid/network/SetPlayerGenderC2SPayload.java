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

public record SetPlayerGenderC2SPayload(int mask, boolean initialSelection) implements NonPacket
{
    public static final Identifier SET_PLAYER_GENDER_ID = new Identifier("needsofnature", "set_player_gender");
    public static final Identifier ID = SET_PLAYER_GENDER_ID;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(mask);
        buf.writeBoolean(initialSelection);
    }

    public static SetPlayerGenderC2SPayload read(PacketByteBuf buf) {
        return new SetPlayerGenderC2SPayload(buf.readInt(), buf.readBoolean());
    }
}

