/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 *  net.minecraft.class_2960
 *  net.minecraft.class_4844
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9129
 *  net.minecraft.class_9139
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.network;

import com.afwid.network.AnimationStageInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_4844;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9139;
import org.jetbrains.annotations.Nullable;

public record StartAnimationS2CPayload(class_2960 animationId, UUID instanceId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, long startTick, double speed, boolean lockOrientation, float lockedYaw, float lockedHeadYaw, float lockedPitch, @Nullable class_243 cameraOrbitTarget) implements class_8710
{
    public static final class_2960 START_ANIMATION_ID = class_2960.method_60655((String)"animationframework", (String)"start_animation");
    public static final class_8710.class_9154<StartAnimationS2CPayload> ID = new class_8710.class_9154(START_ANIMATION_ID);
    private static final int MAX_ACTORS = 16;
    private static final int MAX_STAGES = 16;
    public static final class_9139<class_9129, StartAnimationS2CPayload> CODEC = class_9139.method_56438(StartAnimationS2CPayload::encode, StartAnimationS2CPayload::decode);

    public StartAnimationS2CPayload(class_2960 animationId, UUID instanceId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, long startTick, double speed, boolean lockOrientation, float lockedYaw, float lockedHeadYaw, float lockedPitch, @Nullable class_243 cameraOrbitTarget) {
        actorUuids = List.copyOf(actorUuids);
        actorKeys = actorKeys == null ? List.of() : List.copyOf(actorKeys);
        List<Object> list = stages = stages == null ? List.of() : List.copyOf(stages);
        if (actorUuids.size() > 16) {
            throw new IllegalArgumentException("Too many actor UUIDs: " + actorUuids.size());
        }
        if (!actorKeys.isEmpty() && actorKeys.size() != actorUuids.size()) {
            throw new IllegalArgumentException("actorKeys size mismatch: " + actorKeys.size());
        }
        if (stages.size() > 16) {
            throw new IllegalArgumentException("Too many stages: " + stages.size());
        }
        if (Double.isNaN(speed) || Double.isInfinite(speed) || speed <= 0.0) {
            speed = 1.0;
        }
        if (!lockOrientation) {
            lockedYaw = 0.0f;
            lockedHeadYaw = 0.0f;
            lockedPitch = 0.0f;
        }
        if (cameraOrbitTarget != null && !cameraOrbitTarget.method_76470()) {
            cameraOrbitTarget = null;
        }
    }

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }

    private static void encode(StartAnimationS2CPayload payload, class_9129 buf) {
        class_2960.field_48267.encode((Object)buf, (Object)payload.animationId());
        class_4844.field_48453.encode((Object)buf, (Object)payload.instanceId());
        buf.method_10804(payload.actorUuids().size());
        for (UUID uuid : payload.actorUuids()) {
            class_4844.field_48453.encode((Object)buf, (Object)uuid);
        }
        buf.method_10804(payload.actorKeys().size());
        for (String key : payload.actorKeys()) {
            buf.method_10814(key);
        }
        buf.method_10804(payload.stages().size());
        for (AnimationStageInfo stage : payload.stages()) {
            AnimationStageInfo.CODEC.encode((Object)buf, (Object)stage);
        }
        buf.method_52974(payload.startTick());
        buf.method_52940(payload.speed());
        buf.method_52964(payload.lockOrientation());
        buf.method_52941(payload.lockedYaw());
        buf.method_52941(payload.lockedHeadYaw());
        buf.method_52941(payload.lockedPitch());
        buf.method_52964(payload.cameraOrbitTarget() != null);
        if (payload.cameraOrbitTarget() != null) {
            class_243 target = payload.cameraOrbitTarget();
            buf.method_52940(target.field_1352);
            buf.method_52940(target.field_1351);
            buf.method_52940(target.field_1350);
        }
    }

    private static StartAnimationS2CPayload decode(class_9129 buf) {
        class_2960 animationId = (class_2960)class_2960.field_48267.decode((Object)buf);
        UUID instanceId = (UUID)class_4844.field_48453.decode((Object)buf);
        int actorCount = buf.method_10816();
        if (actorCount < 0 || actorCount > 16) {
            throw new IllegalArgumentException("Too many actor UUIDs: " + actorCount);
        }
        ArrayList<UUID> actorUuids = new ArrayList<UUID>(actorCount);
        for (int i = 0; i < actorCount; ++i) {
            actorUuids.add((UUID)class_4844.field_48453.decode((Object)buf));
        }
        int keyCount = buf.method_10816();
        if (keyCount < 0 || keyCount > 16) {
            throw new IllegalArgumentException("Too many actor keys: " + keyCount);
        }
        ArrayList<String> actorKeys = new ArrayList<String>(keyCount);
        for (int i = 0; i < keyCount; ++i) {
            actorKeys.add(buf.method_10800(128));
        }
        int stageCount = buf.method_10816();
        if (stageCount < 0 || stageCount > 16) {
            throw new IllegalArgumentException("Too many stages: " + stageCount);
        }
        ArrayList<AnimationStageInfo> stages = new ArrayList<AnimationStageInfo>(stageCount);
        for (int i = 0; i < stageCount; ++i) {
            stages.add((AnimationStageInfo)AnimationStageInfo.CODEC.decode((Object)buf));
        }
        long startTick = buf.readLong();
        double speed = buf.readDouble();
        boolean lockOrientation = buf.readBoolean();
        float lockedYaw = buf.readFloat();
        float lockedHeadYaw = buf.readFloat();
        float lockedPitch = buf.readFloat();
        class_243 cameraOrbitTarget = null;
        if (buf.readBoolean()) {
            cameraOrbitTarget = new class_243(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
        return new StartAnimationS2CPayload(animationId, instanceId, actorUuids, actorKeys, stages, startTick, speed, lockOrientation, lockedYaw, lockedHeadYaw, lockedPitch, cameraOrbitTarget);
    }
}

