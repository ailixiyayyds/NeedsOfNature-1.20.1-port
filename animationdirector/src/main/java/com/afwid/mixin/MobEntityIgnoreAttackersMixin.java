/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.server.world.ServerWorld
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={MobEntity.class})
public abstract class MobEntityIgnoreAttackersMixin {
    @Inject(method={"tryAttack"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockDirectAttackOnIgnoredActors(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (!(target instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingTarget = (LivingEntity)target;
        if (!(livingTarget.getEntityWorld() instanceof ServerWorld world)
                || !AfwServerAnimationController.shouldIgnoreAttackers(world, livingTarget)) {
            return;
        }
        cir.setReturnValue(false);
    }
}

