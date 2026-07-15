/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10017
 *  net.minecraft.class_1297
 *  net.minecraft.class_310
 *  net.minecraft.class_897
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import java.util.UUID;
import net.minecraft.class_10017;
import net.minecraft.class_1297;
import net.minecraft.class_310;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_897.class}, priority=1500)
public abstract class EntityRendererAnimationNameTagMixin {
    @Inject(method={"method_3921"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$hideSameAnimationNameLabel(class_1297 entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        if (entity == null) {
            return;
        }
        UUID viewerUuid = EntityRendererAnimationNameTagMixin.afw$getLocalPlayerUuid();
        if (AfwClientAnimationRuntime.shouldHideNameLabelForViewer(viewerUuid, entity.method_5667())) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"method_62354"}, at={@At(value="TAIL")})
    private void afw$offsetOutsideViewerAnimationNameLabel(class_1297 entity, class_10017 state, float tickDelta, CallbackInfo ci) {
        if (entity == null || state == null || state.field_53338 == null) {
            return;
        }
        UUID viewerUuid = EntityRendererAnimationNameTagMixin.afw$getLocalPlayerUuid();
        double yOffset = AfwClientAnimationRuntime.findOutsideViewerNameLabelYOffset(viewerUuid, entity.method_5667());
        if (yOffset <= 0.0) {
            return;
        }
        state.field_53338 = state.field_53338.method_1031(0.0, yOffset, 0.0);
    }

    private static UUID afw$getLocalPlayerUuid() {
        class_310 client = class_310.method_1551();
        return client == null || client.field_1724 == null ? null : client.field_1724.method_5667();
    }
}

