/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.ai.goal.AnimalMateGoal
 *  net.minecraft.entity.passive.AnimalEntity
 *  net.minecraft.server.world.ServerWorld
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={AnimalMateGoal.class})
public abstract class AnimalMateGoalAnimationBreedingMixin {
    @Final
    @Shadow
    protected AnimalEntity animal;
    @Final
    @Shadow
    protected World world;
    @Shadow
    @Nullable
    protected AnimalEntity mate;

    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$startAnimationBreedingWhenClose(CallbackInfo ci) {
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null || !config.useAnimationBreeding()) {
            return;
        }
        AnimalEntity other = this.mate;
        if (other == null || !other.isAlive() || other.isRemoved()) {
            return;
        }
        if (!this.animal.isInLove() || !other.isInLove()) {
            return;
        }
        if (this.animal.squaredDistanceTo((Entity)other) >= 9.0) {
            return;
        }
        if (this.world instanceof ServerWorld serverWorld
                && NeedsOfNature.handleAnimationBreedingAttempt(serverWorld, this.animal, other)) {
            ci.cancel();
        }
    }
}

