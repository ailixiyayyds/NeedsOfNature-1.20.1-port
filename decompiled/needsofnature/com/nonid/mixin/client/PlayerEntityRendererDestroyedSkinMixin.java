/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10055
 *  net.minecraft.class_1007
 *  net.minecraft.class_11890
 *  net.minecraft.class_12079$class_10726
 *  net.minecraft.class_12079$class_12081
 *  net.minecraft.class_1309
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_8685
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin.client;

import com.nonid.client.NonDebugSpinMode;
import com.nonid.client.NonDestroyedSkinClient;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_11890;
import net.minecraft.class_12079;
import net.minecraft.class_1309;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1007.class})
public abstract class PlayerEntityRendererDestroyedSkinMixin {
    @Inject(method={"method_62604"}, at={@At(value="TAIL")})
    private void non$applyDestroyedSkin(class_11890 player, class_10055 state, float tickProgress, CallbackInfo ci) {
        if (NonDebugSpinMode.isEnabled()) {
            return;
        }
        class_8685 textures = state.field_53520;
        if (textures == null || textures.comp_1626() == null) {
            return;
        }
        if (!(player instanceof class_1309)) {
            return;
        }
        class_11890 living = player;
        class_2960 replacement = NonDestroyedSkinClient.resolveBaseTexture((class_1309)living, textures.comp_1626().comp_3627());
        if (replacement == null) {
            return;
        }
        state.field_53520 = new class_8685((class_12079.class_12081)new class_12079.class_10726(replacement, replacement), textures.comp_1627(), textures.comp_1628(), textures.comp_1629(), textures.comp_1630());
    }

    @ModifyArg(method={"method_4220"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_1007;method_23205(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_2960;Lnet/minecraft/class_630;Z)V"), index=3)
    private class_2960 non$applyDestroyedSkinToRightArm(class_2960 skinTexture) {
        return PlayerEntityRendererDestroyedSkinMixin.non$resolveLocalHandTexture(skinTexture);
    }

    @ModifyArg(method={"method_4221"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_1007;method_23205(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_2960;Lnet/minecraft/class_630;Z)V"), index=3)
    private class_2960 non$applyDestroyedSkinToLeftArm(class_2960 skinTexture) {
        return PlayerEntityRendererDestroyedSkinMixin.non$resolveLocalHandTexture(skinTexture);
    }

    private static class_2960 non$resolveLocalHandTexture(class_2960 skinTexture) {
        if (NonDebugSpinMode.isEnabled()) {
            return skinTexture;
        }
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return skinTexture;
        }
        class_2960 replacement = NonDestroyedSkinClient.resolveBaseTexture((class_1309)client.field_1724, skinTexture);
        return replacement != null ? replacement : skinTexture;
    }
}

