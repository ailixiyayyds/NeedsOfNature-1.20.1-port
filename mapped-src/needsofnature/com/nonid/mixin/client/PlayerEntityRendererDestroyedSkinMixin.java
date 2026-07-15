/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.PlayerEntityRenderState
 *  net.minecraft.client.render.entity.PlayerEntityRenderer
 *  net.minecraft.entity.PlayerLikeEntity
 *  net.minecraft.util.AssetInfo$TextureAssetInfo
 *  net.minecraft.util.AssetInfo$TextureAsset
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.entity.player.SkinTextures
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin.client;

import com.nonid.client.NonDebugSpinMode;
import com.nonid.client.NonDestroyedSkinClient;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.util.AssetInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerEntityRenderer.class})
public abstract class PlayerEntityRendererDestroyedSkinMixin {
    @Inject(method={"updateRenderState"}, at={@At(value="TAIL")})
    private void non$applyDestroyedSkin(PlayerLikeEntity player, PlayerEntityRenderState state, float tickProgress, CallbackInfo ci) {
        if (NonDebugSpinMode.isEnabled()) {
            return;
        }
        SkinTextures textures = state.skinTextures;
        if (textures == null || textures.comp_1626() == null) {
            return;
        }
        if (!(player instanceof LivingEntity)) {
            return;
        }
        PlayerLikeEntity living = player;
        Identifier replacement = NonDestroyedSkinClient.resolveBaseTexture((LivingEntity)living, textures.comp_1626().comp_3627());
        if (replacement == null) {
            return;
        }
        state.skinTextures = new SkinTextures((AssetInfo.TextureAsset)new AssetInfo.TextureAssetInfo(replacement, replacement), textures.comp_1627(), textures.comp_1628(), textures.comp_1629(), textures.comp_1630());
    }

    @ModifyArg(method={"renderRightArm"}, at=@At(value="INVOKE", target="Lnet/minecraft/PlayerEntityRenderer;renderArm(Lnet/minecraft/MatrixStack;Lnet/minecraft/OrderedRenderCommandQueue;ILnet/minecraft/Identifier;Lnet/minecraft/ModelPart;Z)V"), index=3)
    private Identifier non$applyDestroyedSkinToRightArm(Identifier skinTexture) {
        return PlayerEntityRendererDestroyedSkinMixin.non$resolveLocalHandTexture(skinTexture);
    }

    @ModifyArg(method={"renderLeftArm"}, at=@At(value="INVOKE", target="Lnet/minecraft/PlayerEntityRenderer;renderArm(Lnet/minecraft/MatrixStack;Lnet/minecraft/OrderedRenderCommandQueue;ILnet/minecraft/Identifier;Lnet/minecraft/ModelPart;Z)V"), index=3)
    private Identifier non$applyDestroyedSkinToLeftArm(Identifier skinTexture) {
        return PlayerEntityRendererDestroyedSkinMixin.non$resolveLocalHandTexture(skinTexture);
    }

    private static Identifier non$resolveLocalHandTexture(Identifier skinTexture) {
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

