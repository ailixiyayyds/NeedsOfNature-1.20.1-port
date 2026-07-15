package com.afwid.network;

import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record StopAnimationS2CPayload(UUID instanceId, long stopTick) implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "stop_animation");
    public static StopAnimationS2CPayload read(PacketByteBuf buf) { return new StopAnimationS2CPayload(buf.readUuid(), buf.readLong()); }
    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) { buf.writeUuid(instanceId); buf.writeLong(stopTick); }
}
