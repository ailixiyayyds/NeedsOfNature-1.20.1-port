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

public record StartManualPeakC2SPayload() implements NonPacket
{
    public static final Identifier START_MANUAL_PEAK_ID = new Identifier("needsofnature", "start_manual_peak");
    public static final Identifier ID = START_MANUAL_PEAK_ID;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
    }

    public static StartManualPeakC2SPayload read(PacketByteBuf buf) {
        return new StartManualPeakC2SPayload();
    }
}

