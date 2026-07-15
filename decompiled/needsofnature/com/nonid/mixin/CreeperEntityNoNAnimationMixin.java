/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  com.afwid.server.AfwServerAnimationController
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1548
 *  net.minecraft.class_1937
 *  net.minecraft.class_238
 *  net.minecraft.class_2940
 *  net.minecraft.class_3218
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin;

import com.afwid.api.AfwAnimationApi;
import com.afwid.server.AfwServerAnimationController;
import com.nonid.NeedsOfNature;
import java.util.UUID;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1548;
import net.minecraft.class_1937;
import net.minecraft.class_238;
import net.minecraft.class_2940;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1548.class})
public abstract class CreeperEntityNoNAnimationMixin {
    @Unique
    private static final double IGNORED_ACTOR_FUSE_RADIUS = 7.0;
    @Shadow
    private int field_7227;
    @Shadow
    private int field_7229;
    @Shadow
    private int field_7228;
    @Shadow
    private static class_2940<Integer> field_7230;
    @Shadow
    private static class_2940<Boolean> field_7231;
    @Unique
    private boolean non$wasAfwAnimationControlled;

    @Inject(method={"method_5773"}, at={@At(value="HEAD")})
    private void non$resetFuseAfterAnimationControlEnds(CallbackInfo ci) {
        boolean controlled = this.non$isAfwAnimationControlled();
        if (this.non$wasAfwAnimationControlled && !controlled) {
            this.non$resetFuse();
        }
        this.non$wasAfwAnimationControlled = controlled;
        if (!controlled && this.non$hasIgnoredAfwActorNearby()) {
            this.non$resetFuse();
        }
    }

    @Inject(method={"method_7007"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$stopFuseDuringAnimation(CallbackInfoReturnable<Integer> cir) {
        if (!this.non$shouldSuppressFuse()) {
            return;
        }
        this.non$resetFuse();
        cir.setReturnValue((Object)0);
    }

    @Inject(method={"method_7005"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$stopFuseSpeedDuringAnimation(int fuseSpeed, CallbackInfo ci) {
        if (!this.non$shouldSuppressFuse()) {
            return;
        }
        this.non$resetFuse();
        ci.cancel();
    }

    @Inject(method={"method_7000"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$ignoreIgnitedDuringAnimation(CallbackInfoReturnable<Boolean> cir) {
        if (!this.non$shouldSuppressFuse()) {
            return;
        }
        this.non$resetFuse();
        cir.setReturnValue((Object)false);
    }

    @Inject(method={"method_7004"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$ignoreManualIgnitionDuringAnimation(CallbackInfo ci) {
        if (!this.non$shouldSuppressFuse()) {
            return;
        }
        this.non$resetFuse();
        ci.cancel();
    }

    @Inject(method={"method_7006"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$blockExplosionDuringGatherOrAnimation(CallbackInfo ci) {
        if (this.non$shouldSuppressFuse()) {
            this.non$resetFuse();
            ci.cancel();
            return;
        }
        if (this.non$isNoNGathering()) {
            this.non$clampGatherFuse();
            ci.cancel();
        }
    }

    @Unique
    private boolean non$shouldSuppressFuse() {
        return this.non$isAfwAnimationControlled() || this.non$hasIgnoredAfwActorNearby();
    }

    @Unique
    private boolean non$isAfwAnimationControlled() {
        class_1548 self = (class_1548)this;
        class_1937 class_19372 = self.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            return false;
        }
        class_3218 world = (class_3218)class_19372;
        return AfwAnimationApi.isActorPendingOrActive((class_3218)world, (UUID)self.method_5667());
    }

    @Unique
    private boolean non$hasIgnoredAfwActorNearby() {
        class_1548 self = (class_1548)this;
        class_1937 class_19372 = self.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            return false;
        }
        class_3218 world = (class_3218)class_19372;
        class_1309 target = self.method_5968();
        if (target != null && AfwServerAnimationController.shouldIgnoreAttackers((class_3218)world, (class_1309)target)) {
            self.method_5980(null);
            self.method_5942().method_6340();
            return true;
        }
        class_238 searchBox = self.method_5829().method_1014(7.0);
        return !world.method_8390(class_1309.class, searchBox, actor -> actor != self && actor.method_5805() && !actor.method_31481() && self.method_5858((class_1297)actor) <= 49.0 && AfwServerAnimationController.shouldIgnoreAttackers((class_3218)world, (class_1309)actor)).isEmpty();
    }

    @Unique
    private boolean non$isNoNGathering() {
        class_1548 self = (class_1548)this;
        class_1937 class_19372 = self.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            return false;
        }
        class_3218 world = (class_3218)class_19372;
        return NeedsOfNature.isNoNGatheringActor(world, self.method_5667());
    }

    @Unique
    private void non$resetFuse() {
        class_1548 self = (class_1548)this;
        this.field_7227 = 0;
        this.field_7229 = 0;
        if (field_7230 != null) {
            self.method_5841().method_12778(field_7230, (Object)0);
        }
        if (field_7231 != null) {
            self.method_5841().method_12778(field_7231, (Object)false);
        }
    }

    @Unique
    private void non$clampGatherFuse() {
        int maxFuse = Math.max(1, this.field_7228);
        int safeFuse = Math.max(0, maxFuse - 1);
        this.field_7227 = Math.min(Math.max(0, this.field_7227), safeFuse);
        this.field_7229 = Math.min(Math.max(0, this.field_7229), this.field_7227);
    }
}

