/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.item.ItemStack
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.item.ItemDisplayContext
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
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
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

    public void submitRenderTask(RenderPassInfo<@NotNull R> renderPassInfo, @NotNull OrderedRenderCommandQueue renderTasks) {
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
            MatrixStack modelStack = new MatrixStack();
            modelStack.peek().copy(renderPassInfo.poseStack().peek());
            for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
                this.renderBoneItems(renderPassInfo, modelStack, bone, boneItems, boneSnapshots, renderTasks);
            }
        });
    }

    private Map<String, AfwGeckoModelEvents.BoneItemProp> getBoneItems(R renderState) {
        Map boneItems = (Map)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.BONE_ITEMS, Map.of());
        return boneItems;
    }

    private void renderBoneItems(RenderPassInfo<@NotNull R> renderPassInfo, MatrixStack poseStack, GeoBone bone, Map<String, AfwGeckoModelEvents.BoneItemProp> boneItems, Map<String, BoneSnapshot> boneSnapshots, OrderedRenderCommandQueue renderTasks) {
        boolean hideChildren;
        boolean hidden;
        poseStack.push();
        RenderUtil.prepMatrixForBone((MatrixStack)poseStack, (GeoBone)bone);
        AfwGeckoModelEvents.BoneItemProp prop = boneItems.get(bone.name());
        BoneSnapshot snapshot = boneSnapshots.get(bone.name());
        boolean bl = hidden = snapshot != null && snapshot.isHidden();
        if (!hidden && prop != null && !prop.stack().isEmpty()) {
            ItemStack stack = prop.stack().copy();
            ItemDisplayContext context = prop.displayContext();
            poseStack.push();
            bone.translateToPivotPoint(poseStack);
            super.submitItemStackRender(poseStack, bone, stack, context, renderPassInfo.renderState(), renderTasks, renderPassInfo.cameraState(), renderPassInfo.packedLight(), renderPassInfo.packedOverlay(), renderPassInfo.renderColor());
            poseStack.pop();
        }
        boolean bl2 = hideChildren = snapshot != null && snapshot.areChildrenHidden();
        if (!hideChildren) {
            for (GeoBone child : bone.children()) {
                this.renderBoneItems(renderPassInfo, poseStack, child, boneItems, boneSnapshots, renderTasks);
            }
        }
        poseStack.pop();
    }

    private void captureBoneSnapshots(GeoBone[] bones, BoneSnapshots snapshots, Map<String, BoneSnapshot> out) {
        for (GeoBone bone : bones) {
            snapshots.get(bone.name()).ifPresent(snapshot -> out.put(bone.name(), (BoneSnapshot)snapshot));
            this.captureBoneSnapshots(bone.children(), snapshots, out);
        }
    }
}

