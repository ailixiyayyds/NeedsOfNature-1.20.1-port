/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11659
 *  net.minecraft.class_1799
 *  net.minecraft.class_4587
 *  net.minecraft.class_811
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.state.BoneSnapshot
 *  software.bernie.geckolib.cache.model.BakedGeoModel
 *  software.bernie.geckolib.cache.model.GeoBone
 *  software.bernie.geckolib.renderer.base.BoneSnapshots
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.renderer.base.GeoRenderer
 *  software.bernie.geckolib.renderer.base.RenderPassInfo
 *  software.bernie.geckolib.renderer.layer.builtin.BlockAndItemGeoLayer
 *  software.bernie.geckolib.renderer.layer.builtin.BlockAndItemGeoLayer$RenderData
 *  software.bernie.geckolib.util.RenderUtil
 */
package com.afwid.client.render.gecko.layer;

import com.afwid.api.AfwGeckoModelEvents;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_11659;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_811;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.model.BakedGeoModel;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.renderer.base.BoneSnapshots;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.layer.builtin.BlockAndItemGeoLayer;
import software.bernie.geckolib.util.RenderUtil;

public final class AfwBoneItemGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState>
extends BlockAndItemGeoLayer<T, O, R> {
    public AfwBoneItemGeoLayer(GeoRenderer<@NotNull T, @NotNull O, @NotNull R> renderer) {
        super(renderer);
    }

    @NotNull
    protected @NotNull List<// Could not load outer class - annotation placement on inner may be incorrect
    BlockAndItemGeoLayer.RenderData<@NotNull R>> getRelevantBones(R renderState, @NotNull BakedGeoModel model) {
        return List.of();
    }

    public void addRenderData(T animatable, O relatedObject, R renderState, float partialTick) {
    }

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull class_11659 renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }
        Map<String, AfwGeckoModelEvents.BoneItemProp> boneItems = this.getBoneItems(renderPassInfo.renderState());
        if (boneItems.isEmpty()) {
            return;
        }
        HashMap boneSnapshots = new HashMap();
        renderPassInfo.addBoneUpdater((passInfo, snapshots) -> this.captureBoneSnapshots(passInfo.model().topLevelBones(), snapshots, boneSnapshots));
        renderPassInfo.renderPosed(() -> {
            class_4587 modelStack = new class_4587();
            modelStack.method_23760().method_66521(renderPassInfo.poseStack().method_23760());
            for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
                this.renderBoneItems(renderPassInfo, modelStack, bone, boneItems, boneSnapshots, renderTasks);
            }
        });
    }

    private Map<String, AfwGeckoModelEvents.BoneItemProp> getBoneItems(R renderState) {
        Map boneItems = (Map)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.BONE_ITEMS, Map.of());
        return boneItems;
    }

    private void renderBoneItems(RenderPassInfo<@NotNull R> renderPassInfo, class_4587 poseStack, GeoBone bone, Map<String, AfwGeckoModelEvents.BoneItemProp> boneItems, Map<String, BoneSnapshot> boneSnapshots, class_11659 renderTasks) {
        boolean hideChildren;
        boolean hidden;
        poseStack.method_22903();
        RenderUtil.prepMatrixForBone((class_4587)poseStack, (GeoBone)bone);
        AfwGeckoModelEvents.BoneItemProp prop = boneItems.get(bone.name());
        BoneSnapshot snapshot = boneSnapshots.get(bone.name());
        boolean bl = hidden = snapshot != null && snapshot.isHidden();
        if (!hidden && prop != null && !prop.stack().method_7960()) {
            class_1799 stack = prop.stack().method_7972();
            class_811 context = prop.displayContext();
            poseStack.method_22903();
            bone.translateToPivotPoint(poseStack);
            super.submitItemStackRender(poseStack, bone, stack, context, renderPassInfo.renderState(), renderTasks, renderPassInfo.cameraState(), renderPassInfo.packedLight(), renderPassInfo.packedOverlay(), renderPassInfo.renderColor());
            poseStack.method_22909();
        }
        boolean bl2 = hideChildren = snapshot != null && snapshot.areChildrenHidden();
        if (!hideChildren) {
            for (GeoBone child : bone.children()) {
                this.renderBoneItems(renderPassInfo, poseStack, child, boneItems, boneSnapshots, renderTasks);
            }
        }
        poseStack.method_22909();
    }

    private void captureBoneSnapshots(GeoBone[] bones, BoneSnapshots snapshots, Map<String, BoneSnapshot> out) {
        for (GeoBone bone : bones) {
            snapshots.get(bone.name()).ifPresent(snapshot -> out.put(bone.name(), (BoneSnapshot)snapshot));
            this.captureBoneSnapshots(bone.children(), snapshots, out);
        }
    }
}

