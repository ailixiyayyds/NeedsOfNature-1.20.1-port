/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11659
 *  net.minecraft.class_243
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
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
import net.minecraft.class_11659;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
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

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull class_11659 renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }
        UUID actorUuid = (UUID)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.ACTOR_UUID);
        if (actorUuid == null) {
            return;
        }
        class_2960 modelId = (class_2960)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.MODEL_ID);
        Map<String, AfwBoneTextureOverrides.ModelLocator> modelLocators = AfwBoneTextureOverrides.getLocators(modelId);
        if (modelLocators == null || modelLocators.isEmpty()) {
            AfwParticleKeyframes.updateLocatorPositions(actorUuid, Map.of());
            return;
        }
        LinkedHashMap<String, class_243> positions = new LinkedHashMap<String, class_243>();
        renderPassInfo.renderPosed(() -> {
            class_4587 modelStack = new class_4587();
            modelStack.method_23760().method_66521(renderPassInfo.poseStack().method_23760());
            for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
                this.captureLocatorPositions(modelStack, bone, modelLocators, positions);
            }
        });
        AfwParticleKeyframes.updateLocatorPositions(actorUuid, positions);
    }

    private void captureLocatorPositions(class_4587 poseStack, GeoBone bone, Map<String, AfwBoneTextureOverrides.ModelLocator> modelLocators, Map<String, class_243> out) {
        poseStack.method_22903();
        RenderUtil.prepMatrixForBone((class_4587)poseStack, (GeoBone)bone);
        for (Map.Entry<String, AfwBoneTextureOverrides.ModelLocator> entry : modelLocators.entrySet()) {
            AfwBoneTextureOverrides.ModelLocator locator = entry.getValue();
            if (locator == null || !bone.name().equals(locator.parentBone())) continue;
            poseStack.method_22903();
            bone.translateToPivotPoint(poseStack);
            poseStack.method_46416((float)((locator.x() - locator.parentPivotX()) / 16.0), (float)((locator.y() - locator.parentPivotY()) / 16.0), (float)((locator.z() - locator.parentPivotZ()) / 16.0));
            class_243 worldPos = this.matrixToWorldPos(poseStack);
            if (worldPos != null && worldPos.method_76470()) {
                out.put(entry.getKey(), worldPos);
            }
            poseStack.method_22909();
        }
        for (GeoBone child : bone.children()) {
            this.captureLocatorPositions(poseStack, child, modelLocators, out);
        }
        poseStack.method_22909();
    }

    private class_243 matrixToWorldPos(class_4587 poseStack) {
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1773 == null || client.field_1773.method_19418() == null) {
            return null;
        }
        class_4184 class_41842 = client.field_1773.method_19418();
        if (!(class_41842 instanceof AfwCameraPosAccess)) {
            return null;
        }
        AfwCameraPosAccess access = (AfwCameraPosAccess)class_41842;
        Vector3f relative = poseStack.method_23760().method_23761().transformPosition(0.0f, 0.0f, 0.0f, new Vector3f());
        class_243 cameraWorldPos = access.afw$getPos();
        return new class_243(cameraWorldPos.field_1352 + (double)relative.x(), cameraWorldPos.field_1351 + (double)relative.y(), cameraWorldPos.field_1350 + (double)relative.z());
    }
}

