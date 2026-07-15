/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.client.render.entity.LivingEntityRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin.client;

import com.nonid.client.NonDebugNameHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntityRenderer.class})
public abstract class LivingEntityRendererDebugLabelMixin {
    @Inject(method={"hasLabel"}, at={@At(value="HEAD")}, cancellable=true)
    private void needsOfNature$forceDebugLabel(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (NonDebugNameHelper.shouldShowDebugLabel((Entity)entity)) {
            cir.setReturnValue(true);
        }
    }
}

