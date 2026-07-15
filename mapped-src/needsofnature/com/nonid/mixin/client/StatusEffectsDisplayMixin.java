/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  net.minecraft.client.gl.RenderPipelines
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.nonid.mixin.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.nonid.client.NonEnergizedEffectTextures;
import com.nonid.client.NonFilledEffectTextures;
import com.nonid.client.NonPregnantEffectTextures;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={StatusEffectsDisplay.class})
public class StatusEffectsDisplayMixin {
    @Redirect(method={"drawStatusEffects"}, at=@At(value="INVOKE", target="Lnet/minecraft/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/Identifier;IIII)V"))
    private void non$drawFilledEffectSprite(DrawContext context, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
        ItemStack eggStack;
        if (NonFilledEffectTextures.isFilledSprite(sprite)) {
            Identifier texture = NonFilledEffectTextures.resolveTexture(MinecraftClient.getInstance().player);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 0.0f, 0.0f, width, height, width, height);
            return;
        }
        if (NonEnergizedEffectTextures.isEnergizedSprite(sprite)) {
            Identifier texture = NonEnergizedEffectTextures.resolveTexture(MinecraftClient.getInstance().player);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 0.0f, 0.0f, width, height, width, height);
            return;
        }
        if (NonPregnantEffectTextures.isPregnantSprite(sprite) && !(eggStack = NonPregnantEffectTextures.resolvePregnantEggStack(MinecraftClient.getInstance().player)).isEmpty()) {
            NonPregnantEffectTextures.drawPregnantEggIcon(context, eggStack, x, y, width, height);
            return;
        }
        context.drawGuiTexture(pipeline, sprite, x, y, width, height);
    }
}

