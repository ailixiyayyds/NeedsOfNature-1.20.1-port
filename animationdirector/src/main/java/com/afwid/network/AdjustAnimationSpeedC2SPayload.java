package com.afwid.network;

import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record AdjustAnimationSpeedC2SPayload(UUID instanceId, double multiplier) implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "adjust_animation_speed");

    public static AdjustAnimationSpeedC2SPayload read(PacketByteBuf buf) {
        return new AdjustAnimationSpeedC2SPayload(buf.readUuid(), buf.readDouble());
    }

    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) { buf.writeUuid(instanceId); buf.writeDouble(multiplier); }
}
