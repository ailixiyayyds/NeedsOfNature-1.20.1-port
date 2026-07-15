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
import net.minecraft.network.PacketByteBuf;

public record DestroyedSkinParticlesS2CPayload(UUID playerUuid, double x, double y, double z, int count, long seed) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "destroyed_skin_particles");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeUuid(playerUuid);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeVarInt(count);
        buf.writeLong(seed);
    }

    public static DestroyedSkinParticlesS2CPayload read(PacketByteBuf buf) {
        return new DestroyedSkinParticlesS2CPayload(buf.readUuid(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarInt(), buf.readLong());
    }
}

