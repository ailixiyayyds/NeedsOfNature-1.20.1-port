/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_922
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin.client;

import com.nonid.client.NonDebugNameHelper;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_922.class})
public abstract class LivingEntityRendererDebugLabelMixin {
    @Inject(method={"method_4055"}, at={@At(value="HEAD")}, cancellable=true)
    private void needsOfNature$forceDebugLabel(class_1309 entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        if (NonDebugNameHelper.shouldShowDebugLabel((class_1297)entity)) {
            cir.setReturnValue((Object)true);
        }
    }
}

