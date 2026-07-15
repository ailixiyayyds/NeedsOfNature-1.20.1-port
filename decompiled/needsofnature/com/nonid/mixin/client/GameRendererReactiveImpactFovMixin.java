/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_757
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 */
package com.nonid.mixin.client;

import com.nonid.NeedsOfNatureClient;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={class_757.class})
public class GameRendererReactiveImpactFovMixin {
    @ModifyArg(method={"method_3188"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_757;method_22973(F)Lorg/joml/Matrix4f;", ordinal=0), index=0)
    private float non$applyReactiveImpactWorldFov(float fov) {
        float offset = NeedsOfNatureClient.getReactiveImpactFovOffset(0.0f);
        if (Math.abs(offset) <= 1.0E-4f) {
            return fov;
        }
        return Math.max(5.0f, fov + offset);
    }
}

