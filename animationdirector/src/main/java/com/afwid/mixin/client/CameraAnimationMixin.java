/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.world.BlockView
 *  net.minecraft.world.World
 *  net.minecraft.world.CollisionView
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.shape.VoxelShape
 *  net.minecraft.block.BlockState
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.block.ShapeContext
 *  net.minecraft.client.render.Camera
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.camera.AfwAnimatedCameraPoseTracker;
import com.afwid.client.camera.AfwAnimationCameraZoom;
import com.afwid.client.camera.AfwFirstPersonCameraCollision;
import com.afwid.client.camera.AfwFirstPersonLookConstraint;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.CollisionView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Camera.class})
public abstract class CameraAnimationMixin {
    private static final double CAMERA_FORWARD_OFFSET = 0.2;
    private static final double THIRD_PERSON_ORBIT_EPSILON_SQ = 1.0E-8;
    private static final long FALLBACK_POSE_HOLD_TICKS = 20L;
    private static final long START_ALIGNMENT_TICKS = 8L;
    private static final long THIRD_PERSON_ORBIT_SMOOTH_TICKS = 6L;
    private static final long NEW_INSTANCE_ORBIT_SAMPLE_DELAY_TICKS = 4L;
    private static final double THIRD_PERSON_FOCUS_BOX_SIZE = 0.25;
    private static final double THIRD_PERSON_FOCUS_UP_STEP = 0.1;
    private static final int THIRD_PERSON_FOCUS_UP_STEPS = 16;
    private static final double THIRD_PERSON_FOCUS_HORIZONTAL_STEP = 0.25;
    private static final int THIRD_PERSON_FOCUS_HORIZONTAL_RINGS = 4;
    private static AfwAnimatedCameraPoseTracker.PoseSample afw$lastValidPosedPose = null;
    private static Vec3d afw$lastConstrainedLook = null;
    private static UUID afw$lastActiveInstanceId = null;
    private static UUID afw$startAlignInstanceId = null;
    private static UUID afw$bodyOrbitInstanceId = null;
    private static Identifier afw$bodyOrbitAnimationId = null;
    private static Vec3d afw$bodyOrbitTarget = null;
    private static boolean afw$delayBodyOrbitForCurrentInstance = false;
    private static UUID afw$smoothedOrbitInstanceId = null;
    private static Identifier afw$smoothedOrbitAnimationId = null;
    private static Vec3d afw$orbitSmoothStart = null;
    private static Vec3d afw$orbitSmoothTarget = null;
    private static Vec3d afw$currentSmoothedOrbitTarget = null;
    private static long afw$orbitSmoothStartTick = 0L;
    private static boolean afw$wasFirstPersonLastFrame = false;
    private static boolean afw$startAlignPendingTarget = false;
    private static boolean afw$startAlignActive = false;
    private static float afw$startAlignFromYaw = 0.0f;
    private static float afw$startAlignFromPitch = 0.0f;
    private static float afw$startAlignTargetYaw = 0.0f;
    private static float afw$startAlignTargetPitch = 0.0f;
    private static long afw$startAlignTick = 0L;

    @Shadow
    protected abstract void setPos(Vec3d var1);

    @Shadow
    protected abstract void setRotation(float var1, float var2);

    @Shadow
    protected abstract void moveBy(double var1, double var2, double var3);

    @Shadow
    private double clipToSpace(double startingDistance) {
        return startingDistance;
    }

