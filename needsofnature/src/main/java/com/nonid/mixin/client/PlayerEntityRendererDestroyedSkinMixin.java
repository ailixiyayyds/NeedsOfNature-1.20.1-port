package com.nonid.mixin.client;

import com.nonid.client.NonDebugSpinMode;
import com.nonid.client.NonDestroyedSkinClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererDestroyedSkinMixin {
    @Inject(method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("RETURN"), cancellable = true)
    private void non$applyDestroyedSkin(AbstractClientPlayerEntity player, CallbackInfoReturnable<Identifier> cir) {
        if (NonDebugSpinMode.isEnabled()) {
            return;
        }
        Identifier replacement = NonDestroyedSkinClient.resolveBaseTexture((LivingEntity)player, cir.getReturnValue());
        if (replacement != null) {
            cir.setReturnValue(replacement);
        }
    }

    @Redirect(method = {"renderRightArm", "renderLeftArm"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getSkinTexture()Lnet/minecraft/util/Identifier;"),
            require = 0)
    private Identifier non$applyDestroyedSkinToArm(AbstractClientPlayerEntity player) {
        Identifier skinTexture = player.getSkinTexture();
        if (NonDebugSpinMode.isEnabled()) {
            return skinTexture;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return skinTexture;
        }
        Identifier replacement = NonDestroyedSkinClient.resolveBaseTexture((LivingEntity)client.player, skinTexture);
        return replacement != null ? replacement : skinTexture;
    }
}
