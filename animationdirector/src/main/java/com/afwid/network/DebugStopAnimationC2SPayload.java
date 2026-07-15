package com.afwid.network;

import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record DebugStopAnimationC2SPayload(UUID instanceId) implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "debug_stop_animation");
    public static DebugStopAnimationC2SPayload read(PacketByteBuf buf) { return new DebugStopAnimationC2SPayload(buf.readUuid()); }
    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) { buf.writeUuid(instanceId); }
}
