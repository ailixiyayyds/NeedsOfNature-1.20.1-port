package com.afwid.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record DebugChatPreferenceC2SPayload(String modeId) implements AfwPacket {
    public static final Identifier ID = new Identifier("animationframework", "debug_chat_pref");
    public static DebugChatPreferenceC2SPayload read(PacketByteBuf buf) { return new DebugChatPreferenceC2SPayload(buf.readString(64)); }
    @Override public Identifier id() { return ID; }
    @Override public void write(PacketByteBuf buf) { buf.writeString(modeId, 64); }
}
