/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.damage.DamageSource
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.server.world.ServerWorld
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityDamageMixin {
    @Inject(method={"damage"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$handleDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self.getEntityWorld() instanceof ServerWorld world
                && !AfwServerAnimationController.handleActorDamage(world, self, source)) {
            cir.setReturnValue(false);
        }
    }
}

