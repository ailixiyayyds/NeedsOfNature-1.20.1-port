/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1792
 *  net.minecraft.class_1792$class_1793
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2394
 *  net.minecraft.class_2398
 *  net.minecraft.class_2400
 *  net.minecraft.class_3218
 *  net.minecraft.class_3414
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 */
package com.nonid.item;

import com.nonid.EnergyHolder;
import com.nonid.particle.NonParticles;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_2400;
import net.minecraft.class_3218;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;

public class EnergyGainAdjustItem
extends class_1792 {
    private final float delta;

    public EnergyGainAdjustItem(class_1792.class_1793 settings, float delta) {
        super(settings);
        this.delta = delta;
    }

    public class_1269 method_7847(class_1799 stack, class_1657 user, class_1309 entity, class_1268 hand) {
        class_1937 class_19372;
        float current;
        float target;
        class_1308 mob;
        if (!(entity instanceof class_1308) || !((mob = (class_1308)entity) instanceof EnergyHolder)) {
            return class_1269.field_5811;
        }
        EnergyHolder holder = (EnergyHolder)mob;
        if (user.method_73183().method_8608()) {
            return class_1269.field_5812;
        }
        boolean removedStabilization = false;
        if (this.delta > 0.0f && mob.method_5752().contains("non.energy_stabilized")) {
            mob.method_5738("non.energy_stabilized");
            removedStabilization = true;
        }
        if (Math.abs((target = EnergyGainAdjustItem.clamp((current = holder.getEnergyGainMultiplier()) + this.delta, 0.4f, 6.0f)) - current) < 1.0E-6f && !removedStabilization) {
            return class_1269.field_5814;
        }
        if (Math.abs(target - current) >= 1.0E-6f) {
            holder.setEnergyGainMultiplier(target);
        }
        if ((class_19372 = user.method_73183()) instanceof class_3218) {
            class_3218 serverWorld = (class_3218)class_19372;
            this.playFeedback(serverWorld, mob, removedStabilization, target);
        }
        if (!user.method_56992()) {
            stack.method_7934(1);
        }
        return class_1269.field_5812;
    }

    private void playFeedback(class_3218 world, class_1308 mob, boolean removedStabilization, float appliedMultiplier) {
        if (removedStabilization) {
            world.method_65096((class_2394)class_2398.field_11249, mob.method_23317(), mob.method_23323(0.6), mob.method_23321(), 12, 0.35, 0.35, 0.35, 0.01);
            world.method_43128(null, mob.method_23317(), mob.method_23318(), mob.method_23321(), class_3417.field_19344, class_3419.field_15254, 0.9f, 0.95f);
            return;
        }
        boolean augmenter = this.delta > 0.0f;
        class_2400 particle = augmenter ? NonParticles.SMALLHEART : class_2398.field_11251;
        class_3414 sound = augmenter ? class_3417.field_14627 : class_3417.field_14978;
        float pitch = EnergyGainAdjustItem.pitchForMultiplier(appliedMultiplier);
        world.method_65096((class_2394)particle, mob.method_23317(), mob.method_23323(0.6), mob.method_23321(), 10, 0.35, 0.35, 0.35, 0.01);
        world.method_43128(null, mob.method_23317(), mob.method_23318(), mob.method_23321(), sound, class_3419.field_15254, 0.9f, pitch);
    }

    private static float pitchForMultiplier(float multiplier) {
        float clamped = EnergyGainAdjustItem.clamp(multiplier, 0.4f, 6.0f);
        float span = 5.6f;
        float t = (clamped - 0.4f) / span;
        return 0.65f + 0.9f * t;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}

