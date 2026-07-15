package com.afwid.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

/** Common 1.20.1 packet contract used by both networking directions. */
public interface AfwPacket {
    Identifier id();

    void write(PacketByteBuf buf);

    default PacketByteBuf toBuffer() {
        PacketByteBuf buf = PacketByteBufs.create();
        write(buf);
        return buf;
    }
}
