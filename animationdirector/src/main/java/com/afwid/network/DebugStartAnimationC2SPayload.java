package com.afwid.network;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record DebugStartAnimationC2SPayload(List<Integer> actorEntityIds, String damageBehaviorId,
                                             boolean ignoreAttackers, int anchorEntityId) implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "debug_start_animation");
    private static final int MAX_ACTORS = 32;

    public DebugStartAnimationC2SPayload {
        actorEntityIds = List.copyOf(actorEntityIds);
        if (actorEntityIds.size() > MAX_ACTORS) throw new IllegalArgumentException("Too many actors");
        damageBehaviorId = damageBehaviorId == null ? "" : damageBehaviorId;
    }

    public static DebugStartAnimationC2SPayload read(PacketByteBuf buf) {
        int count = buf.readVarInt();
        if (count < 0 || count > MAX_ACTORS) throw new IllegalArgumentException("Invalid actor count: " + count);
        List<Integer> ids = new ArrayList<>(count);
        for (int i = 0; i < count; i++) ids.add(buf.readVarInt());
        return new DebugStartAnimationC2SPayload(ids, buf.readString(64), buf.readBoolean(), buf.readVarInt());
    }

    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) {
        buf.writeVarInt(actorEntityIds.size());
        actorEntityIds.forEach(buf::writeVarInt);
        buf.writeString(damageBehaviorId, 64);
        buf.writeBoolean(ignoreAttackers);
        buf.writeVarInt(anchorEntityId);
    }
}
