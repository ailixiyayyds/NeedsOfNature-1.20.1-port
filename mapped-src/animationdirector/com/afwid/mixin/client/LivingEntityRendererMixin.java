/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.LivingEntityRenderState
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.registry.Registries
 *  net.minecraft.client.render.entity.LivingEntityRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.render.AfwRenderStateAccess;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.registry.Registries;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {
    @Inject(method={"updateRenderState"}, at={@At(value="TAIL")})
    private void afw$attachUuidAndType(T entity, S state, float tickDelta, CallbackInfo ci) {
        AfwClientAnimationRuntime.LockedOrientation locked;
        AfwRenderStateAccess access = (AfwRenderStateAccess)state;
        access.afw$setEntityUuid(entity.getUuid());
        access.afw$setEntityTypeId(Registries.ENTITY_TYPE.getId((Object)entity.getType()));
        if (AfwClientAnimationRuntime.hasActiveInstances() && (locked = AfwClientAnimationRuntime.getLockedOrientation(entity.getUuid())) != null) {
            ((LivingEntityRenderState)state).bodyYaw = locked.bodyYaw();
            ((LivingEntityRenderState)state).relativeHeadYaw = MathHelper.wrapDegrees((float)(locked.headYaw() - locked.bodyYaw()));
            ((LivingEntityRenderState)state).pitch = locked.pitch();
        }
    }
}

