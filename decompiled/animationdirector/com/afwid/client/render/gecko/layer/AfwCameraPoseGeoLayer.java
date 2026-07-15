/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11659
 *  net.minecraft.class_243
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
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

import com.afwid.client.camera.AfwAnimatedCameraPoseTracker;
import com.afwid.client.camera.AfwCameraPosAccess;
import com.afwid.client.render.AfwRenderStateAccess;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import java.util.UUID;
import net.minecraft.class_11659;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_310;
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

public final class AfwCameraPoseGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState>
extends GeoRenderLayer<T, O, R> {
    private static final class_2960 PLAYER_TYPE_ID = class_2960.method_60656((String)"player");
    private static final String HEAD_BONE = "head";
    private static final String BODY_BONE = "body";
    private static final float HEAD_CENTER_Y_OFFSET = 0.3f;
    private static final float BODY_CENTER_Y_OFFSET = -0.375f;

    public AfwCameraPoseGeoLayer(GeoRenderer<@NotNull T, @NotNull O, @NotNull R> renderer) {
        super(renderer);
    }

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull class_11659 renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }
        GeoRenderState geoRenderState = renderPassInfo.renderState();
        if (!(geoRenderState instanceof AfwRenderStateAccess)) {
            return;
        }
        AfwRenderStateAccess access = (AfwRenderStateAccess)geoRenderState;
        if (!PLAYER_TYPE_ID.equals((Object)access.afw$getEntityTypeId())) {
            return;
        }
        UUID actorUuid = access.afw$getEntityUuid();
        if (actorUuid == null) {
            return;
        }
        renderPassInfo.renderPosed(() -> {
            class_4587 modelStack = new class_4587();
            modelStack.method_23760().method_66521(renderPassInfo.poseStack().method_23760());
            HeadSample headSample = this.findHeadSample(modelStack, renderPassInfo.model().topLevelBones());
            if (headSample == null) {
                return;
            }
            BodySample bodySample = this.findBodySample(modelStack, renderPassInfo.model().topLevelBones());
            class_310 client = class_310.method_1551();
            long nowTick = client != null && client.field_1687 != null ? client.field_1687.method_75260() : 0L;
            UUID instanceId = (UUID)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.AFW_INSTANCE_ID);
            class_2960 animationId = (class_2960)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.AFW_ANIMATION_ID);
            AfwAnimatedCameraPoseTracker.capture(actorUuid, instanceId, animationId, headSample.worldPos(), bodySample == null ? null : bodySample.worldPos(), headSample.forward(), headSample.up(), headSample.right(), bodySample == null ? null : bodySample.forward(), bodySample == null ? null : bodySample.up(), bodySample == null ? null : bodySample.right(), nowTick);
        });
    }

    private HeadSample findHeadSample(class_4587 poseStack, GeoBone[] bones) {
        for (GeoBone bone : bones) {
            HeadSample headSample = this.findHeadSample(poseStack, bone);
            if (headSample == null) continue;
            return headSample;
        }
        return null;
    }

    private HeadSample findHeadSample(class_4587 poseStack, GeoBone bone) {
        poseStack.method_22903();
        RenderUtil.prepMatrixForBone((class_4587)poseStack, (GeoBone)bone);
        HeadSample headSample = null;
        if (HEAD_BONE.equalsIgnoreCase(bone.name())) {
            poseStack.method_22903();
            bone.translateToPivotPoint(poseStack);
            class_243 headPos = this.matrixToWorldPos(poseStack, 0.3f);
            Basis headBasis = this.matrixToBasis(poseStack);
            if (headPos != null) {
                headSample = new HeadSample(headPos, headBasis == null ? null : headBasis.forward(), headBasis == null ? null : headBasis.up(), headBasis == null ? null : headBasis.right());
            }
            poseStack.method_22909();
        }
        if (headSample == null) {
            GeoBone child;
            GeoBone[] geoBoneArray = bone.children();
            int n = geoBoneArray.length;
            for (int i = 0; i < n && (headSample = this.findHeadSample(poseStack, child = geoBoneArray[i])) == null; ++i) {
            }
        }
        poseStack.method_22909();
        return headSample;
    }

    private BodySample findBodySample(class_4587 poseStack, GeoBone[] bones) {
        for (GeoBone bone : bones) {
            BodySample sample = this.findBodySample(poseStack, bone);
            if (sample == null) continue;
            return sample;
        }
        return null;
    }

    private BodySample findBodySample(class_4587 poseStack, GeoBone bone) {
        poseStack.method_22903();
        RenderUtil.prepMatrixForBone((class_4587)poseStack, (GeoBone)bone);
        BodySample bodySample = null;
        if (BODY_BONE.equalsIgnoreCase(bone.name())) {
            poseStack.method_22903();
            bone.translateToPivotPoint(poseStack);
            class_243 bodyPos = this.matrixToWorldPos(poseStack, -0.375f);
            Basis bodyBasis = this.matrixToBasis(poseStack);
            if (bodyPos != null) {
                bodySample = new BodySample(bodyPos, bodyBasis == null ? null : bodyBasis.forward(), bodyBasis == null ? null : bodyBasis.up(), bodyBasis == null ? null : bodyBasis.right());
            }
            poseStack.method_22909();
        }
        if (bodySample == null) {
            GeoBone child;
            GeoBone[] geoBoneArray = bone.children();
            int n = geoBoneArray.length;
            for (int i = 0; i < n && (bodySample = this.findBodySample(poseStack, child = geoBoneArray[i])) == null; ++i) {
            }
        }
        poseStack.method_22909();
        return bodySample;
    }

    private class_243 matrixToWorldPos(class_4587 poseStack, float localYOffset) {
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1773 == null || client.field_1773.method_19418() == null) {
            return null;
        }
        Vector3f relative = poseStack.method_23760().method_23761().transformPosition(0.0f, localYOffset, 0.0f, new Vector3f());
        class_243 cameraWorldPos = ((AfwCameraPosAccess)client.field_1773.method_19418()).afw$getPos();
        return new class_243(cameraWorldPos.field_1352 + (double)relative.x(), cameraWorldPos.field_1351 + (double)relative.y(), cameraWorldPos.field_1350 + (double)relative.z());
    }

    private Basis matrixToBasis(class_4587 poseStack) {
        Vector3f origin = poseStack.method_23760().method_23761().transformPosition(0.0f, 0.0f, 0.0f, new Vector3f());
        class_243 forwardHint = this.vectorBetween(origin, poseStack.method_23760().method_23761().transformPosition(0.0f, 0.0f, -1.0f, new Vector3f()));
        class_243 upHint = this.vectorBetween(origin, poseStack.method_23760().method_23761().transformPosition(0.0f, 1.0f, 0.0f, new Vector3f()));
        if (forwardHint == null || upHint == null) {
            return null;
        }
        class_243 forward = this.normalize(forwardHint);
        class_243 right = this.normalize(forward.method_1036(upHint));
        if (forward == null || right == null) {
            return null;
        }
        class_243 up = this.normalize(right.method_1036(forward));
        if (up == null) {
            return null;
        }
        return new Basis(forward, up, right);
    }

    private class_243 vectorBetween(Vector3f origin, Vector3f target) {
        if (origin == null || target == null) {
            return null;
        }
        return new class_243((double)(target.x() - origin.x()), (double)(target.y() - origin.y()), (double)(target.z() - origin.z()));
    }

    private class_243 normalize(class_243 vector) {
        if (vector == null || vector.method_1027() < 1.0E-8) {
            return null;
        }
        class_243 normalized = vector.method_1029();
        return normalized.method_76470() ? normalized : null;
    }

    private record HeadSample(class_243 worldPos, class_243 forward, class_243 up, class_243 right) {
    }

    private record Basis(class_243 forward, class_243 up, class_243 right) {
    }

    private record BodySample(class_243 worldPos, class_243 forward, class_243 up, class_243 right) {
    }
}

