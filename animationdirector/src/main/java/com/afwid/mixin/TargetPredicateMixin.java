/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.entity.ai.TargetPredicate
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.ai.TargetPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={TargetPredicate.class})
public abstract class TargetPredicateMixin {
    @Inject(method={"test"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$ignoreAfwActors(LivingEntity tester, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (!(target.getEntityWorld() instanceof ServerWorld world)
                || !AfwServerAnimationController.shouldIgnoreAttackers(world, target)) {
            return;
        }
        cir.setReturnValue(false);
    }
}

