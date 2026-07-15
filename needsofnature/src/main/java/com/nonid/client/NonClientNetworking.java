package com.nonid.client;

import com.nonid.network.NonPacket;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

/** Typed facade over Fabric 1.20.1's identifier-and-buffer client networking API. */
public final class NonClientNetworking {
    private NonClientNetworking() {
    }

    public static <T extends NonPacket> void register(Identifier id, Function<PacketByteBuf, T> reader, BiConsumer<MinecraftClient, T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, networkHandler, buf, responseSender) -> {
            T packet = reader.apply(buf);
            client.execute(() -> handler.accept(client, packet));
        });
    }

    public static void send(NonPacket packet) {
        ClientPlayNetworking.send(packet.id(), packet.toBuffer());
    }
}
