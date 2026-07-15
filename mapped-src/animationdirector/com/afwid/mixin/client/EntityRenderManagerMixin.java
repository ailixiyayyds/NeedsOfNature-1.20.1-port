/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.EntityRenderState
 *  net.minecraft.client.render.entity.state.LivingEntityRenderState
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.client.render.state.CameraRenderState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.registry.Registries
 *  net.minecraft.client.render.entity.EntityRenderer
 *  net.minecraft.client.render.entity.EntityRenderManager
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
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registries;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityRenderManager.class})
public class EntityRenderManagerMixin {
    @Inject(method={"getAndUpdateRenderState"}, at={@At(value="RETURN")})
    private void afw$attachIdentity(Entity entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
        boolean keepShadow;
        EntityRenderState state = (EntityRenderState)cir.getReturnValue();
        if (state instanceof AfwRenderStateAccess) {
            AfwRenderStateAccess access = (AfwRenderStateAccess)state;
            access.afw$setEntityUuid(entity.getUuid());
            access.afw$setEntityTypeId(Registries.ENTITY_TYPE.getId((Object)entity.getType()));
        }
        if (!(state instanceof LivingEntityRenderState)) {
            return;
        }
        LivingEntityRenderState livingState = (LivingEntityRenderState)state;
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity)entity;
        if (!AfwClientAnimationRuntime.hasActiveInstances()) {
            return;
        }
        if (!AfwClientAnimationRuntime.isActorActive(entity.getUuid())) {
            return;
        }
        UUID actorUuid = entity.getUuid();
        UUID anchorUuid = AfwClientAnimationRuntime.findAnchorUuidForActor(actorUuid);
        boolean bl = keepShadow = anchorUuid == null || anchorUuid.equals(actorUuid);
        if (!keepShadow) {
            state.shadowRadius = 0.0f;
            state.shadowPieces.clear();
        }
        AfwGeckoReplacedRender.prepare(living, livingState, tickDelta);
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/EntityRenderer;render(Lnet/minecraft/EntityRenderState;Lnet/minecraft/MatrixStack;Lnet/minecraft/OrderedRenderCommandQueue;Lnet/minecraft/CameraRenderState;)V"))
    private void afw$swapRenderer(EntityRenderer<?, ?> vanillaRenderer, EntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState camera) {
        if (state instanceof LivingEntityRenderState) {
            AfwRenderStateAccess access;
            UUID uuid;
            LivingEntityRenderState livingState = (LivingEntityRenderState)state;
            if (AfwClientAnimationRuntime.hasActiveInstances() && (uuid = (access = (AfwRenderStateAccess)state).afw$getEntityUuid()) != null && AfwClientAnimationRuntime.isActorActive(uuid) && AfwGeckoReplacedRender.render(livingState, matrices, queue, camera)) {
                return;
            }
        }
        ((EntityRendererInvoker)vanillaRenderer).afw$render(state, matrices, queue, camera);
    }
}

