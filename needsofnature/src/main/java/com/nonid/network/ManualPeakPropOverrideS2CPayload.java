/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.Uuids
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodec
 */
package com.nonid.network;

import java.util.UUID;
import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;

public record ManualPeakPropOverrideS2CPayload(UUID instanceId, UUID actorUuid, Identifier itemId) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "manual_peak_prop_override");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeUuid(instanceId);
        buf.writeUuid(actorUuid);
        buf.writeIdentifier(itemId);
    }

    public static ManualPeakPropOverrideS2CPayload read(PacketByteBuf buf) {
        return new ManualPeakPropOverrideS2CPayload(buf.readUuid(), buf.readUuid(), buf.readIdentifier());
    }
}

