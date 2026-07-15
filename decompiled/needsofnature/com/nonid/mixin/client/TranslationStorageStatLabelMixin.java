/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1078
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin.client;

import com.nonid.client.NonStatLabelResolver;
import net.minecraft.class_1078;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1078.class})
public abstract class TranslationStorageStatLabelMixin {
    @Shadow
    public abstract boolean method_4678(String var1);

    @Inject(method={"method_4679"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$resolveDynamicStatLabel(String key, String fallback, CallbackInfoReturnable<String> cir) {
        if (key == null || this.method_4678(key)) {
            return;
        }
        String resolved = NonStatLabelResolver.resolve(key);
        if (resolved != null) {
            cir.setReturnValue((Object)resolved);
        }
    }
}

