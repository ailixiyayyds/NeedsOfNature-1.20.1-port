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

public record GenderSelectionPromptS2CPayload(int allowedMask, int currentMask, boolean permanent) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "gender_selection_prompt");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(allowedMask);
        buf.writeInt(currentMask);
        buf.writeBoolean(permanent);
    }

    public static GenderSelectionPromptS2CPayload read(PacketByteBuf buf) {
        return new GenderSelectionPromptS2CPayload(buf.readInt(), buf.readInt(), buf.readBoolean());
    }
}

