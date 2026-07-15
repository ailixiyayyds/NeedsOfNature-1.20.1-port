/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.GameRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 */
package com.nonid.mixin.client;

import com.nonid.NeedsOfNatureClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={GameRenderer.class})
public class GameRendererReactiveImpactFovMixin {
    @ModifyArg(method={"renderWorld"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/GameRenderer;getBasicProjectionMatrix(D)Lorg/joml/Matrix4f;", ordinal=0), index=0)
    private double non$applyReactiveImpactWorldFov(double fov) {
        float offset = NeedsOfNatureClient.getReactiveImpactFovOffset(0.0f);
        if (Math.abs(offset) <= 1.0E-4f) {
            return fov;
        }
        return Math.max(5.0, fov + offset);
    }
}

