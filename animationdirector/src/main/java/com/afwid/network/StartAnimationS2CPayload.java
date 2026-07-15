package com.afwid.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public record StartAnimationS2CPayload(
        Identifier animationId,
        UUID instanceId,
        List<UUID> actorUuids,
        List<String> actorKeys,
        List<AnimationStageInfo> stages,
        long startTick,
        double speed,
        boolean lockOrientation,
        float lockedYaw,
        float lockedHeadYaw,
        float lockedPitch,
        @Nullable Vec3d cameraOrbitTarget) implements AfwPacket {

    public static final Identifier ID = new Identifier("animationframework", "start_animation");
    private static final int MAX_ACTORS = 16;
    private static final int MAX_STAGES = 16;

    public StartAnimationS2CPayload {
        if (animationId == null || instanceId == null) throw new IllegalArgumentException("Animation and instance IDs are required");
        actorUuids = List.copyOf(actorUuids);
        actorKeys = actorKeys == null ? List.of() : List.copyOf(actorKeys);
        stages = stages == null ? List.of() : List.copyOf(stages);
        if (actorUuids.size() > MAX_ACTORS) throw new IllegalArgumentException("Too many actor UUIDs: " + actorUuids.size());
        if (!actorKeys.isEmpty() && actorKeys.size() != actorUuids.size()) throw new IllegalArgumentException("actorKeys size mismatch");
        if (stages.size() > MAX_STAGES) throw new IllegalArgumentException("Too many stages: " + stages.size());
        if (!Double.isFinite(speed) || speed <= 0.0) speed = 1.0;
        if (!lockOrientation) {
            lockedYaw = 0.0f;
            lockedHeadYaw = 0.0f;
            lockedPitch = 0.0f;
        }
        if (cameraOrbitTarget != null && (!Double.isFinite(cameraOrbitTarget.x)
                || !Double.isFinite(cameraOrbitTarget.y) || !Double.isFinite(cameraOrbitTarget.z))) {
            cameraOrbitTarget = null;
        }
    }

    public static StartAnimationS2CPayload read(PacketByteBuf buf) {
        Identifier animationId = buf.readIdentifier();
        UUID instanceId = buf.readUuid();
        int actorCount = readBoundedCount(buf, MAX_ACTORS, "actors");
        List<UUID> actorUuids = new ArrayList<>(actorCount);
        for (int i = 0; i < actorCount; i++) actorUuids.add(buf.readUuid());
        int keyCount = readBoundedCount(buf, MAX_ACTORS, "actor keys");
        List<String> actorKeys = new ArrayList<>(keyCount);
        for (int i = 0; i < keyCount; i++) actorKeys.add(buf.readString(128));
        int stageCount = readBoundedCount(buf, MAX_STAGES, "stages");
        List<AnimationStageInfo> stages = new ArrayList<>(stageCount);
        for (int i = 0; i < stageCount; i++) stages.add(AnimationStageInfo.read(buf));
        long startTick = buf.readLong();
        double speed = buf.readDouble();
        boolean lockOrientation = buf.readBoolean();
        float lockedYaw = buf.readFloat();
        float lockedHeadYaw = buf.readFloat();
        float lockedPitch = buf.readFloat();
        Vec3d orbitTarget = buf.readBoolean() ? new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()) : null;
        return new StartAnimationS2CPayload(animationId, instanceId, actorUuids, actorKeys, stages, startTick,
                speed, lockOrientation, lockedYaw, lockedHeadYaw, lockedPitch, orbitTarget);
    }

    @Override public Identifier id() { return ID; }

    @Override public void write(PacketByteBuf buf) {
        buf.writeIdentifier(animationId);
        buf.writeUuid(instanceId);
        buf.writeVarInt(actorUuids.size());
        actorUuids.forEach(buf::writeUuid);
        buf.writeVarInt(actorKeys.size());
        actorKeys.forEach(key -> buf.writeString(key, 128));
        buf.writeVarInt(stages.size());
        stages.forEach(stage -> stage.write(buf));
        buf.writeLong(startTick);
        buf.writeDouble(speed);
        buf.writeBoolean(lockOrientation);
        buf.writeFloat(lockedYaw);
        buf.writeFloat(lockedHeadYaw);
        buf.writeFloat(lockedPitch);
        buf.writeBoolean(cameraOrbitTarget != null);
        if (cameraOrbitTarget != null) {
            buf.writeDouble(cameraOrbitTarget.x);
            buf.writeDouble(cameraOrbitTarget.y);
            buf.writeDouble(cameraOrbitTarget.z);
        }
    }

    private static int readBoundedCount(PacketByteBuf buf, int maximum, String label) {
        int count = buf.readVarInt();
        if (count < 0 || count > maximum) throw new IllegalArgumentException("Invalid " + label + " count: " + count);
        return count;
    }
}
