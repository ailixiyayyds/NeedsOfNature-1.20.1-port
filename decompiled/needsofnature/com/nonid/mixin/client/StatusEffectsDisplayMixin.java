/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  net.minecraft.class_10799
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_485
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.nonid.mixin.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.nonid.client.NonEnergizedEffectTextures;
import com.nonid.client.NonFilledEffectTextures;
import com.nonid.client.NonPregnantEffectTextures;
import net.minecraft.class_10799;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_485;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={class_485.class})
public class StatusEffectsDisplayMixin {
    @Redirect(method={"method_2477"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_332;method_52706(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/class_2960;IIII)V"))
    private void non$drawFilledEffectSprite(class_332 context, RenderPipeline pipeline, class_2960 sprite, int x, int y, int width, int height) {
        class_1799 eggStack;
        if (NonFilledEffectTextures.isFilledSprite(sprite)) {
            class_2960 texture = NonFilledEffectTextures.resolveTexture(class_310.method_1551().field_1724);
            context.method_25290(class_10799.field_56883, texture, x, y, 0.0f, 0.0f, width, height, width, height);
            return;
        }
        if (NonEnergizedEffectTextures.isEnergizedSprite(sprite)) {
            class_2960 texture = NonEnergizedEffectTextures.resolveTexture(class_310.method_1551().field_1724);
            context.method_25290(class_10799.field_56883, texture, x, y, 0.0f, 0.0f, width, height, width, height);
            return;
        }
        if (NonPregnantEffectTextures.isPregnantSprite(sprite) && !(eggStack = NonPregnantEffectTextures.resolvePregnantEggStack(class_310.method_1551().field_1724)).method_7960()) {
            NonPregnantEffectTextures.drawPregnantEggIcon(context, eggStack, x, y, width, height);
            return;
        }
        context.method_52706(pipeline, sprite, x, y, width, height);
    }
}

