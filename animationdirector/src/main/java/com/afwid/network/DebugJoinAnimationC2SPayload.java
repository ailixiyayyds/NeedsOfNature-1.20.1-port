package com.afwid.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record DebugJoinAnimationC2SPayload(UUID instanceId, List<Integer> actorEntityIds) implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "debug_join_animation");
    private static final int MAX_ACTORS = 32;

    public DebugJoinAnimationC2SPayload {
        actorEntityIds = List.copyOf(actorEntityIds);
        if (actorEntityIds.size() > MAX_ACTORS) throw new IllegalArgumentException("Too many actors");
    }

    public static DebugJoinAnimationC2SPayload read(PacketByteBuf buf) {
        UUID instanceId = buf.readUuid();
        int count = readBoundedCount(buf, MAX_ACTORS);
        List<Integer> ids = new ArrayList<>(count);
        for (int i = 0; i < count; i++) ids.add(buf.readVarInt());
        return new DebugJoinAnimationC2SPayload(instanceId, ids);
    }

    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) {
        buf.writeUuid(instanceId);
        buf.writeVarInt(actorEntityIds.size());
        actorEntityIds.forEach(buf::writeVarInt);
    }

    private static int readBoundedCount(PacketByteBuf buf, int maximum) {
        int count = buf.readVarInt();
        if (count < 0 || count > maximum) throw new IllegalArgumentException("Invalid actor count: " + count);
        return count;
    }
}
