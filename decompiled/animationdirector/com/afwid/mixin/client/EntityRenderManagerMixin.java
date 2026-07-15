/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10017
 *  net.minecraft.class_10042
 *  net.minecraft.class_11659
 *  net.minecraft.class_12075
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_4587
 *  net.minecraft.class_7923
 *  net.minecraft.class_897
 *  net.minecraft.class_898
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin.client;

import com.afwid.client.render.AfwRenderStateAccess;
import com.afwid.client.render.gecko.AfwGeckoReplacedRender;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import com.afwid.mixin.client.EntityRendererInvoker;
import java.util.UUID;
import net.minecraft.class_10017;
import net.minecraft.class_10042;
import net.minecraft.class_11659;
import net.minecraft.class_12075;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_4587;
import net.minecraft.class_7923;
import net.minecraft.class_897;
import net.minecraft.class_898;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_898.class})
public class EntityRenderManagerMixin {
    @Inject(method={"method_72977"}, at={@At(value="RETURN")})
    private void afw$attachIdentity(class_1297 entity, float tickDelta, CallbackInfoReturnable<class_10017> cir) {
        boolean keepShadow;
        class_10017 state = (class_10017)cir.getReturnValue();
        if (state instanceof AfwRenderStateAccess) {
            AfwRenderStateAccess access = (AfwRenderStateAccess)state;
            access.afw$setEntityUuid(entity.method_5667());
            access.afw$setEntityTypeId(class_7923.field_41177.method_10221((Object)entity.method_5864()));
        }
        if (!(state instanceof class_10042)) {
            return;
        }
        class_10042 livingState = (class_10042)state;
        if (!(entity instanceof class_1309)) {
            return;
        }
        class_1309 living = (class_1309)entity;
        if (!AfwClientAnimationRuntime.hasActiveInstances()) {
            return;
        }
        if (!AfwClientAnimationRuntime.isActorActive(entity.method_5667())) {
            return;
        }
        UUID actorUuid = entity.method_5667();
        UUID anchorUuid = AfwClientAnimationRuntime.findAnchorUuidForActor(actorUuid);
        boolean bl = keepShadow = anchorUuid == null || anchorUuid.equals(actorUuid);
        if (!keepShadow) {
            state.field_61822 = 0.0f;
            state.field_61823.clear();
        }
        AfwGeckoReplacedRender.prepare(living, livingState, tickDelta);
    }

    @Redirect(method={"method_72976"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_897;method_3936(Lnet/minecraft/class_10017;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;Lnet/minecraft/class_12075;)V"))
    private void afw$swapRenderer(class_897<?, ?> vanillaRenderer, class_10017 state, class_4587 matrices, class_11659 queue, class_12075 camera) {
        if (state instanceof class_10042) {
            AfwRenderStateAccess access;
            UUID uuid;
            class_10042 livingState = (class_10042)state;
            if (AfwClientAnimationRuntime.hasActiveInstances() && (uuid = (access = (AfwRenderStateAccess)state).afw$getEntityUuid()) != null && AfwClientAnimationRuntime.isActorActive(uuid) && AfwGeckoReplacedRender.render(livingState, matrices, queue, camera)) {
                return;
            }
        }
        ((EntityRendererInvoker)vanillaRenderer).afw$render(state, matrices, queue, camera);
    }
}

