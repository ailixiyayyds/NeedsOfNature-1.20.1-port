/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.render.WorldRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={WorldRenderer.class})
public class WorldRendererFirstPersonActorMixin {
    @Redirect(method={"fillEntityRenderStates"}, at=@At(value="INVOKE", target="Lnet/minecraft/Camera;isThirdPerson()Z"))
    private boolean afw$keepFocusedActorRenderableInFirstPerson(Camera camera) {
        boolean thirdPerson = camera.isThirdPerson();
        if (thirdPerson) {
            return true;
        }
        Entity class_12972 = camera.getFocusedEntity();
        if (!(class_12972 instanceof PlayerEntity)) {
            return false;
        }
        PlayerEntity player = (PlayerEntity)class_12972;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return false;
        }
        if (!player.getUuid().equals(client.player.getUuid())) {
            return false;
        }
        return AfwClientAnimationRuntime.isActorPendingOrActive(player.getUuid());
    }
}

