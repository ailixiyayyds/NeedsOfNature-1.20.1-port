/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  com.afwid.server.AfwServerAnimationController
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.mob.CreeperEntity
 *  net.minecraft.world.World
 *  net.minecraft.util.math.Box
 *  net.minecraft.entity.data.TrackedData
 *  net.minecraft.server.world.ServerWorld
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={CreeperEntity.class})
public abstract class CreeperEntityNoNAnimationMixin {
    @Unique
    private static final double IGNORED_ACTOR_FUSE_RADIUS = 7.0;
    @Shadow
    private int currentFuseTime;
    @Shadow
    private int lastFuseTime;
    @Shadow
    private int fuseTime;
    @Shadow
    private static TrackedData<Integer> FUSE_SPEED;
    @Shadow
    private static TrackedData<Boolean> IGNITED;
    @Unique
    private boolean non$wasAfwAnimationControlled;

    @Inject(method={"tick"}, at={@At(value="HEAD")})
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

    @Inject(method={"getFuseSpeed"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$stopFuseDuringAnimation(CallbackInfoReturnable<Integer> cir) {
        if (!this.non$shouldSuppressFuse()) {
            return;
        }
        this.non$resetFuse();
        cir.setReturnValue(0);
    }

    @Inject(method={"setFuseSpeed"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$stopFuseSpeedDuringAnimation(int fuseSpeed, CallbackInfo ci) {
        if (!this.non$shouldSuppressFuse()) {
            return;
        }
        this.non$resetFuse();
        ci.cancel();
    }

    @Inject(method={"isIgnited"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$ignoreIgnitedDuringAnimation(CallbackInfoReturnable<Boolean> cir) {
        if (!this.non$shouldSuppressFuse()) {
            return;
        }
        this.non$resetFuse();
        cir.setReturnValue(false);
    }

    @Inject(method={"ignite"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$ignoreManualIgnitionDuringAnimation(CallbackInfo ci) {
        if (!this.non$shouldSuppressFuse()) {
            return;
        }
        this.non$resetFuse();
        ci.cancel();
    }

    @Inject(method={"explode"}, at={@At(value="HEAD")}, cancellable=true)
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
        CreeperEntity self = (CreeperEntity)(Object)this;
        World class_19372 = self.getEntityWorld();
        if (!(class_19372 instanceof ServerWorld)) {
            return false;
        }
        ServerWorld world = (ServerWorld)class_19372;
        return AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)self.getUuid());
    }

    @Unique
    private boolean non$hasIgnoredAfwActorNearby() {
        CreeperEntity self = (CreeperEntity)(Object)this;
        World class_19372 = self.getEntityWorld();
        if (!(class_19372 instanceof ServerWorld)) {
            return false;
        }
        ServerWorld world = (ServerWorld)class_19372;
        LivingEntity target = self.getTarget();
        if (target != null && AfwServerAnimationController.shouldIgnoreAttackers((ServerWorld)world, (LivingEntity)target)) {
            self.setTarget(null);
            self.getNavigation().stop();
            return true;
        }
        Box searchBox = self.getBoundingBox().expand(7.0);
        return !world.getEntitiesByClass(LivingEntity.class, searchBox, actor -> actor != self && actor.isAlive() && !actor.isRemoved() && self.squaredDistanceTo((Entity)actor) <= 49.0 && AfwServerAnimationController.shouldIgnoreAttackers((ServerWorld)world, (LivingEntity)actor)).isEmpty();
    }

    @Unique
    private boolean non$isNoNGathering() {
        CreeperEntity self = (CreeperEntity)(Object)this;
        World class_19372 = self.getEntityWorld();
        if (!(class_19372 instanceof ServerWorld)) {
            return false;
        }
        ServerWorld world = (ServerWorld)class_19372;
        return NeedsOfNature.isNoNGatheringActor(world, self.getUuid());
    }

    @Unique
    private void non$resetFuse() {
        CreeperEntity self = (CreeperEntity)(Object)this;
        this.currentFuseTime = 0;
        this.lastFuseTime = 0;
        if (FUSE_SPEED != null) {
            self.getDataTracker().set(FUSE_SPEED, 0);
        }
        if (IGNITED != null) {
            self.getDataTracker().set(IGNITED, false);
        }
    }

    @Unique
    private void non$clampGatherFuse() {
        int maxFuse = Math.max(1, this.fuseTime);
        int safeFuse = Math.max(0, maxFuse - 1);
        this.currentFuseTime = Math.min(Math.max(0, this.currentFuseTime), safeFuse);
        this.lastFuseTime = Math.min(Math.max(0, this.lastFuseTime), this.currentFuseTime);
    }
}

