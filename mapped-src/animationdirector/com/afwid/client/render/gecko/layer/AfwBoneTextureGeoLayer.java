/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.VertexConsumer
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.state.BoneSnapshot
 *  software.bernie.geckolib.cache.model.GeoBone
 *  software.bernie.geckolib.cache.model.cuboid.CuboidGeoBone
 *  software.bernie.geckolib.cache.model.cuboid.GeoCube
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.renderer.base.GeoRenderer
 *  software.bernie.geckolib.renderer.base.RenderPassInfo
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 *  software.bernie.geckolib.util.RenderUtil
 */
package com.afwid.client.render.gecko.layer;

import com.afwid.client.render.gecko.AfwGeckoTickets;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.cache.model.cuboid.CuboidGeoBone;
import software.bernie.geckolib.cache.model.cuboid.GeoCube;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

public final class AfwBoneTextureGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState>
extends GeoRenderLayer<T, O, R> {
    public AfwBoneTextureGeoLayer(GeoRenderer<@NotNull T, @NotNull O, @NotNull R> renderer) {
        super(renderer);
    }

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull OrderedRenderCommandQueue renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }
        Map<String, Identifier> boneTextures = this.getBoneTextures(renderPassInfo.renderState());
        if (boneTextures.isEmpty()) {
            return;
        }
        LinkedHashMap<Identifier, Set> bonesByTexture = new LinkedHashMap<Identifier, Set>();
        for (Map.Entry<String, Identifier> entry : boneTextures.entrySet()) {
            String boneName = entry.getKey();
            Identifier texture = entry.getValue();
            if (boneName == null || boneName.isBlank() || texture == null) continue;
            bonesByTexture.computeIfAbsent(texture, key -> new LinkedHashSet()).add(boneName);
        }
        if (bonesByTexture.isEmpty()) {
            return;
        }
        int packedOverlay = renderPassInfo.packedOverlay();
        int packedLight = renderPassInfo.packedLight();
        int renderColor = renderPassInfo.renderColor();
        Set<String> explicitlyHiddenBones = this.getExplicitlyHiddenBones(renderPassInfo.renderState());
        for (Map.Entry entry : bonesByTexture.entrySet()) {
            RenderLayer renderLayer;
            Identifier texture = (Identifier)entry.getKey();
            Set boneNames = (Set)entry.getValue();
            if (boneNames == null || boneNames.isEmpty() || (renderLayer = this.renderer.getRenderType(renderPassInfo.renderState(), texture)) == null) continue;
            renderTasks.submitCustom(renderPassInfo.poseStack(), renderLayer, (entryMatrices, vertexConsumer) -> renderPassInfo.renderPosed(() -> {
                MatrixStack modelStack = new MatrixStack();
                modelStack.peek().copy(entryMatrices);
                for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
                    this.renderBoneFiltered(modelStack, bone, vertexConsumer, boneNames, explicitlyHiddenBones, packedOverlay, packedLight, renderColor);
                }
            }));
        }
    }

    private Map<String, Identifier> getBoneTextures(R renderState) {
        Map boneTextures = (Map)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.BONE_TEXTURES, Map.of());
        return boneTextures;
    }

    private void renderBoneFiltered(MatrixStack poseStack, GeoBone bone, VertexConsumer buffer, Set<String> allowedBones, Set<String> explicitlyHiddenBones, int packedOverlay, int packedLight, int renderColor) {
        boolean hideChildren;
        poseStack.push();
        RenderUtil.prepMatrixForBone((MatrixStack)poseStack, (GeoBone)bone);
        BoneSnapshot snapshot = bone.frameSnapshot;
        boolean overrideBone = allowedBones.contains(bone.name());
        boolean explicitlyHidden = explicitlyHiddenBones.contains(bone.name());
        if (overrideBone && !explicitlyHidden && bone instanceof CuboidGeoBone) {
            CuboidGeoBone cuboidBone = (CuboidGeoBone)bone;
            for (GeoBone geoBone : cuboidBone.cubes) {
                poseStack.push();
                this.renderCube((GeoCube)geoBone, poseStack, buffer, packedOverlay, packedLight, renderColor);
                poseStack.pop();
            }
        }
        boolean bl = hideChildren = snapshot != null && snapshot.areChildrenHidden();
        if (!hideChildren) {
            for (GeoBone geoBone : bone.children()) {
                this.renderBoneFiltered(poseStack, geoBone, buffer, allowedBones, explicitlyHiddenBones, packedOverlay, packedLight, renderColor);
            }
        }
        poseStack.pop();
    }

    private Set<String> getExplicitlyHiddenBones(R renderState) {
        Map boneVisibility = (Map)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.BONE_VISIBILITY, Map.of());
        if (boneVisibility.isEmpty()) {
            return Set.of();
        }
        LinkedHashSet<String> out = new LinkedHashSet<String>();
        for (Map.Entry entry : boneVisibility.entrySet()) {
            String boneName = (String)entry.getKey();
            if (boneName == null || boneName.isBlank() || !Boolean.FALSE.equals(entry.getValue())) continue;
            out.add(boneName);
        }
        return out.isEmpty() ? Set.of() : Set.copyOf(out);
    }

    private void renderCube(GeoCube cube, MatrixStack poseStack, VertexConsumer buffer, int packedOverlay, int packedLight, int renderColor) {
        cube.render(poseStack, buffer, packedLight, packedOverlay, renderColor);
    }
}

