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

public record DestroyedSkinSyncS2CPayload(UUID playerUuid, int stage, byte[] pngBytes) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"destroyed_skin_sync");
    public static final CustomPayload.Id<DestroyedSkinSyncS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, DestroyedSkinSyncS2CPayload> CODEC = PacketCodec.ofStatic((buf, payload) -> {
        buf.writeUuid(payload.playerUuid());
        buf.writeVarInt(payload.stage());
        buf.writeByteArray(payload.pngBytes());
    }, buf -> new DestroyedSkinSyncS2CPayload(buf.readUuid(), buf.readVarInt(), buf.readByteArray(65536)));

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