    @Inject(method={"update"}, at={@At(value="TAIL")})
    private void afw$lockFirstPersonCameraToAnimatedPose(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        Vec3d targetLook;
        boolean replacingActiveInstance;
        if (!(focusedEntity instanceof PlayerEntity)) {
            CameraAnimationMixin.afw$resetAnimationCameraState(false);
            return;
        }
        PlayerEntity focusedPlayer = (PlayerEntity)focusedEntity;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null || client.player == null) {
            CameraAnimationMixin.afw$resetAnimationCameraState(!thirdPerson);
            return;
        }
        if (!focusedPlayer.getUuid().equals(client.player.getUuid())) {
            CameraAnimationMixin.afw$resetAnimationCameraState(!thirdPerson);
            return;
        }
        UUID actorUuid = focusedPlayer.getUuid();
        boolean pendingOrActive = AfwClientAnimationRuntime.isActorPendingOrActive(actorUuid);
        UUID activeInstanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(actorUuid);
        if (!pendingOrActive) {
            CameraAnimationMixin.afw$resetAnimationCameraState(!thirdPerson);
            return;
        }
        if (thirdPerson) {
            afw$lastActiveInstanceId = activeInstanceId;
            afw$wasFirstPersonLastFrame = false;
            Identifier activeAnimationId = AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining(actorUuid);
            this.afw$applyThirdPersonCameraAdjustments(actorUuid, activeInstanceId, activeAnimationId, focusedPlayer, tickDelta, client.world.getTime());
            return;
        }
        boolean enteredFirstPerson = !afw$wasFirstPersonLastFrame;
        boolean instanceChanged = !Objects.equals(activeInstanceId, afw$lastActiveInstanceId);
        boolean bl = replacingActiveInstance = instanceChanged && afw$lastActiveInstanceId != null;
        if (activeInstanceId != null && (enteredFirstPerson || instanceChanged && !replacingActiveInstance)) {
            CameraAnimationMixin.afw$beginStartAlignment(activeInstanceId, focusedPlayer.getYaw(tickDelta), focusedPlayer.getPitch(tickDelta));
        } else if (replacingActiveInstance) {
            CameraAnimationMixin.afw$clearStartAlignment();
        }
        afw$lastActiveInstanceId = activeInstanceId;
        afw$wasFirstPersonLastFrame = true;
        long nowTick = client.world.getTime();
        AfwAnimatedCameraPoseTracker.PoseSample posedPose = AfwAnimatedCameraPoseTracker.resolve(actorUuid, nowTick);
        if (posedPose != null) {
            afw$lastValidPosedPose = posedPose;
        } else if (afw$lastValidPosedPose != null && nowTick - afw$lastValidPosedPose.tick() <= 20L) {
            posedPose = afw$lastValidPosedPose;
        } else {
            return;
        }
        if (afw$startAlignPendingTarget && Objects.equals(activeInstanceId, afw$startAlignInstanceId) && (targetLook = CameraAnimationMixin.afw$resolveStartAlignmentTargetLook(posedPose)) != null) {
            afw$startAlignTargetYaw = AfwFirstPersonLookConstraint.toMinecraftYaw(targetLook, afw$startAlignFromYaw);
            afw$startAlignTargetPitch = AfwFirstPersonLookConstraint.toMinecraftPitch(targetLook);
            afw$startAlignTick = nowTick;
            afw$startAlignPendingTarget = false;
            afw$startAlignActive = true;
        }
        float desiredYaw = focusedPlayer.getYaw(tickDelta);
        float desiredPitch = focusedPlayer.getPitch(tickDelta);
        if (afw$startAlignActive && Objects.equals(activeInstanceId, afw$startAlignInstanceId)) {
            float progress = MathHelper.clamp((float)(((float)(nowTick - afw$startAlignTick) + tickDelta) / 8.0f), (float)0.0f, (float)1.0f);
            float eased = CameraAnimationMixin.afw$smootherStep(progress);
            desiredYaw = CameraAnimationMixin.afw$lerpAngleDegrees(eased, afw$startAlignFromYaw, afw$startAlignTargetYaw);
            desiredPitch = MathHelper.lerp((float)eased, (float)afw$startAlignFromPitch, (float)afw$startAlignTargetPitch);
            if (progress >= 1.0f) {
                afw$startAlignActive = false;
            }
        }
        Vec3d desiredLook = Vec3d.fromPolar((float)desiredPitch, (float)desiredYaw);
        Vec3d constrainedLook = AfwFirstPersonLookConstraint.constrain(desiredLook, posedPose, afw$lastConstrainedLook);
        float constrainedYaw = AfwFirstPersonLookConstraint.toMinecraftYaw(constrainedLook, desiredYaw);
        float constrainedPitch = AfwFirstPersonLookConstraint.toMinecraftPitch(constrainedLook);
        focusedPlayer.prevYaw = constrainedYaw;
        focusedPlayer.prevPitch = constrainedPitch;
        focusedPlayer.setYaw(constrainedYaw);
        focusedPlayer.setPitch(constrainedPitch);
        focusedPlayer.prevHeadYaw = constrainedYaw;
        focusedPlayer.headYaw = constrainedYaw;
        focusedPlayer.setHeadYaw(constrainedYaw);
        focusedPlayer.prevBodyYaw = constrainedYaw;
        focusedPlayer.bodyYaw = constrainedYaw;
        focusedPlayer.setBodyYaw(constrainedYaw);
        this.setRotation(constrainedYaw, constrainedPitch);
        Vec3d desiredCameraPos = posedPose.worldPos().add(constrainedLook.multiply(0.2));
        this.setPos(AfwFirstPersonCameraCollision.resolve((CollisionView)area, desiredCameraPos));
        afw$lastConstrainedLook = constrainedLook;
    }

    private static void afw$beginStartAlignment(UUID instanceId, float startYaw, float startPitch) {
        afw$startAlignInstanceId = instanceId;
        afw$startAlignPendingTarget = true;
        afw$startAlignActive = false;
        afw$startAlignFromYaw = startYaw;
        afw$startAlignFromPitch = startPitch;
        afw$startAlignTargetYaw = startYaw;
        afw$startAlignTargetPitch = startPitch;
        afw$startAlignTick = 0L;
    }

    private static void afw$clearStartAlignment() {
        afw$startAlignInstanceId = null;
        afw$startAlignPendingTarget = false;
        afw$startAlignActive = false;
    }

    private static Vec3d afw$resolveStartAlignmentTargetLook(AfwAnimatedCameraPoseTracker.PoseSample pose) {
        Vec3d targetLook = pose.headForward();
        if (targetLook == null || targetLook.lengthSquared() < 1.0E-8 || !afw$isFinite(targetLook)) {
            targetLook = pose.bodyForward();
        }
        if (targetLook == null || targetLook.lengthSquared() < 1.0E-8 || !afw$isFinite(targetLook)) {
            return null;
        }
        return targetLook.normalize();
    }

    private void afw$applyThirdPersonCameraAdjustments(UUID actorUuid, UUID activeInstanceId, Identifier activeAnimationId, PlayerEntity focusedPlayer, float tickDelta, long nowTick) {
        Vec3d focusPos = focusedPlayer.getCameraPosVec(tickDelta);
        if (focusPos == null) {
            return;
        }
        Vec3d adjustedFocus = focusPos;
        Vec3d orbitTarget = AfwClientAnimationRuntime.findCameraOrbitTargetForActor(actorUuid);
        if (orbitTarget != null) {
            CameraAnimationMixin.afw$clearBodyOrbitTarget();
            Vec3d horizontalDelta = new Vec3d(orbitTarget.x - focusPos.x, 0.0, orbitTarget.z - focusPos.z);
            if (afw$isFinite(horizontalDelta) && horizontalDelta.lengthSquared() >= 1.0E-8) {
                adjustedFocus = focusPos.add(horizontalDelta);
            }
        } else {
            Vec3d bodyTarget = CameraAnimationMixin.afw$resolveBodyOrbitTarget(actorUuid, activeInstanceId, activeAnimationId, nowTick);
            if (bodyTarget != null) {
                adjustedFocus = CameraAnimationMixin.afw$resolveSmoothedOrbitTarget(activeInstanceId, activeAnimationId, focusPos, bodyTarget, nowTick, tickDelta);
            } else if (afw$currentSmoothedOrbitTarget != null && afw$isFinite(afw$currentSmoothedOrbitTarget)) {
                adjustedFocus = afw$currentSmoothedOrbitTarget;
            } else if (activeInstanceId == null) {
                CameraAnimationMixin.afw$clearOrbitSmoothing();
            }
        }
        adjustedFocus = CameraAnimationMixin.afw$resolveClearThirdPersonFocus(focusedPlayer.getEntityWorld(), adjustedFocus);
        float zoomDistance = AfwAnimationCameraZoom.distance(tickDelta);
        this.setPos(adjustedFocus);
        this.moveBy(-this.clipToSpace(zoomDistance), 0.0f, 0.0f);
    }

    private static Vec3d afw$resolveClearThirdPersonFocus(World world, Vec3d desiredFocus) {
        if (world == null || desiredFocus == null || !afw$isFinite(desiredFocus)) {
            return desiredFocus;
        }
        if (CameraAnimationMixin.afw$isThirdPersonFocusClear(world, desiredFocus)) {
            return desiredFocus;
        }
        for (int step = 1; step <= 16; ++step) {
            Vec3d candidate = desiredFocus.add(0.0, (double)step * 0.1, 0.0);
            if (!CameraAnimationMixin.afw$isThirdPersonFocusClear(world, candidate)) continue;
            return candidate;
        }
        for (int ring = 1; ring <= 4; ++ring) {
            double distance = (double)ring * 0.25;
            for (int i = 0; i < 8; ++i) {
                double angle = Math.PI * 2 * (double)i / 8.0;
                Vec3d candidate = desiredFocus.add(Math.cos(angle) * distance, 0.0, Math.sin(angle) * distance);
                if (!CameraAnimationMixin.afw$isThirdPersonFocusClear(world, candidate)) continue;
                return candidate;
            }
        }
        return desiredFocus;
    }

    private static boolean afw$isThirdPersonFocusClear(World world, Vec3d focus) {
        if (world == null || focus == null || !afw$isFinite(focus)) {
            return true;
        }
        Box focusBox = Box.of((Vec3d)focus, (double)0.25, (double)0.25, (double)0.25);
        return world.isSpaceEmpty(focusBox) && !CameraAnimationMixin.afw$intersectsCameraCollisionShape(world, focusBox);
    }

    private static boolean afw$intersectsCameraCollisionShape(World world, Box focusBox) {
        int minX = MathHelper.floor((double)focusBox.minX);
        int maxX = MathHelper.floor((double)focusBox.maxX);
        int minY = MathHelper.floor((double)focusBox.minY);
        int maxY = MathHelper.floor((double)focusBox.maxY);
        int minZ = MathHelper.floor((double)focusBox.minZ);
        int maxZ = MathHelper.floor((double)focusBox.maxZ);
        ShapeContext shapeContext = ShapeContext.absent();
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    VoxelShape shape;
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if (state.isAir() || (shape = state.getCameraCollisionShape((BlockView)world, pos, shapeContext)).isEmpty()) continue;
                    for (Box shapeBox : shape.getBoundingBoxes()) {
                        if (!focusBox.intersects(shapeBox.offset(pos))) continue;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static Vec3d afw$resolveBodyOrbitTarget(UUID actorUuid, UUID activeInstanceId, Identifier activeAnimationId, long nowTick) {
        boolean animationChanged;
        if (actorUuid == null || activeInstanceId == null || activeAnimationId == null) {
            CameraAnimationMixin.afw$clearBodyOrbitTarget();
            return null;
        }
        boolean instanceChanged = !Objects.equals(activeInstanceId, afw$bodyOrbitInstanceId);
        boolean bl = animationChanged = !Objects.equals(activeAnimationId, afw$bodyOrbitAnimationId);
        if (instanceChanged || animationChanged) {
            if (instanceChanged) {
                afw$delayBodyOrbitForCurrentInstance = afw$currentSmoothedOrbitTarget == null;
            }
            afw$bodyOrbitInstanceId = activeInstanceId;
            afw$bodyOrbitAnimationId = activeAnimationId;
            afw$bodyOrbitTarget = null;
        }
        if (afw$bodyOrbitTarget != null) {
            return afw$bodyOrbitTarget;
        }
        Long startTick = AfwClientAnimationRuntime.findStartTickForActor(actorUuid);
        if (afw$delayBodyOrbitForCurrentInstance && startTick != null && nowTick - startTick < 4L) {
            return null;
        }
        AfwAnimatedCameraPoseTracker.PoseSample sample = AfwAnimatedCameraPoseTracker.resolve(actorUuid, nowTick);
        if (sample == null || !Objects.equals(sample.instanceId(), activeInstanceId) || !Objects.equals(sample.animationId(), activeAnimationId)) {
            return null;
        }
        if (afw$delayBodyOrbitForCurrentInstance && startTick != null && sample.tick() - startTick < 4L) {
            return null;
        }
        Vec3d bodyPos = sample.bodyWorldPos();
        if (bodyPos == null || !afw$isFinite(bodyPos)) {
            return null;
        }
        afw$bodyOrbitTarget = bodyPos;
        return afw$bodyOrbitTarget;
    }

    private static Vec3d afw$resolveSmoothedOrbitTarget(UUID activeInstanceId, Identifier activeAnimationId, Vec3d fallbackStart, Vec3d desiredTarget, long nowTick, float tickDelta) {
        boolean targetChanged;
        if (desiredTarget == null || !afw$isFinite(desiredTarget)) {
            return fallbackStart;
        }
        boolean bl = targetChanged = afw$orbitSmoothTarget == null || afw$orbitSmoothTarget.squaredDistanceTo(desiredTarget) >= 1.0E-6;
        if (!Objects.equals(activeInstanceId, afw$smoothedOrbitInstanceId) || !Objects.equals(activeAnimationId, afw$smoothedOrbitAnimationId) || targetChanged) {
            afw$smoothedOrbitInstanceId = activeInstanceId;
            afw$smoothedOrbitAnimationId = activeAnimationId;
            afw$orbitSmoothStart = afw$currentSmoothedOrbitTarget != null ? afw$currentSmoothedOrbitTarget : fallbackStart;
            afw$orbitSmoothTarget = desiredTarget;
            afw$orbitSmoothStartTick = nowTick;
        }
        if (afw$orbitSmoothStart == null || afw$orbitSmoothTarget == null) {
            afw$currentSmoothedOrbitTarget = desiredTarget;
            return desiredTarget;
        }
        float progress = MathHelper.clamp((float)(((float)(nowTick - afw$orbitSmoothStartTick) + tickDelta) / 6.0f), (float)0.0f, (float)1.0f);
        float eased = CameraAnimationMixin.afw$smootherStep(progress);
        afw$currentSmoothedOrbitTarget = new Vec3d(MathHelper.lerp((double)eased, (double)CameraAnimationMixin.afw$orbitSmoothStart.x, (double)CameraAnimationMixin.afw$orbitSmoothTarget.x), MathHelper.lerp((double)eased, (double)CameraAnimationMixin.afw$orbitSmoothStart.y, (double)CameraAnimationMixin.afw$orbitSmoothTarget.y), MathHelper.lerp((double)eased, (double)CameraAnimationMixin.afw$orbitSmoothStart.z, (double)CameraAnimationMixin.afw$orbitSmoothTarget.z));
        if (progress >= 1.0f) {
            afw$orbitSmoothStart = afw$orbitSmoothTarget;
        }
        return afw$currentSmoothedOrbitTarget;
    }

    private static void afw$clearBodyOrbitTarget() {
        afw$bodyOrbitInstanceId = null;
        afw$bodyOrbitAnimationId = null;
        afw$bodyOrbitTarget = null;
        afw$delayBodyOrbitForCurrentInstance = false;
    }

    private static void afw$clearOrbitSmoothing() {
        afw$smoothedOrbitInstanceId = null;
        afw$smoothedOrbitAnimationId = null;
        afw$orbitSmoothStart = null;
        afw$orbitSmoothTarget = null;
        afw$currentSmoothedOrbitTarget = null;
        afw$orbitSmoothStartTick = 0L;
    }

    private static float afw$lerpAngleDegrees(float delta, float start, float end) {
        return start + MathHelper.wrapDegrees((float)(end - start)) * delta;
    }

    private static float afw$smootherStep(float value) {
        float clamped = MathHelper.clamp((float)value, (float)0.0f, (float)1.0f);
        return clamped * clamped * clamped * (clamped * (clamped * 6.0f - 15.0f) + 10.0f);
    }

    private static void afw$resetAnimationCameraState(boolean firstPerson) {
        afw$lastValidPosedPose = null;
        afw$lastConstrainedLook = null;
        afw$lastActiveInstanceId = null;
        afw$startAlignInstanceId = null;
        afw$startAlignPendingTarget = false;
        afw$startAlignActive = false;
        CameraAnimationMixin.afw$clearBodyOrbitTarget();
        CameraAnimationMixin.afw$clearOrbitSmoothing();
        afw$wasFirstPersonLastFrame = firstPerson;
    }
    @Unique
    private static boolean afw$isFinite(Vec3d value) {
        return value != null && Double.isFinite(value.x)
                && Double.isFinite(value.y) && Double.isFinite(value.z);
    }
}

