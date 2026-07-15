/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.client.render.entity.state.EntityRenderState
 *  net.minecraft.client.render.entity.state.BipedEntityRenderState
 *  net.minecraft.client.render.entity.state.LivingEntityRenderState
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.entity.EntityPose
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.entity.EntityRenderer
 *  net.minecraft.client.render.entity.EntityRenderManager
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector3f
 */
package com.afwid.client.compat.wildfire;

import com.afwid.AnimationFramework;
import com.afwid.client.compat.wildfire.AfwWildfireGenderState;
import com.afwid.mixin.client.EntityRenderManagerAccessor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityPose;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRenderManager;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class AfwWildfireGenderCompat {
    private static final String MOD_ID = "wildfire_gender";
    private static final AfwWildfireGenderState.UvRect DEFAULT_LEFT_BASE_UV = AfwWildfireGenderState.UvRect.pixels(20, 20, 24, 26);
    private static final AfwWildfireGenderState.UvRect DEFAULT_RIGHT_BASE_UV = AfwWildfireGenderState.UvRect.pixels(24, 20, 28, 26);
    private static final AfwWildfireGenderState.UvRect DEFAULT_LEFT_OVERLAY_UV = AfwWildfireGenderState.UvRect.pixels(20, 36, 24, 42);
    private static final AfwWildfireGenderState.UvRect DEFAULT_RIGHT_OVERLAY_UV = AfwWildfireGenderState.UvRect.pixels(24, 36, 28, 42);
    private static final float POSITION_X_MIN = -1.25f;
    private static final float POSITION_X_MAX = 1.25f;
    private static final float POSITION_Y_MIN = -1.25f;
    private static final float POSITION_Y_MAX = 1.75f;
    private static final float POSITION_REFERENCE_DELTA = 0.08f;
    private static final float POSITION_RESPONSE_EXPONENT = 0.55f;
    private static final float PHYSICS_TARGET_RESPONSE = 0.1f;
    private static final float GRAVITY_MOVEMENT_BOOST = 0.8f;
    private static final float GRAVITY_SIDE_POSITION_SCALE = 0.35f;
    private static final float GRAVITY_FORWARD_POSITION_SCALE = 0.45f;
    private static final float LOCAL_FORWARD_VERTICAL_SCALE = 0.4f;
    private static final float ROTATION_YAW_DIVISOR = 15.0f;
    private static final float ROTATION_VERTICAL_SCALE = 18.0f;
    private static final float ROTATION_LATERAL_SCALE = 16.0f;
    private static final float ROTATION_FORWARD_SCALE = 20.0f;
    private static final float GRAVITY_SIDE_ROTATION_SCALE = 4.0f;
    private static final float GRAVITY_FORWARD_ROTATION_SCALE = 6.0f;
    private static final float ROTATION_MIN = -25.0f;
    private static final float ROTATION_MAX = 25.0f;
    private static final float CHEST_SAMPLE_LOCAL_X = 0.0f;
    private static final float CHEST_SAMPLE_LOCAL_Y = 0.3f;
    private static final float CHEST_SAMPLE_LOCAL_Z = -0.35f;
    private static final float BOUNCE_INTENSITY_SCALE = 12.0f;
    private static final float BOUNCE_INTENSITY_MIN = 0.15f;
    private static final float BOUNCE_INTENSITY_MAX = 3.0f;
    private static final float RENDER_GRAVITY_POSITION_X_SCALE = 0.45f;
    private static final float RENDER_GRAVITY_POSITION_Y_SCALE = 0.85f;
    private static final float RENDER_GRAVITY_ROTATION_SIDE_SCALE = 10.0f;
    private static final float RENDER_GRAVITY_ROTATION_FORWARD_SCALE = 2.0f;
    private static final float RENDER_GRAVITY_POSITION_X_LIMIT = 0.8f;
    private static final float RENDER_GRAVITY_POSITION_Y_LIMIT = 1.2f;
    private static final float RENDER_GRAVITY_ROTATION_LIMIT = 10.0f;
    private static boolean initialized;
    private static boolean available;
    private static boolean loggedFailure;
    private static Method getRenderState;
    private static Field genderField;
    private static Field bustSizeField;
    private static Field armorPhysicsOverrideField;
    private static Field showBreastsInArmorField;
    private static Field hasJacketLayerField;
    private static Field breastsField;
    private static Field leftPhysicsField;
    private static Field rightPhysicsField;
    private static Field leftUvField;
    private static Field rightUvField;
    private static Field leftOverlayUvField;
    private static Field rightOverlayUvField;
    private static Method genderCanHaveBreasts;
    private static Field breastXOffsetField;
    private static Field breastYOffsetField;
    private static Field breastZOffsetField;
    private static Field breastCleavageField;
    private static Field breastUniboobField;
    private static Method physicsPositionXMethod;
    private static Method physicsPositionYMethod;
    private static Method physicsBreastSizeMethod;
    private static Method physicsBounceRotationMethod;
    private static Field physicsPrePositionXField;
    private static Field physicsPositionXField;
    private static Field physicsPrePositionYField;
    private static Field physicsPositionYField;
    private static Field physicsPreBounceRotationField;
    private static Field physicsBounceRotationField;
    private static Method uvGetAllSidesMethod;
    private static Method uvQuadX1Method;
    private static Method uvQuadY1Method;
    private static Method uvQuadX2Method;
    private static Method uvQuadY2Method;
    private static Class<?> genderLayerClass;
    private static Method genderLayerSubmitMethod;
    private static Method contextModelMethod;
    private static Field modelBodyField;
    private static Field bodyOriginXField;
    private static Field bodyOriginYField;
    private static Field bodyOriginZField;
    private static Field bodyPitchField;
    private static Field bodyYawField;
    private static Field bodyRollField;
    private static Method entityConfigGetEntityMethod;
    private static Constructor<?> breastPhysicsConstructor;
    private static Method breastPhysicsFinishTickMethod;
    private static Field breastPhysicsTargetBounceXField;
    private static Field breastPhysicsTargetBounceYField;
    private static Field breastPhysicsTargetRotVelField;
    private static Field breastPhysicsPrePositionXField;
    private static Field breastPhysicsPositionXField;
    private static Field breastPhysicsPrePositionYField;
    private static Field breastPhysicsPositionYField;
    private static Field breastPhysicsPreBounceRotationField;
    private static Field breastPhysicsBounceRotationField;
    private static final Map<Object, Object> GENDER_LAYERS;
    private static final Map<UUID, AfwPhysicsPair> AFW_PHYSICS;

    private AfwWildfireGenderCompat() {
    }

    public static AfwWildfireGenderState resolve(LivingEntity entity, LivingEntityRenderState renderState) {
        if (entity == null || renderState == null) {
            return null;
        }
        if (!AfwWildfireGenderCompat.isAvailable()) {
            return null;
        }
        try {
            Object wildfireState = getRenderState.invoke(null, renderState);
            if (wildfireState == null) {
                return null;
            }
            Object gender = genderField.get(wildfireState);
            if (!AfwWildfireGenderCompat.canHaveBreasts(gender)) {
                return null;
            }
            Object breasts = breastsField.get(wildfireState);
            if (breasts == null) {
                return null;
            }
            AfwWildfireGenderCompat.cacheAfwPhysics(entity);
            float bustSize = AfwWildfireGenderCompat.getFloat(bustSizeField, wildfireState, 0.0f);
            if (bustSize <= 0.02f) {
                return null;
            }
            boolean hasJacketLayer = AfwWildfireGenderCompat.getBoolean(hasJacketLayerField, wildfireState, false);
            float xOffset = AfwWildfireGenderCompat.getFloat(breastXOffsetField, breasts, 0.0f);
            float yOffset = AfwWildfireGenderCompat.getFloat(breastYOffsetField, breasts, 0.0f);
            float zOffset = AfwWildfireGenderCompat.getFloat(breastZOffsetField, breasts, 0.0f);
            float cleavage = AfwWildfireGenderCompat.getFloat(breastCleavageField, breasts, 0.05f);
            boolean uniboob = AfwWildfireGenderCompat.getBoolean(breastUniboobField, breasts, false);
            return new AfwWildfireGenderState(bustSize, xOffset, yOffset, zOffset, cleavage, uniboob, hasJacketLayer, AfwWildfireGenderCompat.readSide(leftPhysicsField.get(wildfireState), leftUvField.get(wildfireState), leftOverlayUvField.get(wildfireState), DEFAULT_LEFT_BASE_UV, DEFAULT_LEFT_OVERLAY_UV), AfwWildfireGenderCompat.readSide(rightPhysicsField.get(wildfireState), rightUvField.get(wildfireState), rightOverlayUvField.get(wildfireState), DEFAULT_RIGHT_BASE_UV, DEFAULT_RIGHT_OVERLAY_UV));
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            AfwWildfireGenderCompat.logFailure("Failed to read Female Gender render state.", e);
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean submitOriginalLayer(LivingEntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, int packedLight) {
        boolean bl;
        if (renderState == null || matrices == null || queue == null) {
            return false;
        }
        if (!AfwWildfireGenderCompat.isAvailable()) {
            return false;
        }
        Object layer = AfwWildfireGenderCompat.getOrCreateOriginalLayer(renderState);
        if (layer == null || genderLayerSubmitMethod == null) {
            return false;
        }
        Object wildfireState = getRenderState.invoke(null, renderState);
        Boolean previousShowBreastsInArmor = AfwWildfireGenderCompat.forceShowBreastsInArmor(wildfireState);
        Boolean previousArmorPhysicsOverride = AfwWildfireGenderCompat.forceArmorPhysicsOverride(wildfireState);
        RenderPoseSnapshot renderPose = AfwWildfireGenderCompat.neutralizeRenderPose(renderState);
        BodyTransformSnapshot bodyTransform = AfwWildfireGenderCompat.neutralizeContextBodyTransform(layer);
        try {
            genderLayerSubmitMethod.invoke(layer, matrices, queue, packedLight, renderState, Float.valueOf(0.0f), Float.valueOf(0.0f));
            bl = true;
        }
        catch (Throwable throwable) {
            try {
                AfwWildfireGenderCompat.restoreContextBodyTransform(bodyTransform);
                AfwWildfireGenderCompat.restoreRenderPose(renderState, renderPose);
                AfwWildfireGenderCompat.restoreShowBreastsInArmor(wildfireState, previousShowBreastsInArmor);
                AfwWildfireGenderCompat.restoreArmorPhysicsOverride(wildfireState, previousArmorPhysicsOverride);
                throw throwable;
            }
            catch (ReflectiveOperationException | RuntimeException e) {
                AfwWildfireGenderCompat.logFailure("Failed to submit Female Gender original render layer.", e);
                return false;
            }
        }
        AfwWildfireGenderCompat.restoreContextBodyTransform(bodyTransform);
        AfwWildfireGenderCompat.restoreRenderPose(renderState, renderPose);
        AfwWildfireGenderCompat.restoreShowBreastsInArmor(wildfireState, previousShowBreastsInArmor);
        AfwWildfireGenderCompat.restoreArmorPhysicsOverride(wildfireState, previousArmorPhysicsOverride);
        return bl;
    }

    @Nullable
    private static Boolean forceShowBreastsInArmor(@Nullable Object wildfireState) throws IllegalAccessException {
        if (wildfireState == null || showBreastsInArmorField == null) {
            return null;
        }
        boolean previous = showBreastsInArmorField.getBoolean(wildfireState);
        showBreastsInArmorField.setBoolean(wildfireState, true);
        return previous;
    }

    private static void restoreShowBreastsInArmor(@Nullable Object wildfireState, @Nullable Boolean previous) throws IllegalAccessException {
        if (wildfireState == null || previous == null || showBreastsInArmorField == null) {
            return;
        }
        showBreastsInArmorField.setBoolean(wildfireState, previous);
    }

    @Nullable
    private static Boolean forceArmorPhysicsOverride(@Nullable Object wildfireState) throws IllegalAccessException {
        if (wildfireState == null || armorPhysicsOverrideField == null) {
            return null;
        }
        boolean previous = armorPhysicsOverrideField.getBoolean(wildfireState);
        armorPhysicsOverrideField.setBoolean(wildfireState, true);
        return previous;
    }

    private static void restoreArmorPhysicsOverride(@Nullable Object wildfireState, @Nullable Boolean previous) throws IllegalAccessException {
        if (wildfireState == null || previous == null || armorPhysicsOverrideField == null) {
            return;
        }
        armorPhysicsOverrideField.setBoolean(wildfireState, previous);
    }

    public static void overrideRenderPhysics(UUID playerUuid, LivingEntityRenderState renderState, MatrixStack rootPose, MatrixStack bonePose) {
        if (playerUuid == null || renderState == null || rootPose == null || bonePose == null) {
            return;
        }
        if (!AfwWildfireGenderCompat.isAvailable()) {
            return;
        }
        try {
            Object wildfireState = getRenderState.invoke(null, renderState);
            if (wildfireState == null) {
                return;
            }
            AfwPhysicsPair physicsPair = AFW_PHYSICS.get(playerUuid);
            if (physicsPair == null) {
                return;
            }
            BonePhysicsSample sample = BonePhysicsSample.from(rootPose, bonePose);
            if (sample == null) {
                return;
            }
            long worldTick = AfwWildfireGenderCompat.currentClientWorldTick();
            if (physicsPair.lastSample == null) {
                physicsPair.lastSample = sample;
                physicsPair.lastTick = worldTick;
            } else if (physicsPair.lastTick != worldTick) {
                AfwWildfireGenderCompat.stepAfwPhysics(wildfireState, physicsPair, physicsPair.lastSample, sample);
                physicsPair.lastSample = sample;
                physicsPair.lastTick = worldTick;
            }
            Object leftPhysics = leftPhysicsField.get(wildfireState);
            Object rightPhysics = rightPhysicsField.get(wildfireState);
            AfwWildfireGenderCompat.copyRealPhysicsToRenderState(leftPhysics, physicsPair.left, physicsPair.previousLeftBias, physicsPair.leftBias);
            AfwWildfireGenderCompat.copyRealPhysicsToRenderState(rightPhysics, physicsPair.right, physicsPair.previousRightBias, physicsPair.rightBias);
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            AfwWildfireGenderCompat.logFailure("Failed to override Female Gender render physics from AFW bone motion.", e);
        }
    }

    private static void stepAfwPhysics(Object wildfireState, AfwPhysicsPair physicsPair, BonePhysicsSample previous, BonePhysicsSample current) throws ReflectiveOperationException {
        float dx = current.chestX() - previous.chestX();
        float dy = current.chestY() - previous.chestY();
        float dz = current.chestZ() - previous.chestZ();
        Vec3d delta = new Vec3d((double)dx, (double)dy, (double)dz);
        float lateral = (float)delta.dotProduct(previous.right());
        float localUp = (float)delta.dotProduct(previous.up());
        float localForward = (float)delta.dotProduct(previous.forward());
        float yawDelta = AfwWildfireGenderCompat.signedYawDelta(previous.forward(), current.forward());
        GravityBias gravity = GravityBias.from(current);
        float movementBoost = 1.0f + gravity.tiltInfluence() * 0.8f;
        float verticalMovement = localUp + localForward * gravity.forwardDown() * 0.4f;
        float bustSize = AfwWildfireGenderCompat.getFloat(bustSizeField, wildfireState, 0.0f);
        float bounceIntensity = AfwWildfireGenderCompat.clamp(bustSize * 12.0f, 0.15f, 3.0f);
        float targetX = AfwWildfireGenderCompat.shapedImpulse(lateral * movementBoost, 0.08f, -1.25f, 1.25f, 0.55f);
        float targetY = AfwWildfireGenderCompat.shapedImpulse(-verticalMovement * movementBoost, 0.08f, -1.25f, 1.75f, 0.55f);
        targetX = AfwWildfireGenderCompat.clamp(targetX + gravity.sidePull() * 0.35f, -1.25f, 1.25f);
        targetY = AfwWildfireGenderCompat.clamp(targetY + gravity.forwardDown() * 0.45f, -1.25f, 1.75f);
        float targetRotVel = AfwWildfireGenderCompat.clamp(-(yawDelta / 15.0f) * bounceIntensity + -localUp * 18.0f * bounceIntensity + lateral * 16.0f * bounceIntensity + localForward * gravity.forwardDown() * 20.0f * bounceIntensity + gravity.sidePull() * 4.0f * bounceIntensity + gravity.forwardDown() * 6.0f * bounceIntensity, -25.0f, 25.0f);
        boolean uniboob = AfwWildfireGenderCompat.isUniboob(wildfireState);
        physicsPair.stepRenderBias(gravity, uniboob);
        TargetImpulse smoothedTarget = physicsPair.smoothTarget(targetX, targetY, targetRotVel);
        if (uniboob) {
            AfwWildfireGenderCompat.driveRealPhysics(physicsPair.left, smoothedTarget.x(), smoothedTarget.y(), 0.0f);
            AfwWildfireGenderCompat.driveRealPhysics(physicsPair.right, smoothedTarget.x(), smoothedTarget.y(), 0.0f);
        } else {
            AfwWildfireGenderCompat.driveRealPhysics(physicsPair.left, smoothedTarget.x(), smoothedTarget.y(), smoothedTarget.rotation());
            AfwWildfireGenderCompat.driveRealPhysics(physicsPair.right, -smoothedTarget.x() * 0.85f, smoothedTarget.y() * 0.95f, -smoothedTarget.rotation() * 0.85f);
        }
    }

    private static void cacheAfwPhysics(LivingEntity entity) throws ReflectiveOperationException {
        if (entity == null || entityConfigGetEntityMethod == null) {
            return;
        }
        Object config = entityConfigGetEntityMethod.invoke(null, entity);
        if (config == null) {
            return;
        }
        UUID uuid = entity.getUuid();
        AfwPhysicsPair existing = AFW_PHYSICS.get(uuid);
        if (existing != null && existing.config == config) {
            return;
        }
        Object left = breastPhysicsConstructor.newInstance(config);
        Object right = breastPhysicsConstructor.newInstance(config);
        AFW_PHYSICS.put(uuid, new AfwPhysicsPair(config, left, right));
    }

    private static void driveRealPhysics(Object physics, float targetX, float targetY, float targetRotVel) throws ReflectiveOperationException {
        if (physics == null) {
            return;
        }
        breastPhysicsPrePositionXField.setFloat(physics, breastPhysicsPositionXField.getFloat(physics));
        breastPhysicsPrePositionYField.setFloat(physics, breastPhysicsPositionYField.getFloat(physics));
        breastPhysicsPreBounceRotationField.setFloat(physics, breastPhysicsBounceRotationField.getFloat(physics));
        breastPhysicsTargetBounceXField.setFloat(physics, targetX);
        breastPhysicsTargetBounceYField.setFloat(physics, targetY);
        breastPhysicsTargetRotVelField.setFloat(physics, targetRotVel);
        breastPhysicsFinishTickMethod.invoke(physics, new Object[0]);
    }

    private static void copyRealPhysicsToRenderState(Object renderPhysics, Object realPhysics, SideRenderBias previousBias, SideRenderBias bias) throws IllegalAccessException {
        if (renderPhysics == null || realPhysics == null) {
            return;
        }
        if (physicsPositionXField != null && physicsPrePositionXField != null) {
            physicsPrePositionXField.setFloat(renderPhysics, breastPhysicsPrePositionXField.getFloat(realPhysics) + previousBias.x());
            physicsPositionXField.setFloat(renderPhysics, breastPhysicsPositionXField.getFloat(realPhysics) + bias.x());
        }
        if (physicsPositionYField != null && physicsPrePositionYField != null) {
            physicsPrePositionYField.setFloat(renderPhysics, breastPhysicsPrePositionYField.getFloat(realPhysics) + previousBias.y());
            physicsPositionYField.setFloat(renderPhysics, breastPhysicsPositionYField.getFloat(realPhysics) + bias.y());
        }
        if (physicsBounceRotationField != null && physicsPreBounceRotationField != null) {
            physicsPreBounceRotationField.setFloat(renderPhysics, breastPhysicsPreBounceRotationField.getFloat(realPhysics) + previousBias.rotation());
            physicsBounceRotationField.setFloat(renderPhysics, breastPhysicsBounceRotationField.getFloat(realPhysics) + bias.rotation());
        }
    }

    private static long currentClientWorldTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) {
            return Long.MIN_VALUE;
        }
        return client.world.getTime();
    }

    private static boolean isUniboob(Object wildfireState) throws ReflectiveOperationException {
        Object breasts = breastsField.get(wildfireState);
        return breasts != null && AfwWildfireGenderCompat.getBoolean(breastUniboobField, breasts, false);
    }

    private static Object getOrCreateOriginalLayer(LivingEntityRenderState renderState) throws ReflectiveOperationException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getEntityRenderDispatcher() == null) {
            return null;
        }
        EntityRenderManager TrialSpawnerDetectionParticle = client.getEntityRenderDispatcher();
        if (!(TrialSpawnerDetectionParticle instanceof EntityRenderManagerAccessor)) {
            return null;
        }
        EntityRenderManagerAccessor accessor = (EntityRenderManagerAccessor)TrialSpawnerDetectionParticle;
        EntityRenderer<?, ?> renderer = accessor.afw$getRenderer((EntityRenderState)renderState);
        if (renderer == null) {
            return null;
        }
        Object cached = GENDER_LAYERS.get(renderer);
        if (cached != null) {
            return cached;
        }
        Constructor<?> constructor = null;
        for (Constructor<?> candidate : genderLayerClass.getConstructors()) {
            Class<?> parameter;
            if (candidate.getParameterCount() != 1 || !(parameter = candidate.getParameterTypes()[0]).isAssignableFrom(renderer.getClass())) continue;
            candidate.setAccessible(true);
            constructor = candidate;
            break;
        }
        if (constructor == null) {
            return null;
        }
        Object created = constructor.newInstance(renderer);
        GENDER_LAYERS.put(renderer, created);
        return created;
    }

    /*
     * Unable to fully structure code
     */
    @Nullable
    private static RenderPoseSnapshot neutralizeRenderPose(LivingEntityRenderState renderState) {
        if (renderState == null) {
            return null;
        }
        v0 = renderState.pose;
        if (!(renderState instanceof BipedEntityRenderState)) ** GOTO lbl-1000
        biped = (BipedEntityRenderState)renderState;
        if (biped.isInSneakingPose) {
            v1 = true;
        } else lbl-1000:
        // 2 sources

        {
            v1 = false;
        }
        snapshot = new RenderPoseSnapshot(v0, v1);
        renderState.pose = EntityPose.STANDING;
        if (renderState instanceof BipedEntityRenderState) {
            biped = (BipedEntityRenderState)renderState;
            biped.isInSneakingPose = false;
        }
        return snapshot;
    }

    private static void restoreRenderPose(LivingEntityRenderState renderState, @Nullable RenderPoseSnapshot snapshot) {
        if (renderState == null || snapshot == null) {
            return;
        }
        renderState.pose = snapshot.pose();
        if (renderState instanceof BipedEntityRenderState) {
            BipedEntityRenderState biped = (BipedEntityRenderState)renderState;
            biped.isInSneakingPose = snapshot.sneaking();
        }
    }

    private static BodyTransformSnapshot neutralizeContextBodyTransform(Object layer) throws IllegalAccessException {
        Object body = AfwWildfireGenderCompat.getContextBody(layer);
        if (body == null || bodyOriginXField == null || bodyOriginYField == null || bodyOriginZField == null || bodyPitchField == null || bodyYawField == null || bodyRollField == null) {
            return null;
        }
        BodyTransformSnapshot snapshot = new BodyTransformSnapshot(body, bodyOriginXField.getFloat(body), bodyOriginYField.getFloat(body), bodyOriginZField.getFloat(body), bodyPitchField.getFloat(body), bodyYawField.getFloat(body), bodyRollField.getFloat(body));
        bodyOriginXField.setFloat(body, 0.0f);
        bodyOriginYField.setFloat(body, 0.0f);
        bodyOriginZField.setFloat(body, 0.0f);
        bodyPitchField.setFloat(body, 0.0f);
        bodyYawField.setFloat(body, 0.0f);
        bodyRollField.setFloat(body, 0.0f);
        return snapshot;
    }

    private static void restoreContextBodyTransform(BodyTransformSnapshot snapshot) throws IllegalAccessException {
        if (snapshot == null) {
            return;
        }
        bodyOriginXField.setFloat(snapshot.body(), snapshot.originX());
        bodyOriginYField.setFloat(snapshot.body(), snapshot.originY());
        bodyOriginZField.setFloat(snapshot.body(), snapshot.originZ());
        bodyPitchField.setFloat(snapshot.body(), snapshot.pitch());
        bodyYawField.setFloat(snapshot.body(), snapshot.yaw());
        bodyRollField.setFloat(snapshot.body(), snapshot.roll());
    }

    private static Object getContextBody(Object layer) {
        try {
            if (contextModelMethod == null) {
                contextModelMethod = AfwWildfireGenderCompat.findNoArgMethod(layer.getClass(), "getContextModel", "getContextModel");
            }
            if (contextModelMethod == null) {
                return null;
            }
            Object model = contextModelMethod.invoke(layer, new Object[0]);
            if (model == null) {
                return null;
            }
            if (modelBodyField == null) {
                modelBodyField = AfwWildfireGenderCompat.findField(model.getClass(), "body", "body");
            }
            if (modelBodyField == null) {
                return null;
            }
            Object body = modelBodyField.get(model);
            if (body == null) {
                return null;
            }
            Class<?> bodyClass = body.getClass();
            if (bodyOriginXField == null) {
                bodyOriginXField = AfwWildfireGenderCompat.findField(bodyClass, "originX", "pivotX", "originX");
            }
            if (bodyOriginYField == null) {
                bodyOriginYField = AfwWildfireGenderCompat.findField(bodyClass, "originY", "pivotY", "originY");
            }
            if (bodyOriginZField == null) {
                bodyOriginZField = AfwWildfireGenderCompat.findField(bodyClass, "originZ", "pivotZ", "originZ");
            }
            if (bodyPitchField == null) {
                bodyPitchField = AfwWildfireGenderCompat.findField(bodyClass, "pitch", "xRot", "roll");
            }
            if (bodyYawField == null) {
                bodyYawField = AfwWildfireGenderCompat.findField(bodyClass, "yaw", "yRot", "yaw");
            }
            if (bodyRollField == null) {
                bodyRollField = AfwWildfireGenderCompat.findField(bodyClass, "roll", "zRot", "pitch");
            }
            return body;
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            AfwWildfireGenderCompat.logFailure("Failed to neutralize Female Gender vanilla body transform.", e);
            return null;
        }
    }

    private static AfwWildfireGenderState.Side readSide(Object physics, Object baseUv, Object overlayUv, AfwWildfireGenderState.UvRect defaultBase, AfwWildfireGenderState.UvRect defaultOverlay) throws ReflectiveOperationException {
        return new AfwWildfireGenderState.Side(AfwWildfireGenderCompat.invokeFloat(physicsPositionXMethod, physics, 0.0f), AfwWildfireGenderCompat.invokeFloat(physicsPositionYMethod, physics, 0.0f), AfwWildfireGenderCompat.invokeFloat(physicsBreastSizeMethod, physics, 0.0f), AfwWildfireGenderCompat.invokeFloat(physicsBounceRotationMethod, physics, 0.0f), AfwWildfireGenderCompat.readNorthUv(baseUv, defaultBase), AfwWildfireGenderCompat.readNorthUv(overlayUv, defaultOverlay));
    }

    private static AfwWildfireGenderState.UvRect readNorthUv(Object uvLayout, AfwWildfireGenderState.UvRect fallback) throws ReflectiveOperationException {
        if (uvLayout == null || uvGetAllSidesMethod == null) {
            return fallback;
        }
        Object sides = uvGetAllSidesMethod.invoke(uvLayout, new Object[0]);
        if (!(sides instanceof Map)) {
            return fallback;
        }
        Map map = (Map)sides;
        for (Map.Entry entry : map.entrySet()) {
            Object direction = entry.getKey();
            Object quad = entry.getValue();
            if (direction == null || quad == null || !"NORTH".equals(String.valueOf(direction))) continue;
            int x1 = (Integer)uvQuadX1Method.invoke(quad, new Object[0]);
            int y1 = (Integer)uvQuadY1Method.invoke(quad, new Object[0]);
            int x2 = (Integer)uvQuadX2Method.invoke(quad, new Object[0]);
            int y2 = (Integer)uvQuadY2Method.invoke(quad, new Object[0]);
            return AfwWildfireGenderState.UvRect.pixels(x1, y1, x2, y2);
        }
        return fallback;
    }

    private static boolean canHaveBreasts(Object gender) throws ReflectiveOperationException {
        Object value;
        if (gender == null) {
            return false;
        }
        if (genderCanHaveBreasts != null && (value = genderCanHaveBreasts.invoke(gender, new Object[0])) instanceof Boolean) {
            Boolean bool = (Boolean)value;
            return bool;
        }
        return !"MALE".equals(String.valueOf(gender));
    }

    private static boolean isAvailable() {
        if (initialized) {
            return available;
        }
        initialized = true;
        if (!FabricLoader.getInstance().isModLoaded(MOD_ID)) {
            available = false;
            return false;
        }
        try {
            Class<?> renderStateClass = Class.forName("com.wildfire.render.GenderRenderState");
            genderLayerClass = Class.forName("com.wildfire.render.GenderLayer");
            getRenderState = AfwWildfireGenderCompat.findStaticMethod(renderStateClass, "get", 1);
            genderLayerSubmitMethod = AfwWildfireGenderCompat.findInstanceMethod(genderLayerClass, "submit", 6);
            genderField = AfwWildfireGenderCompat.field(renderStateClass, "gender");
            bustSizeField = AfwWildfireGenderCompat.field(renderStateClass, "bustSize");
            armorPhysicsOverrideField = AfwWildfireGenderCompat.field(renderStateClass, "armorPhysicsOverride");
            showBreastsInArmorField = AfwWildfireGenderCompat.field(renderStateClass, "showBreastsInArmor");
            hasJacketLayerField = AfwWildfireGenderCompat.field(renderStateClass, "hasJacketLayer");
            breastsField = AfwWildfireGenderCompat.field(renderStateClass, "breasts");
            leftPhysicsField = AfwWildfireGenderCompat.field(renderStateClass, "leftBreastPhysics");
            rightPhysicsField = AfwWildfireGenderCompat.field(renderStateClass, "rightBreastPhysics");
            leftUvField = AfwWildfireGenderCompat.field(renderStateClass, "leftBreastUVLayout");
            rightUvField = AfwWildfireGenderCompat.field(renderStateClass, "rightBreastUVLayout");
            leftOverlayUvField = AfwWildfireGenderCompat.field(renderStateClass, "leftBreastOverlayUVLayout");
            rightOverlayUvField = AfwWildfireGenderCompat.field(renderStateClass, "rightBreastOverlayUVLayout");
            Class<?> genderClass = Class.forName("com.wildfire.main.config.enums.Gender");
            genderCanHaveBreasts = AfwWildfireGenderCompat.methodOrNull(genderClass, "canHaveBreasts");
            Class<?> breastStateClass = Class.forName("com.wildfire.render.GenderRenderState$BreastState");
            breastXOffsetField = AfwWildfireGenderCompat.field(breastStateClass, "xOffset");
            breastYOffsetField = AfwWildfireGenderCompat.field(breastStateClass, "yOffset");
            breastZOffsetField = AfwWildfireGenderCompat.field(breastStateClass, "zOffset");
            breastCleavageField = AfwWildfireGenderCompat.field(breastStateClass, "cleavage");
            breastUniboobField = AfwWildfireGenderCompat.field(breastStateClass, "uniboob");
            Class<?> physicsClass = Class.forName("com.wildfire.render.GenderRenderState$BreastPhysicsState");
            physicsPositionXMethod = AfwWildfireGenderCompat.methodOrNull(physicsClass, "getPositionX");
            physicsPositionYMethod = AfwWildfireGenderCompat.methodOrNull(physicsClass, "getPositionY");
            physicsBreastSizeMethod = AfwWildfireGenderCompat.methodOrNull(physicsClass, "getBreastSize");
            physicsBounceRotationMethod = AfwWildfireGenderCompat.methodOrNull(physicsClass, "getBounceRotation");
            physicsPrePositionXField = AfwWildfireGenderCompat.declaredField(physicsClass, "prePositionX");
            physicsPositionXField = AfwWildfireGenderCompat.declaredField(physicsClass, "positionX");
            physicsPrePositionYField = AfwWildfireGenderCompat.declaredField(physicsClass, "prePositionY");
            physicsPositionYField = AfwWildfireGenderCompat.declaredField(physicsClass, "positionY");
            physicsPreBounceRotationField = AfwWildfireGenderCompat.declaredField(physicsClass, "preBounceRotation");
            physicsBounceRotationField = AfwWildfireGenderCompat.declaredField(physicsClass, "bounceRotation");
            Class<?> entityConfigClass = Class.forName("com.wildfire.main.entitydata.EntityConfig");
            entityConfigGetEntityMethod = AfwWildfireGenderCompat.findStaticMethod(entityConfigClass, "getEntity", 1);
            Class<?> breastPhysicsClass = Class.forName("com.wildfire.physics.BreastPhysics");
            breastPhysicsConstructor = breastPhysicsClass.getConstructor(entityConfigClass);
            breastPhysicsConstructor.setAccessible(true);
            breastPhysicsFinishTickMethod = AfwWildfireGenderCompat.declaredMethod(breastPhysicsClass, "finishTick");
            breastPhysicsTargetBounceXField = AfwWildfireGenderCompat.declaredField(breastPhysicsClass, "targetBounceX");
            breastPhysicsTargetBounceYField = AfwWildfireGenderCompat.declaredField(breastPhysicsClass, "targetBounceY");
            breastPhysicsTargetRotVelField = AfwWildfireGenderCompat.declaredField(breastPhysicsClass, "targetRotVel");
            breastPhysicsPrePositionXField = AfwWildfireGenderCompat.declaredField(breastPhysicsClass, "prePositionX");
            breastPhysicsPositionXField = AfwWildfireGenderCompat.declaredField(breastPhysicsClass, "positionX");
            breastPhysicsPrePositionYField = AfwWildfireGenderCompat.declaredField(breastPhysicsClass, "prePositionY");
            breastPhysicsPositionYField = AfwWildfireGenderCompat.declaredField(breastPhysicsClass, "positionY");
            breastPhysicsPreBounceRotationField = AfwWildfireGenderCompat.declaredField(breastPhysicsClass, "wfg_preBounceRotation");
            breastPhysicsBounceRotationField = AfwWildfireGenderCompat.declaredField(breastPhysicsClass, "wfg_bounceRotation");
            Class<?> uvLayoutClass = Class.forName("com.wildfire.main.uvs.UVLayout");
            uvGetAllSidesMethod = AfwWildfireGenderCompat.methodOrNull(uvLayoutClass, "getAllSides");
            Class<?> uvQuadClass = Class.forName("com.wildfire.main.uvs.UVQuad");
            uvQuadX1Method = AfwWildfireGenderCompat.methodOrNull(uvQuadClass, "x1");
            uvQuadY1Method = AfwWildfireGenderCompat.methodOrNull(uvQuadClass, "y1");
            uvQuadX2Method = AfwWildfireGenderCompat.methodOrNull(uvQuadClass, "x2");
            uvQuadY2Method = AfwWildfireGenderCompat.methodOrNull(uvQuadClass, "y2");
            available = getRenderState != null && genderLayerSubmitMethod != null && entityConfigGetEntityMethod != null && breastPhysicsConstructor != null && breastPhysicsFinishTickMethod != null && breastPhysicsTargetBounceXField != null && breastPhysicsTargetBounceYField != null && breastPhysicsTargetRotVelField != null && breastPhysicsPreBounceRotationField != null && breastPhysicsBounceRotationField != null;
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            available = false;
            AfwWildfireGenderCompat.logFailure("Female Gender compatibility disabled: unsupported API shape.", e);
        }
        return available;
    }

    private static Field field(Class<?> owner, String name) throws NoSuchFieldException {
        Field field = owner.getField(name);
        field.setAccessible(true);
        return field;
    }

    private static Field declaredField(Class<?> owner, String name) throws NoSuchFieldException {
        Field field = owner.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    private static Method declaredMethod(Class<?> owner, String name) throws NoSuchMethodException {
        Method method = owner.getDeclaredMethod(name, new Class[0]);
        method.setAccessible(true);
        return method;
    }

    private static Field findField(Class<?> owner, String ... names) {
        for (Class<?> current = owner; current != null && current != Object.class; current = current.getSuperclass()) {
            for (String name : names) {
                try {
                    Field field = current.getDeclaredField(name);
                    field.setAccessible(true);
                    return field;
                }
                catch (NoSuchFieldException noSuchFieldException) {
                }
            }
        }
        return null;
    }

    private static Method findStaticMethod(Class<?> owner, String name, int parameterCount) {
        for (Method method : owner.getMethods()) {
            if (!method.getName().equals(name) || method.getParameterCount() != parameterCount || !Modifier.isStatic(method.getModifiers())) continue;
            method.setAccessible(true);
            return method;
        }
        return null;
    }

    private static Method findInstanceMethod(Class<?> owner, String name, int parameterCount) {
        for (Method method : owner.getMethods()) {
            if (!method.getName().equals(name) || method.getParameterCount() != parameterCount || Modifier.isStatic(method.getModifiers())) continue;
            method.setAccessible(true);
            return method;
        }
        return null;
    }

    private static Method methodOrNull(Class<?> owner, String name) {
        try {
            Method method = owner.getMethod(name, new Class[0]);
            method.setAccessible(true);
            return method;
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findNoArgMethod(Class<?> owner, String ... names) {
        for (Class<?> current = owner; current != null && current != Object.class; current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                if (method.getParameterCount() != 0 || Modifier.isStatic(method.getModifiers())) continue;
                for (String name : names) {
                    if (!method.getName().equals(name)) continue;
                    method.setAccessible(true);
                    return method;
                }
            }
        }
        return null;
    }

    private static float invokeFloat(Method method, Object target, float fallback) throws ReflectiveOperationException {
        float f;
        if (method == null || target == null) {
            return fallback;
        }
        Object value = method.invoke(target, new Object[0]);
        if (value instanceof Number) {
            Number number = (Number)value;
            f = number.floatValue();
        } else {
            f = fallback;
        }
        return f;
    }

    private static float getFloat(Field field, Object target, float fallback) throws IllegalAccessException {
        if (field == null || target == null) {
            return fallback;
        }
        return field.getFloat(target);
    }

    private static boolean getBoolean(Field field, Object target, boolean fallback) throws IllegalAccessException {
        if (field == null || target == null) {
            return fallback;
        }
        return field.getBoolean(target);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float shapedImpulse(float value, float reference, float min, float max, float exponent) {
        if (Math.abs(value) < 1.0E-6f) {
            return 0.0f;
        }
        float magnitude = Math.min(Math.abs(value) / Math.max(reference, 1.0E-6f), 1.0f);
        float shaped = (float)Math.pow(magnitude, exponent);
        return Math.signum(value) * (value < 0.0f ? Math.abs(min) : max) * shaped;
    }

    private static float signedYawDelta(Vec3d previousForward, Vec3d currentForward) {
        if (previousForward == null || currentForward == null) {
            return 0.0f;
        }
        Vec3d a = new Vec3d(previousForward.x, 0.0, previousForward.z);
        Vec3d b = new Vec3d(currentForward.x, 0.0, currentForward.z);
        if (a.lengthSquared() < 1.0E-8 || b.lengthSquared() < 1.0E-8) {
            return 0.0f;
        }
        a = a.normalize();
        b = b.normalize();
        double dot = MathHelper.clamp((double)a.dotProduct(b), (double)-1.0, (double)1.0);
        double crossY = a.z * b.x - a.x * b.z;
        return (float)Math.toDegrees(Math.atan2(crossY, dot));
    }

    private static void logFailure(String message, Throwable error) {
        if (loggedFailure) {
            return;
        }
        loggedFailure = true;
        AnimationFramework.LOGGER.warn("[AFW] {}", (Object)message, (Object)error);
    }

    static {
        GENDER_LAYERS = new IdentityHashMap<Object, Object>();
        AFW_PHYSICS = new HashMap<UUID, AfwPhysicsPair>();
    }

    private record RenderPoseSnapshot(EntityPose pose, boolean sneaking) {
    }

    private record BodyTransformSnapshot(Object body, float originX, float originY, float originZ, float pitch, float yaw, float roll) {
    }

    private static final class AfwPhysicsPair {
        private final Object config;
        private final Object left;
        private final Object right;
        private long lastTick = Long.MIN_VALUE;
        private BonePhysicsSample lastSample;
        private SideRenderBias previousLeftBias = SideRenderBias.ZERO;
        private SideRenderBias leftBias = SideRenderBias.ZERO;
        private SideRenderBias previousRightBias = SideRenderBias.ZERO;
        private SideRenderBias rightBias = SideRenderBias.ZERO;
        private boolean hasSmoothedTarget;
        private float smoothedTargetX;
        private float smoothedTargetY;
        private float smoothedTargetRotation;

        private AfwPhysicsPair(Object config, Object left, Object right) {
            this.config = config;
            this.left = left;
            this.right = right;
        }

        private void stepRenderBias(GravityBias gravity, boolean uniboob) {
            this.previousLeftBias = this.leftBias;
            this.previousRightBias = this.rightBias;
            float x = AfwWildfireGenderCompat.clamp(gravity.sidePull() * 0.45f, -0.8f, 0.8f);
            float y = AfwWildfireGenderCompat.clamp(gravity.forwardDown() * 0.85f, 0.0f, 1.2f);
            float rotation = AfwWildfireGenderCompat.clamp(gravity.sidePull() * 10.0f + gravity.forwardPull() * 2.0f, -10.0f, 10.0f);
            this.leftBias = new SideRenderBias(x, y, uniboob ? 0.0f : rotation);
            this.rightBias = uniboob ? this.leftBias : this.leftBias.mirrored();
        }

        private TargetImpulse smoothTarget(float x, float y, float rotation) {
            if (!this.hasSmoothedTarget) {
                this.hasSmoothedTarget = true;
                this.smoothedTargetX = x;
                this.smoothedTargetY = y;
                this.smoothedTargetRotation = rotation;
                return new TargetImpulse(x, y, rotation);
            }
            this.smoothedTargetX = MathHelper.lerp((float)0.1f, (float)this.smoothedTargetX, (float)x);
            this.smoothedTargetY = MathHelper.lerp((float)0.1f, (float)this.smoothedTargetY, (float)y);
            this.smoothedTargetRotation = MathHelper.lerp((float)0.1f, (float)this.smoothedTargetRotation, (float)rotation);
            return new TargetImpulse(this.smoothedTargetX, this.smoothedTargetY, this.smoothedTargetRotation);
        }
    }

    private record BonePhysicsSample(float x, float y, float z, float chestX, float chestY, float chestZ, Vec3d forward, Vec3d right, Vec3d up) {
        static BonePhysicsSample from(MatrixStack rootPose, MatrixStack bonePose) {
            Matrix4f relativePose = new Matrix4f((Matrix4fc)rootPose.peek().getPositionMatrix()).invert().mul((Matrix4fc)new Matrix4f((Matrix4fc)bonePose.peek().getPositionMatrix()));
            Vector3f origin = relativePose.transformPosition(0.0f, 0.0f, 0.0f, new Vector3f());
            Vector3f forwardPoint = relativePose.transformPosition(0.0f, 0.0f, -1.0f, new Vector3f());
            Vector3f rightPoint = relativePose.transformPosition(1.0f, 0.0f, 0.0f, new Vector3f());
            Vector3f upPoint = relativePose.transformPosition(0.0f, 1.0f, 0.0f, new Vector3f());
            Vector3f chestPoint = relativePose.transformPosition(0.0f, 0.3f, -0.35f, new Vector3f());
            Vec3d forward = BonePhysicsSample.normalize(new Vec3d((double)(forwardPoint.x() - origin.x()), (double)(forwardPoint.y() - origin.y()), (double)(forwardPoint.z() - origin.z())));
            Vec3d right = BonePhysicsSample.normalize(new Vec3d((double)(rightPoint.x() - origin.x()), (double)(rightPoint.y() - origin.y()), (double)(rightPoint.z() - origin.z())));
            Vec3d up = BonePhysicsSample.normalize(new Vec3d((double)(upPoint.x() - origin.x()), (double)(upPoint.y() - origin.y()), (double)(upPoint.z() - origin.z())));
            if (forward == null || right == null || up == null) {
                return null;
            }
            return new BonePhysicsSample(origin.x(), origin.y(), origin.z(), chestPoint.x(), chestPoint.y(), chestPoint.z(), forward, right, up);
        }

        private static Vec3d normalize(Vec3d vector) {
            if (vector == null || vector.lengthSquared() < 1.0E-8) {
                return null;
            }
            Vec3d normalized = vector.normalize();
            return normalized.isFinite() ? normalized : null;
        }
    }

    private record SideRenderBias(float x, float y, float rotation) {
        private static final SideRenderBias ZERO = new SideRenderBias(0.0f, 0.0f, 0.0f);

        private SideRenderBias mirrored() {
            return new SideRenderBias(-this.x * 0.85f, this.y * 0.95f, -this.rotation * 0.85f);
        }
    }

    private record GravityBias(float sidePull, float forwardPull, float forwardDown, float tiltInfluence) {
        private static final Vec3d MODEL_DOWN = new Vec3d(0.0, -1.0, 0.0);

        static GravityBias from(BonePhysicsSample sample) {
            if (sample == null) {
                return new GravityBias(0.0f, 0.0f, 0.0f, 0.0f);
            }
            float sidePull = (float)sample.right().dotProduct(MODEL_DOWN);
            float forwardPull = (float)sample.forward().dotProduct(MODEL_DOWN);
            float forwardDown = Math.abs(forwardPull);
            float upsideDown = Math.max(0.0f, (float)sample.up().dotProduct(MODEL_DOWN));
            float tiltInfluence = AfwWildfireGenderCompat.clamp(Math.max(Math.abs(sidePull), Math.max(forwardDown, upsideDown)), 0.0f, 1.0f);
            return new GravityBias(sidePull, forwardPull, forwardDown, tiltInfluence);
        }
    }

    private record TargetImpulse(float x, float y, float rotation) {
    }
}

