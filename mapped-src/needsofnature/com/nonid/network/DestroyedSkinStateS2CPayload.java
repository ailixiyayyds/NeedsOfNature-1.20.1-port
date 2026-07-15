/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodec
 */
package com.nonid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record DestroyedSkinStateS2CPayload(UUID playerUuid, int stage) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"destroyed_skin_state");
    public static final CustomPayload.Id<DestroyedSkinStateS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, DestroyedSkinStateS2CPayload> CODEC = PacketCodec.ofStatic((buf, payload) -> {
        buf.writeUuid(payload.playerUuid());
        buf.writeVarInt(payload.stage());
    }, buf -> new DestroyedSkinStateS2CPayload(buf.readUuid(), buf.readVarInt()));

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

