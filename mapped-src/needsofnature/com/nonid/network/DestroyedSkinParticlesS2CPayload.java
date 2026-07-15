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

public record DestroyedSkinParticlesS2CPayload(UUID playerUuid, double x, double y, double z, int count, long seed) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"destroyed_skin_particles");
    public static final CustomPayload.Id<DestroyedSkinParticlesS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, DestroyedSkinParticlesS2CPayload> CODEC = PacketCodec.ofStatic((buf, payload) -> {
        buf.writeUuid(payload.playerUuid());
        buf.writeDouble(payload.x());
        buf.writeDouble(payload.y());
        buf.writeDouble(payload.z());
        buf.writeVarInt(payload.count());
        buf.writeLong(payload.seed());
    }, buf -> new DestroyedSkinParticlesS2CPayload(buf.readUuid(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarInt(), buf.readLong()));

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

