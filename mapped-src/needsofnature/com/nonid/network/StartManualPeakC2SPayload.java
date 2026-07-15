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
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record StartManualPeakC2SPayload() implements CustomPayload
{
    public static final Identifier START_MANUAL_PEAK_ID = Identifier.of((String)"needsofnature", (String)"start_manual_peak");
    public static final CustomPayload.Id<StartManualPeakC2SPayload> ID = new CustomPayload.Id(START_MANUAL_PEAK_ID);
    public static final PacketCodec<RegistryByteBuf, StartManualPeakC2SPayload> CODEC = PacketCodec.unit((Object)new StartManualPeakC2SPayload());

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

