/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  net.minecraft.class_10799
 *  net.minecraft.class_1293
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_329
 *  net.minecraft.class_332
 *  net.minecraft.class_746
 *  net.minecraft.class_9779
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.nonid.client.NonEnergizedEffectTextures;
import com.nonid.client.NonFilledEffectTextures;
import com.nonid.client.NonHudOverlay;
import com.nonid.client.NonPregnantEffectTextures;
import com.nonid.effect.NonStatusEffects;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.class_10799;
import net.minecraft.class_1293;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_746;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_329.class})
public class InGameHudMixin {
    @Inject(method={"method_1753"}, at={@At(value="TAIL")})
    private void non$renderLiquidOverlay(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.isUiPreviewEnabled()) {
            NonHudOverlay.renderUiPreview(context);
        } else if (NonHudOverlay.shouldHideVanillaHud()) {
            NonHudOverlay.renderOverlay(context);
        } else {
            NonHudOverlay.renderLiquidOverlay(context);
        }
    }

    @Inject(method={"method_55798"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideMiscOverlaysDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_1736"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideCrosshairDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_55805"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideMainHudDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_1765"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideStatusEffectsDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_70837"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideBossBarsDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_55799"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideSleepOverlayDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_1766"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideDemoTimerDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_55803"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideScoreboardDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_55800"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideOverlayMessageDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_55801"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideTitleDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_55802"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideChatDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_55804"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hidePlayerListDuringAnimation(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_70839"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$hideSubtitlesDuringAnimation(class_332 context, boolean defer, CallbackInfo ci) {
        if (NonHudOverlay.shouldHideVanillaHud()) {
            ci.cancel();
        }
    }

    @Redirect(method={"method_1765"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_746;method_6026()Ljava/util/Collection;"))
    private Collection<class_1293> non$hideEnergizedFromHudOverlay(class_746 player) {
        Collection effects = player.method_6026();
        if (effects.isEmpty()) {
            return effects;
        }
        ArrayList<class_1293> filtered = new ArrayList<class_1293>(effects.size());
        for (class_1293 effect : effects) {
            if (effect.method_5579().comp_349() == NonStatusEffects.ENERGIZED) continue;
            filtered.add(effect);
        }
        return filtered;
    }

    @Redirect(method={"method_1765"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_332;method_52707(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/class_2960;IIIII)V"))
    private void non$drawFilledStatusEffectIcon(class_332 context, RenderPipeline pipeline, class_2960 sprite, int x, int y, int width, int height, int color) {
        class_1799 eggStack;
        if (NonFilledEffectTextures.isFilledSprite(sprite)) {
            class_2960 texture = NonFilledEffectTextures.resolveTexture(class_310.method_1551().field_1724);
            context.method_25291(class_10799.field_56883, texture, x, y, 0.0f, 0.0f, width, height, width, height, color);
            return;
        }
        if (NonEnergizedEffectTextures.isEnergizedSprite(sprite)) {
            class_2960 texture = NonEnergizedEffectTextures.resolveTexture(class_310.method_1551().field_1724);
            context.method_25291(class_10799.field_56883, texture, x, y, 0.0f, 0.0f, width, height, width, height, color);
            return;
        }
        if (NonPregnantEffectTextures.isPregnantSprite(sprite) && !(eggStack = NonPregnantEffectTextures.resolvePregnantEggStack(class_310.method_1551().field_1724)).method_7960()) {
            NonPregnantEffectTextures.drawPregnantEggIcon(context, eggStack, x, y, width, height);
            return;
        }
        context.method_52707(pipeline, sprite, x, y, width, height, color);
    }
}

