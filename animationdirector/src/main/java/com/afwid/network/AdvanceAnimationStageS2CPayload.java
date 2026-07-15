package com.afwid.network;

import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record AdvanceAnimationStageS2CPayload(UUID instanceId, long advanceTick, int stageIndex) implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "advance_stage");

    public static AdvanceAnimationStageS2CPayload read(PacketByteBuf buf) {
        return new AdvanceAnimationStageS2CPayload(buf.readUuid(), buf.readLong(), buf.readVarInt());
    }

    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) { buf.writeUuid(instanceId); buf.writeLong(advanceTick); buf.writeVarInt(stageIndex); }
}
