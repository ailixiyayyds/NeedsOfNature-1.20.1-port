/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.Uuids
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.network.packet.CustomPayload$Id
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodec
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.network;

import com.afwid.network.AnimationStageInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.Nullable;

public record StartAnimationS2CPayload(Identifier animationId, UUID instanceId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, long startTick, double speed, boolean lockOrientation, float lockedYaw, float lockedHeadYaw, float lockedPitch, @Nullable Vec3d cameraOrbitTarget) implements CustomPayload
{
    public static final Identifier START_ANIMATION_ID = Identifier.of((String)"animationframework", (String)"start_animation");
    public static final CustomPayload.Id<StartAnimationS2CPayload> ID = new CustomPayload.Id(START_ANIMATION_ID);
    private static final int MAX_ACTORS = 16;
    private static final int MAX_STAGES = 16;
    public static final PacketCodec<RegistryByteBuf, StartAnimationS2CPayload> CODEC = PacketCodec.of(StartAnimationS2CPayload::encode, StartAnimationS2CPayload::decode);

    public StartAnimationS2CPayload(Identifier animationId, UUID instanceId, List<UUID> actorUuids, List<String> actorKeys, List<AnimationStageInfo> stages, long startTick, double speed, boolean lockOrientation, float lockedYaw, float lockedHeadYaw, float lockedPitch, @Nullable Vec3d cameraOrbitTarget) {
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
        if (cameraOrbitTarget != null && !cameraOrbitTarget.isFinite()) {
            cameraOrbitTarget = null;
        }
    }

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void encode(StartAnimationS2CPayload payload, RegistryByteBuf buf) {
        Identifier.PACKET_CODEC.encode((Object)buf, (Object)payload.animationId());
        Uuids.PACKET_CODEC.encode((Object)buf, (Object)payload.instanceId());
        buf.writeVarInt(payload.actorUuids().size());
        for (UUID uuid : payload.actorUuids()) {
            Uuids.PACKET_CODEC.encode((Object)buf, (Object)uuid);
        }
        buf.writeVarInt(payload.actorKeys().size());
        for (String key : payload.actorKeys()) {
            buf.writeString(key);
        }
        buf.writeVarInt(payload.stages().size());
        for (AnimationStageInfo stage : payload.stages()) {
            AnimationStageInfo.CODEC.encode((Object)buf, (Object)stage);
        }
        buf.writeLong(payload.startTick());
        buf.writeDouble(payload.speed());
        buf.writeBoolean(payload.lockOrientation());
        buf.writeFloat(payload.lockedYaw());
        buf.writeFloat(payload.lockedHeadYaw());
        buf.writeFloat(payload.lockedPitch());
        buf.writeBoolean(payload.cameraOrbitTarget() != null);
        if (payload.cameraOrbitTarget() != null) {
            Vec3d target = payload.cameraOrbitTarget();
            buf.writeDouble(target.x);
            buf.writeDouble(target.y);
            buf.writeDouble(target.z);
        }
    }

    private static StartAnimationS2CPayload decode(RegistryByteBuf buf) {
        Identifier animationId = (Identifier)Identifier.PACKET_CODEC.decode((Object)buf);
        UUID instanceId = (UUID)Uuids.PACKET_CODEC.decode((Object)buf);
        int actorCount = buf.readVarInt();
        if (actorCount < 0 || actorCount > 16) {
            throw new IllegalArgumentException("Too many actor UUIDs: " + actorCount);
        }
        ArrayList<UUID> actorUuids = new ArrayList<UUID>(actorCount);
        for (int i = 0; i < actorCount; ++i) {
            actorUuids.add((UUID)Uuids.PACKET_CODEC.decode((Object)buf));
        }
        int keyCount = buf.readVarInt();
        if (keyCount < 0 || keyCount > 16) {
            throw new IllegalArgumentException("Too many actor keys: " + keyCount);
        }
        ArrayList<String> actorKeys = new ArrayList<String>(keyCount);
        for (int i = 0; i < keyCount; ++i) {
            actorKeys.add(buf.readString(128));
        }
        int stageCount = buf.readVarInt();
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
        Vec3d cameraOrbitTarget = null;
        if (buf.readBoolean()) {
            cameraOrbitTarget = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
        return new StartAnimationS2CPayload(animationId, instanceId, actorUuids, actorKeys, stages, startTick, speed, lockOrientation, lockedYaw, lockedHeadYaw, lockedPitch, cameraOrbitTarget);
    }
}

