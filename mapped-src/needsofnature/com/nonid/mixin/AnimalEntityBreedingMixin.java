/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.passive.AnimalEntity
 *  net.minecraft.server.world.ServerWorld
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AnimalEntity.class})
public abstract class AnimalEntityBreedingMixin {
    @Inject(method={"canBreedWith"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$skipInvalidGenderPairWhenSelectingMate(AnimalEntity other, CallbackInfoReturnable<Boolean> cir) {
        boolean validPair;
        if (other == null) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null || !config.useAnimationBreeding() || !config.requireMaleFemaleForBreeding()) {
            return;
        }
        AnimalEntity self = (AnimalEntity)this;
        boolean selfMale = self.getCommandTags().contains("gender.male");
        boolean selfFemale = self.getCommandTags().contains("gender.female");
        boolean otherMale = other.getCommandTags().contains("gender.male");
        boolean otherFemale = other.getCommandTags().contains("gender.female");
        boolean bl = validPair = selfMale && otherFemale || selfFemale && otherMale;
        if (!validPair) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"breed"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$overrideVanillaBreeding(ServerWorld world, AnimalEntity other, CallbackInfo ci) {
        AnimalEntity self = (AnimalEntity)this;
        if (NeedsOfNature.handleAnimationBreedingAttempt(world, self, other)) {
            ci.cancel();
        }
    }
}

