/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resource.language.TranslationStorage
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin.client;

import com.nonid.client.NonStatLabelResolver;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={TranslationStorage.class})
public abstract class TranslationStorageStatLabelMixin {
    @Shadow
    public abstract boolean hasTranslation(String var1);

    @Inject(method={"get"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$resolveDynamicStatLabel(String key, String fallback, CallbackInfoReturnable<String> cir) {
        if (key == null || this.hasTranslation(key)) {
            return;
        }
        String resolved = NonStatLabelResolver.resolve(key);
        if (resolved != null) {
            cir.setReturnValue(resolved);
        }
    }
}

