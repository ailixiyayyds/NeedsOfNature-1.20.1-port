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
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record StageProgressS2CPayload(UUID instanceId, Identifier stageAnimationId, long startTick, long endTick, Mode mode) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"stage_progress");
    public static final CustomPayload.Id<StageProgressS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, StageProgressS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, StageProgressS2CPayload::instanceId, (PacketCodec)Identifier.PACKET_CODEC.cast(), StageProgressS2CPayload::stageAnimationId, (PacketCodec)PacketCodecs.LONG, StageProgressS2CPayload::startTick, (PacketCodec)PacketCodecs.LONG, StageProgressS2CPayload::endTick, (PacketCodec)PacketCodecs.INTEGER, payload -> payload.mode().ordinal(), StageProgressS2CPayload::fromPacket);

    public StageProgressS2CPayload {
        mode = mode == null ? Mode.HIDDEN : mode;
    }

    private static StageProgressS2CPayload fromPacket(UUID instanceId, Identifier stageAnimationId, long startTick, long endTick, int modeId) {
        return new StageProgressS2CPayload(instanceId, stageAnimationId, startTick, endTick, Mode.fromId(modeId));
    }

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
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

