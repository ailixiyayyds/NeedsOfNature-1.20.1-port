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

public record ManualDamageDestroyedSkinC2SPayload() implements CustomPayload
{
    public static final Identifier MANUAL_DAMAGE_DESTROYED_SKIN_ID = Identifier.of((String)"needsofnature", (String)"manual_damage_destroyed_skin");
    public static final CustomPayload.Id<ManualDamageDestroyedSkinC2SPayload> ID = new CustomPayload.Id(MANUAL_DAMAGE_DESTROYED_SKIN_ID);
    public static final PacketCodec<RegistryByteBuf, ManualDamageDestroyedSkinC2SPayload> CODEC = PacketCodec.unit((Object)new ManualDamageDestroyedSkinC2SPayload());

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

