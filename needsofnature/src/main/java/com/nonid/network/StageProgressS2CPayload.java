/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.Uuids
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodecs
 *  net.minecraft.network.codec.PacketCodec
 */
package com.nonid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;

public record StageProgressS2CPayload(UUID instanceId, Identifier stageAnimationId, long startTick, long endTick, Mode mode) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "stage_progress");
    public static final Identifier ID = ID_RAW;

    public StageProgressS2CPayload {
        mode = mode == null ? Mode.HIDDEN : mode;
    }

    private static StageProgressS2CPayload fromPacket(UUID instanceId, Identifier stageAnimationId, long startTick, long endTick, int modeId) {
        return new StageProgressS2CPayload(instanceId, stageAnimationId, startTick, endTick, Mode.fromId(modeId));
    }

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeUuid(instanceId);
        buf.writeIdentifier(stageAnimationId);
        buf.writeLong(startTick);
        buf.writeLong(endTick);
        buf.writeInt(mode.ordinal());
    }

    public static StageProgressS2CPayload read(PacketByteBuf buf) {
        return fromPacket(buf.readUuid(), buf.readIdentifier(), buf.readLong(), buf.readLong(), buf.readInt());
    }

    public static enum Mode {
        HIDDEN,
        SERVER_WINDOW,
        AFW_TIMELINE;


        private static Mode fromId(int id) {
            Mode[] values = Mode.values();
            return id >= 0 && id < values.length ? values[id] : HIDDEN;
        }
    }
}

