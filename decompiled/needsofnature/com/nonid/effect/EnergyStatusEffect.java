/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1291
 *  net.minecraft.class_1293
 *  net.minecraft.class_1309
 *  net.minecraft.class_3218
 *  net.minecraft.class_4081
 */
package com.nonid.effect;

import com.nonid.EnergyHolder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1309;
import net.minecraft.class_3218;
import net.minecraft.class_4081;

public class EnergyStatusEffect
extends class_1291 {
    private static final double TOTAL_RATIO = 0.5;
    private final Map<UUID, Integer> initialDuration = new ConcurrentHashMap<UUID, Integer>();
    private final Map<UUID, Double> remainder = new ConcurrentHashMap<UUID, Double>();

    public EnergyStatusEffect() {
        super(class_4081.field_18272, 10494192);
    }

    public boolean method_5552(int duration, int amplifier) {
        return true;
    }

    public boolean method_5572(class_3218 world, class_1309 entity, int amplifier) {
        int initial;
        if (!(entity instanceof EnergyHolder)) {
            return true;
        }
        EnergyHolder holder = (EnergyHolder)entity;
        class_1293 instance = null;
        for (class_1293 effect : entity.method_6026()) {
            if (effect.method_5579().comp_349() != this) continue;
            instance = effect;
            break;
        }
        if (instance == null) {
            return true;
        }
        UUID id = entity.method_5667();
        int duration = instance.method_5584();
        if (duration > (initial = this.initialDuration.getOrDefault(id, duration).intValue())) {
            initial = duration;
        }
        this.initialDuration.put(id, initial);
        int max = holder.getMaxEnergy();
        if (max <= 0) {
            return true;
        }
        double total = (double)max * 0.5 * (double)(amplifier + 1);
        double perTick = total / (double)Math.max(1, initial);
        double accum = this.remainder.getOrDefault(id, 0.0) + perTick;
        int add = (int)Math.floor(accum);
        this.remainder.put(id, accum - (double)add);
        if (add > 0) {
            holder.setEnergy(Math.min(max, holder.getEnergy() + add));
        }
        if (duration <= 1) {
            this.remainder.remove(id);
            this.initialDuration.remove(id);
        }
        return true;
    }
}

