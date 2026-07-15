package com.nonid.mixin.client;

import com.nonid.client.NonHudOverlay;
import com.nonid.effect.NonStatusEffects;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void non$replaceHudDuringAnimation(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (!NonHudOverlay.shouldHideVanillaHud()) {
            return;
        }
        NonHudOverlay.renderOverlay(context);
        ci.cancel();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void non$renderGameplayOverlay(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (NonHudOverlay.isUiPreviewEnabled()) {
            NonHudOverlay.renderUiPreview(context);
        } else {
            NonHudOverlay.renderLiquidOverlay(context);
        }
    }

    @Redirect(
        method = "renderStatusEffectOverlay",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;getStatusEffects()Ljava/util/Collection;"
        )
    )
    private Collection<StatusEffectInstance> non$hideEnergizedFromHudOverlay(ClientPlayerEntity player) {
        Collection<StatusEffectInstance> effects = player.getStatusEffects();
        if (effects.isEmpty()) {
            return effects;
        }
        ArrayList<StatusEffectInstance> filtered = new ArrayList<>(effects.size());
        for (StatusEffectInstance effect : effects) {
            if (effect.getEffectType() != NonStatusEffects.ENERGIZED) {
                filtered.add(effect);
            }
        }
        return filtered;
    }
}
