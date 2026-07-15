package com.afwid.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public final class AfwServerNetworking {
    private AfwServerNetworking() { }

    public static void send(ServerPlayerEntity player, AfwPacket packet) {
        ServerPlayNetworking.send(player, packet.id(), packet.toBuffer());
    }
}
