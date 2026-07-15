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
package com.afwid.network;

import net.minecraft.util.Identifier;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record DebugChatPreferenceC2SPayload(String modeId) implements CustomPayload
{
    public static final Identifier DEBUG_CHAT_PREF_ID = Identifier.of((String)"animationframework", (String)"debug_chat_pref");
    public static final CustomPayload.Id<DebugChatPreferenceC2SPayload> ID = new CustomPayload.Id(DEBUG_CHAT_PREF_ID);
    public static final PacketCodec<RegistryByteBuf, DebugChatPreferenceC2SPayload> CODEC = PacketCodec.of(DebugChatPreferenceC2SPayload::encode, DebugChatPreferenceC2SPayload::decode);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void encode(DebugChatPreferenceC2SPayload payload, RegistryByteBuf buf) {
        buf.writeString(payload.modeId());
    }

    private static DebugChatPreferenceC2SPayload decode(RegistryByteBuf buf) {
        return new DebugChatPreferenceC2SPayload(buf.readString(64));
    }
}

