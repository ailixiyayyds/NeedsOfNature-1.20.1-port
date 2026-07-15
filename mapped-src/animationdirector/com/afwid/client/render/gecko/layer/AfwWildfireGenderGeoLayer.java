/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.LivingEntityRenderState
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.client.util.math.MatrixStack
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.cache.model.GeoBone
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.renderer.base.GeoRenderer
 *  software.bernie.geckolib.renderer.base.RenderPassInfo
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 *  software.bernie.geckolib.util.RenderUtil
 */
package com.afwid.client.render.gecko.layer;

import com.afwid.AnimationFramework;
import com.afwid.client.compat.wildfire.AfwWildfireGenderCompat;
import com.afwid.client.compat.wildfire.AfwWildfireGenderState;
import com.afwid.client.render.AfwRenderStateAccess;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import java.util.UUID;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

public final class AfwWildfireGenderGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState>
extends GeoRenderLayer<T, O, R> {
    private static final String BODY_BONE = "body";
    private static final String WAIST_BONE = "waist";
    private static final String TORSO_BONE = "torso";
    private static boolean loggedMissingBodyBone;

    public AfwWildfireGenderGeoLayer(GeoRenderer<@NotNull T, @NotNull O, @NotNull R> renderer) {
        super(renderer);
    }

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull OrderedRenderCommandQueue renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }
        AfwWildfireGenderState state = (AfwWildfireGenderState)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.WILDFIRE_GENDER_STATE);
        if (state == null) {
            return;
        }
        GeoRenderState geoRenderState = renderPassInfo.renderState();
        if (!(geoRenderState instanceof LivingEntityRenderState)) {
            return;
        }
        LivingEntityRenderState livingState = (LivingEntityRenderState)geoRenderState;
        GeoRenderState geoRenderState2 = renderPassInfo.renderState();
        if (!(geoRenderState2 instanceof AfwRenderStateAccess)) {
            return;
        }
        AfwRenderStateAccess access = (AfwRenderStateAccess)geoRenderState2;
        UUID actorUuid = access.afw$getEntityUuid();
        if (actorUuid == null) {
            return;
        }
        renderPassInfo.renderPosed(() -> {
            MatrixStack modelStack = new MatrixStack();
            modelStack.peek().copy(renderPassInfo.poseStack().peek());
            MatrixStack rootStack = new MatrixStack();
            rootStack.peek().copy(renderPassInfo.poseStack().peek());
            MatrixStack bodyStack = this.findBonePose(rootStack, renderPassInfo, BODY_BONE);
            if (bodyStack == null) {
                AfwWildfireGenderGeoLayer.logMissingBodyBone();
                return;
            }
            if (this.submitAtBone(rootStack, bodyStack, modelStack, renderPassInfo, renderTasks, livingState, actorUuid, WAIST_BONE)) {
                return;
            }
            if (this.submitAtBone(rootStack, bodyStack, modelStack, renderPassInfo, renderTasks, livingState, actorUuid, BODY_BONE)) {
                return;
            }
            this.submitAtBone(rootStack, bodyStack, modelStack, renderPassInfo, renderTasks, livingState, actorUuid, TORSO_BONE);
        });
    }

    private boolean submitAtBone(MatrixStack rootPose, MatrixStack physicsPose, MatrixStack poseStack, RenderPassInfo<@NotNull R> renderPassInfo, OrderedRenderCommandQueue renderTasks, LivingEntityRenderState livingState, UUID actorUuid, String targetBone) {
        for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
            if (!this.submitAtBoneRecursive(rootPose, physicsPose, poseStack, bone, renderTasks, livingState, actorUuid, renderPassInfo.packedLight(), targetBone)) continue;
            return true;
        }
        return false;
    }

    private boolean submitAtBoneRecursive(MatrixStack rootPose, MatrixStack physicsPose, MatrixStack poseStack, GeoBone bone, OrderedRenderCommandQueue renderTasks, LivingEntityRenderState livingState, UUID actorUuid, int packedLight, String targetBone) {
        poseStack.push();
        RenderUtil.prepMatrixForBone((MatrixStack)poseStack, (GeoBone)bone);
        boolean submitted = false;
        if (targetBone.equals(bone.name())) {
            AfwWildfireGenderCompat.overrideRenderPhysics(actorUuid, livingState, rootPose, physicsPose);
            this.applyVanillaLayerBridgeCorrection(poseStack, targetBone);
            submitted = AfwWildfireGenderCompat.submitOriginalLayer(livingState, poseStack, renderTasks, packedLight);
        } else {
            for (GeoBone child : bone.children()) {
                if (!this.submitAtBoneRecursive(rootPose, physicsPose, poseStack, child, renderTasks, livingState, actorUuid, packedLight, targetBone)) continue;
                submitted = true;
                break;
            }
        }
        poseStack.pop();
        return submitted;
    }

    private MatrixStack findBonePose(MatrixStack rootPose, RenderPassInfo<@NotNull R> renderPassInfo, String targetBone) {
        MatrixStack searchStack = new MatrixStack();
        searchStack.peek().copy(rootPose.peek());
        for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
            MatrixStack found = this.findBonePoseRecursive(searchStack, bone, targetBone);
            if (found == null) continue;
            return found;
        }
        return null;
    }

    private MatrixStack findBonePoseRecursive(MatrixStack poseStack, GeoBone bone, String targetBone) {
        poseStack.push();
        RenderUtil.prepMatrixForBone((MatrixStack)poseStack, (GeoBone)bone);
        MatrixStack found = null;
        if (targetBone.equals(bone.name())) {
            found = new MatrixStack();
            found.peek().copy(poseStack.peek());
        } else {
            GeoBone child;
            GeoBone[] geoBoneArray = bone.children();
            int n = geoBoneArray.length;
            for (int i = 0; i < n && (found = this.findBonePoseRecursive(poseStack, child = geoBoneArray[i], targetBone)) == null; ++i) {
            }
        }
        poseStack.pop();
        return found;
    }

    private static void logMissingBodyBone() {
        if (loggedMissingBodyBone) {
            return;
        }
        loggedMissingBodyBone = true;
        AnimationFramework.LOGGER.warn("[AFW] Female Gender compatibility requires Gecko player bone '{}'; skipping layer.", (Object)BODY_BONE);
    }

    private void applyVanillaLayerBridgeCorrection(MatrixStack poseStack, String targetBone) {
        if (WAIST_BONE.equals(targetBone)) {
            poseStack.translate(0.0f, 1.5f, 0.0f);
            poseStack.multiply((Quaternionfc)new Quaternionf().rotationZ((float)Math.PI));
        }
    }
}

