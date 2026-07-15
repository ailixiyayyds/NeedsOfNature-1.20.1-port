/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 *  net.minecraft.server.world.ServerWorld
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class EntityPushAwayMixin {
    @Inject(method={"pushAwayFrom"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$disablePushAwayDuringAnimation(Entity other, CallbackInfo ci) {
        if (other == null) {
            return;
        }
        Entity self = (Entity)this;
        World class_19372 = self.getEntityWorld();
        if (!(class_19372 instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = (ServerWorld)class_19372;
        if (AfwServerAnimationController.isActorPendingOrActive(world, self.getUuid()) || AfwServerAnimationController.isActorPendingOrActive(world, other.getUuid())) {
            ci.cancel();
        }
    }
}

