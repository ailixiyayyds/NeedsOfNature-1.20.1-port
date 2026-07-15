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

import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;

public record SkinRippedInfoRequestS2CPayload() implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "skin_ripped_info_request");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
    }

    public static SkinRippedInfoRequestS2CPayload read(PacketByteBuf buf) {
        return new SkinRippedInfoRequestS2CPayload();
    }
}

