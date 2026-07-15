package com.afwid.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record DebugStopAllAnimationsC2SPayload() implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "debug_stop_all");
    public static DebugStopAllAnimationsC2SPayload read(PacketByteBuf buf) { return new DebugStopAllAnimationsC2SPayload(); }
    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) { }
}
