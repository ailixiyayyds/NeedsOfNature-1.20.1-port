/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.util.math.MatrixStack
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.cache.model.GeoBone
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.renderer.base.GeoRenderer
 *  software.bernie.geckolib.renderer.base.RenderPassInfo
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 *  software.bernie.geckolib.util.RenderUtil
 */
package com.afwid.client.render.gecko.layer;

import com.afwid.client.camera.AfwCameraPosAccess;
import com.afwid.client.render.gecko.AfwBoneTextureOverrides;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import com.afwid.client.render.gecko.AfwParticleKeyframes;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

public final class AfwParticleKeyframeGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState>
extends GeoRenderLayer<T, O, R> {
    public AfwParticleKeyframeGeoLayer(GeoRenderer<@NotNull T, @NotNull O, @NotNull R> renderer) {
        super(renderer);
    }

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull OrderedRenderCommandQueue renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }
        UUID actorUuid = (UUID)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.ACTOR_UUID);
        if (actorUuid == null) {
            return;
        }
        Identifier modelId = (Identifier)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.MODEL_ID);
        Map<String, AfwBoneTextureOverrides.ModelLocator> modelLocators = AfwBoneTextureOverrides.getLocators(modelId);
        if (modelLocators == null || modelLocators.isEmpty()) {
            AfwParticleKeyframes.updateLocatorPositions(actorUuid, Map.of());
            return;
        }
        LinkedHashMap<String, Vec3d> positions = new LinkedHashMap<String, Vec3d>();
        renderPassInfo.renderPosed(() -> {
            MatrixStack modelStack = new MatrixStack();
            modelStack.peek().copy(renderPassInfo.poseStack().peek());
            for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
                this.captureLocatorPositions(modelStack, bone, modelLocators, positions);
            }
        });
        AfwParticleKeyframes.updateLocatorPositions(actorUuid, positions);
    }

    private void captureLocatorPositions(MatrixStack poseStack, GeoBone bone, Map<String, AfwBoneTextureOverrides.ModelLocator> modelLocators, Map<String, Vec3d> out) {
        poseStack.push();
        RenderUtil.prepMatrixForBone((MatrixStack)poseStack, (GeoBone)bone);
        for (Map.Entry<String, AfwBoneTextureOverrides.ModelLocator> entry : modelLocators.entrySet()) {
            AfwBoneTextureOverrides.ModelLocator locator = entry.getValue();
            if (locator == null || !bone.name().equals(locator.parentBone())) continue;
            poseStack.push();
            bone.translateToPivotPoint(poseStack);
            poseStack.translate((float)((locator.x() - locator.parentPivotX()) / 16.0), (float)((locator.y() - locator.parentPivotY()) / 16.0), (float)((locator.z() - locator.parentPivotZ()) / 16.0));
            Vec3d worldPos = this.matrixToWorldPos(poseStack);
            if (worldPos != null && worldPos.isFinite()) {
                out.put(entry.getKey(), worldPos);
            }
            poseStack.pop();
        }
        for (GeoBone child : bone.children()) {
            this.captureLocatorPositions(poseStack, child, modelLocators, out);
        }
        poseStack.pop();
    }

    private Vec3d matrixToWorldPos(MatrixStack poseStack) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.gameRenderer == null || client.gameRenderer.getCamera() == null) {
            return null;
        }
        Camera class_41842 = client.gameRenderer.getCamera();
        if (!(class_41842 instanceof AfwCameraPosAccess)) {
            return null;
        }
        AfwCameraPosAccess access = (AfwCameraPosAccess)class_41842;
        Vector3f relative = poseStack.peek().getPositionMatrix().transformPosition(0.0f, 0.0f, 0.0f, new Vector3f());
        Vec3d cameraWorldPos = access.afw$getPos();
        return new Vec3d(cameraWorldPos.x + (double)relative.x(), cameraWorldPos.y + (double)relative.y(), cameraWorldPos.z + (double)relative.z());
    }
}

