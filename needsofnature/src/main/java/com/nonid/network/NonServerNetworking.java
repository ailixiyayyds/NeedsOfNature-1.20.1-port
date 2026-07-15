package com.nonid.network;

import java.util.function.Function;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/** Typed facade over Fabric 1.20.1's identifier-and-buffer server networking API. */
public final class NonServerNetworking {
    private NonServerNetworking() {
    }

    public static <T extends NonPacket> void register(Identifier id, Function<PacketByteBuf, T> reader, Handler<T> handler) {
        ServerPlayNetworking.registerGlobalReceiver(id, (server, player, networkHandler, buf, responseSender) -> {
            T packet = reader.apply(buf);
            server.execute(() -> handler.handle(server, player, packet));
        });
    }

    public static void send(ServerPlayerEntity player, NonPacket packet) {
        ServerPlayNetworking.send(player, packet.id(), packet.toBuffer());
    }

    @FunctionalInterface
    public interface Handler<T extends NonPacket> {
        void handle(MinecraftServer server, ServerPlayerEntity player, T packet);
    }
}
