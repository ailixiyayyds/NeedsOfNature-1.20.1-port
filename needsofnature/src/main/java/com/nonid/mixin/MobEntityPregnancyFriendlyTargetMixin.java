/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.mob.Angerable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.NeedsOfNature;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.Angerable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={MobEntity.class})
public abstract class MobEntityPregnancyFriendlyTargetMixin {
    @Inject(method={"setTarget"}, at={@At(value="HEAD")}, cancellable=true)
    private void non$blockPlayerTargetingForPregnancyFriendly(LivingEntity target, CallbackInfo ci) {
        if (!(target instanceof PlayerEntity)) {
            return;
        }
        MobEntity mob = (MobEntity)(Object)this;
        if (!NeedsOfNature.isPregnancyFriendlyMob(mob)) {
            return;
        }
        ci.cancel();
    }

    @Inject(method={"mobTick"}, at={@At(value="HEAD")})
    private void non$clearPlayerTargetForPregnancyFriendly(CallbackInfo ci) {
        MobEntity mob = (MobEntity)(Object)this;
        if (!NeedsOfNature.isPregnancyFriendlyMob(mob)) {
            return;
        }
        if (mob.getTarget() instanceof PlayerEntity) {
            mob.setTarget(null);
            if (mob instanceof Angerable) {
                Angerable angerable = (Angerable)mob;
                angerable.stopAnger();
            }
        }
    }
}

