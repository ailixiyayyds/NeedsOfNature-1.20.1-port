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

public record GameplayRuntimeSettingsS2CPayload(int loopSeconds, int peakLoopSeconds, int attackEscapeHits, double attackDecayPerSecond, int attackEscapeDamageDifficultyPercent, boolean attackCreativePlayers) implements CustomPayload
{
    public static final Identifier ID_RAW = Identifier.of((String)"needsofnature", (String)"gameplay_runtime_settings");
    public static final CustomPayload.Id<GameplayRuntimeSettingsS2CPayload> ID = new CustomPayload.Id(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, GameplayRuntimeSettingsS2CPayload> CODEC = PacketCodec.tuple((PacketCodec)PacketCodecs.INTEGER, GameplayRuntimeSettingsS2CPayload::loopSeconds, (PacketCodec)PacketCodecs.INTEGER, GameplayRuntimeSettingsS2CPayload::peakLoopSeconds, (PacketCodec)PacketCodecs.INTEGER, GameplayRuntimeSettingsS2CPayload::attackEscapeHits, (PacketCodec)PacketCodecs.DOUBLE, GameplayRuntimeSettingsS2CPayload::attackDecayPerSecond, (PacketCodec)PacketCodecs.INTEGER, GameplayRuntimeSettingsS2CPayload::attackEscapeDamageDifficultyPercent, (PacketCodec)PacketCodecs.BOOLEAN, GameplayRuntimeSettingsS2CPayload::attackCreativePlayers, GameplayRuntimeSettingsS2CPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

