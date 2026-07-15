/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  software.bernie.geckolib.cache.model.GeoBone
 *  software.bernie.geckolib.cache.model.cuboid.CuboidGeoBone
 *  software.bernie.geckolib.cache.model.cuboid.GeoCube
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.renderer.base.RenderPassInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.render.gecko.AfwGeckoTickets;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.cache.model.cuboid.CuboidGeoBone;
import software.bernie.geckolib.cache.model.cuboid.GeoCube;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

@Mixin(value={CuboidGeoBone.class}, remap=false)
public abstract class CuboidGeoBoneHiddenCubeMixin {
    @Shadow
    @Final
    public GeoCube[] cubes;

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private <R extends GeoRenderState> void afw$skipHiddenCubeIndices(RenderPassInfo<R> renderPassInfo, class_4587 poseStack, class_4588 vertexConsumer, int packedLight, int packedOverlay, int renderColor, CallbackInfo ci) {
        if (renderPassInfo == null || renderPassInfo.renderState() == null) {
            return;
        }
        Map hiddenIndicesByBone = (Map)renderPassInfo.renderState().getOrDefaultGeckolibData(AfwGeckoTickets.HIDDEN_BONE_CUBE_INDICES, Map.of());
        if (hiddenIndicesByBone.isEmpty()) {
            return;
        }
        GeoBone bone = (GeoBone)this;
        Set hiddenIndices = (Set)hiddenIndicesByBone.get(bone.name());
        if (hiddenIndices == null || hiddenIndices.isEmpty()) {
            return;
        }
        if (bone.frameSnapshot == null || !bone.frameSnapshot.isHidden()) {
            for (int i = 0; i < this.cubes.length; ++i) {
                GeoCube cube = this.cubes[i];
                if (cube == null || hiddenIndices.contains(i)) continue;
                poseStack.method_22903();
                cube.render(poseStack, vertexConsumer, packedLight, packedOverlay, renderColor);
                poseStack.method_22909();
            }
        }
        ci.cancel();
    }
}

