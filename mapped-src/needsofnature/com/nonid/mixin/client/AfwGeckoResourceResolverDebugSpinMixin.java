/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.client.render.gecko.AfwGeckoResourceResolver
 *  net.minecraft.util.Identifier
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin.client;

import com.afwid.client.render.gecko.AfwGeckoResourceResolver;
import com.nonid.client.NonDebugSpinMode;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AfwGeckoResourceResolver.class})
public class AfwGeckoResourceResolverDebugSpinMixin {
    @Inject(method={"resolveAnimationResource(Lnet/minecraft/Identifier;Ljava/lang/String;Lnet/minecraft/Identifier;)Lnet/minecraft/Identifier;"}, at={@At(value="HEAD")}, cancellable=true)
    private static void non$overrideAnimationResource(Identifier afwAnimationId, String actorKey, Identifier entityTypeId, CallbackInfoReturnable<Identifier> cir) {
        if (!NonDebugSpinMode.isEnabled()) {
            return;
        }
        cir.setReturnValue((Object)NonDebugSpinMode.DEBUG_SPIN_ANIMATION_RESOURCE);
    }

    @Inject(method={"toAnimationResource(Lnet/minecraft/Identifier;)Lnet/minecraft/Identifier;"}, at={@At(value="HEAD")}, cancellable=true)
    private static void non$overrideFallbackAnimationResource(Identifier afwAnimationId, CallbackInfoReturnable<Identifier> cir) {
        if (!NonDebugSpinMode.isEnabled()) {
            return;
        }
        cir.setReturnValue((Object)NonDebugSpinMode.DEBUG_SPIN_ANIMATION_RESOURCE);
    }
}

