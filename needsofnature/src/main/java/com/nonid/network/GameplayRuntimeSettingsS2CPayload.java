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
import net.minecraft.network.PacketByteBuf;

public record GameplayRuntimeSettingsS2CPayload(int loopSeconds, int peakLoopSeconds, int attackEscapeHits, double attackDecayPerSecond, int attackEscapeDamageDifficultyPercent, boolean attackCreativePlayers) implements NonPacket
{
    public static final Identifier ID_RAW = new Identifier("needsofnature", "gameplay_runtime_settings");
    public static final Identifier ID = ID_RAW;

    public Identifier id() {
        return ID;
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(loopSeconds);
        buf.writeInt(peakLoopSeconds);
        buf.writeInt(attackEscapeHits);
        buf.writeDouble(attackDecayPerSecond);
        buf.writeInt(attackEscapeDamageDifficultyPercent);
        buf.writeBoolean(attackCreativePlayers);
    }

    public static GameplayRuntimeSettingsS2CPayload read(PacketByteBuf buf) {
        return new GameplayRuntimeSettingsS2CPayload(buf.readInt(), buf.readInt(), buf.readInt(), buf.readDouble(), buf.readInt(), buf.readBoolean());
    }
}

