package com.afwid.network;

import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record AnimationSpeedUpdateS2CPayload(UUID instanceId, double speed) implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "animation_speed_update");

    public static AnimationSpeedUpdateS2CPayload read(PacketByteBuf buf) {
        return new AnimationSpeedUpdateS2CPayload(buf.readUuid(), buf.readDouble());
    }

    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) { buf.writeUuid(instanceId); buf.writeDouble(speed); }
}
