/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.client.render.gecko.AfwGeckoResourceResolver
 *  net.minecraft.class_2960
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin.client;

import com.afwid.client.render.gecko.AfwGeckoResourceResolver;
import com.nonid.client.NonDebugSpinMode;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AfwGeckoResourceResolver.class})
public class AfwGeckoResourceResolverDebugSpinMixin {
    @Inject(method={"resolveAnimationResource(Lnet/minecraft/class_2960;Ljava/lang/String;Lnet/minecraft/class_2960;)Lnet/minecraft/class_2960;"}, at={@At(value="HEAD")}, cancellable=true)
    private static void non$overrideAnimationResource(class_2960 afwAnimationId, String actorKey, class_2960 entityTypeId, CallbackInfoReturnable<class_2960> cir) {
        if (!NonDebugSpinMode.isEnabled()) {
            return;
        }
        cir.setReturnValue((Object)NonDebugSpinMode.DEBUG_SPIN_ANIMATION_RESOURCE);
    }

    @Inject(method={"toAnimationResource(Lnet/minecraft/class_2960;)Lnet/minecraft/class_2960;"}, at={@At(value="HEAD")}, cancellable=true)
    private static void non$overrideFallbackAnimationResource(class_2960 afwAnimationId, CallbackInfoReturnable<class_2960> cir) {
        if (!NonDebugSpinMode.isEnabled()) {
            return;
        }
        cir.setReturnValue((Object)NonDebugSpinMode.DEBUG_SPIN_ANIMATION_RESOURCE);
    }
}

