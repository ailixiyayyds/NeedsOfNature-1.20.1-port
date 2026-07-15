/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.EntityRenderState
 *  net.minecraft.entity.Entity
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.render.entity.EntityRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityRenderer.class}, priority=1500)
public abstract class EntityRendererAnimationNameTagMixin {
    @Inject(method={"hasLabel"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$hideSameAnimationNameLabel(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity == null) {
            return;
        }
        UUID viewerUuid = EntityRendererAnimationNameTagMixin.afw$getLocalPlayerUuid();
        if (AfwClientAnimationRuntime.shouldHideNameLabelForViewer(viewerUuid, entity.getUuid())) {
            cir.setReturnValue(false);
        }
    }

    private static UUID afw$getLocalPlayerUuid() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client == null || client.player == null ? null : client.player.getUuid();
    }
}

