/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodecs
 *  net.minecraft.network.codec.PacketCodec
 */
package com.nonid.network;

import net.minecraft.util.Identifier;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.PacketCodec;

public record SetPlayerGenderC2SPayload(int mask, boolean initialSelection) implements CustomPayload
{
    public static final Identifier SET_PLAYER_GENDER_ID = Identifier.of((String)"needsofnature", (String)"set_player_gender");
    public static final CustomPayload.Id<SetPlayerGenderC2SPayload> ID = new CustomPayload.Id(SET_PLAYER_GENDER_ID);
    public static final PacketCodec<RegistryByteBuf, SetPlayerGenderC2SPayload> CODEC = PacketCodec.tuple((PacketCodec)PacketCodecs.INTEGER, SetPlayerGenderC2SPayload::mask, (PacketCodec)PacketCodecs.BOOLEAN, SetPlayerGenderC2SPayload::initialSelection, SetPlayerGenderC2SPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

