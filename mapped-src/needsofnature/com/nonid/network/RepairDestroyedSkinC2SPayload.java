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
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record RepairDestroyedSkinC2SPayload() implements CustomPayload
{
    public static final Identifier REPAIR_DESTROYED_SKIN_ID = Identifier.of((String)"needsofnature", (String)"repair_destroyed_skin");
    public static final CustomPayload.Id<RepairDestroyedSkinC2SPayload> ID = new CustomPayload.Id(REPAIR_DESTROYED_SKIN_ID);
    public static final PacketCodec<RegistryByteBuf, RepairDestroyedSkinC2SPayload> CODEC = PacketCodec.unit((Object)new RepairDestroyedSkinC2SPayload());

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

