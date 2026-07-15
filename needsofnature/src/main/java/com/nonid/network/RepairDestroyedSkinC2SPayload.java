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

import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;

public record RepairDestroyedSkinC2SPayload() implements NonPacket
{
    public static final Identifier REPAIR_DESTROYED_SKIN_ID = new Identifier("needsofnature", "repair_destroyed_skin");
    public static final Identifier ID = REPAIR_DESTROYED_SKIN_ID;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
    }

    public static RepairDestroyedSkinC2SPayload read(PacketByteBuf buf) {
        return new RepairDestroyedSkinC2SPayload();
    }
}

