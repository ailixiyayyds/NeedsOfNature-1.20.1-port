/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9129
 *  net.minecraft.class_9135
 *  net.minecraft.class_9139
 */
package com.nonid.network;

import net.minecraft.class_2960;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record GameplayRuntimeSettingsS2CPayload(int loopSeconds, int peakLoopSeconds, int attackEscapeHits, double attackDecayPerSecond, int attackEscapeDamageDifficultyPercent, boolean attackCreativePlayers) implements class_8710
{
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"needsofnature", (String)"gameplay_runtime_settings");
    public static final class_8710.class_9154<GameplayRuntimeSettingsS2CPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, GameplayRuntimeSettingsS2CPayload> CODEC = class_9139.method_58025((class_9139)class_9135.field_49675, GameplayRuntimeSettingsS2CPayload::loopSeconds, (class_9139)class_9135.field_49675, GameplayRuntimeSettingsS2CPayload::peakLoopSeconds, (class_9139)class_9135.field_49675, GameplayRuntimeSettingsS2CPayload::attackEscapeHits, (class_9139)class_9135.field_48553, GameplayRuntimeSettingsS2CPayload::attackDecayPerSecond, (class_9139)class_9135.field_49675, GameplayRuntimeSettingsS2CPayload::attackEscapeDamageDifficultyPercent, (class_9139)class_9135.field_48547, GameplayRuntimeSettingsS2CPayload::attackCreativePlayers, GameplayRuntimeSettingsS2CPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

