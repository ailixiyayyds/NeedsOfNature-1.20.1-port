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

public record GenderSelectionPromptS2CPayload(int allowedMask, int currentMask, boolean permanent) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"gender_selection_prompt");
    public static final CustomPayload.Id<GenderSelectionPromptS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, GenderSelectionPromptS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)PacketCodecs.INTEGER, GenderSelectionPromptS2CPayload::allowedMask, (PacketCodec)PacketCodecs.INTEGER, GenderSelectionPromptS2CPayload::currentMask, (PacketCodec)PacketCodecs.BOOLEAN, GenderSelectionPromptS2CPayload::permanent, GenderSelectionPromptS2CPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

