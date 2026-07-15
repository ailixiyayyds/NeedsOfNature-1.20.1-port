/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
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

import com.afwid.client.camera.AfwAnimatedCameraPoseTracker;
import com.afwid.client.camera.AfwCameraPosAccess;
import com.afwid.client.render.AfwRenderStateAccess;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import java.util.UUID;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
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

public final class AfwCameraPoseGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState>
extends GeoRenderLayer<T, O, R> {
    private static final Identifier PLAYER_TYPE_ID = Identifier.ofVanilla((String)"player");
    private static final String HEAD_BONE = "head";
    private static final String BODY_BONE = "body";
    private static final float HEAD_CENTER_Y_OFFSET = 0.3f;
    private static final float BODY_CENTER_Y_OFFSET = -0.375f;

    public AfwCameraPoseGeoLayer(GeoRenderer<@NotNull T, @NotNull O, @NotNull R> renderer) {
        super(renderer);
    }

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull OrderedRenderCommandQueue renderTasks) {
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
            MatrixStack modelStack = new MatrixStack();
            modelStack.peek().copy(renderPassInfo.poseStack().peek());
            HeadSample headSample = this.findHeadSample(modelStack, renderPassInfo.model().topLevelBones());
            if (headSample == null) {
                return;
            }
            BodySample bodySample = this.findBodySample(modelStack, renderPassInfo.model().topLevelBones());
            MinecraftClient client = MinecraftClient.getInstance();
            long nowTick = client != null && client.world != null ? client.world.getTime() : 0L;
            UUID instanceId = (UUID)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.AFW_INSTANCE_ID);
            Identifier animationId = (Identifier)renderPassInfo.renderState().getGeckolibData(AfwGeckoTickets.AFW_ANIMATION_ID);
            AfwAnimatedCameraPoseTracker.capture(actorUuid, instanceId, animationId, headSample.worldPos(), bodySample == null ? null : bodySample.worldPos(), headSample.forward(), headSample.up(), headSample.right(), bodySample == null ? null : bodySample.forward(), bodySample == null ? null : bodySample.up(), bodySample == null ? null : bodySample.right(), nowTick);
        });
    }

    private HeadSample findHeadSample(MatrixStack poseStack, GeoBone[] bones) {
        for (GeoBone bone : bones) {
            HeadSample headSample = this.findHeadSample(poseStack, bone);
            if (headSample == null) continue;
            return headSample;
        }
        return null;
    }

    private HeadSample findHeadSample(MatrixStack poseStack, GeoBone bone) {
        poseStack.push();
        RenderUtil.prepMatrixForBone((MatrixStack)poseStack, (GeoBone)bone);
        HeadSample headSample = null;
        if (HEAD_BONE.equalsIgnoreCase(bone.name())) {
            poseStack.push();
            bone.translateToPivotPoint(poseStack);
            Vec3d headPos = this.matrixToWorldPos(poseStack, 0.3f);
            Basis headBasis = this.matrixToBasis(poseStack);
            if (headPos != null) {
                headSample = new HeadSample(headPos, headBasis == null ? null : headBasis.forward(), headBasis == null ? null : headBasis.up(), headBasis == null ? null : headBasis.right());
            }
            poseStack.pop();
        }
        if (headSample == null) {
            GeoBone child;
            GeoBone[] geoBoneArray = bone.children();
            int n = geoBoneArray.length;
            for (int i = 0; i < n && (headSample = this.findHeadSample(poseStack, child = geoBoneArray[i])) == null; ++i) {
            }
        }
        poseStack.pop();
        return headSample;
    }

    private BodySample findBodySample(MatrixStack poseStack, GeoBone[] bones) {
        for (GeoBone bone : bones) {
            BodySample sample = this.findBodySample(poseStack, bone);
            if (sample == null) continue;
            return sample;
        }
        return null;
    }

    private BodySample findBodySample(MatrixStack poseStack, GeoBone bone) {
        poseStack.push();
        RenderUtil.prepMatrixForBone((MatrixStack)poseStack, (GeoBone)bone);
        BodySample bodySample = null;
        if (BODY_BONE.equalsIgnoreCase(bone.name())) {
            poseStack.push();
            bone.translateToPivotPoint(poseStack);
            Vec3d bodyPos = this.matrixToWorldPos(poseStack, -0.375f);
            Basis bodyBasis = this.matrixToBasis(poseStack);
            if (bodyPos != null) {
                bodySample = new BodySample(bodyPos, bodyBasis == null ? null : bodyBasis.forward(), bodyBasis == null ? null : bodyBasis.up(), bodyBasis == null ? null : bodyBasis.right());
            }
            poseStack.pop();
        }
        if (bodySample == null) {
            GeoBone child;
            GeoBone[] geoBoneArray = bone.children();
            int n = geoBoneArray.length;
            for (int i = 0; i < n && (bodySample = this.findBodySample(poseStack, child = geoBoneArray[i])) == null; ++i) {
            }
        }
        poseStack.pop();
        return bodySample;
    }

    private Vec3d matrixToWorldPos(MatrixStack poseStack, float localYOffset) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.gameRenderer == null || client.gameRenderer.getCamera() == null) {
            return null;
        }
        Vector3f relative = poseStack.peek().getPositionMatrix().transformPosition(0.0f, localYOffset, 0.0f, new Vector3f());
        Vec3d cameraWorldPos = ((AfwCameraPosAccess)client.gameRenderer.getCamera()).afw$getPos();
        return new Vec3d(cameraWorldPos.x + (double)relative.x(), cameraWorldPos.y + (double)relative.y(), cameraWorldPos.z + (double)relative.z());
    }

    private Basis matrixToBasis(MatrixStack poseStack) {
        Vector3f origin = poseStack.peek().getPositionMatrix().transformPosition(0.0f, 0.0f, 0.0f, new Vector3f());
        Vec3d forwardHint = this.vectorBetween(origin, poseStack.peek().getPositionMatrix().transformPosition(0.0f, 0.0f, -1.0f, new Vector3f()));
        Vec3d upHint = this.vectorBetween(origin, poseStack.peek().getPositionMatrix().transformPosition(0.0f, 1.0f, 0.0f, new Vector3f()));
        if (forwardHint == null || upHint == null) {
            return null;
        }
        Vec3d forward = this.normalize(forwardHint);
        Vec3d right = this.normalize(forward.crossProduct(upHint));
        if (forward == null || right == null) {
            return null;
        }
        Vec3d up = this.normalize(right.crossProduct(forward));
        if (up == null) {
            return null;
        }
        return new Basis(forward, up, right);
    }

    private Vec3d vectorBetween(Vector3f origin, Vector3f target) {
        if (origin == null || target == null) {
            return null;
        }
        return new Vec3d((double)(target.x() - origin.x()), (double)(target.y() - origin.y()), (double)(target.z() - origin.z()));
    }

    private Vec3d normalize(Vec3d vector) {
        if (vector == null || vector.lengthSquared() < 1.0E-8) {
            return null;
        }
        Vec3d normalized = vector.normalize();
        return normalized.isFinite() ? normalized : null;
    }

    private record HeadSample(Vec3d worldPos, Vec3d forward, Vec3d up, Vec3d right) {
    }

    private record Basis(Vec3d forward, Vec3d up, Vec3d right) {
    }

    private record BodySample(Vec3d worldPos, Vec3d forward, Vec3d up, Vec3d right) {
    }
}

