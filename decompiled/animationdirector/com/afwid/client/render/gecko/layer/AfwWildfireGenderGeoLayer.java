/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10042
 *  net.minecraft.class_11659
 *  net.minecraft.class_4587
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
import net.minecraft.class_10042;
import net.minecraft.class_11659;
import net.minecraft.class_4587;
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

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull class_11659 renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }
        AfwWildfireGenderState state = (AfwWildfireGenderState)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.WILDFIRE_GENDER_STATE);
        if (state == null) {
            return;
        }
        GeoRenderState geoRenderState = renderPassInfo.renderState();
        if (!(geoRenderState instanceof class_10042)) {
            return;
        }
        class_10042 livingState = (class_10042)geoRenderState;
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
            class_4587 modelStack = new class_4587();
            modelStack.method_23760().method_66521(renderPassInfo.poseStack().method_23760());
            class_4587 rootStack = new class_4587();
            rootStack.method_23760().method_66521(renderPassInfo.poseStack().method_23760());
            class_4587 bodyStack = this.findBonePose(rootStack, renderPassInfo, BODY_BONE);
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

    private boolean submitAtBone(class_4587 rootPose, class_4587 physicsPose, class_4587 poseStack, RenderPassInfo<@NotNull R> renderPassInfo, class_11659 renderTasks, class_10042 livingState, UUID actorUuid, String targetBone) {
        for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
            if (!this.submitAtBoneRecursive(rootPose, physicsPose, poseStack, bone, renderTasks, livingState, actorUuid, renderPassInfo.packedLight(), targetBone)) continue;
            return true;
        }
        return false;
    }

    private boolean submitAtBoneRecursive(class_4587 rootPose, class_4587 physicsPose, class_4587 poseStack, GeoBone bone, class_11659 renderTasks, class_10042 livingState, UUID actorUuid, int packedLight, String targetBone) {
        poseStack.method_22903();
        RenderUtil.prepMatrixForBone((class_4587)poseStack, (GeoBone)bone);
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
        poseStack.method_22909();
        return submitted;
    }

    private class_4587 findBonePose(class_4587 rootPose, RenderPassInfo<@NotNull R> renderPassInfo, String targetBone) {
        class_4587 searchStack = new class_4587();
        searchStack.method_23760().method_66521(rootPose.method_23760());
        for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
            class_4587 found = this.findBonePoseRecursive(searchStack, bone, targetBone);
            if (found == null) continue;
            return found;
        }
        return null;
    }

    private class_4587 findBonePoseRecursive(class_4587 poseStack, GeoBone bone, String targetBone) {
        poseStack.method_22903();
        RenderUtil.prepMatrixForBone((class_4587)poseStack, (GeoBone)bone);
        class_4587 found = null;
        if (targetBone.equals(bone.name())) {
            found = new class_4587();
            found.method_23760().method_66521(poseStack.method_23760());
        } else {
            GeoBone child;
            GeoBone[] geoBoneArray = bone.children();
            int n = geoBoneArray.length;
            for (int i = 0; i < n && (found = this.findBonePoseRecursive(poseStack, child = geoBoneArray[i], targetBone)) == null; ++i) {
            }
        }
        poseStack.method_22909();
        return found;
    }

    private static void logMissingBodyBone() {
        if (loggedMissingBodyBone) {
            return;
        }
        loggedMissingBodyBone = true;
        AnimationFramework.LOGGER.warn("[AFW] Female Gender compatibility requires Gecko player bone '{}'; skipping layer.", (Object)BODY_BONE);
    }

    private void applyVanillaLayerBridgeCorrection(class_4587 poseStack, String targetBone) {
        if (WAIST_BONE.equals(targetBone)) {
            poseStack.method_46416(0.0f, 1.5f, 0.0f);
            poseStack.method_22907((Quaternionfc)new Quaternionf().rotationZ((float)Math.PI));
        }
    }
}

