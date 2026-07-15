/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_310
 *  net.minecraft.class_922
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import java.util.UUID;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_922.class}, priority=1500)
public abstract class LivingEntityRendererAnimationNameTagMixin {
    @Inject(method={"method_4055"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$hideSameAnimationNameLabel(class_1309 entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        if (entity == null) {
            return;
        }
        UUID viewerUuid = LivingEntityRendererAnimationNameTagMixin.afw$getLocalPlayerUuid();
        if (AfwClientAnimationRuntime.shouldHideNameLabelForViewer(viewerUuid, entity.method_5667())) {
            cir.setReturnValue((Object)false);
        }
    }

    private static UUID afw$getLocalPlayerUuid() {
        class_310 client = class_310.method_1551();
        return client == null || client.field_1724 == null ? null : client.field_1724.method_5667();
    }
}

