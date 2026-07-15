/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1429
 *  net.minecraft.class_3218
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import net.minecraft.class_1429;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1429.class})
public abstract class AnimalEntityBreedingMixin {
    @Inject(method={"method_6474"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$skipInvalidGenderPairWhenSelectingMate(class_1429 other, CallbackInfoReturnable<Boolean> cir) {
        boolean validPair;
        if (other == null) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null || !config.useAnimationBreeding() || !config.requireMaleFemaleForBreeding()) {
            return;
        }
        class_1429 self = (class_1429)this;
        boolean selfMale = self.method_5752().contains("gender.male");
        boolean selfFemale = self.method_5752().contains("gender.female");
        boolean otherMale = other.method_5752().contains("gender.male");
        boolean otherFemale = other.method_5752().contains("gender.female");
        boolean bl = validPair = selfMale && otherFemale || selfFemale && otherMale;
        if (!validPair) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"method_24650"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$overrideVanillaBreeding(class_3218 world, class_1429 other, CallbackInfo ci) {
        class_1429 self = (class_1429)this;
        if (NeedsOfNature.handleAnimationBreedingAttempt(world, self, other)) {
            ci.cancel();
        }
    }
}

