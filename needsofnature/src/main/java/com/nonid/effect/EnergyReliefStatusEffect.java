/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.entity.effect.StatusEffectCategory
 */
package com.nonid.effect;

import com.nonid.EnergyHolder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.effect.StatusEffectCategory;

public class EnergyReliefStatusEffect
extends StatusEffect {
    private static final double TOTAL_RATIO = 0.5;
    private final Map<UUID, Integer> initialDuration = new ConcurrentHashMap<UUID, Integer>();
    private final Map<UUID, Double> remainder = new ConcurrentHashMap<UUID, Double>();

    public EnergyReliefStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 5219327);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        int initial;
        if (!(entity instanceof EnergyHolder)) {
            return true;
        }
        EnergyHolder holder = (EnergyHolder)entity;
        StatusEffectInstance instance = null;
        for (StatusEffectInstance effect : entity.getStatusEffects()) {
            if (effect.getEffectType() != this) continue;
            instance = effect;
            break;
        }
        if (instance == null) {
            return true;
        }
        UUID id = entity.getUuid();
        int duration = instance.getDuration();
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
        int sub = (int)Math.floor(accum);
        this.remainder.put(id, accum - (double)sub);
        if (sub > 0) {
            holder.setEnergy(Math.max(0, holder.getEnergy() - sub));
        }
        if (duration <= 1) {
            this.remainder.remove(id);
            this.initialDuration.remove(id);
        }
        return true;
    }
}

