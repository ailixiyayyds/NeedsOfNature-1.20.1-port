/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1341
 *  net.minecraft.class_1429
 *  net.minecraft.class_3218
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import net.minecraft.class_1297;
import net.minecraft.class_1341;
import net.minecraft.class_1429;
import net.minecraft.class_3218;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1341.class})
public abstract class AnimalMateGoalAnimationBreedingMixin {
    @Final
    @Shadow
    protected class_1429 field_6404;
    @Final
    @Shadow
    protected class_3218 field_6405;
    @Shadow
    @Nullable
    protected class_1429 field_6406;

    @Inject(method={"method_6268"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$startAnimationBreedingWhenClose(CallbackInfo ci) {
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null || !config.useAnimationBreeding()) {
            return;
        }
        class_1429 other = this.field_6406;
        if (other == null || !other.method_5805() || other.method_31481()) {
            return;
        }
        if (!this.field_6404.method_6479() || !other.method_6479()) {
            return;
        }
        if (this.field_6404.method_5858((class_1297)other) >= 9.0) {
            return;
        }
        if (NeedsOfNature.handleAnimationBreedingAttempt(this.field_6405, this.field_6404, other)) {
            ci.cancel();
        }
    }
}

