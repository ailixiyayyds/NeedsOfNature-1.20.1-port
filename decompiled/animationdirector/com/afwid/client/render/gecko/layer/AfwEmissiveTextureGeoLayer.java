/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11659
 *  net.minecraft.class_12249
 *  net.minecraft.class_1921
 *  net.minecraft.class_2960
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.state.BoneSnapshot
 *  software.bernie.geckolib.cache.model.GeoBone
 *  software.bernie.geckolib.cache.model.cuboid.CuboidGeoBone
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.renderer.base.GeoRenderer
 *  software.bernie.geckolib.renderer.base.RenderPassInfo
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 *  software.bernie.geckolib.util.RenderUtil
 */
package com.afwid.client.render.gecko.layer;

import com.afwid.client.render.gecko.AfwGeckoTickets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_11659;
import net.minecraft.class_12249;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.cache.model.cuboid.CuboidGeoBone;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

public final class AfwEmissiveTextureGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState>
extends GeoRenderLayer<T, O, R> {
    public AfwEmissiveTextureGeoLayer(GeoRenderer<@NotNull T, @NotNull O, @NotNull R> renderer) {
        super(renderer);
    }

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull class_11659 renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }
        List emissiveTextures = (List)renderPassInfo.renderState().getOrDefaultGeckolibData(AfwGeckoTickets.EMISSIVE_TEXTURES, List.of());
        if (emissiveTextures.isEmpty()) {
            return;
        }
        int packedOverlay = renderPassInfo.packedOverlay();
        int renderColor = -1;
        Set<String> customTextureBones = this.getCustomTextureBones(renderPassInfo.renderState());
        Set<String> explicitlyHiddenBones = this.getExplicitlyHiddenBones(renderPassInfo.renderState());
        for (class_2960 texture : emissiveTextures) {
            if (texture == null) continue;
            class_1921 renderLayer = class_12249.method_75996((class_2960)texture);
            renderTasks.method_73483(renderPassInfo.poseStack(), renderLayer, (pose, vertexConsumer) -> renderPassInfo.renderPosed(() -> {
                class_4587 modelStack = new class_4587();
                modelStack.method_23760().method_66521(pose);
                for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
                    this.renderBone(modelStack, bone, vertexConsumer, customTextureBones, explicitlyHiddenBones, packedOverlay, renderColor);
                }
            }));
        }
    }

    private void renderBone(class_4587 poseStack, GeoBone bone, class_4588 buffer, Set<String> customTextureBones, Set<String> explicitlyHiddenBones, int packedOverlay, int renderColor) {
        boolean hideChildren;
        boolean hiddenBySnapshot;
        poseStack.method_22903();
        RenderUtil.prepMatrixForBone((class_4587)poseStack, (GeoBone)bone);
        BoneSnapshot snapshot = bone.frameSnapshot;
        boolean customTextureBone = customTextureBones.contains(bone.name());
        boolean explicitlyHidden = explicitlyHiddenBones.contains(bone.name());
        boolean bl = hiddenBySnapshot = snapshot != null && snapshot.isHidden();
        if (!explicitlyHidden && (!hiddenBySnapshot || customTextureBone) && bone instanceof CuboidGeoBone) {
            CuboidGeoBone cuboidBone = (CuboidGeoBone)bone;
            for (GeoBone geoBone : cuboidBone.cubes) {
                poseStack.method_22903();
                geoBone.render(poseStack, buffer, 0xF000F0, packedOverlay, renderColor);
                poseStack.method_22909();
            }
        }
        boolean bl2 = hideChildren = snapshot != null && snapshot.areChildrenHidden();
        if (!hideChildren) {
            for (GeoBone geoBone : bone.children()) {
                this.renderBone(poseStack, geoBone, buffer, customTextureBones, explicitlyHiddenBones, packedOverlay, renderColor);
            }
        }
        poseStack.method_22909();
    }

    private Set<String> getCustomTextureBones(R renderState) {
        Map boneTextures = (Map)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.BONE_TEXTURES, Map.of());
        if (boneTextures.isEmpty()) {
            return Set.of();
        }
        LinkedHashSet<String> out = new LinkedHashSet<String>();
        for (Map.Entry entry : boneTextures.entrySet()) {
            String boneName = (String)entry.getKey();
            if (boneName == null || boneName.isBlank() || entry.getValue() == null) continue;
            out.add(boneName);
        }
        return out.isEmpty() ? Set.of() : Set.copyOf(out);
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
}

