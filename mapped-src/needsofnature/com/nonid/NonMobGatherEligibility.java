/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.passive.TameableEntity
 *  net.minecraft.entity.passive.FoxEntity
 *  net.minecraft.entity.passive.CamelEntity
 */
package com.nonid;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.CamelEntity;

public final class NonMobGatherEligibility {
    private NonMobGatherEligibility() {
    }

    public static boolean canAutoGather(MobEntity mob) {
        CamelEntity camel;
        FoxEntity fox;
        TameableEntity tameable;
        if (mob == null || !mob.isAlive() || mob.isRemoved()) {
            return false;
        }
        if (mob.isAiDisabled() || mob.isSleeping()) {
            return false;
        }
        if (mob instanceof TameableEntity && ((tameable = (TameableEntity)mob).isSitting() || tameable.isInSittingPose())) {
            return false;
        }
        if (mob instanceof FoxEntity && ((fox = (FoxEntity)mob).isSitting() || fox.isSleeping())) {
            return false;
        }
        return !(mob instanceof CamelEntity) || !(camel = (CamelEntity)mob).isSitting();
    }
}

