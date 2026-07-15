/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_5354
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.NeedsOfNature;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_5354;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1308.class})
public abstract class MobEntityPregnancyFriendlyTargetMixin {
    @Inject(method={"method_5980"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$blockPlayerTargetingForPregnancyFriendly(class_1309 target, CallbackInfo ci) {
        if (!(target instanceof class_1657)) {
            return;
        }
        class_1308 mob = (class_1308)this;
        if (!NeedsOfNature.isPregnancyFriendlyMob(mob)) {
            return;
        }
        ci.cancel();
    }

    @Inject(method={"method_5958"}, at={@At(value="HEAD")})
    private void non$clearPlayerTargetForPregnancyFriendly(CallbackInfo ci) {
        class_1308 mob = (class_1308)this;
        if (!NeedsOfNature.isPregnancyFriendlyMob(mob)) {
            return;
        }
        if (mob.method_5968() instanceof class_1657) {
            mob.method_5980(null);
            if (mob instanceof class_5354) {
                class_5354 angerable = (class_5354)mob;
                angerable.method_29922();
            }
        }
    }
}

