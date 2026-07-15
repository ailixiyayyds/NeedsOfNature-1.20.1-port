package com.afwid.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record StopAllAnimationsS2CPayload(long stopTick) implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "stop_all_animations");
    public static StopAllAnimationsS2CPayload read(PacketByteBuf buf) { return new StopAllAnimationsS2CPayload(buf.readLong()); }
    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) { buf.writeLong(stopTick); }
}
