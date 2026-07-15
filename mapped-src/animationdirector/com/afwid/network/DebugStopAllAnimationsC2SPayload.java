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
package com.afwid.network;

import net.minecraft.util.Identifier;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record DebugStopAllAnimationsC2SPayload() implements CustomPayload
{
    public static final Identifier DEBUG_STOP_ALL_ID = Identifier.of((String)"animationframework", (String)"debug_stop_all");
    public static final CustomPayload.Id<DebugStopAllAnimationsC2SPayload> ID = new CustomPayload.Id(DEBUG_STOP_ALL_ID);
    public static final PacketCodec<RegistryByteBuf, DebugStopAllAnimationsC2SPayload> CODEC = PacketCodec.unit((Object)new DebugStopAllAnimationsC2SPayload());

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

